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
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author varun
 */
public class Support 
{
   
    public int byteToInt(byte[] bytes, int length) 
    {
        int val = 0;
        if(length>4) throw new RuntimeException("Too big to fit in int");
        for (int i = 0; i < length; i++) 
        {
            val=val<<8;
            val=val|(bytes[i] & 0xFF);
        }
        return val;
    }
    
    public short byteToShort(byte[] bytes, int length) 
    {
        short val = 0;
        if(length>2) throw new RuntimeException("Too big to fit in short");
        for (int i = 0; i < length; i++) 
        {
            val=(short) (val<<8);
            val=(short) (val|(bytes[i] & 0xFF));
        }
        return val;
    }
    
    public byte[] toByteArray(String s) 
    {
     return DatatypeConverter.parseHexBinary(s);
    }
    
    public String bytesToHex(byte[] bytes) 
    {
     final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
     char[] hexChars = new char[bytes.length * 2];
     int v;
     for ( int j = 0; j < bytes.length; j++ ) 
     {
        v = bytes[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
     }
     return new String(hexChars);
    }
    
    public String bytesToHex(byte bytes) 
    {
     final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
     char[] hexChars = new char[2];
     int v;
     v = bytes& 0xFF;
     hexChars[0] = hexArray[v >>> 4];
     hexChars[1] = hexArray[v & 0x0F];
     return new String(hexChars);
    }
    
    
    public List<Byte> readHex(String file) throws FileNotFoundException
    {
        List<Byte> list = new ArrayList<>();
        Scanner scan;
        scan = new Scanner(new File(file));
        while(scan.hasNextLine())
        {
         String line = scan.nextLine();
         int length = line.length();
         String startbit=line.substring(0,1);
         byte[] content = toByteArray(line.substring(1,length));
         byte[] count = Arrays.copyOfRange(content,0,1);
         byte[] address = Arrays.copyOfRange(content,1,3);
         byte[] type  = Arrays.copyOfRange(content,3,4);
         int bytecount = byteToInt(count,1);
         int type_int  = byteToInt(type,1); 
         if(startbit!=":")
         {
             list=null;
             break;
         }
         else
         {
             if(type_int==0)
                 break;
             else
             {
                 
             }
         }
         System.out.println(Long.toString(type_int));
        }
        
        return list;
    }
    
    public byte[] inttobyte(int i){
        byte[] bytes = ByteBuffer.allocate(4).putInt(i).array();
        return bytes;
    }
    
    public byte[] shorttobytes(short i){
        byte[] bytes = ByteBuffer.allocate(2).putShort(i).array();
        return bytes;
    }
    
    void sleep(int time)
    {
        try 
        {
            Thread.sleep(time);
        } 
        catch (InterruptedException ex) 
        {
            Logger.getLogger(Fsm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
