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

package de.minigameslib.mclib.pshared;

import java.util.ArrayList;
import java.util.List;

import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * Content widgets of resizable windows.
 * 
 * @author mepeisen
 */
public class WidgetData extends AnnotatedDataFragment
{
    
    /**
     * A label widget.
     */
    @PersistentField
    protected Label label;
    
    /**
     * Submit button.
     */
    @PersistentField
    protected SubmitButton submit;
    
    /**
     * Cancel button.
     */
    @PersistentField
    protected CancelButton cancel;
    
    /**
     * The text input.
     */
    @PersistentField
    protected TextInput textInput;
    
    /**
     * The list input.
     */
    @PersistentField
    protected ListInput listInput;
    
    /**
     * The combobox input.
     */
    @PersistentField
    protected ComboInput comboInput;
    
    /**
     * Returns the label widget.
     * @return the label
     */
    public Label getLabel()
    {
        return this.label;
    }

    /**
     * Sets the label widget.
     * @param label the label to set
     */
    public void setLabel(Label label)
    {
        this.label = label;
    }

    /**
     * Returns the submit button widget.
     * @return the submit
     */
    public SubmitButton getSubmit()
    {
        return this.submit;
    }

    /**
     * Sets the submit button widget.
     * @param submit the submit to set
     */
    public void setSubmit(SubmitButton submit)
    {
        this.submit = submit;
    }

    /**
     * Returns the cancel button widget.
     * @return the cancel
     */
    public CancelButton getCancel()
    {
        return this.cancel;
    }

    /**
     * Sets the cnacel button widget.
     * @param cancel the cancel to set
     */
    public void setCancel(CancelButton cancel)
    {
        this.cancel = cancel;
    }

    /**
     * Returns the text input widget.
     * @return the textInput
     */
    public TextInput getTextInput()
    {
        return this.textInput;
    }

    /**
     * Sets the text input widget.
     * @param textInput the textInput to set
     */
    public void setTextInput(TextInput textInput)
    {
        this.textInput = textInput;
    }
    
    /**
     * Returns the list input widget.
     * @return the listInput
     */
    public ListInput getListInput()
    {
        return this.listInput;
    }

    /**
     * Sets the list input widget.
     * @param listInput the listInput to set
     */
    public void setListInput(ListInput listInput)
    {
        this.listInput = listInput;
    }
    
    /**
     * Returns the combo input widget.
     * @return the comboInput
     */
    public ComboInput getComboInput()
    {
        return this.comboInput;
    }

    /**
     * Sets the combo input widget.
     * @param comboInput the comboInput to set
     */
    public void setComboInput(ComboInput comboInput)
    {
        this.comboInput = comboInput;
    }

    /**
     * A combo box input.
     */
    public static class ComboInput extends AnnotatedDataFragment
    {
        
        /**
         * Allows empty values.
         */
        @PersistentField
        protected boolean allowsNull;
        
        /**
         * Allows new values.
         */
        @PersistentField
        protected boolean allowsNewValues;
        
        /**
         * Data key to query the label from data entry.
         */
        @PersistentField
        protected String labelKey;
        
        /**
         * ID key of the selected data entry.
         */
        @PersistentField
        protected String idKey;
        
        /**
         * Form key of the selected data entry.
         */
        @PersistentField
        protected String formKey;
        
        /**
         * Form key for new values.
         */
        @PersistentField
        protected String nformKey;
     
        /**
         * label of text input.
         */
        @PersistentField
        protected String label;
        
        /**
         * initial value.
         */
        @PersistentField
        protected String value;
        
        /**
         * the possible combobox values.
         */
        @PersistentField
        protected List<ComboValue> values = new ArrayList<>();

        /**
         * Returns the flag to allow nulls/ empty values.
         * @return the allowsNull
         */
        public boolean isAllowsNull()
        {
            return this.allowsNull;
        }

        /**
         * Sets the flag to allow nulls/ empty values.
         * @param allowsNull the allowsNull to set
         */
        public void setAllowsNull(boolean allowsNull)
        {
            this.allowsNull = allowsNull;
        }

        /**
         * Returns the flag to allow new values.
         * @return the allowsNewValues
         */
        public boolean isAllowsNewValues()
        {
            return this.allowsNewValues;
        }

        /**
         * Sets the flag to allow new values.
         * @param allowsNewValues the allowsNewValues to set
         */
        public void setAllowsNewValues(boolean allowsNewValues)
        {
            this.allowsNewValues = allowsNewValues;
        }

        /**
         * Returns the data key for labels.
         * @return the labelKey
         */
        public String getLabelKey()
        {
            return this.labelKey;
        }

        /**
         * Sets the data key for labels.
         * @param labelKey the labelKey to set
         */
        public void setLabelKey(String labelKey)
        {
            this.labelKey = labelKey;
        }

        /**
         * Returns the data key for ids.
         * @return the idKey
         */
        public String getIdKey()
        {
            return this.idKey;
        }

        /**
         * Sets the data key for ids.
         * @param idKey the idKey to set
         */
        public void setIdKey(String idKey)
        {
            this.idKey = idKey;
        }

        /**
         * Sets the data key for identifying new values.
         * @return the nformKey
         */
        public String getNformKey()
        {
            return this.nformKey;
        }

        /**
         * Gets the data key for identifying new values.
         * @param nformKey the nformKey to set
         */
        public void setNformKey(String nformKey)
        {
            this.nformKey = nformKey;
        }

        /**
         * Returns the data key for values.
         * @return the formKey
         */
        public String getFormKey()
        {
            return this.formKey;
        }

        /**
         * Sets the data keys for values.
         * @param formKey the formKey to set
         */
        public void setFormKey(String formKey)
        {
            this.formKey = formKey;
        }

        /**
         * Returns the label text.
         * @return the label
         */
        public String getLabel()
        {
            return this.label;
        }

        /**
         * Sets the label text.
         * @param label the label to set
         */
        public void setLabel(String label)
        {
            this.label = label;
        }

        /**
         * Returns the value.
         * @return the value
         */
        public String getValue()
        {
            return this.value;
        }

        /**
         * Sets the value.
         * @param value the value to set
         */
        public void setValue(String value)
        {
            this.value = value;
        }

        /**
         * Returns the list of possible values.
         * @return the values
         */
        public List<ComboValue> getValues()
        {
            return this.values;
        }
        
    }
    
    /**
     * Combobox value.
     */
    public static class ComboValue extends AnnotatedDataFragment
    {
        /** row data. */
        @PersistentField
        protected DataSection data = new MemoryDataSection();

        /**
         * Combo box data.
         * @return the data
         */
        public DataSection getData()
        {
            return this.data;
        }

        /**
         * Combo box data.
         * @param data the data to set
         */
        public void setData(DataSection data)
        {
            this.data = data;
        }
    }

    /**
     * A list form input.
     */
    public static class ListInput extends AnnotatedDataFragment
    {
        
        /**
         * list columns.
         */
        @PersistentField
        protected List<ListColumn> columns = new ArrayList<>();
        
        /**
         * internal id to query data.
         */
        @PersistentField
        protected String dataId;
        
        /**
         * The list buttons.
         */
        @PersistentField
        protected List<ListButton> buttons = new ArrayList<>();

        /**
         * Returns the data id.
         * @return the dataId
         */
        public String getDataId()
        {
            return this.dataId;
        }

        /**
         * Sets the data id.
         * @param dataId the dataId to set
         */
        public void setDataId(String dataId)
        {
            this.dataId = dataId;
        }

        /**
         * Returns the column definitions.
         * @return the columns
         */
        public List<ListColumn> getColumns()
        {
            return this.columns;
        }

        /**
         * Returns the list buttons.
         * @return the buttons
         */
        public List<ListButton> getButtons()
        {
            return this.buttons;
        }
        
    }
    
    /**
     * A list column.
     */
    public static class ListColumn extends AnnotatedDataFragment
    {
        
        /**
         * Data key to query data from data entry.
         */
        @PersistentField
        protected String dataKey;
        
        /**
         * Form key if this column represents the primary key of the data entries.
         */
        @PersistentField
        protected String formKey;
        
        /**
         * visible flag.
         */
        @PersistentField
        protected boolean visible;
        
        /**
         * column label.
         */
        @PersistentField
        protected String label;

        /**
         * Returns the data key.
         * @return the dataKey
         */
        public String getDataKey()
        {
            return this.dataKey;
        }

        /**
         * Sets the data key.
         * @param dataKey the dataKey to set
         */
        public void setDataKey(String dataKey)
        {
            this.dataKey = dataKey;
        }

        /**
         * Returns the form key.
         * @return the formKey
         */
        public String getFormKey()
        {
            return this.formKey;
        }

        /**
         * Sets the form key.
         * @param formKey the formKey to set
         */
        public void setFormKey(String formKey)
        {
            this.formKey = formKey;
        }

        /**
         * Returns the visible flag.
         * @return the visible
         */
        public boolean isVisible()
        {
            return this.visible;
        }

        /**
         * Sets the visible flag.
         * @param visible the visible to set
         */
        public void setVisible(boolean visible)
        {
            this.visible = visible;
        }

        /**
         * Returns the label key.
         * @return the label
         */
        public String getLabel()
        {
            return this.label;
        }

        /**
         * Sets the label key.
         * @param label the label to set
         */
        public void setLabel(String label)
        {
            this.label = label;
        }
        
    }
    
    /**
     * A list button.
     */
    public static class ListButton extends ButtonData
    {
        
        /**
         * {@code true} if the button needs a selected list entry.
         */
        @PersistentField
        protected boolean needsInput;

        /**
         * Returns the needs input flag.
         * @return the needsInput
         */
        public boolean isNeedsInput()
        {
            return this.needsInput;
        }

        /**
         * Sets the needs input flag.
         * @param needsInput the needsInput to set
         */
        public void setNeedsInput(boolean needsInput)
        {
            this.needsInput = needsInput;
        }
        
    }
    
    /**
     * Request for list data.
     */
    public static class ListRequest extends AnnotatedDataFragment
    {
        /** start index. */
        @PersistentField
        protected int start;
        
        /** row limit. */
        @PersistentField
        protected int limit;

        /**
         * Returns the start index.
         * @return the start
         */
        public int getStart()
        {
            return this.start;
        }

        /**
         * Sets the start index.
         * @param start the start to set
         */
        public void setStart(int start)
        {
            this.start = start;
        }

        /**
         * Returns the row limit.
         * @return the limit
         */
        public int getLimit()
        {
            return this.limit;
        }

        /**
         * Sets the row limit.
         * @param limit the limit to set
         */
        public void setLimit(int limit)
        {
            this.limit = limit;
        }
    }
    
    /**
     * Response for list data.
     */
    public static class ListResponse extends AnnotatedDataFragment
    {
        /** total count. */
        @PersistentField
        protected int count;
        
        /** row data. */
        @PersistentField
        protected List<ListResponseRow> rows = new ArrayList<>();

        /**
         * Returns the count.
         * @return the count
         */
        public int getCount()
        {
            return this.count;
        }

        /**
         * Sets the count.
         * @param count the count to set
         */
        public void setCount(int count)
        {
            this.count = count;
        }

        /**
         * Returns the data rows.
         * @return the rows
         */
        public List<ListResponseRow> getRows()
        {
            return this.rows;
        }
    }
    
    /**
     * Single list row.
     */
    public static class ListResponseRow extends AnnotatedDataFragment
    {
        /** row data. */
        @PersistentField
        protected DataSection data = new MemoryDataSection();

        /**
         * Returns the row data.
         * @return the data
         */
        public DataSection getData()
        {
            return this.data;
        }
    }
    

    /**
     * A text input.
     */
    public static class TextInput extends AnnotatedDataFragment
    {
     
        /**
         * label of text input.
         */
        @PersistentField
        protected String label;
        
        /**
         * initial value.
         */
        @PersistentField
        protected String value;
        
        /**
         * {@code true} if empty values are allowed.
         */
        @PersistentField
        protected boolean allowsEmpty;
        
        /**
         * the form key.
         */
        @PersistentField
        protected String formKey;

        /**
         * Returns the label.
         * @return the label
         */
        public String getLabel()
        {
            return this.label;
        }

        /**
         * Sets the label.
         * @param label the label to set
         */
        public void setLabel(String label)
        {
            this.label = label;
        }

        /**
         * Returns the value.
         * @return the value
         */
        public String getValue()
        {
            return this.value;
        }

        /**
         * Sets the value.
         * @param value the value to set
         */
        public void setValue(String value)
        {
            this.value = value;
        }

        /**
         * returns the flag to allow empty values.
         * @return the allowsEmpty
         */
        public boolean isAllowsEmpty()
        {
            return this.allowsEmpty;
        }

        /**
         * Sets the flag to allow empty values.
         * @param allowsEmpty the allowsEmpty to set
         */
        public void setAllowsEmpty(boolean allowsEmpty)
        {
            this.allowsEmpty = allowsEmpty;
        }

        /**
         * Returns the data key for values.
         * @return the formKey
         */
        public String getFormKey()
        {
            return this.formKey;
        }

        /**
         * Sets the data key for values.
         * @param formKey the formKey to set
         */
        public void setFormKey(String formKey)
        {
            this.formKey = formKey;
        }
        
        
        
    }
    
    /**
     * A label widget.
     */
    public static class Label extends AnnotatedDataFragment
    {
        
        /**
         * Column span of label.
         */
        @PersistentField
        protected int span;
        
        /**
         * Label label.
         */
        @PersistentField
        protected String text;

        /**
         * Returns the column span.
         * @return the span
         */
        public int getSpan()
        {
            return this.span;
        }

        /**
         * Sets the column span.
         * @param span the span to set
         */
        public void setSpan(int span)
        {
            this.span = span;
        }

        /**
         * Returns the text.
         * @return the text
         */
        public String getText()
        {
            return this.text;
        }

        /**
         * Sets the text.
         * @param text the text to set
         */
        public void setText(String text)
        {
            this.text = text;
        }
        
    }
    
    /**
     * A submit button.
     */
    public static class SubmitButton extends ButtonData
    {
        
        // empty
        
    }
    
    /**
     * A cancel button.
     */
    public static class CancelButton extends ButtonData
    {
        
        // empt
        
    }

}
