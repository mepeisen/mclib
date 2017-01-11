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

package de.minigameslib.mclib.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import de.minigameslib.mclib.api.McContext;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.api.util.function.McSupplier;

/**
 * Implmentation of context.
 * 
 * @author mepeisen
 */
class McContextImpl implements McContext
{
    
    /** the registered context handlers. */
    private final Map<Class<?>, List<ContextHandlerInterface<?>>> handlers = new HashMap<>();
    
    /** the thread local storage. */
    final ThreadLocal<TLD> tls = ThreadLocal.withInitial(() -> new TLD());
    
    /** context resolve helper. */
    private final List<ContextResolverInterface> resolvers = new ArrayList<>();
    
    /** resolvers per plugin. */
    private final Map<Plugin, List<ContextResolverInterface>> pluginResolvers = new HashMap<>();
    
    /** handlers per plugin. */
    private final Map<Plugin, Map<Class<?>, List<ContextHandlerInterface<?>>>> pluginHandlers = new HashMap<>();
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getContext(Class<T> clazz)
    {
        final TLD data = this.tls.get();
        if (clazz == Event.class) return clazz.cast(data.event);
        if (clazz == CommandInterface.class) return clazz.cast(data.command);
        if (data.containsKey(clazz))
        {
            return (T) data.get(clazz);
        }
        if (data.computeStack.contains(clazz))
        {
            return null;
        }
        data.computeStack.add(clazz);
        try
        {
            if (this.handlers.containsKey(clazz))
            {
                for (final ContextHandlerInterface<?> handler : this.handlers.get(clazz))
                {
                    Object result = null;
                    if (data.command != null)
                    {
                        result = handler.calculateFromCommand(data.command, this);
                    }
                    else if (data.event != null)
                    {
                        result = handler.calculateFromEvent(data.event, this);
                    }
                    if (result != null)
                    {
                        data.put(clazz, result);
                        return (T) result;
                    }
                }
            }
            data.put(clazz, null);
            return null;
        }
        finally
        {
            data.computeStack.remove(clazz);
        }
    }
    
    @Override
    public <T> void setContext(Class<T> clazz, T value)
    {
        final TLD data = this.tls.get();
        if (clazz == Event.class)
        {
            data.event = (Event) value;
        }
        else if (clazz == CommandInterface.class)
        {
            data.command = (CommandInterface) value;
        }
        else
        {
            data.put(clazz, value);
        }
    }
    
    @Override
    public String resolveContextVar(String src)
    {
        if (!src.contains("$")) return src; //$NON-NLS-1$
        final StringBuilder builder = new StringBuilder();
        int start = src.indexOf('$');
        if (start > 0) builder.append(src, 0, start);
        int end = src.indexOf('$', start + 1);
        final String varWithArgs = src.substring(start + 1, end - 1);
        final String[] splitted = varWithArgs.split(":"); //$NON-NLS-1$
        builder.append(resolve(splitted));
        builder.append(this.resolveContextVar(src.substring(end + 1)));
        return builder.toString();
    }

    /**
     * Resolve context var
     * @param splitted
     * @return resolved var
     */
    private String resolve(String[] splitted)
    {
        final String varName = splitted[0];
        final String[] args = splitted.length == 1 ? new String[0] : Arrays.copyOfRange(splitted, 1, splitted.length);
        for (final ContextResolverInterface resolver : this.resolvers)
        {
            final String result = resolver.resolve(varName, args, this);
            if (result != null) return result;
        }
        return "?"; //$NON-NLS-1$
    }

    @Override
    public void runInNewContext(Event event, CommandInterface command, McPlayerInterface player, ZoneInterface zone, ComponentInterface component, McRunnable runnable) throws McException
    {
        final TLD old = this.tls.get();
        final TLD tld = new TLD();
        this.tls.set(tld);
        try
        {
            tld.clear();
            tld.command = command;
            tld.event = event;
            runnable.run();
        }
        finally
        {
            this.tls.set(old);
            tld.clear();
            tld.command = null;
            tld.event = null;
        }
    }
    
    @Override
    public void runInNewContext(McRunnable runnable) throws McException
    {
        final TLD old = this.tls.get();
        final TLD tld = new TLD();
        this.tls.set(tld);
        try
        {
            tld.clear();
            tld.command = old.command;
            tld.event = old.event;
            runnable.run();
        }
        finally
        {
            this.tls.set(old);
            tld.clear();
            tld.command = null;
            tld.event = null;
        }
    }
    
    @Override
    public void runInCopiedContext(McRunnable runnable) throws McException
    {
        final TLD old = this.tls.get();
        final TLD tld = new TLD();
        this.tls.set(tld);
        try
        {
            tld.clear();
            tld.putAll(old);
            tld.command = old.command;
            tld.event = old.event;
            runnable.run();
        }
        finally
        {
            this.tls.set(old);
            tld.clear();
            tld.command = null;
            tld.event = null;
        }
    }
    
    @Override
    public <T> T calculateInNewContext(McSupplier<T> runnable) throws McException
    {
        final TLD old = this.tls.get();
        final TLD tld = new TLD();
        this.tls.set(tld);
        try
        {
            tld.clear();
            tld.command = old.command;
            tld.event = old.event;
            return runnable.get();
        }
        finally
        {
            this.tls.set(old);
            tld.clear();
            tld.command = null;
            tld.event = null;
        }
    }
    
    @Override
    public <T> T calculateInCopiedContext(McSupplier<T> runnable) throws McException
    {
        final TLD old = this.tls.get();
        final TLD tld = new TLD();
        this.tls.set(tld);
        try
        {
            tld.clear();
            tld.putAll(old);
            tld.command = old.command;
            tld.event = old.event;
            return runnable.get();
        }
        finally
        {
            this.tls.set(old);
            tld.clear();
            tld.command = null;
            tld.event = null;
        }
    }

    @Override
    public <T> void registerContextHandler(Plugin plugin, Class<T> clazz, ContextHandlerInterface<T> handler) throws McException
    {
        this.handlers.computeIfAbsent(clazz, (key) -> new ArrayList<>()).add(handler);
        this.pluginHandlers.computeIfAbsent(plugin, (key) -> new HashMap<>()).computeIfAbsent(clazz, (key) -> new ArrayList<>()).add(handler);
    }

    @Override
    public void registerContextResolver(Plugin plugin, ContextResolverInterface resolver)
    {
        this.resolvers.add(resolver);
        this.pluginResolvers.computeIfAbsent(plugin, (key) -> new ArrayList<>()).add(resolver);
    }

    @Override
    public void unregisterContextHandlersAndResolvers(Plugin plugin)
    {
        final Map<Class<?>, List<ContextHandlerInterface<?>>> map = this.pluginHandlers.get(plugin);
        if (map != null)
        {
            for (final Map.Entry<Class<?>, List<ContextHandlerInterface<?>>> entry : map.entrySet())
            {
                for (final ContextHandlerInterface<?> handler : entry.getValue())
                {
                    this.handlers.get(entry.getKey()).remove(handler);
                }
            }
            this.pluginHandlers.remove(plugin);
        }
        final List<ContextResolverInterface> list = this.pluginResolvers.get(plugin);
        if (list != null)
        {
            for (final ContextResolverInterface resolver : list)
            {
                this.resolvers.remove(resolver);
            }
            this.pluginResolvers.remove(plugin);
        }
    }
    
    /**
     * @author mepeisen
     *
     */
    private final class MyRunnable extends BukkitRunnable
    {
        /**
         * 
         */
        private final AtomicReference<BukkitTask> result;
        /**
         * 
         */
        private final Map<Class<?>, Object>       data;
        /**
         * 
         */
        private final ContextRunnable task;
        
        /**
         * @param result
         * @param data
         * @param task
         */
        protected MyRunnable(AtomicReference<BukkitTask> result, Map<Class<?>, Object> data, ContextRunnable task)
        {
            this.result = result;
            this.data = data;
            this.task = task;
        }
        
        @Override
        public void run()
        {
            final TLD old = McContextImpl.this.tls.get();
            final TLD tld = new TLD();
            McContextImpl.this.tls.set(tld);
            try
            {
                tld.clear();
                tld.putAll(this.data);
                tld.command = old.command;
                tld.event = old.event;
                this.task.run(this.result.get());
            }
            catch (McException e)
            {
                final McPlayerInterface player = McContextImpl.this.getCurrentPlayer();
                if (player == null)
                {
                    // TODO Logging
                }
                else
                {
                    player.sendMessage(e.getErrorMessage(), e.getArgs());
                }
            }
            finally
            {
                McContextImpl.this.tls.set(old);
                tld.clear();
                tld.command = null;
                tld.event = null;
            }
        }
    }

    /**
     * thread local data
     */
    private static final class TLD extends HashMap<Class<?>, Object>
    {
        
        /**
         * serial version uid.
         */
        private static final long serialVersionUID = 3218652840066205979L;
        
        /** the underlying command being executed. */
        public CommandInterface command;
        /** the underlying event being executed. */
        public Event event;
        
        /** stack of computes to detect endless loops. */
        public Set<Class<?>> computeStack = new HashSet<>();
        
        /**
         * Constructor.
         */
        public TLD()
        {
            // empty
        }
    }

    @Override
    public BukkitTask runTask(Plugin plugin, ContextRunnable task) throws IllegalArgumentException
    {
        final Map<Class<?>, Object> data = new HashMap<>(this.tls.get());
        final AtomicReference<BukkitTask> result = new AtomicReference<>();
        final BukkitRunnable runnable = new MyRunnable(result, data, task);
        result.set(runnable.runTask(plugin));
        return result.get();
    }

    @Override
    public BukkitTask runTaskAsynchronously(Plugin plugin, ContextRunnable task) throws IllegalArgumentException
    {
        final Map<Class<?>, Object> data = new HashMap<>(this.tls.get());
        final AtomicReference<BukkitTask> result = new AtomicReference<>();
        final BukkitRunnable runnable = new MyRunnable(result, data, task);
        result.set(runnable.runTaskAsynchronously(plugin));
        return result.get();
    }

    @Override
    public BukkitTask runTaskLater(Plugin plugin, long delay, ContextRunnable task) throws IllegalArgumentException
    {
        final Map<Class<?>, Object> data = new HashMap<>(this.tls.get());
        final AtomicReference<BukkitTask> result = new AtomicReference<>();
        final BukkitRunnable runnable = new MyRunnable(result, data, task);
        result.set(runnable.runTaskLater(plugin, delay));
        return result.get();
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(Plugin plugin, long delay, ContextRunnable task) throws IllegalArgumentException
    {
        final Map<Class<?>, Object> data = new HashMap<>(this.tls.get());
        final AtomicReference<BukkitTask> result = new AtomicReference<>();
        final BukkitRunnable runnable = new MyRunnable(result, data, task);
        result.set(runnable.runTaskLaterAsynchronously(plugin, delay));
        return result.get();
    }

    @Override
    public BukkitTask runTaskTimer(Plugin plugin, long delay, long period, ContextRunnable task) throws IllegalArgumentException
    {
        final Map<Class<?>, Object> data = new HashMap<>(this.tls.get());
        final AtomicReference<BukkitTask> result = new AtomicReference<>();
        final BukkitRunnable runnable = new MyRunnable(result, data, task);
        result.set(runnable.runTaskTimer(plugin, delay, period));
        return result.get();
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, long delay, long period, ContextRunnable task) throws IllegalArgumentException
    {
        final Map<Class<?>, Object> data = new HashMap<>(this.tls.get());
        final AtomicReference<BukkitTask> result = new AtomicReference<>();
        final BukkitRunnable runnable = new MyRunnable(result, data, task);
        result.set(runnable.runTaskTimerAsynchronously(plugin, delay, period));
        return result.get();
    }
    
}
