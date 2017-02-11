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

import de.minigameslib.mclib.api.cmd.AbstractCompositeCommandHandler;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.enums.ChildEnum;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;

/**
 * @author mepeisen
 *
 */
public class MainLocaleCommand extends AbstractCompositeCommandHandler implements SubCommandHandlerInterface
{
    
    /**
     * Constructor
     */
    public MainLocaleCommand()
    {
        this.subCommands.put("add", new MainLocaleAddCommand()); //$NON-NLS-1$
        this.subCommands.put("remove", new MainLocaleRemoveCommand()); //$NON-NLS-1$
        this.subCommands.put("list", new MainLocaleListCommand()); //$NON-NLS-1$
    }

    @Override
    public boolean visible(CommandInterface command)
    {
        return command.checkOpPermission(MclibCommand.CommandPermissions.MainLocale);
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
    protected void sendUsage(CommandInterface command)
    {
        command.send(Messages.Usage);
    }
    
    /**
     * Messages
     */
    @LocalizedMessages("cmd.mclib_mainlocale")
    @ChildEnum({
        MainLocaleListCommand.Messages.class,
        MainLocaleAddCommand.Messages.class,
        MainLocaleRemoveCommand.Messages.class
    })
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Command usage
         */
        @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "Enter " + LocalizedMessage.BLUE + "/mclib mainlocale help " + LocalizedMessage.GRAY + "for detailed help!")
        @MessageComment("Command usage for /mclib mainlocale")
        Usage,
        
        /**
         * Short description
         */
        @LocalizedMessage(defaultMessage = "Display or change your main locales!")
        @MessageComment("Short description for /mclib mainlocale")
        ShortDescription,
        
        /**
         * Description
         */
        @LocalizedMessageList({
            "Display or change your main locales!",
            "The main locales are those languages your server supports.",
            "The users may even use a different language but they get a warning if it is not contained in your main locale list.",
            "Usage: " + LocalizedMessageList.CODE_COLOR + "/mclib mainlocale list " + LocalizedMessageList.INFORMATION_COLOR + "to query your main locales!",
            "Usage: " + LocalizedMessageList.CODE_COLOR + "/mclib mainlocale add [iso_code] " + LocalizedMessageList.INFORMATION_COLOR + "to add a main locale!",
            "Usage: " + LocalizedMessageList.CODE_COLOR + "/mclib mainlocale remove [iso_code] " + LocalizedMessageList.INFORMATION_COLOR + "to remove a main locale!",
            "You can use any iso 639 code, for example \"en\" or \"de\"."
        })
        @MessageComment("Long description for /mclib mainlocale")
        Description,
    }
    
}
