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

package de.minigames.mclib.nms.v185;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import de.minigames.mclib.nms.v185.entity.EntityHelper1_8_5;
import de.minigames.mclib.nms.v185.entity.HologramHelper1_8_5;
import de.minigames.mclib.nms.v185.entity.SignHelper1_8_5;
import de.minigameslib.mclib.nms.api.AnvilManagerInterface;
import de.minigameslib.mclib.nms.api.ChatSystemInterface;
import de.minigameslib.mclib.nms.api.EntityHelperInterface;
import de.minigameslib.mclib.nms.api.EventSystemInterface;
import de.minigameslib.mclib.nms.api.HologramHelperInterface;
import de.minigameslib.mclib.nms.api.InventoryManagerInterface;
import de.minigameslib.mclib.nms.api.ItemHelperInterface;
import de.minigameslib.mclib.nms.api.NmsFactory;
import de.minigameslib.mclib.nms.api.PlayerManagerInterface;
import de.minigameslib.mclib.nms.api.SignHelperInterface;

/**
 * Factory to create NMS relevant classes.
 * 
 * @author mepeisen
 */
public class NmsFactory1_8_5 implements NmsFactory
{
    
    /** the implementation classes. */
    private final Map<Class<?>, Supplier<?>> impls = new HashMap<>();
    
    /**
     * Constructor.
     */
    public NmsFactory1_8_5()
    {
        this.impls.put(EventSystemInterface.class, EventSystem1_8_5::new);
        this.impls.put(InventoryManagerInterface.class, InventoryManager1_8_5::new);
        this.impls.put(AnvilManagerInterface.class, AnvilManager1_8_5::new);
        this.impls.put(PlayerManagerInterface.class, PlayerManager1_8_5::new);
        this.impls.put(ChatSystemInterface.class, ChatSystem1_8_5::new);
        this.impls.put(EntityHelperInterface.class, EntityHelper1_8_5::new);
        this.impls.put(ItemHelperInterface.class, ItemHelper1_8_5::new);
        this.impls.put(HologramHelperInterface.class, HologramHelper1_8_5::new);
        this.impls.put(SignHelperInterface.class, SignHelper1_8_5::new);
    }
    
    @Override
    public <T> T create(Class<T> clazz)
    {
        final Supplier<?> supplier = this.impls.get(clazz);
        if (supplier != null)
        {
            return clazz.cast(supplier.get());
        }
        return null;
    }
    
}
