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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

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
import de.minigameslib.mclib.api.bungee.BungeeServerInterface;
import de.minigameslib.mclib.api.bungee.BungeeServiceInterface;
import de.minigameslib.mclib.api.com.CommunicationBungeeHandler;
import de.minigameslib.mclib.api.com.CommunicationPeerHandler;
import de.minigameslib.mclib.api.com.ServerCommunicationServiceInterface;
import de.minigameslib.mclib.api.config.ConfigInterface;
import de.minigameslib.mclib.api.config.ConfigServiceInterface;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.ext.ExtensionInterface;
import de.minigameslib.mclib.api.ext.ExtensionPointInterface;
import de.minigameslib.mclib.api.ext.ExtensionServiceInterface;
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
import de.minigameslib.mclib.nms.api.AnvilManagerInterface;
import de.minigameslib.mclib.nms.api.EventSystemInterface;
import de.minigameslib.mclib.nms.api.InventoryManagerInterface;
import de.minigameslib.mclib.nms.api.NmsFactory;
import de.minigameslib.mclib.nms.api.PlayerManagerInterface;
import de.minigameslib.mclib.nms.v110.NmsFactory1_10_1;
import de.minigameslib.mclib.nms.v111.NmsFactory1_11;
import de.minigameslib.mclib.pshared.ActionPerformedData;
import de.minigameslib.mclib.pshared.CoreMessages;
import de.minigameslib.mclib.pshared.MclibCommunication;
import de.minigameslib.mclib.pshared.PingData;
import de.minigameslib.mclib.pshared.PongData;
import de.minigameslib.mclib.pshared.QueryFormRequestData;
import de.minigameslib.mclib.pshared.WinClosedData;
import de.minigameslib.mclib.shared.api.com.CommunicationEndpointId;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Main spigot plugin class for MCLIB.
 * 
 * @author mepeisen
 */
public class MclibPlugin extends JavaPlugin implements Listener, EnumServiceInterface, ConfigServiceInterface, MessageServiceInterface, ObjectServiceInterface, PermissionServiceInterface,
        McLibInterface, ServerCommunicationServiceInterface, ExtensionServiceInterface, PluginMessageListener, BungeeServiceInterface
{
    
    /**
     * plugin channel for messages between servers and clients
     */
    private static final String                                          MCLIB_SERVER_TO_CLIENT_CHANNEL = "mclib|sc";                    //$NON-NLS-1$
    
    /**
     * plugin channel for messages between bungee servers
     */
    private static final String                                          MCLIB_SERVER_TO_SERVER_CHANNEL = "mclib|bc";                    //$NON-NLS-1$
    
    /**
     * plugin channel for BungeeCord
     */
    private static final String                                          BUNGEECORD_CHANNEL             = "BungeeCord";                  //$NON-NLS-1$
    
    /** the overall minecraft server versioon. */
    private static final MinecraftVersionsType                           SERVER_VERSION                 = MclibPlugin.getServerVersion();
    
    /** context helper. */
    private final McContextImpl                                          context                        = new McContextImpl();
    
    /** enum service helper. */
    private final EnumServiceImpl                                        enumService                    = new EnumServiceImpl();
    
    /**
     * messages configuration per plugin.
     */
    private final Map<Plugin, MessagesConfigInterface>                   messages                       = new HashMap<>();
    
    /**
     * configuration per plugin.
     */
    private final Map<Plugin, ConfigInterface>                           configurations                 = new HashMap<>();
    
    /** the player registry. */
    PlayerRegistry                                                       players;
    
    /** the objects manager. */
    private ObjectsManager                                               objectsManager;
    
    /** the known endpoints. */
    private final Map<String, Map<String, CommunicationEndpointId>>      peerEndpoints                  = new HashMap<>();
    
    /** the known handlers. */
    private final Map<CommunicationEndpointId, CommunicationPeerHandler> peerHandlers                   = new ConcurrentHashMap<>();
    
    /** the known handlers. */
    private final Map<String, List<CommunicationEndpointId>>             peerEndpointPlugins            = new ConcurrentHashMap<>();
    
    /** the known endpoints. */
    private final Map<String, Map<String, CommunicationEndpointId>>      bungeeEndpoints                = new HashMap<>();
    
    /** the known handlers. */
    final Map<CommunicationEndpointId, CommunicationBungeeHandler>       bungeeHandlers                 = new ConcurrentHashMap<>();
    
    /** the known handlers. */
    private final Map<String, List<CommunicationEndpointId>>             bungeeEndpointPlugins          = new ConcurrentHashMap<>();
    
    /** type of endpoints: true is a client endpoint, false a server endpoint */
    private final Map<CommunicationEndpointId, Boolean>                  endpointTypes                  = new HashMap<>();
    
    /** the current bungee server (myself) */
    private final LocalBungeeServer                                      currentBungee                  = new LocalBungeeServer();
    
    /** the known other bungee servers from network. */
    private final Map<String, BungeeServerInterface>                     bungeeServers                  = new HashMap<>();
    
    /**
     * queue for bungee messages.
     */
    final Queue<Consumer<Player>>                                        bungeeQueue                    = new ConcurrentLinkedQueue<>();
    
    /**
     * the get servers ping.
     */
    private GetServersPing                                               serversPing;
    
    @Override
    public int getApiVersion()
    {
        return APIVERSION_1_0_0;
    }
    
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
            case V1_11:
            case V1_11_R1:
                Bukkit.getServicesManager().register(NmsFactory.class, new NmsFactory1_11(), this, ServicePriority.Highest);
                break;
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
        
        // public api services
        Bukkit.getServicesManager().register(EnumServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(ConfigServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(MessageServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(ObjectServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(PermissionServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(McContext.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(McLibInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(ServerCommunicationServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(ExtensionServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(BungeeServiceInterface.class, this, this, ServicePriority.Highest);
        
        CommunicationEndpointId.CommunicationServiceCache.init(this);
        
        // mclib enumerations
        this.registerEnumClass(this, CommonMessages.class);
        this.registerEnumClass(this, McCoreConfig.class);
        
        // nms services
        final NmsFactory factory = Bukkit.getServicesManager().load(NmsFactory.class);
        Bukkit.getServicesManager().register(EventSystemInterface.class, factory.create(EventSystemInterface.class), this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(InventoryManagerInterface.class, factory.create(InventoryManagerInterface.class), this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(AnvilManagerInterface.class, factory.create(AnvilManagerInterface.class), this, ServicePriority.Highest);
        
        this.objectsManager = new ObjectsManager(this.getDataFolder());
        
        // nms event listeners
        Bukkit.getPluginManager().registerEvents(Bukkit.getServicesManager().load(EventSystemInterface.class), this);
        Bukkit.getPluginManager().registerEvents(Bukkit.getServicesManager().load(InventoryManagerInterface.class), this);
        Bukkit.getPluginManager().registerEvents(Bukkit.getServicesManager().load(AnvilManagerInterface.class), this);
        
        // communication endpoints
        this.registerPeerHandler(this, MclibCommunication.ClientServerCore, new MclibCoreHandler());
        
        // network
        // sc = s[erver]c[client] (both directions)
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, MCLIB_SERVER_TO_CLIENT_CHANNEL);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, MCLIB_SERVER_TO_CLIENT_CHANNEL, this);
        // bc = b[ungee]c[oord]
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, MCLIB_SERVER_TO_SERVER_CHANNEL);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, MCLIB_SERVER_TO_SERVER_CHANNEL, this);
        // bungeecord
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, BUNGEECORD_CHANNEL);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, BUNGEECORD_CHANNEL, this);
        
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServer"); //$NON-NLS-1$
        this.bungeeQueue.add(p -> p.sendPluginMessage(this, BUNGEECORD_CHANNEL, out.toByteArray()));
        
        this.serversPing = new GetServersPing();
        this.serversPing.runTaskTimer(this, 20 * 5, 20 * 60); // once per minute
    }
    
    /**
     * The get servers ping.
     * 
     * @author mepeisen
     */
    private final class GetServersPing extends BukkitRunnable
    {
        
        /**
         * Constructor.
         */
        public GetServersPing()
        {
            // empty.
        }
        
        @Override
        public void run()
        {
            final Collection<? extends Player> onlPlayers = Bukkit.getOnlinePlayers();
            if (onlPlayers.size() > 0)
            {
                // bungee cord ensures this is not send to the client.
                final ByteArrayDataOutput out2 = ByteStreams.newDataOutput();
                out2.writeUTF("GetServers"); //$NON-NLS-1$
                onlPlayers.iterator().next().sendPluginMessage(MclibPlugin.this, BUNGEECORD_CHANNEL, out2.toByteArray());
            }
        }
        
    }
    
    @Override
    public void onDisable()
    {
        this.unregisterAllEnumerations(this);
        this.removeAllCommunicationEndpoints(this);
        Bukkit.getServicesManager().unregisterAll(this);
        
        this.serversPing.cancel();
        this.serversPing = null;
        
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, MCLIB_SERVER_TO_CLIENT_CHANNEL);
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this, MCLIB_SERVER_TO_CLIENT_CHANNEL, this);
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, MCLIB_SERVER_TO_SERVER_CHANNEL);
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this, MCLIB_SERVER_TO_SERVER_CHANNEL, this);
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, BUNGEECORD_CHANNEL);
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this, BUNGEECORD_CHANNEL, this);
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
            if (v.startsWith("v1_11_R1")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_11_R1;
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
    public void unregisterAllEnumerations(Plugin plugin)
    {
        this.enumService.unregisterAllEnumerations(plugin);
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
    public <T> Set<T> getEnumValues(Class<T> clazz)
    {
        return this.enumService.getEnumValues(clazz);
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
        Bukkit.getServicesManager().load(NmsFactory.class).create(PlayerManagerInterface.class).registerChannelEx(evt.getPlayer(), MCLIB_SERVER_TO_CLIENT_CHANNEL);
        this.players.onPlayerJoin(evt);
        
        // try to ping the client mod.
        final PingData ping = new PingData();
        final DataSection section = new MemoryDataSection();
        section.set("KEY", CoreMessages.Ping.name()); //$NON-NLS-1$
        ping.write(section.createSection("data")); //$NON-NLS-1$
        this.getPlayer(evt.getPlayer()).sendToClient(MclibCommunication.ClientServerCore, section);
        
        while (!this.bungeeQueue.isEmpty())
        {
            this.bungeeQueue.poll().accept(evt.getPlayer());
        }
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
    public <T extends ComponentHandlerInterface> void register(ComponentTypeId type, Class<T> handler) throws McException
    {
        this.objectsManager.register(type, handler);
    }
    
    @Override
    public <T extends EntityHandlerInterface> void register(EntityTypeId type, Class<T> handler) throws McException
    {
        this.objectsManager.register(type, handler);
    }
    
    @Override
    public <T extends SignHandlerInterface> void register(SignTypeId type, Class<T> handler) throws McException
    {
        this.objectsManager.register(type, handler);
    }
    
    @Override
    public <T extends ZoneHandlerInterface> void register(ZoneTypeId type, Class<T> handler) throws McException
    {
        this.objectsManager.register(type, handler);
    }
    
    @Override
    public ResumeReport resumeObjects(Plugin plugin)
    {
        return this.objectsManager.resumeObjects(plugin);
    }
    
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
    public ComponentInterface createComponent(ComponentTypeId type, Location location, ComponentHandlerInterface handler, boolean persist) throws McException
    {
        return this.objectsManager.createComponent(type, location, handler, persist);
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
    public EntityInterface createEntity(EntityTypeId type, Entity entity, EntityHandlerInterface handler, boolean persist) throws McException
    {
        return this.objectsManager.createEntity(type, entity, handler, persist);
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
    public SignInterface createSign(SignTypeId type, Sign sign, SignHandlerInterface handler, boolean persist) throws McException
    {
        return this.objectsManager.createSign(type, sign, handler, persist);
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
    public ZoneInterface createZone(ZoneTypeId type, Cuboid cuboid, ZoneHandlerInterface handler, boolean persist) throws McException
    {
        return this.objectsManager.createZone(type, cuboid, handler, persist);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        // if (command.getName().equals("mclib"))
        // {
        // MyCommandHandler.onCommand(sender, command, label, args);
        // }
        // return super.onCommand(sender, command, label, args);
        // TODO commands
        return false;
    }
    
    @Override
    public void registerPeerHandler(Plugin plugin, CommunicationEndpointId id, CommunicationPeerHandler handler)
    {
        synchronized (this.peerEndpoints)
        {
            Map<String, CommunicationEndpointId> map = this.peerEndpoints.get(id.getClass().getName());
            if (map == null)
            {
                map = new HashMap<>();
                this.peerEndpoints.put(id.getClass().getName(), map);
            }
            if (map.containsKey(id))
            {
                throw new IllegalStateException("Duplicate registration of communication endpoint."); //$NON-NLS-1$
            }
            map.put(id.name(), id);
            this.peerHandlers.put(id, handler);
            this.peerEndpointPlugins.computeIfAbsent(plugin.getName(), k -> new ArrayList<>()).add(id);
            this.endpointTypes.put(id, Boolean.TRUE);
        }
    }
    
    @Override
    public void registerBungeeHandler(Plugin plugin, CommunicationEndpointId id, CommunicationBungeeHandler handler)
    {
        synchronized (this.bungeeEndpoints)
        {
            Map<String, CommunicationEndpointId> map = this.bungeeEndpoints.get(id.getClass().getName());
            if (map == null)
            {
                map = new HashMap<>();
                this.bungeeEndpoints.put(id.getClass().getName(), map);
            }
            if (map.containsKey(id))
            {
                throw new IllegalStateException("Duplicate registration of communication endpoint."); //$NON-NLS-1$
            }
            map.put(id.name(), id);
            this.bungeeHandlers.put(id, handler);
            this.bungeeEndpointPlugins.computeIfAbsent(plugin.getName(), k -> new ArrayList<>()).add(id);
            this.endpointTypes.put(id, Boolean.FALSE);
        }
    }
    
    /**
     * Returns the endpoint for given class and element
     * 
     * @param clazz
     * @param name
     * @return endpoint or {@code null} if it does not exist.
     */
    private CommunicationEndpointId getPeerEndpoint(String clazz, String name)
    {
        synchronized (this.peerEndpoints)
        {
            final Map<String, CommunicationEndpointId> map = this.peerEndpoints.get(clazz);
            if (map != null)
            {
                return map.get(name);
            }
        }
        return null;
    }
    
    /**
     * Returns the endpoint for given class and element
     * 
     * @param clazz
     * @param name
     * @return endpoint or {@code null} if it does not exist.
     */
    private CommunicationEndpointId getServerEndpoint(String clazz, String name)
    {
        synchronized (this.bungeeEndpoints)
        {
            final Map<String, CommunicationEndpointId> map = this.bungeeEndpoints.get(clazz);
            if (map != null)
            {
                return map.get(name);
            }
        }
        return null;
    }
    
    @Override
    public void broadcastClients(CommunicationEndpointId id, DataSection... data)
    {
        if (data != null)
        {
            for (final DataSection section : data)
            {
                final ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeByte(0);
                new NetMessage(id, section).toBytes(out);
                byte[] bytes = out.toByteArray();
                if (this.getLogger().isLoggable(Level.FINEST))
                {
                    this.getLogger().finest("Sending NetMessage to all players\n" + Arrays.toString(bytes)); //$NON-NLS-1$
                }
                // TODO find a way to broadcast to all players connected to any server in bungee cord network
                Bukkit.getServer().sendPluginMessage(this, MCLIB_SERVER_TO_CLIENT_CHANNEL, bytes);
            }
        }
    }
    
    @Override
    public void broadcastServers(CommunicationEndpointId id, DataSection... data)
    {
        if (data != null)
        {
            for (final DataSection section : data)
            {
                // TODO only works if both servers have online players
                // TODO if target servers do not have online players the messages should be queued by bungeecord.
                // TODO ensure that we are really within bungeecord environments, else this will be sent to clients (not good)
                final ByteArrayDataOutput out2 = ByteStreams.newDataOutput();
                final ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out2.writeUTF("Forward"); //$NON-NLS-1$
                out2.writeUTF("ALL"); //$NON-NLS-1$
                out2.writeUTF(MCLIB_SERVER_TO_SERVER_CHANNEL);
                out.writeByte(0);
                new NetMessage(id, section).toBytes(out);
                byte[] bytes = out.toByteArray();
                if (this.getLogger().isLoggable(Level.FINEST))
                {
                    this.getLogger().finest("Sending NetMessage to all servers\n" + Arrays.toString(bytes)); //$NON-NLS-1$
                }
                out2.writeShort(bytes.length);
                out2.write(bytes);
                final Collection<? extends Player> onlPlayers = Bukkit.getOnlinePlayers();
                if (onlPlayers.size() > 0)
                {
                    // bungee cord ensures this is not send to the client.
                    onlPlayers.iterator().next().sendPluginMessage(this, BUNGEECORD_CHANNEL, bytes);
                }
                else
                {
                    this.bungeeQueue.add(p -> p.sendPluginMessage(this, BUNGEECORD_CHANNEL, bytes));
                }
            }
        }
    }
    
    @Override
    public void send(CommunicationEndpointId id, DataSection... data)
    {
        // check if we have a server-to-server endpoint or server-to-client endpoint
        if (this.endpointTypes.get(id))
        {
            // server to player
            final McPlayerInterface player = this.getCurrentPlayer();
            if (player == null)
            {
                this.getLogger().fine("Trying to send data to unknown player (invalid context)"); //$NON-NLS-1$
            }
            else if (data != null)
            {
                for (final DataSection section : data)
                {
                    final ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeByte(0);
                    new NetMessage(id, section).toBytes(out);
                    byte[] bytes = out.toByteArray();
                    if (this.getLogger().isLoggable(Level.FINEST))
                    {
                        this.getLogger().finest("Sending NetMessage to player " + player.getPlayerUUID() + "\n" + Arrays.toString(bytes)); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                    player.getBukkitPlayer().sendPluginMessage(this, MCLIB_SERVER_TO_CLIENT_CHANNEL, bytes);
                }
            }
        }
        else
        {
            // server to server means: broadcast to everyone
            this.broadcastServers(id, data);
        }
    }
    
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] buf)
    {
        if (channel.equals(BUNGEECORD_CHANNEL))
        {
            // TODO ensure this comes from bungee
            final ByteArrayDataInput input = ByteStreams.newDataInput(buf);
            final String sub = input.readUTF();
            if (sub.equals("GetServer")) //$NON-NLS-1$
            {
                final String serverName = input.readUTF();
                McCoreConfig.BungeeServerName.setString(serverName);
                McCoreConfig.BungeeServerName.saveConfig();
            }
            else if (sub.equals("GetServers")) //$NON-NLS-1$
            {
                String[] serverList = input.readUTF().split(", "); //$NON-NLS-1$
                final String myself = this.currentBungee.getName();
                synchronized (this.bungeeServers)
                {
                    final Set<String> failed = new HashSet<>(this.bungeeServers.keySet());
                    for (final String serverName : serverList)
                    {
                        if (!serverName.equals(myself))
                        {
                            if (!failed.remove(serverName))
                            {
                                this.bungeeServers.put(serverName, new RemoteBungeeServer(serverName));
                            }
                        }
                    }
                    failed.forEach(this.bungeeServers::remove);
                }
            }
        }
        else if (channel.equals(MCLIB_SERVER_TO_CLIENT_CHANNEL))
        {
            // TODO ensure this comes from client
            // message from client
            final McPlayerInterface mcp = this.getPlayer(player);
            try
            {
                this.runInNewContext(() -> {
                    this.setContext(McPlayerInterface.class, mcp);
                    final NetMessage msg = new NetMessage();
                    final ByteArrayDataInput input = ByteStreams.newDataInput(buf);
                    if (input.readByte() == 0) // forge NetMessage byte code.
                    {
                        msg.fromBytes(input, this::getPeerEndpoint);
                        
                        if (msg.getEndpoint() != null)
                        {
                            final CommunicationPeerHandler handler = this.peerHandlers.get(msg.getEndpoint());
                            if (handler != null)
                            {
                                handler.handleIncomming(mcp, msg.getEndpoint(), msg.getData());
                            }
                        }
                    }
                });
            }
            catch (McException ex)
            {
                this.getLogger().log(Level.WARNING, "Problems handling client message", ex); //$NON-NLS-1$
            }
        }
        else if (channel.equals(MCLIB_SERVER_TO_SERVER_CHANNEL))
        {
            // TODO ensure this comes from bungee
            // message from bungee network
            final NetMessage msg = new NetMessage();
            final ByteArrayDataInput input = ByteStreams.newDataInput(buf);
            if (input.readByte() == 1) // forge NetMessage byte code.
            {
                final String sendingServer = input.readUTF();
                final String receivingServer = input.readUTF();
                final String myName = BungeeServiceInterface.instance().getCurrent().getName();
                if (myName.equals(receivingServer) || "*".equals(receivingServer)) //$NON-NLS-1$
                {
                    msg.fromBytes(input, this::getServerEndpoint);
                    
                    if (msg.getEndpoint() != null)
                    {
                        final CommunicationBungeeHandler handler = this.bungeeHandlers.get(msg.getEndpoint());
                        if (handler != null)
                        {
                            final BungeeServerInterface sender = BungeeServiceInterface.instance().getServer(sendingServer);
                            handler.handleIncomming(sender, msg.getEndpoint(), msg.getData());
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void removeAllCommunicationEndpoints(Plugin plugin)
    {
        synchronized (this.peerEndpoints)
        {
            final List<CommunicationEndpointId> ids = this.peerEndpointPlugins.get(plugin.getName());
            if (ids != null)
            {
                for (final CommunicationEndpointId id : ids)
                {
                    this.peerHandlers.remove(id);
                    this.peerEndpoints.get(id.getClass().getName()).remove(id.name());
                    this.endpointTypes.remove(id);
                }
                this.peerEndpointPlugins.remove(plugin.getName());
            }
        }
        synchronized (this.bungeeEndpoints)
        {
            final List<CommunicationEndpointId> ids = this.bungeeEndpointPlugins.get(plugin.getName());
            if (ids != null)
            {
                for (final CommunicationEndpointId id : ids)
                {
                    this.bungeeHandlers.remove(id);
                    this.bungeeEndpoints.get(id.getClass().getName()).remove(id.name());
                    this.endpointTypes.remove(id);
                }
                this.bungeeEndpointPlugins.remove(plugin.getName());
            }
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see de.minigameslib.mclib.api.ext.ExtensionServiceInterface#register(org.bukkit.plugin.Plugin, de.minigameslib.mclib.api.ext.ExtensionPointInterface,
     * de.minigameslib.mclib.api.ext.ExtensionInterface)
     */
    @Override
    public <T extends ExtensionInterface> void register(Plugin plugin, ExtensionPointInterface<T> extPoint, T extension)
    {
        // TODO Auto-generated method stub
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see de.minigameslib.mclib.api.ext.ExtensionServiceInterface#remove(org.bukkit.plugin.Plugin, de.minigameslib.mclib.api.ext.ExtensionPointInterface,
     * de.minigameslib.mclib.api.ext.ExtensionInterface)
     */
    @Override
    public <T extends ExtensionInterface> void remove(Plugin plugin, ExtensionPointInterface<T> extPoint, T extension)
    {
        // TODO Auto-generated method stub
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see de.minigameslib.mclib.api.ext.ExtensionServiceInterface#getExtensions(de.minigameslib.mclib.api.ext.ExtensionPointInterface)
     */
    @Override
    public <T extends ExtensionInterface> Iterable<T> getExtensions(ExtensionPointInterface<? extends T> extPoint)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see de.minigameslib.mclib.api.com.ServerCommunicationServiceInterface#removeAll(org.bukkit.plugin.Plugin)
     */
    @Override
    public void removeAllExtensions(Plugin plugin)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * Communication handler for incoming network traffic-
     */
    private class MclibCoreHandler implements CommunicationPeerHandler
    {
        
        /**
         * Constructor
         */
        public MclibCoreHandler()
        {
            // empty
        }
        
        @Override
        public void handleIncomming(McPlayerInterface player, CommunicationEndpointId id, DataSection section)
        {
            // silently drop invalid messages.
            if (!section.contains("KEY")) //$NON-NLS-1$
                return;
            
            try
            {
                final String key = section.getString("KEY"); //$NON-NLS-1$
                if (key.equals(CoreMessages.ActionPerformed.name()))
                {
                    // forward to smart gui
                    ((McPlayerImpl) player).parseActionPerformed(section.getFragment(ActionPerformedData.class, "data")); //$NON-NLS-1$
                }
                else if (key.equals(CoreMessages.WinClosed.name()))
                {
                    // forward to smart gui
                    ((McPlayerImpl) player).parseWinClosed(section.getFragment(WinClosedData.class, "data")); //$NON-NLS-1$
                }
                else if (key.equals(CoreMessages.QueryFormRequest.name()))
                {
                    // client has the mod installed.
                    ((McPlayerImpl) player).parseFormRequest(section.getFragment(QueryFormRequestData.class, "data")); //$NON-NLS-1$
                }
                else if (key.equals(CoreMessages.Pong.name()))
                {
                    // client has the mod installed.
                    MclibPlugin.this.players.parsePong(player, section.getFragment(PongData.class, "data")); //$NON-NLS-1$
                }
                // otherwise silently ignore the invalid message.
            }
            catch (McException e)
            {
                MclibPlugin.this.getLogger().log(Level.WARNING, "Problems handling client core message", e); //$NON-NLS-1$
            }
        }
        
    }
    
    @Override
    public BungeeServerInterface getCurrent()
    {
        return this.currentBungee;
    }
    
    @Override
    public BungeeServerInterface getServer(String name)
    {
        synchronized (this.bungeeServers)
        {
            return this.bungeeServers.get(name);
        }
    }
    
    @Override
    public Iterable<BungeeServerInterface> getServers()
    {
        synchronized (this.bungeeServers)
        {
            return new ArrayList<>(this.bungeeServers.values());
        }
    }
    
    /**
     * The local bungee server (myself).
     */
    private final class LocalBungeeServer implements BungeeServerInterface
    {
        
        /**
         * Constructor
         */
        public LocalBungeeServer()
        {
            // empty
        }
        
        @Override
        public String getName()
        {
            return McCoreConfig.BungeeServerName.getString();
        }
        
        @Override
        public void send(CommunicationEndpointId id, DataSection... data)
        {
            // loop back
            if (data != null)
            {
                final CommunicationBungeeHandler handler = MclibPlugin.this.bungeeHandlers.get(id);
                if (handler != null)
                {
                    for (final DataSection section : data)
                    {
                        handler.handleIncomming(this, id, section);
                    }
                }
            }
        }

        @Override
        public void transferPlayer(McPlayerInterface player)
        {
            final ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect"); //$NON-NLS-1$
            out.writeUTF(this.getName());
            player.getBukkitPlayer().sendPluginMessage(MclibPlugin.this, BUNGEECORD_CHANNEL, out.toByteArray());
        }
        
    }
    
    /**
     * The remote bungee server (available through bungeecord).
     */
    private final class RemoteBungeeServer implements BungeeServerInterface
    {
        
        /** bungee coord name. */
        private String name;
        
        /**
         * Constructor
         * 
         * @param name
         */
        public RemoteBungeeServer(String name)
        {
            this.name = name;
        }
        
        @Override
        public String getName()
        {
            return this.name;
        }
        
        @Override
        public void send(CommunicationEndpointId id, DataSection... data)
        {
            // loop back
            if (data != null)
            {
                for (final DataSection section : data)
                {
                    // TODO only works if both servers have online players
                    // TODO if target servers do not have online players the messages should be queued by bungeecord.
                    // TODO ensure that we are really within bungeecord environments, else this will be sent to clients (not good)
                    final ByteArrayDataOutput out2 = ByteStreams.newDataOutput();
                    final ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out2.writeUTF("Forward"); //$NON-NLS-1$
                    out2.writeUTF(this.name);
                    out2.writeUTF(MCLIB_SERVER_TO_SERVER_CHANNEL);
                    out.writeByte(0);
                    new NetMessage(id, section).toBytes(out);
                    byte[] bytes = out.toByteArray();
                    if (MclibPlugin.this.getLogger().isLoggable(Level.FINEST))
                    {
                        MclibPlugin.this.getLogger().finest("Sending NetMessage to server " + this.name + "\n" + Arrays.toString(bytes)); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                    out2.writeShort(bytes.length);
                    out2.write(bytes);
                    final Collection<? extends Player> onlPlayers = Bukkit.getOnlinePlayers();
                    if (onlPlayers.size() > 0)
                    {
                        // bungee cord ensures this is not send to the client.
                        onlPlayers.iterator().next().sendPluginMessage(MclibPlugin.this, BUNGEECORD_CHANNEL, bytes);
                    }
                    else
                    {
                        MclibPlugin.this.bungeeQueue.add(p -> p.sendPluginMessage(MclibPlugin.this, BUNGEECORD_CHANNEL, bytes));
                    }
                }
            }
        }

        @Override
        public void transferPlayer(McPlayerInterface player)
        {
            // TODO for remote players support teleport to myself
        }
        
    }
    
}
