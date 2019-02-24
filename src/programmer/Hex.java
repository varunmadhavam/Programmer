/*
 * The MIT License
 *
 * Copyright 2016 varun.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package programmer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 *
 * @author varun
 */
public class Hex 
{
    short   start_address;
    byte[]  data;
    int     bytecount;
    Support sp;

    public  Hex()
    {
        start_address       =   0xFF;
        sp                  =   new Support();
    }
    
    public void BytesFromHexFile(String file) throws FileNotFoundException
    {
        Scanner scan;
        scan                =   new Scanner(new File("/home/varun/coding/c/avr/blinky/bin/blinky.hex"));
        boolean isFirstLine =   true;
        String  data_hex    =   ""; //stores the entire hex file bytes as a string to be loaded later.
        int     bc          =   0;
        byte[]  content     =   {0x00};
        byte[]  type;
        String  recordType;
        String  line;
        int     length;
        String  startbit;
        byte[]  count       =   {0x00};
        byte[]  address     =   {0x00};
        byte[]  data;
        byte[]  crc;

        while(scan.hasNextLine())//iterate over each line of the hex file.
        {
          line      =   scan.nextLine();
          length    =   line.length();
          startbit  =   line.substring(0,1);

          if(!":".equals(startbit))// all lines of the hex file starts with a :
           {
             System.out.println("Error parsing Hex file..Line not starting with a :");
             System.exit(0);
           }
          else
           {

             recordType  = line.substring(7,9);//substring [7][8] of the line which is the record type. 9 is not included in substring.
             if(recordType.equals("01"))// end of file record type.
             {
                 //System.out.println("Reached End Of file");
                 if(sp.byteToInt(address,2)==(bytecount-sp.byteToInt(count,1)))
                 {
                     System.out.println("Basic sanity check on hex read passed..!");
                     break;
                 }
                 else
                 {
                     System.out.println("The basic sanity check on reading hex failed....Exiting");
                     System.exit(1);
                 }

             }
             else if(recordType.equals("00"))
             {
                content  = sp.toByteArray(line.substring(1,length));
                count    = Arrays.copyOfRange(content,0,1);
                address  = Arrays.copyOfRange(content,1,3);
                //data   = Arrays.copyOfRange(content,4,content.length-1);
                //crc    = Arrays.copyOfRange(content,content.length-1,content.length);
                bc       = sp.byteToInt(count,1);

                if(isFirstLine)
                {
                    start_address=sp.byteToShort(address,2);
                    isFirstLine=false;
                }

                bytecount=bytecount+bc;
                data_hex=data_hex+line.substring(9,9+(bc*2));
                //System.out.println(line.substring(9,9+(bc*2)));
              }
             else
             {
                System.out.println("Unhandled record type in hex file. Exiting");
                exit(1);
             }
            }
        }
        //System.out.println(sp.bytesToHex(sp.shorttobytes(start_address)));
        //System.out.println(bytecount);
        data=sp.toByteArray(data_hex); 
        //System.out.println(data_hex);
    }
    
    public byte[] getdata()
    {
        return this.data;
    }
    
    public int getbytecount()
    {
        return this.bytecount;
    }
    
    public short getaddress()
    {
        return this.start_address;
    }
}
