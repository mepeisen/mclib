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

import de.minigameslib.mclib.api.cmd.AbstractCompositeCommandHandler;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.HelpCommandHandler;
import de.minigameslib.mclib.api.enums.ChildEnum;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.perms.Permission;
import de.minigameslib.mclib.api.perms.Permissions;
import de.minigameslib.mclib.api.perms.PermissionsInterface;

/**
 * Helper for command "/mclib"
 * 
 * @author mepeisen
 */
public class MclibCommand extends AbstractCompositeCommandHandler
{
    
    /**
     * Constructor
     */
    public MclibCommand()
    {
        this.subCommands.put("help", new HelpCommandHandler(this)); //$NON-NLS-1$
        this.subCommands.put("locale", new LocaleCommand()); //$NON-NLS-1$
        this.subCommands.put("defaultlocale", new DefaultLocaleCommand()); //$NON-NLS-1$
        this.subCommands.put("mainlocale", new MainLocaleCommand()); //$NON-NLS-1$
        // TODO this.subCommands.put("translate", new TranslateCommand()); //$NON-NLS-1$
        this.subCommands.put("bungee", new BungeeCommand()); //$NON-NLS-1$
    }
    
    @Override
    protected void sendUsage(CommandInterface command)
    {
        command.send(Messages.Usage);
    }
    
    /**
     * Messages
     */
    @LocalizedMessages("cmd.mclib")
    @ChildEnum({
        LocaleCommand.Messages.class,
        DefaultLocaleCommand.Messages.class,
        MainLocaleCommand.Messages.class,
        BungeeCommand.Messages.class
    })
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Command usage
         */
        @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "Enter " + LocalizedMessage.BLUE + "/mclib help " + LocalizedMessage.GRAY + "for detailed help!")
        @MessageComment("Command usage for /mclib")
        Usage,
    }
    
    /**
     * Permissions
     */
    @Permissions("mclib.command")
    public enum CommandPermissions implements PermissionsInterface
    {
        /**
         * Locale command
         */
        @Permission
        Locale,
        
        /**
         * DefaultLocale command
         */
        @Permission
        DefaultLocale,
        
        /**
         * MainLocale command
         */
        @Permission
        MainLocale,
        
        /**
         * Translate command
         */
        @Permission
        Translate,
        
        /**
         * Bungee command
         */
        @Permission
        Bungee,
    }
    
}
