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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.minigameslib.mclib.client.impl.com.ComHandler;
import de.minigameslib.mclib.client.impl.com.MclibCoreHandler;
import de.minigameslib.mclib.client.impl.com.NetMessage;
import de.minigameslib.mclib.client.impl.gui.MclibGuiHandler;
import de.minigameslib.mclib.pshared.MclibCommunication;
import de.minigameslib.mclib.shared.api.com.CommunicationEndpointId;
import de.minigameslib.mclib.shared.api.com.CommunicationEndpointId.CommunicationServiceInterface;
import de.minigameslib.mclib.shared.api.com.DataSection;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * FORGE Mod for minecraft.
 * 
 * @author mepeisen
 *
 */
@Mod(modid = MclibMod.MODID, version = MclibMod.VERSION, updateJSON = "http://www.minigameslib.de/mclib/client.json")
public class MclibMod implements CommunicationServiceInterface
{
    
    /** the mod id. */
    public static final String          MODID    = "mclib";       //$NON-NLS-1$
    /** the version of this mod. */
    public static final String          VERSION  = "0.0.1";       //$NON-NLS-1$
    
    /** the client stuff proxy. */
    @SidedProxy(clientSide = "de.minigameslib.mclib.client.impl.ClientProxy")
    public static ClientProxy           clientProxy;
    
    /** minecraft instance. */
    public Minecraft                    mc;
    
    /** mod instance. */
    @Instance
    public static MclibMod              instance = new MclibMod();
    
    /** a network wrapper for mclib's channel. */
    private static SimpleNetworkWrapper NETWORK;
    
    /** true to activate tracing output */
    public static final boolean TRACE = false;
    
    /**
     * Initialization.
     * 
     * @param event
     */
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        this.mc = Minecraft.getMinecraft();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new MclibGuiHandler());
        
        MinecraftForge.EVENT_BUS.register(clientProxy);
        NetworkRegistry.INSTANCE.registerGuiHandler(MclibMod.instance, new MclibGuiHandler());
        
        CommunicationEndpointId.CommunicationServiceCache.init(this);
        this.registerCommunicationEndpoint(MclibCommunication.ClientServerCore, new MclibCoreHandler());
        
        // sc = s[erver]c[client] (both directions)
        NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("mclib|sc"); //$NON-NLS-1$
        NETWORK.registerMessage(NetMessage.Handle.class, NetMessage.class, 0, Side.CLIENT);
    }
    
    /** the known endpoints. */
    private final Map<String, Map<String, CommunicationEndpointId>> endpoints = new HashMap<>();
    
    /** the known handlers. */
    private final Map<CommunicationEndpointId, ComHandler>          handlers  = new ConcurrentHashMap<>();
    
    /**
     * Registers an existing communication endpoint with given network handler.
     * 
     * @param id
     *            endpoint id.
     * @param handler
     *            handler.
     */
    public void registerCommunicationEndpoint(CommunicationEndpointId id, ComHandler handler)
    {
        synchronized (this.endpoints)
        {
            Map<String, CommunicationEndpointId> map = this.endpoints.get(id.getClass().getName());
            if (map == null)
            {
                map = new HashMap<>();
                this.endpoints.put(id.getClass().getName(), map);
            }
            if (map.containsKey(id))
            {
                throw new IllegalStateException("Duplicate registration of communication endpoint."); //$NON-NLS-1$
            }
            map.put(id.name(), id);
        }
        this.handlers.put(id, handler);
    }
    
    /**
     * Returns the endpoint for given class and element
     * 
     * @param clazz
     * @param name
     * @return endpoint or {@code null} if it does not exist.
     */
    public CommunicationEndpointId getEndpoint(String clazz, String name)
    {
        synchronized (this.endpoints)
        {
            final Map<String, CommunicationEndpointId> map = this.endpoints.get(clazz);
            if (map != null)
            {
                return map.get(name);
            }
        }
        return null;
    }
    
    /**
     * Returns the handler for given endpoint.
     * 
     * @param id
     * @return handler or {@code null} if no handler was registered.
     */
    public ComHandler getHandler(CommunicationEndpointId id)
    {
        return this.handlers.get(id);
    }
    
    @Override
    public void send(CommunicationEndpointId id, DataSection... data)
    {
        if (data != null)
        {
            for (final DataSection section : data)
            {
                final NetMessage msg = new NetMessage(id, section);
                NETWORK.sendToServer(msg);
            }
        }
    }
    
}
