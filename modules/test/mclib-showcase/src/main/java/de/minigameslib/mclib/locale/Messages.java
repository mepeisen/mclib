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

import de.minigameslib.mclib.api.enums.ChildEnum;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.run.SC00001Tasks.Showcase00001CountdownMessages;
import de.minigameslib.mclib.run.StartShowcase.StartShowcaseMessages;

/**
 * Localized messages
 * 
 * @author mepeisen
 *
 */
@LocalizedMessages(value="core", defaultLocale = "en")
@ChildEnum({StartShowcaseMessages.class, Showcase00001CountdownMessages.class})
public enum Messages implements LocalizedMessageInterface
{
    
    /**
     * Welcome message
     */
    @LocalizedMessage(defaultMessage = "Welcome %1$s!")
    @MessageComment(value = {
            "Welcome message"},
        args = {@Argument({"Players display name."})})
    WelcomeMessage
    
}
