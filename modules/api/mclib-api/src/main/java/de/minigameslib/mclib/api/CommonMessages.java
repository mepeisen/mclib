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
import de.minigameslib.mclib.api.locale.MessageComment;
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
     * Error message displayed when a command must not be invoked on console.
     * 
     * <p>
     * No Arguments
     * </p>
     */
    @LocalizedMessage(defaultMessage = "Invoke this command in-game.", severity = MessageSeverityType.Error)
    @MessageComment({ "Error message displayed if a command must not be invoked on console." })
    InvokeIngame,
    
    /**
     * Error message displayed when a command must not be invoked ingame.
     * 
     * <p>
     * No Arguments
     * </p>
     */
    @LocalizedMessage(defaultMessage = "Invoke this command on console.", severity = MessageSeverityType.Error)
    @MessageComment({ "Error message displayed if a command must not be invoked ingame." })
    InvokeOnConsole,
    
    /**
     * Error message displayed if player does not have permission for a command.
     * 
     * <p>
     * Arguments:
     * </p>
     * 
     * <ol>
     * <li>String: Name of the command.</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "No permission for %1$s command.", severity = MessageSeverityType.Error)
    @MessageComment(value = { "Error message displayed if player does not have permission for a command." }, args = {
            @MessageComment.Argument({ "Name of the command." })
    })
    NoPermissionForCommand,
    
    /**
     * Error message displayed if a command is disabled.
     * 
     * <p>
     * Arguments:
     * </p>
     * 
     * <ol>
     * <li>String: Name of the command</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "%1$s command disabled.", severity = MessageSeverityType.Error)
    @MessageComment(value = { "Error message displayed if a command is disabled." }, args = {
            @MessageComment.Argument({ "Name of the command." })
    })
    CommandDisabled,
    
    // ***** commands, usages etc.
    
    /**
     * Error message displayed if there are too many arguments in command.
     * 
     * <p>
     * No arguments.
     * </p>
     */
    @LocalizedMessage(defaultMessage = "Too many arguments", severity = MessageSeverityType.Error)
    @MessageComment({ "Error message displayed if there are too many arguments in command." })
    TooManyArguments,
    
    /**
     * The usage of mc2 comment; hint to type /mc2 help.
     * 
     * <p>
     * No arguments.
     * </p>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "Type " + LocalizedMessage.BLUE + "/mc2 help " + LocalizedMessage.GRAY + "for detailed help", severity = MessageSeverityType.Success)
    @MessageComment({ "The usage of mc2 comment; hint to type /mc2 help" })
    Mc2CommandUsage,
    
    /**
     * Error message if an invalid sub command was used.
     * 
     * <p>
     * Arguments:
     * </p>
     * 
     * <ol>
     * <li>String: Command path for the main command.</li>
     * <li>String: Command name of the invalid sub command.</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Unknown command " + LocalizedMessage.BLUE + "%1$s %2$s", severity = MessageSeverityType.Error)
    @MessageComment(value = { "Error message if an invalid sub command was used." }, args = {
            @MessageComment.Argument({ "Command path for the main command." }),
            @MessageComment.Argument({ "Command name of the invalid sub command." })
    })
    CompositeUnknownSubCommand,
    
    /**
     * Paged command: header of output for paged command.
     * 
     * <p>
     * Arguments:
     * </p>
     * 
     * <ol>
     * <li>String: Informational text/ command name.</li>
     * <li>Integer: Current page number.</li>
     * <li>Integer: Total number of pages.</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "=====" + LocalizedMessage.BLUE + "%1$s" + LocalizedMessage.GRAY + "===="
            + LocalizedMessage.BLUE + "Page " + LocalizedMessage.DARK_BLUE + "%2$d " + LocalizedMessage.BLUE + " from " + LocalizedMessage.DARK_BLUE + "%3$d" + LocalizedMessage.GRAY + "=====")
    @MessageComment(value = { "Paged command: header of output for paged command." }, args = {
            @MessageComment.Argument({ "Informational text/ command name." }),
            @MessageComment.Argument(value = { "Current page number." }, type = "Integer"),
            @MessageComment.Argument(value = { "Total number of pages." }, type = "Integer")
    })
    PagedHeader,
    
    /**
     * Paged command: line of a paged output.
     * 
     * <p>
     * Arguments:
     * </p>
     * 
     * <ol>
     * <li>String: Line text.</li>
     * <li>Integer: Current line number.</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = " " + LocalizedMessage.GRAY + "%1$s")
    @MessageComment(value = { "Paged command: line of a paged output." }, args = {
            @MessageComment.Argument({ "Line text." }),
            @MessageComment.Argument(value = { "Current line number." }, type = "Integer")
    })
    PagedLine,
    
    /**
     * Paged command: Error message for invalid page number (out of range).
     * 
     * <p>
     * Arguments:
     * </p>
     * 
     * <ol>
     * <li>Integer: Page number that was entered by user</li>
     * <li>Integer: Total page count being available</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Page %1$d out of range. Only values from 1 to %2$d allowed.", severity = MessageSeverityType.Error)
    @MessageComment(value = { "Paged command: Error message for invalid page number (out of range)." }, args = {
            @MessageComment.Argument(value = { "Page number that was entered by user." }, type = "Integer"),
            @MessageComment.Argument(value = { "Total page count being available." }, type = "Integer")
    })
    PagedWrongPageNum,
    
    /**
     * Paged command: Error message for invalid page number (not numeric).
     * 
     * <p>
     * No arguments.
     * </p>
     */
    @LocalizedMessage(defaultMessage = "Invalid page number/ number format error.", severity = MessageSeverityType.Error)
    @MessageComment({ "Paged command: Error message for invalid page number (not numeric)." })
    PagedInvalidNumber,
    
    /**
     * Paged output; Command usage.
     * 
     * <p>
     * Arguments:
     * </p>
     * 
     * <ol>
     * <li>String: The command line entered by the user.</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "Usage: " + LocalizedMessage.BLUE + "%1$s [page] " + LocalizedMessage.GRAY + "Display the given page.")
    @MessageComment(value = { "Paged command: Command usage." }, args = {
            @MessageComment.Argument({ "The command line entered by the user." })
    })
    PageUsage,
    
    /**
     * Help command: header line text.
     * 
     * <p>
     * Arguments:
     * </p>
     * 
     * <ol>
     * <li>String: The command line entered by the user.</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "help")
    @MessageComment(value = { "Help command: header line text." }, args = {
            @MessageComment.Argument({ "The command line entered by the user." })
    })
    HelpHeader,
    
    /**
     * Help command: line of the help command.
     * 
     * <p>
     * Arguments:
     * </p>
     * 
     * <ol>
     * <li>String: The command line entered by the user.</li>
     * <li>String: The short description of the sub command.</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.BLUE + "%1$s " + LocalizedMessage.GRAY + "%2$s")
    @MessageComment(value = { "Help command: line of the help command." }, args = {
            @MessageComment.Argument({ "The command line entered by the user." }),
            @MessageComment.Argument({ "The short description of the sub command." })
    })
    HelpLineUsage,
    
    /**
     * Help command: Short description.
     * 
     * <p>
     * No arguments.
     * </p>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "Display command help")
    @MessageComment({ "Help command: Short description." })
    HelpShortDescription,
    
    /**
     * Help command: Unknown sub command.
     * 
     * <p>
     * Arguments:
     * </p>
     * 
     * <ol>
     * <li>String: The command line entered by the user.</li>
     * <li>String: The sub command name</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Unknown command " + LocalizedMessage.BLUE + "%1$s %2$s", severity = MessageSeverityType.Error)
    @MessageComment(value = { "Help command: Unknown sub command." }, args = {
            @MessageComment.Argument({ "The command line entered by the user." }),
            @MessageComment.Argument({ "The sub command name." })
    })
    HelpUnknownSubCommand,
    
    /**
     * Help command: Long description.
     * 
     * <p>
     * Arguments:
     * </p>
     * 
     * <ol>
     * <li>String: The command line entered by the user</li>
     * </ol>
     */
    @LocalizedMessageList({
            LocalizedMessageList.GRAY + "Displays command help for command " + LocalizedMessageList.BLUE + "%1$s",
            LocalizedMessageList.GRAY + "Usage: " + LocalizedMessageList.BLUE + "%1$s [command] [page] " + LocalizedMessageList.GRAY + "Display the given help page.",
            LocalizedMessageList.GRAY + "The argument " + LocalizedMessageList.BLUE + "page is optional. If not entered it",
            LocalizedMessageList.GRAY + "will always display the first help page.",
            LocalizedMessageList.GRAY + "If a command name is given it will display help on that command.",
    })
    @MessageComment(value = { "Help command: Long description." }, args = {
            @MessageComment.Argument({ "The command line entered by the user." })
    })
    HelpLongDescription,
    
    /**
     * Invoking smart gui commands but client has no forge mod.
     * 
     * <p>
     * No arguments.
     * </p>
     */
    @LocalizedMessage(defaultMessage = "You do not have the mclib client mod.", severity = MessageSeverityType.Error)
    @MessageComment({ "Smart gui/ client mod not installed." })
    NoSmartGui,
    
    /**
     * Error that a plugin object type is broken. Indicates code errors.
     * <p>
     * Arguments:
     * </p>
     * 
     * <ol>
     * <li>String: The plugin name</li>
     * <li>String: The type name</li>
     * <li>String: The enumeration class name</li>
     * </ol>
     */
    @LocalizedMessage(
            defaultMessage = "Problems registering object for object type %2$s",
            defaultAdminMessage = "Problems registering object for object type %1$s:%2$s (enum class name %3$s)",
            severity = MessageSeverityType.Error)
    @MessageComment(value = { "Error that a plugin object type is broken" }, args = {
            @MessageComment.Argument({ "The plugin name." }),
            @MessageComment.Argument({ "The type name." }),
            @MessageComment.Argument({ "The enumeration class name." })
    })
    BrokenObjectType,
    
    /**
     * Error that a plugin object type is broken. Indicates code errors.
     * <p>
     * Arguments:
     * </p>
     * 
     * <ol>
     * <li>String: The plugin name</li>
     * <li>String: The type name</li>
     * <li>String: The enumeration class name</li>
     * </ol>
     */
    @LocalizedMessage(
            defaultMessage = "Problems registering object for object type %2$s",
            defaultAdminMessage = "Problems registering object for object type %1$s:%2$s (enum class name %3$s is not an enum)",
            severity = MessageSeverityType.Error)
    @MessageComment(value = { "Error that a plugin object type is broken" }, args = {
            @MessageComment.Argument({ "The plugin name." }),
            @MessageComment.Argument({ "The type name." }),
            @MessageComment.Argument({ "The enumeration class name." })
    })
    BrokenObjectTypeNotAnEnum,
    
    /**
     * Error that a plugin object type is broken. Indicates code errors.
     * <p>
     * Arguments:
     * </p>
     * 
     * <ol>
     * <li>String: The plugin name</li>
     * <li>String: The type name</li>
     * <li>String: The enumeration class name</li>
     * </ol>
     */
    @LocalizedMessage(
            defaultMessage = "Problems registering object for object type %2$s",
            defaultAdminMessage = "Problems registering object for object type %1$s:%2$s (enum class name %3$s is not registered)",
            severity = MessageSeverityType.Error)
    @MessageComment(value = { "Error that a plugin object type is broken" }, args = {
            @MessageComment.Argument({ "The plugin name." }),
            @MessageComment.Argument({ "The type name." }),
            @MessageComment.Argument({ "The enumeration class name." })
    })
    BrokenObjectTypeEnumNotRegistered,
    
    /**
     * Error that a plugin object type is broken. Indicates code errors.
     * <p>
     * Arguments:
     * </p>
     * 
     * <ol>
     * <li>String: The plugin name</li>
     * <li>String: The type name</li>
     * <li>String: The enumeration class name</li>
     * <li>String: The exception message</li>
     * </ol>
     */
    @LocalizedMessage(
            defaultMessage = "Problems registering object for object type %2$s",
            defaultAdminMessage = "Problems registering object for object type %1$s:%2$s (enum class name %3$s, exception message: %4$s)",
            severity = MessageSeverityType.Error)
    @MessageComment(value = { "Error that a plugin object type is broken" }, args = {
            @MessageComment.Argument({ "The plugin name." }),
            @MessageComment.Argument({ "The type name." }),
            @MessageComment.Argument({ "The enumeration class name." }),
            @MessageComment.Argument({ "The exception message." })
    })
    BrokenObjectTypeEnumException,
    
    /**
     * Plugin is loaded twice.
     * <p>
     * Arguments:
     * </p>
     * 
     * <ol>
     * <li>String: The plugin name</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Plugin %1$s loads twice", severity = MessageSeverityType.Error)
    @MessageComment(value = { "Plugin is loaded twice." }, args = {
            @MessageComment.Argument({ "The plugin name." })
    })
    PluginLoadedTwice,
    
    /**
     * Plugin not loaded.
     * <p>
     * Arguments:
     * </p>
     * 
     * <ol>
     * <li>String: The plugin name</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Plugin %1$s is not loaded", severity = MessageSeverityType.Error)
    @MessageComment(value = { "Plugin is not loaded." }, args = {
            @MessageComment.Argument({ "The plugin name." })
    })
    PluginNotLoaded,
    
    /**
     * Element already deleted error.
     * 
     * <p>
     * No arguments.
     * </p>
     */
    @LocalizedMessage(defaultMessage = "Object already deleted.", severity = MessageSeverityType.Error)
    @MessageComment({ "Element already deleted error." })
    AlreadyDeletedError,
    
    /**
     * Common internal error we do not pass to normal users.
     */
    @LocalizedMessage(defaultMessage = "Internal error.", defaultAdminMessage = "Internal error. Error message: %1$s", severity = MessageSeverityType.Error)
    @MessageComment(value = "Common internal error we do not pass to normal users.", args = { @MessageComment.Argument({ "Error text (only meant to be seen by admins)" }) })
    InternalError,
    
    /**
     * Sign was not found.
     */
    @LocalizedMessage(defaultMessage = "Sign not found.", severity = MessageSeverityType.Error)
    @MessageComment(value = "Error if sign was not found.")
    SignNotFoundError,
    
    /**
     * Entity was not found.
     */
    @LocalizedMessage(defaultMessage = "Entity not found.", severity = MessageSeverityType.Error)
    @MessageComment(value = "Error if entity was not found.")
    EntityNotFoundError,
    
    /**
     * Resource pack is declined.
     */
    @LocalizedMessage(defaultMessage = "Unable to use item tool. You declined the resource pack.")
    @MessageComment({ "Resource pack is declined." })
    ResourcePackDeclined,
    
    /**
     * Resource pack failed.
     */
    @LocalizedMessage(defaultMessage = "Unable to use item tool. Resource pack download failed.")
    @MessageComment({ "Resource pack failed." })
    ResourcePackFailed,
    
    /**
     * Resource pack accepted.
     */
    @LocalizedMessage(defaultMessage = "Wait for resource pack download to finish.")
    @MessageComment({ "Resource pack download in progress." })
    ResourcePackAccepted,
    
    /**
     * A dummy filler.
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.BLACK)
    @MessageComment({ "dummy filler icon" })
    IconFill,
    
    /**
     * float value is too high.
     */
    @LocalizedMessage(defaultMessage = "%1$s: Given value %2$f is too high. Maximum allowed is %3$f.")
    @MessageComment(value = { "float value too high" }, args = { @MessageComment.Argument("config path"), @MessageComment.Argument("given value"), @MessageComment.Argument("max value") })
    ValidateFValueTooHigh,
    
    /**
     * float value is too low.
     */
    @LocalizedMessage(defaultMessage = "%1$s: Given value %2$f is too low. Minimum allowed is %3$f.")
    @MessageComment(value = { "float value too low" }, args = { @MessageComment.Argument("config path"), @MessageComment.Argument("given value"), @MessageComment.Argument("min value") })
    ValidateFValueTooLow,
    
    /**
     * long value is too high.
     */
    @LocalizedMessage(defaultMessage = "%1$s: Given value %2$d is too high. Maximum allowed is %3$d.")
    @MessageComment(value = { "long value too high" }, args = { @MessageComment.Argument("config path"), @MessageComment.Argument("given value"), @MessageComment.Argument("max value") })
    ValidateLValueTooHigh,
    
    /**
     * long value is too low.
     */
    @LocalizedMessage(defaultMessage = "%1$s: Given value %2$d is too low. Minimum allowed is %3$d.")
    @MessageComment(value = { "long value too low" }, args = { @MessageComment.Argument("config path"), @MessageComment.Argument("given value"), @MessageComment.Argument("min value") })
    ValidateLValueTooLow,
    
    /**
     * value is not set.
     */
    @LocalizedMessage(defaultMessage = "%1$s: Value must be set.")
    @MessageComment(value = { "value is not set" }, args = { @MessageComment.Argument("config path") })
    ValidateNotSet,
    
    /**
     * List too small.
     */
    @LocalizedMessage(defaultMessage = "%1$s: List must contain at least %3$d entries. Only %2$d entries given.")
    @MessageComment(value = { "List too small" }, args = { @MessageComment.Argument("config path"), @MessageComment.Argument("given entry count"), @MessageComment.Argument("min entry count") })
    ValidateListTooSmall,
    
    /**
     * List too big.
     */
    @LocalizedMessage(defaultMessage = "%1$s: List must contain at most %3$d entries. %2$d entries given.")
    @MessageComment(value = { "List too big" }, args = { @MessageComment.Argument("config path"), @MessageComment.Argument("given entry count"), @MessageComment.Argument("max entry count") })
    ValidateListTooBig,
    
    /**
     * String too small.
     */
    @LocalizedMessage(defaultMessage = "%1$s: String must contain at least %3$d characters. Only %2$d characters given.")
    @MessageComment(
            value = { "List too small" },
            args = { @MessageComment.Argument("config path"), @MessageComment.Argument("given character count"), @MessageComment.Argument("min character count") })
    ValidateStringTooSmall,
    
    /**
     * String too big.
     */
    @LocalizedMessage(defaultMessage = "%1$s: String must contain at most %3$d characters. %2$d characters given.")
    @MessageComment(value = { "List too big" }, args = { @MessageComment.Argument("config path"), @MessageComment.Argument("given character count"), @MessageComment.Argument("max character count") })
    ValidateStringTooBig,
    
    /**
     * prev page icon
     */
    @LocalizedMessage(defaultMessage = "Previous page")
    @MessageComment(value = "prev page icon")
    IconPreviousPage,
    
    /**
     * next page icon
     */
    @LocalizedMessage(defaultMessage = "Next page")
    @MessageComment(value = "next page icon")
    IconNextPage,
    
}
