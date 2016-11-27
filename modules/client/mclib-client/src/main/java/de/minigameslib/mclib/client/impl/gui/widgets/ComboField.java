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

package de.minigameslib.mclib.client.impl.gui.widgets;

import java.util.List;

import de.matthiasmann.twl.ComboBox;
import de.matthiasmann.twl.model.SimpleListModel;
import de.minigameslib.mclib.pshared.FormData;
import de.minigameslib.mclib.pshared.WidgetData.ComboValue;

/**
 * Combofield widget.
 * 
 * @author mepeisen
 */
public class ComboField extends ComboBox<String> implements FormFieldInterface
{
    
    /** the form key. */
    private String formKey;
    
    /** the id key. */
    private String idKey;
    /** the values key. */
    String valueKey;
    /** the data values list. */
    List<ComboValue> values;

    /**
     * Constructor.
     * @param formKey 
     * @param idKey 
     * @param valueKey 
     * @param values 
     * @param initialValue 
     */
    public ComboField(String formKey, String idKey, String valueKey, List<ComboValue> values, String initialValue)
    {
        this.setTheme("combobox"); //$NON-NLS-1$
        this.formKey = formKey;
        this.idKey = idKey;
        this.valueKey = valueKey;
        this.values = values;
        
        this.setModel(new MyListModel());
        
        int i = 0;
        for (final ComboValue value : values)
        {
            i++;
            if (value.getData().get(idKey).equals(initialValue))
            {
                this.setSelected(i);
                break;
            }
        }
    }

    @Override
    public FormData[] getFormData()
    {
        final FormData data = new FormData();
        data.setKey(this.formKey);
        int selected = this.getSelected();
        if (selected != -1)
        {
            data.setValue(this.values.get(selected).getData().getString(this.idKey));
        }
        return new FormData[]{data};
    }
    
    /**
     * Inline list model.
     */
    private final class MyListModel extends SimpleListModel<String>
    {

        /**
         * Constructor
         */
        public MyListModel()
        {
            // empty
        }

        @Override
        public int getNumEntries()
        {
            return ComboField.this.values.size();
        }

        @Override
        public String getEntry(int paramInt)
        {
            return ComboField.this.values.get(paramInt).getData().getString(ComboField.this.valueKey);
        }
        
    }
    
}
