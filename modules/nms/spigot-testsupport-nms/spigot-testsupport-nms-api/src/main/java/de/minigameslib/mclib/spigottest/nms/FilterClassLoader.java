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

package de.minigameslib.mclib.spigottest.nms;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

/**
 * A class loader filtering classes from given url.
 * 
 * @author mepeisen
 */
public class FilterClassLoader extends ClassLoader
{
    
    /** debug flag for class loader debug output. */
    private static final boolean DEBUG = false;

    /** urls that will be filtered from classpath. */
    private URL[] filters;
    
    /**
     * @param filters
     * @param parent 
     */
    public FilterClassLoader(URL[] filters, ClassLoader parent)
    {
        super(parent);
        this.filters = filters;
    }
    
    /**
     * Adds an url to be filtered.
     * @param url
     */
    public void addFilterUrl(URL url)
    {
        final URL[] newFilters = Arrays.copyOf(this.filters, this.filters.length + 1);
        newFilters[this.filters.length] = url;
        this.filters = newFilters;
    }
    
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
    {
        final String path = name.replace('.', '/').concat(".class"); //$NON-NLS-1$
        for (final URL filter : this.filters)
        {
            try
            {
                final URL url = filter.toURI().resolve(path).toURL();
                if (DEBUG) System.out.println("Check " + name + " against " + url); //$NON-NLS-1$ //$NON-NLS-2$
                try (final InputStream is = url.openStream())
                {
                    // file exists; filter it
                    throw new ClassNotFoundException();
                }
            }
            catch (@SuppressWarnings("unused") URISyntaxException | IOException ex)
            {
                // silently ignore
            }
        }
        return super.loadClass(name, resolve);
    }
    
}
