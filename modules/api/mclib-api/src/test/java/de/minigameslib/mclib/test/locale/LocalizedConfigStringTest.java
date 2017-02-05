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

package de.minigameslib.mclib.test.locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

import de.minigameslib.mclib.api.locale.LocalizedConfigString;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * test case for {@link LocalizedConfigString}
 * 
 * @author mepeisen
 */
public class LocalizedConfigStringTest
{
    
    /**
     * Tests the argument substitution
     */
    @Test
    public void testArgs()
    {
        final LocalizedConfigString line = new LocalizedConfigString();
        line.setUserMessage(Locale.ENGLISH, "foo %2$s %1$s"); //$NON-NLS-1$
        assertEquals("foo bar baz", line.toUserMessage(Locale.GERMAN, "baz", "bar")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    /**
     * Tests {@link LocalizedConfigString#write(de.minigameslib.mclib.shared.api.com.DataSection)}
     */
    @Test
    public void testConfig()
    {
        final LocalizedConfigString line = new LocalizedConfigString();
        line.setUserMessage(Locale.ENGLISH, "foo"); //$NON-NLS-1$
        line.setUserMessage(Locale.GERMAN, "foo2"); //$NON-NLS-1$
        line.setAdminMessage(Locale.ENGLISH, "foo3"); //$NON-NLS-1$
        line.setAdminMessage(Locale.GERMAN, ""); //$NON-NLS-1$
        
        final MemoryDataSection section = new MemoryDataSection();
        line.write(section);
        
        assertEquals("en", section.getString("default_locale")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("foo", section.getString("user.en")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("foo2", section.getString("user.de")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("foo3", section.getString("admin.en")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("", section.getString("admin.de")); //$NON-NLS-1$ //$NON-NLS-2$
        
        final LocalizedConfigString line2 = new LocalizedConfigString();
        line2.read(section);
        
        assertEquals("foo", line2.toUserMessage(Locale.ENGLISH)); //$NON-NLS-1$
        assertEquals("foo2", line2.toUserMessage(Locale.GERMAN)); //$NON-NLS-1$
        assertEquals("foo3", line2.toAdminMessage(Locale.ENGLISH)); //$NON-NLS-1$
        assertEquals("foo2", line2.toAdminMessage(Locale.GERMAN)); //$NON-NLS-1$
    }
    
    /**
     * Tests the {@link LocalizedConfigString#toUserMessage(java.util.Locale, java.io.Serializable...)}
     */
    @Test
    public void testToUserMessage()
    {
        // empty message object
        final LocalizedConfigString line = new LocalizedConfigString();
        assertEquals("", line.toUserMessage(Locale.GERMAN)); //$NON-NLS-1$
        
        // setting default locale
        line.setUserMessage(Locale.ENGLISH, "foo"); //$NON-NLS-1$
        assertEquals("foo", line.toUserMessage(Locale.ENGLISH)); //$NON-NLS-1$
        assertEquals("foo", line.toUserMessage(Locale.GERMAN)); //$NON-NLS-1$
        
        // setting german
        line.setUserMessage(Locale.GERMAN, "foo2"); //$NON-NLS-1$
        assertEquals("foo", line.toUserMessage(Locale.ENGLISH)); //$NON-NLS-1$
        assertEquals("foo2", line.toUserMessage(Locale.GERMAN)); //$NON-NLS-1$
        
        // clearing german
        line.setUserMessage(Locale.GERMAN, null);
        assertEquals("foo", line.toUserMessage(Locale.ENGLISH)); //$NON-NLS-1$
        assertEquals("foo", line.toUserMessage(Locale.GERMAN)); //$NON-NLS-1$
    }
    
    /**
     * Tests the {@link LocalizedConfigString#toAdminMessage(java.util.Locale, java.io.Serializable...)}
     */
    @Test
    public void testToAdminMessage()
    {
        // empty message object
        final LocalizedConfigString line = new LocalizedConfigString();
        assertEquals("", line.toAdminMessage(Locale.GERMAN)); //$NON-NLS-1$
        
        // setting default locale
        line.setAdminMessage(Locale.ENGLISH, "foo"); //$NON-NLS-1$
        assertEquals("foo", line.toAdminMessage(Locale.ENGLISH)); //$NON-NLS-1$
        assertEquals("foo", line.toAdminMessage(Locale.GERMAN)); //$NON-NLS-1$
        
        // setting german
        line.setAdminMessage(Locale.GERMAN, "foo2"); //$NON-NLS-1$
        assertEquals("foo", line.toAdminMessage(Locale.ENGLISH)); //$NON-NLS-1$
        assertEquals("foo2", line.toAdminMessage(Locale.GERMAN)); //$NON-NLS-1$
        
        // clearing german
        line.setAdminMessage(Locale.GERMAN, null);
        assertEquals("foo", line.toAdminMessage(Locale.ENGLISH)); //$NON-NLS-1$
        assertEquals("foo", line.toAdminMessage(Locale.GERMAN)); //$NON-NLS-1$
    }
    
    /**
     * Tests the {@link LocalizedConfigString#toAdminMessage(java.util.Locale, java.io.Serializable...)}
     */
    @Test
    public void testToAdminMessageReturningUserMsg()
    {
        // empty message object
        final LocalizedConfigString line = new LocalizedConfigString();
        line.setUserMessage(Locale.ENGLISH, "foo"); //$NON-NLS-1$
        line.setAdminMessage(Locale.GERMAN, "foo2"); //$NON-NLS-1$
        assertEquals("foo", line.toAdminMessage(Locale.ENGLISH)); //$NON-NLS-1$
        assertEquals("foo2", line.toAdminMessage(Locale.GERMAN)); //$NON-NLS-1$

        line.setAdminMessage(Locale.GERMAN, ""); //$NON-NLS-1$
        assertEquals("foo", line.toAdminMessage(Locale.GERMAN)); //$NON-NLS-1$

        line.setUserMessage(Locale.ENGLISH, "foo3"); //$NON-NLS-1$
        line.setAdminMessage(Locale.ENGLISH, ""); //$NON-NLS-1$
        assertEquals("foo3", line.toAdminMessage(Locale.GERMAN)); //$NON-NLS-1$
    }
    
    /**
     * Tests standard methods.
     */
    @Test
    public void testMe()
    {
        final LocalizedConfigString line = new LocalizedConfigString();
        
        assertTrue(line.isSingleLine());
        assertFalse(line.isMultiLine());
        
        assertEquals(MessageSeverityType.Information, line.getSeverity());
    }
    
    /**
     * Tests {@link LocalizedConfigString#toAdminMessageLine(java.util.Locale, java.io.Serializable...)}
     */
    @Test(expected = IllegalStateException.class)
    public void testToAdminMsgLine()
    {
        new LocalizedConfigString().toAdminMessageLine(Locale.GERMAN);
    }
    
    /**
     * Tests {@link LocalizedConfigString#toUserMessageLine(java.util.Locale, java.io.Serializable...)}
     */
    @Test(expected = IllegalStateException.class)
    public void testToUserMsgLine()
    {
        new LocalizedConfigString().toUserMessageLine(Locale.GERMAN);
    }
    
}
