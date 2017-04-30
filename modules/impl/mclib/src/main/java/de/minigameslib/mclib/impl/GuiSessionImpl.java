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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.McStorage;
import de.minigameslib.mclib.api.gui.AnvilGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.gui.GuiType;
import de.minigameslib.mclib.api.gui.SGuiFormBuilderInterface;
import de.minigameslib.mclib.api.gui.SGuiInterface;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.util.function.McBiConsumer;
import de.minigameslib.mclib.api.util.function.McFunction;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.impl.GuiSessionImpl.SGuiHelper.SGuiImpl;
import de.minigameslib.mclib.nms.api.AnvilManagerInterface;
import de.minigameslib.mclib.nms.api.AnvilManagerInterface.AnvilHelper;
import de.minigameslib.mclib.nms.api.AnvilManagerInterface.AnvilListener;
import de.minigameslib.mclib.nms.api.InventoryManagerInterface;
import de.minigameslib.mclib.nms.api.InventoryManagerInterface.InventoryHelper;
import de.minigameslib.mclib.nms.api.InventoryManagerInterface.InventoryListener;
import de.minigameslib.mclib.pshared.ButtonData;
import de.minigameslib.mclib.pshared.CloseWinData;
import de.minigameslib.mclib.pshared.CoreMessages;
import de.minigameslib.mclib.pshared.DisplayErrorData;
import de.minigameslib.mclib.pshared.DisplayInfoData;
import de.minigameslib.mclib.pshared.DisplayMarkerData;
import de.minigameslib.mclib.pshared.DisplayYesNoCancelData;
import de.minigameslib.mclib.pshared.DisplayYesNoData;
import de.minigameslib.mclib.pshared.FormData;
import de.minigameslib.mclib.pshared.MarkerData;
import de.minigameslib.mclib.pshared.MarkerData.BlockMarkerData;
import de.minigameslib.mclib.pshared.MarkerData.CuboidMarkerData;
import de.minigameslib.mclib.pshared.MclibCommunication;
import de.minigameslib.mclib.pshared.QueryFormAnswerData;
import de.minigameslib.mclib.pshared.QueryFormRequestData;
import de.minigameslib.mclib.pshared.RemoveMarkerData;
import de.minigameslib.mclib.pshared.ResetMarkersData;
import de.minigameslib.mclib.shared.api.com.DataFragment;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Implementation of a gui session
 * 
 * @author mepeisen
 */
public class GuiSessionImpl implements GuiSessionInterface, InventoryListener, AnvilListener
{
    
    /** logger. */
    private static final Logger   LOGGER         = Logger.getLogger(GuiSessionImpl.class.getName());
    
    /** the current gui type. */
    private GuiType               type           = GuiType.None;
    
    /** the gui interface. */
    private ClickGuiInterface     gui;
    /** anvil gui interface. */
    private AnvilGuiInterface     agui;
    /** the arena player. */
    private McPlayerImpl          player;
    /** the current inventory name. */
    private Serializable          currentName;
    /** the current items. */
    private ClickGuiItem[][]      currentItems;
    /** the current clock gui page */
    private ClickGuiPageInterface currentPage;
    /** the line count. */
    private int                   lineCount;
    
    /** gui instance. */
    private InventoryHelper       guiHelper;
    
    /** anvil gui instance. */
    private AnvilHelper           aguiHelper;
    
    /** the smart gui helper. */
    private SGuiHelper            smartGui;
    
    /** the session storage. */
    private StorageImpl           sessionStorage = new StorageImpl();

    /** previous session */
    private GuiSessionInterface   prevSession;

    /** pause flag */
    private boolean isPaused;
    
    /**
     * Constructor
     * 
     * @param gui
     *            the gui to be used
     * @param player
     *            the arena player to be used
     */
    public GuiSessionImpl(ClickGuiInterface gui, McPlayerImpl player)
    {
        this.type = GuiType.ClickGui;
        this.gui = gui;
        this.player = player;
        this.currentPage = gui.getInitialPage();
        this.currentName = this.currentPage.getPageName();
        this.currentItems = this.currentPage.getItems();
        this.lineCount = gui.getLineCount();
        
        final String name = toName(this.player, this.currentName);
        final ItemStack[] items = this.toItemStack();
        this.guiHelper = Bukkit.getServicesManager().load(InventoryManagerInterface.class).openInventory(player.getBukkitPlayer(), name, items, this);
    }
    
    /**
     * Converts given serializable to string
     * 
     * @param p
     * @param src
     * @return string
     */
    private static String toName(McPlayerInterface p, Serializable src)
    {
        if (src instanceof LocalizedMessageInterface)
        {
            return p.getBukkitPlayer().isOp() ? ((LocalizedMessageInterface) src).toAdminMessage(p.getPreferredLocale()) : ((LocalizedMessageInterface) src).toUserMessage(p.getPreferredLocale());
        }
        return src.toString();
    }
    
    /**
     * Constructor
     * 
     * @param gui
     *            the gui to be used
     * @param player
     *            the arena player to be used
     */
    public GuiSessionImpl(AnvilGuiInterface gui, McPlayerImpl player)
    {
        this.type = GuiType.AnvilGui;
        this.agui = gui;
        this.player = player;
        
        final ItemStack item = gui.getItem();
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(translateToAnvilGui(meta.getDisplayName()));
        item.setItemMeta(meta);
        this.aguiHelper = Bukkit.getServicesManager().load(AnvilManagerInterface.class).openGui(player.getBukkitPlayer(), item, this);
    }
    
    /**
     * Constructor for smart gui
     * 
     * @param player
     *            the arena player to be used
     */
    public GuiSessionImpl(McPlayerImpl player)
    {
        this.type = GuiType.SmartGui;
        this.player = player;
        this.smartGui = new SGuiHelper();
    }
    
    /**
     * Converts the existing items to item stack.
     * 
     * @return item stack.
     */
    private ItemStack[] toItemStack()
    {
        final ItemServiceInterface items = ItemServiceInterface.instance();
        final List<ItemStack> result = new ArrayList<>();
        for (int line = 0; line < this.lineCount; line++)
        {
            final ClickGuiItem[] itemline = (this.currentItems == null || this.currentItems.length <= line) ? null : this.currentItems[line];
            for (int column = 0; column < 9; column++)
            {
                if (itemline == null || itemline.length <= column || itemline[column] == null)
                {
                    result.add(new ItemStack(Material.AIR));
                }
                else
                {
                    final ItemStack stack = itemline[column].getItemStack().clone();
                    if (itemline[column].getDisplayName() != null)
                    {
                        final String displayName =
                                this.player.getBukkitPlayer().isOp() ? itemline[column].getDisplayName().toAdminMessage(this.player.getPreferredLocale(), itemline[column].getDisplayNameArgs())
                                        : itemline[column].getDisplayName().toUserMessage(this.player.getPreferredLocale(), itemline[column].getDisplayNameArgs())
                        ;
                        items.setDisplayName(stack, displayName);
                    }
                    result.add(stack);
                }
            }
        }
        return result.toArray(new ItemStack[result.size()]);
    }
    
    @Override
    public void read(DataSection section)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void write(DataSection section)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean test(DataSection section)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public McPlayerInterface getPlayer()
    {
        return this.player;
    }
    
    @Override
    public ClickGuiInterface getClickGui()
    {
        return this.gui;
    }
    
    @Override
    public AnvilGuiInterface getAnvilGui()
    {
        return this.agui;
    }
    
    @Override
    public void setNewPage(ClickGuiPageInterface page)
    {
        this.currentPage = page;
        this.currentName = page.getPageName();
        this.currentItems = page.getItems();
        final String name = toName(this.player, this.currentName);
        final ItemStack[] items = this.toItemStack();
        this.guiHelper.setNewPage(name, items);
    }
    
    @Override
    public void refreshClickGui()
    {
        this.setNewPage(this.currentPage);
    }
    
    @Override
    public void close()
    {
        if (this.type == GuiType.AnvilGui)
        {
            this.player.getBukkitPlayer().getOpenInventory().getTopInventory().clear();
            this.player.getBukkitPlayer().closeInventory();
        }
        else if (this.type == GuiType.ClickGui)
        {
            this.player.getBukkitPlayer().closeInventory();
        }
        else if (this.type == GuiType.SmartGui)
        {
            // TODO close smart gui
        }
    }
    
    /**
     * Pause this session
     */
    protected void pause()
    {
        this.isPaused = true;
        this.close();
    }
    
    /**
     * Resume this session
     */
    protected void resume()
    {
        this.isPaused = false;
        if (this.type == GuiType.AnvilGui)
        {
            final ItemStack item = this.agui.getItem();
            final ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(translateToAnvilGui(meta.getDisplayName()));
            item.setItemMeta(meta);
            this.aguiHelper = Bukkit.getServicesManager().load(AnvilManagerInterface.class).openGui(this.player.getBukkitPlayer(), item, this);
        }
        else if (this.type == GuiType.ClickGui)
        {
            this.currentName = this.currentPage.getPageName();
            this.currentItems = this.currentPage.getItems();
            this.lineCount = this.gui.getLineCount();
            
            final String name = toName(this.player, this.currentName);
            final ItemStack[] items = this.toItemStack();
            this.guiHelper = Bukkit.getServicesManager().load(InventoryManagerInterface.class).openInventory(this.player.getBukkitPlayer(), name, items, this);
        }
        else if (this.type == GuiType.SmartGui)
        {
            // TODO resume smart gui
        }
    }

    /**
     * @param oldSession
     */
    public void setPrevSession(GuiSessionInterface oldSession)
    {
        this.prevSession = oldSession;
    }
    
    @Override
    public McStorage getGuiStorage()
    {
        return this.sessionStorage;
    }
    
    @Override
    public void onClose()
    {
        if (this.isPaused)
        {
            // do nothing on close event
            return;
        }
        
        if (this.type == GuiType.AnvilGui)
        {
            // TODO fire event
        }
        else if (this.type == GuiType.ClickGui)
        {
            // TODO fire event
        }
        else if (this.type == GuiType.SmartGui)
        {
            // TODO fire event
        }
        this.type = GuiType.None;
        this.gui = null;
        this.agui = null;
        this.currentItems = null;
        this.currentName = null;
        this.currentPage = null;
        this.guiHelper = null;
        this.aguiHelper = null;
        this.smartGui = null;
        this.player = null;
        
        if (this.prevSession != null)
        {
            final GuiSessionImpl impl = (GuiSessionImpl) this.prevSession;
            this.prevSession = null;
            new BukkitRunnable() {
                
                @Override
                public void run()
                {
                    impl.resume();
                }
            }.runTaskLater((Plugin) McLibInterface.instance(), 1);
        }
    }

    /**
     * Translates a string from colored string to gui string
     * 
     * @param src
     * @return editable gui string
     */
    private static String translateToAnvilGui(String src)
    {
        return src.replace("%", "%%").replace("§", "%"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
    
    /**
     * Tranbslates a string from gui to represent colored strings
     * 
     * @param src
     *            editable gui string
     * @return colored string
     */
    private static String translateFromAnvilGui(String src)
    {
        final StringBuilder result = new StringBuilder();
        boolean wasPercent = true;
        for (final String elm : src.split("%")) //$NON-NLS-1$
        {
            if (elm.length() == 0)
            {
                if (wasPercent)
                {
                    wasPercent = false;
                }
                else
                {
                    result.append("%"); //$NON-NLS-1$
                    wasPercent = true;
                }
            }
            else
            {
                if (wasPercent)
                {
                    wasPercent = false;
                }
                else
                {
                    result.append("§"); //$NON-NLS-1$
                }
                result.append(elm);
            }
        }
        return result.toString();
    }
    
    @Override
    public boolean onCommit(String name)
    {
        boolean closeMe = true;
        if (this.type == GuiType.AnvilGui)
        {
            try
            {
                this.agui.onInput(translateFromAnvilGui(name));
                
                if (this.type == GuiType.None)
                {
                    // handler explicitly closed gui or opened another gui
                    // we do not want to return the gui again
                    closeMe = false;
                }
                else
                {
                    this.type = GuiType.None;
                    this.gui = null;
                    this.agui = null;
                    this.currentItems = null;
                    this.currentName = null;
                    this.currentPage = null;
                    this.guiHelper = null;
                    this.aguiHelper = null;
                    this.player = null;
                }
                
                if (this.prevSession != null)
                {
                    final GuiSessionImpl impl = (GuiSessionImpl) this.prevSession;
                    this.prevSession = null;
                    new BukkitRunnable() {
                        
                        @Override
                        public void run()
                        {
                            impl.resume();
                        }
                    }.runTaskLater((Plugin) McLibInterface.instance(), 1);
                }
            }
            catch (McException ex)
            {
                this.player.sendMessage(ex.getErrorMessage(), ex.getArgs());
                return false;
            }
            // TODO fire event
        }
        return closeMe;
    }
    
    @Override
    public boolean onClick(ItemStack stack, int rawSlot, int slot)
    {
        if (this.type == GuiType.ClickGui)
        {
            if (stack != null)
            {
                if (slot == rawSlot) // top inventory has slot == rawSlot numbers
                {
                    final int col = slot % 9;
                    final int line = (slot - col) / 9;
                    if (this.currentItems.length > line && this.currentItems[line].length > col)
                    {
                        try
                        {
                            final ClickGuiItem guiItem = this.currentItems[line][col];
                            if (guiItem != null)
                            {
                                return guiItem.handle(this.player, this, this.gui);
                            }
                        }
                        catch (McException ex)
                        {
                            this.player.sendMessage(ex.getErrorMessage(), ex.getArgs());
                        }
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public void setItem(int index, ItemStack item)
    {
        if (this.type == GuiType.ClickGui)
        {
            final int col = index % 9;
            final int line = (index - col) / 9;
            if (this.currentItems.length > line && this.currentItems[line].length > col)
            {
                try
                {
                    final ClickGuiItem guiItem = this.currentItems[line][col];
                    if (guiItem != null)
                    {
                        guiItem.replace(this.player, this, this.gui, item);
                    }
                }
                catch (McException ex)
                {
                    this.player.sendMessage(ex.getErrorMessage(), ex.getArgs());
                }
            }
        }
    }
    
    @Override
    public GuiType getCurrentType()
    {
        return this.type;
    }
    
    @Override
    public SGuiInterface sguiDisplayError(LocalizedMessageInterface title, Serializable titleArgs[], LocalizedMessageInterface message, Serializable messageArgs[], GuiButton okButton)
            throws McException
    {
        checkForSmartGui();
        initSmartGui();
        final SGuiImpl result = this.smartGui.create(null);
        result.registerAction((GuiButtonImpl) okButton);
        
        // send to client
        final DisplayErrorData display = new DisplayErrorData();
        display.setTitle(Arrays.asList(this.player.encodeMessage(title, titleArgs)).stream().collect(Collectors.joining("\n"))); //$NON-NLS-1$
        display.setMessage(Arrays.asList(this.player.encodeMessage(message, messageArgs)).stream().collect(Collectors.joining("\n"))); //$NON-NLS-1$
        display.setOkButton((GuiButtonImpl) okButton);
        display.setId(result.getUuid());
        final DataSection section = new MemoryDataSection();
        section.set("KEY", CoreMessages.DisplayError.name()); //$NON-NLS-1$
        display.write(section.createSection("data")); //$NON-NLS-1$
        this.player.sendToClient(MclibCommunication.ClientServerCore, section);
        return result;
    }
    
    /**
     * Initializes the smart gui.
     */
    private void initSmartGui()
    {
        if (this.smartGui == null)
        {
            if (this.type != GuiType.None)
            {
                this.onClose();
            }
            this.smartGui = new SGuiHelper();
        }
    }
    
    /**
     * Checks the player for haviong a smart gui.
     * 
     * @throws McException
     */
    private void checkForSmartGui() throws McException
    {
        if (!this.player.hasSmartGui())
        {
            throw new McException(CommonMessages.NoSmartGui);
        }
    }
    
    @Override
    public SGuiInterface sguiDisplayInfo(LocalizedMessageInterface title, Serializable titleArgs[], LocalizedMessageInterface message, Serializable messageArgs[], GuiButton okButton)
            throws McException
    {
        checkForSmartGui();
        initSmartGui();
        final SGuiImpl result = this.smartGui.create(null);
        result.registerAction((GuiButtonImpl) okButton);
        
        // send to client
        final DisplayInfoData display = new DisplayInfoData();
        display.setTitle(Arrays.asList(this.player.encodeMessage(title, titleArgs)).stream().collect(Collectors.joining("\n"))); //$NON-NLS-1$
        display.setMessage(Arrays.asList(this.player.encodeMessage(message, messageArgs)).stream().collect(Collectors.joining("\n"))); //$NON-NLS-1$
        display.setOkButton((GuiButtonImpl) okButton);
        display.setId(result.getUuid());
        final DataSection section = new MemoryDataSection();
        section.set("KEY", CoreMessages.DisplayInfo.name()); //$NON-NLS-1$
        display.write(section.createSection("data")); //$NON-NLS-1$
        this.player.sendToClient(MclibCommunication.ClientServerCore, section);
        return result;
    }
    
    @Override
    public GuiButton sguiCreateButton(LocalizedMessageInterface label, Serializable labelArgs[]) throws McException
    {
        return this.sguiCreateButton(label, labelArgs, null);
    }
    
    @Override
    public GuiButton sguiCreateButton(LocalizedMessageInterface label, Serializable labelArgs[], McBiConsumer<SGuiInterface, DataSection> action) throws McException
    {
        return this.sguiCreateButton(label, labelArgs, action, true);
    }
    
    @Override
    public GuiButton sguiCreateButton(LocalizedMessageInterface label, Serializable labelArgs[], McBiConsumer<SGuiInterface, DataSection> action, boolean closeAction) throws McException
    {
        checkForSmartGui();
        
        final GuiButtonImpl button = new GuiButtonImpl();
        button.setLabel(Arrays.asList(this.player.encodeMessage(label, labelArgs)).stream().collect(Collectors.joining("\n"))); //$NON-NLS-1$
        button.setCloseAction(closeAction);
        if (action != null)
        {
            button.setAction((sgui, data) -> {
                final DataSection form = new MemoryDataSection();
                data.forEach(entry -> form.set(entry.getKey(), entry.getValue()));
                action.accept(sgui, form);
            });
        }
        return button;
    }
    
    @Override
    public SGuiInterface sguiDisplayYesNo(LocalizedMessageInterface title, Serializable titleArgs[], LocalizedMessageInterface message, Serializable messageArgs[], GuiButton yesButton,
            GuiButton noButton) throws McException
    {
        checkForSmartGui();
        initSmartGui();
        final SGuiImpl result = this.smartGui.create(null);
        result.registerAction((GuiButtonImpl) yesButton);
        result.registerAction((GuiButtonImpl) noButton);
        
        // send to client
        final DisplayYesNoData display = new DisplayYesNoData();
        display.setTitle(Arrays.asList(this.player.encodeMessage(title, titleArgs)).stream().collect(Collectors.joining("\n"))); //$NON-NLS-1$
        display.setMessage(Arrays.asList(this.player.encodeMessage(message, messageArgs)).stream().collect(Collectors.joining("\n"))); //$NON-NLS-1$
        display.setYesButton((GuiButtonImpl) yesButton);
        display.setNoButton((GuiButtonImpl) noButton);
        display.setId(result.getUuid());
        final DataSection section = new MemoryDataSection();
        section.set("KEY", CoreMessages.DisplayYesNo.name()); //$NON-NLS-1$
        display.write(section.createSection("data")); //$NON-NLS-1$
        this.player.sendToClient(MclibCommunication.ClientServerCore, section);
        return result;
    }
    
    @Override
    public SGuiInterface sguiDisplayYesNoCancel(LocalizedMessageInterface title, Serializable titleArgs[], LocalizedMessageInterface message, Serializable messageArgs[], GuiButton yesButton,
            GuiButton noButton, GuiButton cancelButton) throws McException
    {
        checkForSmartGui();
        initSmartGui();
        final SGuiImpl result = this.smartGui.create(null);
        result.registerAction((GuiButtonImpl) yesButton);
        result.registerAction((GuiButtonImpl) noButton);
        result.registerAction((GuiButtonImpl) cancelButton);
        
        // send to client
        final DisplayYesNoCancelData display = new DisplayYesNoCancelData();
        display.setTitle(Arrays.asList(this.player.encodeMessage(title, titleArgs)).stream().collect(Collectors.joining("\n"))); //$NON-NLS-1$
        display.setMessage(Arrays.asList(this.player.encodeMessage(message, messageArgs)).stream().collect(Collectors.joining("\n"))); //$NON-NLS-1$
        display.setYesButton((GuiButtonImpl) yesButton);
        display.setNoButton((GuiButtonImpl) noButton);
        display.setCancelButton((GuiButtonImpl) cancelButton);
        display.setId(result.getUuid());
        final DataSection section = new MemoryDataSection();
        section.set("KEY", CoreMessages.DisplayYesNoCancel.name()); //$NON-NLS-1$
        display.write(section.createSection("data")); //$NON-NLS-1$
        this.player.sendToClient(MclibCommunication.ClientServerCore, section);
        return result;
    }
    
    @Override
    public SGuiFormBuilderInterface sguiForm(LocalizedMessageInterface title, Serializable[] titleArgs, boolean closable, McRunnable closeAction) throws McException
    {
        checkForSmartGui();
        initSmartGui();
        return new SGuiFormBuilder(title, titleArgs, closable, closeAction, this.smartGui);
    }
    
    /**
     * smart gui helper
     * 
     * @author mepeisen
     */
    final class SGuiHelper
    {
        
        // TODO potential memory leak. If user stays online but client does not send "window closed" event
        // TODO potential memory leak for form builders if display is never called
        /** gui implementations. */
        private final Map<String, SGuiImpl> impls = new HashMap<>();
        
        /**
         * Creates a new SGUI Impl
         * 
         * @param closeAction
         * @return SGUI Impl
         */
        SGuiImpl create(McRunnable closeAction)
        {
            final String uuid = UUID.randomUUID().toString();
            final SGuiImpl impl = new SGuiImpl(uuid, closeAction);
            this.impls.put(uuid, impl);
            return impl;
        }
        
        /**
         * Encodes given message by using players locale
         * 
         * @param msg
         * @param args
         * @return encoded message
         */
        String[] encode(LocalizedMessageInterface msg, Serializable[] args)
        {
            return GuiSessionImpl.this.player.encodeMessage(msg, args);
        }
        
        /**
         * Returns the gui session.
         * 
         * @return gui session
         */
        GuiSessionImpl getGuiSession()
        {
            return GuiSessionImpl.this;
        }
        
        /**
         * Returns the gui with associated key.
         * 
         * @param id
         * @return gui
         */
        SGuiImpl get(String id)
        {
            return this.impls.get(id);
        }
        
        /**
         * Removes the gui with associated key
         * 
         * @param id
         * @return gui
         */
        SGuiImpl remove(String id)
        {
            return this.impls.remove(id);
        }
        
        /**
         * helper for smart guis.
         * 
         * @author mepeisen
         */
        final class SGuiImpl implements SGuiInterface
        {
            
            /** the registered actions. */
            private final Map<String, McBiConsumer<SGuiInterface, List<FormData>>>           actions   = new HashMap<>();
            
            /** the registered suppliers. */
            private final Map<String, McFunction<QueryFormRequestData, QueryFormAnswerData>> suppliers = new HashMap<>();
            
            /** the uuid. */
            private final String                                                             uuid;
            
            /** the close action. */
            private final McRunnable                                                         closeAction;
            
            /**
             * Constructor
             * 
             * @param closeAction
             * @param uuid
             */
            public SGuiImpl(String uuid, McRunnable closeAction)
            {
                this.uuid = uuid;
                this.closeAction = closeAction;
            }
            
            /**
             * @return the closeAction
             */
            public McRunnable getCloseAction()
            {
                return this.closeAction;
            }
            
            /**
             * @param button
             */
            public void registerAction(GuiButtonImpl button)
            {
                if (button != null && button.getAction() != null)
                {
                    final UUID actionUuid = UUID.randomUUID();
                    button.setHasActionListener(true);
                    button.setActionId(actionUuid.toString());
                    this.actions.put(actionUuid.toString(), button.getAction());
                }
            }
            
            /**
             * Performs given action.
             * 
             * @param actionId
             *            action uuid.
             * @param data
             *            from data
             * @throws McException
             */
            public void performAction(String actionId, List<FormData> data) throws McException
            {
                final McBiConsumer<SGuiInterface, List<FormData>> run = this.actions.get(actionId);
                if (run != null)
                {
                    run.accept(this, data);
                }
            }
            
            /**
             * Query data from data suppliers.
             * 
             * @param data
             * @return data result.
             * @throws McException
             */
            public QueryFormAnswerData queryData(QueryFormRequestData data) throws McException
            {
                final McFunction<QueryFormRequestData, QueryFormAnswerData> supplier = this.suppliers.get(data.getInputId());
                if (supplier != null)
                {
                    return supplier.apply(data);
                }
                return null;
            }
            
            @Override
            public void close()
            {
                final CloseWinData data = new CloseWinData();
                data.setWinId(this.getUuid());
                final DataSection section = new MemoryDataSection();
                section.set("KEY", CoreMessages.CloseWin.name()); //$NON-NLS-1$
                data.write(section.createSection("data")); //$NON-NLS-1$
                GuiSessionImpl.this.player.sendToClient(MclibCommunication.ClientServerCore, section);
                
                SGuiHelper.this.remove(this.getUuid());
            }
            
            /**
             * @return the uuid
             */
            public String getUuid()
            {
                return this.uuid;
            }
            
            /**
             * @param dataSupplier
             * @return unique data supplier id.
             */
            public String registerDataSupplier(McFunction<QueryFormRequestData, QueryFormAnswerData> dataSupplier)
            {
                final String result = UUID.randomUUID().toString();
                this.suppliers.put(result, dataSupplier);
                return result;
            }
            
        }
        
        /**
         * Returns the underlying player
         * 
         * @return underlying player
         */
        public McPlayerInterface getPlayer()
        {
            return GuiSessionImpl.this.getPlayer();
        }
        
    }
    
    /**
     * Button data.
     * 
     * @author mepeisen
     */
    public class GuiButtonImpl extends ButtonData implements GuiButton
    {
        
        /** the action. */
        private McBiConsumer<SGuiInterface, List<FormData>> action;
        
        /**
         * @return the action
         */
        public McBiConsumer<SGuiInterface, List<FormData>> getAction()
        {
            return this.action;
        }
        
        /**
         * @param action
         *            the action to set
         */
        public void setAction(McBiConsumer<SGuiInterface, List<FormData>> action)
        {
            this.action = action;
        }
        
    }
    
    /**
     * Performs action.
     * 
     * @param winId
     * @param actionId
     * @param formData
     * @throws McException
     */
    void sguiActionPerformed(String winId, String actionId, List<FormData> formData) throws McException
    {
        if (this.smartGui != null)
        {
            final SGuiImpl impl = this.smartGui.get(winId);
            if (impl != null)
            {
                impl.performAction(actionId, formData);
            }
        }
    }
    
    /**
     * Performs action.
     * 
     * @param winId
     * @throws McException
     */
    void sguiWinClosed(String winId) throws McException
    {
        if (this.smartGui != null)
        {
            final SGuiImpl impl = this.smartGui.remove(winId);
            if (impl != null && impl.getCloseAction() != null)
            {
                try
                {
                    impl.getCloseAction().run();
                }
                catch (McException ex)
                {
                    LOGGER.log(Level.WARNING, "Problems invoke close action", ex); //$NON-NLS-1$
                }
            }
        }
    }
    
    /**
     * @param fragment
     * @throws McException
     */
    void sguiFormRequest(QueryFormRequestData fragment) throws McException
    {
        if (this.smartGui != null)
        {
            final SGuiImpl impl = this.smartGui.get(fragment.getWinId());
            if (impl != null)
            {
                final QueryFormAnswerData answer = impl.queryData(fragment);
                
                final DataSection section = new MemoryDataSection();
                section.set("KEY", CoreMessages.QueryFormAnswer.name()); //$NON-NLS-1$
                answer.write(section.createSection("data")); //$NON-NLS-1$
                this.player.sendToClient(MclibCommunication.ClientServerCore, section);
            }
        }
    }
    
    /**
     * sgui impl
     */
    private static final class SGuiMarker implements SGuiMarkerInterface
    {
        
        /** marker uuid. */
        private UUID              markerId;
        
        /** the player. */
        private McPlayerInterface player;
        
        /**
         * Constructor.
         * 
         * @param player
         * @param markerId
         */
        public SGuiMarker(McPlayerInterface player, UUID markerId)
        {
            this.player = player;
            this.markerId = markerId;
        }
        
        @Override
        public void remove() throws McException
        {
            final RemoveMarkerData data = new RemoveMarkerData();
            data.setMarkerId(this.markerId.toString());
            final DataSection section = new MemoryDataSection();
            section.set("KEY", CoreMessages.RemoveMarker.name()); //$NON-NLS-1$
            data.write(section.createSection("data")); //$NON-NLS-1$
            this.player.sendToClient(MclibCommunication.ClientServerCore, section);
        }
        
        @Override
        public void updateLabel(LocalizedMessageInterface label, Serializable... args) throws McException
        {
            // TODO support marker labels
        }
        
    }
    
    @Override
    public SGuiMarkerInterface sguiShowMarker(ComponentInterface component, LocalizedMessageInterface label, Serializable... labelArgs) throws McException
    {
        // TODO support marker labels
        // TODO listen for location changes and report to client.
        checkForSmartGui();
        initSmartGui();
        final UUID uuid = UUID.randomUUID();
        
        final DisplayMarkerData data = new DisplayMarkerData();
        data.setMarkerId(uuid.toString());
        data.setMarker(new MarkerData());
        // TODO fill plugin and type id
        data.getMarker().setBlock(new BlockMarkerData());
        data.getMarker().getBlock().setX(component.getLocation().getBlockX());
        data.getMarker().getBlock().setY(component.getLocation().getBlockY());
        data.getMarker().getBlock().setZ(component.getLocation().getBlockZ());
        final DataSection section = new MemoryDataSection();
        section.set("KEY", CoreMessages.DisplayMarker.name()); //$NON-NLS-1$
        data.write(section.createSection("data")); //$NON-NLS-1$
        this.player.sendToClient(MclibCommunication.ClientServerCore, section);
        
        return new SGuiMarker(this.player, uuid);
    }
    
    @Override
    public SGuiMarkerInterface sguiShowMarker(SignInterface sign, LocalizedMessageInterface label, Serializable... labelArgs) throws McException
    {
        // TODO support marker labels
        // TODO listen for location changes and report to client.
        checkForSmartGui();
        initSmartGui();
        final UUID uuid = UUID.randomUUID();
        
        final DisplayMarkerData data = new DisplayMarkerData();
        data.setMarkerId(uuid.toString());
        data.setMarker(new MarkerData());
        // TODO fill plugin and type id
        data.getMarker().setBlock(new BlockMarkerData());
        data.getMarker().getBlock().setX(sign.getLocation().getBlockX());
        data.getMarker().getBlock().setY(sign.getLocation().getBlockY());
        data.getMarker().getBlock().setZ(sign.getLocation().getBlockZ());
        final DataSection section = new MemoryDataSection();
        section.set("KEY", CoreMessages.DisplayMarker.name()); //$NON-NLS-1$
        data.write(section.createSection("data")); //$NON-NLS-1$
        this.player.sendToClient(MclibCommunication.ClientServerCore, section);
        
        return new SGuiMarker(this.player, uuid);
    }
    
    @Override
    public SGuiMarkerInterface sguiShowMarker(ZoneInterface zone, LocalizedMessageInterface label, Serializable... labelArgs) throws McException
    {
        // TODO support marker labels
        // TODO listen for location changes and report to client.
        checkForSmartGui();
        initSmartGui();
        final UUID uuid = UUID.randomUUID();
        
        final DisplayMarkerData data = new DisplayMarkerData();
        data.setMarkerId(uuid.toString());
        data.setMarker(new MarkerData());
        data.getMarker().setCuboid(new CuboidMarkerData());
        data.getMarker().getCuboid().setX1(zone.getCuboid().getLowLoc().getBlockX());
        data.getMarker().getCuboid().setY1(zone.getCuboid().getLowLoc().getBlockY());
        data.getMarker().getCuboid().setZ1(zone.getCuboid().getLowLoc().getBlockZ());
        data.getMarker().getCuboid().setX2(zone.getCuboid().getHighLoc().getBlockX());
        data.getMarker().getCuboid().setY2(zone.getCuboid().getHighLoc().getBlockY());
        data.getMarker().getCuboid().setZ2(zone.getCuboid().getHighLoc().getBlockZ());
        final DataSection section = new MemoryDataSection();
        section.set("KEY", CoreMessages.DisplayMarker.name()); //$NON-NLS-1$
        data.write(section.createSection("data")); //$NON-NLS-1$
        this.player.sendToClient(MclibCommunication.ClientServerCore, section);
        
        return new SGuiMarker(this.player, uuid);
    }
    
    @Override
    public SGuiMarkerInterface sguiShowMarker(ComponentInterface component, int r, int g, int b, int alpha, LocalizedMessageInterface label, Serializable... labelArgs) throws McException
    {
        // TODO support marker labels
        // TODO listen for location changes and report to client.
        checkForSmartGui();
        initSmartGui();
        final UUID uuid = UUID.randomUUID();
        
        final DisplayMarkerData data = new DisplayMarkerData();
        data.setMarkerId(uuid.toString());
        data.setMarker(new MarkerData());
        // TODO fill plugin and type id
        data.getMarker().setBlock(new BlockMarkerData());
        data.getMarker().getBlock().setX(component.getLocation().getBlockX());
        data.getMarker().getBlock().setY(component.getLocation().getBlockY());
        data.getMarker().getBlock().setZ(component.getLocation().getBlockZ());
        data.getMarker().getBlock().setColor(new MarkerData.MarkerColorData());
        data.getMarker().getBlock().getColor().setR(r);
        data.getMarker().getBlock().getColor().setG(g);
        data.getMarker().getBlock().getColor().setB(b);
        data.getMarker().getBlock().getColor().setAlpha(alpha);
        final DataSection section = new MemoryDataSection();
        section.set("KEY", CoreMessages.DisplayMarker.name()); //$NON-NLS-1$
        data.write(section.createSection("data")); //$NON-NLS-1$
        this.player.sendToClient(MclibCommunication.ClientServerCore, section);
        
        return new SGuiMarker(this.player, uuid);
    }
    
    @Override
    public SGuiMarkerInterface sguiShowMarker(SignInterface sign, int r, int g, int b, int alpha, LocalizedMessageInterface label, Serializable... labelArgs) throws McException
    {
        // TODO support marker labels
        // TODO listen for location changes and report to client.
        checkForSmartGui();
        initSmartGui();
        final UUID uuid = UUID.randomUUID();
        
        final DisplayMarkerData data = new DisplayMarkerData();
        data.setMarkerId(uuid.toString());
        data.setMarker(new MarkerData());
        // TODO fill plugin and type id
        data.getMarker().setBlock(new BlockMarkerData());
        data.getMarker().getBlock().setX(sign.getLocation().getBlockX());
        data.getMarker().getBlock().setY(sign.getLocation().getBlockY());
        data.getMarker().getBlock().setZ(sign.getLocation().getBlockZ());
        data.getMarker().getBlock().setColor(new MarkerData.MarkerColorData());
        data.getMarker().getBlock().getColor().setR(r);
        data.getMarker().getBlock().getColor().setG(g);
        data.getMarker().getBlock().getColor().setB(b);
        data.getMarker().getBlock().getColor().setAlpha(alpha);
        final DataSection section = new MemoryDataSection();
        section.set("KEY", CoreMessages.DisplayMarker.name()); //$NON-NLS-1$
        data.write(section.createSection("data")); //$NON-NLS-1$
        this.player.sendToClient(MclibCommunication.ClientServerCore, section);
        
        return new SGuiMarker(this.player, uuid);
    }
    
    @Override
    public SGuiMarkerInterface sguiShowMarker(ZoneInterface zone, int r, int g, int b, int alpha, LocalizedMessageInterface label, Serializable... labelArgs) throws McException
    {
        // TODO support marker labels
        // TODO listen for location changes and report to client.
        checkForSmartGui();
        initSmartGui();
        final UUID uuid = UUID.randomUUID();
        
        final DisplayMarkerData data = new DisplayMarkerData();
        data.setMarkerId(uuid.toString());
        data.setMarker(new MarkerData());
        data.getMarker().setCuboid(new CuboidMarkerData());
        data.getMarker().getCuboid().setX1(zone.getCuboid().getLowLoc().getBlockX());
        data.getMarker().getCuboid().setY1(zone.getCuboid().getLowLoc().getBlockY());
        data.getMarker().getCuboid().setZ1(zone.getCuboid().getLowLoc().getBlockZ());
        data.getMarker().getCuboid().setX2(zone.getCuboid().getHighLoc().getBlockX());
        data.getMarker().getCuboid().setY2(zone.getCuboid().getHighLoc().getBlockY());
        data.getMarker().getCuboid().setZ2(zone.getCuboid().getHighLoc().getBlockZ());
        data.getMarker().getCuboid().setColor(new MarkerData.MarkerColorData());
        data.getMarker().getCuboid().getColor().setR(r);
        data.getMarker().getCuboid().getColor().setG(g);
        data.getMarker().getCuboid().getColor().setB(b);
        data.getMarker().getCuboid().getColor().setAlpha(alpha);
        final DataSection section = new MemoryDataSection();
        section.set("KEY", CoreMessages.DisplayMarker.name()); //$NON-NLS-1$
        data.write(section.createSection("data")); //$NON-NLS-1$
        this.player.sendToClient(MclibCommunication.ClientServerCore, section);
        
        return new SGuiMarker(this.player, uuid);
    }
    
    @Override
    public void sguiRemoveAllMarkers() throws McException
    {
        final ResetMarkersData data = new ResetMarkersData();
        final DataSection section = new MemoryDataSection();
        section.set("KEY", CoreMessages.ResetMarkers.name()); //$NON-NLS-1$
        data.write(section.createSection("data")); //$NON-NLS-1$
        this.player.sendToClient(MclibCommunication.ClientServerCore, section);
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
    
}
