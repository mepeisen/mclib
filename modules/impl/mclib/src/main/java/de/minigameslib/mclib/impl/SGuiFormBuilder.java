/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-signed commercial license from minigames.de
    you are not allowed to use this software in any way except using
    GPL (see below).

------

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package de.minigameslib.mclib.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.SGuiFormBuilderInterface;
import de.minigameslib.mclib.api.gui.SGuiInterface;
import de.minigameslib.mclib.api.gui.SGuiListBuilderInterface;
import de.minigameslib.mclib.api.gui.SGuiListSupplier;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.util.function.McBiConsumer;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.impl.GuiSessionImpl.GuiButtonImpl;
import de.minigameslib.mclib.impl.GuiSessionImpl.SGuiHelper;
import de.minigameslib.mclib.impl.GuiSessionImpl.SGuiHelper.SGuiImpl;
import de.minigameslib.mclib.pshared.ButtonData;
import de.minigameslib.mclib.pshared.CoreMessages;
import de.minigameslib.mclib.pshared.DisplayResizableWinData;
import de.minigameslib.mclib.pshared.MclibCommunication;
import de.minigameslib.mclib.pshared.QueryFormAnswerData;
import de.minigameslib.mclib.pshared.QueryFormRequestData;
import de.minigameslib.mclib.pshared.SendErrorData;
import de.minigameslib.mclib.pshared.WidgetData;
import de.minigameslib.mclib.pshared.WidgetData.ComboInput;
import de.minigameslib.mclib.pshared.WidgetData.ComboValue;
import de.minigameslib.mclib.pshared.WidgetData.Label;
import de.minigameslib.mclib.pshared.WidgetData.ListButton;
import de.minigameslib.mclib.pshared.WidgetData.ListColumn;
import de.minigameslib.mclib.pshared.WidgetData.ListInput;
import de.minigameslib.mclib.pshared.WidgetData.ListRequest;
import de.minigameslib.mclib.pshared.WidgetData.ListResponse;
import de.minigameslib.mclib.pshared.WidgetData.ListResponseRow;
import de.minigameslib.mclib.pshared.WidgetData.TextInput;
import de.minigameslib.mclib.shared.api.com.DataFragment;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * A form builder instance.
 * 
 * @author mepeisen
 */
public class SGuiFormBuilder implements SGuiFormBuilderInterface
{
    
    /** logging. */
    private static final Logger LOGGER = Logger.getLogger(SGuiFormBuilder.class.getName());

    /** the smart gui. */
    private SGuiHelper smartGui;
    
    /** the display win data. */
    private DisplayResizableWinData data = new DisplayResizableWinData();
    
    /** the window. */
    SGuiImpl window;

    /**
     * Constructor
     * @param closeAction 
     * @param closable 
     * @param titleArgs 
     * @param title 
     * @param smartGui
     */
    public SGuiFormBuilder(LocalizedMessageInterface title, Serializable[] titleArgs, boolean closable, McRunnable closeAction, SGuiHelper smartGui)
    {
        this.smartGui = smartGui;
        
        this.data.setClosable(closable);
        this.data.setTitle(this.encode(title, titleArgs));
        
        this.window = this.smartGui.create(closeAction);
        
        this.data.setId(this.window.getUuid());
    }
    
    /**
     * Encodes given message by using players locale
     * @param msg
     * @param args
     * @return encoded message
     */
    String encode(LocalizedMessageInterface msg, Serializable[] args)
    {
        return Arrays.asList(this.smartGui.encode(msg, args)).stream().collect(Collectors.joining("\n")); //$NON-NLS-1$
    }

    @Override
    public SGuiFormBuilderInterface addText(int span, LocalizedMessageInterface text, Serializable... args)
    {
        final WidgetData widget = new WidgetData();
        final Label label = new Label();
        label.setSpan(span);
        label.setText(this.encode(text, args));
        widget.setLabel(label);
        this.data.getWidgets().add(widget);
        return this;
    }
    
    /**
     * Creates a new gui button.
     * @param label
     * @param labelArgs
     * @param action
     * @param closeAction
     * @return gui button
     */
    GuiButtonImpl sguiCreateButton(LocalizedMessageInterface label, Serializable labelArgs[], McBiConsumer<SGuiInterface, DataSection> action, boolean closeAction)
    {
        try
        {
            return (GuiButtonImpl) this.smartGui.getGuiSession().sguiCreateButton(label, labelArgs, action, false);
        }
        catch (McException ex)
        {
            LOGGER.log(Level.WARNING, "Exception creating button", ex); //$NON-NLS-1$
        }
        return null;
    }

    @Override
    public SGuiFormBuilderInterface addSubmitButton(LocalizedMessageInterface label, Serializable[] labelArgs, McBiConsumer<SGuiInterface, DataSection> action)
    {
        final GuiButtonImpl guiButton = this.sguiCreateButton(label, labelArgs, (gui, formdata) -> { this.handleSubmit(action, gui, formdata); }, false);
        this.window.registerAction(guiButton);
        final ButtonData submit = new ButtonData();
        submit.setActionId(guiButton.getActionId());
        submit.setLabel(guiButton.getLabel());
        submit.setCloseAction(false);
        submit.setHasActionListener(true);
        this.data.getButtons().add(submit);
        return this;
    }

    /**
     * @param action
     * @param gui 
     * @param formdata
     */
    private void handleSubmit(McBiConsumer<SGuiInterface, DataSection> action, SGuiInterface gui, DataSection formdata)
    {
        try
        {
            action.accept(gui, formdata);
            this.window.close();
        }
        catch (McException ex)
        {
            // veto, send error message to client
            final SendErrorData error = new SendErrorData();
            error.setId(this.window.getUuid());
            error.setMessage(this.encode(ex.getErrorMessage(), ex.getArgs()));
            final DataSection section = new MemoryDataSection();
            section.set("KEY", CoreMessages.SendError.name()); //$NON-NLS-1$
            error.write(section.createSection("data")); //$NON-NLS-1$
            this.smartGui.getPlayer().sendToClient(MclibCommunication.ClientServerCore, section);
        }
    }

    @Override
    public SGuiFormBuilderInterface addCancelButton(LocalizedMessageInterface label, Serializable[] labelArgs, McBiConsumer<SGuiInterface, DataSection> action)
    {

        final GuiButtonImpl guiButton = this.sguiCreateButton(label, labelArgs, action, false);
        this.window.registerAction(guiButton);
        final ButtonData cancel = new ButtonData();
        cancel.setActionId(guiButton.getActionId());
        cancel.setLabel(guiButton.getLabel());
        cancel.setCloseAction(false);
        cancel.setHasActionListener(true);
        this.data.getButtons().add(cancel);
        return this;
    }

    @Override
    public SGuiFormBuilderInterface addTextInput(LocalizedMessageInterface label, Serializable[] labelArgs, String formKey, String initialValue, boolean allowsEmpty)
    {
        final WidgetData widget = new WidgetData();
        final TextInput input = new TextInput();
        input.setAllowsEmpty(allowsEmpty);
        input.setFormKey(formKey);
        input.setValue(initialValue);
        input.setLabel(this.encode(label, labelArgs));
        widget.setTextInput(input);
        this.data.getWidgets().add(widget);
        return this;
    }

    @Override
    public SGuiInterface display()
    {
        final DataSection section = new MemoryDataSection();
        section.set("KEY", CoreMessages.DisplayResizableWin.name()); //$NON-NLS-1$
        this.data.write(section.createSection("data")); //$NON-NLS-1$
        this.smartGui.getPlayer().sendToClient(MclibCommunication.ClientServerCore, section);
        return this.window;
    }

    @Override
    public SGuiListBuilderInterface addList(SGuiListSupplier supplier)
    {
        final String uuid = this.window.registerDataSupplier(request -> this.parseListRequest(request, supplier));
        final WidgetData widget = new WidgetData();
        widget.setListInput(new ListInput());
        widget.getListInput().setDataId(uuid);
        this.data.getWidgets().add(widget);
        return new SGuiListBuilderImpl(this, widget.getListInput());
    }
    
    /**
     * Parses and performs a list request
     * @param request
     * @param supplier
     * @return answer
     * @throws McException 
     */
    private QueryFormAnswerData parseListRequest(QueryFormRequestData request, SGuiListSupplier supplier) throws McException
    {
        final ListRequest lrq = new ListRequest();
        lrq.read(request.getData());
        
        final ListResponse lresp = new ListResponse();
        lresp.setCount(supplier.count());
        for (final DataFragment df : supplier.select(lrq.getStart(), lrq.getLimit()))
        {
            final ListResponseRow row = new ListResponseRow();
            df.write(row.getData());
            lresp.getRows().add(row);
        }
        final QueryFormAnswerData answer = new QueryFormAnswerData();
        answer.setWinId(request.getWinId());
        answer.setInputId(request.getInputId());
        lresp.write(answer.getData());
        return answer;
    }

    /**
     * Helper to build a list control
     * @author mepeisen
     */
    private static final class SGuiListBuilderImpl implements SGuiListBuilderInterface
    {

        /** the list widget. */
        private ListInput listInput;
        
        /** the underlying formbuilder. */
        private SGuiFormBuilder form;

        /**
         * Constructor to create a list control builder.
         * @param form
         * @param listInput
         */
        public SGuiListBuilderImpl(SGuiFormBuilder form, ListInput listInput)
        {
            this.listInput = listInput;
            this.form = form;
        }

        @Override
        public SGuiListBuilderInterface addColumn(String dataKey, String formKey, LocalizedMessageInterface label, Serializable... args)
        {
            final ListColumn column = new ListColumn();
            column.setDataKey(dataKey);
            column.setFormKey(formKey);
            column.setLabel(this.form.encode(label, args));
            column.setVisible(true);
            this.listInput.getColumns().add(column);
            return this;
        }

        @Override
        public SGuiListBuilderInterface addColumn(String dataKey, String formKey)
        {
            final ListColumn column = new ListColumn();
            column.setDataKey(dataKey);
            column.setFormKey(formKey);
            column.setVisible(false);
            this.listInput.getColumns().add(column);
            return this;
        }

        @Override
        public SGuiListBuilderInterface addSubmitButton(LocalizedMessageInterface label, Serializable[] labelArgs, McBiConsumer<SGuiInterface, DataSection> action)
        {
            final GuiButtonImpl guiButton = this.form.sguiCreateButton(label, labelArgs, action, false);
            this.form.window.registerAction(guiButton);
            final ListButton button = new ListButton();
            button.setActionId(guiButton.getActionId());
            button.setCloseAction(guiButton.isCloseAction());
            button.setHasActionListener(true);
            button.setLabel(guiButton.getLabel());
            button.setNeedsInput(true);
            this.listInput.getButtons().add(button);
            return this;
        }

        @Override
        public SGuiListBuilderInterface addButton(LocalizedMessageInterface label, Serializable[] labelArgs, McBiConsumer<SGuiInterface, DataSection> action)
        {
            final GuiButtonImpl guiButton = this.form.sguiCreateButton(label, labelArgs, action, false);
            this.form.window.registerAction(guiButton);
            final ListButton button = new ListButton();
            button.setActionId(guiButton.getActionId());
            button.setCloseAction(guiButton.isCloseAction());
            button.setHasActionListener(true);
            button.setLabel(guiButton.getLabel());
            button.setNeedsInput(false);
            this.listInput.getButtons().add(button);
            return this;
        }
        
    }

    @Override
    public SGuiFormBuilderInterface addCombo(LocalizedMessageInterface label, Serializable[] labelArgs, boolean allowsNull, String idKey, String labelKey, String formKey, String value,
            List<DataSection> values)
    {
        final ComboInput input = new ComboInput();
        input.setAllowsNewValues(false);
        input.setAllowsNull(allowsNull);
        input.setFormKey(formKey);
        input.setIdKey(idKey);
        input.setLabel(this.encode(label, labelArgs));
        input.setLabelKey(labelKey);
        input.setValue(value);
        for (final DataSection cvdata : values)
        {
            final ComboValue cv = new ComboValue();
            cv.setData(cvdata);
            input.getValues().add(cv);
        }
        final WidgetData w = new WidgetData();
        w.setComboInput(input);
        this.data.getWidgets().add(w);
        return this;
    }

    @Override
    public SGuiFormBuilderInterface addCombo(LocalizedMessageInterface label, Serializable[] labelArgs, boolean allowsNull, String idKey, String labelKey, String formKey, String value,
            List<DataSection> values, String newValueKey)
    {
        final ComboInput input = new ComboInput();
        input.setAllowsNewValues(true);
        input.setAllowsNull(allowsNull);
        input.setFormKey(formKey);
        input.setNformKey(newValueKey);
        input.setIdKey(idKey);
        input.setLabel(this.encode(label, labelArgs));
        input.setLabelKey(labelKey);
        input.setValue(value);
        for (final DataSection cvdata : values)
        {
            final ComboValue cv = new ComboValue();
            cv.setData(cvdata);
            input.getValues().add(cv);
        }
        final WidgetData w = new WidgetData();
        w.setComboInput(input);
        this.data.getWidgets().add(w);
        return this;
    }
    
}
