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

package de.minigameslib.mclib.spigottest.nms.v19;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.yaml.snakeyaml.error.YAMLException;

import de.minigameslib.mclib.spigottest.nms.FilterableClassLoader;

/**
 * @author mepeisen
 *
 */
public class LocalPluginLoader implements PluginLoader
{
    
    /** the server. */
    private Server                server;
    
    /** the plugin file filters. */
    private final Pattern[]       fileFilters = { Pattern.compile("\\.localplugin$") }; //$NON-NLS-1$
    
    private Map<String, Class<?>> classes;
    private Map<String, URLClassLoader>  loaders;
    
    private JavaPluginLoader      javaLoader;
    
    /**
     * Constructor
     * 
     * @param instance
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public LocalPluginLoader(Server instance) throws Exception
    {
        Validate.notNull(instance, "Server cannot be null"); //$NON-NLS-1$
        this.server = instance;
        
        final PluginManager mng = ((ExtendedPluginManager) Bukkit.getServer().getPluginManager()).getDelegate();
        final Field fileAssocField = mng.getClass().getDeclaredField("fileAssociations"); //$NON-NLS-1$
        fileAssocField.setAccessible(true);
        final Map<Pattern, PluginLoader> fileAssociations = (Map<Pattern, PluginLoader>) fileAssocField.get(mng);
        
        for (final PluginLoader loader : fileAssociations.values())
        {
            if (loader instanceof JavaPluginLoader)
            {
                this.javaLoader = (JavaPluginLoader) loader;
                final Field classesField = JavaPluginLoader.class.getDeclaredField("classes"); //$NON-NLS-1$
                classesField.setAccessible(true);
                this.classes = (Map<String, Class<?>>) classesField.get(this.javaLoader);
                
                final Field loadersField = JavaPluginLoader.class.getDeclaredField("loaders"); //$NON-NLS-1$
                loadersField.setAccessible(true);
                this.loaders = (Map<String, URLClassLoader>) loadersField.get(this.javaLoader);
            }
        }
        
        Validate.notNull(this.javaLoader, "javaLoader cannot be null"); //$NON-NLS-1$
        Validate.notNull(this.classes, "classes cannot be null"); //$NON-NLS-1$
        Validate.notNull(this.loaders, "loaders cannot be null"); //$NON-NLS-1$
    }
    
    @Override
    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, Plugin plugin)
    {
        return this.javaLoader.createRegisteredListeners(listener, plugin);
    }
    
    @Override
    public void disablePlugin(Plugin plugin)
    {
        this.javaLoader.disablePlugin(plugin);
    }
    
    @Override
    public void enablePlugin(Plugin plugin)
    {
        this.javaLoader.enablePlugin(plugin);
    }
    
    @Override
    public Pattern[] getPluginFileFilters()
    {
        return this.fileFilters.clone();
    }
    
    /**
     * Reads properties from eclipse-project file.
     * 
     * @param file
     * @return properties.
     * @throws IOException
     */
    private Properties fetchProperties(File file) throws IOException
    {
        final Properties properties = new Properties();
        try (final InputStream is = new FileInputStream(file))
        {
            properties.load(is);
        }
        return properties;
    }
    
    @SuppressWarnings("resource")
    @Override
    public Plugin loadPlugin(File file) throws InvalidPluginException, UnknownDependencyException
    {
        Validate.notNull(file, "File cannot be null");
        
        if (!(file.exists()))
        {
            throw new InvalidPluginException(new FileNotFoundException(file.getPath() + " does not exist"));
        }
        PluginDescriptionFile description;
        try
        {
            description = getPluginDescription(file);
        }
        catch (InvalidDescriptionException ex)
        {
            throw new InvalidPluginException(ex);
        }
        File parentFile = file.getParentFile();
        File dataFolder = new File(parentFile, description.getName());
        
        File oldDataFolder = new File(parentFile, description.getRawName());
        
        if (!(dataFolder.equals(oldDataFolder)))
        {
            if ((dataFolder.isDirectory()) && (oldDataFolder.isDirectory()))
            {
                this.server.getLogger().warning(
                        String.format("While loading %s (%s) found old-data folder: `%s' next to the new one `%s'", new Object[] { description.getFullName(), file, oldDataFolder, dataFolder }));
            }
            else if ((oldDataFolder.isDirectory()) && (!(dataFolder.exists())))
            {
                if (!(oldDataFolder.renameTo(dataFolder)))
                {
                    throw new InvalidPluginException("Unable to rename old data folder: `" + oldDataFolder + "' to: `" + dataFolder + "'");
                }
                this.server.getLogger().log(Level.INFO,
                        String.format("While loading %s (%s) renamed data folder: `%s' to `%s'", new Object[] { description.getFullName(), file, oldDataFolder, dataFolder }));
            }
        }
        
        if ((dataFolder.exists()) && (!(dataFolder.isDirectory())))
        {
            throw new InvalidPluginException(String.format("Projected datafolder: `%s' for %s (%s) exists and is not a directory", new Object[] { dataFolder, description.getFullName(), file }));
        }
        
        for (String pluginName : description.getDepend())
        {
            Plugin current = this.server.getPluginManager().getPlugin(pluginName);
            
            if (current == null)
            {
                throw new UnknownDependencyException(pluginName);
            }
        }
        URLClassLoader loader;
        Plugin plugin;
        try
        {
            final Class<? extends URLClassLoader> clazz = Class.forName("org.bukkit.plugin.java.PluginClassLoader").asSubclass(URLClassLoader.class); //$NON-NLS-1$
            final Constructor<? extends URLClassLoader> ctor = clazz.getDeclaredConstructor(JavaPluginLoader.class, ClassLoader.class, PluginDescriptionFile.class, File.class, File.class);
            final ClassLoader appLoader = this.javaLoader.getClass().getClassLoader();
            ctor.setAccessible(true);
            final Properties props = fetchProperties(file);
            final URL url = new URL(props.getProperty("pluginYml")); //$NON-NLS-1$
            final File classesFolder = Paths.get(url.toURI()).toFile().getParentFile();
            final File testClassesFolder = new File(Paths.get(url.toURI()).toFile().getParentFile().getParentFile(), "test-classes"); //$NON-NLS-1$
            ((FilterableClassLoader)appLoader).addFilterUrl(classesFolder.toURI().toURL());
            ((FilterableClassLoader)appLoader).addFilterUrl(testClassesFolder.toURI().toURL());
            loader = ctor.newInstance(this.javaLoader, appLoader, description, dataFolder, classesFolder);
            final Method addUrl = URLClassLoader.class.getDeclaredMethod("addURL", URL.class); //$NON-NLS-1$
            addUrl.setAccessible(true);
            addUrl.invoke(loader, testClassesFolder.toURI().toURL());
            
            final Field field = clazz.getDeclaredField("plugin"); //$NON-NLS-1$
            field.setAccessible(true);
            plugin = (Plugin) field.get(loader);
        }
        catch (Throwable ex)
        {
            throw new InvalidPluginException(ex);
        }
        this.loaders.put(plugin.getName(), loader);
        
        return plugin;
    }
    
    @Override
    public PluginDescriptionFile getPluginDescription(File file) throws InvalidDescriptionException
    {
        Validate.notNull(file, "File cannot be null"); //$NON-NLS-1$
        
        try
        {
            final Properties props = fetchProperties(file);
            final URL url = new URL(props.getProperty("pluginYml")); //$NON-NLS-1$
            
            try (final InputStream is = url.openStream())
            {
                return new PluginDescriptionFile(is);
            }
        }
        catch (IOException | YAMLException ex)
        {
            throw new InvalidDescriptionException(ex);
        }
    }
    
}
