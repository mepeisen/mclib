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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

/**
 * @author mepeisen
 *
 */
public class SpigotJunit4Runner extends Suite
{
    
    /** no runners. */
    private static final List<Runner> NO_RUNNERS = Collections.<Runner>emptyList();
    
    /** the runners. */
    private final List<Runner> runners;

    /**
     * @param klass
     * @throws InitializationError
     */
    public SpigotJunit4Runner(Class<?> klass) throws InitializationError
    {
        super(klass, NO_RUNNERS);
        this.runners = Collections.unmodifiableList(this.createRunners());
    }
    
    /**
     * Creates the runners.
     * @return runners.
     * @throws InitializationError 
     */
    private List<Runner> createRunners() throws InitializationError
    {
        final List<Runner> result = new ArrayList<>();
        final SpigotTest test = getTestClass().getAnnotation(SpigotTest.class);
        final Set<SpigotVersion> versions = new TreeSet<>();
        if (test == null)
        {
            versions.add(SpigotVersion.Latest);
        }
        else
        {
            for (final SpigotVersion version : test.versions())
            {
                versions.add(version);
            }
            if (test.all())
            {
                for (final SpigotVersion version : SpigotVersion.values())
                {
                    versions.add(version);
                }
            }
        }
        if (versions.contains(SpigotVersion.Latest))
        {
            versions.remove(SpigotVersion.Latest);
            versions.add(SpigotVersion.values()[SpigotVersion.values().length - 1]);
        }
        
        for (final SpigotVersion version : versions)
        {
            try
            {
                result.add(new SpigotTestRunner(getTestClass().getJavaClass(), version));
            }
            catch (IOException e)
            {
                throw new InitializationError(e);
            }
        }
        
        return result;
    }

    @Override
    protected List<Runner> getChildren()
    {
        return this.runners;
    }
    
}
