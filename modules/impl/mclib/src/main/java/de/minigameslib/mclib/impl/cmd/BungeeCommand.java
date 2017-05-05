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
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.enums.ChildEnum;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;

/**
 * Bungee chat command (/mclib bungee).
 * 
 * @author mepeisen
 *
 */
public class BungeeCommand extends AbstractCompositeCommandHandler implements SubCommandHandlerInterface
{
    
    /**
     * Constructor.
     */
    public BungeeCommand()
    {
        this.subCommands.put("help", new HelpCommandHandler((AbstractCompositeCommandHandler) this)); //$NON-NLS-1$
        this.subCommands.put("list", new BungeeListCommand()); //$NON-NLS-1$
        // TODO this.subCommands.put("getservers", new BungeeGetServersCommand()); //$NON-NLS-1$
    }
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.checkOpPermission(MclibCommand.CommandPermissions.Bungee);
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
    
    @Override
    protected void sendUsage(CommandInterface command)
    {
        command.send(Messages.Usage);
    }
    
    /**
     * Messages for bungee chat command.
     */
    @LocalizedMessages("cmd.mclib_bungee")
    @ChildEnum({
        BungeeListCommand.Messages.class,
        BungeeGetServersCommand.Messages.class
    })
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Command usage.
         */
        @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "Enter " + LocalizedMessage.BLUE + "/mclib bungee help " + LocalizedMessage.GRAY + "for detailed help!")
        @MessageComment("Command usage for /mclib bungee")
        Usage,
        
        /**
         * Short description.
         */
        @LocalizedMessage(defaultMessage = "Information on bungee network!")
        @MessageComment("Short description for /mclib bungee")
        ShortDescription,
        
        /**
         * Description.
         */
        @LocalizedMessageList({
            "Display or manipulate your bungee network!",
            "Usage: " + LocalizedMessageList.CODE_COLOR + "/mclib bungee list " + LocalizedMessageList.INFORMATION_COLOR + "to list your bungee servers (cached)",
            "Usage: " + LocalizedMessageList.CODE_COLOR + "/mclib bungee getservers " + LocalizedMessageList.INFORMATION_COLOR + "to query existing bungee servers"
        })
        @MessageComment("Long description for /mclib bungee")
        Description,
    }
    
}
