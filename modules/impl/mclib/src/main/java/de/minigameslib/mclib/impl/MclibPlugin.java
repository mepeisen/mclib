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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import de.minigames.mclib.nms.v18.NetworkManager1_8;
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
import de.minigameslib.mclib.api.cmd.CommandImpl;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.com.CommunicationBungeeHandler;
import de.minigameslib.mclib.api.com.CommunicationPeerHandler;
import de.minigameslib.mclib.api.com.ServerCommunicationServiceInterface;
import de.minigameslib.mclib.api.config.ConfigColorData;
import de.minigameslib.mclib.api.config.ConfigInterface;
import de.minigameslib.mclib.api.config.ConfigServiceInterface;
import de.minigameslib.mclib.api.config.ConfigVectorData;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.ext.ExtensionInterface;
import de.minigameslib.mclib.api.ext.ExtensionPointInterface;
import de.minigameslib.mclib.api.ext.ExtensionServiceInterface;
import de.minigameslib.mclib.api.gui.RawMessageInterface;
import de.minigameslib.mclib.api.items.BlockServiceInterface;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.items.ResourceServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.MessageServiceInterface;
import de.minigameslib.mclib.api.locale.MessagesConfigInterface;
import de.minigameslib.mclib.api.mcevent.ComponentCreateEvent;
import de.minigameslib.mclib.api.mcevent.ComponentCreatedEvent;
import de.minigameslib.mclib.api.mcevent.ComponentDeleteEvent;
import de.minigameslib.mclib.api.mcevent.ComponentDeletedEvent;
import de.minigameslib.mclib.api.mcevent.ComponentRelocateEvent;
import de.minigameslib.mclib.api.mcevent.ComponentRelocatedEvent;
import de.minigameslib.mclib.api.mcevent.EntityCreateEvent;
import de.minigameslib.mclib.api.mcevent.EntityCreatedEvent;
import de.minigameslib.mclib.api.mcevent.EntityDeleteEvent;
import de.minigameslib.mclib.api.mcevent.EntityDeletedEvent;
import de.minigameslib.mclib.api.mcevent.EntityEnteredZoneEvent;
import de.minigameslib.mclib.api.mcevent.EntityLeftZoneEvent;
import de.minigameslib.mclib.api.mcevent.ObjectCreateEvent;
import de.minigameslib.mclib.api.mcevent.ObjectCreatedEvent;
import de.minigameslib.mclib.api.mcevent.ObjectDeleteEvent;
import de.minigameslib.mclib.api.mcevent.ObjectDeletedEvent;
import de.minigameslib.mclib.api.mcevent.PlayerCloseGuiEvent;
import de.minigameslib.mclib.api.mcevent.PlayerDisplayGuiPageEvent;
import de.minigameslib.mclib.api.mcevent.PlayerEnteredZoneEvent;
import de.minigameslib.mclib.api.mcevent.PlayerGuiClickEvent;
import de.minigameslib.mclib.api.mcevent.PlayerLeftZoneEvent;
import de.minigameslib.mclib.api.mcevent.PlayerOpenGuiEvent;
import de.minigameslib.mclib.api.mcevent.SignCreateEvent;
import de.minigameslib.mclib.api.mcevent.SignCreatedEvent;
import de.minigameslib.mclib.api.mcevent.SignDeleteEvent;
import de.minigameslib.mclib.api.mcevent.SignDeletedEvent;
import de.minigameslib.mclib.api.mcevent.SignRelocateEvent;
import de.minigameslib.mclib.api.mcevent.SignRelocatedEvent;
import de.minigameslib.mclib.api.mcevent.ZoneCreateEvent;
import de.minigameslib.mclib.api.mcevent.ZoneCreatedEvent;
import de.minigameslib.mclib.api.mcevent.ZoneDeleteEvent;
import de.minigameslib.mclib.api.mcevent.ZoneDeletedEvent;
import de.minigameslib.mclib.api.mcevent.ZoneRelocateEvent;
import de.minigameslib.mclib.api.mcevent.ZoneRelocatedEvent;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.EntityIdInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.NpcServiceInterface;
import de.minigameslib.mclib.api.objects.ObjectIdInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.SignIdInterface;
import de.minigameslib.mclib.api.objects.ZoneIdInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.perms.PermissionServiceInterface;
import de.minigameslib.mclib.api.skin.SkinServiceInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.api.util.function.McSupplier;
import de.minigameslib.mclib.impl.cmd.MclibCommand;
import de.minigameslib.mclib.impl.com.PlayerProxy;
import de.minigameslib.mclib.impl.comp.ComponentId;
import de.minigameslib.mclib.impl.comp.EntityId;
import de.minigameslib.mclib.impl.comp.ObjectId;
import de.minigameslib.mclib.impl.comp.SignId;
import de.minigameslib.mclib.impl.comp.ZoneId;
import de.minigameslib.mclib.impl.items.ItemServiceImpl;
import de.minigameslib.mclib.impl.skin.SkinServiceImpl;
import de.minigameslib.mclib.impl.yml.YmlFile;
import de.minigameslib.mclib.nms.api.AnvilManagerInterface;
import de.minigameslib.mclib.nms.api.EntityHelperInterface;
import de.minigameslib.mclib.nms.api.EventBus;
import de.minigameslib.mclib.nms.api.EventSystemInterface;
import de.minigameslib.mclib.nms.api.InventoryManagerInterface;
import de.minigameslib.mclib.nms.api.ItemHelperInterface;
import de.minigameslib.mclib.nms.api.MgEventListener;
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
import de.minigameslib.mclib.shared.api.com.ColorDataFragment;
import de.minigameslib.mclib.shared.api.com.CommunicationEndpointId;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;
import de.minigameslib.mclib.shared.api.com.PlayerDataFragment;
import de.minigameslib.mclib.shared.api.com.UniqueEnumerationValue;
import de.minigameslib.mclib.shared.api.com.VectorDataFragment;

/**
 * Main spigot plugin class for MCLIB.
 * 
 * @author mepeisen
 */
public class MclibPlugin extends JavaPlugin implements Listener, ConfigServiceInterface, MessageServiceInterface, PermissionServiceInterface, McLibInterface, ServerCommunicationServiceInterface,
        ExtensionServiceInterface, PluginMessageListener, BungeeServiceInterface, MgEventListener
{
    
    /**
     * plugin channel for messages between servers and clients
     */
    private static final String                                          MCLIB_SERVER_TO_CLIENT_CHANNEL = "mclib|sc";                              //$NON-NLS-1$
    
    /**
     * plugin channel for messages between bungee servers
     */
    private static final String                                          MCLIB_SERVER_TO_SERVER_CHANNEL = "mclib|bc";                              //$NON-NLS-1$
    
    /**
     * plugin channel for BungeeCord
     */
    private static final String                                          BUNGEECORD_CHANNEL             = "BungeeCord";                            //$NON-NLS-1$
    
    /** the overall minecraft server versioon. */
    private static final MinecraftVersionsType                           SERVER_VERSION                 = MclibPlugin.getServerVersion();
    
    /** context helper. */
    private final McContextImpl                                          context                        = new McContextImpl();
    
    /** enum service helper. */
    private final EnumServiceImpl                                        enumService                    = new EnumServiceImpl();
    
    /** message service helper. */
    private MessageServiceInterface                                      msgService                     = new MessageServiceImpl(this.enumService);
    
    /**
     * configuration per plugin.
     */
    private final Map<Plugin, ConfigInterface>                           configurations                 = new HashMap<>();
    
    /**
     * configuration per plugin.
     */
    private final Map<Plugin, Map<File, ConfigInterface>>                configurationsPerDataFolder    = new HashMap<>();
    
    /**
     * configuration providers per plugin.
     */
    private final Map<Plugin, Map<Class<?>, McSupplier<File>>>           configProviders                = new HashMap<>();
    
    /** the player registry. */
    PlayerRegistry                                                       players;
    
    /** the objects manager. */
    ObjectsManager                                                       objectsManager;
    
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
    
    /** the event bus. */
    private EventBus                                                     eventBus;
    
    /** mclib chat command */
    private MclibCommand                                                 mclibCommand                   = new MclibCommand();
    
    /**
     * plugin instance
     */
    private static MclibPlugin                                           instance;
    
    /**
     * asynchronous thread pools
     */
    private final ExecutorService                                        executor                       = Executors.newFixedThreadPool(3);         // TODO configure threads
    
    /**
     * the item service impl
     */
    ItemServiceImpl                                                      itemService;
    
    static
    {
        if (MemoryDataSection.isFragmentImplementationLocked())
        {
            throw new IllegalStateException("Invalid class initialization"); //$NON-NLS-1$
        }
        MemoryDataSection.initFragmentImplementation(PlayerDataFragment.class, PlayerProxy.class);
        MemoryDataSection.initFragmentImplementation(McPlayerInterface.class, PlayerProxy.class);
        MemoryDataSection.initFragmentImplementation(ColorDataFragment.class, ConfigColorData.class);
        // TODO MemoryDataSection.initFragmentImplementation(ItemStackDataFragment.class, ConfigItemStackData.class);
        MemoryDataSection.initFragmentImplementation(VectorDataFragment.class, ConfigVectorData.class);
        
        MemoryDataSection.initFragmentImplementation(ObjectIdInterface.class, ObjectId.class);
        MemoryDataSection.initFragmentImplementation(ComponentIdInterface.class, ComponentId.class);
        MemoryDataSection.initFragmentImplementation(ZoneIdInterface.class, ZoneId.class);
        MemoryDataSection.initFragmentImplementation(SignIdInterface.class, SignId.class);
        MemoryDataSection.initFragmentImplementation(EntityIdInterface.class, EntityId.class);
        
        MemoryDataSection.initUniqueEnumValueFactory(MclibPlugin::create);
        MemoryDataSection.lockFragmentImplementations();
    }
    
    @Override
    public int getApiVersion()
    {
        return APIVERSION_1_0_0;
    }
    
    @Override
    public void onEnable()
    {
        instance = this;
        
        // detect server version
        detectServerVersionAndInstallNms();
        
        Bukkit.getPluginManager().registerEvents(this, this);
        
        // mclib enumerations
        this.enumService.registerEnumClass(this, CommonMessages.class);
        this.enumService.registerEnumClass(this, MclibCommand.Messages.class);
        this.enumService.registerEnumClass(this, MclibCommand.CommandPermissions.class);
        this.enumService.registerEnumClass(this, McCoreConfig.class);
        
        // public api services
        registerPublicServices();
        
        this.players = new PlayerRegistry(new File(this.getDataFolder(), "players")); //$NON-NLS-1$
        
        // item service
        initItemsAndBlocksAndResources();
        
        CommunicationEndpointId.CommunicationServiceCache.init(this);
        
        // nms services
        initNms();
        
        registerMclibEvents();
        
        initObjectsManager();
        
        // nms event listeners
        Bukkit.getPluginManager().registerEvents(Bukkit.getServicesManager().load(EventSystemInterface.class), this);
        Bukkit.getPluginManager().registerEvents(Bukkit.getServicesManager().load(InventoryManagerInterface.class), this);
        Bukkit.getPluginManager().registerEvents(Bukkit.getServicesManager().load(AnvilManagerInterface.class), this);
        
        // communication endpoints
        initNetworking();
        
        if (this.getMinecraftVersion().isAtLeast(MinecraftVersionsType.V1_8_R3))
        {
            Bukkit.getPluginManager().registerEvents(new ResourcePackListener(this.players, this.itemService), this);
        }
        MclibPlugin.this.itemService.init();
    }

    /**
     * 
     */
    private void initNetworking()
    {
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
        final ByteArrayDataOutput out2 = ByteStreams.newDataOutput();
        out2.writeUTF("GetServers"); //$NON-NLS-1$
        this.bungeeQueue.add(p -> p.sendPluginMessage(this, BUNGEECORD_CHANNEL, out2.toByteArray()));
        
        this.serversPing = new GetServersPing();
        this.serversPing.runTaskTimer(this, 20 * 5, 20 * 60); // once per minute
    }

    /**
     * 
     */
    private void initObjectsManager()
    {
        try
        {
            this.objectsManager = new ObjectsManager(this.getDataFolder(), this.players);
            Bukkit.getServicesManager().register(ObjectServiceInterface.class, this.objectsManager, this, ServicePriority.Highest);
            Bukkit.getServicesManager().register(NpcServiceInterface.class, this.objectsManager, this, ServicePriority.Highest);
        }
        catch (McException ex)
        {
            this.getLogger().log(Level.SEVERE, "Problems creating objects manager", ex); //$NON-NLS-1$
            // TODO what do we do at this point?
            // having no object manager will cause a dead plugin at all
        }
        new BukkitRunnable() {
            
            @Override
            public void run()
            {
                MclibPlugin.this.objectsManager.init();
            }
        }.runTaskLater(this, 1);
    }

    /**
     * 
     */
    private void initNms()
    {
        final NmsFactory factory = Bukkit.getServicesManager().load(NmsFactory.class);
        factory.create(ItemHelperInterface.class).initBlocks();
        Bukkit.getServicesManager().register(EventSystemInterface.class, factory.create(EventSystemInterface.class), this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(InventoryManagerInterface.class, factory.create(InventoryManagerInterface.class), this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(AnvilManagerInterface.class, factory.create(AnvilManagerInterface.class), this, ServicePriority.Highest);
        
        Bukkit.getServicesManager().load(EventSystemInterface.class).addEventListener(this);
        this.eventBus = Bukkit.getServicesManager().load(EventSystemInterface.class).createEventBus();
    }

    /**
     * 
     */
    private void registerMclibEvents()
    {
        this.registerEvent(this, ComponentCreatedEvent.class);
        this.registerEvent(this, ComponentCreateEvent.class);
        this.registerEvent(this, ComponentDeletedEvent.class);
        this.registerEvent(this, ComponentDeleteEvent.class);
        this.registerEvent(this, ComponentRelocatedEvent.class);
        this.registerEvent(this, ComponentRelocateEvent.class);
        this.registerEvent(this, EntityCreatedEvent.class);
        this.registerEvent(this, EntityCreateEvent.class);
        this.registerEvent(this, EntityDeletedEvent.class);
        this.registerEvent(this, EntityDeleteEvent.class);
        this.registerEvent(this, EntityEnteredZoneEvent.class);
        this.registerEvent(this, EntityLeftZoneEvent.class);
        this.registerEvent(this, ObjectCreatedEvent.class);
        this.registerEvent(this, ObjectCreateEvent.class);
        this.registerEvent(this, ObjectDeletedEvent.class);
        this.registerEvent(this, ObjectDeleteEvent.class);
        this.registerEvent(this, PlayerCloseGuiEvent.class);
        this.registerEvent(this, PlayerDisplayGuiPageEvent.class);
        this.registerEvent(this, PlayerEnteredZoneEvent.class);
        this.registerEvent(this, PlayerGuiClickEvent.class);
        this.registerEvent(this, PlayerLeftZoneEvent.class);
        this.registerEvent(this, PlayerOpenGuiEvent.class);
        this.registerEvent(this, SignCreatedEvent.class);
        this.registerEvent(this, SignCreateEvent.class);
        this.registerEvent(this, SignDeletedEvent.class);
        this.registerEvent(this, SignDeleteEvent.class);
        this.registerEvent(this, SignRelocatedEvent.class);
        this.registerEvent(this, SignRelocateEvent.class);
        this.registerEvent(this, ZoneCreatedEvent.class);
        this.registerEvent(this, ZoneCreateEvent.class);
        this.registerEvent(this, ZoneDeletedEvent.class);
        this.registerEvent(this, ZoneDeleteEvent.class);
        this.registerEvent(this, ZoneRelocatedEvent.class);
        this.registerEvent(this, ZoneRelocateEvent.class);
    }

    /**
     * 
     */
    private void initItemsAndBlocksAndResources()
    {
        this.enumService.registerEnumClass(this, CommonItems.class);
        this.itemService = new ItemServiceImpl();
        Bukkit.getServicesManager().register(ItemServiceInterface.class, this.itemService, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(ResourceServiceInterface.class, this.itemService, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(BlockServiceInterface.class, this.itemService, this, ServicePriority.Highest);
        new BukkitRunnable() {
            
            @Override
            public void run()
            {
                try
                {
                    MclibPlugin.this.itemService.createResourcePack(new File(MclibPlugin.this.getDataFolder(), "mclib_core_resources_v1.zip"), ResourceServiceInterface.ResourceVersion.PACK_FORMAT_1); //$NON-NLS-1$
                    MclibPlugin.this.itemService.createResourcePack(new File(MclibPlugin.this.getDataFolder(), "mclib_core_resources_v2.zip"), ResourceServiceInterface.ResourceVersion.PACK_FORMAT_2); //$NON-NLS-1$
                    MclibPlugin.this.itemService.createResourcePack(new File(MclibPlugin.this.getDataFolder(), "mclib_core_resources_v3.zip"), ResourceServiceInterface.ResourceVersion.PACK_FORMAT_3); //$NON-NLS-1$
                }
                catch (IOException e)
                {
                    MclibPlugin.this.getLogger().log(Level.WARNING, "Error creating resource pack", e); //$NON-NLS-1$
                }
            }
        }.runTaskLaterAsynchronously(this, 2);
    }

    /**
     * 
     */
    private void registerPublicServices()
    {
        Bukkit.getServicesManager().register(EnumServiceInterface.class, this.enumService, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(ConfigServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(MessageServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(PermissionServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(McContext.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(McLibInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(ServerCommunicationServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(ExtensionServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(BungeeServiceInterface.class, this, this, ServicePriority.Highest);
        
        Bukkit.getServicesManager().register(SkinServiceInterface.class, new SkinServiceImpl(this.executor), this, ServicePriority.Highest);
    }

    /**
     * 
     */
    private void detectServerVersionAndInstallNms()
    {
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
            final ObjectServiceInterface osi = ObjectServiceInterface.instance();
            final Optional<? extends Player> player = Bukkit.getOnlinePlayers().stream().filter(p -> !osi.isHuman(p)).findFirst();
            if (player.isPresent())
            {
                // bungee cord ensures this is not send to the client.
                final ByteArrayDataOutput out2 = ByteStreams.newDataOutput();
                out2.writeUTF("GetServers"); //$NON-NLS-1$
                player.get().sendPluginMessage(MclibPlugin.this, BUNGEECORD_CHANNEL, out2.toByteArray());
            }
        }
        
    }
    
    @Override
    public void onDisable()
    {
        this.objectsManager.disable();
        this.enumService.unregisterAllEnumerations(this);
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
     * World loaded.
     * 
     * @param evt
     */
    @EventHandler
    public void onWorldLoaded(WorldLoadEvent evt)
    {
        this.objectsManager.onWorldLoaded(evt.getWorld());
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
        return this.msgService.getMessagesFromMsg(item);
    }
    
    /**
     * Hook into message service.
     * 
     * @param func
     */
    public void hookMessageService(Function<MessageServiceInterface, MessageServiceInterface> func)
    {
        this.msgService = func.apply(this.msgService);
    }
    
    @Override
    public ConfigInterface getConfigFromCfg(ConfigurationValueInterface item)
    {
        final Plugin plugin = this.enumService.getPlugin(item);
        if (plugin == null)
        {
            return null;
        }
        final Map<Class<?>, McSupplier<File>> map = this.configProviders.get(plugin);
        if (map != null)
        {
            final McSupplier<File> supplier = map.get(item.getClass());
            if (supplier != null)
            {
                try
                {
                    final File dataFolder = supplier.get();
                    return this.configurationsPerDataFolder.computeIfAbsent(plugin, key -> new HashMap<>()).computeIfAbsent(dataFolder, key -> new ConfigImpl(dataFolder, this.enumService));
                }
                catch (McException e)
                {
                    this.getLogger().log(Level.WARNING, "Exception while fetching custom config", e); //$NON-NLS-1$
                    throw new IllegalStateException("Exception while fetching custom config", e); //$NON-NLS-1$
                }
            }
        }
        return this.configurations.computeIfAbsent(plugin, (key) -> new ConfigImpl(plugin, this.enumService));
    }
    
    @Override
    public void registerFileProvider(Plugin plugin, Class<? extends ConfigurationValueInterface> clazz, McSupplier<File> provider)
    {
        this.configProviders.computeIfAbsent(plugin, (key) -> new HashMap<>()).put(clazz, provider);
    }
    
    // events
    
    /**
     * Player join event
     * 
     * @param evt
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent evt)
    {
        final Player player = evt.getPlayer();
        
        if (ObjectServiceInterface.instance().isHuman(player))
            return;
        
        while (!this.bungeeQueue.isEmpty())
        {
            this.bungeeQueue.poll().accept(player);
        }
        
        if (this.getMinecraftVersion().isBelow(MinecraftVersionsType.V1_8_R3))
        {
            NetworkManager1_8.hookResourcePackStatus(player, new ResourcePackHandler(this.players, this.itemService));
        }
        
        // TODO Why do we need to register bungeecord here???
        // TODO Disable Server-To-Server and BungeeCord in Non-BungeeCord environments
        final PlayerManagerInterface playerManager = Bukkit.getServicesManager().load(NmsFactory.class).create(PlayerManagerInterface.class);
        playerManager.registerChannelEx(player, MCLIB_SERVER_TO_CLIENT_CHANNEL);
        playerManager.registerChannelEx(player, MCLIB_SERVER_TO_SERVER_CHANNEL);
        playerManager.registerChannelEx(player, BUNGEECORD_CHANNEL);
        this.players.onPlayerJoin(evt);
        
        // try to ping the client mod.
        final PingData ping = new PingData();
        ping.setApi(this.getApiVersion());
        this.itemService.populate(ping);
        final DataSection section = new MemoryDataSection();
        section.set("KEY", CoreMessages.Ping.name()); //$NON-NLS-1$
        ping.write(section.createSection("data")); //$NON-NLS-1$
        this.players.getPlayer(player).sendToClient(MclibCommunication.ClientServerCore, section);
        
        // resources
        if (ResourceServiceInterface.instance().isAutoResourceDownload())
        {
            new BukkitRunnable() {
                
                @Override
                public void run()
                {
                    if (player.isOnline())
                    {
                        player.setResourcePack(ResourceServiceInterface.instance().getDownloadUrl());
                    }
                }
            }.runTaskLater(this, ResourceServiceInterface.instance().getAutoResourceTicks());
        }
        
        // clear inventory
        new BukkitRunnable() {
            
            @Override
            public void run()
            {
                MclibPlugin.this.itemService.clearTools(player.getInventory());
            }
        }.runTaskLater(this, 2);
        
        // hide dummy humans
        new BukkitRunnable() {
            
            @Override
            public void run()
            {
                if (player.isOnline())
                {
                    final NmsFactory factory = Bukkit.getServicesManager().load(NmsFactory.class);
                    factory.create(EntityHelperInterface.class).playerOnline(player);
                }
            }
        }.runTaskLater(this, 10);
    }
    
    /**
     * Player quit event.
     * 
     * @param evt
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent evt)
    {
        if (!ObjectServiceInterface.instance().isHuman(evt.getPlayer()))
        {
            final NmsFactory factory = Bukkit.getServicesManager().load(NmsFactory.class);
            factory.create(EntityHelperInterface.class).playerOffline(evt.getPlayer());
            this.players.onPlayerQuit(evt);
        }
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
    public Collection<Locale> getMainLocales()
    {
        return Arrays.stream(McCoreConfig.MainLocales.getStringList()).map(Locale::new).collect(Collectors.toList());
    }
    
    @Override
    public void removeMainLocale(Locale locale) throws McException
    {
        final String[] main = McCoreConfig.MainLocales.getStringList();
        if (main.length == 1 && main[0].equals(locale.toString()))
        {
            // TODO throw exception
        }
        McCoreConfig.MainLocales.setStringList(Arrays.stream(main).filter(p -> p.equals(locale.toString())).toArray(String[]::new));
        McCoreConfig.MainLocales.saveConfig();
    }
    
    @Override
    public void addMainLocale(Locale locale) throws McException
    {
        final String[] main = McCoreConfig.MainLocales.getStringList();
        for (final String l : main)
            if (l.equals(locale.toString()))
                return;
        final String[] result = Arrays.copyOf(main, main.length + 1);
        result[main.length] = locale.toString();
        McCoreConfig.MainLocales.setStringList(result);
        McCoreConfig.MainLocales.saveConfig();
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
            this.enumService.unregisterAllEnumerations(evt.getPlugin());
            this.objectsManager.onDisable(evt.getPlugin());
            this.players.onDisable(evt.getPlugin());
            this.eventBus.onDisable(evt.getPlugin());
            this.configurations.remove(evt.getPlugin());
            this.configProviders.remove(evt.getPlugin());
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
            this.itemService.onEnable(evt.getPlugin());
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (command.getName().equals("mclib")) //$NON-NLS-1$
        {
            if (args != null && args.length == 2 && args[0].equals("rawcommand") && sender instanceof Player) //$NON-NLS-1$
            {
                final McPlayerImpl player = this.players.getPlayer((Player) sender);
                try
                {
                    final UUID uuid = UUID.fromString(args[1]);
                    player.onRawCommand(uuid);
                }
                catch (IllegalArgumentException ex)
                {
                    this.getLogger().log(Level.FINE, "Problems parsing rawmessage. no uuid: " + args[1], ex); //$NON-NLS-1$
                }
            }
            else
            {
                CommandImpl.onCommand(sender, command, label, args, this.mclibCommand);
            }
        }
        return false;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        if (command.getName().equals("mclib")) //$NON-NLS-1$
        {
            return CommandImpl.onTabComplete(sender, command, alias, args, this.mclibCommand);
        }
        return null;
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
                final ObjectServiceInterface osi = ObjectServiceInterface.instance();
                final Optional<? extends Player> player = Bukkit.getOnlinePlayers().stream().filter(p -> !osi.isHuman(p)).findFirst();
                if (player.isPresent())
                {
                    // bungee cord ensures this is not send to the client.
                    player.get().sendPluginMessage(this, BUNGEECORD_CHANNEL, bytes);
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
    
    /**
     * Sign delete event
     * 
     * @param evt
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSignDelete(BlockBreakEvent evt)
    {
        this.objectsManager.onSignDelete(evt);
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
            final McPlayerInterface mcp = this.players.getPlayer(player);
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
    
    @Override
    public <T extends ExtensionInterface> void register(Plugin plugin, ExtensionPointInterface<T> extPoint, T extension)
    {
        // TODO support extensions
    }
    
    @Override
    public <T extends ExtensionInterface> void remove(Plugin plugin, ExtensionPointInterface<T> extPoint, T extension)
    {
        // TODO support extensions
    }
    
    @Override
    public <T extends ExtensionInterface> Iterable<T> getExtensions(ExtensionPointInterface<? extends T> extPoint)
    {
        // TODO support extensions
        return null;
    }
    
    @Override
    public void removeAllExtensions(Plugin plugin)
    {
        // TODO support extensions
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
    public Collection<BungeeServerInterface> getServers()
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
            // TODO for remote players support teleport to myself
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
                    final ObjectServiceInterface osi = ObjectServiceInterface.instance();
                    final Optional<? extends Player> player = Bukkit.getOnlinePlayers().stream().filter(p -> !osi.isHuman(p)).findFirst();
                    if (player.isPresent())
                    {
                        // bungee cord ensures this is not send to the client.
                        player.get().sendPluginMessage(MclibPlugin.this, BUNGEECORD_CHANNEL, bytes);
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
            final ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect"); //$NON-NLS-1$
            out.writeUTF(this.getName());
            player.getBukkitPlayer().sendPluginMessage(MclibPlugin.this, BUNGEECORD_CHANNEL, out.toByteArray());
        }
        
    }
    
    @Override
    public RawMessageInterface createRaw()
    {
        return new RawMessage();
    }
    
    @Override
    public BukkitTask runTask(Plugin plugin, ContextRunnable task) throws IllegalArgumentException
    {
        return this.context.runTask(plugin, task);
    }
    
    @Override
    public BukkitTask runTaskAsynchronously(Plugin plugin, ContextRunnable task) throws IllegalArgumentException
    {
        return this.context.runTaskAsynchronously(plugin, task);
    }
    
    @Override
    public BukkitTask runTaskLater(Plugin plugin, long delay, ContextRunnable task) throws IllegalArgumentException
    {
        return this.context.runTaskLater(plugin, delay, task);
    }
    
    @Override
    public BukkitTask runTaskLaterAsynchronously(Plugin plugin, long delay, ContextRunnable task) throws IllegalArgumentException
    {
        return this.context.runTaskLaterAsynchronously(plugin, delay, task);
    }
    
    @Override
    public BukkitTask runTaskTimer(Plugin plugin, long delay, long period, ContextRunnable task) throws IllegalArgumentException
    {
        return this.context.runTaskTimer(plugin, delay, period, task);
    }
    
    @Override
    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, long delay, long period, ContextRunnable task) throws IllegalArgumentException
    {
        return this.context.runTaskLaterAsynchronously(plugin, delay, task);
    }
    
    @Override
    public void runInNewContext(Event event, CommandInterface command, McPlayerInterface player, ZoneInterface zone, ComponentInterface component, McRunnable runnable) throws McException
    {
        this.context.runInNewContext(event, command, player, zone, component, runnable);
    }
    
    @Override
    public <T> T calculateInNewContext(Event event, CommandInterface command, McPlayerInterface player, ZoneInterface zone, ComponentInterface component, McSupplier<T> runnable) throws McException
    {
        return this.context.calculateInNewContext(event, command, player, zone, component, runnable);
    }
    
    @Override
    public <Evt extends MinecraftEvent<?, Evt>> void registerHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> handler)
    {
        this.eventBus.registerHandler(plugin, clazz, handler);
    }
    
    @Override
    public void registerHandlers(Plugin plugin, McListener listener)
    {
        this.eventBus.registerHandlers(plugin, listener);
    }
    
    @Override
    public <Evt extends MinecraftEvent<?, Evt>> void unregisterHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> handler)
    {
        this.eventBus.unregisterHandler(plugin, clazz, handler);
    }
    
    @Override
    public void unregisterHandlers(Plugin plugin, McListener listener)
    {
        this.eventBus.unregisterHandlers(plugin, listener);
    }
    
    @Override
    public <T extends Event, Evt extends MinecraftEvent<T, Evt>> void handle(Class<Evt> eventClass, Evt event)
    {
        this.eventBus.handle(eventClass, event);
    }
    
    @Override
    public DataSection readYmlFile(File file) throws IOException
    {
        return new YmlFile(file);
    }
    
    @Override
    public DataSection readYmlFile(InputStream file) throws IOException
    {
        return new YmlFile(file);
    }
    
    @Override
    public void saveYmlFile(DataSection section, File file) throws IOException
    {
        if (section instanceof YmlFile)
        {
            ((YmlFile) section).saveFile(file);
        }
        else
        {
            final YmlFile yml = new YmlFile();
            section.getValues(true).forEach(yml::set);
            yml.saveFile(file);
        }
    }
    
    /**
     * enum value factory method
     * 
     * @param plugin
     * @param name
     * @param clazz
     * @return enumeration value or {@code null} if it does not exist
     */
    private static <T extends UniqueEnumerationValue> T create(String plugin, String name, Class<T> clazz)
    {
        return instance.enumService.create(plugin, name, clazz);
    }
    
    @Override
    public <Evt extends Event & MinecraftEvent<Evt, Evt>> void registerEvent(Plugin plugin, Class<Evt> clazz)
    {
        Bukkit.getServicesManager().load(EventSystemInterface.class).registerEvent(plugin, clazz);
    }
    
}
