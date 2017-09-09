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

package de.minigameslib.mclib3p.impl;

import java.util.function.BooleanSupplier;

import de.minigameslib.mclib3p.impl.placeholder.PlaceholderBridge;

/**
 * Enumeration representing third party plugins.
 * 
 * @author mepeisen
 */
public enum ThirdpartyPlugins
{
    /**
     * Bridge to placeholder api.
     */
    PlaceholderAPI("PlaceholderAPI", PlaceholderBridge::test, PlaceholderBridge::onSetup, PlaceholderBridge::onDisableMclib, PlaceholderBridge::onDisablePlugin); //$NON-NLS-1$
    
    /**
     * the plugin name this extension is dependeing on.
     */
    private final String          pluginName;
    
    /**
     * The tester function to check if extension is available. May do some setup checks.
     */
    private final BooleanSupplier tester;
    
    /**
     * Setup function to be executed on plugin enable.
     */
    private final Runnable        onSetup;
    
    /**
     * Function to run once mclib is disabled.
     */
    private final Runnable        onDisableMclib;
    
    /**
     * Function to run once the target plugin is disabled.
     */
    private final Runnable        onDisablePlugin;
    
    /**
     * Constructor.
     * 
     * @param pluginName
     *            the plugin name this extension is dependeing on
     * @param tester
     *            The tester function to check if extension is available
     * @param onSetup
     *            Setup function to be executed on plugin enable
     * @param onDisableMclib
     *            Function to run once mclib is disabled
     * @param onDisablePlugin
     *            Function to run once the target plugin is disabled
     */
    private ThirdpartyPlugins(String pluginName, BooleanSupplier tester, Runnable onSetup, Runnable onDisableMclib, Runnable onDisablePlugin)
    {
        this.pluginName = pluginName;
        this.tester = tester;
        this.onSetup = onSetup;
        this.onDisableMclib = onDisableMclib;
        this.onDisablePlugin = onDisablePlugin;
    }
    
    /**
     * Returns the plugin name this extension is dependeing on.
     * 
     * @return the pluginName
     */
    public String getPluginName()
    {
        return this.pluginName;
    }
    
    /**
     * Returns The tester function to check if extension is available.
     * 
     * @return the tester
     */
    public BooleanSupplier getTester()
    {
        return this.tester;
    }
    
    /**
     * Returns Setup function to be executed on plugin enable.
     * 
     * @return the onSetup
     */
    public Runnable getOnSetup()
    {
        return this.onSetup;
    }
    
    /**
     * Returns Function to run once mclib is disabled.
     * 
     * @return the onDisableMclib
     */
    public Runnable getOnDisableMclib()
    {
        return this.onDisableMclib;
    }
    
    /**
     * Returns Function to run once the target plugin is disabled.
     * 
     * @return the onDisablePlugin
     */
    public Runnable getOnDisablePlugin()
    {
        return this.onDisablePlugin;
    }
    
}
