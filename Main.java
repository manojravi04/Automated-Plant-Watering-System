import java.util.Timer;
import org.firmata4j.IODevice;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;
import org.firmata4j.I2CDevice;

public class Main {
    public static void main(String[] args){
        String myPort = "COM3"; // The USB port name varies.
        IODevice myGroveBoard = new FirmataDevice(myPort); // Board object, using the name of a port

        try {
            myGroveBoard.start();
            myGroveBoard.ensureInitializationIsDone();

            I2CDevice i2cObject = myGroveBoard.getI2CDevice((byte)0x3C);
            SSD1306 myOledObject = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64);
            myOledObject.init();

            var Sensor = myGroveBoard.getPin(17);
            Sensor.setMode(Pin.Mode.ANALOG);

            var Pump = myGroveBoard.getPin(2);
            Pump.setMode(Pin.Mode.OUTPUT);

            Timer TimerTask = new Timer();

            var task = new sensorTask(Sensor,Pump,myOledObject);

            TimerTask.schedule(task, 0, 1000);


        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}