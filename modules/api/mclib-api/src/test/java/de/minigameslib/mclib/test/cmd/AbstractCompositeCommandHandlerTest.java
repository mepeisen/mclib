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

package de.minigameslib.mclib.test.cmd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.AbstractCompositeCommandHandler;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.MessageServiceInterface;
import de.minigameslib.mclib.api.locale.MessagesConfigInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.FalseStub;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;
import de.minigameslib.mclib.api.util.function.TrueStub;

/**
 * Test case for {@link AbstractCompositeCommandHandler}.
 * 
 * @author mepeisen
 */
public class AbstractCompositeCommandHandlerTest
{
    
    /** the messages. */
    private MessagesConfigInterface messages;
    /** library. */
    private MessageServiceInterface lib;

    /**
     * Some setup.
     * @throws ClassNotFoundException 
     */
    @Before
    public void setup() throws ClassNotFoundException
    {
        this.lib = mock(MessageServiceInterface.class);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.locale.MessageServiceCache"), "SERVICES", this.lib); //$NON-NLS-1$ //$NON-NLS-2$
        when(this.lib.resolveContextVar(anyString())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable
            {
                return invocation.getArgumentAt(0, String.class);
            }
        });
        this.messages = mock(MessagesConfigInterface.class);
        when(this.lib.getMessagesFromMsg(anyObject())).thenReturn(this.messages);
    }
    
    /**
     * Tests pre method
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void testPreFailed() throws McException
    {
        final CommandSender sender = mock(CommandSender.class);
        
        new DummyCommandHandler().handle(new Command(sender, null, null, null, null));
    }
    
    /**
     * Tests usage sending on empty command
     * @throws McException 
     */
    @Test
    public void testSendsUsage() throws McException
    {
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final Player bukkitPlayer = mock(Player.class);
        
        new DummyCommandHandler().handle(new Command(bukkitPlayer, player, null, null, new String[0]));
        
        verify(player, times(1)).sendMessage(CommonMessages.HelpLineUsage, "usage", "help"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Tests sending unknown sub command error
     * @throws McException 
     */
    @Test
    public void testSendsUnknownSubCommand() throws McException
    {
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final Player bukkitPlayer = mock(Player.class);
        
        new DummyCommandHandler().handle(new Command(bukkitPlayer, player, null, null, new String[]{"unknown"})); //$NON-NLS-1$
        
        verify(player, times(1)).sendMessage(CommonMessages.CompositeUnknownSubCommand, "path", "unknown"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Tests invoking sub command
     * @throws McException 
     */
    @Test
    public void testInvokingSubCommand() throws McException
    {
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final Player bukkitPlayer = mock(Player.class);
        
        final SubCommandHandlerInterface subCommand = mock(SubCommandHandlerInterface.class);
        final DummyCommandHandler handler = new DummyCommandHandler();
        assertTrue(handler.injectSubCommand("sub", subCommand)); //$NON-NLS-1$
        // no twice injection
        assertFalse(handler.injectSubCommand("Sub", null)); //$NON-NLS-1$
        
        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                final CommandInterface cmd = invocation.getArgumentAt(0, CommandInterface.class);
                cmd.send(CommonMessages.CommandDisabled, cmd.getArgs()[0]);
                return null;
            }
        }).when(subCommand).handle(anyObject());
        
        handler.handle(new Command(bukkitPlayer, player, null, null, new String[]{"SUB", "FOO"})); //$NON-NLS-1$ //$NON-NLS-2$
        
        verify(player, times(1)).sendMessage(CommonMessages.CommandDisabled, "FOO"); //$NON-NLS-1$
    }
    
    /**
     * Tests invoking on Tab complete
     * @throws McException 
     */
    @Test
    public void testOnTabComplete1() throws McException
    {
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final Player bukkitPlayer = mock(Player.class);
        
        final SubCommandHandlerInterface subCommand = mock(SubCommandHandlerInterface.class);
        final DummyCommandHandler handler = new DummyCommandHandler();
        assertTrue(handler.injectSubCommand("sub", subCommand)); //$NON-NLS-1$
        
        final List<String> results = handler.onTabComplete(new Command(bukkitPlayer, player, null, null, new String[]{}), ""); //$NON-NLS-1$
        assertEquals(1, results.size());
        assertTrue(results.contains("sub")); //$NON-NLS-1$
    }
    
    /**
     * Tests invoking on Tab complete
     * @throws McException 
     */
    @Test
    public void testOnTabComplete2() throws McException
    {
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final Player bukkitPlayer = mock(Player.class);
        
        final SubCommandHandlerInterface subCommand = mock(SubCommandHandlerInterface.class);
        final DummyCommandHandler handler = new DummyCommandHandler();
        assertTrue(handler.injectSubCommand("sub", subCommand)); //$NON-NLS-1$
        assertTrue(handler.injectSubCommand("sub2", subCommand)); //$NON-NLS-1$
        
        final List<String> results = handler.onTabComplete(new Command(bukkitPlayer, player, null, null, new String[]{}), ""); //$NON-NLS-1$
        assertEquals(2, results.size());
        assertTrue(results.contains("sub")); //$NON-NLS-1$
        assertTrue(results.contains("sub2")); //$NON-NLS-1$
    }
    
    /**
     * Tests invoking on Tab complete
     * @throws McException 
     */
    @Test
    public void testOnTabComplete2b() throws McException
    {
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final Player bukkitPlayer = mock(Player.class);
        
        final SubCommandHandlerInterface subCommand = mock(SubCommandHandlerInterface.class);
        final DummyCommandHandler handler = new DummyCommandHandler();
        assertTrue(handler.injectSubCommand("sub", subCommand)); //$NON-NLS-1$
        assertTrue(handler.injectSubCommand("foo", subCommand)); //$NON-NLS-1$
        
        final List<String> results = handler.onTabComplete(new Command(bukkitPlayer, player, null, null, new String[]{}), "f"); //$NON-NLS-1$
        assertEquals(1, results.size());
        assertTrue(results.contains("foo")); //$NON-NLS-1$
    }
    
    /**
     * Tests invoking on Tab complete
     * @throws McException 
     */
    @Test
    public void testOnTabComplete2c() throws McException
    {
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final Player bukkitPlayer = mock(Player.class);
        
        final SubCommandHandlerInterface subCommand = mock(SubCommandHandlerInterface.class);
        final DummyCommandHandler handler = new DummyCommandHandler();
        assertTrue(handler.injectSubCommand("sub", subCommand)); //$NON-NLS-1$
        assertTrue(handler.injectSubCommand("foo", subCommand)); //$NON-NLS-1$
        
        final List<String> results = handler.onTabComplete(new Command(bukkitPlayer, player, null, null, new String[]{}), "q"); //$NON-NLS-1$
        assertEquals(0, results.size());
    }
    
    /**
     * Tests invoking on Tab complete
     * @throws McException 
     */
    @Test
    public void testOnTabCompleteUnknown() throws McException
    {
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final Player bukkitPlayer = mock(Player.class);
        
        final SubCommandHandlerInterface subCommand = mock(SubCommandHandlerInterface.class);
        final DummyCommandHandler handler = new DummyCommandHandler();
        assertTrue(handler.injectSubCommand("sub", subCommand)); //$NON-NLS-1$
        assertTrue(handler.injectSubCommand("foo", subCommand)); //$NON-NLS-1$
        
        final List<String> results = handler.onTabComplete(new Command(bukkitPlayer, player, null, null, new String[]{"unknown"}), ""); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(0, results.size());
    }
    
    /**
     * Tests invoking on Tab complete
     * @throws McException 
     */
    @Test
    public void testOnTabCompleteKnown() throws McException
    {
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final Player bukkitPlayer = mock(Player.class);
        
        final SubCommandHandlerInterface subCommand = mock(SubCommandHandlerInterface.class);
        final DummyCommandHandler handler = new DummyCommandHandler();
        assertTrue(handler.injectSubCommand("sub", subCommand)); //$NON-NLS-1$
        assertTrue(handler.injectSubCommand("foo", subCommand)); //$NON-NLS-1$
        
        when(subCommand.onTabComplete(anyObject(), anyString())).thenReturn(new ArrayList<>(Collections.singleton("bar"))); //$NON-NLS-1$
        
        final List<String> results = handler.onTabComplete(new Command(bukkitPlayer, player, null, null, new String[]{"sub"}), ""); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(1, results.size());
        assertTrue(results.contains("bar")); //$NON-NLS-1$
    }
    
    /**
     * dummy composite command
     */
    private static final class DummyCommandHandler extends AbstractCompositeCommandHandler
    {

        /**
         * constructor
         */
        public DummyCommandHandler()
        {
            // empty
        }

        @Override
        protected void sendUsage(CommandInterface command)
        {
            command.send(CommonMessages.HelpLineUsage, "usage", "help"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
    }
    
    /**
     * Sample Command impl
     */
    private static final class Command implements CommandInterface
    {
        
        /** sender */
        private final CommandSender sender;
        /** player */
        private final McPlayerInterface player;
        /** command */
        private final org.bukkit.command.Command command;
        /** label */
        private final String label;
        /** args */
        private final String[] args;

        /**
         * @param sender
         * @param player
         * @param command
         * @param label
         * @param args
         */
        public Command(CommandSender sender, McPlayerInterface player, org.bukkit.command.Command command, String label, String[] args)
        {
            this.sender = sender;
            this.player = player;
            this.command = command;
            this.label = label;
            this.args = args;
        }

        @Override
        public CommandSender getSender()
        {
            return this.sender;
        }

        @Override
        public McPlayerInterface getPlayer()
        {
            return this.player;
        }

        @Override
        public org.bukkit.command.Command getCommand()
        {
            return this.command;
        }

        @Override
        public String getLabel()
        {
            return this.label;
        }

        @Override
        public String[] getArgs()
        {
            return this.args;
        }

        @Override
        public CommandInterface consumeArgs(int count)
        {
            // dummy
            return new Command(this.sender, this.player, this.command, this.label, Arrays.copyOfRange(this.args, 1, this.args.length));
        }

        @Override
        public String getCommandPath()
        {
            // dummy
            return "path"; //$NON-NLS-1$
        }

        @Override
        public Locale getLocale()
        {
            // dummy
            return Locale.ENGLISH;
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
        
    }
    
}
