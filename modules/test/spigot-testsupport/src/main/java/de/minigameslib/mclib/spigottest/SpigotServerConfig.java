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

package de.minigameslib.mclib.spigottest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.yaml.snakeyaml.Yaml;

import de.minigameslib.mclib.spigottest.SpigotTest.CustomPlugin;

/**
 * Configuration for spigot test servers.
 * 
 * @author mepeisen
 */
public class SpigotServerConfig
{
    
    /** logger. */
    static final Logger   LOGGER        = Logger.getLogger(SpigotServerConfig.class.getName());
    
    /** server version to be used. */
    private SpigotVersion serverVersion = SpigotVersion.Latest;
    
    /** the temporary directory used as server installation. */
    private File          tempDirectory;
    
    /**
     * A directory to store downloaded runtimes.
     */
    private File          runtimeDirectory;
    
    /**
     * The plugins to be used.
     */
    private List<Plugin>  plugins       = new ArrayList<>();
    
    /** the server type to be used. */
    private ServerType    serverType    = ServerType.Local;
    
    /** main game port. */
    private int mainPort = 25565;
    
    /** the world type for initialization. */
    private String defaultWorldType = "FLAT"; //$NON-NLS-1$
    
    /**
     * override server properties.
     */
    private Map<String, String> serverProperties = new HashMap<>();
    
    /** flag to control if local plugin was already added. */
    private boolean localPluginAdded = false;
    
    /**
     * Creates a spigot server for this config.
     * 
     * @return spigot server.
     * @throws IOException 
     */
    public SpigotServer create() throws IOException
    {
        if (this.tempDirectory == null)
        {
            this.tempDirectory = new File(".").getAbsoluteFile(); //$NON-NLS-1$
        }
        else if (!this.tempDirectory.exists())
        {
            this.tempDirectory.mkdirs();
        }
        
        if (this.runtimeDirectory == null)
        {
            this.runtimeDirectory = this.tempDirectory;
        }
        
        final SpigotServer server = new SpigotServer(this);
        return server;
    }

    /**
     * Override server properties
     * @param key
     * @param value
     * @return this object for chaining
     */
    public SpigotServerConfig setServerProperty(String key, String value)
    {
        this.serverProperties.put(key, value);
        return this;
    }
    
    /**
     * @return the serverProperties
     */
    public Map<String, String> getServerProperties()
    {
        return this.serverProperties;
    }

    /**
     * @return the mainPort
     */
    public int getMainPort()
    {
        return this.mainPort;
    }

    /**
     * @param mainPort the mainPort to set
     * @return this object for chaining
     */
    public SpigotServerConfig setMainPort(int mainPort)
    {
        this.mainPort = mainPort;
        return this;
    }

    /**
     * @return the defaultWorldType
     */
    public String getDefaultWorldType()
    {
        return this.defaultWorldType;
    }

    /**
     * @param defaultWorldType the defaultWorldType to set
     * @return this object for chaining
     */
    public SpigotServerConfig setDefaultWorldType(String defaultWorldType)
    {
        this.defaultWorldType = defaultWorldType;
        return this;
    }

    /**
     * @return the serverType
     */
    public ServerType getServerType()
    {
        return this.serverType;
    }

    /**
     * @param serverType the serverType to set
     * @return this object for chaining
     */
    public SpigotServerConfig setServerType(ServerType serverType)
    {
        this.serverType = serverType;
        return this;
    }

    /**
     * @return the serverVersion
     */
    public SpigotVersion getServerVersion()
    {
        return this.serverVersion;
    }
    
    /**
     * @param serverVersion
     *            the serverVersion to set
     * @return this object for chaining
     */
    public SpigotServerConfig setServerVersion(SpigotVersion serverVersion)
    {
        this.serverVersion = serverVersion;
        return this;
    }
    
    /**
     * @return the tempDirectory
     */
    public File getTempDirectory()
    {
        return this.tempDirectory;
    }
    
    /**
     * @param tempDirectory
     *            the tempDirectory to set
     * @return this object for chaining
     */
    public SpigotServerConfig setTempDirectory(File tempDirectory)
    {
        this.tempDirectory = tempDirectory;
        return this;
    }
    
    /**
     * @return the runtimeDirectory
     */
    public File getRuntimeDirectory()
    {
        return this.runtimeDirectory;
    }
    
    /**
     * @param runtimeDirectory
     *            the runtimeDirectory to set
     * @return this object for chaining
     */
    public SpigotServerConfig setRuntimeDirectory(File runtimeDirectory)
    {
        this.runtimeDirectory = runtimeDirectory;
        return this;
    }
    
    /**
     * @return the list of available plugins.
     */
    public Iterable<Plugin> getPlugins()
    {
        return this.plugins;
    }
    
    /**
     * Adds a jar file as plugin.
     * 
     * @param jarFile
     * @return this object for chaining
     */
    public SpigotServerConfig addJarPlugin(File jarFile)
    {
        this.plugins.add(new JarPlugin(jarFile));
        return this;
    }
    
    /**
     * Adds a new plugin from one or multiple classpath entries
     * 
     * @param builder
     * @return this object for chaining
     */
    public SpigotServerConfig addClasspathPlugin(Consumer<ClasspathPluginBuilder> builder)
    {
        final ClasspathPluginBuilder cpb = new ClasspathPluginBuilder();
        builder.accept(cpb);
        this.plugins.add(cpb.build());
        return this;
    }

    /**
     * Adds the plugin from local classpath
     * @return this object for chaining
     */
    public SpigotServerConfig addLocalPlugin()
    {
        if (!this.localPluginAdded)
        {
            this.plugins.add(new LocalPlugin());
        }
        return this;
    }

    /**
     * Adds a custom plugin
     * @param plugin
     */
    public void addCustomPlugin(CustomPlugin plugin)
    {
        this.plugins.add(new CustomPlugin_(plugin));
    }
    
    /**
     * Returns the root for given url
     * @param url
     * @param segmentCount
     * @return root
     * @throws MalformedURLException
     */
    static File getRootForCpFile(URL url, int segmentCount) throws MalformedURLException
    {
        final String surl = url.toString();
        if (surl.startsWith("file:")) //$NON-NLS-1$
        {
            File file = new File(surl.substring(5));
            for (int i = 0; i < segmentCount; i++)
            {
                file = file.getParentFile();
            }
            return file;
        }
        if (surl.startsWith("jar:file:")) //$NON-NLS-1$
        {
            return new File(surl.substring(9, surl.lastIndexOf('!')));
        }
        throw new IllegalArgumentException("unknown url type: " + url); //$NON-NLS-1$
    }
    
    /**
     * A builder to create classpath plugins.
     * 
     * @author mepeisen
     */
    public static final class ClasspathPluginBuilder
    {
        
        /** the plugin name. */
        private String           pluginFilename;
        
        /** the plugin classes folder. */
        private File             classesFolder;
        
        /** the additional cp */
        private final List<File> additionalCP = new ArrayList<>();
        
        /**
         * @return the pluginFilename
         */
        public String getPluginFilename()
        {
            return this.pluginFilename;
        }
        
        /**
         * @param pluginFilename
         *            the pluginFilename to set
         * @return this object for chaining
         */
        public ClasspathPluginBuilder setPluginFilename(String pluginFilename)
        {
            this.pluginFilename = pluginFilename;
            return this;
        }
        
        /**
         * @return the classesFolder
         */
        public File getClassesFolder()
        {
            return this.classesFolder;
        }
        
        /**
         * @param classesFolder
         *            the classesFolder to set
         * @return this object for chaining
         */
        public ClasspathPluginBuilder setClassesFolder(File classesFolder)
        {
            this.classesFolder = classesFolder;
            return this;
        }
        
        /**
         * @return the additionalCP
         */
        public Iterable<File> getAdditionalCP()
        {
            return this.additionalCP;
        }
        
        /**
         * @param folder
         * @return this object for chaining
         */
        public ClasspathPluginBuilder addAdditionalCP(File folder)
        {
            this.additionalCP.add(folder);
            return this;
        }
        
        /**
         * Creates the plugin.
         * 
         * @return plugin
         */
        ClasspathPlugin build()
        {
            return new ClasspathPlugin(this.pluginFilename, this.classesFolder, this.additionalCP.toArray(new File[this.additionalCP.size()]));
        }
    }
    
    /**
     * Plugin base class.
     * 
     * @author mepeisen
     */
    public static abstract class Plugin
    {
        /**
         * Prepares the jar file in given install folder (plugins sub folder)
         * 
         * @param installFolder
         */
        abstract void prepare(File installFolder);
    }
    
    /**
     * Local plugin class
     * @author mepeisen
     */
    static class LocalPlugin extends Plugin
    {
        
        /**
         * Constructor
         */
        public LocalPlugin()
        {
            // empty
        }
        
        @SuppressWarnings("unchecked")
        @Override
        void prepare(File installFolder)
        {
            LOGGER.log(Level.FINE, "creating local plugin files"); //$NON-NLS-1$
            try
            {
                final Enumeration<URL> urls = SpigotServerConfig.class.getClassLoader().getResources("plugin.yml"); //$NON-NLS-1$
                while (urls.hasMoreElements())
                {
                    final URL url = urls.nextElement();
                    final Yaml yaml = new Yaml();
                    Map<String, Object> values = null;
                    try (final InputStream is = url.openStream())
                    {
                        values = (Map<String, Object>) yaml.load(is);
                    }
                    final String name = values.get("name").toString(); //$NON-NLS-1$
                    if (name == null)
                    {
                        throw new IllegalStateException("invalid plugin.yml at " + url); //$NON-NLS-1$
                    }
                    final Properties properties = new Properties();
                    properties.setProperty("pluginYml", url.toString()); //$NON-NLS-1$
                    properties.setProperty("cp", getRootForCpFile(url, 1).toString()); //$NON-NLS-1$
                    try (final FileOutputStream fos = new FileOutputStream(new File(installFolder, name + ".localplugin"))) //$NON-NLS-1$
                    {
                        properties.store(fos, ""); //$NON-NLS-1$
                    }
                }
            }
            catch (IOException ex)
            {
                throw new IllegalStateException(ex);
            }
            LOGGER.log(Level.FINE, "created local plugin files"); //$NON-NLS-1$
        }
        
    }
    
    /**
     * custom plugin class
     * @author mepeisen
     */
    static class CustomPlugin_ extends Plugin
    {
        
        /** custom plugin annotation data */
        private CustomPlugin custom;

        /**
         * Constructor
         * @param custom the annotation data
         */
        public CustomPlugin_(CustomPlugin custom)
        {
            this.custom = custom;
        }
        
        @Override
        void prepare(File installFolder)
        {
            LOGGER.log(Level.FINE, "creating custom plugin file for " + this.custom.name()); //$NON-NLS-1$

            final Properties properties = new Properties();
            properties.setProperty("name", this.custom.name()); //$NON-NLS-1$
            properties.setProperty("main", this.custom.pluginClass().getName()); //$NON-NLS-1$
            final String className = this.custom.pluginClass().getName();
            final URL url = SpigotServerConfig.class.getClassLoader().getResource(className.replace('.', '/').concat(".class")); //$NON-NLS-1$
            try (final FileOutputStream fos = new FileOutputStream(new File(installFolder, this.custom.name() + ".localplugin"))) //$NON-NLS-1$
            {
                properties.setProperty("cp", getRootForCpFile(url, className.length() - className.replace(".", "").length()).toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                properties.store(fos, ""); //$NON-NLS-1$
            }
            catch (IOException ex)
            {
                throw new IllegalStateException(ex);
            }
            LOGGER.log(Level.FINE, "created local plugin files"); //$NON-NLS-1$
        }
        
    }
    
    /**
     * Jar file plugins.
     * 
     * @author mepeisen
     */
    static class JarPlugin extends Plugin
    {
        
        /** the plugin jar file. */
        private final File jarFile;
        
        /**
         * Constructor
         * 
         * @param jarFile
         */
        public JarPlugin(File jarFile)
        {
            this.jarFile = jarFile;
        }
        
        /**
         * @return the jarFile
         */
        public File getJarFile()
        {
            return this.jarFile;
        }
        
        @Override
        void prepare(File installFolder)
        {
            LOGGER.log(Level.FINE, "copying " + this.jarFile + " to " + installFolder); //$NON-NLS-1$//$NON-NLS-2$
            try (FileInputStream fis = new FileInputStream(this.jarFile))
            {
                try (final FileChannel src = fis.getChannel())
                {
                    try (final FileOutputStream fos = new FileOutputStream(new File(installFolder, this.jarFile.getName())))
                    {
                        try (final FileChannel dst = fos.getChannel())
                        {
                            src.transferTo(0, this.jarFile.length(), dst);
                        }
                    }
                }
            }
            catch (IOException ex)
            {
                throw new IllegalStateException(ex);
            }
            LOGGER.log(Level.FINE, "copied " + this.jarFile + " to " + installFolder); //$NON-NLS-1$//$NON-NLS-2$
        }
        
    }
    
    /**
     * Classpath plugins.
     * 
     * @author mepeisen
     */
    static class ClasspathPlugin extends Plugin
    {
        
        /** the plugin name. */
        private final String     pluginFilename;
        
        /** the plugin classes folder. */
        private final File       classesFolder;
        
        /** the additional cp */
        private final List<File> additionalCP = new ArrayList<>();
        
        /**
         * Constructor
         * 
         * @param pluginFilename
         * @param classesFolder
         * @param additionalCP
         */
        public ClasspathPlugin(String pluginFilename, File classesFolder, File... additionalCP)
        {
            this.pluginFilename = pluginFilename;
            this.classesFolder = classesFolder;
            if (additionalCP != null)
            {
                for (final File additional : additionalCP)
                {
                    this.additionalCP.add(additional);
                }
            }
        }
        
        /**
         * @return the pluginFilename
         */
        public String getPluginFilename()
        {
            return this.pluginFilename;
        }
        
        /**
         * @return the classesFolder
         */
        public File getClassesFolder()
        {
            return this.classesFolder;
        }
        
        /**
         * @return the additionalCP
         */
        public Iterable<File> getAdditionalCP()
        {
            return this.additionalCP;
        }
        
        @Override
        void prepare(File installFolder)
        {
            LOGGER.log(Level.FINE, "building " + this.pluginFilename + " in " + installFolder); //$NON-NLS-1$//$NON-NLS-2$
            final File dst = new File(installFolder, this.pluginFilename);
            final Manifest manifest = new Manifest();
            manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0"); //$NON-NLS-1$
            try (final FileOutputStream fos = new FileOutputStream(dst))
            {
                try (final JarOutputStream jos = new JarOutputStream(fos, manifest))
                {
                    addDir(jos, this.classesFolder);
                    for (final File folder : this.additionalCP)
                    {
                        addDir(jos, folder);
                    }
                }
            }
            catch (IOException ex)
            {
                throw new IllegalStateException(ex);
            }
            LOGGER.log(Level.FINE, "built " + this.pluginFilename + " in " + installFolder); //$NON-NLS-1$//$NON-NLS-2$
        }
        
        /**
         * Adds a directory to jar file.
         * 
         * @param jos
         * @param folder
         * @throws IOException
         */
        private void addDir(JarOutputStream jos, File folder) throws IOException
        {
            for (final File nested : folder.listFiles())
            {
                if (nested.isDirectory())
                {
                    addDir(jos, nested, "/" + nested.getName() + "/"); //$NON-NLS-1$//$NON-NLS-2$
                }
                else if (nested.isFile())
                {
                    addFile(jos, nested, "/" + nested.getName()); //$NON-NLS-1$
                }
            }
        }
        
        /**
         * Adds a directory to jar file.
         * 
         * @param jos
         * @param folder
         * @param relpath
         * @throws IOException
         */
        private void addDir(JarOutputStream jos, File folder, String relpath) throws IOException
        {
            final JarEntry entry = new JarEntry(relpath);
            entry.setTime(folder.lastModified());
            jos.putNextEntry(entry);
            jos.closeEntry();
            
            for (final File nested : folder.listFiles())
            {
                if (nested.isDirectory())
                {
                    addDir(jos, nested, relpath + nested.getName() + "/"); //$NON-NLS-1$
                }
                else if (nested.isFile())
                {
                    addFile(jos, nested, relpath + nested.getName());
                }
            }
        }
        
        /**
         * Adds a file to jar file.
         * 
         * @param jos
         * @param file
         * @param relpath
         * @throws IOException
         */
        private void addFile(JarOutputStream jos, File file, String relpath) throws IOException
        {
            final JarEntry entry = new JarEntry(relpath);
            entry.setTime(file.lastModified());
            jos.putNextEntry(entry);
            try (final FileInputStream fis = new FileInputStream(file))
            {
                try (final BufferedInputStream in = new BufferedInputStream(fis))
                {
                    byte[] buffer = new byte[256 * 256];
                    while (true)
                    {
                        int count = in.read(buffer);
                        if (count == -1)
                            break;
                        jos.write(buffer, 0, count);
                    }
                }
            }
            
            jos.closeEntry();
        }
        
    }
    
}
