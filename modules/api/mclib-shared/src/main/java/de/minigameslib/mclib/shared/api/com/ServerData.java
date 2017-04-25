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

package de.minigameslib.mclib.shared.api.com;

/**
 * Server fragment.
 * 
 * @author mepeisen
 */
public class ServerData implements ServerDataFragment
{

    /**
     * the server name.
     */
    private String name;

    /**
     * Constructor to read from file.
     */
    public ServerData()
    {
        // empty
    }

    /**
     * Constructor with new data.
     * @param name server name
     */
    public ServerData(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public void read(DataSection section)
    {
        this.name = section.getString("name"); //$NON-NLS-1$
    }

    @Override
    public void write(DataSection section)
    {
        section.set("name",  this.name); //$NON-NLS-1$
    }

    @Override
    public boolean test(DataSection section)
    {
        return section.isString("name"); //$NON-NLS-1$
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        ServerData other = (ServerData) obj;
        if (this.name == null)
        {
            if (other.name != null)
            {
                return false;
            }
        }
        else if (!this.name.equals(other.name))
        {
            return false;
        }
        return true;
    }
    
}
