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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.List;
import java.util.Locale;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.cmd.AbstractCompositeCommandHandler;
import de.minigameslib.mclib.api.cmd.CommandImpl;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.HelpCommandHandler;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageServiceInterface;
import de.minigameslib.mclib.api.locale.MessagesConfigInterface;

/**
 * Test case for {@link HelpCommandHandler}
 * 
 * @author mepeisen
 */
public class HelpCommandHandlerTest
{
    
    /** msg services. */
    private MessageServiceInterface msgService;
    /** api services. */
    private McLibInterface lib;
    /** messages interface. */
    private MessagesConfigInterface messages;
    
    /**
     * Tests the tab complete.
     * @throws McException 
     */
    @Test
    public void testTabComplete() throws McException
    {
        final HelpCommandHandler help = genHelp();
        
        final CommandSender sender = mock(CommandSender.class);
        final Command command = mock(Command.class);
        final CommandInterface cmd = new CommandImpl(sender, command, "help", new String[]{}, "help"); //$NON-NLS-1$ //$NON-NLS-2$

        final List<String> completions = help.onTabComplete(cmd, ""); //$NON-NLS-1$
        assertEquals(25, completions.size());
        for (int i = 0; i < 25; i++)
        {
            assertTrue(completions.contains("sub" + (i+1))); //$NON-NLS-1$
        }
    }
    
    /**
     * Tests the tab complete.
     * @throws McException 
     */
    @Test
    public void testTabComplete2() throws McException
    {
        final HelpCommandHandler help = genHelp();
        
        final CommandSender sender = mock(CommandSender.class);
        final Command command = mock(Command.class);
        final CommandInterface cmd = new CommandImpl(sender, command, "help", new String[]{}, "help"); //$NON-NLS-1$ //$NON-NLS-2$

        final List<String> completions = help.onTabComplete(cmd, "sub"); //$NON-NLS-1$
        assertEquals(25, completions.size());
        for (int i = 0; i < 25; i++)
        {
            assertTrue(completions.contains("sub" + (i+1))); //$NON-NLS-1$
        }
    }
    
    /**
     * Tests the tab complete.
     * @throws McException 
     */
    @Test
    public void testTabComplete3() throws McException
    {
        final HelpCommandHandler help = genHelp();
        
        final CommandSender sender = mock(CommandSender.class);
        final Command command = mock(Command.class);
        final CommandInterface cmd = new CommandImpl(sender, command, "help", new String[]{}, "help"); //$NON-NLS-1$ //$NON-NLS-2$

        final List<String> completions = help.onTabComplete(cmd, "sub2"); //$NON-NLS-1$
        assertEquals(7, completions.size());
        assertTrue(completions.contains("sub2")); //$NON-NLS-1$
        for (int i = 19; i < 25; i++)
        {
            assertTrue(completions.contains("sub" + (i+1))); //$NON-NLS-1$
        }
    }
    
    /**
     * Tests the help command.
     * @throws McException 
     */
    @Test
    public void testReturnsHelpForSubCommand() throws McException
    {
        final HelpCommandHandler help = genHelp();
        
        final CommandSender sender = mock(CommandSender.class);
        final Command command = mock(Command.class);
        final CommandInterface cmd = new CommandImpl(sender, command, "help", new String[]{"sub1"}, "help"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

//        doAnswer(new Answer<Void>(){
//
//            @Override
//            public Void answer(InvocationOnMock invocation) throws Throwable
//            {
//                System.out.println(invocation.getArgumentAt(0, String.class));
//                return null;
//            }
//            
//        }).when(sender).sendMessage(anyString());
        
        help.handle(cmd);
        
        verify(sender, atLeast(1)).isOp();
        verify(sender, times(1)).sendMessage("§f§7=====§9help§7====§9Page §11 §9 from §12§7====="); //$NON-NLS-1$
        for (int i = 0; i < 10; i++)
        {
            verify(sender, times(1)).sendMessage("§f §7line " + (i + 1)); //$NON-NLS-1$
        }
        verifyNoMoreInteractions(sender);
    }
    
    /**
     * Tests the help command list.
     * @throws McException 
     */
    @Test
    public void testReturnsCommandList() throws McException
    {
        final HelpCommandHandler help = genHelp();
        
        final CommandSender sender = mock(CommandSender.class);
        final Command command = mock(Command.class);
        final CommandInterface cmd = new CommandImpl(sender, command, "help", new String[]{}, "help"); //$NON-NLS-1$ //$NON-NLS-2$

//        doAnswer(new Answer<Void>(){
//
//            @Override
//            public Void answer(InvocationOnMock invocation) throws Throwable
//            {
//                System.out.println(invocation.getArgumentAt(0, String.class));
//                return null;
//            }
//            
//        }).when(sender).sendMessage(anyString());
        
        help.handle(cmd);
        
        verify(sender, atLeast(1)).isOp();
        verify(sender, times(1)).sendMessage("§f§7=====§9help§7====§9Page §11 §9 from §13§7====="); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub1 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub10 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub11 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub12 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub13 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub14 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub15 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub16 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub17 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub18 §7SingleLine"); //$NON-NLS-1$
        verifyNoMoreInteractions(sender);
    }
    
    /**
     * Tests the help command list.
     * @throws McException 
     */
    @Test
    public void testReturnsCommandListPage2() throws McException
    {
        final HelpCommandHandler help = genHelp();
        
        final CommandSender sender = mock(CommandSender.class);
        final Command command = mock(Command.class);
        final CommandInterface cmd = new CommandImpl(sender, command, "help", new String[]{"2"}, "help"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

//        doAnswer(new Answer<Void>(){
//
//            @Override
//            public Void answer(InvocationOnMock invocation) throws Throwable
//            {
//                System.out.println(invocation.getArgumentAt(0, String.class));
//                return null;
//            }
//            
//        }).when(sender).sendMessage(anyString());
        
        help.handle(cmd);
        
        verify(sender, atLeast(1)).isOp();
        verify(sender, times(1)).sendMessage("§f§7=====§9help§7====§9Page §12 §9 from §13§7====="); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub19 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub2 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub20 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub21 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub22 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub23 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub24 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub25 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub3 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub4 §7SingleLine"); //$NON-NLS-1$
        verifyNoMoreInteractions(sender);
    }
    
    /**
     * Tests the help command list.
     * @throws McException 
     */
    @Test
    public void testReturnsCommandListPage3() throws McException
    {
        final HelpCommandHandler help = genHelp();
        
        final CommandSender sender = mock(CommandSender.class);
        final Command command = mock(Command.class);
        final CommandInterface cmd = new CommandImpl(sender, command, "help", new String[]{"3"}, "help"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

//        doAnswer(new Answer<Void>(){
//
//            @Override
//            public Void answer(InvocationOnMock invocation) throws Throwable
//            {
//                System.out.println(invocation.getArgumentAt(0, String.class));
//                return null;
//            }
//            
//        }).when(sender).sendMessage(anyString());
        
        help.handle(cmd);
        
        verify(sender, atLeast(1)).isOp();
        verify(sender, times(1)).sendMessage("§f§7=====§9help§7====§9Page §13 §9 from §13§7====="); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub5 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub6 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub7 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub8 §7SingleLine"); //$NON-NLS-1$
        verify(sender, times(1)).sendMessage("§f §7§9sub9 §7SingleLine"); //$NON-NLS-1$
        verifyNoMoreInteractions(sender);
    }
    
    /**
     * Tests the help command.
     * @throws McException 
     */
    @Test
    public void testReturnsHelpForSubCommandSecondPage() throws McException
    {
        final HelpCommandHandler help = genHelp();
        
        final CommandSender sender = mock(CommandSender.class);
        final Command command = mock(Command.class);
        final CommandInterface cmd = new CommandImpl(sender, command, "help", new String[]{"sub1", "2"}, "help"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        
//        doAnswer(new Answer<Void>(){
//
//            @Override
//            public Void answer(InvocationOnMock invocation) throws Throwable
//            {
//                System.out.println(invocation.getArgumentAt(0, String.class));
//                return null;
//            }
//            
//        }).when(sender).sendMessage(anyString());
        
        help.handle(cmd);
        
        verify(sender, atLeast(1)).isOp();
        verify(sender, times(1)).sendMessage("§f§7=====§9help§7====§9Page §12 §9 from §12§7====="); //$NON-NLS-1$
        for (int i = 10; i < 20; i++)
        {
            verify(sender, times(1)).sendMessage("§f §7line " + (i + 1)); //$NON-NLS-1$
        }
        verifyNoMoreInteractions(sender);
    }
    
    /**
     * Tests the help command.
     * @throws McException 
     */
    @Test
    public void testReturnsHelpForUnknownCommand() throws McException
    {
        final HelpCommandHandler help = genHelp();
        
        final CommandSender sender = mock(CommandSender.class);
        final Command command = mock(Command.class);
        final CommandInterface cmd = new CommandImpl(sender, command, "help", new String[]{"unknown"}, "help"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        help.handle(cmd);
        
        verify(sender, times(1)).sendMessage("§4Unknown command §9help unknown"); //$NON-NLS-1$
    }
    
    /**
     * Tests the help command.
     * @throws McException 
     */
    @Test
    public void testReturnsHelpForInvisibleCommand() throws McException
    {
        final HelpCommandHandler help = genHelp();
        
        final CommandSender sender = mock(CommandSender.class);
        final Command command = mock(Command.class);
        final CommandInterface cmd = new CommandImpl(sender, command, "help", new String[]{"invis1"}, "help"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        help.handle(cmd);
        
        verify(sender, times(1)).sendMessage("§4Unknown command §9help invis1"); //$NON-NLS-1$
    }

    /**
     * Generate help command
     * @return help command.
     */
    private HelpCommandHandler genHelp()
    {
        final DummyComposite composite = new DummyComposite();
        for (int i = 0; i < 25; i++)
        {
            final SubCommandHandlerInterface sub1 = mock(SubCommandHandlerInterface.class);
            when(sub1.getDescription(any(CommandInterface.class))).thenReturn(Msgs.MultiLine);
            when(sub1.getShortDescription(any(CommandInterface.class))).thenReturn(Msgs.SingleLine);
            when(sub1.visible(any(CommandInterface.class))).thenReturn(true);
            composite.injectSubCommand("sub" + (i + 1), sub1); //$NON-NLS-1$
            
            final SubCommandHandlerInterface invis1 = mock(SubCommandHandlerInterface.class);
            when(invis1.getDescription(any(CommandInterface.class))).thenReturn(Msgs.MultiLine);
            when(invis1.getShortDescription(any(CommandInterface.class))).thenReturn(Msgs.SingleLine);
            when(invis1.visible(any(CommandInterface.class))).thenReturn(false);
            composite.injectSubCommand("invis" + (i + 1), invis1); //$NON-NLS-1$
        }
        final HelpCommandHandler result = new HelpCommandHandler(composite);
        return result;
    }
    
    /**
     * Init test case
     * @throws ClassNotFoundException
     */
    @Before
    public void init() throws ClassNotFoundException
    {
        this.msgService = mock(MessageServiceInterface.class);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.locale.MessageServiceCache"), "SERVICES", this.msgService); //$NON-NLS-1$ //$NON-NLS-2$
        this.messages = mock(MessagesConfigInterface.class);
        when(this.msgService.getMessagesFromMsg(anyObject())).thenReturn(this.messages);
        when(this.messages.getStringList(any(Locale.class), anyString(), any())).thenAnswer(new Answer<String[]>() {

            @Override
            public String[] answer(InvocationOnMock invocation) throws Throwable
            {
                return invocation.getArgumentAt(2, String[].class);
            }
        });
        when(this.messages.getString(any(Locale.class), anyString(), anyString())).thenAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable
            {
                return invocation.getArgumentAt(2, String.class);
            }
        });
        
        this.lib = mock(McLibInterface.class);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.McLibCache"), "SERVICES", this.lib); //$NON-NLS-1$ //$NON-NLS-2$
        when(this.lib.getDefaultLocale()).thenReturn(Locale.ENGLISH);
    }
    
    /**
     * Dummy helper
     */
    private static final class DummyComposite extends AbstractCompositeCommandHandler
    {

        /**
         * Constructor
         */
        public DummyComposite()
        {
            // empty
        }

        @Override
        protected void sendUsage(CommandInterface command)
        {
            // empty
        }
        
    }
    
    /**
     * Messages helper
     * @author mepeisen
     */
    @LocalizedMessages(value = "test")
    private static enum Msgs implements LocalizedMessageInterface
    {
        
        /**
         * A multi line.
         */
        @LocalizedMessageList({
            "line 1",
            "line 2",
            "line 3",
            "line 4",
            "line 5",
            "line 6",
            "line 7",
            "line 8",
            "line 9",
            "line 10",
            "line 11",
            "line 12",
            "line 13",
            "line 14",
            "line 15",
            "line 16",
            "line 17",
            "line 18",
            "line 19",
            "line 20"
        })
        MultiLine,
        
        /**
         * A single line.
         */
        @LocalizedMessage(defaultMessage = "SingleLine")
        SingleLine,
        
    }
    
}
