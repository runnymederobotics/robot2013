package robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.Pneumatic;
import robot.commands.TeleopPickupCommand;
import robot.parsable.SendableInt;

public class PickupSubsystem extends Subsystem {

    Victor pickupRoller = new Victor(Constants.PICKUP_ROLLER_MOTOR_CHANNEL);
    Victor elevatorRoller = new Victor(Constants.ELEVATOR_ROLLER_MOTOR_CHANNEL);
    DigitalInput frisbeeSensor = new DigitalInput(Constants.PICKUP_FRISBEE_SENSOR);
    SendableInt pickupFrisbee = new SendableInt("pickupFrisbee", 0);
    //Pneumatics are initialized in CommandBase.java
    public Pneumatic pickupPneumatic;
    boolean lastFrisbeeSensor = false;
    double lastFrisbeeSensorTime = 0.0;
    double lastNoFrisbeeSensorTime = 0.0;
    boolean disablePickupRoller = false;

    public PickupSubsystem() {
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new TeleopPickupCommand());
    }

    public void disable() {
    }

    public void enable() {
    }
    
    public void updateSensors() {
        pickupFrisbee.set(frisbeeSensor.get() ? 0 : 1);
    }

    public void setPneumatic(boolean value) {        
        double now = Timer.getFPGATimestamp();
        if (!value) {
            //If we're trying to raise the pickup
            if (now - lastFrisbeeSensorTime > Constants.PICKUP_DELAY_AFTER_FRISBEE.get()) {
                //If we've waited enough time since the last time we saw a frisbee
                pickupPneumatic.set(false); //Raise the pickup
            }
        } else {
            pickupPneumatic.set(true); //Lower the pickup
        }

        //There is a frisbee in the pickup when frisbeeSensor.get() is false
        lastFrisbeeSensorTime = !frisbeeSensor.get() ? now : lastFrisbeeSensorTime;
        //The amount of time since our pickup was clear
        lastNoFrisbeeSensorTime = frisbeeSensor.get() ? now : lastNoFrisbeeSensorTime;
    }

    public void runRoller(boolean value, boolean reverse) {
        double pickupSpeed = 0.0;
        double elevatorSpeed = 0.0;
        double now = Timer.getFPGATimestamp();
        if (pickupDown() && (value || now - lastFrisbeeSensorTime < Constants.PICKUP_DELAY_AFTER_FRISBEE.get())) {
            //Keep running the roller if we havent waited long enough since the last frisbee
            pickupSpeed = Constants.PICKUP_SPEED.get();
            elevatorSpeed = Constants.ELEVATOR_SPEED.get();
        }
        
        //THIS CODE WAS JUST ADDED, MUST TEST
        //DONT DOWNLOAD UNLESS SUFFICIENT TIME TO TEST
        //If we've seen a frisbee for more than this amount of time, reverse the roller
        /*if(pickupDown() && now - lastNoFrisbeeSensorTime > Constants.PICKUP_FRISBEE_JAM_TIME.get()) {
            //Run the roller in reverse
            pickupSpeed = Constants.PICKUP_SPEED.get();
            reverse = true;
        }*/

        if (!disablePickupRoller) {
            pickupRoller.set(reverse ? -pickupSpeed : pickupSpeed);
        } else {
            pickupRoller.set(0);
        }
        elevatorRoller.set(elevatorSpeed);//reverse ? -elevatorSpeed : elevatorSpeed);
    }
    
    public void setDisablePickupRoller(boolean value) {
        disablePickupRoller = value;
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
