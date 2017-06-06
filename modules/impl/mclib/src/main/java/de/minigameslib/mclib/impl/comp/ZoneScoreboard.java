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

package de.minigameslib.mclib.impl.comp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.locale.MessageServiceInterface;
import de.minigameslib.mclib.api.locale.MessageServiceInterface.PlaceholderListener;
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.ZoneScoreboardVariant;
import de.minigameslib.mclib.impl.McPlayerImpl;

/**
 * Zone scoreboards.
 * 
 * @author mepeisen
 *
 */
public class ZoneScoreboard implements ZoneScoreboardVariant, PlaceholderListener
{
    
    /** text lines. */
    private final List<Serializable> lines             = new ArrayList<>();
    
    /** the hidden flag. */
    private boolean                  hidden            = true;
    
    /** whitelist. */
    private final Set<UUID>          whitelist         = new HashSet<>();
    
    /** name of the scoreboard. */
    private final String             name;
    
    /**
     * the current cuboid.
     */
    private Cuboid                   cuboid;
    
    /**
     * Players currently seeing this scoreboard.
     */
    private final Set<UUID>          associatedPlayers = new HashSet<>();
    
    /**
     * Constructor.
     * 
     * @param name
     *            name of the scoreboard
     * @param cuboid
     *            the current zone cuboid
     */
    public ZoneScoreboard(String name, Cuboid cuboid)
    {
        this.name = name;
        this.cuboid = cuboid;
        MessageServiceInterface.instance().registerPlaceholderListener(
            (Plugin)McLibInterface.instance(), new String[][]{{}}, this);
    }

    @Override
    public void handleChangedPlaceholder(String[][] placeholders)
    {
        // TODO performance: only listen for interesting placeholders
        this.updateLines();
    }
    
    /**
     * Show scoreboard to given player.
     * 
     * @param player
     *            player uuid
     */
    public void showToPlayer(McPlayerImpl player)
    {
        this.associatedPlayers.add(player.getPlayerUUID());
        player.setScoreboard(this);
    }
    
    /**
     * Remove scoreboard from given player.
     * 
     * @param player
     *            player uuid.
     */
    public void removeFromPlayer(McPlayerImpl player)
    {
        this.associatedPlayers.remove(player.getPlayerUUID());
        player.removeScoreboard(this);
    }
    
    @Override
    public void updateLines()
    {
        this.associatedPlayers.stream()
            .map(p -> (McPlayerImpl) ObjectServiceInterface.instance().getPlayer(p))
            .forEach(McPlayerImpl::updateScoreboard);
    }
    
    @Override
    public void setLines(Serializable... strings)
    {
        this.lines.clear();
        this.lines.addAll(Arrays.asList(strings));
        this.updateLines();
    }
    
    @Override
    public List<Serializable> getLines()
    {
        return new ArrayList<>(this.lines);
    }
    
    @Override
    public void addLine(Serializable string)
    {
        this.lines.add(string);
        this.updateLines();
    }
    
    @Override
    public void removeLine(int lineNumber)
    {
        this.lines.remove(lineNumber);
        this.updateLines();
    }
    
    @Override
    public void changeLine(int lineNumber, Serializable string)
    {
        this.lines.set(lineNumber, string);
        this.updateLines();
    }
    
    @Override
    public void clearLines()
    {
        this.lines.clear();
        this.updateLines();
    }
    
    @Override
    public String getName()
    {
        return this.name;
    }
    
    @Override
    public void addPlayerToWhiteList(UUID uuid)
    {
        if (this.whitelist.add(uuid) && this.hidden)
        {
            final McPlayerImpl player = (McPlayerImpl) ObjectServiceInterface.instance().getPlayer(uuid);
            player.recalcBestScoreboard();
        }
    }
    
    @Override
    public void removePlayerFromWhiteList(UUID uuid)
    {
        if (this.whitelist.remove(uuid) && this.hidden)
        {
            final McPlayerImpl player = (McPlayerImpl) ObjectServiceInterface.instance().getPlayer(uuid);
            player.recalcBestScoreboard();
        }
    }
    
    @Override
    public Set<UUID> getWhitelist()
    {
        return Collections.unmodifiableSet(this.whitelist);
    }
    
    @Override
    public boolean isHidden()
    {
        return this.hidden;
    }
    
    @Override
    public void hide()
    {
        if (!this.hidden)
        {
            this.hidden = true;
            final Set<UUID> players = new HashSet<>();
            players.addAll(this.whitelist);
            players.addAll(this.associatedPlayers);
            players.stream()
                .map(p -> (McPlayerImpl) ObjectServiceInterface.instance().getPlayer(p))
                .forEach(McPlayerImpl::recalcBestScoreboard);
        }
    }
    
    @Override
    public void show()
    {
        if (this.hidden)
        {
            this.hidden = false;
            final Set<UUID> players = new HashSet<>();
            players.addAll(this.whitelist);
            players.addAll(this.associatedPlayers);
            players.stream()
                .map(p -> (McPlayerImpl) ObjectServiceInterface.instance().getPlayer(p))
                .forEach(McPlayerImpl::recalcBestScoreboard);
        }
    }
    
    /**
     * Deletes this scoreboard.
     */
    public void delete()
    {
        MessageServiceInterface.instance().unregisterPlaceholderListener(
            (Plugin)McLibInterface.instance(), new String[][]{{}}, this);
        this.hidden = true;
        this.whitelist.clear();
        final Set<UUID> players = new HashSet<>();
        players.addAll(this.associatedPlayers);
        players.stream()
            .map(p -> (McPlayerImpl) ObjectServiceInterface.instance().getPlayer(p))
            .forEach(McPlayerImpl::recalcBestScoreboard);
    }
    
    /**
     * Change cuboid.
     * 
     * @param newCuboid
     *            new bounds
     */
    public void changeCuboid(Cuboid newCuboid)
    {
        ObjectServiceInterface.instance().findPlayers(this.cuboid).forEach(p -> ((McPlayerImpl)p).recalcBestScoreboard());
        this.cuboid = newCuboid;
        ObjectServiceInterface.instance().findPlayers(this.cuboid).forEach(p -> ((McPlayerImpl)p).recalcBestScoreboard());
    }
    
}
