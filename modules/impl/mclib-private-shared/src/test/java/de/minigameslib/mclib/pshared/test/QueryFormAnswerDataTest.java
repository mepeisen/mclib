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

package de.minigameslib.mclib.pshared.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.minigameslib.mclib.pshared.QueryFormAnswerData;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Test case for {@link QueryFormAnswerData}
 * 
 * @author mepeisen
 *
 */
public class QueryFormAnswerDataTest
{
    
    /**
     * Simple test case for reading/storing data
     */
    @Test
    public void testMe()
    {
        final QueryFormAnswerData data = new QueryFormAnswerData();
        data.setWinId("winid"); //$NON-NLS-1$
        data.setInputId("inputid"); //$NON-NLS-1$
        data.getData().set("datakey", "datavalue"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("winid", data.getWinId()); //$NON-NLS-1$
        assertEquals("inputid", data.getInputId()); //$NON-NLS-1$
        assertEquals("datavalue", data.getData().get("datakey")); //$NON-NLS-1$ //$NON-NLS-2$

        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        
        final QueryFormAnswerData data2 = section.getFragment(QueryFormAnswerData.class, "FOO"); //$NON-NLS-1$
        assertEquals("winid", data2.getWinId()); //$NON-NLS-1$
        assertEquals("inputid", data2.getInputId()); //$NON-NLS-1$
        assertEquals("datavalue", data2.getData().get("datakey")); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
}
