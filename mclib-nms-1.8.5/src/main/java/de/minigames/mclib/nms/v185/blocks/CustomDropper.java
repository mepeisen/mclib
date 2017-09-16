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

package de.minigames.mclib.nms.v185.blocks;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.block.Dropper;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftDropper;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import de.minigameslib.mclib.api.items.BlockDropperRuleInterface;
import de.minigameslib.mclib.pshared.DataSectionTools;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;
import net.minecraft.server.v1_8_R3.BlockDropper;
import net.minecraft.server.v1_8_R3.IUpdatePlayerListBox;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.TileEntity;
import net.minecraft.server.v1_8_R3.TileEntityDropper;
import net.minecraft.server.v1_8_R3.World;

/**
 * A custom block implementation.
 * 
 * @author mepeisen
 */
public class CustomDropper extends BlockDropper
{
    
    /** the dropper rule class. */
    private Class<? extends BlockDropperRuleInterface> ruleClass;
    
    /**
     * Sets the dropper rule.
     * 
     * @param dropperRule
     *            the dropperRule to set
     */
    public void setDropperRule(Class<? extends BlockDropperRuleInterface> dropperRule)
    {
        this.ruleClass = dropperRule;
    }
    
    @Override
    public TileEntity a(World paramWorld, int paramInt)
    {
        try
        {
            return new MyTileEntity(this.ruleClass.newInstance());
        }
        catch (@SuppressWarnings("unused") InstantiationException | IllegalAccessException ex)
        {
            // TODO Logging
        }
        return super.a(paramWorld, paramInt);
    }
    
    /**
     * tile tntiy for custom dropper.
     */
    public final class MyTileEntity extends TileEntityDropper implements IUpdatePlayerListBox
    {
        
        /**
         * the dropper rule.
         */
        private BlockDropperRuleInterface dropperRule;
        
        /**
         * Constructor.
         * 
         * @param rule
         *            the rule instance.
         */
        public MyTileEntity(BlockDropperRuleInterface rule)
        {
            this.dropperRule = rule;
        }
        
        @Override
        public void a(NBTTagCompound nbttagcompound)
        {
            super.a(nbttagcompound);
            
            if (nbttagcompound.hasKey("mclibdata")) //$NON-NLS-1$
            {
                final byte[] bytes = nbttagcompound.getByteArray("mclibdata"); //$NON-NLS-1$
                try
                {
                    try (final DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes)))
                    {
                        final DataSection section = DataSectionTools.read(dis);
                        this.dropperRule.read(section);
                    }
                }
                catch (@SuppressWarnings("unused") IOException ex)
                {
                    // TODO logging
                }
            }
        }
        
        @Override
        public void b(NBTTagCompound nbttagcompound)
        {
            super.b(nbttagcompound);
            try
            {
                try (final ByteArrayOutputStream baos = new ByteArrayOutputStream())
                {
                    try (final DataOutputStream dos = new DataOutputStream(baos))
                    {
                        final DataSection section = new MemoryDataSection();
                        this.dropperRule.write(section);
                        DataSectionTools.toBytes(dos, section);
                    }
                    nbttagcompound.setByteArray("mclibdata", baos.toByteArray()); //$NON-NLS-1$
                }
            }
            catch (@SuppressWarnings("unused") IOException ex)
            {
                // TODO logging
            }
        }
        
        @Override
        public boolean b(int pos, ItemStack itemstack)
        {
            final Dropper dropper = new CraftDropper(this.getLocation().getBlock());
            switch (this.dropperRule.isItemValidForSlot(pos, CraftItemStack.asCraftMirror(itemstack), dropper))
            {
                default:
                case CallSuper:
                    return super.b(pos, itemstack);
                case False:
                    return false;
                case True:
                    return true;
            }
        }
        
        @Override
        public void update()
        {
            final Dropper dropper = new CraftDropper(this.getLocation().getBlock());
            switch (this.dropperRule.update(dropper))
            {
                default:
                case CallSuper:
                    super.update();
                    return;
                case Return:
                    return;
            }
        }
        
        /**
         * returns the block position.
         * 
         * @return position.
         */
        public Location getLocation()
        {
            return new Location(this.world.getWorld(), this.position.getX(), this.position.getY(), this.position.getZ());
        }

        @Override
        public void c()
        {
            // update tick
            final Dropper dropper = new CraftDropper(this.getLocation().getBlock());
            this.dropperRule.updateTick(dropper);
        }
    }
    
}
