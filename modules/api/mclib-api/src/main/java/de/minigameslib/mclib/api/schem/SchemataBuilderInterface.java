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

package de.minigameslib.mclib.api.schem;

import java.util.Locale;

import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.ObjectIdInterface;
import de.minigameslib.mclib.api.objects.SignIdInterface;
import de.minigameslib.mclib.api.objects.ZoneIdInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;

/**
 * A builder to create schemata.
 * 
 * @author mepeisen
 *
 */
public interface SchemataBuilderInterface extends SchemataInterface
{
    
    /**
     * Sets the title of this schemata.
     * 
     * @param locale
     *            locale for the title.
     * @param title
     *            title string for given locale.
     * @return this object for chaining
     */
    SchemataBuilderInterface setTitle(Locale locale, String title);
    
    /**
     * Sets the authors name.
     * 
     * @param author
     *            author name.
     * @return this object for chaining
     */
    SchemataBuilderInterface setAuthor(String author);
    
    /**
     * Sets the unique schemata url.
     * 
     * @param url
     *            schemata url.
     * @return this object for chaining
     */
    SchemataBuilderInterface setSchemataUrl(String url);
    
    /**
     * Sets a schemata version to identify newer schemas of unique schemata url.
     * 
     * @param version
     *            schemata version
     * @return this object for chaining
     */
    SchemataBuilderInterface setSchemataVersion(int version);
    
    /**
     * Adds a new schemata part (asynchronous).
     * 
     * @param name
     *            part name
     * @param cuboid
     *            the cuboid for blocks and objects.
     * @param success
     *            function to invoke on success
     * @param failure
     *            function to invoke on failure
     * @param options
     *            the options to use for building the part
     * @return this object for chaining
     */
    SchemataBuilderInterface addPart(String name, Cuboid cuboid, McRunnable success, McRunnable failure, PartBuildOptions... options);
    
    /**
     * Adds a new schemata part (asynchronous).
     * 
     * @param name
     *            part name
     * @param zone
     *            the zone to be added (cuboid from zone)
     * @param success
     *            function to invoke on success
     * @param failure
     *            function to invoke on failure
     * @param options
     *            the options to use for building the part
     * @return this object for chaining
     */
    SchemataBuilderInterface addPart(String name, ZoneIdInterface zone, McRunnable success, McRunnable failure, PartBuildOptions... options);
    
    /**
     * Adds a new schemata part (asynchronous).
     * 
     * @param name
     *            part name
     * @param component
     *            the component to be added (single block part)
     * @param success
     *            function to invoke on success
     * @param failure
     *            function to invoke on failure
     * @param options
     *            the options to use for building the part
     * @return this object for chaining
     */
    SchemataBuilderInterface addPart(String name, ComponentIdInterface component, McRunnable success, McRunnable failure, PartBuildOptions... options);
    
    /**
     * Adds a new schemata part (asynchronous).
     * 
     * @param name
     *            part name
     * @param sign
     *            the sign to be added (single block part)
     * @param success
     *            function to invoke on success
     * @param failure
     *            function to invoke on failure
     * @param options
     *            the options to use for building the part
     * @return this object for chaining
     */
    SchemataBuilderInterface addPart(String name, SignIdInterface sign, McRunnable success, McRunnable failure, PartBuildOptions... options);
    
    /**
     * Adds a new object to schemata (asynchronous).
     * 
     * @param objectId
     *            object id to be added
     * @param success
     *            function to invoke on success
     * @param failure
     *            function to invoke on failure
     * @return this object for chaining
     */
    SchemataBuilderInterface addObject(ObjectIdInterface objectId, McRunnable success, McRunnable failure);
    
    /**
     * Part building options.
     */
    public enum PartBuildOptions
    {
        /** including objects from ObjectServiceInterface inside the given cuboid. */
        WithObjects,
        
        /** including air blocks so that they replace existing blocks with air. */
        WithAir
    }
    
}
