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

package de.minigames.mclib.nms.v19;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.craftbukkit.v1_9_R1.util.CraftChatMessage;

import de.minigameslib.mclib.nms.api.ChatSystemInterface;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.IChatBaseComponent.ChatSerializer;

/**
 * Implementation of chat system.
 * 
 * @author mepeisen
 */
public class ChatSystem1_9 implements ChatSystemInterface
{

    @Override
    public String toJson(String src)
    {
        final IChatBaseComponent[] components = CraftChatMessage.fromString(src, true);
        return Arrays.asList(components).stream().map(c -> ChatSerializer.a(c)).collect(Collectors.joining(", ", "{ \"text\": \"\", \"extra\":[ ", " ] }")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
}
