package robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.Pneumatic;
import robot.commands.TeleopHopperCommand;
import robot.parsable.SendableInt;

public class HopperSubsystem extends Subsystem {

    //Pneumatics are initialized in RobotTemplate.java
    public Pneumatic shooterLoader;
    DigitalInput frisbeeSensor = new DigitalInput(Constants.HOPPER_FRISBEE_SENSOR);
    SendableInt hopperFrisbee = new SendableInt("hopperFrisbee", 0);
    double lastReleaseTime = 0.0;
    double startTime = 0.0;
    int curState = HopperState.RESTING;

    class HopperState {

        public static final int RESTING = 0;
        public static final int RELEASING = 1;
        public static final int RETRACTING = 2;
        public static final int FINISHING = 3;
    }

    public HopperSubsystem() {
    }

    public void initDefaultCommand() {
        setDefaultCommand(new TeleopHopperCommand());
    }

    public void disable() {
    }

    public void enable() {
        reset();
    }
    
    public void updateSensors() {
        hopperFrisbee.set(frisbeeSensor.get() ? 0 : 1);
    }

    public void reset() {
        shooterLoader.set(false);
    }

    public boolean hasFrisbee() {
        return !frisbeeSensor.get();
    }

    public void update(boolean requestShot) {        
        double now = Timer.getFPGATimestamp();

        switch (curState) {
            case HopperState.RESTING:
                if (requestShot) {
                    curState = HopperState.RELEASING;
                }
                break;
            case HopperState.RELEASING:
                //Fire the pneumatic
                startTime = now;
                shooterLoader.set(true);
                curState = HopperState.RETRACTING;
                break;
            case HopperState.RETRACTING:
                if (now - startTime > Constants.HOPPER_PNEUMATIC_DELAY.get()) {
                    //When we've waited long enough for the piston to push
                    lastReleaseTime = now;
                    shooterLoader.set(false);
                    curState = HopperState.FINISHING;
                }
                break;
            case HopperState.FINISHING:
                if (now - lastReleaseTime > Constants.HOPPER_RELEASE_DELAY.get()) {
                    //If we've waited long enough since last shot
                    curState = HopperState.RESTING;
                }
                break;
        }
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("curState: " + curState);
        System.out.println("shooterLoader: " + shooterLoader.get());
        System.out.println("Frisbee Sensor: " + frisbeeSensor.get());
    }
}
