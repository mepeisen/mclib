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

package de.minigameslib.mclib.run;

import org.bukkit.scheduler.BukkitTask;

import de.minigameslib.mclib.MclibShowcasePlugin;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.objects.McPlayerInterface;

/**
 * Showcase step #00001, the tasks to let the user click.
 * 
 * @author mepeisen
 */
public class SC00001Tasks
{
    
    /** task 1 */
    private BukkitTask task1;
    /** task 2 */
    private BukkitTask task2;
    /** task 3 */
    private BukkitTask task3;
    /** task 4 */
    private BukkitTask task4;
    /** task 5 */
    private BukkitTask task5;
    
    /**
     * Constructor
     * @param player
     */
    public SC00001Tasks(McPlayerInterface player)
    {
        this.task1 = player.runTaskLater(MclibShowcasePlugin.instance(), 5*20, this::on5Seconds);
        this.task2 = player.runTaskLater(MclibShowcasePlugin.instance(), 10*20, this::on10Seconds);
        this.task3 = player.runTaskLater(MclibShowcasePlugin.instance(), 20*20, this::on20Seconds);
        this.task4 = player.runTaskLater(MclibShowcasePlugin.instance(), 40*20, this::on40Seconds);
        this.task5 = player.runTaskLater(MclibShowcasePlugin.instance(), 60*20, this::on60Seconds);
    }
    
    /**
     * Invoked after 5 seconds.
     * @param task
     */
    private void on5Seconds(BukkitTask task)
    {
        this.task1 = null;
        McLibInterface.instance().getCurrentPlayer().sendMessage(Showcase00001CountdownMessages.After5Seconds);
    }
    
    /**
     * Invoked after 10 seconds.
     * @param task
     */
    private void on10Seconds(BukkitTask task)
    {
        this.task2 = null;
        McLibInterface.instance().getCurrentPlayer().sendMessage(Showcase00001CountdownMessages.After10Seconds);
    }
    
    /**
     * Invoked after 20 seconds.
     * @param task
     */
    private void on20Seconds(BukkitTask task)
    {
        this.task3 = null;
        McLibInterface.instance().getCurrentPlayer().sendMessage(Showcase00001CountdownMessages.After20Seconds);
    }
    
    /**
     * Invoked after 40 seconds.
     * @param task
     */
    private void on40Seconds(BukkitTask task)
    {
        this.task4 = null;
        McLibInterface.instance().getCurrentPlayer().sendMessage(Showcase00001CountdownMessages.After40Seconds);
    }
    
    /**
     * Invoked after 60 seconds.
     * @param task
     */
    private void on60Seconds(BukkitTask task)
    {
        this.task5 = null;
        McLibInterface.instance().getCurrentPlayer().sendMessage(Showcase00001CountdownMessages.After60Seconds);
    }
    
    /**
     * Cancels all pending tasks.
     */
    public void cancel()
    {
        if (this.task1 != null) this.task1.cancel();
        if (this.task2 != null) this.task2.cancel();
        if (this.task3 != null) this.task3.cancel();
        if (this.task4 != null) this.task4.cancel();
        if (this.task5 != null) this.task5.cancel();
    }

    /**
     * Localized messages
     * 
     * @author mepeisen
     *
     */
    @LocalizedMessages(value="showcase.step00001.countdown", defaultLocale = "en")
    public enum Showcase00001CountdownMessages implements LocalizedMessageInterface
    {
        
        /**
         * After 5 seconds
         */
        @LocalizedMessage(defaultMessage = LocalizedMessage.BLUE + "you want to let me sing for you?")
        @MessageComment(value = {"After 5 seconds"})
        After5Seconds,
        
        /**
         * After 10 seconds
         */
        @LocalizedMessage(defaultMessage = LocalizedMessage.DARK_AQUA + "really? are you sure?")
        @MessageComment(value = {"After 10 seconds"})
        After10Seconds,
        
        /**
         * After 20 seconds
         */
        @LocalizedMessage(defaultMessage = LocalizedMessage.GOLD + "♩♫♬ " + LocalizedMessage.DARK_RED + "LAST CHRISTMAS I GAVE YOU MY COW " + LocalizedMessage.GOLD + "♩♫♬")
        @MessageComment(value = {"After 20 seconds"})
        After20Seconds,
        
        /**
         * After 40 seconds
         */
        @LocalizedMessage(defaultMessage = LocalizedMessage.GOLD + "♩♫♬ " + LocalizedMessage.DARK_RED + "BUT THE VERY NEXT DAY YOU CALLED A CREEPER " + LocalizedMessage.GOLD + "♩♫♬")
        @MessageComment(value = {"After 40 seconds"})
        After40Seconds,
        
        /**
         * After 60 seconds
         */
        @LocalizedMessageList({
            LocalizedMessageList.BLUE + "I am not good at singing.",
            LocalizedMessageList.BLUE + "But why did you not click?",
            LocalizedMessageList.BLUE + "Hum. Let us go on. I will click for you."})
        @MessageComment(value = {"After 60 seconds"})
        After60Seconds,
        
    }
    
}
