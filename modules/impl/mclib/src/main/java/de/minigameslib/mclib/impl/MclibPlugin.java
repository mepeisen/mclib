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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import de.minigames.mclib.nms.v18.NmsFactory1_8;
import de.minigames.mclib.nms.v183.NmsFactory1_8_3;
import de.minigames.mclib.nms.v185.NmsFactory1_8_5;
import de.minigames.mclib.nms.v19.NmsFactory1_9;
import de.minigames.mclib.nms.v194.NmsFactory1_9_4;
import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McContext;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.MinecraftVersionsType;
import de.minigameslib.mclib.api.config.ConfigInterface;
import de.minigameslib.mclib.api.config.ConfigServiceInterface;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.MessageServiceInterface;
import de.minigameslib.mclib.api.locale.MessagesConfigInterface;
import de.minigameslib.mclib.api.objects.ComponentHandlerInterface;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.ComponentTypeId;
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.EntityHandlerInterface;
import de.minigameslib.mclib.api.objects.EntityIdInterface;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.EntityTypeId;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.SignHandlerInterface;
import de.minigameslib.mclib.api.objects.SignIdInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.SignTypeId;
import de.minigameslib.mclib.api.objects.ZoneHandlerInterface;
import de.minigameslib.mclib.api.objects.ZoneIdInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mclib.api.perms.PermissionServiceInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.api.util.function.McSupplier;
import de.minigameslib.mclib.nms.api.NmsFactory;
import de.minigameslib.mclib.nms.v110.NmsFactory1_10_1;

/**
 * Main spigot plugin class for MCLIB.
 * 
 * @author mepeisen
 */
public class MclibPlugin extends JavaPlugin
        implements Listener, EnumServiceInterface, ConfigServiceInterface, MessageServiceInterface, ObjectServiceInterface, PermissionServiceInterface, McLibInterface
{
    
    /** the overall minecraft server versioon. */
    private static final MinecraftVersionsType         SERVER_VERSION = MclibPlugin.getServerVersion();
    
    /** context helper. */
    private final McContextImpl                        context        = new McContextImpl();
    
    /** enum service helper. */
    private final EnumServiceImpl                      enumService    = new EnumServiceImpl();
    
    /**
     * messages configuration per plugin.
     */
    private final Map<Plugin, MessagesConfigInterface> messages       = new HashMap<>();
    
    /**
     * configuration per plugin.
     */
    private final Map<Plugin, ConfigInterface>         configurations = new HashMap<>();
    
    /** the player registry. */
    private PlayerRegistry                             players;
    
    /** the objects manager. */
    private ObjectsManager                             objectsManager;
    
    @Override
    public void onEnable()
    {
        this.players = new PlayerRegistry(new File(this.getDataFolder(), "players")); //$NON-NLS-1$
        
        switch (SERVER_VERSION)
        {
            case Unknown:
            case V1_7:
            case V1_7_R1:
            case V1_7_R2:
            case V1_7_R3:
            case V1_7_R4:
            default:
                throw new IllegalStateException("Unsupported minecraft server version."); //$NON-NLS-1$
            case V1_10:
            case V1_10_R1:
                Bukkit.getServicesManager().register(NmsFactory.class, new NmsFactory1_10_1(), this, ServicePriority.Highest);
                break;
            case V1_8:
            case V1_8_R1:
                Bukkit.getServicesManager().register(NmsFactory.class, new NmsFactory1_8(), this, ServicePriority.Highest);
                break;
            case V1_8_R2:
                Bukkit.getServicesManager().register(NmsFactory.class, new NmsFactory1_8_3(), this, ServicePriority.Highest);
                break;
            case V1_8_R3:
                Bukkit.getServicesManager().register(NmsFactory.class, new NmsFactory1_8_5(), this, ServicePriority.Highest);
                break;
            case V1_9:
            case V1_9_R1:
                Bukkit.getServicesManager().register(NmsFactory.class, new NmsFactory1_9(), this, ServicePriority.Highest);
                break;
            case V1_9_R2:
                Bukkit.getServicesManager().register(NmsFactory.class, new NmsFactory1_9_4(), this, ServicePriority.Highest);
                break;
        }
        
        Bukkit.getPluginManager().registerEvents(this, this);
        
        Bukkit.getServicesManager().register(EnumServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(ConfigServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(MessageServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(ObjectServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(PermissionServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(McContext.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(McLibInterface.class, this, this, ServicePriority.Highest);
        
        this.registerEnumClass(this, CommonMessages.class);
        this.registerEnumClass(this, McCoreConfig.class);
        
        this.objectsManager = new ObjectsManager(this.getDataFolder());
    }
    
    @Override
    public void onDisable()
    {
        this.unregisterAll(this);
        Bukkit.getServicesManager().unregisterAll(this);
    }
    
    /**
     * Calculates the minecraft server version.
     * 
     * @return Minecraft server version.
     */
    private static MinecraftVersionsType getServerVersion()
    {
        try
        {
            final String v = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1); //$NON-NLS-1$
            if (v.startsWith("v1_7_R1")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_7_R1;
            }
            if (v.startsWith("v1_7_R2")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_7_R2;
            }
            if (v.startsWith("v1_7_R3")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_7_R3;
            }
            if (v.startsWith("v1_7_R4")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_7_R4;
            }
            if (v.startsWith("v1_8_R1")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_8_R1;
            }
            if (v.startsWith("v1_8_R2")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_8_R2;
            }
            if (v.startsWith("v1_8_R3")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_8_R3;
            }
            if (v.startsWith("v1_9_R1")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_9_R1;
            }
            if (v.startsWith("v1_9_R2")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_9_R2;
            }
            if (v.startsWith("v1_10_R1")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_10_R1;
            }
        }
        catch (@SuppressWarnings("unused") Exception ex)
        {
            // silently ignore
        }
        return MinecraftVersionsType.Unknown;
    }
    
    // enum services
    
    @Override
    public void registerEnumClass(Plugin plugin, Class<? extends Enum<?>> clazz)
    {
        this.enumService.registerEnumClass(plugin, clazz);
    }
    
    @Override
    public void unregisterAll(Plugin plugin)
    {
        this.enumService.unregisterAll(plugin);
    }
    
    @Override
    public Plugin getPlugin(Enum<?> enumValue)
    {
        return this.enumService.getPlugin(enumValue);
    }
    
    @Override
    public Set<Enum<?>> getEnumValues(Plugin plugin)
    {
        return this.enumService.getEnumValues(plugin);
    }
    
    @Override
    public <T> Set<T> getEnumValues(Plugin plugin, Class<T> clazz)
    {
        return this.getEnumValues(plugin, clazz);
    }
    
    // context
    
    @Override
    public <T> T getContext(Class<T> clazz)
    {
        return this.context.getContext(clazz);
    }
    
    @Override
    public <T> void setContext(Class<T> clazz, T value)
    {
        this.context.setContext(clazz, value);
    }
    
    @Override
    public <T> void registerContextHandler(Plugin plugin, Class<T> clazz, ContextHandlerInterface<T> handler) throws McException
    {
        this.context.registerContextHandler(plugin, clazz, handler);
    }
    
    @Override
    public void registerContextResolver(Plugin plugin, ContextResolverInterface resolver)
    {
        this.context.registerContextResolver(plugin, resolver);
    }
    
    @Override
    public void unregisterContextHandlersAndResolvers(Plugin plugin)
    {
        this.context.unregisterContextHandlersAndResolvers(plugin);
    }
    
    @Override
    public String resolveContextVar(String src)
    {
        return this.context.resolveContextVar(src);
    }
    
    @Override
    public void runInNewContext(McRunnable runnable) throws McException
    {
        this.context.runInNewContext(runnable);
    }
    
    @Override
    public void runInCopiedContext(McRunnable runnable) throws McException
    {
        this.context.runInCopiedContext(runnable);
    }
    
    @Override
    public <T> T calculateInNewContext(McSupplier<T> runnable) throws McException
    {
        return this.context.calculateInNewContext(runnable);
    }
    
    @Override
    public <T> T calculateInCopiedContext(McSupplier<T> runnable) throws McException
    {
        return this.context.calculateInCopiedContext(runnable);
    }
    
    // messages
    
    @Override
    public MessagesConfigInterface getMessagesFromMsg(LocalizedMessageInterface item)
    {
        if (!(item instanceof Enum<?>))
        {
            return null;
        }
        final Plugin plugin = this.enumService.getPlugin((Enum<?>) item);
        if (plugin == null)
        {
            return null;
        }
        return this.messages.computeIfAbsent(plugin, (key) -> new MessagesConfigImpl(plugin, this.enumService));
    }
    
    @Override
    public ConfigInterface getConfigFromCfg(ConfigurationValueInterface item)
    {
        if (!(item instanceof Enum<?>))
        {
            return null;
        }
        final Plugin plugin = this.enumService.getPlugin((Enum<?>) item);
        if (plugin == null)
        {
            return null;
        }
        return this.configurations.computeIfAbsent(plugin, (key) -> new ConfigImpl(plugin, this.enumService));
    }
    
    // object services
    
    @Override
    public McPlayerInterface getPlayer(Player player)
    {
        return this.players.getPlayer(player);
    }
    
    @Override
    public McPlayerInterface getPlayer(OfflinePlayer player)
    {
        return this.players.getPlayer(player);
    }
    
    @Override
    public McPlayerInterface getPlayer(UUID uuid)
    {
        return this.players.getPlayer(uuid);
    }
    
    /**
     * Player join event
     * 
     * @param evt
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent evt)
    {
        this.players.onPlayerJoin(evt);
    }
    
    /**
     * Player quit event.
     * 
     * @param evt
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent evt)
    {
        this.players.onPlayerQuit(evt);
    }
    
    // library
    
    @Override
    public MinecraftVersionsType getMinecraftVersion()
    {
        return SERVER_VERSION;
    }
    
    // gui
    
    /**
     * Inventory close event
     * 
     * @param evt
     *            inventory close event
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent evt)
    {
        if (evt.getPlayer() instanceof Player)
        {
            final McPlayerImpl player = this.players.getPlayer((Player) evt.getPlayer());
            if (player.getGuiSession() != null)
            {
                player.onCloseGui();
            }
        }
    }
    
    /**
     * Inventory click event
     * 
     * @param evt
     *            inventory click event
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt)
    {
        if (evt.getWhoClicked() instanceof Player)
        {
            final McPlayerImpl player = this.players.getPlayer((Player) evt.getWhoClicked());
            final GuiSessionImpl session = (GuiSessionImpl) player.getGuiSession();
            if (session != null)
            {
                session.onClick(evt);
            }
        }
    }
    
    /**
     * Inventory drag event
     * 
     * @param evt
     *            inventory drag event
     */
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent evt)
    {
        if (evt.getWhoClicked() instanceof Player)
        {
            final McPlayerImpl player = this.players.getPlayer((Player) evt.getWhoClicked());
            if (player.getGuiSession() != null)
            {
                evt.setCancelled(true);
            }
        }
    }
    
    @Override
    public Locale getDefaultLocale()
    {
        return new Locale(McCoreConfig.DefaultLocale.getString());
    }
    
    @Override
    public void setDefaultLocale(Locale locale) throws McException
    {
        McCoreConfig.DefaultLocale.setString(locale.toString());
        McCoreConfig.DefaultLocale.saveConfig();
    }
    
    @Override
    public boolean debug()
    {
        return McCoreConfig.Debug.getBoolean();
    }
    
    @Override
    public void setDebug(boolean enabled)
    {
        McCoreConfig.Debug.setBoolean(enabled);
        McCoreConfig.Debug.saveConfig();
    }
    
    /**
     * event handler for plugin disable events
     * 
     * @param evt
     */
    @EventHandler
    public void onPluginDisable(PluginDisableEvent evt)
    {
        if (!evt.getPlugin().isEnabled())
        {
            this.objectsManager.onDisable(evt.getPlugin());
        }
    }
    
    /**
     * event handler for plugin enable events
     * 
     * @param evt
     */
    @EventHandler
    public void onPluginEnable(PluginEnableEvent evt)
    {
        if (evt.getPlugin().isEnabled())
        {
            this.objectsManager.onEnable(evt.getPlugin());
        }
    }
    
    // objects api
    
    @Override
    public ComponentInterface findComponent(Location location)
    {
        return this.objectsManager.findComponent(location);
    }
    
    @Override
    public ComponentInterface findComponent(Block block)
    {
        return this.findComponent(block.getLocation());
    }
    
    @Override
    public ComponentInterface findComponent(ComponentIdInterface id)
    {
        return this.objectsManager.findComponent(id);
    }
    
    @Override
    public ComponentInterface createComponent(ComponentTypeId type, Location location, ComponentHandlerInterface handler) throws McException
    {
        return this.objectsManager.createComponent(type, location, handler);
    }
    
    @Override
    public EntityInterface findEntity(Entity entity)
    {
        return this.objectsManager.findEntity(entity);
    }
    
    @Override
    public EntityInterface findEntity(EntityIdInterface id)
    {
        return this.objectsManager.findEntity(id);
    }
    
    @Override
    public EntityInterface createEntity(EntityTypeId type, Entity entity, EntityHandlerInterface handler) throws McException
    {
        return this.objectsManager.createEntity(type, entity, handler);
    }
    
    @Override
    public SignInterface findSign(Location location)
    {
        return this.objectsManager.findSign(location);
    }
    
    @Override
    public SignInterface findSign(Block block)
    {
        return this.objectsManager.findSign(block.getLocation());
    }
    
    @Override
    public SignInterface findSign(Sign sign)
    {
        return this.objectsManager.findSign(sign.getLocation());
    }
    
    @Override
    public SignInterface findSign(SignIdInterface id)
    {
        return this.objectsManager.findSign(id);
    }
    
    @Override
    public SignInterface createSign(SignTypeId type, Sign sign, SignHandlerInterface handler) throws McException
    {
        return this.objectsManager.createSign(type, sign, handler);
    }
    
    @Override
    public ZoneInterface findZone(Location location)
    {
        return this.objectsManager.findZone(location);
    }
    
    @Override
    public Iterable<ZoneInterface> findZones(Location location)
    {
        return this.objectsManager.findZones(location);
    }
    
    @Override
    public ZoneInterface findZoneWithoutY(Location location)
    {
        return this.objectsManager.findZoneWithoutY(location);
    }
    
    @Override
    public Iterable<ZoneInterface> findZonesWithoutY(Location location)
    {
        return this.objectsManager.findZonesWithoutY(location);
    }
    
    @Override
    public ZoneInterface findZoneWithoutYD(Location location)
    {
        return this.objectsManager.findZoneWithoutYD(location);
    }
    
    @Override
    public Iterable<ZoneInterface> findZonesWithoutYD(Location location)
    {
        return this.objectsManager.findZonesWithoutYD(location);
    }
    
    @Override
    public ZoneInterface findZone(ZoneIdInterface id)
    {
        return this.objectsManager.findZone(id);
    }
    
    @Override
    public ZoneInterface createZone(ZoneTypeId type, Cuboid cuboid, ZoneHandlerInterface handler) throws McException
    {
        return this.objectsManager.createZone(type, cuboid, handler);
    }
    
}
