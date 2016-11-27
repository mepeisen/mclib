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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ColumnLayout;
import de.matthiasmann.twl.Table;
import de.matthiasmann.twl.TableRowSelectionManager;
import de.matthiasmann.twl.model.AbstractTableModel;
import de.matthiasmann.twl.model.TableSingleSelectionModel;
import de.minigameslib.mclib.pshared.CoreMessages;
import de.minigameslib.mclib.pshared.MclibCommunication;
import de.minigameslib.mclib.pshared.QueryFormRequestData;
import de.minigameslib.mclib.pshared.WidgetData.ListColumn;
import de.minigameslib.mclib.pshared.WidgetData.ListRequest;
import de.minigameslib.mclib.pshared.WidgetData.ListResponse;
import de.minigameslib.mclib.pshared.WidgetData.ListResponseRow;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * A data list widget.
 * 
 * @author mepeisen
 *
 */
public class DataTable extends ColumnLayout implements FormFieldInterface, FormQueryWidgetInterface
{

    /** table. */
    private Table      table;
    
    /** the data. */
    List<DataSection> data         = new ArrayList<>();
    
    /** the data model. */
    private MyTableModel model;

    /**
     * the associated column definitions.
     */
    private List<ListColumn> columnDefinitions;

    /**
     * the associated column definitions (only visible columns).
     */
    List<ListColumn> visibleColumns;

    /** the input id to query the data. */
    private String inputId;

    /** the form key */
    private String formKey;

    /** window id for data queries. */
    private String winId;
    
    /** list limit. */
    private static final int LIMIT = 50;
    
    /**
     * Constructor
     * @param columns 
     * @param winId
     * @param inputId 
     * @param formKey 
     */
    public DataTable(List<ListColumn> columns, String winId, String inputId, String formKey)
    {
        // TODO Support sorting and filtering
        // TODO Support load errors (through sending error to this widget)
        // TODO lock during load and use timeouts
        this.setTheme("datatable"); //$NON-NLS-1$
        this.columnDefinitions = columns;
        this.inputId = inputId;
        this.formKey = formKey;
        this.winId = winId;
        this.visibleColumns.stream().filter(ListColumn::isVisible).collect(Collectors.toList());
        this.model = new MyTableModel();
        this.table = new Table(this.model);
        this.table.setSelectionManager(new TableRowSelectionManager(new TableSingleSelectionModel()));
        this.addRow("table").add(this.table); //$NON-NLS-1$
    }
    
    /**
     * Queries given rows from server.
     * @param page the page, starting at 1.
     */
    public void queryRows(int page)
    {
        // send query to server.
        final QueryFormRequestData request = new QueryFormRequestData();
        request.setWinId(this.winId);
        request.setInputId(this.inputId);
        final ListRequest lrq = new ListRequest();
        lrq.setLimit(LIMIT);
        lrq.setStart((page - 1) * LIMIT);
        lrq.write(request.getData());
        final DataSection section = new MemoryDataSection();
        section.set("KEY", CoreMessages.QueryFormRequest.name()); //$NON-NLS-1$
        request.write(section.createSection("data")); //$NON-NLS-1$
        MclibCommunication.ClientServerCore.send(section);
    }
    
    /**
     * Sets the dialog buttons.
     * @param buttons
     */
    public void setButtons(List<Button> buttons)
    {
        final List<String> cols = new ArrayList<>();
        for (int i = 0; i < buttons.size(); i++)
        {
            cols.add("button" + i); //$NON-NLS-1$
        }
        final Row row = this.addRow(cols.toArray(new String[cols.size()]));
        for (final Button button : buttons)
        {
            row.add(button);
        }
    }

    @Override
    public String getInputId()
    {
        return this.inputId;
    }

    @Override
    public void parseFormQuery(DataSection section)
    {
        // TODO display data count and support paging.
        this.data.clear();
        final ListResponse response = new ListResponse();
        response.read(section);
        for (final ListResponseRow row : response.getRows())
        {
            this.data.add(row.getData());
        }
        this.model.fireAllChanged();
    }

    @Override
    public String getFormKey()
    {
        return this.formKey;
    }

    @Override
    public String getFormValue()
    {
        final int row = this.table.getSelectionManager().getSelectionModel().getFirstSelected();
        if (row != TableSingleSelectionModel.NO_SELECTION)
        {
            final DataSection rowdata = this.data.get(row);
            for (final ListColumn column : this.columnDefinitions)
            {
                if (column.getFormKey() != null)
                {
                    return rowdata.getString(column.getFormKey());
                }
            }
        }
        return null;
    }
    
    /**
     * @author mepeisen
     *
     */
    private final class MyTableModel extends AbstractTableModel
    {
        /**
         * Constructor
         */
        public MyTableModel()
        {
            // empty
        }

        @Override
        public int getNumColumns()
        {
            return DataTable.this.visibleColumns.size();
        }
        
        @Override
        public String getColumnHeaderText(int paramInt)
        {
            return DataTable.this.visibleColumns.get(paramInt).getLabel();
        }
        
        @Override
        public int getNumRows()
        {
            return DataTable.this.data.size();
        }
        
        @Override
        public Object getCell(int paramInt1, int paramInt2)
        {
            final DataSection section = DataTable.this.data.get(paramInt1);
            return section.getString(DataTable.this.visibleColumns.get(paramInt2).getDataKey());
        }

        @Override
        public void fireAllChanged()
        {
            super.fireAllChanged();
        }
        
    }
    
}
