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

package de.minigameslib.mclib.api.gui;

import java.util.List;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.shared.api.com.DataFragment;

/**
 * Interface for providing list data.
 * 
 * @author mepeisen
 */
public interface SGuiListSupplier
{
    
    /**
     * Select Data from list without ordering or filtering
     * 
     * @param startIndex
     *            the start index
     * @param maxCount
     *            the maximum entry count
     * @return the list of data
     * @throws McException
     *             thrown if there are errors accessing the data
     */
    List<? extends DataFragment> select(int startIndex, int maxCount) throws McException;
    
    /**
     * Counts the data entries without filters.
     * 
     * @return number of data entries in the list
     * @throws McException
     *             thrown if there are errors accessing the data
     */
    int count() throws McException;
    
}
