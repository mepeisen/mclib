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
import java.util.List;
import java.util.Map;

import de.minigameslib.mclib.impl.yml.YmlFile.MyCommentMap;
import de.minigameslib.mclib.shared.api.com.CommentableDataSection;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * A section holding mappings including comments for nodes.
 * 
 * @author mepeisen
 */
public class YmlCommentableSection extends MemoryDataSection implements CommentableDataSection
{
    
    /**
     * comments for yml nodes.
     */
    private Map<String, String[]> comments = new HashMap<>();
    
    /**
     * The comments on map level.
     */
    private String[]              mapComments;
    
    /**
     * Constructor.
     */
    protected YmlCommentableSection()
    {
        super();
    }
    
    /**
     * Constructor.
     * 
     * @param path
     *            path of this section
     * @param name
     *            name of this section
     * @param parent
     *            the parent section
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
    
    @Override
    protected void clearAll()
    {
        super.clearAll();
        this.comments.clear();
        this.mapComments = null;
    }
    
    /**
     * Loads data from given comment map.
     * 
     * @param map
     *            map to read data from.
     */
    protected void load(MyCommentMap map)
    {
        this.clearAll();
        // null mp --> empty file
        if (map == null)
        {
            return;
        }
        boolean isFirst = true;
        boolean isFirstCommentSet = false;
        String firstKey = null;
        
        for (final Map.Entry<Object, Object> entry : map.entrySet())
        {
            final String strKey = entry.getKey().toString();
            if (entry.getValue() instanceof MyCommentMap)
            {
                ((YmlCommentableSection) this.createSection(strKey)).load((MyCommentMap) entry.getValue());
            }
            else if (entry.getValue() instanceof List<?>)
            {
                this.setPrimitiveList(strKey, (List<?>) entry.getValue());
            }
            else
            {
                this.set(strKey, entry.getValue());
            }
            if (map.keyComments != null && map.keyComments.containsKey(entry.getKey()))
            {
                this.comments.put(strKey, removeCommentChar(map.keyComments.get(entry.getKey())));
            }
            // TODO work around current snakeyaml bug to associate comments with first map key
            if (isFirst)
            {
                isFirst = false;
                isFirstCommentSet = this.comments.containsKey(strKey);
                firstKey = strKey;
            }
        }
        this.mapComments = removeCommentChar(map.mapLevelComments);
        if (this.mapComments != null && !isFirstCommentSet)
        {
            this.comments.put(firstKey, this.mapComments);
            this.mapComments = null;
        }
    }
    
    /**
     * removes comment char from input.
     * 
     * @param strings
     *            input.
     * 
     * @return string array
     */
    private String[] removeCommentChar(String[] strings)
    {
        if (strings == null)
        {
            return null;
        }
        final String[] dest = new String[strings.length];
        for (int i = dest.length - 1; i >= 0; i--)
        {
            final String src = strings[i];
            if (src.startsWith("# ")) //$NON-NLS-1$
            {
                dest[i] = src.substring(2);
            }
            else if (src.startsWith("#")) //$NON-NLS-1$
            {
                dest[i] = src.substring(1);
            }
            else
            {
                dest[i] = src;
            }
        }
        return dest;
    }
    
    /**
     * Stores data to given comment map.
     * 
     * @param map
     *            target map
     */
    protected void store(MyCommentMap map)
    {
        map.mapLevelComments = addCommentChar(this.mapComments);
        for (final String key : this.getKeys(false))
        {
            final Object value = this.get(key);
            if (value instanceof YmlCommentableSection)
            {
                final MyCommentMap child = new MyCommentMap();
                map.put(key, child);
                ((YmlCommentableSection) value).store(child);
            }
            else
            {
                map.put(key, value);
                if (this.comments.containsKey(key))
                {
                    if (map.keyComments == null)
                    {
                        map.keyComments = new HashMap<>();
                    }
                    map.keyComments.put(key, addCommentChar(this.comments.get(key)));
                }
            }
        }
    }
    
    /**
     * Adds comment char to output.
     * 
     * @param strings
     *            source string.
     * 
     * @return string array
     */
    private String[] addCommentChar(String[] strings)
    {
        if (strings == null)
        {
            return null;
        }
        final String[] dest = new String[strings.length];
        for (int i = dest.length - 1; i >= 0; i--)
        {
            final String src = strings[i];
            if (src.startsWith("# ")) //$NON-NLS-1$
            {
                dest[i] = src;
            }
            else if (src.startsWith("#")) //$NON-NLS-1$
            {
                dest[i] = "# " + src.substring(1); //$NON-NLS-1$
            }
            else
            {
                dest[i] = "# " + src; //$NON-NLS-1$
            }
        }
        return dest;
    }
    
    /**
     * Returns the comment for given key.
     * 
     * @param key
     *            section key or path
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
     * 
     * @param key
     *            section key or path
     * @param newValue
     *            new comment lines or {@code null} to clear the comment.
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
     * Clears comment for given key or path.
     * 
     * @param key
     *            key or path to clear the comment
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
     * Sets the comment.
     * 
     * @param key
     *            section key or path to set the comment
     * @param value
     *            comment value
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
    
    @Override
    public String[] getSectionComments()
    {
        return this.mapComments;
    }
    
    @Override
    public void setSectionComments(String[] commentLines)
    {
        this.mapComments = commentLines;
    }
    
    @Override
    public String[] getValueComments(String key)
    {
        if (key.indexOf('.') != -1)
        {
            throw new IllegalArgumentException("key must not contain '.'"); //$NON-NLS-1$
        }
        return this.comments.get(key);
    }
    
    @Override
    public void setValueComments(String key, String[] commentLines)
    {
        if (key.indexOf('.') != -1)
        {
            throw new IllegalArgumentException("key must not contain '.'"); //$NON-NLS-1$
        }
        if (commentLines == null)
        {
            this.comments.remove(key);
        }
        else
        {
            this.comments.put(key, commentLines);
        }
    }
    
}
