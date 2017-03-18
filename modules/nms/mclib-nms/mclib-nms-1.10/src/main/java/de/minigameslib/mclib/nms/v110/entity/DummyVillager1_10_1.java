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

package de.minigameslib.mclib.nms.v110.entity;

import java.lang.reflect.Field;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftVillager;

import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.EntityHuman;
import net.minecraft.server.v1_10_R1.EntityVillager;
import net.minecraft.server.v1_10_R1.EnumHand;
import net.minecraft.server.v1_10_R1.IBlockData;
import net.minecraft.server.v1_10_R1.ItemStack;
import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_10_R1.World;

/**
 * @author mepeisen
 *
 */
public class DummyVillager1_10_1 extends EntityVillager
{
    
    /** goal selector list field. */
    private static final Field GOALSELECTOR_FIELD;
    
    static
    {
        try
        {
            GOALSELECTOR_FIELD = PathfinderGoalSelector.class.getDeclaredField("b"); //$NON-NLS-1$
            GOALSELECTOR_FIELD.setAccessible(true);
        }
        catch (Exception ex)
        {
            throw new IllegalStateException("problems fetching pathfinder goal field. Should never happen.", ex); //$NON-NLS-1$
        }
    }
    
    /**
     * @param world
     */
    public DummyVillager1_10_1(World world)
    {
        super(world);
        clearGoals(this.goalSelector);
        clearGoals(this.targetSelector);
    }
    
    /**
     * Clear goals of given selector
     * @param selector
     */
    private void clearGoals(PathfinderGoalSelector selector)
    {
        try
        {
            Set<?> list = (Set<?>) GOALSELECTOR_FIELD.get(selector);
            list.clear();
        }
        catch (Exception e)
        {
            throw new IllegalStateException("error cleaning goals", e); //$NON-NLS-1$
        }
    }
    
    @Override
    public void a(double d0, boolean flag, IBlockData iblockdata, BlockPosition blockposition)
    {
        // do nothing (falling)
    }
    
    @Override
    public boolean a(EntityHuman human, EnumHand enumhand, ItemStack itemstack)
    {
        // do nothing (trades)
        return false;
    }

    @Override
    public boolean d(NBTTagCompound save)
    {
        // check if entity will be saved.
        return false;
    }

    @Override
    protected void L()
    {
        // do nothing (world despawn)
    }

    @Override
    public void e(float f, float f1)
    {
        // do nothing (jump)
    }

    @Override
    public void g(double x, double y, double z)
    {
        // do nothing (move)
    }

    @Override
    public void g(float f, float f1)
    {
        // do nothing (flying)
    }

    @Override
    public CraftEntity getBukkitEntity()
    {
        if (this.bukkitEntity == null)
        {
            this.bukkitEntity = new VillagerNPC(this);
        }
        return super.getBukkitEntity();
    }

    @Override
    public boolean m_()
    {
        // do nothing (ladder)
        return false;
    }

    /**
     * @param loc
     */
    void setPosition(Location loc)
    {
        this.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }
    
    /**
     * Bukkit npc class
     */
    protected static final class VillagerNPC extends CraftVillager
    {
        /**
         * Constructor.
         * @param handle
         */
        public VillagerNPC(DummyVillager1_10_1 handle)
        {
            super((CraftServer) Bukkit.getServer(), handle);
        }
    }
    
}
