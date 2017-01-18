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

package de.minigameslib.mclib.api.cmd;

import java.io.Serializable;
import java.util.Arrays;

import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;

/**
 * Helper to display a multi line localized message (f.e. a manual) as paable chat command.
 * 
 * @author mepeisen
 */
public class LocalizedPagableCommand extends AbstractPagableCommandHandler
{
    
    /** the message lines. */
    private LocalizedMessageInterface localizedMessage;
    
    /** the pagable header line. */
    private Serializable header;
    
    /**
     * @param localizedMessage
     * @param header
     */
    public LocalizedPagableCommand(LocalizedMessageInterface localizedMessage, Serializable header)
    {
        this.localizedMessage = localizedMessage;
        this.header = header;
    }
    
    /**
     * gets lines from message.
     * @param cmd
     * @return lines.
     */
    private Serializable[] toLines(CommandInterface cmd)
    {
        if (cmd.isPlayer())
        {
            return cmd.getPlayer().encodeMessage(this.localizedMessage);
        }
        return cmd.isOp() ?
                this.localizedMessage.toAdminMessageLine(McLibInterface.instance().getDefaultLocale()) :
                    this.localizedMessage.toUserMessageLine(McLibInterface.instance().getDefaultLocale());
    }

    @Override
    protected int getLineCount(CommandInterface command)
    {
        return this.toLines(command).length;
    }
    
    @Override
    protected Serializable getHeader(CommandInterface command)
    {
        return this.header;
    }
    
    @Override
    protected Serializable[] getLines(CommandInterface command, int start, int count)
    {
        return Arrays.stream(toLines(command)).skip(start).limit(count).toArray(Serializable[]::new);
    }
    
}
