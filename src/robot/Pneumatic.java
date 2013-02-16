package robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public class Pneumatic {

    DoubleSolenoid doubleSolenoid = null;
    Solenoid singleSolenoid = null;
    boolean state = false;

    //For double solenoids
    public Pneumatic(int module, int channelOne, int channelTwo) {
        System.out.println("new doubleSolenoid " + module + ", " + channelOne + ", " + channelTwo);
        doubleSolenoid = new DoubleSolenoid(module, channelOne, channelTwo);
    }

    //For single solenoids
    public Pneumatic(int module, int channel) {
        System.out.println("new singleSolenoid " + module + ", " + channel);
        singleSolenoid = new Solenoid(module, channel);
    }

    public void set(boolean value) {
        state = value;

        if (doubleSolenoid != null) {
            doubleSolenoid.set(state ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        } else if (singleSolenoid != null) {
            singleSolenoid.set(state);
        }
    }

    public boolean get() {
        return state;
    }
}
