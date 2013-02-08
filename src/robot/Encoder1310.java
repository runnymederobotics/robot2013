package robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

public class Encoder1310 {

    DigitalInput digitalInput;
    double lastStateChange = 0;
    boolean oldValue = false;
    double revsPerMillisec = 0;

    private class UpdateThread extends Thread {

        public synchronized void run() {
            boolean newValue = digitalInput.get();

            if (newValue != oldValue) {
                double now = Timer.getFPGATimestamp();
                double deltaT = now - lastStateChange;
                revsPerMillisec = 1 / (2 * deltaT);

                lastStateChange = now;
            }

            oldValue = newValue;
        }
    }

    public Encoder1310(int channel) {
        digitalInput = new DigitalInput(channel);
        initThread();
    }

    public Encoder1310(int moduleNumber, int channel) {
        digitalInput = new DigitalInput(moduleNumber, channel);
        initThread();
    }

    private void initThread() {
        UpdateThread encoderThread = new UpdateThread();
        encoderThread.start();
    }

    public synchronized double get() {
        return revsPerMillisec;
    }
}
