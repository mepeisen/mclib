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

package de.minigameslib.mclib.shared.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * Testing AnnotatedDataFragment
 * 
 * @author mepeisen
 */
public class AnnotatedDataFragmentTest
{
    
    /**
     * Simple test case.
     */
    @Test
    public void testStoreAndReload()
    {
        final Data1 d1 = new Data1();
        d1.int2 = 123;
        d1.str1 = "FOO"; //$NON-NLS-1$
        final Data2 d2 = new Data2();
        d2.int2 = 1234;
        d2.str1 = "FOO1"; //$NON-NLS-1$
        d2.int3 = Integer.valueOf(12);
        d1.lst4.add(d2);
        final MemoryDataSection section = new MemoryDataSection();
        d1.write(section.createSection("d1")); //$NON-NLS-1$
        
        final Data1 res = section.getFragment(Data1.class, "d1"); //$NON-NLS-1$
        assertEquals(d1.int2, res.int2);
        assertEquals(d1.int3, res.int3);
        assertEquals(d1.str1, res.str1);
        assertEquals(1, res.lst4.size());
        assertEquals(d1.lst4.get(0).int2, res.lst4.get(0).int2);
        assertEquals(d1.lst4.get(0).int3, res.lst4.get(0).int3);
        assertEquals(d1.lst4.get(0).str1, res.lst4.get(0).str1);
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testInheritance()
    {
        final Data3 d1 = new Data3();
        d1.int2 = 123;
        d1.str1 = "FOO"; //$NON-NLS-1$
        final Data2 d2 = new Data2();
        d2.int2 = 1234;
        d2.str1 = "FOO1"; //$NON-NLS-1$
        d2.int3 = Integer.valueOf(12);
        d1.lst4.add(d2);
        d1.str5 = "BAZ"; //$NON-NLS-1$
        final MemoryDataSection section = new MemoryDataSection();
        d1.write(section.createSection("d1")); //$NON-NLS-1$
        
        final Data3 res = section.getFragment(Data3.class, "d1"); //$NON-NLS-1$
        assertEquals(d1.int2, res.int2);
        assertEquals(d1.int3, res.int3);
        assertEquals(d1.str1, res.str1);
        assertEquals(d1.str5, res.str5);
        assertEquals(1, res.lst4.size());
        assertEquals(d1.lst4.get(0).int2, res.lst4.get(0).int2);
        assertEquals(d1.lst4.get(0).int3, res.lst4.get(0).int3);
        assertEquals(d1.lst4.get(0).str1, res.lst4.get(0).str1);
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testRef()
    {
        final DataRef ref = new DataRef();
        final MemoryDataSection section = new MemoryDataSection();
        section.set("ref", ref); //$NON-NLS-1$
        assertEquals(ref, section.getFragment(DataRef.class, "ref")); //$NON-NLS-1$
        
        ref.data1 = new Data1();
        ref.data1.int2 = 10;
        section.set("ref", ref); //$NON-NLS-1$
        assertEquals(ref, section.getFragment(DataRef.class, "ref")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testRefMap()
    {
        final DataRefMap ref = new DataRefMap();
        final MemoryDataSection section = new MemoryDataSection();
        section.set("ref", ref); //$NON-NLS-1$
        assertEquals(ref, section.getFragment(DataRefMap.class, "ref")); //$NON-NLS-1$
        
        final Data1 data1 = new Data1();
        ref.data1.put("FOO", data1); //$NON-NLS-1$
        data1.int2 = 10;
        section.set("ref", ref); //$NON-NLS-1$
        assertEquals(ref, section.getFragment(DataRefMap.class, "ref")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testAll()
    {
        final AllData data1 = AllData.result();
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data1); //$NON-NLS-1$
        assertEquals(data1, section.getFragment(AllData.class, "FOO")); //$NON-NLS-1$
    }
    
    /**
     * data class.
     */
    public static class Data1 extends AnnotatedDataFragment
    {
        
        /**
         * field.
         */
        @PersistentField
        public String str1;
        
        /**
         * field.
         */
        @PersistentField
        public int int2;
        
        /**
         * field.
         */
        @PersistentField
        public Integer int3;
        
        /**
         * field.
         */
        @PersistentField
        public List<Data2> lst4 = new ArrayList<>();

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + this.int2;
            result = prime * result + ((this.int3 == null) ? 0 : this.int3.hashCode());
            result = prime * result + ((this.lst4 == null) ? 0 : this.lst4.hashCode());
            result = prime * result + ((this.str1 == null) ? 0 : this.str1.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Data1 other = (Data1) obj;
            if (this.int2 != other.int2)
                return false;
            if (this.int3 == null)
            {
                if (other.int3 != null)
                    return false;
            }
            else if (!this.int3.equals(other.int3))
                return false;
            if (this.lst4 == null)
            {
                if (other.lst4 != null)
                    return false;
            }
            else if (!this.lst4.equals(other.lst4))
                return false;
            if (this.str1 == null)
            {
                if (other.str1 != null)
                    return false;
            }
            else if (!this.str1.equals(other.str1))
                return false;
            return true;
        }
    }
    
    /**
     * data class.
     */
    public static class Data2 extends AnnotatedDataFragment
    {
        
        /**
         * field.
         */
        @PersistentField
        public String str1;
        
        /**
         * field.
         */
        @PersistentField
        public int int2;
        
        /**
         * field.
         */
        @PersistentField
        public Integer int3;

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + this.int2;
            result = prime * result + ((this.int3 == null) ? 0 : this.int3.hashCode());
            result = prime * result + ((this.str1 == null) ? 0 : this.str1.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Data2 other = (Data2) obj;
            if (this.int2 != other.int2)
                return false;
            if (this.int3 == null)
            {
                if (other.int3 != null)
                    return false;
            }
            else if (!this.int3.equals(other.int3))
                return false;
            if (this.str1 == null)
            {
                if (other.str1 != null)
                    return false;
            }
            else if (!this.str1.equals(other.str1))
                return false;
            return true;
        }
    }
    
    /**
     * data class.
     */
    public static class Data3 extends Data1
    {
        /**
         * field.
         */
        @PersistentField
        public String str5;

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((this.str5 == null) ? 0 : this.str5.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            Data3 other = (Data3) obj;
            if (this.str5 == null)
            {
                if (other.str5 != null)
                    return false;
            }
            else if (!this.str5.equals(other.str5))
                return false;
            return true;
        }
    }
    
    /**
     * Test helper class
     */
    public static class DataRefMap extends AnnotatedDataFragment
    {
        
        /**
         * data1
         */
        @PersistentField
        public Map<String, Data1> data1 = new HashMap<>();

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.data1 == null) ? 0 : this.data1.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            DataRefMap other = (DataRefMap) obj;
            if (this.data1 == null)
            {
                if (other.data1 != null)
                    return false;
            }
            else if (!this.data1.equals(other.data1))
                return false;
            return true;
        }
        
    }
    
    /**
     * Test helper class
     */
    public static class DataRef extends AnnotatedDataFragment
    {
        
        /**
         * data1
         */
        @PersistentField
        public Data1 data1;

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.data1 == null) ? 0 : this.data1.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            DataRef other = (DataRef) obj;
            if (this.data1 == null)
            {
                if (other.data1 != null)
                    return false;
            }
            else if (!this.data1.equals(other.data1))
                return false;
            return true;
        }
        
    }
    
    /**
     * data class.
     */
    public static class AllData extends AnnotatedDataFragment
    {
        
        /**
         * Creates a test data value
         * @return test data value
         */
        public static AllData result()
        {
            final AllData result = new AllData();
            
            result.bfalse = false;
            result.btrue = true;
            
            result.date1 = LocalDate.of(2016, 1, 2);
            result.date2 = LocalDate.of(2016, 3, 4);
            result.datetime1 = LocalDateTime.of(2016, 1, 2, 5, 6);
            result.datetime2 = LocalDateTime.of(2016, 1, 2, 5, 7);
            result.time1 = LocalTime.of(1, 2, 3);
            result.time2 = LocalTime.of(3, 4, 5);
            
            result.maxByte = Byte.MAX_VALUE;
            result.maxChar = Character.MAX_VALUE;
            result.maxDouble = Double.MAX_VALUE;
            result.maxFloat = Float.MAX_VALUE;
            result.maxInt = Integer.MAX_VALUE;
            result.maxLong = Long.MAX_VALUE;
            result.maxShort = Short.MAX_VALUE;
            
            result.minByte = Byte.MIN_VALUE;
            result.minChar = Character.MIN_VALUE;
            result.minDouble = Double.MIN_VALUE;
            result.minFloat = Float.MIN_VALUE;
            result.minInt = Integer.MIN_VALUE;
            result.minLong = Long.MIN_VALUE;
            result.minShort = Short.MIN_VALUE;
            
            result.zeroByte = 0;
            result.zeroChar = 0;
            result.zeroDouble = 0;
            result.zeroFloat = 0;
            result.zeroInt = 0;
            result.zeroLong = 0;
            result.zeroShort = 0;
            
            return result;
        }
        
        // datetime
        
        /**
         * field.
         */
        @PersistentField
        public LocalDateTime datetime1;
        
        /**
         * field.
         */
        @PersistentField
        public LocalDateTime datetime2;
        
        /**
         * field.
         */
        @PersistentField
        public LocalDateTime datetimeNull;
        
        // time
        
        /**
         * field.
         */
        @PersistentField
        public LocalTime time1;
        
        /**
         * field.
         */
        @PersistentField
        public LocalTime time2;
        
        /**
         * field.
         */
        @PersistentField
        public LocalTime timeNull;
        
        // date
        
        /**
         * field.
         */
        @PersistentField
        public LocalDate date1;
        
        /**
         * field.
         */
        @PersistentField
        public LocalDate date2;
        
        /**
         * field.
         */
        @PersistentField
        public LocalDate dateNull;
        
        // boolean
        
        /**
         * field.
         */
        @PersistentField
        public boolean btrue;
        
        /**
         * field.
         */
        @PersistentField
        public boolean bfalse;
        
        /**
         * field.
         */
        @PersistentField
        public Boolean bnull;
        
        // char
        
        /**
         * field.
         */
        @PersistentField
        public char minChar;
        
        /**
         * field.
         */
        @PersistentField
        public char maxChar;
        
        /**
         * field.
         */
        @PersistentField
        public char zeroChar;
        
        /**
         * field.
         */
        @PersistentField
        public Character nullChar;
        
        // byte
        
        /**
         * field.
         */
        @PersistentField
        public byte minByte;
        
        /**
         * field.
         */
        @PersistentField
        public byte maxByte;
        
        /**
         * field.
         */
        @PersistentField
        public byte zeroByte;
        
        /**
         * field.
         */
        @PersistentField
        public Byte nullByte;
        
        // short
        
        /**
         * field.
         */
        @PersistentField
        public short minShort;
        
        /**
         * field.
         */
        @PersistentField
        public short maxShort;
        
        /**
         * field.
         */
        @PersistentField
        public short zeroShort;
        
        /**
         * field.
         */
        @PersistentField
        public Short nullShort;
        
        // int
        
        /**
         * field.
         */
        @PersistentField
        public int minInt;
        
        /**
         * field.
         */
        @PersistentField
        public int maxInt;
        
        /**
         * field.
         */
        @PersistentField
        public int zeroInt;
        
        /**
         * field.
         */
        @PersistentField
        public Integer nullInt;
        
        // long
        
        /**
         * field.
         */
        @PersistentField
        public long minLong;
        
        /**
         * field.
         */
        @PersistentField
        public long maxLong;
        
        /**
         * field.
         */
        @PersistentField
        public long zeroLong;
        
        /**
         * field.
         */
        @PersistentField
        public Long nullLong;
        
        // double
        
        /**
         * field.
         */
        @PersistentField
        public double minDouble;
        
        /**
         * field.
         */
        @PersistentField
        public double maxDouble;
        
        /**
         * field.
         */
        @PersistentField
        public double zeroDouble;
        
        /**
         * field.
         */
        @PersistentField
        public Double nullDouble;
        
        // float
        
        /**
         * field.
         */
        @PersistentField
        public float minFloat;
        
        /**
         * field.
         */
        @PersistentField
        public float maxFloat;
        
        /**
         * field.
         */
        @PersistentField
        public float zeroFloat;
        
        /**
         * field.
         */
        @PersistentField
        public Float nullFloat;

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + (this.bfalse ? 1231 : 1237);
            result = prime * result + ((this.bnull == null) ? 0 : this.bnull.hashCode());
            result = prime * result + (this.btrue ? 1231 : 1237);
            result = prime * result + ((this.date1 == null) ? 0 : this.date1.hashCode());
            result = prime * result + ((this.date2 == null) ? 0 : this.date2.hashCode());
            result = prime * result + ((this.dateNull == null) ? 0 : this.dateNull.hashCode());
            result = prime * result + ((this.datetime1 == null) ? 0 : this.datetime1.hashCode());
            result = prime * result + ((this.datetime2 == null) ? 0 : this.datetime2.hashCode());
            result = prime * result + ((this.datetimeNull == null) ? 0 : this.datetimeNull.hashCode());
            result = prime * result + this.maxByte;
            result = prime * result + this.maxChar;
            long temp;
            temp = Double.doubleToLongBits(this.maxDouble);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            result = prime * result + Float.floatToIntBits(this.maxFloat);
            result = prime * result + this.maxInt;
            result = prime * result + (int) (this.maxLong ^ (this.maxLong >>> 32));
            result = prime * result + this.maxShort;
            result = prime * result + this.minByte;
            result = prime * result + this.minChar;
            temp = Double.doubleToLongBits(this.minDouble);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            result = prime * result + Float.floatToIntBits(this.minFloat);
            result = prime * result + this.minInt;
            result = prime * result + (int) (this.minLong ^ (this.minLong >>> 32));
            result = prime * result + this.minShort;
            result = prime * result + ((this.nullByte == null) ? 0 : this.nullByte.hashCode());
            result = prime * result + ((this.nullChar == null) ? 0 : this.nullChar.hashCode());
            result = prime * result + ((this.nullDouble == null) ? 0 : this.nullDouble.hashCode());
            result = prime * result + ((this.nullFloat == null) ? 0 : this.nullFloat.hashCode());
            result = prime * result + ((this.nullInt == null) ? 0 : this.nullInt.hashCode());
            result = prime * result + ((this.nullLong == null) ? 0 : this.nullLong.hashCode());
            result = prime * result + ((this.nullShort == null) ? 0 : this.nullShort.hashCode());
            result = prime * result + ((this.time1 == null) ? 0 : this.time1.hashCode());
            result = prime * result + ((this.time2 == null) ? 0 : this.time2.hashCode());
            result = prime * result + ((this.timeNull == null) ? 0 : this.timeNull.hashCode());
            result = prime * result + this.zeroByte;
            result = prime * result + this.zeroChar;
            temp = Double.doubleToLongBits(this.zeroDouble);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            result = prime * result + Float.floatToIntBits(this.zeroFloat);
            result = prime * result + this.zeroInt;
            result = prime * result + (int) (this.zeroLong ^ (this.zeroLong >>> 32));
            result = prime * result + this.zeroShort;
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            AllData other = (AllData) obj;
            if (this.bfalse != other.bfalse)
                return false;
            if (this.bnull == null)
            {
                if (other.bnull != null)
                    return false;
            }
            else if (!this.bnull.equals(other.bnull))
                return false;
            if (this.btrue != other.btrue)
                return false;
            if (this.date1 == null)
            {
                if (other.date1 != null)
                    return false;
            }
            else if (!this.date1.equals(other.date1))
                return false;
            if (this.date2 == null)
            {
                if (other.date2 != null)
                    return false;
            }
            else if (!this.date2.equals(other.date2))
                return false;
            if (this.dateNull == null)
            {
                if (other.dateNull != null)
                    return false;
            }
            else if (!this.dateNull.equals(other.dateNull))
                return false;
            if (this.datetime1 == null)
            {
                if (other.datetime1 != null)
                    return false;
            }
            else if (!this.datetime1.equals(other.datetime1))
                return false;
            if (this.datetime2 == null)
            {
                if (other.datetime2 != null)
                    return false;
            }
            else if (!this.datetime2.equals(other.datetime2))
                return false;
            if (this.datetimeNull == null)
            {
                if (other.datetimeNull != null)
                    return false;
            }
            else if (!this.datetimeNull.equals(other.datetimeNull))
                return false;
            if (this.maxByte != other.maxByte)
                return false;
            if (this.maxChar != other.maxChar)
                return false;
            if (Double.doubleToLongBits(this.maxDouble) != Double.doubleToLongBits(other.maxDouble))
                return false;
            if (Float.floatToIntBits(this.maxFloat) != Float.floatToIntBits(other.maxFloat))
                return false;
            if (this.maxInt != other.maxInt)
                return false;
            if (this.maxLong != other.maxLong)
                return false;
            if (this.maxShort != other.maxShort)
                return false;
            if (this.minByte != other.minByte)
                return false;
            if (this.minChar != other.minChar)
                return false;
            if (Double.doubleToLongBits(this.minDouble) != Double.doubleToLongBits(other.minDouble))
                return false;
            if (Float.floatToIntBits(this.minFloat) != Float.floatToIntBits(other.minFloat))
                return false;
            if (this.minInt != other.minInt)
                return false;
            if (this.minLong != other.minLong)
                return false;
            if (this.minShort != other.minShort)
                return false;
            if (this.nullByte == null)
            {
                if (other.nullByte != null)
                    return false;
            }
            else if (!this.nullByte.equals(other.nullByte))
                return false;
            if (this.nullChar == null)
            {
                if (other.nullChar != null)
                    return false;
            }
            else if (!this.nullChar.equals(other.nullChar))
                return false;
            if (this.nullDouble == null)
            {
                if (other.nullDouble != null)
                    return false;
            }
            else if (!this.nullDouble.equals(other.nullDouble))
                return false;
            if (this.nullFloat == null)
            {
                if (other.nullFloat != null)
                    return false;
            }
            else if (!this.nullFloat.equals(other.nullFloat))
                return false;
            if (this.nullInt == null)
            {
                if (other.nullInt != null)
                    return false;
            }
            else if (!this.nullInt.equals(other.nullInt))
                return false;
            if (this.nullLong == null)
            {
                if (other.nullLong != null)
                    return false;
            }
            else if (!this.nullLong.equals(other.nullLong))
                return false;
            if (this.nullShort == null)
            {
                if (other.nullShort != null)
                    return false;
            }
            else if (!this.nullShort.equals(other.nullShort))
                return false;
            if (this.time1 == null)
            {
                if (other.time1 != null)
                    return false;
            }
            else if (!this.time1.equals(other.time1))
                return false;
            if (this.time2 == null)
            {
                if (other.time2 != null)
                    return false;
            }
            else if (!this.time2.equals(other.time2))
                return false;
            if (this.timeNull == null)
            {
                if (other.timeNull != null)
                    return false;
            }
            else if (!this.timeNull.equals(other.timeNull))
                return false;
            if (this.zeroByte != other.zeroByte)
                return false;
            if (this.zeroChar != other.zeroChar)
                return false;
            if (Double.doubleToLongBits(this.zeroDouble) != Double.doubleToLongBits(other.zeroDouble))
                return false;
            if (Float.floatToIntBits(this.zeroFloat) != Float.floatToIntBits(other.zeroFloat))
                return false;
            if (this.zeroInt != other.zeroInt)
                return false;
            if (this.zeroLong != other.zeroLong)
                return false;
            if (this.zeroShort != other.zeroShort)
                return false;
            return true;
        }
        
    }
    
}
