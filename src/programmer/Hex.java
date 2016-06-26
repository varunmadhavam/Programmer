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

/**
 *
 * @author varun
 */
public class Hex 
{
    short start_address=0xFF;
    byte[] data;
    int bytecount;
    Support sp = new Support();
    
    public void BytesFromHex(String file) throws FileNotFoundException
    {
        Scanner scan;
        scan = new Scanner(new File("/home/varun/blinkhex/blink.hex"));
        int iter=0;
        String data_hex="";
        
        while(scan.hasNextLine())
        {
          String line = scan.nextLine();
          int length = line.length();
          String startbit=line.substring(0,1);         
          if(!":".equals(startbit))
           {
             System.out.println("Error parsing Hex file");
             break;
           }
          else
           {
             byte[] content = sp.toByteArray(line.substring(1,length));
             byte[] type  = Arrays.copyOfRange(content,3,4);
             int type_int  = sp.byteToInt(type,1);
             if(type_int==1)
             {
                 //System.out.println("Reached End Of file");
                 break;
             }
             else if(type_int==0)
             {
                byte[] count = Arrays.copyOfRange(content,0,1);
                byte[] address = Arrays.copyOfRange(content,1,3);
                byte[] data  = Arrays.copyOfRange(content,4,content.length-1);
                byte[] crc   = Arrays.copyOfRange(content,content.length-1,content.length);
                int bc = sp.byteToInt(count,1);
                if(iter==0)
                {
                 start_address=sp.byteToShort(address,2);
                 iter=1;
                }
                bytecount=bytecount+bc;
                data_hex=data_hex+line.substring(9,9+(bc*2));
                //System.out.println(line.substring(9,9+(bc*2)));               
              }
             else
             {
                 
             }
            }
        }
        //System.out.println(sp.bytesToHex(sp.shorttobytes(start_address)));
        //System.out.println(bytecount);
        data=sp.toByteArray(data_hex); 
        //System.out.println(sp.bytesToHex(data));
        //System.out.println(data.length);
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
