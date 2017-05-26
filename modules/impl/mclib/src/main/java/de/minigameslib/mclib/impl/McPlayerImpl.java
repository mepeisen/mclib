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
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McContext;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.McStorage;
import de.minigameslib.mclib.api.cli.ClientInterface;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.McPlayerMoveEvent;
import de.minigameslib.mclib.api.event.McPlayerTeleportEvent;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.gui.AnvilGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.gui.GuiType;
import de.minigameslib.mclib.api.gui.RawMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.mcevent.PlayerEnteredZoneEvent;
import de.minigameslib.mclib.api.mcevent.PlayerLeftZoneEvent;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mclib.api.perms.PermissionsInterface;
import de.minigameslib.mclib.api.util.function.FalseStub;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.api.util.function.TrueStub;
import de.minigameslib.mclib.impl.RawMessage.RawAction;
import de.minigameslib.mclib.impl.comp.ZoneId;
import de.minigameslib.mclib.impl.gui.cfg.QueryText;
import de.minigameslib.mclib.impl.player.MclibPlayersConfig;
import de.minigameslib.mclib.impl.yml.YmlFile;
import de.minigameslib.mclib.nms.api.ChatSystemInterface;
import de.minigameslib.mclib.nms.api.EventBus;
import de.minigameslib.mclib.nms.api.EventSystemInterface;
import de.minigameslib.mclib.nms.api.MgEventListener;
import de.minigameslib.mclib.nms.api.NmsFactory;
import de.minigameslib.mclib.pshared.ActionPerformedData;
import de.minigameslib.mclib.pshared.PongData;
import de.minigameslib.mclib.pshared.QueryFormRequestData;
import de.minigameslib.mclib.pshared.WinClosedData;
import de.minigameslib.mclib.shared.api.com.CommunicationEndpointId;
import de.minigameslib.mclib.shared.api.com.DataFragment;
import de.minigameslib.mclib.shared.api.com.DataSection;
import net.jodah.expiringmap.ExpiringMap;

/**
 * Implementation of arena players.
 * 
 * @author mepeisen
 *
 */
class McPlayerImpl implements McPlayerInterface, MgEventListener, ClientInterface
{
    
    /** logger. */
    static final Logger                         LOGGER           = Logger.getLogger(McPlayerImpl.class.getName());
    
    /** players uuid. */
    private UUID                                uuid;
    
    /** the players name. */
    private String                              name;
    
    /** the session storage. */
    private StorageImpl                         sessionStorage   = new StorageImpl();
    
    /** persistent player configuration. */
    private MclibPlayersConfig                  config           = new MclibPlayersConfig();
    
    /** persistent storage file. */
    YmlFile                                     persistentStorage;
    
    /** persistent storage file. */
    private File                                persistentStorageFile;
    
    /** the persistent storage. */
    private PersistentStorageImpl               pstorage         = new PersistentStorageImpl();
    
    /**
     * {@code true} if the client mod is installed.
     */
    private boolean                             hasForgeMod;
    
    /** list of client extensions that were reported by client mod. */
    private Map<String, Integer>                clientExtensions = new HashMap<>();
    
    /**
     * the client api version.
     */
    private int                                 clientApi        = -1;
    
    /** the raw actions. */
    private final ExpiringMap<UUID, McRunnable> rawActions       = ExpiringMap.builder().variableExpiration().build();
    
    /** an event bus to handle events. */
    private final EventBus                      eventBus         = Bukkit.getServicesManager().load(EventSystemInterface.class).createEventBus();
    
    /**
     * the players zone manager.
     */
    private final ZoneManager                   zoneManager      = new PlayerZoneManager();
    
    /**
     * Constructor.
     * 
     * @param uuid
     *            players uuid
     * @param persistentStorage
     *            the persistent storage file
     */
    public McPlayerImpl(UUID uuid, File persistentStorage)
    {
        this.uuid = uuid;
        final OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if (player != null)
        {
            this.name = player.getName();
        }
        this.persistentStorageFile = persistentStorage;
        this.persistentStorage = new YmlFile();
        if (persistentStorage.exists())
        {
            try (final FileReader fr = new FileReader(persistentStorage))
            {
                this.persistentStorage.load(fr);
            }
            catch (IOException ex)
            {
                LOGGER.log(Level.WARNING, "Problems loading player storage from file; player uuid " + this.uuid.toString(), ex); //$NON-NLS-1$
            }
        }
        else
        {
            this.config.write(this.persistentStorage.createSection("core")); //$NON-NLS-1$
            this.persistentStorage.createSection("storage"); //$NON-NLS-1$
            this.saveStore();
        }
    }
    
    /**
     * Fire bukkit event for entering new zones.
     * 
     * @param newZones
     *            the new zones
     */
    protected void fireZonesEntered(Set<ZoneId> newZones)
    {
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        for (final ZoneId id : newZones)
        {
            final PlayerEnteredZoneEvent event = new PlayerEnteredZoneEvent(osi.findZone(id), this);
            Bukkit.getPluginManager().callEvent(event);
        }
    }
    
    /**
     * Fire bukkit event for leaving zones.
     * 
     * @param oldZones
     *            the zones that were left
     */
    protected void fireZonesLeft(Set<ZoneId> oldZones)
    {
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        for (final ZoneId id : oldZones)
        {
            final PlayerLeftZoneEvent event = new PlayerLeftZoneEvent(osi.findZone(id), this);
            Bukkit.getPluginManager().callEvent(event);
        }
    }
    
    /**
     * Parses the pong data from client.
     * 
     * @param fragment
     *            data fragment
     */
    void parsePong(PongData fragment)
    {
        this.hasForgeMod = true;
        if (fragment != null)
        {
            this.clientExtensions.putAll(fragment.getClientExtensions());
            this.clientApi = fragment.getApi();
        }
    }
    
    /**
     * Saves the persistent storage.
     */
    void saveStore()
    {
        try
        {
            this.persistentStorage.saveFile(this.persistentStorageFile);
        }
        catch (IOException ex)
        {
            LOGGER.log(Level.WARNING, "Problems saving player storage to file; player uuid " + this.uuid.toString(), ex); //$NON-NLS-1$
        }
    }
    
    @Override
    public Player getBukkitPlayer()
    {
        return Bukkit.getPlayer(this.uuid);
    }
    
    @Override
    public String getName()
    {
        return this.name;
    }
    
    @Override
    public OfflinePlayer getOfflinePlayer()
    {
        return Bukkit.getOfflinePlayer(this.uuid);
    }
    
    @Override
    public UUID getPlayerUUID()
    {
        return this.uuid;
    }
    
    @Override
    public String[] encodeMessage(LocalizedMessageInterface msg, Serializable... args)
    {
        return McLibInterface.instance().calculateInCopiedContextUnchecked(() -> {
            McLibInterface.instance().setContext(McPlayerInterface.class, this);
            final OfflinePlayer player = this.getOfflinePlayer();
            String[] msgs = null;
            if (msg.isSingleLine())
            {
                msgs = new String[] { player.isOp() ? msg.toAdminMessage(this.getPreferredLocale(), args) : msg.toUserMessage(this.getPreferredLocale(), args) };
            }
            else
            {
                msgs = player.isOp() ? msg.toAdminMessageLine(this.getPreferredLocale(), args) : msg.toUserMessageLine(this.getPreferredLocale(), args);
            }
            
            final String[] result = new String[msgs.length];
            
            for (int i = 0; i < result.length; i++)
            {
                // TODO use semantic colors
                // TODO allow override of semantic colors via server config
                switch (msg.getSeverity())
                {
                    default:
                    case Neutral:
                        result[i] = msgs[i];
                        break;
                    case Error:
                        result[i] = ChatColor.DARK_RED + msgs[i];
                        break;
                    case Information:
                        result[i] = ChatColor.WHITE + msgs[i];
                        break;
                    case Loser:
                        result[i] = ChatColor.RED + msgs[i];
                        break;
                    case Success:
                        result[i] = ChatColor.GREEN + msgs[i];
                        break;
                    case Warning:
                        result[i] = ChatColor.YELLOW + msgs[i];
                        break;
                    case Winner:
                        result[i] = ChatColor.GOLD + msgs[i];
                        break;
                }
            }
            return result;
        });
    }
    
    @Override
    public void sendMessage(LocalizedMessageInterface msg, Serializable... args)
    {
        final Player player = this.getBukkitPlayer();
        if (player != null)
        {
            final String[] msgs = this.encodeMessage(msg, args);
            
            for (final String smsg : msgs)
            {
                player.sendMessage(smsg);
            }
        }
    }
    
    @Override
    public Locale getPreferredLocale()
    {
        return this.config.getPreferredLocale();
    }
    
    @Override
    public void setPreferredLocale(Locale locale) throws McException
    {
        this.config.setPreferredLocale(locale);
        this.config.write(this.persistentStorage.createSection("core")); //$NON-NLS-1$
        this.saveStore();
    }
    
    @Override
    public boolean checkPermission(PermissionsInterface perm)
    {
        final Player player = this.getBukkitPlayer();
        return player == null ? false : player.hasPermission(perm.fullPath());
    }
    
    @Override
    public McOutgoingStubbing<McPlayerInterface> when(McPredicate<McPlayerInterface> test) throws McException
    {
        if (test.test(this))
        {
            return new TrueStub<>(this);
        }
        return new FalseStub<>(this);
    }
    
    @Override
    public McStorage getContextStorage()
    {
        final McContext context = Bukkit.getServicesManager().load(McContext.class);
        ContextStorage ctxStorage = context.getContext(ContextStorage.class);
        if (ctxStorage == null)
        {
            ctxStorage = new ContextStorage();
            context.setContext(ContextStorage.class, ctxStorage);
        }
        return ctxStorage.computeIfAbsent(this.uuid, (key) -> new StorageImpl());
    }
    
    @Override
    public McStorage getSessionStorage()
    {
        return this.sessionStorage;
    }
    
    @Override
    public McStorage getPersistentStorage()
    {
        return this.pstorage;
    }
    
    /**
     * The zone manager for players.
     * 
     * @author mepeisen
     *
     */
    private final class PlayerZoneManager extends ZoneManager
    {
        /**
         * Constructor.
         */
        public PlayerZoneManager()
        {
            // empty
        }
        
        @Override
        protected void fireZonesEntered(Set<ZoneId> newZones)
        {
            McPlayerImpl.this.fireZonesEntered(newZones);
        }
        
        @Override
        protected void fireZonesLeft(Set<ZoneId> oldZones)
        {
            McPlayerImpl.this.fireZonesLeft(oldZones);
        }
    }
    
    /**
     * The persistent storage impl.
     * 
     * @author mepeisen
     */
    private final class PersistentStorageImpl implements McStorage
    {
        
        /**
         * Constructor.
         */
        public PersistentStorageImpl()
        {
            // empty
        }
        
        @Override
        public <T extends DataFragment> T get(Class<T> clazz)
        {
            final DataSection section = McPlayerImpl.this.persistentStorage.getSection("storage." + clazz.getName()); //$NON-NLS-1$
            if (section != null)
            {
                try
                {
                    final T result = clazz.newInstance();
                    result.read(section);
                    return result;
                }
                catch (InstantiationException | IllegalAccessException ex)
                {
                    McPlayerImpl.LOGGER.log(Level.WARNING, "Problems saving player storage to file; player uuid " + McPlayerImpl.this.getPlayerUUID().toString(), ex); //$NON-NLS-1$
                }
            }
            return null;
        }
        
        @Override
        public <T extends DataFragment> void set(Class<T> clazz, T value)
        {
            final DataSection section = McPlayerImpl.this.persistentStorage.createSection("storage." + clazz.getName()); //$NON-NLS-1$
            value.write(section);
            McPlayerImpl.this.saveStore();
        }
        
    }
    
    /**
     * Helper for context storage.
     * 
     * @author mepeisen
     */
    private static final class ContextStorage extends HashMap<UUID, StorageImpl>
    {
        
        /**
         * serial version uid.
         */
        private static final long serialVersionUID = 3803764167708189047L;
        
        /**
         * Constructor.
         */
        public ContextStorage()
        {
            // empty
        }
        
    }
    
    /**
     * Simple implementation of storage map.
     * 
     * @author mepeisen
     */
    private static final class StorageImpl implements McStorage
    {
        
        /** the underlying data map. */
        private final Map<Class<?>, DataFragment> data = new HashMap<>();
        
        /**
         * Constructor.
         */
        public StorageImpl()
        {
            // empty
        }
        
        @Override
        public <T extends DataFragment> T get(Class<T> clazz)
        {
            return clazz.cast(this.data.get(clazz));
        }
        
        @Override
        public <T extends DataFragment> void set(Class<T> clazz, T value)
        {
            this.data.put(clazz, value);
        }
        
    }
    
    @Override
    public GuiSessionInterface getGuiSession()
    {
        final GuiSessionInterface session = this.getSessionStorage().get(GuiSessionInterface.class);
        if (session != null && session.getCurrentType() != GuiType.None)
        {
            return session;
        }
        return null;
    }
    
    @Override
    public GuiSessionInterface openClickGui(ClickGuiInterface gui) throws McException
    {
        final McStorage storage = this.getSessionStorage();
        final GuiSessionInterface oldSession = storage.get(GuiSessionInterface.class);
        if (oldSession != null && oldSession.getCurrentType() != GuiType.None)
        {
            oldSession.close();
        }
        final GuiSessionInterface newSession = new GuiSessionImpl(gui, this);
        storage.set(GuiSessionInterface.class, newSession);
        return newSession;
    }
    
    @Override
    public GuiSessionInterface nestClickGui(ClickGuiInterface gui) throws McException
    {
        final McStorage storage = this.getSessionStorage();
        final GuiSessionInterface oldSession = storage.get(GuiSessionInterface.class);
        if (oldSession != null && oldSession.getCurrentType() != GuiType.None)
        {
            ((GuiSessionImpl) oldSession).pause();
        }
        final GuiSessionImpl newSession = new GuiSessionImpl(gui, this);
        newSession.setPrevSession(oldSession);
        storage.set(GuiSessionInterface.class, newSession);
        return newSession;
    }
    
    @Override
    public GuiSessionInterface openAnvilGui(AnvilGuiInterface gui) throws McException
    {
        final McStorage storage = this.getSessionStorage();
        final GuiSessionInterface oldSession = storage.get(GuiSessionInterface.class);
        if (oldSession != null && oldSession.getCurrentType() != GuiType.None)
        {
            oldSession.close();
        }
        final GuiSessionInterface newSession = new GuiSessionImpl(gui, this);
        storage.set(GuiSessionInterface.class, newSession);
        return newSession;
    }
    
    @Override
    public GuiSessionInterface nestAnvilGui(AnvilGuiInterface gui) throws McException
    {
        final McStorage storage = this.getSessionStorage();
        final GuiSessionInterface oldSession = storage.get(GuiSessionInterface.class);
        if (oldSession != null && oldSession.getCurrentType() != GuiType.None)
        {
            ((GuiSessionImpl) oldSession).pause();
        }
        final GuiSessionImpl newSession = new GuiSessionImpl(gui, this);
        newSession.setPrevSession(oldSession);
        storage.set(GuiSessionInterface.class, newSession);
        return newSession;
    }
    
    /**
     * Player quit event.
     */
    public void onPlayerQuit()
    {
        // clear session storage
        this.sessionStorage = new StorageImpl();
        if (this.getGuiSession() != null)
        {
            this.onCloseGui();
        }
        // clear client information
        this.hasForgeMod = false;
        this.clientExtensions.clear();
        // clear handlers
        this.eventBus.clear();
        this.zoneManager.registerMovement(null);
    }
    
    /**
     * Player join event.
     */
    public void onPlayerJoin()
    {
        // clear session storage
        this.sessionStorage = new StorageImpl();
        this.zoneManager.registerMovement(null);
    }
    
    /**
     * Client closed the gui.
     */
    public void onCloseGui()
    {
        final McStorage storage = this.getSessionStorage();
        storage.set(GuiSessionInterface.class, null);
    }
    
    @Override
    public boolean hasSmartGui()
    {
        return this.hasForgeMod;
    }
    
    @Override
    public GuiSessionInterface openSmartGui() throws McException
    {
        if (!this.hasSmartGui())
        {
            throw new McException(CommonMessages.NoSmartGui);
        }
        
        final McStorage storage = this.getSessionStorage();
        GuiSessionInterface session = storage.get(GuiSessionInterface.class);
        if (session == null)
        {
            session = new GuiSessionImpl(this);
            storage.set(GuiSessionInterface.class, session);
        }
        else if (session.getCurrentType() != GuiType.SmartGui)
        {
            session.close();
            session = new GuiSessionImpl(this);
            storage.set(GuiSessionInterface.class, session);
        }
        return session;
    }
    
    /**
     * Parse an action performed message.
     * 
     * @param fragment
     *            data fragment
     * @throws McException
     *             thrown from action handler.
     */
    void parseActionPerformed(ActionPerformedData fragment) throws McException
    {
        final String actionId = fragment.getActionId();
        final McStorage storage = this.getSessionStorage();
        GuiSessionInterface session = storage.get(GuiSessionInterface.class);
        ((GuiSessionImpl) session).sguiActionPerformed(fragment.getWinId(), actionId, fragment.getData());
    }
    
    /**
     * Parse a win closed message.
     * 
     * @param fragment
     *            data fragment
     * @throws McException
     *             thrown from close handler.
     */
    void parseWinClosed(WinClosedData fragment) throws McException
    {
        final McStorage storage = this.getSessionStorage();
        GuiSessionInterface session = storage.get(GuiSessionInterface.class);
        ((GuiSessionImpl) session).sguiWinClosed(fragment.getWinId());
    }
    
    /**
     * Parse a form request message.
     * 
     * @param fragment
     *            data fragment
     * @throws McException
     *             thrown from data supplier.
     */
    void parseFormRequest(QueryFormRequestData fragment) throws McException
    {
        final McStorage storage = this.getSessionStorage();
        GuiSessionInterface session = storage.get(GuiSessionInterface.class);
        ((GuiSessionImpl) session).sguiFormRequest(fragment);
    }
    
    @Override
    public void sendToClient(CommunicationEndpointId endpoint, DataSection... data)
    {
        try
        {
            McLibInterface.instance().runInNewContext(() ->
            {
                McLibInterface.instance().setContext(McPlayerInterface.class, this);
                endpoint.send(data);
            });
        }
        catch (McException ex)
        {
            LOGGER.log(Level.WARNING, "Exception sending message to client", ex); //$NON-NLS-1$
        }
    }
    
    @Override
    public String getDisplayName()
    {
        final Player bukkit = this.getBukkitPlayer();
        return bukkit == null ? this.getOfflinePlayer().getName() : bukkit.getDisplayName();
    }
    
    @Override
    public void read(DataSection section)
    {
        // we do not read this directly. Instead a proxy is used.
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void write(DataSection section)
    {
        section.set("uuid", this.getPlayerUUID().toString()); //$NON-NLS-1$
        section.set("name", this.getDisplayName()); //$NON-NLS-1$
    }
    
    @Override
    public boolean test(DataSection section)
    {
        // we do not read this directly. Instead a proxy is used.
        return false;
    }
    
    @Override
    public ZoneInterface getZone()
    {
        return this.zoneManager.getZone();
    }
    
    @Override
    public ZoneInterface getZone(ZoneTypeId... type)
    {
        return this.zoneManager.getZone(type);
    }
    
    @Override
    public boolean isInsideZone(ZoneInterface zone)
    {
        return this.zoneManager.isInsideZone(zone);
    }
    
    @Override
    public boolean isInsideRandomZone(ZoneInterface... zone)
    {
        return this.zoneManager.isInsideRandomZone(zone);
    }
    
    @Override
    public boolean isInsideRandomZone(ZoneTypeId... type)
    {
        return this.zoneManager.isInsideRandomZone(type);
    }
    
    @Override
    public boolean isInsideAllZones(ZoneInterface... zone)
    {
        return this.zoneManager.isInsideAllZones(zone);
    }
    
    @Override
    public Collection<ZoneInterface> getZones()
    {
        return this.zoneManager.getZones();
    }
    
    @Override
    public Collection<ZoneInterface> getZones(ZoneTypeId... type)
    {
        return this.zoneManager.getZones(type);
    }
    
    @Override
    public void sendRaw(RawMessageInterface raw) throws McException
    {
        final RawMessage message = (RawMessage) raw;
        final String json = message.toJson(this);
        
        for (final Map.Entry<UUID, RawAction> action : message.actions.entrySet())
        {
            final long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), action.getValue().getExpires());
            this.rawActions.put(action.getKey(), action.getValue().getHandler(), seconds, TimeUnit.SECONDS);
        }
        
        Bukkit.getServicesManager().load(NmsFactory.class).create(ChatSystemInterface.class).sendMessage(this.getBukkitPlayer(), json);
        
    }
    
    /**
     * Performs a raw command passed from mclib console command.
     * 
     * @param commandUuid
     *            the unique command uuid.
     */
    public void onRawCommand(UUID commandUuid)
    {
        final McRunnable runnable = this.rawActions.get(commandUuid);
        if (runnable == null)
        {
            // TODO report error
        }
        else
        {
            try
            {
                McLibInterface.instance().runInNewContext(() ->
                {
                    McLibInterface.instance().setContext(McPlayerInterface.class, this);
                    runnable.run();
                });
            }
            catch (McException ex)
            {
                this.sendMessage(ex.getErrorMessage(), ex.getArgs());
            }
        }
    }
    
    @Override
    public <EVT extends MinecraftEvent<?, EVT>> void registerHandler(Plugin plugin, Class<EVT> clazz, McConsumer<EVT> handler)
    {
        this.eventBus.registerHandler(plugin, clazz, handler);
    }
    
    @Override
    public void registerHandlers(Plugin plugin, McListener listener)
    {
        this.eventBus.registerHandlers(plugin, listener);
    }
    
    @Override
    public <EVT extends MinecraftEvent<?, EVT>> void unregisterHandler(Plugin plugin, Class<EVT> clazz, McConsumer<EVT> handler)
    {
        this.eventBus.unregisterHandler(plugin, clazz, handler);
    }
    
    @Override
    public void unregisterHandlers(Plugin plugin, McListener listener)
    {
        this.eventBus.unregisterHandlers(plugin, listener);
    }
    
    @Override
    public <T extends Event, EVT extends MinecraftEvent<T, EVT>> void handle(Class<EVT> eventClass, EVT event)
    {
        if (eventClass == McPlayerMoveEvent.class)
        {
            this.zoneManager.registerMovement(((McPlayerMoveEvent) event).getBukkitEvent().getTo());
        }
        else if (eventClass == McPlayerTeleportEvent.class)
        {
            this.zoneManager.registerMovement(((McPlayerTeleportEvent) event).getBukkitEvent().getTo());
        }
        this.eventBus.handle(eventClass, event);
    }
    
    /**
     * Plugin disable.
     * 
     * @param plugin
     *            the plugin that was disabled.
     */
    public void onDisable(Plugin plugin)
    {
        this.eventBus.onDisable(plugin);
    }
    
    @Override
    public McPlayerInterface getPlayer()
    {
        return this;
    }
    
    @Override
    public int getApiVersion()
    {
        return this.clientApi;
    }
    
    @Override
    public ClientInterface getClient()
    {
        return this;
    }

    @Override
    public GuiSessionInterface openTextEditor(String src, McRunnable onCancel, McConsumer<String> onInput, boolean preUseInput, LocalizedMessageInterface description, Serializable... descriptionArgs)
        throws McException
    {
        final String[] desc = this.encodeMessage(description, descriptionArgs);
        final QueryText gui = new QueryText(src, onCancel, onInput, desc, preUseInput);
        return this.openAnvilGui(gui);
    }

    @Override
    public GuiSessionInterface nestTextEditor(String src, McRunnable onCancel, McConsumer<String> onInput, boolean preUseInput, LocalizedMessageInterface description, Serializable... descriptionArgs)
        throws McException
    {
        final String[] desc = this.encodeMessage(description, descriptionArgs);
        final QueryText gui = new QueryText(src, onCancel, onInput, desc, preUseInput);
        return this.nestAnvilGui(gui);
    }
    
}
