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
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * An interface to produce form displayed on the client.
 * 
 * @author mepeisen
 */
public interface SGuiFormBuilderInterface
{
    
    /**
     * Adds a common text to display.
     * 
     * @param span
     *            1 (only label column) or 2 (whole dialog size)
     * @param text
     * @param args
     * @return this object for chaining
     */
    SGuiFormBuilderInterface addText(int span, LocalizedMessageInterface text, Serializable... args);
    
    /**
     * Adds a submit button
     * 
     * @param label
     * @param labelArgs
     * @param action
     *            an action to parse the form data; can throw exceptions to indicate form errors.
     * @return this object for chaining
     */
    SGuiFormBuilderInterface addSubmitButton(LocalizedMessageInterface label, Serializable[] labelArgs, McConsumer<DataSection> action);
    
    /**
     * Adds a cancel button
     * 
     * @param label
     * @param labelArgs
     * @param action
     * @return this object for chaining
     */
    SGuiFormBuilderInterface addCancelButton(LocalizedMessageInterface label, Serializable[] labelArgs, McConsumer<DataSection> action);
    
    /**
     * Adds text input field.
     * 
     * @param label
     * @param labelArgs
     * @param formKey
     * @param initialValue
     * @param allowsEmpty
     * @return this object for chaining
     */
    SGuiFormBuilderInterface addTextInput(LocalizedMessageInterface label, Serializable[] labelArgs, String formKey, String initialValue, boolean allowsEmpty);
    
    // SGuiFormBuilderInterface addComboInput(LocalizedMessageInterface label, Serializable[] labelArgs, String formKey, McSupplier<Iterable<SGuiComboValue>> values, String initialValue, boolean
    // allowsNull, boolean allowsUserDefined);
    
    /**
     * Displays the form on client.
     * 
     * @return gui interface
     */
    SGuiInterface display();
    
}
