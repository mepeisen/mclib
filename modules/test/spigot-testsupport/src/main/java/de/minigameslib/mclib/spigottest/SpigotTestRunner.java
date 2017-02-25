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

package de.minigameslib.mclib.spigottest;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import de.minigameslib.mclib.spigottest.SpigotTest.CustomPlugin;

/**
 * @author mepeisen
 *
 */
public class SpigotTestRunner extends BlockJUnit4ClassRunner
{
    
    /**
     * spigot server class name.
     */
    private static final String SPIGOT_SERVER = "de.minigameslib.mclib.spigottest.SpigotServer"; //$NON-NLS-1$

    /** spigot version to be used. */
    private SpigotVersion version;
    
    /** the spigot server. */
    SpigotServer server;

    /**
     * Constructor
     * @param klass
     * @param version
     * @throws InitializationError 
     * @throws IOException 
     */
    public SpigotTestRunner(Class<?> klass, SpigotVersion version) throws InitializationError, IOException
    {
        super(klass);
        this.version = version;
        final SpigotServerConfig config = new SpigotServerConfig().setServerVersion(this.version);
        final SpigotTest test = klass.getAnnotation(SpigotTest.class);
        if (test != null)
        {
            if (test.loadLocalPlugin())
            {
                config.addLocalPlugin();
            }
            if (test.customPlugins() != null && test.customPlugins().length > 0)
            {
                for (final CustomPlugin plugin : test.customPlugins())
                {
                    config.addCustomPlugin(plugin);
                }
            }
        }
        this.server = config.create();
    }

    @Override
    public Object createTest() throws Exception
    {
        final Object obj = this.server.createInstance(this.getTestClass().getJavaClass());
        
        // initialize inject fields.
        for (final FrameworkField field : this.getTestClass().getAnnotatedFields(SpigotInject.class))
        {
            final Field reflectField = obj.getClass().getDeclaredField(field.getName());
            reflectField.setAccessible(true);
            switch (reflectField.getType().getName())
            {
                case SPIGOT_SERVER:
                    reflectField.set(obj, this.server);
                    break;
                default:
                    System.out.println("Unknown class type for spigot inject. field: " + field.getName() + " / type: " + reflectField.getType()); //$NON-NLS-1$ //$NON-NLS-2$
                    break;
            }
        }
        
        return obj;
    }

    @Override
    protected String getName()
    {
        return "[" + this.version.name() + "] " + super.getName();  //$NON-NLS-1$//$NON-NLS-2$
    }

    @Override
    protected String testName(FrameworkMethod method)
    {
        return "[" + this.version.name() + "] " + super.testName(method);  //$NON-NLS-1$//$NON-NLS-2$
    }

    @Override
    protected Statement classBlock(RunNotifier notifier)
    {
        final Statement classBlock = super.classBlock(notifier);
        return new Statement() {
            
            @Override
            public void evaluate() throws Throwable
            {
                SpigotTestRunner.this.server.start();
                try
                {
                    assertTrue(SpigotTestRunner.this.server.waitGameCycle(60000));
                    classBlock.evaluate();
                }
                finally
                {
                    try
                    {
                        SpigotTestRunner.this.server.stop();
                        if (!SpigotTestRunner.this.server.waitShutdown(25000))
                        {
                            SpigotTestRunner.this.server.destroy();
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                        SpigotTestRunner.this.server.destroy();
                    }
                }
            }
        };
    }
    
    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test)
    {
        return new Statement() {
            
            @Override
            public void evaluate() throws Throwable
            {
                final Method mth = test.getClass().getDeclaredMethod(method.getName());
                new ReflectiveCallable() {
                    
                    @Override
                    protected Object runReflectiveCall() throws Throwable
                    {
                        return mth.invoke(test);
                    }
                }.run();
            }
        };
    }
    
}
