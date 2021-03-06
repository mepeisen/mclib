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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

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
    private final Map<Class<?>, List<ContextHandlerInterface<?>>>              handlers       = new HashMap<>();
    
    /** the thread local storage. */
    final ThreadLocal<MyTls>                                                   tls            = ThreadLocal.withInitial(() -> new MyTls());
    
    /** handlers per plugin. */
    private final Map<Plugin, Map<Class<?>, List<ContextHandlerInterface<?>>>> pluginHandlers = new HashMap<>();
    
    @Override
    public <T> Function<Supplier<T>, T> createContextSupplier()
    {
        final Map<Class<?>, Object> data = new HashMap<>(this.tls.get());
        return r -> this.calculateInNewContextUnchecked(() -> {
            this.tls.get().putAll(data);
            return r.get();
        });
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getContext(Class<T> clazz)
    {
        final MyTls data = this.tls.get();
        if (clazz == Event.class)
        {
            return clazz.cast(data.event);
        }
        if (clazz == CommandInterface.class)
        {
            return clazz.cast(data.command);
        }
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
        final MyTls data = this.tls.get();
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
    public void runInNewContext(Event event, CommandInterface command, McPlayerInterface player, ZoneInterface zone, ComponentInterface component, McRunnable runnable) throws McException
    {
        final MyTls old = this.tls.get();
        final MyTls tld = new MyTls();
        this.tls.set(tld);
        try
        {
            tld.clear();
            tld.command = command;
            tld.event = event;
            if (player != null)
            {
                tld.put(McPlayerInterface.class, player);
            }
            if (zone != null)
            {
                tld.put(ZoneInterface.class, zone);
            }
            if (component != null)
            {
                tld.put(ComponentInterface.class, component);
            }
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
        final MyTls old = this.tls.get();
        final MyTls tld = new MyTls();
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
    public void runInNewContextUnchecked(Event event, CommandInterface command, McPlayerInterface player, ZoneInterface zone, ComponentInterface component, Runnable runnable)
    {
        final MyTls old = this.tls.get();
        final MyTls tld = new MyTls();
        this.tls.set(tld);
        try
        {
            tld.clear();
            tld.command = command;
            tld.event = event;
            if (player != null)
            {
                tld.put(McPlayerInterface.class, player);
            }
            if (zone != null)
            {
                tld.put(ZoneInterface.class, zone);
            }
            if (component != null)
            {
                tld.put(ComponentInterface.class, component);
            }
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
    public void runInNewContextUnchecked(Runnable runnable)
    {
        final MyTls old = this.tls.get();
        final MyTls tld = new MyTls();
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
        final MyTls old = this.tls.get();
        final MyTls tld = new MyTls();
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
    public void runInCopiedContextUnchecked(Runnable runnable)
    {
        final MyTls old = this.tls.get();
        final MyTls tld = new MyTls();
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
    public <T> T calculateInNewContext(Event event, CommandInterface command, McPlayerInterface player, ZoneInterface zone, ComponentInterface component, McSupplier<T> runnable) throws McException
    {
        final MyTls old = this.tls.get();
        final MyTls tld = new MyTls();
        this.tls.set(tld);
        try
        {
            tld.clear();
            tld.command = command;
            tld.event = event;
            if (player != null)
            {
                tld.put(McPlayerInterface.class, player);
            }
            if (zone != null)
            {
                tld.put(ZoneInterface.class, zone);
            }
            if (component != null)
            {
                tld.put(ComponentInterface.class, component);
            }
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
    public <T> T calculateInNewContext(McSupplier<T> runnable) throws McException
    {
        final MyTls old = this.tls.get();
        final MyTls tld = new MyTls();
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
    public <T> T calculateInNewContextUnchecked(Event event, CommandInterface command, McPlayerInterface player, ZoneInterface zone, ComponentInterface component, Supplier<T> runnable)
    {
        final MyTls old = this.tls.get();
        final MyTls tld = new MyTls();
        this.tls.set(tld);
        try
        {
            tld.clear();
            tld.command = command;
            tld.event = event;
            if (player != null)
            {
                tld.put(McPlayerInterface.class, player);
            }
            if (zone != null)
            {
                tld.put(ZoneInterface.class, zone);
            }
            if (component != null)
            {
                tld.put(ComponentInterface.class, component);
            }
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
    public <T> T calculateInNewContextUnchecked(Supplier<T> runnable)
    {
        final MyTls old = this.tls.get();
        final MyTls tld = new MyTls();
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
        final MyTls old = this.tls.get();
        final MyTls tld = new MyTls();
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
    public <T> T calculateInCopiedContextUnchecked(Supplier<T> runnable)
    {
        final MyTls old = this.tls.get();
        final MyTls tld = new MyTls();
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
    
    /**
     * Bukkit runnable wrapper for context sensitive execution.
     * 
     * @author mepeisen
     */
    private final class MyRunnable extends BukkitRunnable
    {
        /**
         * The underlying bukkit task.
         */
        private final AtomicReference<BukkitTask> result;
        /**
         * context data.
         */
        private final Map<Class<?>, Object>       data;
        /**
         * the task to be executed.
         */
        private final ContextRunnable             task;
        
        /**
         * Constructor to create a runnable.
         * 
         * @param result
         *            the bukkit task (will be created in constructor and returned).
         * @param data
         *            the context data to be used
         * @param task
         *            he task to be executed
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
            final MyTls old = McContextImpl.this.tls.get();
            final MyTls tld = new MyTls();
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
     * thread local data.
     */
    private static final class MyTls extends HashMap<Class<?>, Object>
    {
        
        /**
         * serial version uid.
         */
        private static final long serialVersionUID = 3218652840066205979L;
        
        /** the underlying command being executed. */
        public CommandInterface   command;
        /** the underlying event being executed. */
        public Event              event;
        
        /** stack of computes to detect endless loops. */
        public Set<Class<?>>      computeStack     = new HashSet<>();
        
        /**
         * Constructor.
         */
        public MyTls()
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
