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

package de.minigameslib.mclib.impl.yml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import de.minigameslib.mclib.snakeyaml.Yaml;

/**
 * File config.
 * @author mepeisen
 */
public class YmlFile extends YmlCommentableSection
{
    
    /** yaml. */
    private final Yaml yml = new Yaml();
    
    /**
     * Constructor
     */
    public YmlFile()
    {
        // TODO
    }
    
    /**
     * Constructor
     * @param file
     * @throws IOException
     */
    public YmlFile(File file) throws IOException
    {
        // TODO
    }
    
    /**
     * Saves config to file.
     * @param file
     * @throws IOException
     */
    public void saveFile(File file) throws IOException
    {
        // TODO
    }

    /**
     * @param configFile
     */
    public void save(File configFile) throws IOException
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * @param fr
     */
    public void load(FileReader fr) throws IOException
    {
        // TODO Auto-generated method stub
        
    }
    
}
