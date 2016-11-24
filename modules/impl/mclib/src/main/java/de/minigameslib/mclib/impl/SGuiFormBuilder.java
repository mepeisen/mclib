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

import java.io.Serializable;

import de.minigameslib.mclib.api.gui.SGuiFormBuilderInterface;
import de.minigameslib.mclib.api.gui.SGuiInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.impl.GuiSessionImpl.SGuiHelper;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * A form builder instance.
 * 
 * @author mepeisen
 */
public class SGuiFormBuilder implements SGuiFormBuilderInterface
{

    /** the smart gui. */
    private SGuiHelper smartGui;

    /**
     * Constructor
     * @param smartGui
     */
    public SGuiFormBuilder(SGuiHelper smartGui)
    {
        this.smartGui = smartGui;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.gui.SGuiFormBuilderInterface#addText(int, de.minigameslib.mclib.api.locale.LocalizedMessageInterface, java.io.Serializable[])
     */
    @Override
    public SGuiFormBuilderInterface addText(int span, LocalizedMessageInterface text, Serializable... args)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.gui.SGuiFormBuilderInterface#addSubmitButton(de.minigameslib.mclib.api.locale.LocalizedMessageInterface, java.io.Serializable[], de.minigameslib.mclib.api.util.function.McConsumer)
     */
    @Override
    public SGuiFormBuilderInterface addSubmitButton(LocalizedMessageInterface label, Serializable[] labelArgs, McConsumer<DataSection> action)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.gui.SGuiFormBuilderInterface#addCancelButton(de.minigameslib.mclib.api.locale.LocalizedMessageInterface, java.io.Serializable[], de.minigameslib.mclib.api.util.function.McRunnable)
     */
    @Override
    public SGuiFormBuilderInterface addCancelButton(LocalizedMessageInterface label, Serializable[] labelArgs, McRunnable action)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.gui.SGuiFormBuilderInterface#addTextInput(de.minigameslib.mclib.api.locale.LocalizedMessageInterface, java.io.Serializable[], java.lang.String, java.lang.String, boolean)
     */
    @Override
    public SGuiFormBuilderInterface addTextInput(LocalizedMessageInterface label, Serializable[] labelArgs, String formKey, String initialValue, boolean allowsEmpty)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.gui.SGuiFormBuilderInterface#display()
     */
    @Override
    public SGuiInterface display()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
