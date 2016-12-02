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

package de.minigameslib.mclib.pshared;

/**
 * Enumeration with message codes for core client-server communication.
 * 
 * @author mepeisen
 */
public enum CoreMessages
{
    
    /**
     * Ping from server to client
     * @see PingData
     */
    Ping,
    
    /**
     * Pong from client to server
     * @see PongData
     */
    Pong,
    
    /**
     * displays an error message.
     * @see DisplayErrorData
     */
    DisplayError,
    
    /**
     * displays a yes/no message.
     * @see DisplayYesNoData
     */
    DisplayYesNo,
    
    /**
     * displays a yes/no/cancel message.
     * @see DisplayYesNoCancelData
     */
    DisplayYesNoCancel,
    
    /**
     * displays an info message.
     * @see DisplayInfoData
     */
    DisplayInfo,
    
    /**
     * displays a panel/window.
     * @see DisplayResizableWinData
     */
    DisplayResizableWin,
    
    /**
     * client notifies about a closed window.
     * @see WinClosedData
     */
    WinClosed,
    
    /**
     * server requires a window to be closed.
     * @see CloseWinData
     */
    CloseWin,
    
    /**
     * An action has performed from client side.
     * @see ActionPerformedData
     */
    ActionPerformed,
    
    /**
     * Sends an error message to an existing resizable win.
     * @see SendErrorData
     */
    SendError,
    
    /**
     * Form data for form inputs (f.e. lists) has been queried.
     * @see QueryFormRequestData
     */
    QueryFormRequest,
    
    /**
     * Form data for form inputs (f.e. lists).
     * @see QueryFormAnswerData
     */
    QueryFormAnswer,
    
    /**
     * Displays a marker
     * @see DisplayMarkerData
     */
    DisplayMarker,
    
    /**
     * Removes a marker
     * @see RemoveMarkerData
     */
    RemoveMarker,
    
    /**
     * Removes all displayed markers
     * @see ResetMarkersData
     */
    ResetMarkers,
    
}
