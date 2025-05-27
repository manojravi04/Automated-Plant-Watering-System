import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;
import java.io.IOException;
import java.util.TimerTask;
import edu.princeton.cs.introcs.StdDraw;
import java.util.HashMap;


public class sensorTask extends TimerTask {
        private final Pin mySensor;
        private final Pin myPump;
        private final SSD1306 Oled;

        int dry = 400;
        int littleWet = 450;
        int wet = 500;
        String myPlant;

        double Sample=0;

        public sensorTask(Pin sensorPin, Pin pumpPin, SSD1306 display) {
                this.mySensor = sensorPin;
                this.myPump = pumpPin;
                this.Oled = display;
        }

        @Override
        public void run() {
                int sensorValue = (int) mySensor.getValue();


                this.Oled.getCanvas().clear();
                try {
                        if (sensorValue >= dry) {
                                myPump.setValue(1);
                                Thread.sleep(500);
                                System.out.println("Plant need water, watering now");
                                myPlant = "Plant need water, watering now";

                        } else if (sensorValue > littleWet) {
                                myPump.setValue(1);
                                Thread.sleep(500);
                                System.out.println("plant still needs water, watering now");
                                myPlant = "Plant still need water, watering now";

                        } else if (sensorValue <= wet) {
                                myPump.setValue(0);
                                Thread.sleep(500);
                                System.out.println("plant does not need water anymore, stopping now");
                                myPlant = "Plant does not need water, stopping now";

                        } else {
                                myPump.setValue(0);
                                System.out.println("error");
                                myPlant = "error";
                        }
                        Oled.getCanvas().setTextsize(2);
                        Oled.getCanvas().drawString(0,0,myPlant);
                        Oled.display();

                        HashMap<Double, Integer> PlotData = new HashMap<>();


                        int PlotValue = (int) mySensor.getValue();

                        PlotData.put(Sample, PlotValue);
                        Sample=Sample+2;

                        PlotData.forEach((xValue, yValue) ->

                                StdDraw.text(xValue, yValue, "*"));

                        System.out.println(Sample);     


                        StdDraw.setXscale(0, 100);
                        StdDraw.setYscale(0, 800);

                        StdDraw.setPenRadius(0.005);
                        StdDraw.setPenColor(StdDraw.BLUE);
                        StdDraw.line(0, 0, 0, 1000);
                        StdDraw.line(0, 0, 1000, 0);

                        StdDraw.text(50, -20, "Time(ms)");
                        StdDraw.text(-3, 500, "V");
                        StdDraw.text(50, 800, "Moisture vs Time Graph");



                }catch (IOException ex){
                        ex.printStackTrace();
                } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                }


        }
}