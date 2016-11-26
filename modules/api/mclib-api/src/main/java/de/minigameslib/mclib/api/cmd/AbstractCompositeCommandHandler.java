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

package de.minigameslib.mclib.api.cmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;

/**
 * A handler for enabling sub commands.
 * 
 * @author mepeisen
 */
public abstract class AbstractCompositeCommandHandler implements CommandHandlerInterface
{
    
    /**
     * the configured sub commands (ordered).
     */
    protected Map<String, SubCommandHandlerInterface> subCommands = new TreeMap<>();
    
    /** logger. */
    private static final Logger LOGGER = Logger.getLogger(AbstractCompositeCommandHandler.class.getName());
    
    /**
     * pre parse the command.
     * 
     * @param command
     *            command
     * @return {@code true} if the execution can proceed.
     * @throws McException
     *             thrown if there are problems.
     */
    protected boolean pre(CommandInterface command) throws McException
    {
        // only in-game
        command.when(c -> !c.isPlayer()).thenThrow(CommonMessages.InvokeIngame);
        
        return true;
    }
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        if (!pre(command))
        {
            return;
        }
        
        // check for sub command
        if (command.getArgs().length == 0)
        {
            sendUsage(command);
            return;
        }
        
        final String name = command.getArgs()[0].toLowerCase();
        final SubCommandHandlerInterface handler = this.subCommands.get(name);
        if (handler == null || !handler.visible(command))
        {
            command.send(CommonMessages.CompositeUnknownSubCommand, command.getCommandPath(), command.getArgs()[0]);
            sendUsage(command);
            return;
        }
        
        handler.handle(command.consumeArgs(1));
    }
    
    /**
     * Injects a new sub command.
     * 
     * @param name
     *            sub command name
     * @param handler
     *            handler
     * @return {@code true} if the sub command was added, {@code false} if it already exists.
     */
    public boolean injectSubCommand(String name, SubCommandHandlerInterface handler)
    {
        if (this.subCommands.containsKey(name.toLowerCase()))
        {
            LOGGER.log(Level.WARNING, "Duplicate sub command " + name); //$NON-NLS-1$
            return false;
        }
        this.subCommands.put(name.toLowerCase(), handler);
        return true;
    }
    
    /**
     * Sends usage information.
     * 
     * @param command
     *            the command to be used.
     */
    protected abstract void sendUsage(CommandInterface command);

    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws McException
    {
        if (command.getArgs().length > 0)
        {
            final String name = command.getArgs()[0].toLowerCase();
            final SubCommandHandlerInterface handler = this.subCommands.get(name);
            if (handler == null || !handler.visible(command))
            {
                return Collections.emptyList();
            }
            return handler.onTabComplete(command.consumeArgs(1), lastArg);
        }
        return new ArrayList<>(this.subCommands.keySet()).stream().filter(elm -> elm.startsWith(lastArg)).collect(Collectors.toList());
    }

    /**
     * Returns the sub command by name.
     * @param key name of the sub command
     * @return the sub command; even invisible commands
     */
    public SubCommandHandlerInterface getSubCommand(String key)
    {
        return this.subCommands.get(key);
    }
    
}
