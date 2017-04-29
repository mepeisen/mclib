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

/**
 * Some constants shared between client and server.
 * 
 * @author mepeisen
 */
public interface MclibConstants
{
    
    /**
     * the minimum block id for custom blocks.
     */
    int MIN_BLOCK_ID = 3500;
    
    /**
     * the maximum block id for custom blocks.
     */
    int MAX_BLOCK_ID = 3749;
    
    /**
     * the minimum item id for custom items.
     */
    int MIN_ITEM_ID = 3750;
    
    /**
     * the maximum item id for custom items.
     */
    int MAX_ITEM_ID = 3999;
    
}
