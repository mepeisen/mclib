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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.fakeclient.IFakeController;

/**
 * Fake controller implementation.
 * 
 * @author mepeisen
 */
class FakeController implements IFakeController
{
    
    /** executors pool. */
    private ExecutorService pool;
    /** original underlying nms class. */
    private IFakeController orig;

    /**
     * Constructor.
     * @param pool
     * @param orig
     */
    public FakeController(ExecutorService pool, IFakeController orig)
    {
        this.pool = pool;
        this.orig = orig;
    }

    @Override
    public void connect(Plugin plugin, Location spawn)
    {
        Bukkit.getScheduler().runTaskLater(plugin, () -> this.orig.connect(plugin, spawn), 0);
    }
    
    @Override
    public void disconnect()
    {
        this.pool.execute(() -> this.orig.disconnect());
    }
    
    @Override
    public Player getPlayer()
    {
        return this.orig.getPlayer();
    }
    
    @Override
    public OfflinePlayer getOfflinePlayer()
    {
        return this.orig.getOfflinePlayer();
    }
    
    @Override
    public void say(String msg)
    {
        this.pool.execute(() -> this.orig.say(msg));
    }
    
}
