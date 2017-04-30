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

package de.minigameslib.mclib.pshared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * Data Fragment for the {@link CoreMessages#Ping} message.
 * 
 * @author mepeisen
 */
public class PingData extends AnnotatedDataFragment
{
    
    /**
     * the block meta data.
     */
    @PersistentField
    protected List<BlockMetaData> blocks = new ArrayList<>();
    
    /**
     * the item meta data.
     */
    @PersistentField
    protected List<ItemMetaData> items = new ArrayList<>();
    
    /**
     * Api version of server.
     */
    @PersistentField
    protected int api;
    
    /**
     * Ping is sent on player login.
     */
    @PersistentField
    protected boolean login;

    /**
     * Returns the api version of the server.
     * @return the api
     */
    public int getApi()
    {
        return this.api;
    }

    /**
     * Sets the api version of the server.
     * @param api the api to set
     */
    public void setApi(int api)
    {
        this.api = api;
    }
    
    /**
     * Returns the player login flag.
     * @return the login
     */
    public boolean isLogin()
    {
        return this.login;
    }

    /**
     * Sets the player login flag.
     * @param login the login to set
     */
    public void setLogin(boolean login)
    {
        this.login = login;
    }

    /**
     * Adds new block meta data.
     * @param id block id
     * @param hardness block hardness
     * @param resistance block resistance
     * @return block meta data
     */
    public BlockMetaData addMeta(int id, float hardness, float resistance)
    {
        final BlockMetaData m = new BlockMetaData();
        m.setId(id);
        m.setHardness(hardness);
        m.setResistance(resistance);
        this.blocks.add(m);
        return m;
    }
    
    /**
     * Adds new item meta data.
     * @param id item id
     * @param durability item durability
     * @param attackSpeed attack speed
     * @param attackDmg attack damage
     * @param cls item class
     */
    public void addMeta(int id, int durability, double attackSpeed, double attackDmg, ItemClass cls)
    {
        final ItemMetaData m = new ItemMetaData();
        m.setId(id);
        m.setDurability(durability);
        m.setSpeed(attackSpeed);
        m.setDamage(attackDmg);
        m.setCls(cls);
        this.items.add(m);
    }
    
    /**
     * Returns the list of meta data.
     * @return the meta
     */
    public List<BlockMetaData> getBlocks()
    {
        return this.blocks;
    }
    
    /**
     * Returns the list of meta data.
     * @return the meta
     */
    public List<ItemMetaData> getItems()
    {
        return this.items;
    }
    
    /**
     * the item class.
     * @author mepeisen
     *
     */
    public enum ItemClass
    {
        /** a pickaxe. */
        Pickaxe,
        /** an axe. */
        Axe,
        /** a shovel. */
        Shovel,
        /** a hoe. */
        Hoe,
        /** weapon. */
        Sword,
        /** armor: helmet. */
        Helmet,
        /** armor: chestplate. */
        Chestplate,
        /** armor: leggins. */
        Leggins,
        /** armor: boots. */
        Boots
    }

    /**
     * Single item data.
     */
    public static class ItemMetaData extends AnnotatedDataFragment
    {
        
        /** numeric item id. */
        @PersistentField
        protected int id;
        
        /** durability. */
        @PersistentField
        protected int dur;
        
        /**
         * The attack damage.
         */
        @PersistentField
        protected double dmg;
        
        /**
         * The attack speed.
         */
        @PersistentField
        protected double speed;
        
        /** item class. */
        @PersistentField
        protected ItemClass cls;

        /**
         * Returns the item class.
         * @return the cls
         */
        public ItemClass getCls()
        {
            return this.cls;
        }

        /**
         * Sets the item class.
         * @param cls the cls to set
         */
        public void setCls(ItemClass cls)
        {
            this.cls = cls;
        }

        /**
         * Returns the item id.
         * @return the id
         */
        public int getId()
        {
            return this.id;
        }

        /**
         * Sets the item id.
         * @param id the id to set
         */
        public void setId(int id)
        {
            this.id = id;
        }

        /**
         * returns the durability.
         * @return the durability
         */
        public int getDurability()
        {
            return this.dur;
        }

        /**
         * Sets the durability.
         * @param durability the durability to set
         */
        public void setDurability(int durability)
        {
            this.dur = durability;
        }

        /**
         * Returns the damage.
         * @return the damage
         */
        public double getDamage()
        {
            return this.dmg;
        }

        /**
         * Sets the damage.
         * @param damage the damage to set
         */
        public void setDamage(double damage)
        {
            this.dmg = damage;
        }

        /**
         * Returns the speed.
         * @return the speed
         */
        public double getSpeed()
        {
            return this.speed;
        }

        /**
         * Sets the speed.
         * @param speed the speed to set
         */
        public void setSpeed(double speed)
        {
            this.speed = speed;
        }
    }

    /**
     * Single block data.
     */
    public static class BlockMetaData extends AnnotatedDataFragment
    {
        
        /** numeric block id. */
        @PersistentField
        protected int id;
        
        //CHECKSTYLE:OFF
        /** hardness. */
        @PersistentField
        protected float h;
        
        /** resistance. */
        @PersistentField
        protected float r;
        //CHECKSTYLE:ON
        
        /**
         * opaqueness of block variants.
         */
        @PersistentField
        protected Map<Integer, Boolean> op = new HashMap<>();

        /**
         * Returns the block id.
         * @return the id
         */
        public int getId()
        {
            return this.id;
        }

        /**
         * Sets the block id.
         * @param id the id to set
         */
        public void setId(int id)
        {
            this.id = id;
        }

        /**
         * Returns the hadrness.
         * @return the h
         */
        public float getHardness()
        {
            return this.h;
        }

        /**
         * Sets the hardness.
         * @param h the h to set
         */
        public void setHardness(float h)
        {
            this.h = h;
        }

        /**
         * Returns the resistance.
         * @return the r
         */
        public float getResistance()
        {
            return this.r;
        }

        /**
         * Sets the resistance.
         * @param r the r to set
         */
        public void setResistance(float r)
        {
            this.r = r;
        }

        /**
         * Returns the opaqueness of block variants.
         * @return the opaqueness of block variants
         */
        public Map<Integer, Boolean> getOpaqueness()
        {
            return this.op;
        }
        
    }
    
}
