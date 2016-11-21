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

import java.io.EOFException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.BiFunction;

import com.google.common.base.Charsets;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import de.minigameslib.mclib.shared.api.com.CommunicationEndpointId;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Helper class for network messages
 * 
 * @author mepeisen
 */
class NetMessage
{
    
    // Implementation hint: This class contains duplicate code from corresponding NetMessage class in mclib client forge mod
    
    /** the communication endpoint. */
    private CommunicationEndpointId endpoint;
    
    /** the data section. */
    private DataSection data;

    /**
     * 
     */
    public NetMessage()
    {
        // empty
    }

    /**
     * @param endpoint
     * @param data
     */
    public NetMessage(CommunicationEndpointId endpoint, DataSection data)
    {
        this.endpoint = endpoint;
        this.data = data;
    }

    /**
     * @return the endpoint
     */
    public CommunicationEndpointId getEndpoint()
    {
        return this.endpoint;
    }

    /**
     * @param endpoint the endpoint to set
     */
    public void setEndpoint(CommunicationEndpointId endpoint)
    {
        this.endpoint = endpoint;
    }

    /**
     * @return the data
     */
    public DataSection getData()
    {
        return this.data;
    }

    /**
     * @param data the data to set
     */
    public void setData(DataSection data)
    {
        this.data = data;
    }

    /**
     * Reads message from given input
     * @param buf
     * @param endpointFactory a function to receive endpoints by string
     */
    public void fromBytes(ByteArrayDataInput buf, BiFunction<String, String, CommunicationEndpointId> endpointFactory)
    {
        final String endpointclass = readUtf8(buf);
        final String endpointName = readUtf8(buf);
        this.endpoint = endpointFactory.apply(endpointclass, endpointName);
        
        this.data = new MemoryDataSection();
        try
        {
            while (true)
            {
                final String key = readUtf8(buf);
                final byte typeNum = buf.readByte();
                switch (typeNum)
                {
                    case TYPE_STRING:
                        this.data.set(key, this.readUtf8(buf));
                        break;
                    case TYPE_BYTE:
                        this.data.set(key, buf.readByte());
                        break;
                    case TYPE_SHORT:
                        this.data.set(key, buf.readShort());
                        break;
                    case TYPE_INT:
                        this.data.set(key, buf.readInt());
                        break;
                    case TYPE_LONG:
                        this.data.set(key, buf.readLong());
                        break;
                    case TYPE_BOOL:
                        this.data.set(key, buf.readBoolean());
                        break;
                    case TYPE_CHAR:
                        this.data.set(key, buf.readChar());
                        break;
                    case TYPE_FLOAT:
                        this.data.set(key, buf.readFloat());
                        break;
                    case TYPE_DOUBLE:
                        this.data.set(key, buf.readDouble());
                        break;
                    case TYPE_DATETIME:
                        this.data.set(key, LocalDateTime.parse(this.readUtf8(buf)));
                        break;
                    case TYPE_DATE:
                        this.data.set(key, LocalDate.parse(this.readUtf8(buf)));
                        break;
                    case TYPE_TIME:
                        this.data.set(key, LocalTime.parse(this.readUtf8(buf)));
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
            
            if (! (ex.getCause() instanceof EOFException))
            {
                // rethrow unknown exception
                throw ex;
            }
        }
    }
    
    /** type num. */
    private static final byte TYPE_STRING = 0;
    /** type num. */
    private static final byte TYPE_BYTE = 1;
    /** type num. */
    private static final byte TYPE_SHORT = 2;
    /** type num. */
    private static final byte TYPE_INT = 3;
    /** type num. */
    private static final byte TYPE_LONG = 4;
    /** type num. */
    private static final byte TYPE_BOOL = 5;
    /** type num. */
    private static final byte TYPE_CHAR = 6;
    /** type num. */
    private static final byte TYPE_FLOAT = 7;
    /** type num. */
    private static final byte TYPE_DOUBLE = 8;
    /** type num. */
    private static final byte TYPE_DATETIME = 9;
    /** type num. */
    private static final byte TYPE_DATE = 10;
    /** type num. */
    private static final byte TYPE_TIME = 11;

    /**
     * @param buf
     * @return utf8 string
     */
    private String readUtf8(ByteArrayDataInput buf)
    {
        int size = buf.readInt();
        if (size > 150000) throw new IllegalStateException("string to big; max 150000"); //$NON-NLS-1$
        byte[] strbuf = new byte[size];
        buf.readFully(strbuf);
        final String result = new String(strbuf, Charsets.UTF_8);
        return result;
    }

    /**
     * Converts this message to byte stream.
     * @param buf
     */
    public void toBytes(ByteArrayDataOutput buf)
    {
        final String endpointclass = this.endpoint.getClass().getName();
        final String endpointName = this.endpoint.name();
        buf.writeInt(endpointclass.length());
        buf.write(endpointclass.getBytes(Charsets.UTF_8));
        buf.writeInt(endpointName.length());
        buf.write(endpointName.getBytes(Charsets.UTF_8));
        
        for (final Map.Entry<String, Object> entry : this.data.getValues(true).entrySet())
        {
            buf.writeInt(entry.getKey().length());
            buf.write(entry.getKey().getBytes(Charsets.UTF_8));
            if (entry.getValue() instanceof String)
            {
                final String value = (String) entry.getValue();
                buf.writeByte(TYPE_STRING);
                buf.writeInt(value.length());
                buf.write(value.getBytes(Charsets.UTF_8));
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
                buf.writeInt(value.length());
                buf.write(value.getBytes(Charsets.UTF_8));
            }
            else if (entry.getValue() instanceof LocalDate)
            {
                final String value = ((LocalDate) entry.getValue()).format(DateTimeFormatter.ISO_LOCAL_DATE);
                buf.writeByte(TYPE_DATE);
                buf.writeInt(value.length());
                buf.write(value.getBytes(Charsets.UTF_8));
            }
            else if (entry.getValue() instanceof LocalTime)
            {
                final String value = ((LocalTime) entry.getValue()).format(DateTimeFormatter.ISO_LOCAL_TIME);
                buf.writeByte(TYPE_TIME);
                buf.writeInt(value.length());
                buf.write(value.getBytes(Charsets.UTF_8));
            }
            else
            {
                throw new IllegalStateException("Invalid content type: " + entry.getValue().getClass()); //$NON-NLS-1$
            }
        }
    }
    
}
