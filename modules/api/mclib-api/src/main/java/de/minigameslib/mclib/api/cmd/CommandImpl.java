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

import java.util.Arrays;
import java.util.Locale;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.util.function.FalseStub;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;
import de.minigameslib.mclib.api.util.function.TrueStub;

/**
 * Implementation of command interface.
 * 
 * @author mepeisen
 */
public class CommandImpl implements CommandInterface
{
    
    /**
     * the command sender.
     */
    private final CommandSender   sender;
    
    /**
     * The original command.
     */
    private final Command         command;
    
    /**
     * The original label.
     */
    private final String          label;
    
    /**
     * The original command line arguments.
     */
    private final String[]        args;
    
    /**
     * current command path.
     */
    private final String          commandPath;
    
    /**
     * Constructor to create the command.
     * 
     * @param sender
     *            the command sender.
     * @param command
     *            the original command
     * @param label
     *            the command label
     * @param args
     *            the command arguments
     * @param commandPath
     *            the current command path
     */
    public CommandImpl(CommandSender sender, Command command, String label, String[] args, String commandPath)
    {
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;
        this.commandPath = commandPath;
    }
    
    @Override
    public CommandSender getSender()
    {
        return this.sender;
    }
    
    @Override
    public McPlayerInterface getPlayer()
    {
        if (this.getSender() instanceof Player)
        {
            return ObjectServiceInterface.instance().getPlayer((Player) this.getSender());
        }
        return null;
    }
    
    @Override
    public Command getCommand()
    {
        return this.command;
    }
    
    @Override
    public String getLabel()
    {
        return this.label;
    }
    
    @Override
    public CommandInterface consumeArgs(int count)
    {
        final String[] args2 = Arrays.copyOfRange(this.args, count, this.args.length);
        StringBuilder newPath = new StringBuilder(this.commandPath);
        for (int i = 0; i < count; i++)
        {
            newPath.append(' ');
            newPath.append(this.args[i]);
        }
        return new CommandImpl(this.sender, this.command, this.label, args2, newPath.toString());
    }
    
    @Override
    public String[] getArgs()
    {
        return this.args;
    }
    
    @Override
    public McOutgoingStubbing<CommandInterface> when(McPredicate<CommandInterface> test) throws McException
    {
        if (test.test(this))
        {
            return new TrueStub<>(this);
        }
        return new FalseStub<>(this);
    }
    
    @Override
    public String getCommandPath()
    {
        return this.commandPath;
    }

    @Override
    public Locale getLocale()
    {
        if (this.getPlayer() != null)
        {
            return this.getPlayer().getPreferredLocale();
        }
        return McLibInterface.instance().getDefaultLocale();
    }
    
}
