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

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;

/**
 * A common message box for various messages.
 * 
 * @author mepeisen
 */
public class MessageBox extends ResizableFrame
{
    
    /**
     * Constructor creating a message box.
     * @param title
     * @param message
     * @param buttons
     */
    public MessageBox(String title, String message, Button... buttons)
    {
        setTheme("/alertbox"); //$NON-NLS-1$
        setTitle(title);
        
        final SimpleTextAreaModel tam = new SimpleTextAreaModel();
        tam.setText(message);
        
        final TextArea scrolledWidget2 = new TextArea(tam);
        scrolledWidget2.setTheme("textarea"); //$NON-NLS-1$
        
        ScrollPane scrollPane2 = new ScrollPane(scrolledWidget2);
        scrollPane2.setTheme("scrollpane"); //$NON-NLS-1$
        scrollPane2.setFixed(ScrollPane.Fixed.HORIZONTAL);
        
        final DialogLayout layout = new DialogLayout();
        final Group groupH = layout.createSequentialGroup();
        groupH.addGap();
        
        final Group groupV = layout.createParallelGroup();
        
        for (final Button button : buttons)
        {
            groupH.addWidget(button);
            groupV.addWidget(button);
        }
        
        layout.setTheme("/dialoglayout"); //$NON-NLS-1$
        layout.setHorizontalGroup(layout.createParallelGroup().addWidget(scrollPane2).addGroup(groupH));
        layout.setVerticalGroup(layout.createSequentialGroup().addWidget(scrollPane2).addGroup(groupV));
        add(layout);
    }
    
}
