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

package de.minigames.mclib.nms.v18.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftVillager;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.mojang.authlib.yggdrasil.response.MinecraftProfilePropertiesResponse;
import com.mojang.util.UUIDTypeAdapter;

import de.minigameslib.mclib.nms.api.EntityHelperInterface;
import net.minecraft.server.v1_8_R1.MinecraftServer;
import net.minecraft.server.v1_8_R1.PlayerInteractManager;
import net.minecraft.server.v1_8_R1.WorldServer;

/**
 * NMS implementation of entity helper.
 * 
 * @author mepeisen
 *
 */
public class EntityHelper1_8 implements EntityHelperInterface
{
    
    /**
     * Property cache loader.
     */
    private static final class PropertyCacheLoader extends CacheLoader<UUID, PropertyMap>
    {
        /**
         * Constructor.
         */
        public PropertyCacheLoader()
        {
            // empty
        }
        
        @Override
        public PropertyMap load(UUID key) throws Exception
        {
            final Player player = Bukkit.getPlayer(key);
            return EntityHelper1_8.getRemoteProfile(player);
        }
    }
    
    /** dummy humans. */
    private static final EntityWithWhitelistHelper<DummyHuman1_8.EntityHelper> HUMANS     = new EntityWithWhitelistHelper<>();
    
    /** properties cache. */
    private static final LoadingCache<UUID, PropertyMap>                       PROPERTIES = CacheBuilder.newBuilder().maximumSize(10000).expireAfterAccess(20, TimeUnit.MINUTES)
        .build(new PropertyCacheLoader());
    
    @Override
    public Villager spawnDummyVillager(Location loc, Profession profession)
    {
        try
        {
            CustomEntityType1_8.DUMMY_VILLAGER.registerEntity();
            final DummyVillager1_8 villager = new DummyVillager1_8(((CraftWorld) loc.getWorld()).getHandle());
            villager.setPosition(loc);
            villager.setProfession(profession.getId());
            ((CraftWorld) loc.getWorld()).getHandle().addEntity(villager, SpawnReason.CUSTOM);
            return (CraftVillager) villager.getBukkitEntity();
        }
        finally
        {
            CustomEntityType1_8.DUMMY_VILLAGER.unregisterEntity();
        }
    }
    
    @Override
    public HumanEntity spawnDummyHuman(Location loc, String name, String skinTexture)
    {
        final CraftWorld world = (CraftWorld) loc.getWorld();
        final MinecraftServer server = world.getHandle().getMinecraftServer();
        final GameProfile profile = new GameProfile(UUID.randomUUID(), name);
        if (skinTexture != null)
        {
            setSkinToProfile(profile, skinTexture);
        }
        final DummyHuman1_8 result = new DummyHuman1_8(server, world.getHandle(), profile, new PlayerInteractManager(world.getHandle()), loc);
        HUMANS.add(result.getHelper());
        ((WorldServer) result.world).tracker.untrackEntity(result);
        return result.getBukkitEntity();
    }
    
    @Override
    public String getSkin(HumanEntity entity)
    {
        final DummyHuman1_8 human = (DummyHuman1_8) ((CraftPlayer) entity).getHandle();
        final Collection<Property> props = human.getProfile().getProperties().get("textures"); //$NON-NLS-1$
        if (props.isEmpty())
        {
            return null;
        }
        final Property p = props.iterator().next();
        return p.getValue() + ':' + p.getSignature();
    }
    
    @Override
    public String loadSkinTexture(Player player) throws ExecutionException
    {
        final PropertyMap prop = PROPERTIES.get(player.getUniqueId());
        final Collection<Property> props = prop.get("textures"); //$NON-NLS-1$
        if (props.isEmpty())
        {
            return null;
        }
        final Property p = props.iterator().next();
        return p.getValue() + ':' + p.getSignature();
    }
    
    @Override
    public void setSkin(HumanEntity entity, String texture)
    {
        final DummyHuman1_8 human = (DummyHuman1_8) ((CraftPlayer) entity).getHandle();
        setSkinToProfile(human.getProfile(), texture);
        human.getHelper().respawnAll();
    }
    
    /**
     * Setting skin to profiles.
     * 
     * @param profile
     *            game profile.
     * @param texture
     *            string texture skin.
     */
    private void setSkinToProfile(GameProfile profile, String texture)
    {
        final String[] splitted = texture.split(":"); //$NON-NLS-1$
        profile.getProperties().put("textures", new Property("textures", splitted[0], splitted[1])); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    @Override
    public void playerOnline(Player player)
    {
        HUMANS.playerOnline(player);
    }
    
    @Override
    public void playerOffline(Player player)
    {
        HUMANS.playerOffline(player);
    }
    
    @Override
    public void setProfileWithSkull(SkullMeta meta, String textures)
    {
        try
        {
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            setSkinToProfile(profile, textures);
            Field profileField = meta.getClass().getDeclaredField("profile"); //$NON-NLS-1$
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        }
        catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException | ClassCastException e)
        {
            throw new IllegalStateException("Problems setting skull.", e); //$NON-NLS-1$
        }
    }
    
    /**
     * get remote profile properties.
     * 
     * @param player
     *            target player.
     * @return profile properties
     * @throws Exception
     *             thrown on problems.
     */
    static PropertyMap getRemoteProfile(Player player) throws Exception
    {
        final MinecraftSessionService sessionService = ((CraftServer) Bukkit.getServer()).getServer().aB();
        boolean requireSecure = true;
        final GameProfile profile = ((CraftPlayer) player).getProfile();
        final YggdrasilAuthenticationService auth = ((YggdrasilMinecraftSessionService) sessionService).getAuthenticationService();
        
        URL url = HttpAuthenticationService.constantURL(new StringBuilder()
            .append("https://sessionserver.mojang.com/session/minecraft/profile/") //$NON-NLS-1$
            .append(UUIDTypeAdapter.fromUUID(profile.getId())).toString());
        url = HttpAuthenticationService.concatenateURL(url,
            new StringBuilder().append("unsigned=").append(!requireSecure).toString()); //$NON-NLS-1$
        
        final Method mthMakeRequest = YggdrasilAuthenticationService.class.getDeclaredMethod(
            "makeRequest", URL.class, Object.class, Class.class); //$NON-NLS-1$
        mthMakeRequest.setAccessible(true);
        
        final MinecraftProfilePropertiesResponse response = (MinecraftProfilePropertiesResponse) mthMakeRequest.invoke(
            auth, url, null, MinecraftProfilePropertiesResponse.class);
        if (response == null)
        {
            return profile.getProperties();
        }
        
        return response.getProperties();
    }
    
    @Override
    public void delete(Villager entity)
    {
        entity.remove();
    }
    
    @Override
    public void delete(HumanEntity entity)
    {
        ((CraftPlayer) entity).kickPlayer("delete"); //$NON-NLS-1$
        final DummyHuman1_8 human = (DummyHuman1_8) ((CraftPlayer) entity).getHandle();
        HUMANS.delete(human.getHelper());
    }
    
    @Override
    public void clearSkinCache(Player player)
    {
        PROPERTIES.invalidate(player.getUniqueId());
    }
    
    @Override
    public boolean isDummyVillager(Villager villager)
    {
        return villager instanceof DummyVillager1_8.VillagerNPC;
    }
    
    @Override
    public boolean isDummyHuman(HumanEntity human)
    {
        return human instanceof DummyHuman1_8.HumanNpc;
    }
    
}
