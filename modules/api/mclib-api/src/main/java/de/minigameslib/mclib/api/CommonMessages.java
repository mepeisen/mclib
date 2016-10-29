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

package de.minigameslib.mclib.api;

import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageSeverityType;

/**
 * Common messages within minigames lib.
 * 
 * @author mepeisen
 */
@LocalizedMessages("messages")
public enum CommonMessages implements LocalizedMessageInterface
{
    
    /**
     * Command must be executed in game.
     * 
     * <p>No Arguments</p>
     */
    @LocalizedMessage(defaultMessage = "Invoke this command in-game.", severity = MessageSeverityType.Error)
    InvokeIngame,
    
    /**
     * No permissions for a command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: command name</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "No permission for %1$s command.", severity = MessageSeverityType.Error)
    NoPermissionForCommand,
    
    /**
     * The command is disabled.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: command name</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "%1$s command disabled.", severity = MessageSeverityType.Error)
    CommandDisabled,
    
    // ***** commands, usages etc.
    
    /**
     * Error message for invalid commands (too many arguments)
     * 
     * <p>No arguments.</p>
     */
    @LocalizedMessage(defaultMessage = "Too many arguments", severity = MessageSeverityType.Error)
    TooManyArguments,
    
    /**
     * Mc2 command usage.
     * 
     * <p>No arguments.</p>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "Type " + LocalizedMessage.BLUE + "/mc2 help " + LocalizedMessage.GRAY + "for detailed help", severity = MessageSeverityType.Success)
    Mc2CommandUsage,
    
    /**
     * Invalid sub command in composite command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: current command path</li>
     * <li>String: current sub command</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Unknown command " + LocalizedMessage.BLUE + "%1$s %2$s", severity = MessageSeverityType.Error)
    CompositeUnknownSubCommand,
    
    /**
     * Paged output; header.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: info text</li>
     * <li>Integer: current page</li>
     * <li>Integer: total pages</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "=====" + LocalizedMessage.BLUE + "%1$s" + LocalizedMessage.GRAY + "====" + LocalizedMessage.BLUE + "Page " + LocalizedMessage.DARK_BLUE + "%2$d " + LocalizedMessage.BLUE + " from " + LocalizedMessage.DARK_BLUE + "%3$d" + LocalizedMessage.GRAY + "=====")
    PagedHeader,
    
    /**
     * Paged output; line.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: line text</li>
     * <li>Integer: line number</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = " " + LocalizedMessage.GRAY + "%1$s")
    PagedLine,
    
    /**
     * Paged output; wrong page number.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>Integer: Page number that was entered by user</li>
     * <li>Integer: Total page count being available</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Page %1$d out of range. Only values from 1 to %2$d allowed.", severity = MessageSeverityType.Error)
    PagedWrongPageNum,
    

    /**
     * Paged output; invalid page number (not numeric).
     * 
     * <p>No arguments.</p>
     */
    @LocalizedMessage(defaultMessage = "Invalid page number/ number format error.", severity = MessageSeverityType.Error)
    PagedInvalidNumber,
    
    /**
     * Paged output; usage information
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The command that was entered</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "Usage: " + LocalizedMessage.BLUE + "%1$s [page] " + LocalizedMessage.GRAY + "Display the given page.")
    PageUsage,
    
    /**
     * Help command header.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The command that was entered</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "help")
    HelpHeader,
    
    /**
     * Help line.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The command line</li>
     * <li>String: The short description</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.BLUE + "%1$s " + LocalizedMessage.GRAY + "%2$s")
    HelpLineUsage,
    
    /**
     * Help command short description.
     * 
     * <p>No arguments.</p>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "Display command help")
    HelpShortDescription,
    
    /**
     * Help on unknown sub command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The command line</li>
     * <li>String: The sub command</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Unknown command " + LocalizedMessage.BLUE + "%1$s %2$s", severity = MessageSeverityType.Error)
    HelpUnknownSubCommand,
    
    /**
     * Help command long description.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The command that was entered</li>
     * <li>String: The usage of this command</li>
     * </ol>
     */
    @LocalizedMessageList({
        LocalizedMessageList.GRAY + "Displays command help for command " + LocalizedMessageList.BLUE + "%1$s",
        LocalizedMessageList.GRAY + "Usage: " + LocalizedMessageList.BLUE + "%1$s [command] [page] " + LocalizedMessageList.GRAY + "Display the given help page.",
        LocalizedMessageList.GRAY + "The argument " + LocalizedMessageList.BLUE + "page is optional. If not entered it",
        LocalizedMessageList.GRAY + "will always display the first help page.",
        LocalizedMessageList.GRAY + "If a command name is given it will display help on that command.",
    })
    HelpLongDescription;
    
}
