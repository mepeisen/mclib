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

package de.minigameslib.mclib.api;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.api.util.function.McSupplier;

/**
 * The mclib execution context.
 * 
 * <p>
 * The execution context is some kind of thread local session storage. Once a command or event is being processed the context is responsible for providing common objects. For example the default
 * implementation can return the current player being responsible for a command call or event and to return the zone this player is located in.
 * </p>
 * 
 * @author mepeisen
 */
public interface McContext
{
    
    /**
     * Returns a session variable.
     * 
     * @param clazz
     *            the class of the variable to be returned.
     * @return Context variable or {@code null} if the variable was not set.
     * @param <T>
     *            Type of context value
     */
    <T> T getContext(Class<T> clazz);
    
    /**
     * Sets a context variable.
     * 
     * @param clazz
     *            the class of the variable
     * @param value
     *            the new value
     * @param <T>
     *            Type of context value
     */
    <T> void setContext(Class<T> clazz, T value);
    
    /**
     * Resolves a context variable and performs variable substitution.
     * 
     * @param src
     *            source string
     * @return result
     */
    String resolveContextVar(String src);
    
    /**
     * Runs the code in new context; changes made inside the runnable will be undone.
     * 
     * @param runnable
     *            the runnable to execute.
     * @throws McException
     *             rethrown from runnable.
     */
    void runInNewContext(McRunnable runnable) throws McException;
    
    /**
     * Runs the code in new context but copies all context variables before; changes made inside the runnable will be undone.
     * 
     * @param runnable
     *            the runnable to execute.
     * @throws McException
     *             rethrown from runnable.
     */
    void runInCopiedContext(McRunnable runnable) throws McException;
    
    /**
     * Runs the code in new context; changes made inside the runnable will be undone.
     * 
     * @param runnable
     *            the runnable to execute.
     * @return result from runnable
     * @throws McException
     *             rethrown from runnable.
     * @param <T>
     *            Type of return value
     */
    <T> T calculateInNewContext(McSupplier<T> runnable) throws McException;
    
    /**
     * Runs the code but copies all context variables before; changes made inside the runnable will be undone.
     * 
     * @param runnable
     *            the runnable to execute.
     * @return result from runnable
     * @throws McException
     *             rethrown from runnable.
     * @param <T>
     *            Type of return value
     */
    <T> T calculateInCopiedContext(McSupplier<T> runnable) throws McException;
    
    /**
     * Returns the current command if context is executed within command.
     * 
     * @return current command.
     */
    default CommandInterface getCurrentCommand()
    {
        return this.getContext(CommandInterface.class);
    }
    
    /**
     * Returns the current bukkit event if context is executed within bukkit event.
     * 
     * @return bukkit event.
     */
    default Event getCurrentEvent()
    {
        return this.getContext(Event.class);
    }
    
    /**
     * Returns the current player.
     * 
     * @return current player.
     */
    default McPlayerInterface getCurrentPlayer()
    {
        return this.getContext(McPlayerInterface.class);
    }
    
    /**
     * Returns the current zone.
     * 
     * @return current zone.
     */
    default ZoneInterface getCurrentZone()
    {
        return this.getContext(ZoneInterface.class);
    }
    
    /**
     * Returns the current component.
     * 
     * @return current component.
     */
    default ComponentInterface getCurrentComponent()
    {
        return this.getContext(ComponentInterface.class);
    }
    
    /**
     * An interface to calculate context variables.
     * 
     * @author mepeisen
     * @param <T>
     *            context class
     */
    public interface ContextHandlerInterface<T>
    {
        
        /**
         * Calculates the context object from command.
         * 
         * @param command
         *            command to process
         * @param context
         *            current minigame context
         * @return context object or {@code null} if it cannot be calculated
         */
        T calculateFromCommand(CommandInterface command, McContext context);
        
        /**
         * Calculates the context object from minigame event.
         * 
         * @param event
         *            event to process
         * @param context
         *            current minigame context
         * @return context object or {@code null} if it cannot be calculated
         */
        T calculateFromEvent(Event event, McContext context);
        
    }
    
    /**
     * Registers a context handler to calculate context variables.
     * 
     * @param plugin
     *            Plugin owner of the context handler.
     * @param clazz
     *            context class.
     * @param handler
     *            the context handler.
     * @throws McException
     *             thrown if the class to register is already registered.
     * @param <T>
     *            context class to register
     */
    <T> void registerContextHandler(Plugin plugin, Class<T> clazz, ContextHandlerInterface<T> handler) throws McException;
    
    /**
     * An interface being able to resolve variables with contexts.
     * 
     * @author mepeisen
     */
    public interface ContextResolverInterface
    {
        
        /**
         * Tries to resolve given variable name.
         * 
         * @param varName
         *            variable name to resolve.
         * @param args
         *            arguments for resolve
         * @param context
         *            the context
         * @return the resolved string or {@code null} if the variable cannot be resolved.
         */
        String resolve(String varName, String[] args, McContext context);
        
    }
    
    /**
     * Registers a resolver for context variables.
     * 
     * @param plugin
     * @param resolver
     */
    void registerContextResolver(Plugin plugin, ContextResolverInterface resolver);
    
    /**
     * Unregisters context handlers and resolvers of given plugin.
     * 
     * @param plugin
     */
    void unregisterContextHandlersAndResolvers(Plugin plugin);
    
}
