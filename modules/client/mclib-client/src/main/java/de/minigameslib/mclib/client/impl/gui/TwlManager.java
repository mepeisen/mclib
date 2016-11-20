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
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.lwjgl.opengl.Util;

import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.input.lwjgl.LWJGLInput;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * gui manager helper
 * 
 * @author mepeisen
 */
public class TwlManager extends Widget
{
    
    /**
     * The initialized instance of TwlManager.
     */
    public static TwlManager instance;
    /**
     * The height of the screen that the widget will render on.
     */
    public static int        screenheight;
    /**
     * The width of the screen that the widget will render on.
     */
    public static int        screenwidth;
    
    /** the theme url. */
    public static URL        themeURL;
    
    /**
     * The widget that is currently displayed.
     */
    public Widget            currentWidget = null;
    /**
     * This is a reference to a TWL class that is used to render the widgets.
     */
    public GUI               gui           = null;
    /**
     * This is a reference to Minecraft.
     */
    public Minecraft         minecraftInstance;
    /**
     * This is the rendered used by TWL.
     */
    public LWJGLRenderer     renderer      = null;
    /**
     * This is the ScaledResolution class that is used to scale all of the widgets.
     */
    public ScaledResolution  screenSize    = null;
    /**
     * This the the ThemeManager for GuiAPI.
     */
    public ThemeManager      theme         = null;
    
    /**
     * get the instance of GuiWidget, creating it if needed
     * 
     * @return TwlManager singleton
     */
    public static TwlManager getInstance()
    {
        if (TwlManager.instance != null)
        {
            return TwlManager.instance;
        }
        
        try
        {
            Util.checkGLError();
            TwlManager.instance = new TwlManager();
            Util.checkGLError();
            TwlManager.instance.renderer = new LWJGLRenderer();
            Util.checkGLError();
            String themename = "guiTheme.xml"; //$NON-NLS-1$
            Util.checkGLError();
            TwlManager.instance.gui = new GUI(TwlManager.instance, TwlManager.instance.renderer, new LWJGLInput());
            Util.checkGLError();
            TwlManager.themeURL = new URL("classloader", "", -1, themename, new URLStreamHandler() { //$NON-NLS-1$ //$NON-NLS-2$
                @Override
                protected URLConnection openConnection(URL paramURL) throws IOException
                {
                    String file = paramURL.getFile();
                    if (file.startsWith("/")) //$NON-NLS-1$
                    {
                        file = file.substring(1);
                    }
                    return TwlManager.class.getClassLoader().getResource(file).openConnection();
                }
            });
            Util.checkGLError();
            Util.checkGLError();
            TwlManager.instance.theme = ThemeManager.createThemeManager(TwlManager.themeURL, TwlManager.instance.renderer);
            Util.checkGLError();
            if (TwlManager.instance.theme == null)
            {
                throw new RuntimeException("Theme loading error"); //$NON-NLS-1$
            }
            TwlManager.instance.setTheme(""); //$NON-NLS-1$
            TwlManager.instance.gui.applyTheme(TwlManager.instance.theme);
            TwlManager.instance.minecraftInstance = Minecraft.getMinecraft();
            TwlManager.instance.screenSize = new ScaledResolution(TwlManager.instance.minecraftInstance);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            RuntimeException e2 = new RuntimeException("error loading theme"); //$NON-NLS-1$
            e2.initCause(e);
            throw e2;
        }
        return TwlManager.instance;
    }
    
    /**
     * This creates a new instance of GuiWidgetScreen. It should only be used internally. Please use the static method getInstance() instead.
     */
    private TwlManager()
    {
    }
    
    @Override
    public void layout()
    {
        this.screenSize = new ScaledResolution(this.minecraftInstance);
        if (this.currentWidget != null)
        {
            TwlManager.screenwidth = this.screenSize.getScaledWidth();
            TwlManager.screenheight = this.screenSize.getScaledHeight();
            this.currentWidget.setSize(TwlManager.screenwidth, TwlManager.screenheight);
            this.currentWidget.setPosition(0, 0);
        }
    }
    
    /**
     * Removes all children and clears the current widget.
     */
    public void resetScreen()
    {
        removeAllChildren();
        this.currentWidget = null;
    }
    
    /**
     * to be called only from GuiModScreen, sets the widget to display. GuiModScreen manages this.
     * 
     * @param widget
     *            widget to display
     */
    public void setScreen(Widget widget)
    {
        this.gui.resyncTimerAfterPause();
        this.gui.clearKeyboardState();
        this.gui.clearMouseState();
        removeAllChildren();
        add(widget);
        
        this.currentWidget = widget;
    }
    
}
