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

package de.minigameslib.mclib.shared.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * Testing AnnotatedDataFragment
 * 
 * @author mepeisen
 */
public class AnnotatedDataFragmentTest
{
    
    /**
     * Simple test case.
     */
    @Test
    public void testStoreAndReload()
    {
        final Data1 d1 = new Data1();
        d1.int2 = 123;
        d1.str1 = "FOO"; //$NON-NLS-1$
        final Data2 d2 = new Data2();
        d2.int2 = 1234;
        d2.str1 = "FOO1"; //$NON-NLS-1$
        d2.int3 = Integer.valueOf(12);
        d1.lst4.add(d2);
        final MemoryDataSection section = new MemoryDataSection();
        d1.write(section.createSection("d1"));
        
        final Data1 res = section.getFragment(Data1.class, "d1");
        assertEquals(d1.int2, res.int2);
        assertEquals(d1.int3, res.int3);
        assertEquals(d1.str1, res.str1);
        assertEquals(1, res.lst4.size());
        assertEquals(d1.lst4.get(0).int2, res.lst4.get(0).int2);
        assertEquals(d1.lst4.get(0).int3, res.lst4.get(0).int3);
        assertEquals(d1.lst4.get(0).str1, res.lst4.get(0).str1);
    }
    
    public static class Data1 extends AnnotatedDataFragment
    {
        @PersistentField
        public String str1;
        @PersistentField
        public int int2;
        @PersistentField
        public Integer int3;
        @PersistentField
        public List<Data2> lst4 = new ArrayList<>();
    }
    
    public static class Data2 extends AnnotatedDataFragment
    {
        @PersistentField
        public String str1;
        @PersistentField
        public int int2;
        @PersistentField
        public Integer int3;
    }
    
}
