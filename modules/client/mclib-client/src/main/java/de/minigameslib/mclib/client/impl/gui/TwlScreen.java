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

package de.minigameslib.mclib.client.impl.gui;

import java.io.IOException;

import de.matthiasmann.twl.Widget;
import net.minecraft.client.gui.GuiScreen;

/**
 * Minecraft screen to handle TWL.
 * 
 * @author mepeisen
 */
public class TwlScreen extends GuiScreen
{
    
    /** TWL widget to be displayed. */
    private Widget widget;
    
    /**
     * Constructor.
     * 
     * @param widget
     */
    public TwlScreen(Widget widget)
    {
        this.widget = widget;
    }
    
    @Override
    public void drawScreen(int var1, int var2, float var3)
    {
        this.drawDefaultBackground();
        
        // LWJGLRenderer var4 = (LWJGLRenderer) TwlManager.getInstance().gui
        // .getRenderer();
        // ScaledResolution var5 = new ScaledResolution(
        // TwlManager.getInstance().minecraftInstance);
        // RenderScale.scale = var5.getScaleFactor();
        // var4.syncViewportSize();
        TwlManager.getInstance().gui.update();
    }
    
    @Override
    public void onGuiClosed()
    {
        TwlManager.getInstance().resetScreen();
        super.onGuiClosed();
    }
    
    /**
     * Returns the main widget that will be displayed.
     * 
     * @return main widget
     */
    public Widget getMainWidget()
    {
        return this.widget;
    }
    
    @Override
    public void handleInput() throws IOException
    {
        // empty
    }
    
}
