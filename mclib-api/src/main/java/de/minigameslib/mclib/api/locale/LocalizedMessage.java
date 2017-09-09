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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A single localized message.
 * 
 * @author mepeisen
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface LocalizedMessage
{
    
    /**
     * Represents black.
     */
    String BLACK             = "§0";    //$NON-NLS-1$
    
    /**
     * Represents dark blue.
     */
    String DARK_BLUE         = "§1";    //$NON-NLS-1$
    
    /**
     * Represents dark green.
     */
    String DARK_GREEN        = "§2";    //$NON-NLS-1$
    
    /**
     * Represents dark blue (aqua).
     */
    String DARK_AQUA         = "§3";    //$NON-NLS-1$
    
    /**
     * Represents dark red.
     */
    String DARK_RED          = "§4";    //$NON-NLS-1$
    
    /**
     * Represents dark purple.
     */
    String DARK_PURPLE       = "§5";    //$NON-NLS-1$
    
    /**
     * Represents gold.
     */
    String GOLD              = "§6";    //$NON-NLS-1$
    
    /**
     * Represents gray.
     */
    String GRAY              = "§7";    //$NON-NLS-1$
    
    /**
     * Represents dark gray.
     */
    String DARK_GRAY         = "§8";    //$NON-NLS-1$
    
    /**
     * Represents blue.
     */
    String BLUE              = "§9";    //$NON-NLS-1$
    
    /**
     * Represents green.
     */
    String GREEN             = "§a";    //$NON-NLS-1$
    
    /**
     * Represents aqua.
     */
    String AQUA              = "§b";    //$NON-NLS-1$
    
    /**
     * Represents red.
     */
    String RED               = "§c";    //$NON-NLS-1$
    
    /**
     * Represents light purple.
     */
    String LIGHT_PURPLE      = "§d";    //$NON-NLS-1$
    
    /**
     * Represents yellow.
     */
    String YELLOW            = "§e";    //$NON-NLS-1$
    
    /**
     * Represents white.
     */
    String WHITE             = "§f";    //$NON-NLS-1$
    
    /**
     * Represents magical characters that change around randomly.
     */
    String MAGIC             = "§k";    //$NON-NLS-1$
    
    /**
     * Makes the text bold.
     */
    String BOLD              = "§l";    //$NON-NLS-1$
    
    /**
     * Makes a line appear through the text.
     */
    String STRIKETHROUGH     = "§m";    //$NON-NLS-1$
    
    /**
     * Makes the text appear underlined.
     */
    String UNDERLINE         = "§n";    //$NON-NLS-1$
    
    /**
     * Makes the text italic.
     */
    String ITALIC            = "§o";    //$NON-NLS-1$
    
    /**
     * Resets all previous chat colors or formats.
     */
    String RESET             = "§r";    //$NON-NLS-1$
    
    /** semantic color: error message severity. */
    String ERROR_COLOR       = DARK_RED;
    /** semantic color: information message severity. */
    String INFORMATION_COLOR = WHITE;
    /** semantic color: loser message severity. */
    String LOSER_COLOR       = RED;
    /** semantic color: success message severity. */
    String SUCCESS_COLOR     = GREEN;
    /** semantic color: warning message severity. */
    String WARNING_COLOR     = YELLOW;
    /** semantic color: winner message severity. */
    String WINNER_COLOR      = GOLD;
    /** semantic color: message arguments or chat codes. */
    String CODE_COLOR        = BLUE;
    
    /**
     * Returns the default user message used as fallback; must be in locale returned by {@link LocalizedMessages#defaultLocale()}.
     * 
     * <p>
     * Uses {@link String#format(String, Object...)} to build the message with arguments.
     * </p>
     * 
     * @return default message.
     */
    String defaultMessage();
    
    /**
     * Returns the default administration message used as fallback; must be in locale returned by {@link LocalizedMessages#defaultLocale()}.
     * 
     * <p>
     * Uses {@link String#format(String, Object...)} to build the message with arguments.
     * </p>
     * 
     * @return default message; empty string if it should default to the user message
     */
    String defaultAdminMessage() default "";
    
    /**
     * Returns the message severity.
     * 
     * @return message severity.
     */
    MessageSeverityType severity() default MessageSeverityType.Information;
    
}
