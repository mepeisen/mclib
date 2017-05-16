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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.AbstractPagableCommandHandler;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McBiFunction;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;

/**
 * Tests for {@link AbstractPagableCommandHandler}.
 * 
 * @author mepeisen
 *
 */
public class AbstractPagableCommandHandlerTest
{
    
    /**
     * tests the first page.
     * 
     * @throws McException
     *             thrown on errors
     */
    @Test
    public void testFirstPage() throws McException
    {
        final CommandSender sender = mock(CommandSender.class);
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final DummyHandler dummy = new DummyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[] {});
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedHeader, "DUMMY-HEADER", 1, 3); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 1", 1); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 2", 2); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 3", 3); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 4", 4); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 5", 5); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 6", 6); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 7", 7); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 8", 8); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 9", 9); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 10", 10); //$NON-NLS-1$
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * tests the second page.
     * 
     * @throws McException
     *             thrown on errors
     */
    @Test
    public void testFirstPage2() throws McException
    {
        final CommandSender sender = mock(CommandSender.class);
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final DummyHandler dummy = new DummyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[] { "1" }); //$NON-NLS-1$
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedHeader, "DUMMY-HEADER", 1, 3); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 1", 1); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 2", 2); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 3", 3); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 4", 4); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 5", 5); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 6", 6); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 7", 7); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 8", 8); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 9", 9); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 10", 10); //$NON-NLS-1$
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * tests the second page.
     * 
     * @throws McException
     *             thrown on errors
     */
    @Test
    public void testSecondPage() throws McException
    {
        final CommandSender sender = mock(CommandSender.class);
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final DummyHandler dummy = new DummyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[] { "2" }); //$NON-NLS-1$
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedHeader, "DUMMY-HEADER", 2, 3); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 11", 11); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 12", 12); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 13", 13); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 14", 14); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 15", 15); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 16", 16); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 17", 17); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 18", 18); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 19", 19); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 20", 20); //$NON-NLS-1$
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * tests the third page.
     * 
     * @throws McException
     *             thrown on errors
     */
    @Test
    public void testThirdPage() throws McException
    {
        final CommandSender sender = mock(CommandSender.class);
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final DummyHandler dummy = new DummyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[] { "3" }); //$NON-NLS-1$
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedHeader, "DUMMY-HEADER", 3, 3); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 21", 21); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 22", 22); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 23", 23); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 24", 24); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 25", 25); //$NON-NLS-1$
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * tests the negative page.
     * 
     * @throws McException
     *             thrown on errors
     */
    @Test
    public void testNegativePage() throws McException
    {
        final CommandSender sender = mock(CommandSender.class);
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final DummyHandler dummy = new DummyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[] { "-1" }); //$NON-NLS-1$
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedWrongPageNum, -1, 3);
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * tests the zero page.
     * 
     * @throws McException
     *             thrown on errors
     */
    @Test
    public void testZeroPage() throws McException
    {
        final CommandSender sender = mock(CommandSender.class);
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final DummyHandler dummy = new DummyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[] { "0" }); //$NON-NLS-1$
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedWrongPageNum, 0, 3);
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * tests the fourth page.
     * 
     * @throws McException
     *             thrown on errors
     */
    @Test
    public void testFourthPage() throws McException
    {
        final CommandSender sender = mock(CommandSender.class);
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final DummyHandler dummy = new DummyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[] { "4" }); //$NON-NLS-1$
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedWrongPageNum, 4, 3);
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * tests the number format exception.
     * 
     * @throws McException
     *             thrown on errors
     */
    @Test
    public void testNfe() throws McException
    {
        final CommandSender sender = mock(CommandSender.class);
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final DummyHandler dummy = new DummyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[] { "foo" }); //$NON-NLS-1$
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedInvalidNumber);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PageUsage, "path"); //$NON-NLS-1$
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * tests the first page.
     * 
     * @throws McException
     *             thrown on errors
     */
    @Test
    public void testEmptyFirstPage() throws McException
    {
        final CommandSender sender = mock(CommandSender.class);
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final EmptyHandler dummy = new EmptyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[] {});
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedHeader, "EMPTY-HEADER", 1, 1); //$NON-NLS-1$
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * tests the first page.
     * 
     * @throws McException
     *             thrown on errors
     */
    @Test
    public void testEmptyFirstPage2() throws McException
    {
        final CommandSender sender = mock(CommandSender.class);
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final EmptyHandler dummy = new EmptyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[] { "1" }); //$NON-NLS-1$
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedHeader, "EMPTY-HEADER", 1, 1); //$NON-NLS-1$
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * Sample Command impl.
     */
    private static final class Command implements CommandInterface
    {
        
        /** sender. */
        private final CommandSender              sender;
        /** player. */
        private final McPlayerInterface          player;
        /** command. */
        private final org.bukkit.command.Command command;
        /** label. */
        private final String                     label;
        /** args. */
        private final String[]                   args;
        
        /**
         * Constructor.
         * 
         * @param sender
         *            sender
         * @param player
         *            player
         * @param command
         *            command
         * @param label
         *            label
         * @param args
         *            args
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
            return null;
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
            // dummy
            return null;
        }
        
        @Override
        public <T> Optional<T> fetch(McBiFunction<CommandInterface, String, T> mapper) throws McException
        {
            return Optional.empty();
        }
        
    }
    
    /**
     * Test Helper.
     */
    private static class DummyHandler extends AbstractPagableCommandHandler
    {
        
        /**
         * Constructor.
         */
        public DummyHandler()
        {
            // empty
        }
        
        @Override
        protected int getLineCount(CommandInterface command)
        {
            return 25;
        }
        
        @Override
        protected Serializable getHeader(CommandInterface command)
        {
            return "DUMMY-HEADER"; //$NON-NLS-1$
        }
        
        @Override
        protected Serializable[] getLines(CommandInterface command, int start, int count)
        {
            final List<Serializable> result = new ArrayList<>();
            for (int i = start; i < 25 && result.size() < count; i++)
            {
                result.add("Line " + (i + 1)); //$NON-NLS-1$
            }
            return result.toArray(new Serializable[result.size()]);
        }
        
    }
    
    /**
     * Test Helper.
     */
    private static class EmptyHandler extends AbstractPagableCommandHandler
    {
        
        /**
         * Constructor.
         */
        public EmptyHandler()
        {
            // empty
        }
        
        @Override
        protected int getLineCount(CommandInterface command)
        {
            return 0;
        }
        
        @Override
        protected Serializable getHeader(CommandInterface command)
        {
            return "EMPTY-HEADER"; //$NON-NLS-1$
        }
        
        @Override
        protected Serializable[] getLines(CommandInterface command, int start, int count)
        {
            return new Serializable[0];
        }
        
    }
    
}
