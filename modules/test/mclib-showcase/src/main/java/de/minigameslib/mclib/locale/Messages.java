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

package de.minigameslib.mclib.locale;

import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.locale.MessageSeverityType;

/**
 * Localized messages
 * 
 * @author mepeisen
 *
 */
@LocalizedMessages(value="core", defaultLocale = "en")
public enum Messages implements LocalizedMessageInterface
{
    
    /**
     * Welcome message
     */
    @LocalizedMessage(defaultMessage = "Welcome %1$s!")
    @MessageComment(value = {
            "Welcome message"},
        args = {@Argument({"Players display name."})})
    WelcomeMessage,
    
    /**
     * Click here text
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.BOLD + LocalizedMessage.UNDERLINE  + "Click HERE", severity = MessageSeverityType.Success)
    @MessageComment(value = {"Click here text"})
    StartShoecase_ClickHere,
    
    /**
     * Starting showcase text
     */
    @LocalizedMessage(defaultMessage = " to start the showcase", severity = MessageSeverityType.Information)
    @MessageComment(value = {"Starting showcase text"})
    StartShoecase_ToStartShowcase,
    
}
