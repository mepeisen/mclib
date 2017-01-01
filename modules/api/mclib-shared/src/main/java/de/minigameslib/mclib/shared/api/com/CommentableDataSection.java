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

package de.minigameslib.mclib.shared.api.com;

/**
 * An extension interface to DataSections.
 * 
 * <p>
 * A commentable data section can hold comment lines for the section itself or for
 * single values within the data section. Not all data section implementations support
 * this feature. Check the data section with instanceof if the feature is supported.
 * </p>
 * 
 * @author mepeisen
 */
public interface CommentableDataSection
{
    
    /**
     * Returns the comments for this section.
     * @return section comments lines, each array entry is one line; maybe {@code null}
     */
    String[] getSectionComments();
    
    /**
     * Sets the comments for this section.
     * @param commentLines comments lines, each array entry is one line; maybe {@code null} to clear the comments.
     */
    void setSectionComments(String[] commentLines);
    
    /**
     * Returns the value comments for a value identified by key.
     * @param key The key the comment is associated with. Periods are not allowed.
     * @return comments lines, each array entry is one line; maybe {@code null}
     */
    String[] getValueComments(String key);
    
    /**
     * Sets the comments associated with given key.
     * @param key The key the comment is associated with. Periods are not allowed.
     * @param commentLines comments lines, each array entry is one line; maybe {@code null} to clear the comments.
     */
    void setValueComments(String key, String[] commentLines);
    
}
