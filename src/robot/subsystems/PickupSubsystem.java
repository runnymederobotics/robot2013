package robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.Pneumatic;
import robot.commands.TeleopPickupCommand;

public class PickupSubsystem extends Subsystem {

    Victor pickupRoller = new Victor(Constants.PICKUP_ROLLER_MOTOR_CHANNEL);
    Victor elevatorRoller = new Victor(Constants.ELEVATOR_ROLLER_MOTOR_CHANNEL);
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
         if(now - lastFrisbeeSensorTime > Constants.PICKUP_DELAY_AFTER_FRISBEE.get()) {
         //If we've waited enough time since the last time we saw a frisbee
         pickupPneumatic.set(value);
         }
         } else {
         pickupPneumatic.set(value);
         }
         lastFrisbeeSensorTime = frisbeeSensor.get() ? now : lastFrisbeeSensorTime;*/
    }

    public void runRoller(boolean value, boolean reverse) {
        double pickupSpeed = value ? Constants.PICKUP_SPEED.get() : 0.0;
        double elevatorSpeed = value ? Constants.ELEVATOR_SPEED.get() : 0.0;
        pickupRoller.set(reverse ? -pickupSpeed : pickupSpeed);
        elevatorRoller.set(reverse ? -elevatorSpeed : elevatorSpeed);
    }

    public boolean pickupDown() {
        return pickupPneumatic.get();
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("pickupRoller: " + pickupRoller.get() + " pickupPneumatic: " + pickupPneumatic.get());
        System.out.println("elevatorRoller: " + elevatorRoller.get());
        System.out.println("frisbeeSensor: " + frisbeeSensor.get());
    }
}
