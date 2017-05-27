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

package de.minigameslib.mclib.impl.gui;

import java.io.Serializable;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.GuiServiceInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.impl.gui.cfg.QueryText;

/**
 * The common gui service.
 * 
 * @author mepeisen
 */
public class GuiServiceImpl implements GuiServiceInterface
{
    
    @Override
    public GuiSessionInterface openTextEditor(McPlayerInterface player, String src, McRunnable onCancel, McConsumer<String> onInput, boolean preUseInput, LocalizedMessageInterface description,
        Serializable... descriptionArgs) throws McException
    {
        final String[] desc = player.encodeMessage(description, descriptionArgs);
        final QueryText gui = new QueryText(src, onCancel, onInput, desc, preUseInput);
        return player.openAnvilGui(gui);
    }
    
    @Override
    public GuiSessionInterface nestTextEditor(McPlayerInterface player, String src, McRunnable onCancel, McConsumer<String> onInput, boolean preUseInput, LocalizedMessageInterface description,
        Serializable... descriptionArgs) throws McException
    {
        final String[] desc = player.encodeMessage(description, descriptionArgs);
        final QueryText gui = new QueryText(src, onCancel, onInput, desc, preUseInput);
        return player.nestAnvilGui(gui);
    }
    
}
