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

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;

/**
 * Query for bungee servers (/mclib bungee getservers).
 * 
 * @author mepeisen
 *
 */
public class BungeeGetServersCommand implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.checkOpPermission(MclibCommand.CommandPermissions.Bungee);
    }
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        command.checkOpPermission(MclibCommand.CommandPermissions.Bungee);
        // TODO implement /mclib bungee getservers
    }
    
    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws McException
    {
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
     * Messages for /mclib bungee getservers.
     */
    @LocalizedMessages("cmd.mclib_bungee_getservers")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description.
         */
        @LocalizedMessage(defaultMessage = "Query bungee servers!")
        @MessageComment("Short description for /mclib bungee getservers")
        ShortDescription,
        
        /**
         * Description.
         */
        @LocalizedMessageList({
            "Query bungee servers live!"
        })
        @MessageComment("Long description for /mclib bungee getservers")
        Description
    }
    
}
