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

package de.minigameslib.mclib.impl.cmd;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageSeverityType;

/**
 * Command implementation for /mclib mainlocale remove.
 * 
 * @author mepeisen
 *
 */
public class MainLocaleRemoveCommand implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.checkOpPermission(MclibCommand.CommandPermissions.MainLocale);
    }
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        command.checkOpPermission(MclibCommand.CommandPermissions.MainLocale);
        final String locale = command.fetchString(Messages.MissingLocale);
        command.checkMaxArgCount(0);
        
        final Locale loc = new Locale(locale);
        if (McLibInterface.instance().getMainLocales().contains(loc))
        {
            McLibInterface.instance().removeMainLocale(loc);
            displaySuccess(command, loc);
        }
        else
        {
            displayNotFound(command, loc);
        }
    }
    
    /**
     * Display success (mainlocale removed).
     * 
     * @param command
     *            command interface
     * @param loc
     *            locale that was removed
     */
    private void displaySuccess(CommandInterface command, Locale loc)
    {
        command.send(Messages.Removed, loc.toString());
    }
    
    /**
     * Display error (locale not found).
     * 
     * @param command
     *            command interface
     * @param loc
     *            locale that was not found.
     */
    private void displayNotFound(CommandInterface command, Locale loc)
    {
        command.send(Messages.NotFound, loc.toString());
    }
    
    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws McException
    {
        if (command.getArgs().length == 0)
        {
            return McLibInterface.instance().getMainLocales().stream().map(Locale::toString).filter(l -> l.toLowerCase().startsWith(lastArg)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    
    @Override
    public LocalizedMessageInterface getShortDescription(CommandInterface command)
    {
        return Messages.ShortDescription;
    }
    
    @Override
    public LocalizedMessageInterface getDescription(CommandInterface command)
    {
        return Messages.Description;
    }
    
    /**
     * Messages for /mclib mainlocale remove.
     */
    @LocalizedMessages("cmd.mclib_defaultlocale_remove")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description.
         */
        @LocalizedMessage(defaultMessage = "Remove a server main locale!")
        @MessageComment("Short description for /mclib mainlocale remove")
        ShortDescription,
        
        /**
         * Description.
         */
        @LocalizedMessageList({
            "Remove a server main locale!",
            "Usage: " + LocalizedMessageList.CODE_COLOR + "/mclib mainlocale remove [iso_code] " + LocalizedMessageList.INFORMATION_COLOR + "to remove a server main locale!",
            "You can use any iso 639 code, for example \"en\" or \"de\"."
        })
        @MessageComment("Long description for /mclib mainlocale remove")
        Description,
        
        /**
         * Locale argument missing.
         */
        @LocalizedMessage(defaultMessage = "Locale argument missing", severity = MessageSeverityType.Error)
        @MessageComment("Locale argument missing")
        MissingLocale,
        
        /**
         * Locale not found in list.
         */
        @LocalizedMessage(defaultMessage = "Locale not found in list: " + LocalizedMessage.CODE_COLOR + "%1$s", severity = MessageSeverityType.Error)
        @MessageComment(value = "Locale not found in list", args = @MessageComment.Argument("locale"))
        NotFound,
        
        /**
         * Locale removed.
         */
        @LocalizedMessage(defaultMessage = "Removed main locale: " + LocalizedMessage.CODE_COLOR + "%1$s", severity = MessageSeverityType.Success)
        @MessageComment(value = "Locale was removed", args = @MessageComment.Argument("locale"))
        Removed,
    }
    
}
