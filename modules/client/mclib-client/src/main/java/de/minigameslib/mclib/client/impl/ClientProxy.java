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

package de.minigameslib.mclib.client.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import de.minigameslib.mclib.client.impl.gui.TwlManager;
import de.minigameslib.mclib.client.impl.gui.TwlScreen;
import de.minigameslib.mclib.client.impl.markers.MarkerInterface;
import de.minigameslib.mclib.client.impl.util.CameraPos;
import de.minigameslib.mclib.client.nms.MclibClientNms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

/**
 * Client stuff
 * 
 * @author mepeisen
 */
public class ClientProxy
{
    
    /** minecraft instance. */
    private static Minecraft        mc          = Minecraft.getMinecraft();
    
    /** key binding to activate MINIGAMES gui. */
    private static final KeyBinding KEY_BINDING = new KeyBinding("Sample GUI", Keyboard.KEY_M, "MINIGAMES"); //$NON-NLS-1$ //$NON-NLS-2$
    
    /** block markers. */
    private final Map<String, MarkerInterface> markers = new ConcurrentHashMap<>();
    
    /**
     * Constructor.
     */
    public ClientProxy()
    {
        ClientRegistry.registerKeyBinding(KEY_BINDING);
    }
    
    /**
     * Key event to display MINIGAMES Gui.
     * @param evt
     */
    @SubscribeEvent
    public void onKey(KeyInputEvent evt)
    {
        if (mc.thePlayer != null && mc.theWorld != null && KEY_BINDING.isPressed())
        {
            // set the marker
//            if (this.markers.size() == 2)
//            {
//                this.markers.clear();
//            }
//            else if (this.markers.size() == 0)
//            {
//                final int x = (int) Math.floor(mc.thePlayer.posX);
//                final int y = (int) Math.floor(mc.thePlayer.posY) - 1;
//                final int z = (int) Math.floor(mc.thePlayer.posZ);
//                if (MclibMod.TRACE)
//                {
//                    System.out.println("Adding marker at " + x + "/" + y + "/" + z); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//                }
//                this.markers.add(new BlockMarker(x, y, z, MarkerColor.YELLOW));
//            }
//            else
//            {
//                final int x = (int) Math.floor(mc.thePlayer.posX);
//                final int y = (int) Math.floor(mc.thePlayer.posY) - 1;
//                final int z = (int) Math.floor(mc.thePlayer.posZ);
//                if (MclibMod.TRACE)
//                {
//                    System.out.println("Adding marker at " + x + "/" + y + "/" + z); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//                }
//                this.markers.add(new CuboidMarker(x, y, z, x + 5, y + 5, z + 5, MarkerColor.BLUE));
//            }
        }
    }
    
    /**
     * Gui open event to control TWL.
     * @param evt
     */
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent evt)
    {
        if (MclibClientNms.getGui(evt) instanceof TwlScreen)
        {
            TwlManager.getInstance().setScreen(((TwlScreen) MclibClientNms.getGui(evt)).getMainWidget());
        }
    }
    
    /**
     * world render event.
     * @param event
     */
    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event)
    {
        onRender(MclibClientNms.getPartialTicks(event));
    }

    /**
     * Rendering
     * @param partialTicks
     */
    public void onRender(float partialTicks)
    {
        // TODO enabled check
//        if(CUIController.isEnable()==false)
//            return;

        try
        {
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LIGHT0);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            try
            {
                CameraPos cameraPos = new CameraPos(Minecraft.getMinecraft().getRenderViewEntity(), partialTicks);
                for (final MarkerInterface marker : this.markers.values().toArray(new MarkerInterface[0]))
                {
                    marker.render(cameraPos);
                }
            }
            catch (Exception e)
            {
                // TODO logging
                e.printStackTrace();
            }

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHT0);

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
        catch (Exception ex)
        {
            // TODO logging
            ex.printStackTrace();
        }
    }

    /**
     * @param markerId
     * @param marker
     */
    public void setMarker(String markerId, MarkerInterface marker)
    {
        this.markers.put(markerId, marker);
    }

    /**
     * @param markerId
     */
    public void removeMarker(String markerId)
    {
        this.markers.remove(markerId);
    }

    /**
     * 
     */
    public void resetMarkers()
    {
        this.markers.clear();
    }
    
}
