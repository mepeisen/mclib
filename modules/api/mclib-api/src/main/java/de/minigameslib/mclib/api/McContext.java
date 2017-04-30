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
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
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
     * Sets the given data before calling the runnable
     * 
     * @param event the event to set 
     * @param command the command to set
     * @param player the player to set
     * @param zone the zone to set
     * @param component the component to set
     * @param runnable
     *            the runnable to execute.
     * @throws McException
     *             rethrown from runnable.
     */
    void runInNewContext(Event event, CommandInterface command, McPlayerInterface player, ZoneInterface zone, ComponentInterface component, McRunnable runnable) throws McException;
    
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
     * @param <T>
     *            Type of return value
     * @return result from runnable
     * @throws McException
     *             rethrown from runnable.
     */
    <T> T calculateInNewContext(McSupplier<T> runnable) throws McException;
    
    /**
     * Runs the code in new context; changes made inside the runnable will be undone.
     * Sets the given data before calling the runnable
     * 
     * @param event the event to set 
     * @param command the command to set
     * @param player the player to set
     * @param zone the zone to set
     * @param component the component to set
     * @param runnable
     *            the runnable to execute.
     * @param <T>
     *            Type of return value
     * @return result from runnable
     * @throws McException
     *             rethrown from runnable.
     */
    <T> T calculateInNewContext(Event event, CommandInterface command, McPlayerInterface player, ZoneInterface zone, ComponentInterface component, McSupplier<T> runnable) throws McException;
    
    /**
     * Runs the code but copies all context variables before; changes made inside the runnable will be undone.
     * 
     * @param runnable
     *            the runnable to execute.
     * @param <T>
     *            Type of return value
     * @return result from runnable
     * @throws McException
     *             rethrown from runnable.
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
     * Returns the current sign.
     * 
     * @return current sign.
     */
    default SignInterface getCurrentSign()
    {
        return this.getContext(SignInterface.class);
    }
    
    /**
     * Returns the current object.
     * 
     * @return current object.
     */
    default ObjectInterface getCurrentObject()
    {
        return this.getContext(ObjectInterface.class);
    }
    
    /**
     * Returns the current entity.
     * 
     * @return current entity.
     */
    default EntityInterface getCurrentEntity()
    {
        return this.getContext(EntityInterface.class);
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
     * @param <T>
     *            context class to register
     * @throws McException
     *             thrown if the class to register is already registered.
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
     *            plugin that is responsible for given resolver
     * @param resolver
     *            helper for resolving context variables
     */
    void registerContextResolver(Plugin plugin, ContextResolverInterface resolver);
    
    /**
     * Unregisters context handlers and resolvers of given plugin.
     * 
     * @param plugin
     *            plugin that is disabled
     */
    void unregisterContextHandlersAndResolvers(Plugin plugin);
    
    // delayed execution
    
    /**
     * A runnable used by context enabled tasks.
     */
    @FunctionalInterface
    interface ContextRunnable
    {
        /**
         * Invoked to run the task
         * @param task bukkit task interface
         * @throws McException thrown if there were problems during task execution. If task was executed with current user this exception
         *     will be printed as error message to the current user. Be careful to throw this on recurring tasks.
         */
        void run(BukkitTask task) throws McException;
    }
    
    /**
     * Schedules this in the Bukkit scheduler to run on next tick.
     * 
     * <p>
     * This method will run the new method with a copy of current context.
     * </p>
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     */
    BukkitTask runTask(Plugin plugin, ContextRunnable task) throws IllegalArgumentException;

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules this in the Bukkit scheduler to run asynchronously.
     * </p>
     * 
     * <p>
     * This method will run the new method with a copy of current context.
     * </p>
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException if this was already scheduled
     */
    BukkitTask runTaskAsynchronously(Plugin plugin, ContextRunnable task) throws IllegalArgumentException;

    /**
     * Schedules this to run after the specified number of server ticks.
     * 
     * <p>
     * This method will run the new method with a copy of current context.
     * </p>
     *
     * @param plugin the reference to the plugin scheduling task
     * @param delay the ticks to wait before running the task
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     */
    BukkitTask runTaskLater(Plugin plugin, long delay, ContextRunnable task) throws IllegalArgumentException;

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules this to run asynchronously after the specified number of
     * server ticks.
     * </p>
     * 
     * <p>
     * This method will run the new method with a copy of current context.
     * </p>
     *
     * @param plugin the reference to the plugin scheduling task
     * @param delay the ticks to wait before running the task
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     */
    BukkitTask runTaskLaterAsynchronously(Plugin plugin, long delay, ContextRunnable task) throws IllegalArgumentException;

    /**
     * Schedules this to repeatedly run until cancelled, starting after the
     * specified number of server ticks.
     * 
     * <p>
     * This method will run the new method with a copy of current context.
     * </p>
     *
     * @param plugin the reference to the plugin scheduling task
     * @param delay the ticks to wait before running the task
     * @param period the ticks to wait between runs
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException if this was already scheduled
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    BukkitTask runTaskTimer(Plugin plugin, long delay, long period, ContextRunnable task) throws IllegalArgumentException;

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules this to repeatedly run asynchronously until cancelled,
     * starting after the specified number of server ticks.
     * </p>
     * 
     * <p>
     * This method will run the new method with a copy of current context.
     * </p>
     *
     * @param plugin the reference to the plugin scheduling task
     * @param delay the ticks to wait before running the task for the first
     *     time
     * @param period the ticks to wait between runs
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException if this was already scheduled
     * @see BukkitScheduler#runTaskTimerAsynchronously(Plugin, Runnable, long,
     *     long)
     */
    BukkitTask runTaskTimerAsynchronously(Plugin plugin, long delay, long period, ContextRunnable task) throws IllegalArgumentException;
    
}
