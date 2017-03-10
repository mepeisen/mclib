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
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private static final PrintStream sysOut = System.out;
    
    /** error. */
    private static final PrintStream sysErr = System.err;
    
    /** finished flag. */
    private volatile boolean finished;
    
    /** piped console output. */
    private PipedInputStream in;
    
    /** console. */
    private List<String> console;

    /** piped output stream. */
    private PipedOutputStream os;
    
    /**
     * @param in
     * @param os 
     * @param console
     */
    public ConsoleThread(PipedInputStream in, PipedOutputStream os, List<String> console)
    {
        this.in = in;
        this.os = os;
        this.console = console;
        System.setOut(new PrintStream(os));
        System.setErr(new PrintStream(os));
        System.out.println("fetch sysout/syserr: " + sysOut + "/" + sysErr);
        System.out.println("new sysout/syserr: " + System.out + "/" + System.err);
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
                            if (line != null)
                            {
                                synchronized (this.console)
                                {
                                    sysOut.println(line);
                                    this.console.add(line);
                                    this.console.notifyAll();
                                }
                            }
                        }
                        catch (@SuppressWarnings("unused") UncheckedTimeoutException ex)
                        {
                            // silently ignore timeouts
                        }
                        catch (@SuppressWarnings("unused") IOException ex)
                        {
                            // silently ignore io exceptions, my indicate dead streams, however we wait for
                            // real termination from spigottestrunner
                        }
                        catch (Exception ex)
                        {
                            sysOut.println("Problems reading console"); //$NON-NLS-1$
                            ex.printStackTrace(sysOut);
                        }
                    }
                }
                finally
                {
                    System.out.println("resetting sysout/syserr: " + sysOut + "/" + sysErr);
                    System.setOut(sysOut);
                    System.setErr(sysErr);
                    System.out.println("resetting sysout/syserr finished");
                }
            }
        }
        catch (IOException ex)
        {
            sysOut.println("Problems reading console"); //$NON-NLS-1$
            ex.printStackTrace(sysOut);
        }
    }
    
    /**
     * Terminates the thread.
     */
    public void done()
    {
        this.finished = true;
        try
        {
            this.in.close();
        }
        catch (IOException ex)
        {
            // TODO logging
        }
        try
        {
            this.os.close();
        }
        catch (IOException ex)
        {
            // TODO logging
        }
        try
        {
            this.join();
        }
        catch (@SuppressWarnings("unused") InterruptedException e)
        {
            // silently ignore
        }
    }
    
}