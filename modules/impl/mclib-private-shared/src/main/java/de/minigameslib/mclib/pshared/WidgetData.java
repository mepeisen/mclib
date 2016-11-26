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
     * Submit button
     */
    @PersistentField
    protected SubmitButton submit;
    
    /**
     * Cancel button
     */
    @PersistentField
    protected CancelButton cancel;
    
    /**
     * The text input
     */
    @PersistentField
    protected TextInput textInput;
    
    /**
     * The list input
     */
    @PersistentField
    protected ListInput listInput;
    
    /**
     * @return the label
     */
    public Label getLabel()
    {
        return this.label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(Label label)
    {
        this.label = label;
    }

    /**
     * @return the submit
     */
    public SubmitButton getSubmit()
    {
        return this.submit;
    }

    /**
     * @param submit the submit to set
     */
    public void setSubmit(SubmitButton submit)
    {
        this.submit = submit;
    }

    /**
     * @return the cancel
     */
    public CancelButton getCancel()
    {
        return this.cancel;
    }

    /**
     * @param cancel the cancel to set
     */
    public void setCancel(CancelButton cancel)
    {
        this.cancel = cancel;
    }

    /**
     * @return the textInput
     */
    public TextInput getTextInput()
    {
        return this.textInput;
    }

    /**
     * @param textInput the textInput to set
     */
    public void setTextInput(TextInput textInput)
    {
        this.textInput = textInput;
    }
    
    /**
     * @return the listInput
     */
    public ListInput getListInput()
    {
        return this.listInput;
    }

    /**
     * @param listInput the listInput to set
     */
    public void setListInput(ListInput listInput)
    {
        this.listInput = listInput;
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
         * @return the dataId
         */
        public String getDataId()
        {
            return this.dataId;
        }

        /**
         * @param dataId the dataId to set
         */
        public void setDataId(String dataId)
        {
            this.dataId = dataId;
        }

        /**
         * @return the columns
         */
        public List<ListColumn> getColumns()
        {
            return this.columns;
        }

        /**
         * @return the buttons
         */
        public List<ListButton> getButtons()
        {
            return this.buttons;
        }
        
    }
    
    /**
     * A list column
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
         * @return the dataKey
         */
        public String getDataKey()
        {
            return this.dataKey;
        }

        /**
         * @param dataKey the dataKey to set
         */
        public void setDataKey(String dataKey)
        {
            this.dataKey = dataKey;
        }

        /**
         * @return the formKey
         */
        public String getFormKey()
        {
            return this.formKey;
        }

        /**
         * @param formKey the formKey to set
         */
        public void setFormKey(String formKey)
        {
            this.formKey = formKey;
        }

        /**
         * @return the visible
         */
        public boolean isVisible()
        {
            return this.visible;
        }

        /**
         * @param visible the visible to set
         */
        public void setVisible(boolean visible)
        {
            this.visible = visible;
        }

        /**
         * @return the label
         */
        public String getLabel()
        {
            return this.label;
        }

        /**
         * @param label the label to set
         */
        public void setLabel(String label)
        {
            this.label = label;
        }
        
    }
    
    /**
     * A list button
     */
    public static class ListButton extends ButtonData
    {
        
        /** {@code true} if the button needs a selected list entry. */
        @PersistentField
        protected boolean needsInput;

        /**
         * @return the needsInput
         */
        public boolean isNeedsInput()
        {
            return this.needsInput;
        }

        /**
         * @param needsInput the needsInput to set
         */
        public void setNeedsInput(boolean needsInput)
        {
            this.needsInput = needsInput;
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
         * initial value
         */
        @PersistentField
        protected String value;
        
        /**
         * {@code true} if empty values are allowed.
         */
        @PersistentField
        protected boolean allowsEmpty;
        
        /**
         * the form key
         */
        @PersistentField
        protected String formKey;

        /**
         * @return the label
         */
        public String getLabel()
        {
            return this.label;
        }

        /**
         * @param label the label to set
         */
        public void setLabel(String label)
        {
            this.label = label;
        }

        /**
         * @return the value
         */
        public String getValue()
        {
            return this.value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(String value)
        {
            this.value = value;
        }

        /**
         * @return the allowsEmpty
         */
        public boolean isAllowsEmpty()
        {
            return this.allowsEmpty;
        }

        /**
         * @param allowsEmpty the allowsEmpty to set
         */
        public void setAllowsEmpty(boolean allowsEmpty)
        {
            this.allowsEmpty = allowsEmpty;
        }

        /**
         * @return the formKey
         */
        public String getFormKey()
        {
            return this.formKey;
        }

        /**
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
         * @return the span
         */
        public int getSpan()
        {
            return this.span;
        }

        /**
         * @param span the span to set
         */
        public void setSpan(int span)
        {
            this.span = span;
        }

        /**
         * @return the text
         */
        public String getText()
        {
            return this.text;
        }

        /**
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
     * A cancel button
     */
    public static class CancelButton extends ButtonData
    {
        
        // empt
        
    }

}
