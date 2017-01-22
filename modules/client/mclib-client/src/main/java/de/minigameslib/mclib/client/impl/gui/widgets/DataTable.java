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
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Table;
import de.matthiasmann.twl.TableRowSelectionManager;
import de.matthiasmann.twl.model.AbstractTableModel;
import de.matthiasmann.twl.model.TableSingleSelectionModel;
import de.minigameslib.mclib.pshared.CoreMessages;
import de.minigameslib.mclib.pshared.FormData;
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

    /** window id for data queries. */
    private String winId;

    /** start button. */
    private Button startBtn;

    /** prev button. */
    private Button prevBtn;

    /** start page input field. */
    private EditField startPage;

    /** page count label. */
    private Label pageCount;

    /** end index label. */
    private Label endIx;

    /** start index label. */
    private Label startIx;

    /** next button. */
    private Button nextBtn;

    /** end button. */
    private Button endBtn;
    
    /** current page number requested by query. */
    private int curPage;
    
    /** total count of entries in list. */
    private int totalCount;
    
    /** list limit. */
    private static final int LIMIT = 50;
    
    /**
     * Constructor
     * @param columns 
     * @param winId
     * @param inputId 
     */
    public DataTable(List<ListColumn> columns, String winId, String inputId)
    {
        // TODO Support sorting and filtering
        // TODO Support load errors (through sending error to this widget)
        // TODO lock during load and use timeouts
        this.setTheme("datatable"); //$NON-NLS-1$
        this.columnDefinitions = columns;
        this.inputId = inputId;
        this.winId = winId;
        this.visibleColumns = this.columnDefinitions.stream().filter(ListColumn::isVisible).collect(Collectors.toList());
        this.model = new MyTableModel();
        this.table = new Table(this.model);
        this.table.setTheme("/table"); //$NON-NLS-1$
        this.table.setSelectionManager(new TableRowSelectionManager(new TableSingleSelectionModel()));
        this.addRow("table").add(this.table); //$NON-NLS-1$
        this.startBtn = new Button("<<"); //$NON-NLS-1$
        this.prevBtn = new Button("<"); //$NON-NLS-1$
        this.startPage = new EditField();
        this.pageCount = new Label();
        this.endIx = new Label();
        this.startIx = new Label();
        this.nextBtn = new Button(">"); //$NON-NLS-1$
        this.endBtn = new Button(">>"); //$NON-NLS-1$
        this.addRow("startbtn", "prevpage", "startpage", "l0", "pagecount", "l1", "startix", "l2", "endix", "l4", "nextpage", "endpage") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$
            .add(this.startBtn).add(this.prevBtn)
            .add(this.startPage).add(new Label("/")).add(this.pageCount) //$NON-NLS-1$
            .add(new Label(" (#")).add(this.startIx).add(new Label(" - #")).add(this.endIx).add(new Label(")")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            .add(this.nextBtn).add(this.endBtn);
        
        this.startBtn.addCallback(() -> this.queryRows(1));
        this.prevBtn.addCallback(() -> this.queryRows(this.curPage <= 1 ? 1 : this.curPage - 1));
        this.nextBtn.addCallback(() -> this.queryRows(this.curPage * LIMIT >= this.totalCount ? this.curPage : this.curPage + 1));
        this.endBtn.addCallback(() -> this.queryRows(this.totalCount / LIMIT));
        this.startPage.addCallback(key -> {
            if (key == Event.KEY_RETURN)
            {
                try
                {
                    final int page = Integer.valueOf(this.startPage.getText());
                    if (page >= 1 && (page * LIMIT) < this.totalCount)
                    {
                        this.queryRows(page);
                    }
                    else
                    {
                        this.queryRows(1);
                    }
                }
                catch (@SuppressWarnings("unused") NumberFormatException ex)
                {
                    this.queryRows(1);
                }
            }
        });
    }
    
    /**
     * Queries given rows from server.
     * @param page the page, starting at 1.
     */
    public void queryRows(int page)
    {
        this.curPage = page;
        
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
        
        this.totalCount = response.getCount();
        this.startPage.setText(String.valueOf(this.curPage));
        this.startIx.setText(String.valueOf(1 + (this.curPage - 1) * LIMIT));
        this.endIx.setText(String.valueOf(this.curPage * LIMIT));
        this.pageCount.setText(String.valueOf(this.totalCount / LIMIT));
    }

    @Override
    public FormData[] getFormData()
    {
        final List<FormData> result = new ArrayList<>();

        final int row = this.table.getSelectionManager().getSelectionModel().getFirstSelected();
        if (row != TableSingleSelectionModel.NO_SELECTION)
        {
            final DataSection rowdata = this.data.get(row);
            for (final ListColumn column : this.columnDefinitions)
            {
                if (column.getFormKey() != null)
                {
                    final FormData fd = new FormData();
                    fd.setKey(column.getFormKey());
                    fd.setValue(rowdata.getString(column.getDataKey()));
                    result.add(fd);
                    break;
                }
            }
        }
        
        return result.toArray(new FormData[result.size()]);
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
            final String dataKey = DataTable.this.visibleColumns.get(paramInt2).getDataKey();
            final Object result = section.get(dataKey);
            if (result instanceof String) return result;
            // TODO timestamp support etc.
            return result == null ? null : result.toString();
        }

        @Override
        public void fireAllChanged()
        {
            super.fireAllChanged();
        }
        
    }

    /**
     * @return table selection model.
     */
    public TableSingleSelectionModel getSelectionModel()
    {
        return (TableSingleSelectionModel) this.table.getSelectionManager().getSelectionModel();
    }
    
}
