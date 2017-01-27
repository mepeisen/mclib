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

package de.minigameslib.mclib.api.perms;

import de.minigameslib.mclib.shared.api.com.EnumerationValue;

/**
 * An interface for enumerations that represent list of permissions.
 * 
 * @author mepeisen
 */
public interface PermissionsInterface extends EnumerationValue
{
    
    /**
     * Returns the full permission name.
     * 
     * @return full permission name.
     */
    default String fullPath()
    {
        try
        {
            final Permissions permissions = this.getClass().getAnnotation(Permissions.class);
            final Permission perm = this.getClass().getDeclaredField(((Enum<?>) this).name()).getAnnotation(Permission.class);
            if (permissions == null || perm == null)
            {
                throw new IllegalStateException("Invalid permission class."); //$NON-NLS-1$
            }
            return permissions.value() + '.' + (perm.value().length() == 0 ? ((Enum<?>) this).name() : perm.value());
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the resolved permission name.
     * 
     * @return resolved permission name.
     */
    default String resolveName()
    {
        final String srcName = this.fullPath();
        return PermissionServiceInterface.instance().resolveContextVar(srcName);
    }
    
}
