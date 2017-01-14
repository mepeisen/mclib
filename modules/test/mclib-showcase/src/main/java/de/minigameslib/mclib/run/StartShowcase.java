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

package de.minigameslib.mclib.run;

import java.time.LocalDateTime;

import de.minigameslib.mclib.MclibShowcasePlugin;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.McPlayerJoinEvent;
import de.minigameslib.mclib.api.gui.RawMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mclib.locale.Messages;

/**
 * Handler to display "start showcase".
 * 
 * @author mepeisen
 */
public class StartShowcase implements McListener
{
    
    /** tasks to sing for the user. */
    private SC00001Tasks sc00001Tasks;

    /**
     * User online event to set display message.
     * @param evt
     */
    @McEventHandler
    public void onUserOnline(McPlayerJoinEvent evt)
    {
        evt.setJoinMessage(Messages.WelcomeMessage);
        
        evt.getPlayer().runTaskLater(MclibShowcasePlugin.instance(), 40, task -> {
                final RawMessageInterface raw = McLibInterface.instance().createRaw();
                raw.addHandler(StartShowcaseMessages.ClickHere, null, this::onStartShowcase, LocalDateTime.now().plusMinutes(10));
                raw.addMsg(StartShowcaseMessages.ToStartShowcase);
                McLibInterface.instance().getCurrentPlayer().sendRaw(raw);
            });
        this.sc00001Tasks = new SC00001Tasks(McLibInterface.instance().getCurrentPlayer());
    }
    
    /**
     * Starts the showcase
     */
    protected void onStartShowcase()
    {
        this.sc00001Tasks.cancel();
        McLibInterface.instance().getCurrentPlayer().sendMessage(StartShowcaseMessages.NiceOne);
    }

    /**
     * Localized messages
     * 
     * @author mepeisen
     *
     */
    @LocalizedMessages(value="showcase.start", defaultLocale = "en")
    public enum StartShowcaseMessages implements LocalizedMessageInterface
    {
        
        /**
         * Click here text
         */
        @LocalizedMessage(defaultMessage = LocalizedMessage.BOLD + LocalizedMessage.UNDERLINE  + "Click HERE (within 10 minutes)", severity = MessageSeverityType.Success)
        @MessageComment(value = {"Click here text"})
        ClickHere,
        
        /**
         * Starting showcase text
         */
        @LocalizedMessage(defaultMessage = " to start the showcase", severity = MessageSeverityType.Information)
        @MessageComment(value = {"Starting showcase text"})
        ToStartShowcase,
        
        /**
         * Message text for successful click.
         */
        @LocalizedMessage(defaultMessage = "nice one!", severity = MessageSeverityType.Success)
        @MessageComment(value = {"Message text for successful click."})
        NiceOne
        
    }

}
