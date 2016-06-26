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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import jssc.SerialPort;
import jssc.SerialPortException;
/**
 *
 * @author varun
 */
public class Avr 
{
    public String chipname;
    public byte[] chipcode = new byte[3];
    int wordsize_flash;
    int wordsize_eeprom;
    int flash_size;
    int eeprom_size;
    int twd_fuse;
    int twd_flash;
    int twd_eeprom;
    int twd_erase;
    SerialPort serialPort;
    Support support=new Support();
    int current_state;
    
    public Avr(String name,byte[] code,int wsf,int wse,int fs,int es,
            int twdfu,int twdfl,int twdee,int twder,SerialPort serial)
    {
        this.chipname=name;
        this.chipcode=code;
        this.wordsize_flash=wsf;
        this.wordsize_eeprom=wse;
        this.flash_size=fs;
        this.eeprom_size=es;
        this.twd_fuse=twdfu;
        this.twd_flash=twdfl;
        this.twd_eeprom=twdee;
        this.twd_erase=twder;
        this.serialPort=serial;
        //this.support=new Support();
        
    }
    
    public Avr(SerialPort serial)
    {
        this.serialPort=serial;
        
    }      
    
    public void enter_programming() throws SerialPortException
    {
        System.out.println("enter_programming() :- Enter Programming.");
        serialPort.writeInt(0x91);
    }
    
    public void enable_programming() throws SerialPortException
    {
          serialPort.writeIntArray(new int[]{0xAC,0x53,0x00,0x00});
       
    }
    
    public void leave_programming() throws SerialPortException
    {
        System.out.println("leave_programming() :- Leaving Programming.");
        serialPort.writeInt(0x92);
    }
    
    public void toggle_reset() throws SerialPortException
    {
        System.out.println("toggle_reset() :- Toggling reset.");
        serialPort.writeInt(0x93);
    }
    public void chip_erase() throws SerialPortException
    {
        System.out.println("chip_erase() :- Strating Chip Erase Cycle.");
        serialPort.writeIntArray(new int[]{0xAC,0x80,0x00,0x00});
    }
    
    public void read_signature() throws SerialPortException
    {
        System.out.println("read_signature() :- Strating Chip Signature Read.");
        serialPort.writeIntArray(new int[]{0x30,0x00,0x00,0x00,0x30,0x00,0x01,0x00,0x30,0x00,0x02,0x00});
    }
    
    public void read_fuse_low() throws SerialPortException
    {
        System.out.println("read_fuse_low() :- Read fuse low byte.");
        serialPort.writeIntArray(new int[]{0x50,0x00,0x00,0x00});
    }
    
    public void read_fuse_high() throws SerialPortException
    {
        System.out.println("read_fuse_high() :- Read fuse high byte.");
        serialPort.writeIntArray(new int[]{0x58,0x08,0x00,0x00});
    }
    
    public void read_lock() throws SerialPortException
    {
        System.out.println("read_lock() :- Read Lock byte.");
        serialPort.writeIntArray(new int[]{0x58,0x00,0x00,0x00});
    }
    
    public void load_memory_page(byte address,byte low,byte high) throws SerialPortException
    {
        System.out.println("load_memory_page() :- LOading memory page");
        serialPort.writeIntArray(new int[]{0x40,0x00,address,low});
        serialPort.writeIntArray(new int[]{0x48,0x00,address,high});
    }
    
    public void write_memory_page(byte[] address) throws SerialPortException
    {
        System.out.println("load_memory_page() :- LOading memory page");
        serialPort.writeIntArray(new int[]{0x4C,address[0],address[1],0x00});
    }
    
    
    public void BurnFlash(byte[] data,int bytecount,short address) throws FileNotFoundException, SerialPortException
    {
        int i;
        int wordsize=0;
        short pageadd=address;
        byte wordadd=0x00;
        if(bytecount>flash_size*2)
        {
            
        }
        else
        {
            for(i=0;i<bytecount;i+=2)
            {
                load_memory_page(wordadd,data[i],data[i+1]);
                System.out.println("Load Memory @ "+support.bytesToHex(wordadd)+
                        " with low byte : "+support.bytesToHex(data[i])+" and high byte : "
                        +support.bytesToHex(data[i+1]));
                wordsize=wordsize+1;
                wordadd=(byte)(wordadd+0x01);
                if(wordsize==wordsize_flash)
                {
                    write_memory_page(support.shorttobytes(pageadd));
                    System.out.println("Write page at adress : "+support.bytesToHex(support.shorttobytes(pageadd)));
                    pageadd=(short)(pageadd+wordsize_flash);
                    wordadd=0x00;
                    wordsize=0;
                    support.sleep(20);
                }
            }
            if(wordsize<wordsize_flash)
            {
                write_memory_page(support.shorttobytes(pageadd));
                System.out.println("Write page at adress : "+support.bytesToHex(support.shorttobytes(pageadd)));
            }
        }
        
    }
      
    
}
