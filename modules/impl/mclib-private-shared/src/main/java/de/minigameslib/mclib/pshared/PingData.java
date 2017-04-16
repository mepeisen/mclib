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
import java.util.List;

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
     * Api version of server
     */
    @PersistentField
    protected int api;
    
    /**
     * Ping is sent on player login
     */
    @PersistentField
    protected boolean login;

    /**
     * @return the api
     */
    public int getApi()
    {
        return this.api;
    }

    /**
     * @param api the api to set
     */
    public void setApi(int api)
    {
        this.api = api;
    }
    
    /**
     * @return the login
     */
    public boolean isLogin()
    {
        return this.login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(boolean login)
    {
        this.login = login;
    }

    /**
     * Adds new meta data
     * @param id
     * @param hardness
     * @param resistance
     */
    public void addMeta(int id, float hardness, float resistance)
    {
        final BlockMetaData m = new BlockMetaData();
        m.setId(id);
        m.setHardness(hardness);
        m.setResistance(resistance);
        this.blocks.add(m);
    }
    
    /**
     * Adds new meta data
     * @param id
     * @param durability
     * @param attackSpeed 
     * @param attackDmg 
     * @param cls
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
     * Returns the list of meta data
     * @return the meta
     */
    public List<BlockMetaData> getBlocks()
    {
        return this.blocks;
    }
    
    /**
     * Returns the list of meta data
     * @return the meta
     */
    public List<ItemMetaData> getItems()
    {
        return this.items;
    }
    
    /**
     * the item class
     * @author mepeisen
     *
     */
    public enum ItemClass
    {
        /** a pickaxe */
        Pickaxe,
        /** an axe */
        Axe,
        /** a shovel */
        Shovel,
        /** a hoe */
        Hoe,
        /** weapon */
        Sword,
        /** armor: helmet */
        Helmet,
        /** armor: chestplate */
        Chestplate,
        /** armor: leggins */
        Leggins,
        /** armor: boots */
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
        
        /** durability */
        @PersistentField
        protected int dur;
        
        /**
         * The attack damage
         */
        @PersistentField
        protected double dmg;
        
        /**
         * The attack speed
         */
        @PersistentField
        protected double speed;
        
        /** item class */
        @PersistentField
        protected ItemClass cls;

        /**
         * @return the cls
         */
        public ItemClass getCls()
        {
            return this.cls;
        }

        /**
         * @param cls the cls to set
         */
        public void setCls(ItemClass cls)
        {
            this.cls = cls;
        }

        /**
         * @return the id
         */
        public int getId()
        {
            return this.id;
        }

        /**
         * @param id the id to set
         */
        public void setId(int id)
        {
            this.id = id;
        }

        /**
         * @return the durability
         */
        public int getDurability()
        {
            return this.dur;
        }

        /**
         * @param durability the durability to set
         */
        public void setDurability(int durability)
        {
            this.dur = durability;
        }

        /**
         * @return the damage
         */
        public double getDamage()
        {
            return this.dmg;
        }

        /**
         * @param damage the damage to set
         */
        public void setDamage(double damage)
        {
            this.dmg = damage;
        }

        /**
         * @return the speed
         */
        public double getSpeed()
        {
            return this.speed;
        }

        /**
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
        
        /** hardness */
        @PersistentField
        protected float h;
        
        /** resistance */
        @PersistentField
        protected float r;

        /**
         * @return the id
         */
        public int getId()
        {
            return this.id;
        }

        /**
         * @param id the id to set
         */
        public void setId(int id)
        {
            this.id = id;
        }

        /**
         * @return the h
         */
        public float getHardness()
        {
            return this.h;
        }

        /**
         * @param h the h to set
         */
        public void setHardness(float h)
        {
            this.h = h;
        }

        /**
         * @return the r
         */
        public float getResistance()
        {
            return this.r;
        }

        /**
         * @param r the r to set
         */
        public void setResistance(float r)
        {
            this.r = r;
        }
        
    }
    
}
