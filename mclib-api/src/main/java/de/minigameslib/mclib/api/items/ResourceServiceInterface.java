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

package de.minigameslib.mclib.api.items;

import java.io.File;
import java.io.IOException;

import de.minigameslib.mclib.api.MinecraftVersionsType;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;

/**
 * A service for custom resource packs.
 * 
 * @author mepeisen
 */
public interface ResourceServiceInterface
{
    
    /**
     * Returns the item services instance.
     * 
     * @return item services instance.
     */
    static ResourceServiceInterface instance()
    {
        return ResourceServiceCache.get();
    }
    
    /**
     * Changes the resource pack download url for current server resource version.
     * 
     * @param url
     *            the download url
     */
    void setDownloadUrl(String url);
    
    /**
     * Changes the resource pack download url.
     * 
     * @param url
     *            download url
     * @param version
     *            server resource version
     */
    void setDownloadUrl(String url, ResourceVersion version);
    
    /**
     * Returns the download url.
     * 
     * @return download url.
     */
    String getDownloadUrl();
    
    /**
     * Returns the download url.
     * 
     * @param version
     *            resource version
     * @return download url.
     */
    String getDownloadUrl(ResourceVersion version);
    
    /**
     * Returns true for automatic resource download on login.
     * 
     * @return automatic resource download flag
     */
    boolean isAutoResourceDownload();
    
    /**
     * Sets the automatic resource download flag.
     * 
     * @param newValue
     *            automatic resource download flag
     */
    void setAutoResourceDownload(boolean newValue);
    
    /**
     * Returns the number of ticks to wait for automatic resource download.
     * 
     * @return automatic resource download.
     */
    int getAutoResourceTicks();
    
    /**
     * Sets the number of ticks to wait for automatic resource download.
     * 
     * @param ticks
     *            number of ticks waiting before download
     */
    void setAutoResourceTicks(int ticks);
    
    /**
     * Checks if the player has accepted and installed the resource pack.
     * 
     * @param player
     *            target player
     * @return {@code true} if player has installed the resource pack.
     */
    boolean hasResourcePack(McPlayerInterface player);
    
    /**
     * Returns the resource pack status.
     * 
     * @param player
     *            target player
     * @return resource pack status or {@code null} if unknown
     */
    ResourcePackStatus getState(McPlayerInterface player);
    
    /**
     * Status of the resource pack.
     */
    public enum ResourcePackStatus
    {
        
        /**
         * The resource pack has been successfully downloaded and applied to the client.
         */
        SUCCESSFULLY_LOADED,
        /**
         * The client refused to accept the resource pack.
         */
        DECLINED,
        /**
         * The client accepted the pack, but download failed.
         */
        FAILED_DOWNLOAD,
        /**
         * The client accepted the pack and is beginning a download of it.
         */
        ACCEPTED;
    }
    
    /**
     * Forces downloading the resource pack (sends download request).
     * 
     * @param player
     *            target player
     * @param success
     *            invoked on successful download
     */
    void forceDownload(McPlayerInterface player, McRunnable success);
    
    /**
     * Forces downloading the resource pack (sends download request).
     * 
     * @param player
     *            target player
     * @param url
     *            a custom url
     * @param success
     *            invoked on successful download
     * @param failure
     *            invoked on failed downloads
     * @param declined
     *            invoked on declined downloads
     */
    void forceDownload(McPlayerInterface player, String url, McRunnable success, McRunnable failure, McRunnable declined);
    
    /**
     * Creates a resource pack file for all installed items; creates it for current server version.
     * 
     * @param target
     *            target file
     * @throws IOException
     *             thrown if there was a problem writing the file
     */
    void createResourcePack(File target) throws IOException;
    
    /**
     * Creates a resource pack file for all installed items; creates it for current server version.
     * 
     * @param target
     *            target file
     * @param version
     *            the version to be used
     * @throws IOException
     *             thrown if there was a problem writing the file
     */
    void createResourcePack(File target, ResourceVersion version) throws IOException;
    
    /**
     * Returns the resource version for given minecraft version type.
     * 
     * @param minecraftVersion
     *            minecraft version type.
     * @return resource version
     */
    ResourceVersion getResourceVersion(MinecraftVersionsType minecraftVersion);
    
    /**
     * The resource version enum.
     * 
     * @author mepeisen
     */
    public enum ResourceVersion
    {
        /** before 1.9 */
        PACK_FORMAT_1,
        /** 1.9 and 1.10 */
        PACK_FORMAT_2,
        /** 1.11 and above */
        PACK_FORMAT_3
    }
    
}
