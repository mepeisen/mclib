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

package de.minigameslib.mclib.client.impl.gui;

import de.matthiasmann.twl.TreeTable;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.AbstractTreeTableModel;
import de.matthiasmann.twl.model.AbstractTreeTableNode;
import de.matthiasmann.twl.model.TreeTableModel;
import de.matthiasmann.twl.model.TreeTableNode;
import de.minigameslib.mclib.client.impl.gui.widgets.ResizableWin;

public class ImSampleGui2 extends TwlScreen
{

	public ImSampleGui2()
	{
		super(createMain());
	}

	/**
	 * @return
	 */
	private static Widget createMain() {
		final TreeTable t = new TreeTable(createModel());
		t.setTheme("/table");
        t.registerCellRenderer(SpanString.class, new SpanRenderer());
        // t.registerCellRenderer(StringModel.class, new EditFieldCellRenderer());
        t.setDefaultSelectionManager();
        
		return new ResizableWin("MINIGAMES", t, true, true);
	}

	private static TreeTableModel createModel() {
		MyModel m = new MyModel();
		MyNode a = m.insert("A", "1");
        a.insert("Aa", "2");
        a.insert("Ab", "3");
        MyNode ac = a.insert("Ac", "4");
        ac.insert("Ac1", "Hello");
        ac.insert("Ac2", "World");
        ac.insert("EditField", "FOOFOO");
        a.insert("Ad", "5");
        MyNode b = m.insert("B", "6");
        b.insert("Ba", "7");
        b.insert("Bb", "8");
        b.insert("Bc", "9");
        /*dynamicNode =*/ b.insert("Dynamic", "stuff");
        m.insert(new SpanString("This is a very long string which will span into the next column.", 2), "Not visible");
        m.insert("This is a very long string which will be clipped.", "This is visible");
		return m;
	}

    static class SpanString {
        private final String str;
        private final int span;

        public SpanString(String str, int span) {
            this.str = str;
            this.span = span;
        }

        @Override
        public String toString() {
            return str;
        }
    }
    
    static class SpanRenderer extends TreeTable.StringCellRenderer {
        int span;

        @Override
        public void setCellData(int row, int column, Object data) {
            super.setCellData(row, column, data);
            span = ((SpanString)data).span;
        }

        @Override
        public int getColumnSpan() {
            return span;
        }
    }

    static class MyNode extends AbstractTreeTableNode {
        private Object str0;
        private Object str1;

        public MyNode(TreeTableNode parent, Object str0, Object str1) {
            super(parent);
            this.str0 = str0;
            this.str1 = str1;
            setLeaf(true);
        }

        public Object getData(int column) {
            return (column == 0) ? str0 : str1;
        }

        public MyNode insert(Object str0, Object str1) {
            MyNode n = new MyNode(this, str0, str1);
            insertChild(n, getNumChildren());
            setLeaf(false);
            return n;
        }

        public void remove(int idx) {
            removeChild(idx);
        }

        public void removeAll() {
            removeAllChildren();
        }
    }

    static class MyModel extends AbstractTreeTableModel {
        private static final String[] COLUMN_NAMES = {"Left", "Right"};
        public int getNumColumns() {
            return 2;
        }
        public String getColumnHeaderText(int column) {
            return COLUMN_NAMES[column];
        }
        public MyNode insert(Object str0, String str1) {
            MyNode n = new MyNode(this, str0, str1);
            insertChild(n, getNumChildren());
            return n;
        }
    }

}
