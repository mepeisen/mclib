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

package de.minigameslib.mclib.test.impl.yml;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import de.minigameslib.mclib.impl.yml.YmlCommentableSection;
import de.minigameslib.mclib.impl.yml.YmlFile;

/**
 * Tests reading and writing yml files.
 * 
 * @author mepeisen
 */
public class YmlFileTest
{
    
    /** a simple config file. */
    private static final String FILE_1 =
            "# basic comment\n" //$NON-NLS-1$
            + "config:\n" //$NON-NLS-1$
            + "# other comment\n" //$NON-NLS-1$
            + "  other: 123\n" //$NON-NLS-1$
            + "# third comment\n" //$NON-NLS-1$
            + "  third: 234\n"; //$NON-NLS-1$
    
    /**
     * Reads file with some yml content
     * @throws IOException 
     */
    @Test
    public void readFile() throws IOException
    {
        final File temp = File.createTempFile("config", "yml"); //$NON-NLS-1$ //$NON-NLS-2$
        temp.deleteOnExit();
        Files.write(FILE_1, temp, Charsets.UTF_8);
        
        final YmlFile file = new YmlFile(temp);
        assertTrue(file.isSection("config")); //$NON-NLS-1$
        
        assertEquals(123, file.get("config.other")); //$NON-NLS-1$
        assertEquals(234, file.get("config.third")); //$NON-NLS-1$
        
        assertArrayEquals(new String[]{"basic comment"}, file.getComment("config")); //$NON-NLS-1$ //$NON-NLS-2$
        final YmlCommentableSection section = (YmlCommentableSection) file.getSection("config"); //$NON-NLS-1$
        assertArrayEquals(new String[]{"other comment"}, section.getComment("other")); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"third comment"}, section.getComment("third")); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Writes yml to file.
     * @throws IOException
     */
    public void writeFile() throws IOException
    {
        final File temp = File.createTempFile("config", "yml"); //$NON-NLS-1$ //$NON-NLS-2$
        temp.deleteOnExit();
        
        final YmlFile file = new YmlFile();
        file.set("config.other", 123); //$NON-NLS-1$
        file.set("config.third", 234); //$NON-NLS-1$
        file.setSectionComments(new String[]{"basic comment"}); //$NON-NLS-1$
        final YmlCommentableSection section = (YmlCommentableSection) file.getSection("config"); //$NON-NLS-1$
        section.setComment("other", new String[]{"basic comment"}); //$NON-NLS-1$ //$NON-NLS-2$
        section.setComment("third", new String[]{"basic comment"}); //$NON-NLS-1$ //$NON-NLS-2$
        
        file.saveFile(temp);
        final String content = Files.toString(temp, Charsets.UTF_8);
        assertEquals(FILE_1, content);
    }
    
}
