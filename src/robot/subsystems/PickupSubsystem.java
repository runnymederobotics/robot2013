package robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.Pneumatic;
import robot.commands.TeleopPickupCommand;
import robot.parsable.ParsableDouble;

public class PickupSubsystem extends Subsystem {

    ParsableDouble ROLLER_SPEED = new ParsableDouble("pickup_roller_speed", -1.0);
    ParsableDouble DELAY_AFTER_FRISBEE = new ParsableDouble("delay_after_frisbee", 1.0);
    Victor pickupRoller = new Victor(Constants.PICKUP_ROLLER_MOTOR_CHANNEL);
    DigitalInput frisbeeSensor = new DigitalInput(Constants.PICKUP_FRISBEE_SENSOR);
    //Pneumatics are initialized in CommandBase.java
    public Pneumatic pickupPneumatic;
    boolean lastFrisbeeSensor = false;
    double lastFrisbeeSensorTime = 0.0;
    
    public PickupSubsystem() {
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new TeleopPickupCommand());
    }
    
    public void disable() {
    }
    
    public void enable() {
    }

    //Down is false, up is true
    public void setPneumatic(boolean value) {
        pickupPneumatic.set(value);
        /*double now = Timer.getFPGATimestamp();
        if(value) {
            //If we're trying to lower the pickup
            if(now - lastFrisbeeSensorTime > DELAY_AFTER_FRISBEE.get()) {
                //If we've waited enough time since the last time we saw a frisbee
                pickupPneumatic.set(value);
            }
        } else {
            pickupPneumatic.set(value);
        }
        lastFrisbeeSensorTime = frisbeeSensor.get() ? now : lastFrisbeeSensorTime;*/
    }

    public void runRoller(boolean value) {
        pickupRoller.set(value ? ROLLER_SPEED.get() : 0.0);
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("pickupRoller: " + pickupRoller.get() + " pickupPneumatic: " + pickupPneumatic.get());
        System.out.println("frisbeeSensor: " + frisbeeSensor.get());
    }
}
