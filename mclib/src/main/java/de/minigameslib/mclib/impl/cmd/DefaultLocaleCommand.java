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
 * Default locale command (/mclib defaultlocale).
 * 
 * @author mepeisen
 *
 */
public class DefaultLocaleCommand implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.checkOpPermission(MclibCommand.CommandPermissions.DefaultLocale);
    }
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        command.checkOpPermission(MclibCommand.CommandPermissions.DefaultLocale);
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
            McLibInterface.instance().setDefaultLocale(loc);
            displaySuccess(command, loc);
        }
    }
    
    /**
     * Display success message.
     * 
     * @param command
     *            command interface
     * @param loc
     *            new locale
     */
    private void displaySuccess(CommandInterface command, Locale loc)
    {
        command.send(Messages.Changed, loc.toString());
    }
    
    /**
     * Display locale warning.
     * 
     * @param command
     *            command interface
     * @param loc
     *            new locale
     */
    private void displayLocaleWarning(CommandInterface command, Locale loc)
    {
        command.send(Messages.LocaleWarning, loc.toString());
    }
    
    /**
     * Display current locale.
     * 
     * @param command
     *            command interface
     */
    private void displayLocale(CommandInterface command)
    {
        command.send(Messages.DisplayLocale, McLibInterface.instance().getDefaultLocale().toString());
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
     * Messages for /mclib defaultlocale.
     */
    @LocalizedMessages("cmd.mclib_defaultlocale")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description.
         */
        @LocalizedMessage(defaultMessage = "Display or change server default locale!")
        @MessageComment("Short description for /mclib defaultlocale")
        ShortDescription,
        
        /**
         * Description.
         */
        @LocalizedMessageList({
            "Display or change server default locale!",
            "Usage: " + LocalizedMessageList.CODE_COLOR + "/mclib defaultlocale " + LocalizedMessageList.INFORMATION_COLOR + "to query server default locale!",
            "Usage: " + LocalizedMessageList.CODE_COLOR + "/mclib defaultlocale [iso_code] " + LocalizedMessageList.INFORMATION_COLOR + "to change server default locale!",
            "You can use any iso 639 code, for example \"en\" or \"de\"."
        })
        @MessageComment("Long description for /mclib defaultlocale")
        Description,
        
        /**
         * Locale warning.
         */
        @LocalizedMessageList(value = {
            "Server default locale " + LocalizedMessage.CODE_COLOR + "%1$s " + LocalizedMessage.WARNING_COLOR + " may not be supported on this server.",
            "This is only a warning. You should add the locale with " + LocalizedMessage.CODE_COLOR + "/mclib mainlocale add %1$s",
            "You should check if all messages are translated with " + LocalizedMessage.CODE_COLOR + "/mclib translate check %1$s",
        }, severity = MessageSeverityType.Warning)
        @MessageComment(value = "Chosen locale is not in main list", args = @MessageComment.Argument("server default locale"))
        LocaleWarning,
        
        /**
         * Display locale.
         */
        @LocalizedMessage(defaultMessage = "Server default locale: " + LocalizedMessage.CODE_COLOR + "%1$s")
        @MessageComment(value = "Displays server default locale", args = @MessageComment.Argument("server default locale"))
        DisplayLocale,
        
        /**
         * Locale changed.
         */
        @LocalizedMessage(defaultMessage = "New server default locale : " + LocalizedMessage.CODE_COLOR + "%1$s", severity = MessageSeverityType.Success)
        @MessageComment(value = "Locale was changed", args = @MessageComment.Argument("server default locale"))
        Changed,
    }
    
}
