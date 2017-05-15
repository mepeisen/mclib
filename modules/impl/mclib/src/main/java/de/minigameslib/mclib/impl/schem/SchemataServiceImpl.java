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

package de.minigameslib.mclib.impl.schem;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.zip.GZIPInputStream;

import de.minigameslib.mclib.api.schem.SchemataBuilderInterface;
import de.minigameslib.mclib.api.schem.SchemataInterface;
import de.minigameslib.mclib.api.schem.SchemataServiceInterface;

/**
 * Implementation of schema services.
 * 
 * @author mepeisen
 */
public class SchemataServiceImpl implements SchemataServiceInterface
{
    
    /** magic stream string. */
    static final String           STREAM_MAGIC = "http://www.minigameslib.de/schemata-stream-magic"; //$NON-NLS-1$
    
    /** the executor service to execute asynchronous tasks. */
    private final ExecutorService executorService;
    
    /**
     * Constructor.
     * 
     * @param executorService
     *            The executor service for asynchronous tasks.
     */
    public SchemataServiceImpl(ExecutorService executorService)
    {
        this.executorService = executorService;
    }
    
    @Override
    public SchemataInterface loadFromFile(File file) throws IOException
    {
        try (final FileInputStream fis = new FileInputStream(file))
        {
            return loadFromStream(fis);
        }
    }
    
    @Override
    public SchemataInterface loadFromStream(InputStream is) throws IOException
    {
        SchemataInterface schemata = null;
        try (final GZIPInputStream gzis = new GZIPInputStream(is))
        {
            try (final DataInputStream buffer = new DataInputStream(gzis))
            {
                if (!buffer.readUTF().equals(STREAM_MAGIC))
                {
                    throw new IOException("Invalid magic. Is this a valid mclib schemata?"); //$NON-NLS-1$
                }
                schemata = new SchemataImpl(this.executorService, buffer);
            }
        }
        return schemata;
    }
    
    @Override
    public SchemataBuilderInterface builder()
    {
        return new SchemataImpl(this.executorService);
    }
    
}
