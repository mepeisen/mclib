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

package de.minigameslib.mclib.test.impl;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.minigameslib.mclib.api.enums.EnumServiceInterface;

/**
 * @author mepeisen
 *
 */
public class MclibTestPlugin extends JavaPlugin
{

    @Override
    public void onEnable()
    {
        EnumServiceInterface.instance().registerEnumClass(this, GuiIds.class); // test
    }

    @Override
    public void onDisable()
    {
        EnumServiceInterface.instance().unregisterAll(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (command.getName().equals("mclibt")) //$NON-NLS-1$
        {
            MyCommandHandler.onCommand(sender, command, label, args);
        }
        return super.onCommand(sender, command, label, args);
    }
    
}
