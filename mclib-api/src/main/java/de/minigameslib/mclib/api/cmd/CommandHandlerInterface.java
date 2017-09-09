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

import java.util.List;

import de.minigameslib.mclib.api.McException;

/**
 * Interface for command handling.
 * 
 * @author mepeisen
 */
public interface CommandHandlerInterface
{
    
    /**
     * Handles given command.
     * 
     * @param command
     *            the command to handle.
     * 
     * @throws McException
     *             thrown if there are any problems during command handling.
     */
    void handle(CommandInterface command) throws McException;
    
    /**
     * On Tab complete handler.
     * 
     * @param command
     *            the command to handle.
     * @param lastArg
     *            the last argument to be completed.
     * @return list of possible command completions.
     * 
     * @throws McException
     *             thrown if there are any problems during command handling.
     */
    List<String> onTabComplete(CommandInterface command, String lastArg) throws McException;
    
}
