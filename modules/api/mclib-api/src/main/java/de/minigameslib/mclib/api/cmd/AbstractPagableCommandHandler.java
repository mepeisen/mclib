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
import java.util.Collections;
import java.util.List;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;

/**
 * A handler for pageable command output.
 * 
 * @author mepeisen
 */
public abstract class AbstractPagableCommandHandler implements CommandHandlerInterface
{
    
    /**
     * Counts the lines this command will return.
     * 
     * @param command
     *            command to proceed
     * @return number of returned lines.
     */
    protected abstract int getLineCount(CommandInterface command);
    
    /**
     * Returns the localized header line
     * 
     * @param command
     *            command to proceed
     * @return A header line, for example "help for command /mg2"; use the toArgs method of the localizedMessage.
     */
    protected abstract Serializable getHeader(CommandInterface command);
    
    /**
     * Returns the lines
     * 
     * @param command
     *            command to proceed
     * @param start
     *            starting index (first line starts with 0)
     * @param count
     *            number of lines to be returned.
     * @return lines
     */
    protected abstract Serializable[] getLines(CommandInterface command, int start, int count);
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        int start = 0;
        int page = 1;
        int pageLimit = 10;
        int lineCount = this.getLineCount(command);
        int pageCount = (int) Math.ceil((double)lineCount / pageLimit);
        if (pageCount == 0)
        {
            pageCount++;
        }
        
        if (command.getArgs().length > 1)
        {
            sendUsage(command);
            return;
        }
        
        if (command.getArgs().length == 1)
        {
            try
            {
                page = Integer.parseInt(command.getArgs()[0]);
                if (page < 1)
                {
                    sendWrongPage(command, page, pageCount);
                    return;
                }
                if (page > pageCount && page != 1)
                {
                    sendWrongPage(command, page, pageCount);
                    return;
                }
                start = (page - 1) * pageLimit;
            }
            catch (@SuppressWarnings("unused") NumberFormatException ex)
            {
                sendNumberFormatError(command);
                sendUsage(command);
                return;
            }
        }
        
        command.send(CommonMessages.PagedHeader, this.getHeader(command), page, pageCount);
        int i = start + 1;
        for (Serializable line : this.getLines(command, start, pageLimit))
        {
            command.send(CommonMessages.PagedLine, line, i);
            i++;
        }
    }
    
    /**
     * Sends a wrong page number error.
     * 
     * @param command
     *            the command
     * @param page
     *            the page number that was given by arguments
     * @param pageCount
     *            the total page count.
     */
    private void sendWrongPage(CommandInterface command, int page, int pageCount)
    {
        command.send(CommonMessages.PagedWrongPageNum, page, pageCount);
    }
    
    /**
     * Sends a number format error.
     * 
     * @param command
     *            the command
     */
    private void sendNumberFormatError(CommandInterface command)
    {
        command.send(CommonMessages.PagedInvalidNumber);
    }
    
    /**
     * sends a usage info.
     * 
     * @param command
     *            the command.
     */
    private void sendUsage(CommandInterface command)
    {
        command.send(CommonMessages.PageUsage, command.getCommandPath());
    }

    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws McException
    {
        return Collections.emptyList();
    }
    
}
