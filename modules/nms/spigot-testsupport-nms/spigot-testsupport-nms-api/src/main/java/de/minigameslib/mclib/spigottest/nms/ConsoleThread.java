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

package de.minigameslib.mclib.spigottest.nms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import com.google.common.util.concurrent.UncheckedTimeoutException;

/**
 * thread to fetch console.
 * @author mepeisen
 */
public class ConsoleThread extends Thread
{
    
    /** logger. */
    static final Logger LOGGER = Logger.getLogger(ConsoleThread.class.getName());
    
    /** finished flag. */
    private volatile boolean finished;
    
    /** piped console output. */
    private PipedInputStream in;
    
    /** console. */
    private List<String> console;
    
    /**
     * @param in
     * @param console
     */
    public ConsoleThread(PipedInputStream in, List<String> console)
    {
        this.in = in;
        this.console = console;
    }

    @Override
    public void run()
    {
        try
        {
            try (final InputStreamReader ir = new InputStreamReader(this.in))
            {
                try (final BufferedReader br = new BufferedReader(ir))
                {
                    while (!this.finished)
                    {
                        final TimeLimiter limiter = new SimpleTimeLimiter();
                        try
                        {
                            final String line = limiter.callWithTimeout(br::readLine, 1, TimeUnit.SECONDS, false);
                            synchronized (this.console)
                            {
                                this.console.add(line);
                                this.console.notifyAll();
                            }
                        }
                        catch (@SuppressWarnings("unused") UncheckedTimeoutException ex)
                        {
                            // silently ignore timeouts
                        }
                        catch (Exception ex)
                        {
                            LOGGER.log(Level.SEVERE, "Problems reading console", ex); //$NON-NLS-1$
                        }
                    }
                }
            }
        }
        catch (IOException ex)
        {
            LOGGER.log(Level.SEVERE, "Problems reading console", ex); //$NON-NLS-1$
        }
    }
    
    /**
     * Terminates the thread.
     */
    public void done()
    {
        this.finished = true;
    }
    
}