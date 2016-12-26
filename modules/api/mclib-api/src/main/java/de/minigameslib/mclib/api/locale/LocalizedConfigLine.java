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

package de.minigameslib.mclib.api.locale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.minigameslib.mclib.shared.api.com.DataFragment;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * A localized string placed within configuration files (not from messages.yml).
 * 
 * @author mepeisen
 */
public class LocalizedConfigLine implements DataFragment, LocalizedMessageInterface
{
    
    /**
     * serial version uid.
     */
    private static final long   serialVersionUID = -2121306517414707420L;
    
    /**
     * The default locale.
     */
    private Locale              defaultLocale    = Locale.ENGLISH;
    
    /**
     * The user strings per locale.
     */
    private Map<Locale, List<String>> userStrings      = new HashMap<>();
    
    /**
     * The admin strings per locale.
     */
    private Map<Locale, List<String>> adminStrings     = new HashMap<>();
    
    /**
     * Sets the user messages.
     * @param locale locale to use
     * @param message message array to be used.
     */
    public void setUserMessages(Locale locale, String[] message)
    {
        if (message == null)
        {
            this.userStrings.remove(locale);
        }
        else
        {
            final List<String> list = this.userStrings.computeIfAbsent(locale, (key) -> new ArrayList<>());
            list.clear();
            for (final String msg : message)
            {
                list.add(msg);
            }
        }
    }
    
    /**
     * Sets the admin messages.
     * @param locale locale to use
     * @param message message array to be used.
     */
    public void setAdminMessages(Locale locale, String[] message)
    {
        if (message == null)
        {
            this.adminStrings.remove(locale);
        }
        else
        {
            final List<String> list = this.adminStrings.computeIfAbsent(locale, (key) -> new ArrayList<>());
            list.clear();
            for (final String msg : message)
            {
                list.add(msg);
            }
        }
    }
    
    @Override
    public void read(DataSection section)
    {
        if (section.contains("default_locale")) //$NON-NLS-1$
        {
            this.defaultLocale = new Locale(section.getString("default_locale"), ""); //$NON-NLS-1$ //$NON-NLS-2$
        }
        this.userStrings.clear();
        if (section.contains("user")) //$NON-NLS-1$
        {
            for (final String key : section.getSection("user").getKeys(false)) //$NON-NLS-1$
            {
                final Locale locale = new Locale(key, ""); //$NON-NLS-1$
                this.userStrings.put(locale, section.getStringList("user." + key)); //$NON-NLS-1$
            }
        }
        this.adminStrings.clear();
        if (section.contains("admin")) //$NON-NLS-1$
        {
            for (final String key : section.getSection("admin").getKeys(false)) //$NON-NLS-1$
            {
                final Locale locale = new Locale(key, ""); //$NON-NLS-1$
                this.adminStrings.put(locale, section.getStringList("admin." + key)); //$NON-NLS-1$
            }
        }
    }
    
    @Override
    public void write(DataSection section)
    {
        section.set("default_locale", this.defaultLocale.toString()); //$NON-NLS-1$
        for (final Map.Entry<Locale, List<String>> userStr : this.userStrings.entrySet())
        {
            section.setPrimitiveList("user." + userStr.getKey().toString(), userStr.getValue()); //$NON-NLS-1$
        }
        for (final Map.Entry<Locale, List<String>> userStr : this.adminStrings.entrySet())
        {
            section.setPrimitiveList("admin." + userStr.getKey().toString(), userStr.getValue()); //$NON-NLS-1$
        }
    }
    
    @Override
    public boolean test(DataSection section)
    {
        return true;
    }
    
    @Override
    public boolean isSingleLine()
    {
        return false;
    }
    
    @Override
    public boolean isMultiLine()
    {
        return true;
    }
    
    @Override
    public MessageSeverityType getSeverity()
    {
        return MessageSeverityType.Information;
    }
    
    @Override
    public String toUserMessage(Locale locale, Serializable... args)
    {
        throw new IllegalStateException("Invalid message class."); //$NON-NLS-1$
    }
    
    @Override
    public String[] toUserMessageLine(Locale locale, Serializable... args)
    {
        List<String> smsg = this.userStrings.get(locale);
        if (smsg == null)
        {
            smsg = this.userStrings.get(this.defaultLocale);
        }
        if (smsg == null)
        {
            return new String[0];
        }
        final String[] result = new String[smsg.size()];
        int i = 0;
        for (final String lmsg : smsg)
        {
            result[i] = String.format(locale, lmsg, (Object[]) MessageTool.convertArgs(locale, false, args));
            i++;
        }
        return result;
    }
    
    @Override
    public String toAdminMessage(Locale locale, Serializable... args)
    {
        throw new IllegalStateException("Invalid message class."); //$NON-NLS-1$
    }
    
    @Override
    public String[] toAdminMessageLine(Locale locale, Serializable... args)
    {
        List<String> smsg = this.adminStrings.get(locale);
        if (smsg == null || smsg.size() == 0)
        {
            smsg = this.userStrings.get(locale);
        }
        if (smsg == null)
        {
            smsg = this.adminStrings.get(this.defaultLocale);
        }
        if (smsg == null || smsg.size() == 0)
        {
            smsg = this.userStrings.get(this.defaultLocale);
        }
        if (smsg == null)
        {
            return new String[0];
        }
        final String[] result = new String[smsg.size()];
        int i = 0;
        for (final String lmsg : smsg)
        {
            result[i] = String.format(locale, lmsg, (Object[]) MessageTool.convertArgs(locale, true, args));
            i++;
        }
        return result;
    }
    
}
