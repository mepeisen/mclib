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

package de.minigameslib.mclib.api.schem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * A service to manage schematas.
 * 
 * @author mepeisen
 */
public interface SchemataServiceInterface
{
    
    /**
     * Returns the schemata services instance.
     * 
     * @return schemata services instance.
     */
    static SchemataServiceInterface instance()
    {
        return SchemataServiceCache.get();
    }
    
    /**
     * Loads a schemata from file.
     * 
     * @param file
     *            file to read.
     * @return schemata
     * @throws IOException
     *             thrown on read errors.
     */
    SchemataInterface loadFromFile(File file) throws IOException;
    
    /**
     * Loads a schemata from strema.
     * 
     * @param is
     *            input stream
     * @return schemata
     * @throws IOException
     *             thrown on read errors.
     */
    SchemataInterface loadFromStream(InputStream is) throws IOException;
    
    /**
     * Creates a schemata builder for creating new schemata.
     * 
     * @return schemata builder
     */
    SchemataBuilderInterface builder();
    
}
