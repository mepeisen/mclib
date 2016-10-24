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
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * The spighot class loader.
 * 
 * <p>
 * A special variant of URLClassloader for test support. Preferrs classes from given urls instead of parent class loader.
 * </p>
 * 
 * @author mepeisen
 */
public class SpigotClassLoader extends ClassLoader implements FilterableClassLoader
{
    
    /** debug flag for class loader debug output. */
    private static final boolean DEBUG = false;
    
    /**
     * The packages to always load from parent class loader.
     */
    String[]             parentPackages;
    
    /**
     * The real child class loader.
     */
    private ChildClassLoader     childClassLoader;

    /** urls that will be filtered from classpath. */
    URL[] filters = new URL[0];
    
    @Override
    public void addFilterUrl(URL url)
    {
        final URL[] newFilters = Arrays.copyOf(this.filters, this.filters.length + 1);
        newFilters[this.filters.length] = url;
        this.filters = newFilters;
    }
    
    /**
     * @param parentPackages
     * @param urls
     */
    public SpigotClassLoader(URL[] urls, String[] parentPackages)
    {
        super(Thread.currentThread().getContextClassLoader());
        this.parentPackages = parentPackages;
        this.childClassLoader = new ChildClassLoader(urls, new DetectClassLoader(this.getParent()));
    }
    
    /**
     * @author mepeisen
     */
    private static final class DetectClassLoader extends ClassLoader
    {
        
        /**
         * @param parent
         */
        public DetectClassLoader(ClassLoader parent)
        {
            super(parent);
        }
        
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException
        {
            if (DEBUG)
                System.out.println("DetectClassLoader.findClass " + name); //$NON-NLS-1$
            return super.findClass(name);
        }
        
    }
    /**
     * @author mepeisen
     */
    private final class ChildClassLoader extends URLClassLoader implements FilterableClassLoader
    {
        
        /**
         * parent class loader.
         */
        private ClassLoader realParent;
        
        /**
         * @param urls
         * @param parent
         */
        public ChildClassLoader(URL[] urls, ClassLoader parent)
        {
            super(urls, null);
            this.realParent = parent;
        }
        
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException
        {
            boolean isParent = false;
            for (final String parentPkg : SpigotClassLoader.this.parentPackages)
            {
                if (name.startsWith(parentPkg))
                {
                    isParent = true;
                    break;
                }
            }
            
            final String path = name.replace('.', '/').concat(".class"); //$NON-NLS-1$
            for (final URL filter : SpigotClassLoader.this.filters)
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
            
            if (isParent)
            {
                if (DEBUG)
                    System.out.println("isParent->realParent.findClass " + name); //$NON-NLS-1$
                return this.realParent.loadClass(name);
            }
            
            try
            {
                if (DEBUG)
                    System.out.println("ChildClassLoader->findLoadedClass " + name); //$NON-NLS-1$
                Class<?> loaded = super.findLoadedClass(name);
                if (loaded != null)
                    return loaded;
                if (DEBUG)
                    System.out.println("ChildClassLoader->findClass " + name); //$NON-NLS-1$
                return super.findClass(name);
            }
            catch (@SuppressWarnings("unused") ClassNotFoundException ex)
            {
                if (DEBUG)
                    System.out.println("!isParent->realParent.findClass " + name); //$NON-NLS-1$
                return this.realParent.loadClass(name);
            }
        }
        
        @Override
        public URL getResource(String name)
        {
            if (DEBUG)
                System.out.println("super.getResource " + name); //$NON-NLS-1$
            URL url = super.getResource(name);
            if (url == null)
            {
                if (DEBUG)
                    System.out.println("realParent.getResource " + name); //$NON-NLS-1$
                url = this.realParent.getResource(name);
            }
            return url;
        }
        
        @Override
        public Enumeration<URL> getResources(String name) throws IOException
        {
            return super.getResources(name);
        }
        
        @Override
        public void addFilterUrl(URL url)
        {
            SpigotClassLoader.this.addFilterUrl(url);
        }
        
    }
    
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
    {
        boolean isParent = false;
        for (final String parentPkg : this.parentPackages)
        {
            if (name.startsWith(parentPkg))
            {
                isParent = true;
                break;
            }
        }
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
        synchronized (getClassLoadingLock(name))
        {
            if (isParent)
            {
                if (DEBUG)
                    System.out.println("isParent->SpigotClassLoader2->loadClass " + name); //$NON-NLS-1$
                return super.loadClass(name, resolve);
            }
            
            try
            {
                if (DEBUG)
                    System.out.println("isParent->SpigotClassLoader2->childClassLoader " + name); //$NON-NLS-1$
                return this.childClassLoader.findClass(name);
            }
            catch (@SuppressWarnings("unused") ClassNotFoundException e)
            {
                if (DEBUG)
                    System.out.println("!isParent->SpigotClassLoader2->loadClass " + name); //$NON-NLS-1$
                return super.loadClass(name, resolve);
            }
        }
    }
    
    @Override
    public URL getResource(String name)
    {
        // TODO Check filterUrl
        if (DEBUG)
            System.out.println("childClassLoader.getResource " + name); //$NON-NLS-1$
        URL url = this.childClassLoader.getResource(name);
        if (url == null)
        {
            if (DEBUG)
                System.out.println("super.getResource " + name); //$NON-NLS-1$
            url = super.getResource(name);
        }
        return url;
    }
    
    @Override
    public Enumeration<URL> getResources(String name) throws IOException
    {
        // TODO Check filterUrl
        return this.childClassLoader.getResources(name);
    }
    
}
