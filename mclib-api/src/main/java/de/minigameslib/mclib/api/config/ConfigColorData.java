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

package de.minigameslib.mclib.api.config;

import org.bukkit.Color;

import de.minigameslib.mclib.shared.api.com.ColorData;

/**
 * Color object for storing and reloading from config.
 * 
 * @author mepeisen
 */
public class ConfigColorData extends ColorData
{
    
    /**
     * Constructor for reading from file.
     */
    public ConfigColorData()
    {
        // empty
    }
    
    /**
     * Constructor for new data.
     * 
     * @param red
     *            red color component
     * @param green
     *            green color component
     * @param blue
     *            blue color component
     */
    public ConfigColorData(byte red, byte green, byte blue)
    {
        super(red, green, blue);
    }
    
    /**
     * Constructor for new data.
     * 
     * @param red
     *            red color component
     * @param green
     *            green color component
     * @param blue
     *            blue color component
     */
    public ConfigColorData(int red, int green, int blue)
    {
        super((byte) red, (byte) green, (byte) blue);
    }
    
    /**
     * Converts this color to a bukkit color.
     * 
     * @return bukkit color.
     */
    public Color toBukkitColor()
    {
        return Color.fromRGB(this.getRedAsInt(), this.getGreenAsInt(), this.getBlueAsInt());
    }
    
    /**
     * Converts bukkit color to config color.
     * 
     * @param bukkitColor
     *            the bukkit color
     * @return config color
     */
    public static ConfigColorData fromBukkitColor(Color bukkitColor)
    {
        return new ConfigColorData(bukkitColor.getRed(), bukkitColor.getGreen(), bukkitColor.getBlue());
    }
    
}
