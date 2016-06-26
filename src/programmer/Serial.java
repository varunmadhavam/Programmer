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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 *
 * @author varun
 */
public class Serial implements SerialPortEventListener,Runnable
{
    SerialPort sp;
    BlockingQueue<Message> queue;
    CountDownLatch latch;
    Avr avr;
    Hex hex;
    public Serial(SerialPort serial,BlockingQueue<Message> qu,CountDownLatch lt,Avr av,Hex hx)
    {
        this.sp=serial;
        this.queue=qu;
        this.latch=lt;
        this.avr=av;
        this.hex=hx;
        
    }
    public void run()
    { 
        try 
        {    
            sp.openPort();
            sp.setParams(SerialPort.BAUDRATE_9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
            sp.addEventListener(this, SerialPort.MASK_RXCHAR);
            queue.clear();
            Thread y = new Thread(new Fsm(latch,queue,Fsm.MODE_PGM_FLASH,avr,hex));
            y.start();
            
        }
        catch (SerialPortException ex) 
        {
            Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
    
    @Override
    public void serialEvent(SerialPortEvent event) 
    {
        Message msg;
        if(event.isRXCHAR() && event.getEventValue() > 0) 
        {
            try 
            {  
                int i;
                byte[] a = sp.readBytes(event.getEventValue());
                for(i=0;i<a.length;i++)
                {
                    try 
                    {
                      msg=new Message(a[i]);
                      queue.put(msg);
                    } 
                    catch(InterruptedException ex)
                    {
                      Logger.getLogger(Programmer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                          
                }                
                
            } 
            catch (SerialPortException ex) 
            {
                Logger.getLogger(Programmer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }     
    
    
}
