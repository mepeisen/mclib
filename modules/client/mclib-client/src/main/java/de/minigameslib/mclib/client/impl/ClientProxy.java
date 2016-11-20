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

import org.lwjgl.input.Keyboard;

import de.minigameslib.mclib.client.impl.gui.MclibGuiHandler;
import de.minigameslib.mclib.client.impl.gui.TwlManager;
import de.minigameslib.mclib.client.impl.gui.TwlScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiOpenEvent;
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
            mc.thePlayer.openGui(MclibMod.instance, MclibGuiHandler.IM_SAMPLE_GUI, mc.theWorld, 0, 0, 0);
        }
    }
    
    /**
     * Gui open event to control TWL.
     * @param evt
     */
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent evt)
    {
        if (evt.getGui() instanceof TwlScreen)
        {
            TwlManager.getInstance().setScreen(((TwlScreen) evt.getGui()).getMainWidget());
        }
    }
    
}
