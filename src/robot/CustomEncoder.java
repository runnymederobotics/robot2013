package robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import robot.parsable.SendableDouble;

public class CustomEncoder extends Thread {

    //The lowest speed we will be able to see is 1 RPS
    private static double lowestResolution = 0.5;
    DigitalInput digitalInput;
    SendableDouble RPS;
    double lastStateChange = 0;
    boolean oldValue = false;
    double revsPerSec = 0;

    private synchronized void doCalculations() {
        boolean newValue = digitalInput.get();

        double now = Timer.getFPGATimestamp();
        double deltaT = now - lastStateChange;

        //RPS = 1 / (2 * deltaT)
        //if (deltaT > lowestResolution) {
            //RPS.set(0);
            //lastStateChange = now;
        if (newValue != oldValue) {
            RPS.set(1 / (2 * deltaT));

            lastStateChange = now;
        }

        oldValue = newValue;

    }
    public void run() {
        while (true) {
            doCalculations();
        }
    }

    public CustomEncoder(String name, int channel) {
        digitalInput = new DigitalInput(channel);
        init(name);
    }

    public CustomEncoder(String name, int moduleNumber, int channel) {
        digitalInput = new DigitalInput(moduleNumber, channel);
        init(name);
    }

    private void init(String name) {
        this.RPS = new SendableDouble(name, 0);
        this.start();
    }

    public synchronized double get() {
        return RPS.get();
    }
}
