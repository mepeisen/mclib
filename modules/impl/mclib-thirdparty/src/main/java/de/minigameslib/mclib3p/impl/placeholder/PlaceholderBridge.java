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

package de.minigameslib.mclib3p.impl.placeholder;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.ext.ExtensionServiceInterface;
import de.minigameslib.mclib.api.locale.MessageServiceInterface.Placeholder;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.impl.hooks.PlaceholderExtension;
import de.minigameslib.mclib3p.impl.Mclib3pPlugin;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;

/**
 * Bridge between mclib and placerholder api.
 * 
 * @author mepeisen
 *
 */
public class PlaceholderBridge implements PlaceholderExtension
{
    
    /** the registered placeholders in placeholder-api. */
    private final Map<String, MclibPlaceholder> placeholders = new HashMap<>();
    
    /** singletone instance. */
    private static final PlaceholderBridge INSTANCE = new PlaceholderBridge();
    
    /**
     * Check for target plugin.
     * @return {@code true} if target plugin is available.
     */
    public static boolean test()
    {
        // TODO version check etc.
        // should work with 2.0.6 and higher
        return true;
    }
    
    /**
     * Setup Bridge.
     */
    public static void onSetup()
    {
        ExtensionServiceInterface.instance().register(Mclib3pPlugin.INSTANCE, PlaceholderExtension.POINT, PlaceholderBridge.INSTANCE);
    }
    
    /**
     * On disable mclib.
     */
    public static void onDisableMclib()
    {
        PlaceholderAPI.unregisterPlaceholderHook(Mclib3pPlugin.INSTANCE);
        INSTANCE.placeholders.clear();
    }
    
    /**
     * On disable target plugin.
     */
    public static void onDisablePlugin()
    {
        ExtensionServiceInterface.instance().remove(Mclib3pPlugin.INSTANCE, PlaceholderExtension.POINT, PlaceholderBridge.INSTANCE);
    }

    @Override
    public void registerPlaceholders(Plugin plugin, String prefix, Placeholder placeholder)
    {
        this.placeholders.computeIfAbsent(prefix, MclibPlaceholder::new);
    }

    @Override
    public void unregisterPlaceholders(Plugin plugin, String prefix, Placeholder placeholder)
    {
        // ignore, maybe it could remove from placeholder-api but currently the api does not support it
    }

    @Override
    public void notifyPlaceholderChanges(String[][] parray)
    {
        // ignore
    }

    @Override
    public String replacePlaceholder(String placeholder, String prefix, String[] args)
    {
        final PlaceholderHook hook = PlaceholderAPI.getPlaceholders().get(prefix);
        if (hook != null)
        {
            final McPlayerInterface player = McLibInterface.instance().getCurrentPlayer();
            return hook.onPlaceholderRequest(player == null ? null : player.getBukkitPlayer(), String.join("_", args)); //$NON-NLS-1$
        }
        return null;
    }
    
}
