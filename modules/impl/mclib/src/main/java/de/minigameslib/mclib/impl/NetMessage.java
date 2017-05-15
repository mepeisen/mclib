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

package de.minigameslib.mclib.impl;

import java.util.function.BiFunction;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import de.minigameslib.mclib.shared.api.com.CommunicationEndpointId;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * Helper class for network messages.
 * 
 * @author mepeisen
 */
class NetMessage
{
    
    // Implementation hint: This class contains duplicate code from corresponding NetMessage class in mclib client forge mod
    
    /** the communication endpoint. */
    private CommunicationEndpointId endpoint;
    
    /** the data section. */
    private DataSection             data;
    
    /**
     * Constructor.
     */
    public NetMessage()
    {
        // empty
    }
    
    /**
     * Constructor.
     * 
     * @param endpoint
     *            the endpoint to be used.
     * @param data
     *            the data to be used
     */
    public NetMessage(CommunicationEndpointId endpoint, DataSection data)
    {
        this.endpoint = endpoint;
        this.data = data;
    }
    
    /**
     * Returns the endpoint.
     * 
     * @return the endpoint
     */
    public CommunicationEndpointId getEndpoint()
    {
        return this.endpoint;
    }
    
    /**
     * Sets the endpoint.
     * 
     * @param endpoint
     *            the endpoint to set
     */
    public void setEndpoint(CommunicationEndpointId endpoint)
    {
        this.endpoint = endpoint;
    }
    
    /**
     * Returns the data.
     * 
     * @return the data
     */
    public DataSection getData()
    {
        return this.data;
    }
    
    /**
     * Sets the data.
     * 
     * @param data
     *            the data to set
     */
    public void setData(DataSection data)
    {
        this.data = data;
    }
    
    /**
     * Reads message from given input.
     * 
     * @param buf
     *            byte buffer
     * @param endpointFactory
     *            a function to receive endpoints by string
     */
    public void fromBytes(ByteArrayDataInput buf, BiFunction<String, String, CommunicationEndpointId> endpointFactory)
    {
        final String endpointclass = DataSectionTools.readUtf8(buf);
        final String endpointName = DataSectionTools.readUtf8(buf);
        this.endpoint = endpointFactory.apply(endpointclass, endpointName);
        
        this.data = DataSectionTools.read(buf);
    }
    
    /**
     * Converts this message to byte stream.
     * 
     * @param buf byte buffer
     */
    public void toBytes(ByteArrayDataOutput buf)
    {
        final String endpointclass = this.endpoint.getClass().getName();
        final String endpointName = this.endpoint.name();
        DataSectionTools.writeUtf8(buf, endpointclass);
        DataSectionTools.writeUtf8(buf, endpointName);
        
        DataSectionTools.toBytes(buf, this.data);
    }
    
}
