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

package de.minigameslib.mclib.impl.player;

import java.util.Locale;

import org.bukkit.configuration.ConfigurationSection;

import de.minigameslib.mclib.api.config.Configurable;

/**
 * Players persistent data.
 * 
 * @author mepeisen
 */
public class MclibPlayersConfig implements Configurable
{
    
    /** the preferred locale. */
    private Locale preferredLocale = Locale.ENGLISH;
    
    // TODO refactor with abstract base class reading fields with annotations.

    /**
     * @return the preferredLocale
     */
    public Locale getPreferredLocale()
    {
        return this.preferredLocale;
    }

    /**
     * @param preferredLocale the preferredLocale to set
     */
    public void setPreferredLocale(Locale preferredLocale)
    {
        this.preferredLocale = preferredLocale;
    }

    @Override
    public void readFromConfig(ConfigurationSection section)
    {
        this.preferredLocale = new Locale(section.getString("PreferredLocale", "en")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public void writeToConfig(ConfigurationSection section)
    {
        section.set("PreferredLocale", this.preferredLocale.getLanguage()); //$NON-NLS-1$
    }
    
}
