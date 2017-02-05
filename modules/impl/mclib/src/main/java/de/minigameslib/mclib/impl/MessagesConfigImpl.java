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

package de.minigameslib.mclib.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessagesConfigInterface;
import de.minigameslib.mclib.impl.yml.YmlFile;

/**
 * Implementation of messages config interface
 * 
 * @author mepeisen
 */
class MessagesConfigImpl implements MessagesConfigInterface
{
    
    // TODO check if there are messages that are not needed any more.
    // remove them from file. But be aware of path variable substitution.
    
    // TODO support data versions and migrations
    
    /** the file configuration. */
    private YmlFile                         config      = null;
    /** the yml file. */
    private File                            file        = null;
    /** the java plugin. */
    private Plugin                          plugin      = null;
    /** the enum services. */
    private EnumServiceImpl                 enumService = null;
    
    /** the defaults for this messages. */
    private List<LocalizedMessageInterface> defaults    = new ArrayList<>();
    
    /**
     * Constructor.
     * 
     * @param plugin
     * @param enumService
     */
    public MessagesConfigImpl(Plugin plugin, EnumServiceImpl enumService)
    {
        this.plugin = plugin;
        this.enumService = enumService;
        this.defaults.addAll(this.enumService.getEnumValues(plugin, LocalizedMessageInterface.class));
    }
    
    @Override
    public String getString(Locale locale, String path, String defaultValue)
    {
        final YmlFile config1 = this.getConfig();
        String result = config1.getString(path + ".user." + (locale == null ? McLibInterface.instance().getDefaultLocale().toString() : locale.toString())); //$NON-NLS-1$
        if (result == null)
        {
            final String defaultLocale = config1.getString(path + ".default_locale"); //$NON-NLS-1$
            if (defaultLocale != null)
            {
                result = config1.getString(path + ".user." + defaultLocale); //$NON-NLS-1$
            }
        }
        return result == null ? defaultValue : result;
    }
    
    @Override
    public String getAdminString(Locale locale, String path, String defaultValue)
    {
        final YmlFile config1 = this.getConfig();
        String result = config1.getString(path + ".admin." + (locale == null ? McLibInterface.instance().getDefaultLocale().toString() : locale.toString())); //$NON-NLS-1$
        if (result == null)
        {
            final String defaultLocale = config1.getString(path + ".default_locale"); //$NON-NLS-1$
            if (defaultLocale != null)
            {
                result = config1.getString(path + ".admin." + defaultLocale); //$NON-NLS-1$
            }
        }
        return result == null ? defaultValue : result;
    }
    
    @Override
    public String[] getStringList(Locale locale, String path, String[] defaultValue)
    {
        final YmlFile config1 = this.getConfig();
        List<String> result = config1.getStringList(path + ".user." + (locale == null ? McLibInterface.instance().getDefaultLocale().toString() : locale.toString())); //$NON-NLS-1$
        if (result == null)
        {
            final String defaultLocale = config1.getString(path + ".default_locale"); //$NON-NLS-1$
            if (defaultLocale != null)
            {
                result = config1.getStringList(path + ".user." + defaultLocale); //$NON-NLS-1$
            }
        }
        return result == null ? defaultValue : result.toArray(new String[result.size()]);
    }
    
    @Override
    public String[] getAdminStringList(Locale locale, String path, String[] defaultValue)
    {
        final YmlFile config1 = this.getConfig();
        List<String> result = config1.getStringList(path + ".admin." + (locale == null ? McLibInterface.instance().getDefaultLocale().toString() : locale.toString())); //$NON-NLS-1$
        if (result == null)
        {
            final String defaultLocale = config1.getString(path + ".default_locale"); //$NON-NLS-1$
            if (defaultLocale != null)
            {
                result = config1.getStringList(path + ".admin." + defaultLocale); //$NON-NLS-1$
            }
        }
        return result == null ? defaultValue : result.toArray(new String[result.size()]);
    }
    
    /**
     * Returns the file configuration.
     * 
     * @return file configuration.
     */
    YmlFile getConfig()
    {
        if (this.config == null)
        {
            try
            {
                this.reloadConfig();
            }
            catch (final IOException ex)
            {
                this.plugin.getLogger().log(Level.WARNING, "Cannot reload messages configuration", ex); //$NON-NLS-1$
            }
        }
        return this.config;
    }
    
    /**
     * Saves the configuration.
     */
    void saveConfig()
    {
        if (this.config == null || this.file == null)
        {
            return;
        }
        try
        {
            this.getConfig().saveFile(this.file);
        }
        catch (final IOException ex)
        {
            this.plugin.getLogger().log(Level.WARNING, "Cannot save messages configuration", ex); //$NON-NLS-1$
        }
    }
    
    /**
     * Reloads the configuration file.
     * @throws IOException 
     */
    void reloadConfig() throws IOException
    {
        // TODO support comments.
        if (this.file == null)
        {
            this.file = new File(this.plugin.getDataFolder(), "messages.yml"); //$NON-NLS-1$
        }
        if (this.file.exists())
        {
            this.config = new YmlFile(this.file);
        }
        else
        {
            this.config = new YmlFile();
        }
        
        // add the defaults.
        for (final LocalizedMessageInterface msg : this.defaults)
        {
            try
            {
                final LocalizedMessages clazzDef = msg.getClass().getAnnotation(LocalizedMessages.class);
                final LocalizedMessage valueDef = msg.getClass().getDeclaredField(msg.name()).getAnnotation(LocalizedMessage.class);
                final LocalizedMessageList listDef = msg.getClass().getDeclaredField(msg.name()).getAnnotation(LocalizedMessageList.class);
                if (clazzDef == null || (listDef == null && valueDef == null))
                {
                    throw new IllegalStateException("Invalid message class."); //$NON-NLS-1$
                }
                
                if (valueDef == null && listDef != null)
                {
                    final String path = clazzDef.value() + '.' + msg.name();
                    if (!this.config.contains(path))
                    {
                        this.config.set(path + ".default_locale", clazzDef.defaultLocale()); //$NON-NLS-1$
                        this.config.setPrimitiveList(path + ".user." + clazzDef.defaultLocale(), Arrays.asList(listDef.value())); //$NON-NLS-1$
                        if (listDef.adminMessages().length > 0)
                        {
                            this.config.setPrimitiveList(path + ".admin." + clazzDef.defaultLocale(), Arrays.asList(listDef.adminMessages())); //$NON-NLS-1$
                        }
                    }
                }
                else if (valueDef != null)
                {
                    final String path = clazzDef.value() + '.' + msg.name();
                    this.config.set(path + ".default_locale", clazzDef.defaultLocale()); //$NON-NLS-1$
                    this.config.set(path + ".user." + clazzDef.defaultLocale(), valueDef.defaultMessage()); //$NON-NLS-1$
                    if (valueDef.defaultAdminMessage().length() > 0)
                    {
                        this.config.set(path + ".admin." + clazzDef.defaultLocale(), valueDef.defaultAdminMessage()); //$NON-NLS-1$
                    }
                }
            }
            catch (NoSuchFieldException ex)
            {
                throw new IllegalStateException(ex);
            }
        }
        
        this.config.saveFile(this.file);
        this.saveConfig();
    }
    
}
