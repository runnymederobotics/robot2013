package robot.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.Pneumatic;
import robot.commands.TeleopPickupCommand;
import robot.parsable.ParsableDouble;

public class PickupSubsystem extends Subsystem {

    Victor pickupRoller = new Victor(Constants.PICKUP_ROLLER_MOTOR_CHANNEL);
    //Pneumatics are initialized in CommandBase.java
    public Pneumatic pickupPneumatic;
    ParsableDouble pickupRollerSpeed = new ParsableDouble("pickup_roller_speed", -1.0);

    public PickupSubsystem() {
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new TeleopPickupCommand());
    }

    public void setPneumatic(boolean value) {
        pickupPneumatic.set(value);
    }

    public void runRoller(boolean value) {
        pickupRoller.set(value ? pickupRollerSpeed.get() : 0.0);
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("pickupRoller: " + pickupRoller.get() + " pickupPneumatic: " + pickupPneumatic.get());
    }
}
