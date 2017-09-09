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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Some tooling functions for data sections.
 * 
 * @author mepeisen
 */
public class DataSectionTools
{
    
    /**
     * Reads data section from given google stream.
     * 
     * @param buf
     *            data input
     * @return data section.
     * @throws IOException
     *             thrown on io errors
     */
    public static DataSection read(DataInputStream buf) throws IOException
    {
        final DataSection data = new MemoryDataSection();
        
        int num = buf.readInt();
        for (int i = 0; i < num; i++)
        {
            final String key = buf.readUTF();
            final byte typeNum = buf.readByte();
            switch (typeNum)
            {
                case TYPE_STRING:
                    data.set(key, buf.readUTF());
                    break;
                case TYPE_BYTE:
                    data.set(key, buf.readByte());
                    break;
                case TYPE_SHORT:
                    data.set(key, buf.readShort());
                    break;
                case TYPE_INT:
                    data.set(key, buf.readInt());
                    break;
                case TYPE_LONG:
                    data.set(key, buf.readLong());
                    break;
                case TYPE_BOOL:
                    data.set(key, buf.readBoolean());
                    break;
                case TYPE_CHAR:
                    data.set(key, buf.readChar());
                    break;
                case TYPE_FLOAT:
                    data.set(key, buf.readFloat());
                    break;
                case TYPE_DOUBLE:
                    data.set(key, buf.readDouble());
                    break;
                case TYPE_DATETIME:
                    data.set(key, LocalDateTime.parse(buf.readUTF()));
                    break;
                case TYPE_DATE:
                    data.set(key, LocalDate.parse(buf.readUTF()));
                    break;
                case TYPE_TIME:
                    data.set(key, LocalTime.parse(buf.readUTF()));
                    break;
                default:
                    throw new IllegalStateException("Invalid content type: " + typeNum); //$NON-NLS-1$
            }
        }
        return data;
    }
    
    /**
     * Reads data section from given google stream.
     * 
     * @param buf
     *            data input
     * @return data section.
     */
    public static DataSection read(ByteArrayDataInput buf)
    {
        final DataSection data = new MemoryDataSection();
        try
        {
            while (true)
            {
                final String key = readUtf8(buf);
                final byte typeNum = buf.readByte();
                switch (typeNum)
                {
                    case TYPE_STRING:
                        data.set(key, readUtf8(buf));
                        break;
                    case TYPE_BYTE:
                        data.set(key, buf.readByte());
                        break;
                    case TYPE_SHORT:
                        data.set(key, buf.readShort());
                        break;
                    case TYPE_INT:
                        data.set(key, buf.readInt());
                        break;
                    case TYPE_LONG:
                        data.set(key, buf.readLong());
                        break;
                    case TYPE_BOOL:
                        data.set(key, buf.readBoolean());
                        break;
                    case TYPE_CHAR:
                        data.set(key, buf.readChar());
                        break;
                    case TYPE_FLOAT:
                        data.set(key, buf.readFloat());
                        break;
                    case TYPE_DOUBLE:
                        data.set(key, buf.readDouble());
                        break;
                    case TYPE_DATETIME:
                        data.set(key, LocalDateTime.parse(readUtf8(buf)));
                        break;
                    case TYPE_DATE:
                        data.set(key, LocalDate.parse(readUtf8(buf)));
                        break;
                    case TYPE_TIME:
                        data.set(key, LocalTime.parse(readUtf8(buf)));
                        break;
                    default:
                        throw new IllegalStateException("Invalid content type: " + typeNum); //$NON-NLS-1$
                }
            }
        }
        catch (IllegalStateException ex)
        {
            // silently ignore; data input cannot be asked for remaining bytes.
            // so we will read till be get an EOFException
            // google io wraps EOF inside illegal state
            
            // TODO maybe we drop corrupt streams silently because EOF can be thrown by reading the keys or values.
            // should we add a "number of items" as first data?
            
            if (!(ex.getCause() instanceof EOFException))
            {
                // rethrow unknown exception
                throw ex;
            }
        }
        return data;
    }
    
    /** type num. */
    private static final byte TYPE_STRING   = 0;
    /** type num. */
    private static final byte TYPE_BYTE     = 1;
    /** type num. */
    private static final byte TYPE_SHORT    = 2;
    /** type num. */
    private static final byte TYPE_INT      = 3;
    /** type num. */
    private static final byte TYPE_LONG     = 4;
    /** type num. */
    private static final byte TYPE_BOOL     = 5;
    /** type num. */
    private static final byte TYPE_CHAR     = 6;
    /** type num. */
    private static final byte TYPE_FLOAT    = 7;
    /** type num. */
    private static final byte TYPE_DOUBLE   = 8;
    /** type num. */
    private static final byte TYPE_DATETIME = 9;
    /** type num. */
    private static final byte TYPE_DATE     = 10;
    /** type num. */
    private static final byte TYPE_TIME     = 11;
    
    /**
     * Reads utf8 string.
     * 
     * @param buf
     *            byte buffer
     * @return utf8 string
     */
    public static String readUtf8(ByteArrayDataInput buf)
    {
        int size = buf.readInt();
        if (size > 150000)
        {
            throw new IllegalStateException("string to big; max 150000"); //$NON-NLS-1$
        }
        byte[] strbuf = new byte[size];
        buf.readFully(strbuf);
        final String result = new String(strbuf, Charsets.UTF_8);
        return result;
    }
    
    /**
     * Writes utf8 string.
     * 
     * @param buf
     *            byte buffer
     * @param value
     *            value to be written
     */
    public static void writeUtf8(ByteArrayDataOutput buf, String value)
    {
        final byte[] bytes = value.getBytes(Charsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.write(bytes);
    }
    
    /**
     * Converts this message to byte stream.
     * 
     * @param buf
     *            byte buffer
     * @param data
     *            the data to be written
     */
    public static void toBytes(ByteArrayDataOutput buf, DataSection data)
    {
        for (final Map.Entry<String, Object> entry : data.getValues(true).entrySet())
        {
            writeUtf8(buf, entry.getKey());
            if (entry.getValue() instanceof String)
            {
                final String value = (String) entry.getValue();
                buf.writeByte(TYPE_STRING);
                writeUtf8(buf, value);
            }
            else if (entry.getValue() instanceof Byte)
            {
                buf.writeByte(TYPE_BYTE);
                buf.writeByte((Byte) entry.getValue());
            }
            else if (entry.getValue() instanceof Short)
            {
                buf.writeByte(TYPE_SHORT);
                buf.writeShort((Short) entry.getValue());
            }
            else if (entry.getValue() instanceof Integer)
            {
                buf.writeByte(TYPE_INT);
                buf.writeInt((Integer) entry.getValue());
            }
            else if (entry.getValue() instanceof Long)
            {
                buf.writeByte(TYPE_LONG);
                buf.writeLong((Long) entry.getValue());
            }
            else if (entry.getValue() instanceof Boolean)
            {
                buf.writeByte(TYPE_BOOL);
                buf.writeBoolean((Boolean) entry.getValue());
            }
            else if (entry.getValue() instanceof Character)
            {
                buf.writeByte(TYPE_CHAR);
                buf.writeChar((Character) entry.getValue());
            }
            else if (entry.getValue() instanceof Float)
            {
                buf.writeByte(TYPE_FLOAT);
                buf.writeFloat((Float) entry.getValue());
            }
            else if (entry.getValue() instanceof Double)
            {
                buf.writeByte(TYPE_DOUBLE);
                buf.writeDouble((Double) entry.getValue());
            }
            else if (entry.getValue() instanceof LocalDateTime)
            {
                final String value = ((LocalDateTime) entry.getValue()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                buf.writeByte(TYPE_DATETIME);
                writeUtf8(buf, value);
            }
            else if (entry.getValue() instanceof LocalDate)
            {
                final String value = ((LocalDate) entry.getValue()).format(DateTimeFormatter.ISO_LOCAL_DATE);
                buf.writeByte(TYPE_DATE);
                writeUtf8(buf, value);
            }
            else if (entry.getValue() instanceof LocalTime)
            {
                final String value = ((LocalTime) entry.getValue()).format(DateTimeFormatter.ISO_LOCAL_TIME);
                buf.writeByte(TYPE_TIME);
                writeUtf8(buf, value);
            }
            else
            {
                throw new IllegalStateException("Invalid content type: " + entry.getValue().getClass()); //$NON-NLS-1$
            }
        }
    }
    
    /**
     * Converts this message to byte stream.
     * 
     * @param buf
     *            byte buffer
     * @param data
     *            the data to be written
     * @throws IOException
     *             thrown on io problems
     */
    public static void toBytes(DataOutputStream buf, DataSection data) throws IOException
    {
        final Map<String, Object> values = data.getValues(true);
        buf.writeInt(values.size());
        for (final Map.Entry<String, Object> entry : values.entrySet())
        {
            buf.writeUTF(entry.getKey());
            if (entry.getValue() instanceof String)
            {
                final String value = (String) entry.getValue();
                buf.writeByte(TYPE_STRING);
                buf.writeUTF(value);
            }
            else if (entry.getValue() instanceof Byte)
            {
                buf.writeByte(TYPE_BYTE);
                buf.writeByte((Byte) entry.getValue());
            }
            else if (entry.getValue() instanceof Short)
            {
                buf.writeByte(TYPE_SHORT);
                buf.writeShort((Short) entry.getValue());
            }
            else if (entry.getValue() instanceof Integer)
            {
                buf.writeByte(TYPE_INT);
                buf.writeInt((Integer) entry.getValue());
            }
            else if (entry.getValue() instanceof Long)
            {
                buf.writeByte(TYPE_LONG);
                buf.writeLong((Long) entry.getValue());
            }
            else if (entry.getValue() instanceof Boolean)
            {
                buf.writeByte(TYPE_BOOL);
                buf.writeBoolean((Boolean) entry.getValue());
            }
            else if (entry.getValue() instanceof Character)
            {
                buf.writeByte(TYPE_CHAR);
                buf.writeChar((Character) entry.getValue());
            }
            else if (entry.getValue() instanceof Float)
            {
                buf.writeByte(TYPE_FLOAT);
                buf.writeFloat((Float) entry.getValue());
            }
            else if (entry.getValue() instanceof Double)
            {
                buf.writeByte(TYPE_DOUBLE);
                buf.writeDouble((Double) entry.getValue());
            }
            else if (entry.getValue() instanceof LocalDateTime)
            {
                final String value = ((LocalDateTime) entry.getValue()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                buf.writeByte(TYPE_DATETIME);
                buf.writeUTF(value);
            }
            else if (entry.getValue() instanceof LocalDate)
            {
                final String value = ((LocalDate) entry.getValue()).format(DateTimeFormatter.ISO_LOCAL_DATE);
                buf.writeByte(TYPE_DATE);
                buf.writeUTF(value);
            }
            else if (entry.getValue() instanceof LocalTime)
            {
                final String value = ((LocalTime) entry.getValue()).format(DateTimeFormatter.ISO_LOCAL_TIME);
                buf.writeByte(TYPE_TIME);
                buf.writeUTF(value);
            }
            else
            {
                throw new IllegalStateException("Invalid content type: " + entry.getValue().getClass()); //$NON-NLS-1$
            }
        }
    }
    
}
