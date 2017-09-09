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

package de.minigameslib.mclib.impl.cmd;

import java.io.Serializable;
import java.util.Locale;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.cmd.AbstractPagableCommandHandler;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;

/**
 * Command implementation for "/mclib mainlocale list".
 * 
 * @author mepeisen
 *
 */
public class MainLocaleListCommand extends AbstractPagableCommandHandler implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.checkOpPermission(MclibCommand.CommandPermissions.MainLocale);
    }
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        command.checkOpPermission(MclibCommand.CommandPermissions.MainLocale);
        super.handle(command);
    }
    
    @Override
    public LocalizedMessageInterface getShortDescription(CommandInterface command)
    {
        return Messages.ShortDescription;
    }
    
    @Override
    public LocalizedMessageInterface getDescription(CommandInterface command)
    {
        return Messages.Description;
    }
    
    @Override
    protected int getLineCount(CommandInterface command)
    {
        return McLibInterface.instance().getMainLocales().size();
    }
    
    @Override
    protected Serializable getHeader(CommandInterface command)
    {
        return Messages.Header;
    }
    
    @Override
    protected Serializable[] getLines(CommandInterface command, int start, int count)
    {
        return McLibInterface.instance().getMainLocales().stream().map(Locale::toString).skip(start).limit(count).toArray(Serializable[]::new);
    }
    
    /**
     * Messages for /mclib mainlocale list.
     */
    @LocalizedMessages("cmd.mclib_mainlocale_list")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description.
         */
        @LocalizedMessage(defaultMessage = "Display list of main locales!")
        @MessageComment("Short description for /mclib mainlocale")
        ShortDescription,
        
        /**
         * Description.
         */
        @LocalizedMessageList({
            "Display list of main locales!"
        })
        @MessageComment("Long description for /mclib mainlocale")
        Description,
        
        /**
         * Page header.
         */
        @LocalizedMessage(defaultMessage = "main locales")
        @MessageComment("Page header")
        Header
    }
    
}
