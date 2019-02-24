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

import java.io.FileNotFoundException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortException;

/**
 *
 * @author varun
 */
public class Fsm implements Runnable
{
    
    public final static int MODE_PGM_FLASH=0;
    public final static int MODE_CHIP_ERASE=1;
    public final static int PGM_EEPROM=2;
    public final static int READ_FLASH=3;
    public final static int READ_EEPROM=4;
    public final static int WRITE_LOCK=5;
    public final static int READ_LOCK=6;
    public final static int WRITE_FUSE=7;
    public final static int READ_FUSE=8;
    
    public final static int STATE_INITIAL=0;
    public final static int STATE_ENTER_PGM=1;
    public final static int STATE_ENABLE_PGM=2;
    public final static int STATE_CHIP_ERASE=3;
    
    
    
    
    int mode;
    CountDownLatch latch;
    BlockingQueue<Message> queue;
    Support sp;
    Avr avr;
    String file;
    Hex hex;
    
    public  Fsm(CountDownLatch latch,BlockingQueue<Message> qu,int md,Avr av,Hex hx)
    {
        this.latch = latch;
        this.queue = qu;
        this.mode = md;
        this.avr=av;
        this.hex=hx;
        this.sp = new Support();
    } 
     public  Fsm(CountDownLatch latch,BlockingQueue<Message> qu,int md,Avr av,String fl)
    {
        this.latch = latch;
        this.queue = qu;
        this.mode = md;
        this.avr=av;
        this.file=fl;
        this.sp = new Support();
    } 
    public void run() 
    {
        byte[] d = new byte[4];
        switch(mode)
        {
            case Fsm.MODE_PGM_FLASH:
                                    {
                                      try {
                                            sp.sleep(3000);
                                            //avr.enter_programming();
                                            d=readdata();
                                            if(d[3]!=(byte)0x91)
                                                break;
                                            avr.enable_programming();
                                            d=readdata();
                                            if(d[3]!=(byte)0x53)
                                                break;
                                            avr.chip_erase();                                           
                                            sp.sleep(20);
                                            d=readdata();
                                            if(d[3]!=(byte)0x08)
                                                break;
                                            //avr.toggle_reset();
                                            d=readdata();
                                            if(d[3]!=(byte)0x93)
                                                break;
                                            avr.leave_programming();
                                            d=readdata();
                                            if(d[3]!=(byte)0x92)
                                                break;
                                            //avr.enter_programming();
                                            d=readdata();
                                            if(d[3]!=(byte)0x91)
                                                break;
                                            avr.enable_programming();
                                            d=readdata();
                                            if(d[3]!=(byte)0x53)
                                                break;
                                            //avr.BurnFlash(hex.getdata(),hex.getbytecount(), hex.getaddress());
                                            avr.leave_programming();
                                            d=readdata();
                                            if(d[3]!=(byte)0x92)
                                                break;
                                          } 
                                       catch (SerialPortException ex) 
                                          {
                                             Logger.getLogger(Fsm.class.getName()).log(Level.SEVERE, null, ex);
                                          } 
                                       /*catch (FileNotFoundException ex) 
                                          {
                                             Logger.getLogger(Fsm.class.getName()).log(Level.SEVERE, null, ex);
                                          }*/
                                    }
                                    
                                    break;
        }
        latch.countDown();
    }
    
    private byte[] readdata()
    {
        Message msg;
        byte[] data = new byte[4];
        try
        {
            int i;
            for(i=0;i<4;i++)
            {              
                msg=queue.take();
                System.out.println(sp.bytesToHex(msg.getdata()));
                data[i]=msg.getdata();
            }
           // latch.countDown();
        }
        catch(Exception err)
        {
            err.printStackTrace();
        }
        return data;
    }
    
    
}
