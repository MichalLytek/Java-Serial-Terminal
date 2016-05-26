package pl.polsl.pl.java.serial.terminal.model;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import pl.polsl.pl.java.serial.terminal.main.Controler;

/**
 * Custom implementation of SerialPortEventListener, handle low level logic.
 * It handle receiving of all chars and decide when to put char in buffer
 * or show the buffer content on the screen.
 * 
 * @see SerialPortEventListener
 * @author Michał Lytek
 */
public class SerialPortReader implements SerialPortEventListener {
    
    /** Instance of connected SerialPort to read from */
    private SerialPort serialPort;
    /** Termination string (need to detect end of statement) */
    private String terminatorToInsert;
    /** Instance of Controler class */
    private Controler controler;
    
    /** Received characters buffer */
    private List<Byte> byteBufferList;
    /** Last received char */
    private byte lastCharacter = 0;

    /**
     * The only one constructor for this custom event listener.
     * The parameters are needed to read value from serial port,
     * decide when termination char(s) received
     * and be able to show chars on the screen.
     * 
     * @param serialPort the instance of connected SerialPort from controler
     * @param terminator the termination string
     * @param controler the instance of MVC controler to inform about received chars
     */
    public SerialPortReader(SerialPort serialPort, String terminator, Controler controler) {
        this.serialPort = serialPort;
        this.terminatorToInsert = terminator;
        this.controler = controler;
        
        this.byteBufferList = new ArrayList<>();
    }

    /**
     * Overrided method, lauched on every serial port event.
     * It read chars from serial port to the buffer and looks for the terminator char.
     * Then it shows the buffer content in GUI.
     * NOTICE: when there's no termination char, it shows chars in GUI one by one!
     * 
     * @param serialPortEvent 
     */
    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        // if data is available
        if (serialPortEvent.isRXCHAR()) {
            try {
                byte[] receivedChars = serialPort.readBytes();
                if (receivedChars != null) {
                    // at first look for ping request
                    for (byte character : receivedChars) {
                            if (character == 6) { //ACK
                                controler.receivedPingResponse();
                                return;
                            } else if (character == 5) { //ENQ
                                serialPort.writeByte((byte) 6); //ACK
                                return;
                            }
                    }
                    if (terminatorToInsert.equalsIgnoreCase("")) {
                        controler.receivedNewChars(new String(receivedChars));
                    } else {
                        for (byte character : receivedChars) {
                            if (isTheSingleTerminationChar(character) || isTheSecondTerminationChar(character)) {
                                if (isTheSecondTerminationChar(character)) {
                                    // remove the first sign of terminator
                                    byteBufferList.remove(byteBufferList.size() - 1);
                                }
                                
                                // convert char list to array
                                byte[] array = new byte[byteBufferList.size()];
                                int i = 0;
                                for (Byte current : byteBufferList) {
                                    array[i++] = current;
                                }
                                byteBufferList.clear();
                                String receivedText = new String(array, StandardCharsets.US_ASCII);
                                controler.receivedNewLine(receivedText);
//                                switch (receivedText) {
//                                    case "*PING":
//                                        serialPort.writeByte((byte) 5); //ENQ
////                                        controler.sendText("*PONG");
//                                        break;
//                                    case "*PONG":
//                                        controler.receivedPingResponse();
//                                        break;
//                                    default:
//                                        controler.receivedNewLine(receivedText);
//                                        break;
//                                }
                                
                            } else {
                                byteBufferList.add(character);
                            }
                            lastCharacter = character;
                        }
                    }
                }
            } catch (SerialPortException ex) {
                System.err.println(ex);
            }
        }
        
        if (serialPortEvent.isCTS()) {
            boolean ctsState = (serialPortEvent.getEventValue() == 1);
            controler.showCTS(ctsState);
            System.err.println("CTS");
        }
        
        if (serialPortEvent.isDSR()) {
            boolean dsrState = (serialPortEvent.getEventValue() == 1);
            controler.showDSR(dsrState);
            System.err.println("DSR");
        }
    }
    
    /**
     * Checks if the char is the second char of the 2-sign terminator
     * and the previous char was the first char of the terminator.
     * 
     * @param character the examined char
     * 
     * @return true if it is termination char, false if not
     */
    private boolean isTheSecondTerminationChar(byte character) {
        return (terminatorToInsert.length() == 2 && lastCharacter == terminatorToInsert.getBytes()[0] && character == terminatorToInsert.getBytes()[1]);
    }
    
    /**
     * Checks if the char is the termination char of single sign terminator.
     * 
     * @param character the examined char
     * 
     * @return true if it is termination char, false if not
     */
    private boolean isTheSingleTerminationChar(byte character) {
        return (terminatorToInsert.length() == 1 && character == terminatorToInsert.getBytes()[0]);
    }
}
