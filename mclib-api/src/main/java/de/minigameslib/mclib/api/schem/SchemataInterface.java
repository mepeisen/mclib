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
import java.io.OutputStream;

import org.bukkit.Location;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.locale.LocalizedConfigString;
import de.minigameslib.mclib.api.util.function.McRunnable;

/**
 * Interface for schemata.
 * 
 * @author mepeisen
 */
public interface SchemataInterface
{
    
    /** url for random schemata/ default url. */
    String RANDOM_SCHEMATA = "http://www.minigameslib.de/random-schemata"; //$NON-NLS-1$
    
    /**
     * Returns the localized schemata title.
     * 
     * @return localized schemata title.
     */
    LocalizedConfigString getTitle();
    
    /**
     * Returns the author.
     * 
     * @return author.
     */
    String getAuthor();
    
    /**
     * Returns a unique url to identify the schemata.
     * 
     * @return unique url.
     */
    String getSchemataUrl();
    
    /**
     * Returns a numeric version of this schemata.
     * 
     * @return numeric version of schemata
     */
    int getSchemataVersion();
    
    /**
     * Returns the api version used to build the schemata.
     * 
     * @return api version
     * 
     * @see McLibInterface#getApiVersion()
     */
    int getApiVersion();
    
    /**
     * returns the number of parts within this schemata.
     * 
     * @return number of parts.
     */
    int getPartCount();
    
    /**
     * Returns the part with given index.
     * 
     * @param index
     *            first part is 0, second part is 1 etc.
     * @return part or {@code null} if part does not exist.
     */
    SchemataPartInterface getPart(int index);
    
    /**
     * Apply schemata to world (asynchronous).
     * 
     * @param locations
     *            locations to use; first location will be for first part, second location for second part etc. The locations represent the bottom cornor of the schemata.
     * @param success
     *            function to invoke on success
     * @param failure
     *            function to invoke on failures
     * @throws McException
     *             thrown if there are locations missing or if the schemata is not valid
     */
    void applyToWorld(Location[] locations, McRunnable success, McRunnable failure) throws McException;
    
    /**
     * Saves this schemata to given output stream.
     * 
     * @param os
     *            target output stream
     * @throws IOException
     *             io errors upon save
     */
    void saveToStream(OutputStream os) throws IOException;
    
    /**
     * Saves this schemata to given file.
     * 
     * @param file
     *            target file
     * @throws IOException
     *             io errors upon save
     */
    void saveToFile(File file) throws IOException;
    
}
