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

import java.io.Serializable;

/**
 * An interface used to identify communication endpoints.
 * 
 * <p>
 * Inherit this interface in your enumeration classes.
 * </p>
 * 
 * @author mepeisen
 */
public interface CommunicationEndpointId extends Serializable
{
    
    /**
     * Enumeration value name.
     * 
     * @return enumeration value name.
     */
    String name();
    
    /**
     * Sends given data to the other side.
     * 
     * <p>
     * Clients will send the data to the server and servers will send it to clients. A proper handler will receive the data and handle it.
     * </p>
     * 
     * @param data
     *            the data to be sent
     * 
     * @throws IllegalStateException
     *             thrown on communication errors.
     */
    default void send(DataSection... data)
    {
        CommunicationServiceInterface.instance().send(this, data);
    }
    
    /**
     * Helper interface to send the actual data.
     * 
     * @author mepeisen
     */
    interface CommunicationServiceInterface
    {
        
        /**
         * Returns the communication services instance.
         * 
         * @return communication services instance.
         */
        static CommunicationServiceInterface instance()
        {
            return CommunicationServiceCache.get();
        }
        
        /**
         * Sends given data to the other side of the communication endpoint.
         * 
         * @param id
         *            endpoint id to send a message to
         * @param data
         *            message data to be sent
         */
        void send(CommunicationEndpointId id, DataSection... data);
        
    }
    
    /**
     * Cache helper.
     */
    class CommunicationServiceCache
    {
        
        /** the communication services. */
        private static CommunicationServiceInterface SERVICES;
        
        /**
         * Returns the communication services instance.
         * 
         * @return communication services.
         */
        static CommunicationServiceInterface get()
        {
            return SERVICES;
        }
        
        /**
         * Initializes the cache.
         * 
         * @param service
         *            the communication services implementation
         */
        public static void init(CommunicationServiceInterface service)
        {
            if (SERVICES == null)
            {
                SERVICES = service;
            }
        }
        
    }
    
}
