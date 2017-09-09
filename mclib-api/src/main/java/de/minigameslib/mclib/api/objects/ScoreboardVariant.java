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

package de.minigameslib.mclib.api.objects;

import java.io.Serializable;
import java.util.List;

/**
 * A scoreboard variant created for a list of users.
 * 
 * @author mepeisen
 */
public interface ScoreboardVariant
{
    
    /**
     * Updates lines and resent them to clients.
     */
    void updateLines();
    
    /**
     * Replaces lines with given strings.
     * 
     * @param strings
     *            text to display; maybe a String or a localized message.
     */
    void setLines(Serializable... strings);
    
    /**
     * Returns the scoreboard lines.
     * 
     * @return scoreboard lines.
     */
    List<Serializable> getLines();
    
    /**
     * Adds a line to be displayed.
     * 
     * @param string
     *            text to display; maybe a String or a localized message.
     */
    void addLine(Serializable string);
    
    /**
     * Removes a line.
     * 
     * @param lineNumber
     *            line to remove; starting with 1.
     */
    void removeLine(int lineNumber);
    
    /**
     * Changes line.
     * 
     * @param lineNumber
     *            line to change; starting with 1.
     * @param string
     *            text to display; maybe a String or a localized message.
     */
    void changeLine(int lineNumber, Serializable string);
    
    /**
     * Removes all scoreboard lines.
     */
    void clearLines();
    
}
