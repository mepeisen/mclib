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

import java.io.Serializable;

import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.util.function.McBiConsumer;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * A builder to create list controls.
 * 
 * @author mepeisen
 */
public interface SGuiListBuilderInterface
{
    
    /**
     * Adds a visible list column.
     * 
     * @param dataKey
     *            the data key to fetch the column data from list data entry
     * @param formKey
     *            the form key or {@code null} if this column is not the primary key to identify rows
     * @param label
     *            the text label
     * @param args
     *            the label text arguments
     * @return this object for chaining
     */
    SGuiListBuilderInterface addColumn(String dataKey, String formKey, LocalizedMessageInterface label, Serializable... args);
    
    /**
     * Adds an invisible list column.
     * 
     * @param dataKey
     *            the data key to fetch the column data from list data entry
     * @param formKey
     *            the form key or {@code null} if this column is not the primary key to identify rows
     * @return this object for chaining
     */
    SGuiListBuilderInterface addColumn(String dataKey, String formKey);
    
    /**
     * Adds a submit button that requires a selected list entry.
     * 
     * @param label
     *            button label
     * @param labelArgs
     *            arguments for button label
     * @param action
     *            the action to perform
     * @return this object for chaining
     */
    SGuiListBuilderInterface addSubmitButton(LocalizedMessageInterface label, Serializable[] labelArgs, McBiConsumer<SGuiInterface, DataSection> action);
    
    /**
     * Adds a button that is independent from list selection.
     * 
     * @param label
     *            button label
     * @param labelArgs
     *            arguments for button label
     * @param action
     *            the action to perform
     * @return this object for chaining
     */
    SGuiListBuilderInterface addButton(LocalizedMessageInterface label, Serializable[] labelArgs, McBiConsumer<SGuiInterface, DataSection> action);
    
}
