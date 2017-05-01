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

import de.minigameslib.mclib.api.enums.ChildEnum;

/**
 * The common items used by mclib.
 * 
 * @author mepeisen
 */
@ChildEnum({
    BlockData.CustomVariantId.class
})
public enum CommonItems implements ItemId
{
    
    // implementation detail: new items should be added at the end
    // and they should be alphabetically ordered. So that a new resource
    // zip will be backward compatible to all previous versions.
    
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/3d bar chart.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_3d_bar_chart,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Abort.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Abort,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/About.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_About,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Accounting.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Accounting,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Add.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Add,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Address book.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Address_book,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Alarm clock.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Alarm_clock,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Alarm.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Alarm,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Alert.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Alert,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Alien.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Alien,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Anchor.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Anchor,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Application.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Application,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Apply.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Apply,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Back.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Back,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Bad mark.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Bad_mark,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Bee.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Bee,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Black bookmark.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Black_bookmark,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Black pin.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Black_pin,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Black tag.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Black_tag,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Blog.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Blog,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Blue bookmark.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Blue_bookmark,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Blue key.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Blue_key,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Blue pin.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Blue_pin,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Blue tag.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Blue_tag,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Bomb.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Bomb,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Bookmark.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Bookmark,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Boss.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Boss,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Bottom.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Bottom,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Briefcase.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Briefcase,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Brush.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Brush,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Bubble.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Bubble,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Buy.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Buy,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Calculator.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Calculator,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Calendar.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Calendar,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Car key.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Car_key,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/CD.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_CD,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Clipboard.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Clipboard,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Clock.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Clock,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Close.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Close,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Coin.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Coin,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Comment.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Comment,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Company.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Company,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Compass.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Compass,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Component.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Component,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Computer.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Computer,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Copy.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Copy,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Create.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Create,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Cut.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Cut,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Danger.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Danger,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Database.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Database,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Delete.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Delete,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Delivery.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Delivery,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Diagram.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Diagram,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Dial.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Dial,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Disaster.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Disaster,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Display.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Display,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Dollar.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Dollar,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Door.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Door,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Down.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Down,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Download.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Download,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Downloads folder.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Downloads_folder,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/E mail.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_E_mail,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Earth.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Earth,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Eject.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Eject,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Equipment.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Equipment,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Erase.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Erase,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Error.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Error,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Euro.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Euro,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Exit.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Exit,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Expand.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Expand,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Eye.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Eye,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Fall.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Fall,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Fast forward.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Fast_forward,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Favourites.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Favourites,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Female symbol.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Female_symbol,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Female.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Female,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Film.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Film,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Filter.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Filter,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Find.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Find,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/First record.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_First_record,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/First.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_First,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Flag.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Flag,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Flash drive.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Flash_drive,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Folder.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Folder,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Forbidden.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Forbidden,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Forward.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Forward,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Free bsd.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Free_bsd,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Gift.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Gift,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Globe.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Globe,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Go back.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Go_back,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Go forward.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Go_forward,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Go.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Go,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Good mark.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Good_mark,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Green bookmark.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Green_bookmark,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Green pin.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Green_pin,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Green tag.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Green_tag,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Hard disk.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Hard_disk,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Heart.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Heart,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Help book 3d.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Help_book_3d,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Help book.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Help_book,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Help.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Help,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Hint.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Hint,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/History.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_History,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Home.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Home,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Hourglass.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Hourglass,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/How to.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_How_to,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Hungup.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Hungup,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/In yang.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_In_yang,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Info.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Info,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Iphone.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Iphone,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Key.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Key,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Last recor.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Last_recor,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Last.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Last,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Left right.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Left_right,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Letter.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Letter,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Lightning.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Lightning,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Liner.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Liner,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Linux.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Linux,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/List.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_List,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Load.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Load,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Lock.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Lock,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Low rating.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Low_rating,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Magic wand.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Magic_wand,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Mail.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Mail,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Male symbol.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Male_symbol,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Male.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Male,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Medium rating.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Medium_rating,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Message.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Message,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Mobile phone.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Mobile_phone,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Modify.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Modify,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Move.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Move,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Movie.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Movie,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Music.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Music,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Mute.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Mute,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Network connection.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Network_connection,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/New document.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_New_document,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/New.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_New,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Next track.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Next_track,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Next.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Next,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/No entry.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_No_entry,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/No.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_No,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Notes.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Notes,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/OK.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_OK,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Paste.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Paste,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Pause.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Pause,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/People.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_People,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Percent.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Percent,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Person.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Person,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Phone number.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Phone_number,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Phone.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Phone,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Picture.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Picture,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Pie chart.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Pie_chart,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Pinion.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Pinion,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Play music.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Play_music,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Play.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Play,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Playback.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Playback,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Previous record.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Previous_record,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Previous.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Previous,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Print.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Print,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Problem.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Problem,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Question.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Question,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Radiation.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Radiation,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Raise.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Raise,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Record.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Record,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Red bookmark.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Red_bookmark,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Red mark.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Red_mark,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Red pin.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Red_pin,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Red star.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Red_star,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Red tag.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Red_tag,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Redo.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Redo,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Refresh.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Refresh,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Remove.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Remove,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Repair.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Repair,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Report.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Report,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Retort.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Retort,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Rewind.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Rewind,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Sad.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Sad,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Save.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Save,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Schedule.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Schedule,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Script.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Script,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Search.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Search,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Shield.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Shield,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Shopping cart.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Shopping_cart,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Silence.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Silence,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Smile.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Smile,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Sound.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Sound,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Stock graph.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Stock_graph,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Stop sign.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Stop_sign,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Stop.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Stop,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Stopwatch.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Stopwatch,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Sum.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Sum,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Sync.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Sync,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Table.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Table,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Target.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Target,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Taxi.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Taxi,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Terminate.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Terminate,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Text preview.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Text_preview,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Text.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Text,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Thumbs down.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Thumbs_down,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Thumbs up.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Thumbs_up,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Toolbox.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Toolbox,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Top.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Top,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Trackback.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Trackback,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Trash.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Trash,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Tune.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Tune,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Turn off.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Turn_off,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Twitter.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Twitter,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Undo.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Undo,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Unlock.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Unlock,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Up down.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Up_down,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Up.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Up,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Update.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Update,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Upload.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Upload,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/User group.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_User_group,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/View.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_View,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Volume.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Volume,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Wallet.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Wallet,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Warning.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Warning,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Wrench.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Wrench,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Yellow bookmark.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Yellow_bookmark,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Yellow pin.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Yellow_pin,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Yellow tag.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Yellow_tag,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Yes.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Yes,
    /** aha-soft app icon. */
    @ItemData(textures = "de/minigameslib/mclib/resources/common/textures/items/ahasoft-app/Zoom.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
    App_Zoom,
    
}
