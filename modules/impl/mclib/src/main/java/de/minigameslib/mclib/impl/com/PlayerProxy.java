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

package de.minigameslib.mclib.impl.com;

import java.io.Serializable;
import java.util.Collection;
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
 * A proxy helper for players.
 * 
 * @author mepeisen
 */
public class PlayerProxy extends PlayerData implements McPlayerInterface
{
    
    /** the target player interface. */
    private McPlayerInterface target;
    
    @Override
    public void read(DataSection section)
    {
        super.read(section);
        this.target = ObjectServiceInterface.instance().getPlayer(this.getPlayerUUID());
    }
    
    @Override
    public String getDisplayName()
    {
        final Player player = this.getBukkitPlayer();
        return player == null ? super.getDisplayName() : player.getDisplayName();
    }
    
    @Override
    public Player getBukkitPlayer()
    {
        return this.target.getBukkitPlayer();
    }
    
    @Override
    public String getName()
    {
        return this.target.getName();
    }
    
    @Override
    public OfflinePlayer getOfflinePlayer()
    {
        return this.target.getOfflinePlayer();
    }
    
    @Override
    public void sendMessage(LocalizedMessageInterface msg, Serializable... args)
    {
        this.target.sendMessage(msg, args);
    }
    
    @Override
    public Locale getPreferredLocale()
    {
        return this.target.getPreferredLocale();
    }
    
    @Override
    public void setPreferredLocale(Locale locale) throws McException
    {
        this.target.setPreferredLocale(locale);
    }
    
    @Override
    public String[] encodeMessage(LocalizedMessageInterface msg, Serializable... args)
    {
        return this.target.encodeMessage(msg, args);
    }
    
    @Override
    public boolean checkPermission(PermissionsInterface perm)
    {
        return this.target.checkPermission(perm);
    }
    
    @Override
    public McStorage getContextStorage()
    {
        return this.target.getContextStorage();
    }
    
    @Override
    public McStorage getSessionStorage()
    {
        return this.target.getSessionStorage();
    }
    
    @Override
    public McStorage getPersistentStorage()
    {
        return this.target.getPersistentStorage();
    }
    
    @Override
    public boolean hasSmartGui()
    {
        return this.target.hasSmartGui();
    }
    
    @Override
    public GuiSessionInterface getGuiSession()
    {
        return this.target.getGuiSession();
    }
    
    @Override
    public GuiSessionInterface openClickGui(ClickGuiInterface gui) throws McException
    {
        return this.target.openClickGui(gui);
    }
    
    @Override
    public GuiSessionInterface openAnvilGui(AnvilGuiInterface gui) throws McException
    {
        return this.target.openAnvilGui(gui);
    }
    
    @Override
    public GuiSessionInterface openSmartGui() throws McException
    {
        return this.target.openSmartGui();
    }
    
    @Override
    public ZoneInterface getZone()
    {
        return this.target.getZone();
    }
    
    @Override
    public McOutgoingStubbing<McPlayerInterface> when(McPredicate<McPlayerInterface> test) throws McException
    {
        return this.target.when(test);
    }
    
    @Override
    public void sendToClient(CommunicationEndpointId endpoint, DataSection... data)
    {
        this.target.sendToClient(endpoint, data);
    }
    
    @Override
    public void sendRaw(RawMessageInterface raw) throws McException
    {
        this.target.sendRaw(raw);
    }
    
    @Override
    public boolean isInsideZone(ZoneInterface zone)
    {
        return this.target.isInsideZone(zone);
    }
    
    @Override
    public boolean isInsideRandomZone(ZoneInterface... zone)
    {
        return this.target.isInsideRandomZone(zone);
    }
    
    @Override
    public boolean isInsideAllZones(ZoneInterface... zone)
    {
        return this.target.isInsideAllZones(zone);
    }
    
    @Override
    public ZoneInterface getZone(ZoneTypeId... type)
    {
        return this.target.getZone(type);
    }
    
    @Override
    public boolean isInsideRandomZone(ZoneTypeId... type)
    {
        return this.target.isInsideRandomZone(type);
    }
    
    @Override
    public Collection<ZoneInterface> getZones()
    {
        return this.target.getZones();
    }
    
    @Override
    public Collection<ZoneInterface> getZones(ZoneTypeId... type)
    {
        return this.target.getZones();
    }
    
    @Override
    public <Evt extends MinecraftEvent<?, Evt>> void registerHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> handler)
    {
        this.target.registerHandler(plugin, clazz, handler);
    }
    
    @Override
    public void registerHandlers(Plugin plugin, McListener listener)
    {
        this.target.registerHandlers(plugin, listener);
    }
    
    @Override
    public <Evt extends MinecraftEvent<?, Evt>> void unregisterHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> handler)
    {
        this.target.unregisterHandler(plugin, clazz, handler);
    }
    
    @Override
    public void unregisterHandlers(Plugin plugin, McListener listener)
    {
        this.target.unregisterHandlers(plugin, listener);
    }
    
    @Override
    public ClientInterface getClient()
    {
        return this.target.getClient();
    }
    
    @Override
    public GuiSessionInterface nestClickGui(ClickGuiInterface gui) throws McException
    {
        return this.target.nestClickGui(gui);
    }
    
    @Override
    public GuiSessionInterface nestAnvilGui(AnvilGuiInterface gui) throws McException
    {
        return this.target.nestAnvilGui(gui);
    }
    
}
