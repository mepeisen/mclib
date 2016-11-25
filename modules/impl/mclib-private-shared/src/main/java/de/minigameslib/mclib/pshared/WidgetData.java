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
    private Label label;
    
    /**
     * Submit button
     */
    @PersistentField
    private SubmitButton submit;
    
    /**
     * Cancel button
     */
    @PersistentField
    private CancelButton cancel;
    
    /**
     * The text input
     */
    @PersistentField
    private TextInput textInput;
    
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
     * A text input.
     */
    public static class TextInput extends AnnotatedDataFragment
    {
     
        /**
         * label of text input.
         */
        @PersistentField
        private String label;
        
        /**
         * initial value
         */
        @PersistentField
        private String value;
        
        /**
         * {@code true} if empty values are allowed.
         */
        @PersistentField
        private boolean allowsEmpty;
        
        /**
         * the form key
         */
        @PersistentField
        private String formKey;

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
        private int span;
        
        /**
         * Label label.
         */
        @PersistentField
        private String text;

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
