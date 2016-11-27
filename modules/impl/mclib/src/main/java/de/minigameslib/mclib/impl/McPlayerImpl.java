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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McContext;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.McStorage;
import de.minigameslib.mclib.api.config.Configurable;
import de.minigameslib.mclib.api.gui.AnvilGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.gui.GuiType;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.perms.PermissionsInterface;
import de.minigameslib.mclib.api.util.function.FalseStub;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;
import de.minigameslib.mclib.api.util.function.TrueStub;
import de.minigameslib.mclib.impl.player.MclibPlayersConfig;
import de.minigameslib.mclib.pshared.ActionPerformedData;
import de.minigameslib.mclib.pshared.PongData;
import de.minigameslib.mclib.pshared.QueryFormRequestData;
import de.minigameslib.mclib.pshared.WinClosedData;
import de.minigameslib.mclib.shared.api.com.CommunicationEndpointId;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * Implementation of arena players.
 * 
 * @author mepeisen
 *
 */
class McPlayerImpl implements McPlayerInterface
{
    
    /** logger. */
    static final Logger           LOGGER         = Logger.getLogger(McPlayerImpl.class.getName());
    
    /** players uuid. */
    private UUID                  uuid;
    
    /** the players name. */
    private String                name;
    
    /** the session storage. */
    private StorageImpl           sessionStorage = new StorageImpl();
    
    /** persistent player configuration. */
    private MclibPlayersConfig    config = new MclibPlayersConfig();
    
    /** persistent storage file. */
    FileConfiguration             persistentStorage;
    
    /** persistent storage file. */
    private File                  persistentStorageFile;
    
    /** the persistent storage. */
    private PersistentStorageImpl pstorage       = new PersistentStorageImpl();

    /** {@code true} if the client mod is installed. */
    private boolean hasForgeMod;

    /** list of client extensions that were reported by client mod. */
    private Set<String> clientExtensions = new HashSet<>();
    
    /**
     * Constructor
     * 
     * @param uuid
     *            players uuid
     * @param persistentStorage
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
        this.persistentStorage = new YamlConfiguration();
        if (persistentStorage.exists())
        {
            try (final FileReader fr = new FileReader(persistentStorage))
            {
                this.persistentStorage.load(fr);
            }
            catch (InvalidConfigurationException | IOException ex)
            {
                LOGGER.log(Level.WARNING, "Problems loading player storage from file; player uuid " + this.uuid.toString(), ex); //$NON-NLS-1$
            }
        }
        else
        {
            this.config.writeToConfig(this.persistentStorage.createSection("core")); //$NON-NLS-1$
            this.persistentStorage.createSection("storage"); //$NON-NLS-1$
            this.saveStore();
        }
    }

    /**
     * @param fragment
     */
    void parsePong(PongData fragment)
    {
        this.hasForgeMod = true;
        if (fragment != null)
        {
            this.clientExtensions.addAll(fragment.getClientExtensions());
        }
    }
    
    /**
     * Saves the persistent storage
     */
    void saveStore()
    {
        try
        {
            this.persistentStorage.save(this.persistentStorageFile);
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
        final OfflinePlayer player = this.getOfflinePlayer();
        String[] msgs = null;
        if (msg.isSingleLine())
        {
            msgs = new String[] { player.isOp() ? (msg.toAdminMessage(this.getPreferredLocale(), args)) : (msg.toUserMessage(this.getPreferredLocale(), args)) };
        }
        else
        {
            msgs = player.isOp() ? (msg.toAdminMessageLine(this.getPreferredLocale(), args)) : (msg.toUserMessageLine(this.getPreferredLocale(), args));
        }
        
        final String[] result = new String[msgs.length];
        
        for (int i = 0; i < result.length; i++)
        {
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
    public ZoneInterface getZone()
    {
        // TODO Support zones
        return null;
    }
    
    @Override
    public void setPreferredLocale(Locale locale) throws McException
    {
        this.config.setPreferredLocale(locale);
        this.config.writeToConfig(this.persistentStorage.createSection("core")); //$NON-NLS-1$
        this.saveStore();
    }
    
    @Override
    public boolean checkPermission(PermissionsInterface perm)
    {
        final Player player = this.getBukkitPlayer();
        return player == null ? false : player.hasPermission(perm.resolveName());
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
     * The persistent storage impl
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
        public <T extends Configurable> T get(Class<T> clazz)
        {
            final ConfigurationSection section = McPlayerImpl.this.persistentStorage.getConfigurationSection("storage." + clazz.getName()); //$NON-NLS-1$
            if (section != null)
            {
                try
                {
                    final T result = clazz.newInstance();
                    result.readFromConfig(section);
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
        public <T extends Configurable> void set(Class<T> clazz, T value)
        {
            final ConfigurationSection section = McPlayerImpl.this.persistentStorage.createSection("storage." + clazz.getName()); //$NON-NLS-1$
            value.writeToConfig(section);
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
         * serial version uid
         */
        private static final long serialVersionUID = 3803764167708189047L;
        
        /**
         * Constructor
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
        private final Map<Class<?>, Configurable> data = new HashMap<>();
        
        /**
         * Constructor.
         */
        public StorageImpl()
        {
            // empty
        }
        
        @Override
        public <T extends Configurable> T get(Class<T> clazz)
        {
            return clazz.cast(this.data.get(clazz));
        }
        
        @Override
        public <T extends Configurable> void set(Class<T> clazz, T value)
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
    
    /**
     * Player quit event
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
    }
    
    /**
     * Player join event
     */
    public void onPlayerJoin()
    {
        // clear session storage
        this.sessionStorage = new StorageImpl();
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
     * Parse an action performed message
     * @param fragment
     * @throws McException 
     */
    void parseActionPerformed(ActionPerformedData fragment) throws McException
    {
        final String actionId = fragment.getActionId();
        final McStorage storage = this.getSessionStorage();
        GuiSessionInterface session = storage.get(GuiSessionInterface.class);
        ((GuiSessionImpl)session).sguiActionPerformed(fragment.getWinId(), actionId, fragment.getData());
    }

    /**
     * Parse a win closed message
     * @param fragment
     * @throws McException 
     */
    void parseWinClosed(WinClosedData fragment) throws McException
    {
        final McStorage storage = this.getSessionStorage();
        GuiSessionInterface session = storage.get(GuiSessionInterface.class);
        ((GuiSessionImpl)session).sguiWinClosed(fragment.getWinId());
    }

    /**
     * Parse a form request message
     * @param fragment
     * @throws McException 
     */
    void parseFormRequest(QueryFormRequestData fragment) throws McException
    {
        final McStorage storage = this.getSessionStorage();
        GuiSessionInterface session = storage.get(GuiSessionInterface.class);
        ((GuiSessionImpl)session).sguiFormRequest(fragment);
    }

    @Override
    public void sendToClient(CommunicationEndpointId endpoint, DataSection... data)
    {
        try
        {
            McLibInterface.instance().runInNewContext(() -> {
                McLibInterface.instance().setContext(McPlayerInterface.class, this);
                endpoint.send(data);
            });
        }
        catch (McException ex)
        {
            // TODO Logging; should never happen
        }
    }
    
}
