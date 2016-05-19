package pl.polsl.pl.java.serial.terminal.main;

import java.nio.charset.StandardCharsets;

import java.util.Timer;
import java.util.TimerTask;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import pl.polsl.pl.java.serial.terminal.model.CustomSerialPortEventListener;
import pl.polsl.pl.java.serial.terminal.view.MainFrame;

/**
 * The most important class in the app, controls all logic.
 * Share connection parametrs for the GUI, handle sending request,
 * setting up serial connection and testing it.
 * 
 * @author Michał Lytek
 */
public class Controler {

    /** Instance of GUI class */
    private MainFrame view;
    /** Instance of serial port */
    private SerialPort serialPort;
    
    /** Ping start time */
    private long start_time;
    /** Ping duration time */
    private int pingResult;
    
    /* All variables stores connection parameters */
    private String terminatorToShow,
            terminatorToInsert;
    private int baudRate,
            signBits,
            stopBits,
            parity,
            flowControlMask;
    private String signFormat;
    private String serialPortName;

    /**
     * Creates the controler, initates the GUI and ping test fields.
     */
    public Controler() {
        this.view = new MainFrame(this);
        
        // prevent field be a nullpointer
        this.start_time = System.nanoTime();
        this.pingResult = -1;
    }
    
    /**
     * Setting up a serial port connection parameters
     * - just store received values in configuration fields.
     * 
     * @param portName the system name of selected serial port
     * @param portSpeed the port baudrate
     * @param signBits the number of bits for each sign - 7 or 8
     * @param parityControl the parity control setting - index of combobox
     * @param stopBits the bumber of stop bits - 1 or 2
     * @param flowControl the flow control setting - index of combobox
     * @param terminator the terminator setting or apporopiate terminator
     */
    public void setupSerialPort(String portName, int portSpeed, int signBits, int parityControl, int stopBits, int flowControl, String terminator) {
        this.serialPortName = portName;
        this.baudRate = portSpeed;
        this.signBits = signBits;
        this.stopBits = stopBits;
        this.terminatorToShow = terminator;

        // parse sign setting to text representation
        this.signFormat = Integer.toString(signBits);
        switch (parityControl) {
            case 0:
                this.parity = SerialPort.PARITY_NONE;
                this.signFormat += "N";
                break;
            case 1:
                this.parity = SerialPort.PARITY_EVEN;
                this.signFormat += "E";
                break;
            case 2:
                this.parity = SerialPort.PARITY_ODD;
                this.signFormat += "O";
                break;
            default:
                this.parity = SerialPort.PARITY_NONE;
        }
        this.signFormat += Integer.toString(stopBits);

        // parse flow control settings to the serial port mask
        switch (flowControl) {
            case 0:
                this.flowControlMask = SerialPort.FLOWCONTROL_NONE;
                break;
            case 1:
                this.flowControlMask = SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT;
                break;
            case 2:
                this.flowControlMask = SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT;
                break;
            default:
                this.flowControlMask = SerialPort.FLOWCONTROL_NONE;
        }

        // parse terminator settings to appropriate format
        switch (terminator) {
            case "Brak":
                this.terminatorToInsert = "";
                break;
            case "CR":
                this.terminatorToInsert = "\r";
                break;
            case "LF":
                this.terminatorToInsert = "\n";
                break;
            case "CR-LF":
                this.terminatorToInsert = "\r\n";
                break;
            default:
                this.terminatorToInsert = terminator;
                break;
        }

        view.showConnectionParameters();
    }
    
    /**
     * Perform connection to the port using earlier defined parameters.
     * 
     * @return true if connected succesfuly, false if error occured
     */
    public boolean connectToPort() {
        try {
            this.serialPort = new SerialPort(this.serialPortName);
            this.serialPort.openPort();
            this.serialPort.setParams(this.baudRate, this.signBits, this.stopBits, this.parity);
            this.serialPort.setFlowControlMode(this.flowControlMask);
            this.serialPort.addEventListener(new CustomSerialPortEventListener(serialPort, terminatorToInsert, this));
        } catch (SerialPortException ex) {
            System.err.println(ex);
            return false;
        }
        return true;
    }

    /**
     * Disconnect from an earlier connected port.
     * 
     * @return true if disconected, false if error occured
     */
    public boolean disconnectFromPort() {
        try {
            this.serialPort.closePort();
        } catch (SerialPortException ex) {
            System.err.println(ex);
            return false;
        }
        return true;
    }
    
    /**
     * Perform ping test of serial connection.
     * Send ENQ sign, set start time and wait 5s timeout for receive ACK.
     */
    public void testConnection() {
        try {
//            this.sendText("*PING");
            serialPort.writeByte((byte) 5); //ENQ
            this.start_time = System.nanoTime();
            this.pingResult = -1;
            Timer timeoutTimer = new Timer();
            timeoutTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (Controler.this.pingResult == -1) {
                        view.showConnectionTestResults(false, 0);
                    }
                }
            }, 5000);
        } catch (SerialPortException ex) {
            System.err.println(ex);
        }
    }
    
    /**
     * Handle receiving ping response request.
     * Calculate time difference and send it to GUI.
     */
    public void receivedPingResponse() {
        long end_time = System.nanoTime();
        Double difference = (end_time - start_time)/1e6;
        this.pingResult = difference.intValue();
        view.showConnectionTestResults(true, pingResult);
    }
    
    /**
     * Split received text to lines and send each one by serial interface,
     * adding terminator to the end of all command.
     * 
     * @param text string to send through serial connection
     * 
     * @return true if was sended ok, false if an error occured
     */
    public boolean sendText(String text) {
        String[] commandLines = text.split(System.getProperty("line.separator"));
        for (String command : commandLines) {
            String textToSend = command + terminatorToInsert;
            try {
                byte[] sendedChars = textToSend.getBytes(StandardCharsets.US_ASCII);
                serialPort.writeBytes(sendedChars);
            } catch (SerialPortException ex) {
                System.err.println(ex);
                return false;
            }
        }
        return true;
    }
    
    /**
     * Wraps model request - call GUI method to show text in the field.
     * 
     * @param chars string to be showed in single line
     */
    public void receivedNewChars(String chars) {
        view.insertReceivedText(chars, false);
    }
    
    /**
     * Wraps model request - call GUI method to show text in the field.
     * 
     * @param line string to be showed in new line
     */
    public void receivedNewLine(String line) {
        view.insertReceivedText(line, true);
    }
    
    
    /*
    Getters to serial port informations.
    */
    
    /**
     * List all avaiables serial port names in system.
     * @return the array of serial port names
     */
    public String[] getAvaiablePortsNames() {
        return SerialPortList.getPortNames();
    }

    /**
     * Getter to the connected port system name.
     * @return the port name
     */
    public String getConnectedPortName() {
        return this.serialPortName;
    }

    /**
     * Convert setted up port speed (baudrate) to string representation.
     * @return the connected port speed
     */
    public String getConnectedPortSpeed() {
        return Integer.toString(this.baudRate);
    }

    /**
     * Getter to the sign format setting.
     * @return the sign format setting
     */
    public String getConnectedPortSignFormat() {
        return this.signFormat;
    }

    /**
     * Convert setted up flow control mask to string representation.
     * @return the current flow control setting
     */
    public String getConnectedPortFlowControl() {
        switch (this.flowControlMask) {
            case SerialPort.FLOWCONTROL_NONE:
                return "brak";
            case SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT:
                return "sprzętowa";
            case SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT:
                return "programowa";
            default:
                return "";
        }
    }

    /**
     * Getter to the setted up terminator string.
     * @return the terminator string
     */
    public String getConnectedPortTerminator() {
        return this.terminatorToShow;
    }

}
