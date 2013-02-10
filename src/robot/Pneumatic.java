package robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.communication.ModulePresence;

public class Pneumatic {

    DoubleSolenoid doubleSolenoid = null;
    Solenoid singleSolenoid = null;
    Relay relay = null;
    boolean state = false;

    //For double solenoids. Safe constructor, if module is not plugged in then the robot will still function
    public Pneumatic(int module, int channelOne, int channelTwo) {
        //if (ModulePresence.getModulePresence(ModulePresence.ModuleType.kSolenoid, module)) {
            System.out.println("new doubleSolenoid " + module + ", " + channelOne + ", " + channelTwo);
            doubleSolenoid = new DoubleSolenoid(module, channelOne, channelTwo);
        //}
    }

    //For single solenoids and relays. Safe constructor, if module is not plugged in then the robot will still function
    public Pneumatic(boolean solenoid, int module, int channel) {
        //if (ModulePresence.getModulePresence(ModulePresence.ModuleType.kSolenoid, module)) {
            System.out.println("new singleSolenoid " + module + ", " + channel);
            singleSolenoid = new Solenoid(module, channel);
        //} else if (ModulePresence.getModulePresence(ModulePresence.ModuleType.kDigital, module)) {
            //System.out.println("new relay " + module + ", " + channel);
            //relay = new Relay(module, channel);
        //}
    }

    /*public Pneumatic(DoubleSolenoid doubleSolenoid) {
        this.doubleSolenoid = doubleSolenoid;
    }

    public Pneumatic(Solenoid singleSolenoid) {
        this.singleSolenoid = singleSolenoid;
    }

    public Pneumatic(Relay relay) {
        this.relay = relay;
    }*/

    public void set(boolean value) {
        state = value;

        if (doubleSolenoid != null) {
            doubleSolenoid.set(state ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        } else if (singleSolenoid != null) {
            singleSolenoid.set(state);
        } else if (relay != null) {
            relay.set(state ? Relay.Value.kForward : Relay.Value.kReverse);
        }
    }

    public boolean get() {
        return state;
    }
}
