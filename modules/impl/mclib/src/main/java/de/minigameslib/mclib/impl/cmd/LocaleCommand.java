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
 * @author mepeisen
 *
 */
public class LocaleCommand implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.isOnline() && command.checkOpPermission(MclibCommand.CommandPermissions.Locale);
    }
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        command.checkOnline();
        command.checkOpPermission(MclibCommand.CommandPermissions.Locale);
        if (command.getArgs().length == 0)
        {
            displayLocale(command);
        }
        else
        {
            final String locale = command.fetchString(null);
            command.checkMaxArgCount(0);
            
            final Locale loc = new Locale(locale);
            if (!McLibInterface.instance().getMainLocales().contains(loc))
            {
                displayLocaleWarning(command, loc);
            }
            command.getPlayer().setPreferredLocale(loc);
            displaySuccess(command, loc);
        }
    }
    
    /**
     * @param command
     * @param loc
     */
    private void displaySuccess(CommandInterface command, Locale loc)
    {
        command.send(Messages.Changed, loc.toString());
    }
    
    /**
     * @param command
     * @param loc
     */
    private void displayLocaleWarning(CommandInterface command, Locale loc)
    {
        command.send(Messages.LocaleWarning, loc.toString());
    }
    
    /**
     * @param command
     */
    private void displayLocale(CommandInterface command)
    {
        command.send(Messages.DisplayLocale, command.getPlayer().getPreferredLocale().toString());
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
     * Messages
     */
    @LocalizedMessages("cmd.mclib_locale")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description
         */
        @LocalizedMessage(defaultMessage = "Display or change your preferred locale!")
        @MessageComment("Short description for /mclib locale")
        ShortDescription,
        
        /**
         * Description
         */
        @LocalizedMessageList({
            "Display or change your preferred locale!",
            "Usage: " + LocalizedMessageList.CODE_COLOR + "/mclib locale " + LocalizedMessageList.INFORMATION_COLOR + "to query your preferred locale!",
            "Usage: " + LocalizedMessageList.CODE_COLOR + "/mclib locale [iso_code] " + LocalizedMessageList.INFORMATION_COLOR + "to change your preferred locale!",
            "You can use any iso 639 code, for example \"en\" or \"de\"."
        })
        @MessageComment("Long description for /mclib locale")
        Description,
        
        /**
         * Locale warning
         */
        @LocalizedMessageList(value = {
            "Your locale " + LocalizedMessage.CODE_COLOR + "%1$s " + LocalizedMessage.WARNING_COLOR + " may not be supported on this server.",
            "This is only a warning. Ask the support for supporting your locale."
        }, severity = MessageSeverityType.Warning)
        @MessageComment(value = "Chosen locale is not in default list", args = @MessageComment.Argument("users current locale"))
        LocaleWarning,
        
        /**
         * Display locale
         */
        @LocalizedMessage(defaultMessage = "Your current locale: " + LocalizedMessage.CODE_COLOR + "%1$s")
        @MessageComment(value = "Displays users current locale", args = @MessageComment.Argument("users current locale"))
        DisplayLocale,
        
        /**
         * Locale changed
         */
        @LocalizedMessage(defaultMessage = "Your new locale: " + LocalizedMessage.CODE_COLOR + "%1$s", severity = MessageSeverityType.Success)
        @MessageComment(value = "Locale was changed", args = @MessageComment.Argument("users current locale"))
        Changed,
    }
    
}
