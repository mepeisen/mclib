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

package de.minigameslib.fakeplayer.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import de.minigameslib.fakeclient.nms110.FakeController110;
import de.minigameslib.fakeclient.nms111.FakeController111;
import de.minigameslib.fakeclient.nms112.FakeController112;
import de.minigameslib.mclib.fakeclient.IFakeClient;
import de.minigameslib.mclib.fakeclient.IFakeController;

/**
 * A factory to create fake players.
 * 
 * @author mepeisen
 */
public class FakeFactory
{
    
    /** singleton instance. */
    private static final FakeFactory INSTANCE = new FakeFactory();
    
    /** thread pool. */
    private final ExecutorService    pool     = Executors.newFixedThreadPool(2);
    
    /**
     * Fake factory constructor.
     */
    private FakeFactory()
    {
        // hidden
    }
    
    /**
     * Returns the fake factory instance.
     * 
     * @return fake factory.
     */
    public static FakeFactory instance()
    {
        return INSTANCE;
    }
    
    /**
     * Creates a fake player.
     * 
     * @param plugin
     *            the owning plugin
     * @param client
     *            the client simulator
     * @param name
     *            name of the player.
     * @return fake player.
     */
    public IFakeController createFakePlayer(Plugin plugin, IFakeClient client, String name)
    {
        IFakeController orig = null;
        final String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3]; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
        switch (version)
        {
            case "v1_8_R1": //$NON-NLS-1$
                // TODO
                break;
            case "v1_8_R2": //$NON-NLS-1$
                // TODO
                break;
            case "v1_8_R3": //$NON-NLS-1$
                // TODO
                break;
            case "v1_9_R1": //$NON-NLS-1$
                // TODO
                break;
            case "v1_9_R2": //$NON-NLS-1$
                // TODO
                break;
            case "v1_10_R1": //$NON-NLS-1$
                orig = new FakeController110(client, name);
                break;
            case "v1_11_R1": //$NON-NLS-1$
                orig = new FakeController111(client, name);
                break;
            case "v1_12_R1": //$NON-NLS-1$
                orig = new FakeController112(client, name);
                break;
            default:
                break;
        }
        
        return new FakeController(plugin, this.pool, orig);
    }
    
}
