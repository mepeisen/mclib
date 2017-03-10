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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Annotation for spigot test support.
 * 
 * @author mepeisen
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface SpigotTest
{
    
    /**
     * Returns the version to test against.
     * @return spigot versions for testing.
     */
    SpigotVersion[] versions() default {SpigotVersion.Latest};
    
    /**
     * Flag to test against all known spigot versions.
     * @return {@code true} if all versions will be tested (overrides {@link #versions()}).
     */
    boolean all() default false;
    
    /**
     * Flag to load the local plugin from classpath
     * @return {@code true} for loading local plugin from classpath; assumes that we are testing within plugin code.
     */
    boolean loadLocalPlugin() default true;
    
    /**
     * Custom plugins to be loaded.
     * The custom plugins do not have their own plugin.yml.
     * They will be loaded after other localPlugins (if {@link #loadLocalPlugin()} is true) in order of the returned array.
     * @return list of custom plugins.
     */
    CustomPlugin[] customPlugins() default {};
    
    /**
     * Returns the world configuration.
     * @return world configuration.
     */
    WorldConfig world() default @WorldConfig();
    
    /**
     * Custom server properties for server.properties file.
     * @return custom server properties
     */
    ServerProperty[] serverProperties() default {};
    
    /**
     * Port number for spigot server
     * @return minecraft port number
     */
    int port() default 25565;
    
    /**
     * Resets the plugin files
     * @return {@code true} to reset the plugin files at startup
     */
    boolean resetPluginFiles() default false;
    
    /**
     * Annotation for loading custom plugins.
     */
    public @interface CustomPlugin
    {
        /**
         * Name of the plugin.
         * @return plugin name.
         */
        String name();
        
        /**
         * Plugin class to be loaded.
         * @return plugin class.
         */
        Class<? extends JavaPlugin> pluginClass();
    }
    
    /**
     * Annotation for spigot test world config.
     */
    public @interface WorldConfig
    {
        
        /**
         * Flag to reset the world on test startup
         * @return {@code true} to reset/delete the world on test startup.
         */
        boolean reset() default false;
        
        /**
         * Returns the default world type.
         * @return world type.
         */
        String worldType() default "FLAT";
        
    }
    
    /**
     * Annotation for custom server properties.
     */
    public @interface ServerProperty
    {
        /**
         * Property name.
         * @return property name.
         */
        String name();
        
        /**
         * Property value.
         * @return property value.
         */
        String value();
    }
    
}
