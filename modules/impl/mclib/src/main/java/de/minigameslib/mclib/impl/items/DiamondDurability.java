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

package de.minigameslib.mclib.impl.items;

import java.util.Locale;

/**
 * The diamond durability values.
 * 
 * @author mepeisen
 */
class DiamondDurability implements Durability
{
    
    /** the durability values. */
    public static final DiamondDurability[] VALUES;
    
    static
    {
        int count = 1562;
        VALUES = new DiamondDurability[count - 1];
        for (int i = 0; i < count - 1; i++)
        {
            final short svalue = (short) (i + 1);
            final double dvalue = ((double) svalue) / ((double) count);
            final String strvalue = String.format(Locale.ENGLISH, "%20.19f", dvalue); //$NON-NLS-1$
            VALUES[i] = new DiamondDurability(svalue, strvalue);
        }
    }
    
    /** the short value. */
    private final short  shortValue;
    
    /** the string value. */
    private final String stringValue;
    
    /**
     * @param shortValue
     * @param stringValue
     */
    private DiamondDurability(short shortValue, String stringValue)
    {
        this.shortValue = shortValue;
        this.stringValue = stringValue;
    }
    
    @Override
    public short getItemStackDurability()
    {
        return this.shortValue;
    }
    
    @Override
    public String getModelDurability()
    {
        return this.stringValue;
    }
    
}
