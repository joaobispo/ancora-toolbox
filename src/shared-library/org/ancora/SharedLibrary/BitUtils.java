/*
 *  Copyright 2009 Ancora Research Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package org.ancora.SharedLibrary;

/**
 * Methods for bit manipulation.
 *
 * @author Joao Bispo
 */
public class BitUtils {

   /**
    * Pads the string with zeros on the left until it has the
    * requested size, and prefixes "0x" to the resulting String.
    *
    * <p>Example:
    * <br>Input - padHexString(A6, 4)
    * <br>Output - 0x00A6.
    *
    * @param hexNumber an hexadecimal number in String format.
    * @param size the pretended number of digits of the hexadecimal number.
    * @return a string
    */
   public static String padHexString(String hexNumber, int size) {
      int stringSize = hexNumber.length();

      if (stringSize >= size) {
         return hexNumber;
      }

      int numZeros = size - stringSize;
      StringBuilder builder = new StringBuilder(numZeros + HEX_PREFIX.length());
      builder.append(HEX_PREFIX);
      for (int i = 0; i < numZeros; i++) {
         builder.append(ZERO);
      }

      return builder.toString() + hexNumber;
   }

   /**
    * Pads the string with zeros on the left until it has the requested size.
    *
    * <p>Example:
    * <br>Input - padBinaryString(101, 5)
    * <br>Output - 00101.
    *
    * @param binaryNumber a binary number in String format.
    * @param size the pretended number of digits of the binary number.
    * @return a string
    */
   public static String padBinaryString(String binaryNumber, int size) {
       int stringSize = binaryNumber.length();
       if(stringSize >= size) {
           return binaryNumber;
       }

       int numZeros = size - stringSize;
       StringBuilder builder = new StringBuilder(numZeros);
       for(int i=0; i<numZeros; i++) {
           builder.append(ZERO);
       }

       return builder.toString() + binaryNumber;
   }

   /**
    * Gets the a single bit of the integer target.
    *
    * @param position a number between 0 and 31, inclusive
    * @param target an integer
    * @return 1 if the bit at the specified position is 1; 0 otherwise
    */
   public static int getBit(int position, int target) {
      return (target >>> position) & MASK_BIT_1;
   }

   /**
    * Returns an integer representing the 16 bits from the long number from a
    * specified offset.
    * 
    * @param data a long number
    * @param offset a number between 0 and 3, inclusive
    * @return an int representing the 16 bits of the specified offset
    */
   public static int get16BitsAligned(long data, int offset) {
      // Normalize offset
      offset = offset%4;
      //System.out.println("offset:"+offset);
      // Align the mask
      long mask = MASK_16_BITS << 16*offset;
      //System.out.println("Mask:"+Long.toHexString(mask));
      //System.out.println("Data:"+Long.toHexString(data));

      // Get the bits
      long result = data & mask;

      // Put bits in position
      return (int) (result >>> (16*offset));
   }

   /**
    * Paul Hsieh's Hash Function, for long numbers.
    *
    * @param data data to hash
    * @param hash previous value of the hash. If this it is the start of the
    * method, a recomended value to use is the length of the data. In this case
    * because it is a long use the number 8 (8 bytes).
    * @return a hash value
    */
   public static int superFastHash(long data, int hash) {
      int tmp;
      //int rem;

      //if (len <= 0) {
      //   return 0;
      //}

      //rem = len & 3;
      //len >>= 2;

      //Main Loop
      for (int i = 0; i < 4; i += 2) {
         // Get lower 16 bits
         hash += BitUtils.get16BitsAligned(data, i);
         // Calculate some random value with second-lower 16 bits
         tmp = (BitUtils.get16BitsAligned(data, i + 1) << 11) ^ hash;
         hash = (hash << 16) ^ tmp;
         // At this point, it would advance the data, but since it is restricted
         // to longs (64-bit values), it is unnecessary).
         hash += hash >> 11;
      }

      // Handle end cases //
      // There are no end cases, main loop is done in chuncks of 32 bits.

      // Force "avalanching" of final 127 bits //
      hash ^= hash << 3;
      hash += hash >> 5;
      hash ^= hash << 4;
      hash += hash >> 17;
      hash ^= hash << 25;
      hash += hash >> 6;

      return hash;
   }
   /**
    * Paul Hsieh's Hash Function, for int numbers.
    *
    * @param data data to hash
    * @param hash previous value of the hash. If this it is the start of the
    * method, a recomended value to use is the length of the data. In this case
    * because it is an integer use the number 4 (4 bytes).
    * @return a hash value
    */
   public static int superFastHash(int data, int hash) {
      int tmp;
      //int rem;

      //if (len <= 0) {
      //   return 0;
      //}

      //rem = len & 3;
      //len >>= 2;

      //Main Loop
      for (int i = 0; i < 2; i += 2) {
         // Get lower 16 bits
         hash += BitUtils.get16BitsAligned(data, i);
         // Calculate some random value with second-lower 16 bits
         tmp = (BitUtils.get16BitsAligned(data, i + 1) << 11) ^ hash;
         hash = (hash << 16) ^ tmp;
         // At this point, it would advance the data, but since it is restricted
         // to longs (64-bit values), it is unnecessary).
         hash += hash >> 11;
      }

      // Handle end cases //
      // There are no end cases, main loop is done in chuncks of 32 bits.

      // Force "avalanching" of final 127 bits //
      hash ^= hash << 3;
      hash += hash >> 5;
      hash ^= hash << 4;
      hash += hash >> 17;
      hash ^= hash << 25;
      hash += hash >> 6;

      return hash;
   }

   /**
    * Sets a specific bit of an int.
    *
    * @param bit the bit to set. The least significant bit is bit 0
    * @param target the integer where the bit will be set
    * @return the updated value of the target
    */
   public static int setBit(int bit, int target) {
      // Create mask
      int mask = 1 << bit;
      // Set bit
      return target | mask;
   }


   ///
   // CONSTANTS
   ///
   private static final String ZERO = "0";
   private static final String HEX_PREFIX = "0x";
   private static final long MASK_16_BITS = 0xFFFFL;
   private static final int MASK_BIT_1 = 0x1;
}
