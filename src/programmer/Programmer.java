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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import jssc.*;

/**
 *
 * @author varun
 */
public class Programmer
{
    /*public static void main(String[] args) throws InterruptedException, SerialPortException, FileNotFoundException
    {    
        SerialPort serialPort = new SerialPort("/dev/ttyACM0");
        BlockingQueue<Message> queue = new ArrayBlockingQueue<>(100);
        CountDownLatch latch = new CountDownLatch(1);
        Avr avr= new Avr("Atmega8",new byte[]{(byte)0x1E,(byte)0x93,(byte)0x07},32,4,4096,512,10,10,10,10,serialPort);
        Hex hex = new Hex();
        hex.BytesFromHexFile("");
        int mode=0;
        Thread x = new Thread(new Serial(serialPort,queue,latch,avr,hex));
        x.start();
        //Test test = new Test(avr);
        //test.runtest();
        latch.await();
        serialPort.removeEventListener();        
    }*/
    
    public static void main(String[] args) throws FileNotFoundException, Exception
    {
        Test test = new Test();
        test.runtest();
    }

}

