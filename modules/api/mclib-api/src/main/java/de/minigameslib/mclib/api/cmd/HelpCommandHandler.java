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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;

/**
 * Prints help based on single sub command handlers.
 * 
 * @author mepeisen
 *
 */
public class HelpCommandHandler extends AbstractPagableCommandHandler implements SubCommandHandlerInterface
{
    
    /** help on sub command. */
    private SubCommandHandlerInterface subCommand;
    
    /** help on composite command. */
    private AbstractCompositeCommandHandler compositeCommand;
    
    /** logger. */
    private static final Logger LOGGER = Logger.getLogger(HelpCommandHandler.class.getName());
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        if (command.getArgs().length >= 1 && this.compositeCommand != null)
        {
            try
            {
                Integer.parseInt(command.getArgs()[0]);
            }
            catch (@SuppressWarnings("unused") NumberFormatException ex)
            {
                // assume we have a command name given to us.
                final String name = command.getArgs()[0].toLowerCase();
                final SubCommandHandlerInterface sub = this.compositeCommand.subCommands.get(name);
                if (sub == null || !sub.visible(command))
                {
                    command.send(CommonMessages.HelpUnknownSubCommand, command.getCommandPath(), name);
                    return;
                }
                new HelpCommandHandler(sub).handle(command.consumeArgs(1));
                return;
            }
        }
        super.handle(command);
    }

    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws McException
    {
        if (command.getArgs().length > 0 || this.compositeCommand == null)
        {
            return Collections.emptyList();
        }
        return getVisibleCommands(command).map(e -> e.getKey()).filter(elm -> elm.toLowerCase().startsWith(lastArg)).collect(Collectors.toList());
    }

    /**
     * Constructor.
     * 
     * @param command
     *            the underlying command for the help.
     */
    public HelpCommandHandler(SubCommandHandlerInterface command)
    {
        this.subCommand = command;
    }
    
    /**
     * Constructor.
     * 
     * @param command
     *            the underlying command for the help.
     */
    public HelpCommandHandler(AbstractCompositeCommandHandler command)
    {
        this.compositeCommand = command;
    }
    
    @Override
    public LocalizedMessageInterface getShortDescription(CommandInterface command)
    {
        return CommonMessages.HelpShortDescription;
    }
    
    @Override
    public LocalizedMessageInterface getDescription(CommandInterface command)
    {
        return CommonMessages.HelpLongDescription;
    }

    @Override
    protected int getLineCount(CommandInterface command)
    {
        if (this.compositeCommand != null)
        {
            return (int) this.getVisibleCommands(command).count();
        }
        final LocalizedMessageInterface description = this.subCommand.getDescription(command);
        if (description.isSingleLine())
        {
            return 1;
        }
        return description.toListArg(command.getCommandPath()).apply(command.getLocale(), command.isOp()).length;
    }

    @Override
    protected Serializable getHeader(CommandInterface command)
    {
        return CommonMessages.HelpHeader.toArg(command.getCommandPath());
    }

    @Override
    protected Serializable[] getLines(CommandInterface command, int start, int count)
    {
        if (this.compositeCommand != null)
        {
            final List<String> keys = new ArrayList<>();
            getVisibleCommands(command).map(e -> e.getKey()).forEach(keys::add);
            final List<Serializable> result = new ArrayList<>();
            if (start < keys.size())
            {
                final int max = Math.min(start + count, keys.size());
                for (final String key : keys.subList(start, max))
                {
                    LocalizedMessageInterface.DynamicArg shortDesc = null;
                    final SubCommandHandlerInterface sch = this.compositeCommand.subCommands.get(key);
                    try
                    {
                        shortDesc = sch.getShortDescription(command).toArg(command.getCommandPath());
                    }
                    catch (Throwable t)
                    {
                        LOGGER.log(Level.WARNING, "Problems getting short description on command " + key + "/" + sch, t);  //$NON-NLS-1$//$NON-NLS-2$
                    }
                    result.add(CommonMessages.HelpLineUsage.toArg(key, shortDesc));
                }
            }
            return result.toArray(new Serializable[result.size()]);
        }
        final LocalizedMessageInterface description = this.subCommand.getDescription(command);
        if (description.isSingleLine())
        {
            if (start == 0)
            {
                return new Serializable[]{
                        description.toArg(
                                new Serializable[]{command.getCommandPath()}
                        ).apply(command.getLocale(), command.isOp())
                    };
            }
            return new Serializable[0];
        }
        return description.toListArg(start, count, new Serializable[]{command.getCommandPath()}).apply(command.getLocale(), command.isOp());
    }

    /**
     * Filters sub commands by checking for visibility.
     * @param command the command to check for visible sub commands
     * @return the visible sub commands
     */
    private Stream<Entry<String, SubCommandHandlerInterface>> getVisibleCommands(CommandInterface command)
    {
        return this.compositeCommand.subCommands.entrySet().stream().filter(e -> e.getValue().visible(command));
    }
    
}
