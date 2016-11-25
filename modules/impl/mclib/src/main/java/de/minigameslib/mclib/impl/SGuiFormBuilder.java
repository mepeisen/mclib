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
import java.util.stream.Collectors;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.SGuiFormBuilderInterface;
import de.minigameslib.mclib.api.gui.SGuiInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.impl.GuiSessionImpl.GuiButtonImpl;
import de.minigameslib.mclib.impl.GuiSessionImpl.SGuiHelper;
import de.minigameslib.mclib.impl.GuiSessionImpl.SGuiHelper.SGuiImpl;
import de.minigameslib.mclib.pshared.CoreMessages;
import de.minigameslib.mclib.pshared.DisplayResizableWinData;
import de.minigameslib.mclib.pshared.MclibCommunication;
import de.minigameslib.mclib.pshared.SendErrorData;
import de.minigameslib.mclib.pshared.WidgetData;
import de.minigameslib.mclib.pshared.WidgetData.CancelButton;
import de.minigameslib.mclib.pshared.WidgetData.Label;
import de.minigameslib.mclib.pshared.WidgetData.SubmitButton;
import de.minigameslib.mclib.pshared.WidgetData.TextInput;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * A form builder instance.
 * 
 * @author mepeisen
 */
public class SGuiFormBuilder implements SGuiFormBuilderInterface
{

    /** the smart gui. */
    private SGuiHelper smartGui;
    
    /** the display win data. */
    private DisplayResizableWinData data = new DisplayResizableWinData();
    
    /** the window. */
    private SGuiImpl window;

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
    }
    
    /**
     * Encodes given message by using players locale
     * @param msg
     * @param args
     * @return encoded message
     */
    private String encode(LocalizedMessageInterface msg, Serializable[] args)
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

    @Override
    public SGuiFormBuilderInterface addSubmitButton(LocalizedMessageInterface label, Serializable[] labelArgs, McConsumer<DataSection> action)
    {
        try
        {
            final GuiButtonImpl guiButton = (GuiButtonImpl) this.smartGui.getGuiSession().sguiCreateButton(label, labelArgs, (formdata) -> { this.handleSubmit(action, formdata); }, false);
            this.window.registerAction(guiButton);
            final SubmitButton submit = new SubmitButton();
            submit.setCloseAction(false);
            submit.setActionId(guiButton.getActionId());
            submit.setLabel(guiButton.getLabel());
            submit.setCloseAction(false);
            submit.setHasActionListener(true);
            this.data.getButtons().add(submit);
        }
        catch (McException ex)
        {
            // TODO logging, should not happen
        }
        return this;
    }

    /**
     * @param action
     * @param formdata
     */
    private void handleSubmit(McConsumer<DataSection> action, DataSection formdata)
    {
        try
        {
            action.accept(formdata);
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
            MclibCommunication.ClientServerCore.send(section);
        }
    }

    @Override
    public SGuiFormBuilderInterface addCancelButton(LocalizedMessageInterface label, Serializable[] labelArgs, McConsumer<DataSection> action)
    {
        try
        {
            final GuiButtonImpl guiButton = (GuiButtonImpl) this.smartGui.getGuiSession().sguiCreateButton(label, labelArgs, action, false);
            this.window.registerAction(guiButton);
            final CancelButton cancel = new CancelButton();
            cancel.setCloseAction(false);
            cancel.setActionId(guiButton.getActionId());
            cancel.setLabel(guiButton.getLabel());
            cancel.setCloseAction(false);
            cancel.setHasActionListener(true);
            this.data.getButtons().add(cancel);
        }
        catch (McException ex)
        {
            // TODO logging, should not happen
        }
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
        MclibCommunication.ClientServerCore.send(section);
        return this.window;
    }
    
}
