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

package de.minigameslib.mclib.impl.skin;

import de.minigameslib.mclib.api.skin.SkinInterface;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * A skin represented from string.
 * 
 * @author mepeisen
 */
public class SkinFromTextures extends AnnotatedDataFragment implements SkinInterface
{
    
    /**
     * String textures
     */
    @PersistentField
    protected String textures;

    /**
     * 
     */
    public SkinFromTextures()
    {
        // empty
    }

    /**
     * @param textures
     */
    public SkinFromTextures(String textures)
    {
        this.textures = textures;
    }

    /**
     * @return the textures
     */
    public String getTextures()
    {
        return this.textures;
    }
    
}
