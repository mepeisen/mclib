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

package de.minigameslib.mclib.impl.yml;

import java.util.HashMap;
import java.util.Map;

import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * A section holding mappings including comments for nodes.
 * 
 * @author mepeisen
 */
public class YmlCommentableSection extends MemoryDataSection
{
    
    /**
     * comments for yml nodes.
     */
    private Map<String, String[]> comments = new HashMap<>();

    /**
     * Constructor
     */
    protected YmlCommentableSection()
    {
        super();
    }

    /**
     * Constructor.
     * @param path
     * @param name
     * @param parent
     */
    public YmlCommentableSection(String path, String name, MemoryDataSection parent)
    {
        super(path, name, parent);
    }

    @Override
    protected MemoryDataSection createSection(String path2, String name2, MemoryDataSection parent2)
    {
        return new YmlCommentableSection(path2, name2, parent2);
    }
    
    /**
     * Returns the comment for given key.
     * @param key
     * @return comment lines or {@code null} if no comment was set.
     */
    public String[] getComment(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            return this.comments.get(key);
        }
        final String subkey = key.substring(0, indexof);
        final DataSection obj = this.getSection(subkey);
        if (obj instanceof YmlCommentableSection)
        {
            return ((YmlCommentableSection) obj).getComment(key.substring(indexof + 1));
        }
        return null;
    }
    
    /**
     * Sets the comment lines.
     * @param key
     * @param newValue new comment lines or {@code null} to clear the comment.
     */
    public void setComment(String key, String[] newValue)
    {
        if (newValue == null)
        {
            this.clearComment(key);
        }
        else
        {
            this.doSetComment(key, newValue);
        }
    }
    
    /**
     * @param key
     */
    private void clearComment(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            this.comments.remove(key);
            return;
        }
        final DataSection obj = this.getSection(key.substring(0, indexof));
        if (obj instanceof YmlCommentableSection)
        {
            ((YmlCommentableSection) obj).clearComment(key.substring(indexof + 1));
        }
    }
    
    /**
     * @param key
     * @param value
     */
    private void doSetComment(String key, String[] value)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            this.comments.put(key, value);
            return;
        }
        ((YmlCommentableSection) this.createSection(key.substring(0, indexof))).doSetComment(key.substring(indexof + 1), value);
    }
    
}
