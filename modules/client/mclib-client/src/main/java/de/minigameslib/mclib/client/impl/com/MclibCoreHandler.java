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

package de.minigameslib.mclib.client.impl.com;

import java.util.ArrayList;
import java.util.List;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Widget;
import de.minigameslib.mclib.client.impl.MclibMod;
import de.minigameslib.mclib.client.impl.gui.TwlScreen;
import de.minigameslib.mclib.client.impl.gui.widgets.FormEditField;
import de.minigameslib.mclib.client.impl.gui.widgets.FormWindow;
import de.minigameslib.mclib.client.impl.gui.widgets.MessageBox;
import de.minigameslib.mclib.pshared.ActionPerformedData;
import de.minigameslib.mclib.pshared.ButtonData;
import de.minigameslib.mclib.pshared.CloseWinData;
import de.minigameslib.mclib.pshared.CoreMessages;
import de.minigameslib.mclib.pshared.DisplayErrorData;
import de.minigameslib.mclib.pshared.DisplayInfoData;
import de.minigameslib.mclib.pshared.DisplayResizableWinData;
import de.minigameslib.mclib.pshared.DisplayYesNoCancelData;
import de.minigameslib.mclib.pshared.DisplayYesNoData;
import de.minigameslib.mclib.pshared.MclibCommunication;
import de.minigameslib.mclib.pshared.PingData;
import de.minigameslib.mclib.pshared.PongData;
import de.minigameslib.mclib.pshared.SendErrorData;
import de.minigameslib.mclib.pshared.WidgetData;
import de.minigameslib.mclib.pshared.WinClosedData;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author mepeisen
 */
public class MclibCoreHandler implements ComHandler
{
    
    @Override
    public void handle(MessageContext context, NetMessage message)
    {
        if (MclibMod.TRACE) System.out.println(message.getData().getValues(true));
        // silently drop invalid messages.
        if (!message.getData().contains("KEY")) return; //$NON-NLS-1$
        
        final String key = message.getData().getString("KEY"); //$NON-NLS-1$
        if (key.equals(CoreMessages.Ping.name()))
        {
            this.answerPing(context, message.getData().getFragment(PingData.class, "data")); //$NON-NLS-1$
        }
        else if (key.equals(CoreMessages.DisplayError.name()))
        {
            this.displayError(context, message.getData().getFragment(DisplayErrorData.class, "data")); //$NON-NLS-1$
        }
        else if (key.equals(CoreMessages.DisplayYesNo.name()))
        {
            this.displayYesNo(context, message.getData().getFragment(DisplayYesNoData.class, "data")); //$NON-NLS-1$
        }
        else if (key.equals(CoreMessages.DisplayYesNoCancel.name()))
        {
            this.displayYesNoCancel(context, message.getData().getFragment(DisplayYesNoCancelData.class, "data")); //$NON-NLS-1$
        }
        else if (key.equals(CoreMessages.DisplayInfo.name()))
        {
            this.displayInfo(context, message.getData().getFragment(DisplayInfoData.class, "data")); //$NON-NLS-1$
        }
        else if (key.equals(CoreMessages.DisplayResizableWin.name()))
        {
            this.displayResizableWin(context, message.getData().getFragment(DisplayResizableWinData.class, "data")); //$NON-NLS-1$
        }
        else if (key.equals(CoreMessages.CloseWin.name()))
        {
            this.closeWin(context, message.getData().getFragment(CloseWinData.class, "data")); //$NON-NLS-1$
        }
        else if (key.equals(CoreMessages.SendError.name()))
        {
            this.sendError(context, message.getData().getFragment(SendErrorData.class, "data")); //$NON-NLS-1$
        }
        // otherwise silently ignore the invalid message.
    }

    /**
     * @param context
     * @param fragment
     */
    private void answerPing(MessageContext context, PingData fragment)
    {
        // send pong to server.
        final PongData answer = new PongData();
        // TODO add extensions
        final DataSection section = new MemoryDataSection();
        section.set("KEY", CoreMessages.Pong.name()); //$NON-NLS-1$
        answer.write(section.createSection("data")); //$NON-NLS-1$
        MclibCommunication.ClientServerCore.send(section);
    }

    /**
     * @param context
     * @param fragment
     */
    private void closeWin(MessageContext context, CloseWinData fragment)
    {
        closeCallback(fragment.getWinId());
    }

    /**
     * @param context
     * @param fragment
     */
    private void sendError(MessageContext context, SendErrorData fragment)
    {
        final Widget widget = getWidget(fragment.getId());
        if (widget != null)
        {
            // TODO
        }
    }

    /**
     * @param context
     * @param fragment
     */
    private void displayResizableWin(MessageContext context, DisplayResizableWinData fragment)
    {
        final String title = fragment.getTitle() == null ? "Error" : fragment.getTitle(); //$NON-NLS-1$
        final List<Button> buttons = new ArrayList<>();
        for (final ButtonData button : fragment.getButtons())
        {
            buttons.add(this.createButton(fragment.getId(), button, "OK")); //$NON-NLS-1$
        }
        
        final FormWindow form = new FormWindow(title, buttons.toArray(new Button[buttons.size()]));
        
        for (final WidgetData wd : fragment.getWidgets())
        {
            if (wd.getLabel() != null)
            {
                // TODO support spans
                form.addRow("col1").addLabel(wd.getLabel().getText()); //$NON-NLS-1$
            }
            else if (wd.getSubmit() != null)
            {
                form.addRow("col1").add(this.createButton(fragment.getId(), wd.getSubmit(), "Submit")); //$NON-NLS-1$ //$NON-NLS-2$
            }
            else if (wd.getCancel() != null)
            {
                form.addRow("col1").add(this.createButton(fragment.getId(), wd.getSubmit(), "Submit")); //$NON-NLS-1$ //$NON-NLS-2$
            }
            else if (wd.getTextInput() != null)
            {
                final FormEditField editField = new FormEditField(wd.getTextInput().getFormKey());
                editField.setText(wd.getTextInput().getValue());
                form.addRow("col1", "col2").addWithLabel(wd.getTextInput().getLabel(), editField); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        
        this.displayWidget(fragment.getId(), form);
    }

    /**
     * @param context
     * @param fragment
     */
    private void displayInfo(MessageContext context, DisplayInfoData fragment)
    {
        final String title = fragment.getTitle() == null ? "Information" : fragment.getTitle(); //$NON-NLS-1$
        final String message = fragment.getMessage() == null ? "" : fragment.getMessage(); //$NON-NLS-1$
        final Button button = createButton(fragment.getId(), fragment.getOkButton(), "OK"); //$NON-NLS-1$
        
        final MessageBox msgbox = new MessageBox(title, message, button);
        this.displayWidget(fragment.getId(), msgbox);
    }

    /**
     * Creates a button with default text
     * @param winId
     * @param buttonData
     * @param defaultLabel
     * @return button widget
     */
    private Button createButton(String winId, ButtonData buttonData, String defaultLabel)
    {
        if (buttonData == null)
        {
            final Button button = new Button(defaultLabel);
            button.addCallback(() -> {
                MclibCoreHandler.closeCallback(winId);
                // send close info to server.
                final WinClosedData answer = new WinClosedData();
                answer.setWinId(winId);
                final DataSection section = new MemoryDataSection();
                section.set("KEY", CoreMessages.WinClosed.name()); //$NON-NLS-1$
                answer.write(section.createSection("data")); //$NON-NLS-1$
                MclibCommunication.ClientServerCore.send(section);
            });
            return button;
        }
        
        final Button button = new Button(buttonData.getLabel() == null ? defaultLabel : buttonData.getLabel());
        button.addCallback(() -> {
            if (buttonData.isCloseAction()) {
                closeCallback(winId);
            }
            if (buttonData.isHasActionListener()) {
                // send action performed to server.
                final ActionPerformedData answer = new ActionPerformedData();
                answer.setWinId(winId);
                answer.setActionId(buttonData.getActionId());
                // TODO FormData
                final DataSection section = new MemoryDataSection();
                section.set("KEY", CoreMessages.ActionPerformed.name()); //$NON-NLS-1$
                answer.write(section.createSection("data")); //$NON-NLS-1$
                MclibCommunication.ClientServerCore.send(section);
            }
        });
        return button;
    }

    /**
     * @param context
     * @param fragment
     */
    private void displayYesNoCancel(MessageContext context, DisplayYesNoCancelData fragment)
    {
        final String title = fragment.getTitle() == null ? "Confirmation" : fragment.getTitle(); //$NON-NLS-1$
        final String message = fragment.getMessage() == null ? "" : fragment.getMessage(); //$NON-NLS-1$
        final Button button1 = createButton(fragment.getId(), fragment.getYesButton(), "Yes"); //$NON-NLS-1$
        final Button button2 = createButton(fragment.getId(), fragment.getNoButton(), "No"); //$NON-NLS-1$
        final Button button3 = createButton(fragment.getId(), fragment.getCancelButton(), "Cancel"); //$NON-NLS-1$
        
        final MessageBox msgbox = new MessageBox(title, message, button1, button2, button3);
        this.displayWidget(fragment.getId(), msgbox);
    }

    /**
     * @param context
     * @param fragment
     */
    private void displayYesNo(MessageContext context, DisplayYesNoData fragment)
    {
        final String title = fragment.getTitle() == null ? "Confirmation" : fragment.getTitle(); //$NON-NLS-1$
        final String message = fragment.getMessage() == null ? "" : fragment.getMessage(); //$NON-NLS-1$
        final Button button1 = createButton(fragment.getId(), fragment.getYesButton(), "Yes"); //$NON-NLS-1$
        final Button button2 = createButton(fragment.getId(), fragment.getNoButton(), "No"); //$NON-NLS-1$
        
        final MessageBox msgbox = new MessageBox(title, message, button1, button2);
        this.displayWidget(fragment.getId(), msgbox);
    }

    /**
     * @param context
     * @param fragment
     */
    private void displayError(MessageContext context, DisplayErrorData fragment)
    {
        final String title = fragment.getTitle() == null ? "Error" : fragment.getTitle(); //$NON-NLS-1$
        final String message = fragment.getMessage() == null ? "" : fragment.getMessage(); //$NON-NLS-1$
        final Button button = createButton(fragment.getId(), fragment.getOkButton(), "OK"); //$NON-NLS-1$
        
        final MessageBox msgbox = new MessageBox(title, message, button);
        this.displayWidget(fragment.getId(), msgbox);
    }
    
    /**
     * Displays given widget
     * @param id
     * @param widget
     */
    private void displayWidget(String id, Widget widget)
    {
        // TODO support multiple widgets
        Minecraft.getMinecraft().displayGuiScreen(new TwlScreen(widget));
    }

    /**
     * @param id
     * @return widget
     */
    private Widget getWidget(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * A callback to close the current widget.
     * @param winId
     */
    private static final void closeCallback(final String winId)
    {
        // TODO support multiple guis... Only reset the gui with associated id.
        Minecraft.getMinecraft().displayGuiScreen(null);
    }
    
}
