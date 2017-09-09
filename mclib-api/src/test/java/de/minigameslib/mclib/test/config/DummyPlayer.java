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

package de.minigameslib.mclib.test.config;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McStorage;
import de.minigameslib.mclib.api.cli.ClientInterface;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.gui.AnvilGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.gui.RawMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mclib.api.perms.PermissionsInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;
import de.minigameslib.mclib.shared.api.com.CommunicationEndpointId;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.PlayerData;

/**
 * Dummy player.
 * 
 * @author mepeisen
 *
 */
public class DummyPlayer extends PlayerData implements McPlayerInterface
{
    
    /**
     * Constructor.
     */
    public DummyPlayer()
    {
        super();
    }
    
    /**
     * Constructor.
     * 
     * @param player4 player.
     */
    public DummyPlayer(McPlayerInterface player4)
    {
        super(player4.getPlayerUUID(), player4.getName());
    }

    @Override
    public Player getBukkitPlayer()
    {
        return ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).getBukkitPlayer();
    }
    
    @Override
    public String getName()
    {
        return ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).getName();
    }
    
    @Override
    public OfflinePlayer getOfflinePlayer()
    {
        return ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).getOfflinePlayer();
    }
    
    @Override
    public void sendMessage(LocalizedMessageInterface msg, Serializable... args)
    {
        ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).sendMessage(msg, args);
    }
    
    @Override
    public Locale getPreferredLocale()
    {
        return ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).getPreferredLocale();
    }
    
    @Override
    public void setPreferredLocale(Locale locale) throws McException
    {
        ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).setPreferredLocale(locale);
    }
    
    @Override
    public String[] encodeMessage(LocalizedMessageInterface msg, Serializable... args)
    {
        return ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).encodeMessage(msg, args);
    }
    
    @Override
    public boolean checkPermission(PermissionsInterface perm)
    {
        return ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).checkPermission(perm);
    }
    
    @Override
    public McStorage getContextStorage()
    {
        return ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).getContextStorage();
    }
    
    @Override
    public McStorage getSessionStorage()
    {
        return ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).getSessionStorage();
    }
    
    @Override
    public McStorage getPersistentStorage()
    {
        return ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).getPersistentStorage();
    }
    
    @Override
    public boolean hasSmartGui()
    {
        return ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).hasSmartGui();
    }
    
    @Override
    public GuiSessionInterface getGuiSession()
    {
        return ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).getGuiSession();
    }
    
    @Override
    public GuiSessionInterface openClickGui(ClickGuiInterface gui) throws McException
    {
        return ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).openClickGui(gui);
    }
    
    @Override
    public GuiSessionInterface openAnvilGui(AnvilGuiInterface gui) throws McException
    {
        return ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).openAnvilGui(gui);
    }
    
    @Override
    public GuiSessionInterface openSmartGui() throws McException
    {
        return ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).openSmartGui();
    }
    
    @Override
    public ZoneInterface getZone()
    {
        return ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).getZone();
    }

    @Override
    public ZoneInterface getZone(ZoneTypeId... type)
    {
        return null;
    }
    
    @Override
    public McOutgoingStubbing<McPlayerInterface> when(McPredicate<McPlayerInterface> test) throws McException
    {
        return ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).when(test);
    }
    
    @Override
    public void sendToClient(CommunicationEndpointId endpoint, DataSection... data)
    {
        ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID()).sendToClient(endpoint, data);
    }

    @Override
    public void sendRaw(RawMessageInterface raw) throws McException
    {
        // not used
    }

    @Override
    public boolean isInsideZone(ZoneInterface zone)
    {
        return false;
    }

    @Override
    public boolean isInsideRandomZone(ZoneInterface... zone)
    {
        return false;
    }

    @Override
    public boolean isInsideRandomZone(ZoneTypeId... type)
    {
        return false;
    }

    @Override
    public boolean isInsideAllZones(ZoneInterface... zone)
    {
        return false;
    }

    @Override
    public Collection<ZoneInterface> getZones()
    {
        return Collections.emptyList();
    }

    @Override
    public Collection<ZoneInterface> getZones(ZoneTypeId... type)
    {
        return Collections.emptyList();
    }

    @Override
    public <EVT extends MinecraftEvent<?, EVT>> void registerHandler(Plugin plugin, Class<EVT> clazz, McConsumer<EVT> handler)
    {
        // empty
    }

    @Override
    public void registerHandlers(Plugin plugin, McListener listener)
    {
        // empty
    }

    @Override
    public <EVT extends MinecraftEvent<?, EVT>> void unregisterHandler(Plugin plugin, Class<EVT> clazz, McConsumer<EVT> handler)
    {
        // empty
    }

    @Override
    public void unregisterHandlers(Plugin plugin, McListener listener)
    {
        // empty
    }

    @Override
    public ClientInterface getClient()
    {
        // empty
        return null;
    }

    @Override
    public GuiSessionInterface nestClickGui(ClickGuiInterface gui) throws McException
    {
        // empty
        return null;
    }

    @Override
    public GuiSessionInterface nestAnvilGui(AnvilGuiInterface gui) throws McException
    {
        // empty
        return null;
    }
    
}
