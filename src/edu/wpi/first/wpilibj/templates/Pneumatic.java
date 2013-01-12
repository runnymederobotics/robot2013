package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;

public class Pneumatic {
    DoubleSolenoid doubleSolenoid = null;
    Solenoid singleSolenoid = null;
    Relay relay = null;
    
    boolean state = false;
    
    public Pneumatic(DoubleSolenoid doubleSolenoid) {
        this.doubleSolenoid = doubleSolenoid;
    }
    
    public Pneumatic(Solenoid singleSolenoid) {
        this.singleSolenoid = singleSolenoid;
    }
    
    public Pneumatic(Relay relay) {
        this.relay = relay;
    }
    
    public void set(boolean value) {
        state = value;
        
        if(doubleSolenoid != null) {
            doubleSolenoid.set(state ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        } else if(singleSolenoid != null) {
            singleSolenoid.set(state);
        } else if(relay != null) {
            relay.set(state ? Relay.Value.kForward : Relay.Value.kReverse);
        }
    }
    
    public boolean get() {
        return state;
    }
}
