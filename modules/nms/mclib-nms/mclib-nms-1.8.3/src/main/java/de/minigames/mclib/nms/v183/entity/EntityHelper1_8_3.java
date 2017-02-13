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

package de.minigames.mclib.nms.v183.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftServer;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftVillager;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

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

import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.nms.api.EntityHelperInterface;
import net.minecraft.server.v1_8_R2.EntityPlayer;
import net.minecraft.server.v1_8_R2.MinecraftServer;
import net.minecraft.server.v1_8_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R2.PlayerInteractManager;

/**
 * @author mepeisen
 *
 */
public class EntityHelper1_8_3 implements EntityHelperInterface
{
    
    /** dummy humans. */
    private static final Set<DummyHuman1_8_3> HUMANS = new HashSet<>();
    
    /** properties cache. */
    private final LoadingCache<UUID, PropertyMap> properties = CacheBuilder.newBuilder().
            maximumSize(10000).
            expireAfterAccess(20, TimeUnit.MINUTES).
            build(new CacheLoader<UUID, PropertyMap>(){
                @Override
                public PropertyMap load(UUID key) throws Exception
                {
                    final Player player = Bukkit.getPlayer(key);
                    return EntityHelper1_8_3.this.getRemoteProfile(player);
                }
            });
    
    @Override
    public Villager spawnDummyVillager(Location loc, Profession profession)
    {
        try
        {
            CustomEntityType1_8_3.DUMMY_VILLAGER.registerEntity();
            final DummyVillager1_8_3 villager = new DummyVillager1_8_3(((CraftWorld)loc.getWorld()).getHandle());
            villager.setPosition(loc);
            villager.setProfession(profession.getId());
            ((CraftWorld)loc.getWorld()).getHandle().addEntity(villager, SpawnReason.CUSTOM);
            return (CraftVillager) villager.getBukkitEntity();
        }
        finally
        {
            CustomEntityType1_8_3.DUMMY_VILLAGER.unregisterEntity();
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
        final DummyHuman1_8_3 result = new DummyHuman1_8_3(server, world.getHandle(), profile, new PlayerInteractManager(world.getHandle()), loc);
        HUMANS.add(result);
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) McLibInterface.instance(), new Runnable() {
            
            @Override
            public void run()
            {
                final PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(
                        PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { result });
                server.getPlayerList().sendAll(packet);
            }
        }, 10);
        return result.getBukkitEntity();
    }

    @Override
    public String getSkin(HumanEntity entity)
    {
        final DummyHuman1_8_3 human = (DummyHuman1_8_3) ((CraftPlayer)entity).getHandle();
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
        final PropertyMap prop = this.properties.get(player.getUniqueId());
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
        final DummyHuman1_8_3 human = (DummyHuman1_8_3) ((CraftPlayer)entity).getHandle();
        setSkinToProfile(human.getProfile(), texture);
        // TODO Sending updates to clients does not work
//        Bukkit.getOnlinePlayers().forEach(p -> {
//            p.hidePlayer(human.getBukkitEntity());
//            p.showPlayer(human.getBukkitEntity());
//        });
    }
    
    /** 
     * Setting skin to profiles
     * @param profile
     * @param texture
     */
    private void setSkinToProfile(GameProfile profile, String texture)
    {
        final String[] splitted = texture.split(":"); //$NON-NLS-1$
        profile.getProperties().put("textures", new Property("textures", splitted[0], splitted[1])); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public void updateVisibilityList(Player player)
    {
        for (final DummyHuman1_8_3 human : HUMANS)
        {
            final PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { human });
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        }
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
     * get remote profile properties
     * @param player
     * @return profile properties
     * @throws Exception
     */
    protected PropertyMap getRemoteProfile(Player player) throws Exception
    {
        final MinecraftSessionService sessionService = ((CraftServer) Bukkit.getServer()).getServer().aC();
        boolean requireSecure = true;
        final GameProfile profile = ((CraftPlayer)player).getProfile();
        final YggdrasilAuthenticationService auth = ((YggdrasilMinecraftSessionService) sessionService).getAuthenticationService();
        
        URL url = HttpAuthenticationService.constantURL(new StringBuilder()
                .append("https://sessionserver.mojang.com/session/minecraft/profile/") //$NON-NLS-1$
                .append(UUIDTypeAdapter.fromUUID(profile.getId())).toString());
        url = HttpAuthenticationService.concatenateURL(url,
                new StringBuilder().append("unsigned=").append(!requireSecure).toString()); //$NON-NLS-1$
        
        final Method MAKE_REQUEST = YggdrasilAuthenticationService.class.getDeclaredMethod(
                "makeRequest", URL.class, Object.class, Class.class); //$NON-NLS-1$
        MAKE_REQUEST.setAccessible(true);
        
        final MinecraftProfilePropertiesResponse response = (MinecraftProfilePropertiesResponse) MAKE_REQUEST.invoke(
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
    }
    
}
