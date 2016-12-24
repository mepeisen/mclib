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

package de.minigameslib.mclib.pshared.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.minigameslib.mclib.pshared.WidgetData;
import de.minigameslib.mclib.pshared.WidgetData.CancelButton;
import de.minigameslib.mclib.pshared.WidgetData.ComboInput;
import de.minigameslib.mclib.pshared.WidgetData.ComboValue;
import de.minigameslib.mclib.pshared.WidgetData.Label;
import de.minigameslib.mclib.pshared.WidgetData.ListButton;
import de.minigameslib.mclib.pshared.WidgetData.ListColumn;
import de.minigameslib.mclib.pshared.WidgetData.ListInput;
import de.minigameslib.mclib.pshared.WidgetData.ListRequest;
import de.minigameslib.mclib.pshared.WidgetData.ListResponse;
import de.minigameslib.mclib.pshared.WidgetData.ListResponseRow;
import de.minigameslib.mclib.pshared.WidgetData.SubmitButton;
import de.minigameslib.mclib.pshared.WidgetData.TextInput;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Test case for {@link WidgetData}
 * 
 * @author mepeisen
 *
 */
public class WidgetDataTest
{
    
    /**
     * Simple test case for reading/storing data
     */
    @Test
    public void testLabel()
    {
        final WidgetData data = new WidgetData();
        final Label label = new Label();
        data.setLabel(label);
        label.setSpan(2);
        label.setText("label"); //$NON-NLS-1$
        assertEquals("label", data.getLabel().getText()); //$NON-NLS-1$
        assertEquals(2, data.getLabel().getSpan());

        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        
        final WidgetData data2 = section.getFragment(WidgetData.class, "FOO"); //$NON-NLS-1$
        assertEquals("label", data2.getLabel().getText()); //$NON-NLS-1$
        assertEquals(2, data2.getLabel().getSpan());
    }
    
    /**
     * Simple test case for reading/storing data
     */
    @Test
    public void testSubmit()
    {
        final WidgetData data = new WidgetData();
        final SubmitButton submit = new SubmitButton();
        data.setSubmit(submit);
        submit.setActionId("actionId"); //$NON-NLS-1$
        submit.setCloseAction(true);
        submit.setHasActionListener(true);
        submit.setLabel("label"); //$NON-NLS-1$
        assertEquals("actionId", data.getSubmit().getActionId()); //$NON-NLS-1$
        assertTrue(data.getSubmit().isCloseAction());
        assertTrue(data.getSubmit().isHasActionListener());
        assertEquals("label", data.getSubmit().getLabel()); //$NON-NLS-1$

        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        
        final WidgetData data2 = section.getFragment(WidgetData.class, "FOO"); //$NON-NLS-1$
        assertEquals("actionId", data2.getSubmit().getActionId()); //$NON-NLS-1$
        assertTrue(data2.getSubmit().isCloseAction());
        assertTrue(data2.getSubmit().isHasActionListener());
        assertEquals("label", data2.getSubmit().getLabel()); //$NON-NLS-1$
    }
    
    /**
     * Simple test case for reading/storing data
     */
    @Test
    public void testCancel()
    {
        final WidgetData data = new WidgetData();
        final CancelButton cancel = new CancelButton();
        data.setCancel(cancel);
        cancel.setActionId("actionId"); //$NON-NLS-1$
        cancel.setCloseAction(true);
        cancel.setHasActionListener(true);
        cancel.setLabel("label"); //$NON-NLS-1$
        assertEquals("actionId", data.getCancel().getActionId()); //$NON-NLS-1$
        assertTrue(data.getCancel().isCloseAction());
        assertTrue(data.getCancel().isHasActionListener());
        assertEquals("label", data.getCancel().getLabel()); //$NON-NLS-1$

        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        
        final WidgetData data2 = section.getFragment(WidgetData.class, "FOO"); //$NON-NLS-1$
        assertEquals("actionId", data2.getCancel().getActionId()); //$NON-NLS-1$
        assertTrue(data2.getCancel().isCloseAction());
        assertTrue(data2.getCancel().isHasActionListener());
        assertEquals("label", data2.getCancel().getLabel()); //$NON-NLS-1$
    }
    
    /**
     * Simple test case for reading/storing data
     */
    @Test
    public void testTextInput()
    {
        final WidgetData data = new WidgetData();
        final TextInput text = new TextInput();
        data.setTextInput(text);
        text.setAllowsEmpty(true);
        text.setFormKey("formkey"); //$NON-NLS-1$
        text.setLabel("label"); //$NON-NLS-1$
        text.setValue("value"); //$NON-NLS-1$
        assertTrue(data.getTextInput().isAllowsEmpty());
        assertEquals("formkey", data.getTextInput().getFormKey()); //$NON-NLS-1$
        assertEquals("label", data.getTextInput().getLabel()); //$NON-NLS-1$
        assertEquals("value", data.getTextInput().getValue()); //$NON-NLS-1$

        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        
        final WidgetData data2 = section.getFragment(WidgetData.class, "FOO"); //$NON-NLS-1$
        assertTrue(data2.getTextInput().isAllowsEmpty());
        assertEquals("formkey", data2.getTextInput().getFormKey()); //$NON-NLS-1$
        assertEquals("label", data2.getTextInput().getLabel()); //$NON-NLS-1$
        assertEquals("value", data2.getTextInput().getValue()); //$NON-NLS-1$
    }
    
    /**
     * Simple test case for reading/storing data
     */
    @Test
    public void testComboInput()
    {
        final WidgetData data = new WidgetData();
        final ComboInput combo = new ComboInput();
        data.setComboInput(combo);
        combo.setAllowsNull(true);
        combo.setAllowsNewValues(true);
        combo.setFormKey("formkey"); //$NON-NLS-1$
        combo.setIdKey("idkey"); //$NON-NLS-1$
        combo.setLabel("label"); //$NON-NLS-1$
        combo.setLabelKey("labelkey"); //$NON-NLS-1$
        combo.setNformKey("nformkey"); //$NON-NLS-1$
        combo.setValue("value"); //$NON-NLS-1$
        final ComboValue value = new ComboValue();
        final MemoryDataSection combodata = new MemoryDataSection();
        combodata.set("foo", "bar"); //$NON-NLS-1$ //$NON-NLS-2$
        value.setData(combodata);
        combo.getValues().add(value);
        
        assertTrue(data.getComboInput().isAllowsNewValues());
        assertTrue(data.getComboInput().isAllowsNull());
        assertEquals("formkey", data.getComboInput().getFormKey()); //$NON-NLS-1$
        assertEquals("idkey", data.getComboInput().getIdKey()); //$NON-NLS-1$
        assertEquals("label", data.getComboInput().getLabel()); //$NON-NLS-1$
        assertEquals("labelkey", data.getComboInput().getLabelKey()); //$NON-NLS-1$
        assertEquals("nformkey", data.getComboInput().getNformKey()); //$NON-NLS-1$
        assertEquals("value", data.getComboInput().getValue()); //$NON-NLS-1$
        assertEquals(1, data.getComboInput().getValues().size());
        assertEquals("bar", data.getComboInput().getValues().get(0).getData().get("foo")); //$NON-NLS-1$ //$NON-NLS-2$

        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        
        final WidgetData data2 = section.getFragment(WidgetData.class, "FOO"); //$NON-NLS-1$
        
        assertTrue(data2.getComboInput().isAllowsNewValues());
        assertTrue(data2.getComboInput().isAllowsNull());
        assertEquals("formkey", data2.getComboInput().getFormKey()); //$NON-NLS-1$
        assertEquals("idkey", data2.getComboInput().getIdKey()); //$NON-NLS-1$
        assertEquals("label", data2.getComboInput().getLabel()); //$NON-NLS-1$
        assertEquals("labelkey", data2.getComboInput().getLabelKey()); //$NON-NLS-1$
        assertEquals("nformkey", data2.getComboInput().getNformKey()); //$NON-NLS-1$
        assertEquals("value", data2.getComboInput().getValue()); //$NON-NLS-1$
        assertEquals(1, data2.getComboInput().getValues().size());
        assertEquals("bar", data2.getComboInput().getValues().get(0).getData().get("foo")); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Simple test case for reading/storing data
     */
    @Test
    public void testListInput()
    {
        final WidgetData data = new WidgetData();
        final ListInput list = new ListInput();
        data.setListInput(list);
        list.setDataId("dataid"); //$NON-NLS-1$
        final ListButton button = new ListButton();
        button.setLabel("label"); //$NON-NLS-1$
        button.setNeedsInput(true);
        button.setCloseAction(true);
        button.setActionId("actionid"); //$NON-NLS-1$
        button.setHasActionListener(true);
        list.getButtons().add(button);
        final ListColumn column = new ListColumn();
        column.setDataKey("datakey"); //$NON-NLS-1$
        column.setFormKey("formkey"); //$NON-NLS-1$
        column.setLabel("column"); //$NON-NLS-1$
        column.setVisible(true);
        list.getColumns().add(column);
        
        assertEquals("dataid", data.getListInput().getDataId()); //$NON-NLS-1$
        assertEquals(1, data.getListInput().getButtons().size());
        assertEquals("label", data.getListInput().getButtons().get(0).getLabel()); //$NON-NLS-1$
        assertEquals("actionid", data.getListInput().getButtons().get(0).getActionId()); //$NON-NLS-1$
        assertTrue(data.getListInput().getButtons().get(0).isNeedsInput());
        assertTrue(data.getListInput().getButtons().get(0).isCloseAction());
        assertTrue(data.getListInput().getButtons().get(0).isHasActionListener());
        assertEquals(1, data.getListInput().getColumns().size());
        assertEquals("datakey", data.getListInput().getColumns().get(0).getDataKey()); //$NON-NLS-1$
        assertEquals("formkey", data.getListInput().getColumns().get(0).getFormKey()); //$NON-NLS-1$
        assertEquals("column", data.getListInput().getColumns().get(0).getLabel()); //$NON-NLS-1$
        assertTrue(data.getListInput().getColumns().get(0).isVisible());

        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        
        final WidgetData data2 = section.getFragment(WidgetData.class, "FOO"); //$NON-NLS-1$
        
        assertEquals("dataid", data2.getListInput().getDataId()); //$NON-NLS-1$
        assertEquals(1, data2.getListInput().getButtons().size());
        assertEquals("label", data2.getListInput().getButtons().get(0).getLabel()); //$NON-NLS-1$
        assertEquals("actionid", data2.getListInput().getButtons().get(0).getActionId()); //$NON-NLS-1$
        assertTrue(data2.getListInput().getButtons().get(0).isNeedsInput());
        assertTrue(data2.getListInput().getButtons().get(0).isCloseAction());
        assertTrue(data2.getListInput().getButtons().get(0).isHasActionListener());
        assertEquals(1, data2.getListInput().getColumns().size());
        assertEquals("datakey", data2.getListInput().getColumns().get(0).getDataKey()); //$NON-NLS-1$
        assertEquals("formkey", data2.getListInput().getColumns().get(0).getFormKey()); //$NON-NLS-1$
        assertEquals("column", data2.getListInput().getColumns().get(0).getLabel()); //$NON-NLS-1$
        assertTrue(data2.getListInput().getColumns().get(0).isVisible());
    }
    
    /**
     * Simple test case for reading/storing data
     */
    @Test
    public void testListRequest()
    {
        final ListRequest data = new ListRequest();
        data.setLimit(1);
        data.setStart(2);
        assertEquals(1, data.getLimit());
        assertEquals(2, data.getStart());

        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        
        final ListRequest data2 = section.getFragment(ListRequest.class, "FOO"); //$NON-NLS-1$
        assertEquals(1, data2.getLimit());
        assertEquals(2, data2.getStart());
    }
    
    /**
     * Simple test case for reading/storing data
     */
    @Test
    public void testListResponse()
    {
        final ListResponse data = new ListResponse();
        data.setCount(1);
        final ListResponseRow row = new ListResponseRow();
        row.getData().set("foo",  "bar"); //$NON-NLS-1$ //$NON-NLS-2$
        data.getRows().add(row);
        assertEquals(1, data.getCount());
        assertEquals(1, data.getRows().size());
        assertEquals("bar", data.getRows().get(0).getData().get("foo")); //$NON-NLS-1$ //$NON-NLS-2$

        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        
        final ListResponse data2 = section.getFragment(ListResponse.class, "FOO"); //$NON-NLS-1$
        assertEquals(1, data2.getCount());
        assertEquals(1, data2.getRows().size());
        assertEquals("bar", data2.getRows().get(0).getData().get("foo")); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
}
