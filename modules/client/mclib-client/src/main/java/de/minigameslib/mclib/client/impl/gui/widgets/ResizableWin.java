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

package de.minigameslib.mclib.client.impl.gui.widgets;

import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.Widget;
import net.minecraft.client.Minecraft;

/**
 * A resizable window.
 * 
 * @author mepeisen
 */
public class ResizableWin extends ResizableFrame
{
    
    /**
     * Constructor
     * 
     * @param title window title.
     * @param content the window content.
     * @param closable {@code true} to display a close button
     * @param hasScroll {@code true} to wrap contents inside a scroll pane.
     */
    public ResizableWin(String title, Widget content, boolean closable, boolean hasScroll)
    {
        setTheme("scrollPaneDemoDialog1"); //$NON-NLS-1$
        setTitle(title);
        
        if (hasScroll)
        {
            final ScrollPane scrollPane = new ScrollPane(content);
            scrollPane.setTheme("/tableScrollPane"); //$NON-NLS-1$
            add(scrollPane);
        }
        else
        {
            this.add(content);
        }
        
        if (closable)
        {
            this.addCloseCallback(new Runnable(){

                @Override
                public void run()
                {
                    Minecraft.getMinecraft().displayGuiScreen(null);
                }
                
            });
        }
    }
    
}
