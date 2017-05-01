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

/**
 * A simple implementation of click gui.
 * 
 * @author mepeisen
 */
public class SimpleClickGui implements ClickGuiInterface
{
    
    /** the unique gui id. */
    private final ClickGuiId            guiId;
    
    /** the initial gui page. */
    private final ClickGuiPageInterface initPage;
    
    /** the line count. */
    private final int                   lineCount;
    
    /**
     * Constructor to create a simple click gui.
     * 
     * @param guiId
     *            the gui id.
     * @param initPage
     *            the initial page.
     * @param lineCount
     *            the line count.
     */
    public SimpleClickGui(ClickGuiId guiId, ClickGuiPageInterface initPage, int lineCount)
    {
        this.guiId = guiId;
        this.initPage = initPage;
        this.lineCount = lineCount;
    }
    
    @Override
    public ClickGuiId getUniqueId()
    {
        return this.guiId;
    }
    
    @Override
    public ClickGuiPageInterface getInitialPage()
    {
        return this.initPage;
    }
    
    @Override
    public int getLineCount()
    {
        return this.lineCount;
    }
    
}
