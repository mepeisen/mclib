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

package de.minigameslib.mclib.impl.skin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.skin.SkinInterface;
import de.minigameslib.mclib.api.skin.SkinServiceInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.nms.api.EntityHelperInterface;
import de.minigameslib.mclib.nms.api.NmsFactory;
import de.minigameslib.mclib.shared.api.com.DataFragment;

/**
 * @author mepeisen
 *
 */
public class SkinServiceImpl implements SkinServiceInterface
{
    
    /** the executor service. */
    private ExecutorService executor;
    
    /**
     * Constructor
     * @param executor the executor service for asynchronous service execution
     */
    public SkinServiceImpl(ExecutorService executor)
    {
        this.executor = executor;
    }
    
    @Override
    public SkinInterface load(DataFragment section, String key) throws McException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void save(SkinInterface skin, DataFragment section, String key) throws McException
    {
        // TODO Auto-generated method stub
    }

    @Override
    public SkinInterface get(McPlayerInterface player)
    {
        return new SkinFromPlayer(player.getPlayerUUID());
    }

    @Override
    public SkinInterface getFromHuman(EntityInterface entity) throws McException
    {
        final HumanEntity human = (HumanEntity) entity.getBukkitEntity();
        final String textures = Bukkit.getServicesManager().load(NmsFactory.class).create(EntityHelperInterface.class).getSkin(human);
        if (textures != null)
        {
            return new SkinFromTextures(textures);
        }
        return null;
    }

    @Override
    public void getSkinSnapshot(SkinInterface skin, McConsumer<SkinInterface> completion)
    {
        if (skin instanceof SkinFromTextures)
        {
            // already finished
            new Completion(completion, skin).runTaskLater((Plugin) McLibInterface.instance(), 1);
        }
        else if (skin instanceof SkinFromPlayer)
        {
            this.executor.submit(() -> this.fetch((SkinFromPlayer) skin, completion));
        }
    }

    /**
     * Fetch player skin.
     * @param skin player skin
     * @param completion the completion func
     */
    private void fetch(SkinFromPlayer skin, McConsumer<SkinInterface> completion)
    {
        final McPlayerInterface player = ObjectServiceInterface.instance().getPlayer(skin.getPlayerUuid());
        try
        {
            final String textures = Bukkit.getServicesManager().load(NmsFactory.class).create(EntityHelperInterface.class).loadSkinTexture(player.getBukkitPlayer());
            completion.accept(new SkinFromTextures(textures));
        }
        catch (ExecutionException | McException e)
        {
            // TODO logging
        }
    }

    @Override
    public void getSkinSnapshot(McPlayerInterface player, McConsumer<SkinInterface> completion)
    {
        this.getSkinSnapshot(this.get(player), completion);
    }

    @Override
    public void setToHuman(EntityInterface entity, SkinInterface skin) throws McException
    {
        final HumanEntity human = (HumanEntity) entity.getBukkitEntity(); 
        if (skin instanceof SkinFromPlayer)
        {
            this.getSkinSnapshot(skin, s -> {
                Bukkit.getServicesManager().load(NmsFactory.class).create(EntityHelperInterface.class).setSkin(human, ((SkinFromTextures) s).getTextures());
            });
        }
        else if (skin instanceof SkinFromTextures)
        {
            Bukkit.getServicesManager().load(NmsFactory.class).create(EntityHelperInterface.class).setSkin(human, ((SkinFromTextures) skin).getTextures());
        }
    }

    @Override
    public ItemStack getSkull(SkinInterface skin, String name)
    {
        final ItemStack result = new ItemStack(Material.SKULL_ITEM);
        final ItemMeta meta = result.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setDisplayName(name);
        if (skin instanceof SkinFromPlayer)
        {
            final OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(((SkinFromPlayer) skin).getPlayerUuid());
            ((SkullMeta)meta).setOwner(player.getName()); // TODO maybe unsafe?
        }
        else if (skin instanceof SkinFromTextures)
        {
            Bukkit.getServicesManager().load(NmsFactory.class).create(EntityHelperInterface.class).setProfileWithSkull((SkullMeta) meta, ((SkinFromTextures) skin).getTextures());
        }
        result.setItemMeta(meta);
        return result;
    }
    
    /**
     * Completion task
     */
    private static final class Completion extends BukkitRunnable
    {
        
        /** consumer */
        private final McConsumer<SkinInterface> consumer;
        
        /** skin */
        private final SkinInterface skin;

        /**
         * @param consumer
         * @param skin
         */
        public Completion(McConsumer<SkinInterface> consumer, SkinInterface skin)
        {
            this.consumer = consumer;
            this.skin = skin;
        }

        @Override
        public void run()
        {
            try
            {
                this.consumer.accept(this.skin);
            }
            catch (McException ex)
            {
                // TODO logging
            }
        }
        
    }
    
}
