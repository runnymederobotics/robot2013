package robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.Pneumatic;
import robot.commands.TeleopHopperCommand;
import robot.parsable.ParsableDouble;

public class HopperSubsystem extends Subsystem {

    //Pneumatics are initialized in CommandBase.java
    public Pneumatic stackDropper;
    public Pneumatic stackHolder;
    public Pneumatic shooterLoader;
    Victor vicRelease = new Victor(Constants.HOPPER_RELEASE_MOTOR_CHANNEL);
    DigitalInput releaseSensor = new DigitalInput(Constants.HOPPER_RELEASE_SENSOR);
    public static ParsableDouble RELEASE_MOTOR_SPEED = new ParsableDouble("release_motor_speed", 1.0);
    public static ParsableDouble PNEUMATIC_DELAY = new ParsableDouble("release_delay", 0.5);
    public static ParsableDouble RELEASE_DELAY = new ParsableDouble("finish_delay", 0.5);
    double lastReleaseTime = 0.0;
    double startTime = 0.0;
    int curState = HopperState.RESTING;
    boolean lastSensorState = false;

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

    public void reset() {
        shooterLoader.set(false);
    }

    public void update(boolean requestShot) {
        boolean curSensorState = releaseSensor.get();
        if (!lastSensorState && curSensorState) {
            //We weren't seeing the motor before, now we're seeing the target
            vicRelease.set(0.0);
        } else if (requestShot && lastSensorState && curSensorState) {
            //We want to shoot and we are and have been seeing the target
            vicRelease.set(RELEASE_MOTOR_SPEED.get());
        }
        lastSensorState = curSensorState;

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
                if (now - startTime > PNEUMATIC_DELAY.get()) {
                    //When we've waited long enough for the piston to push
                    lastReleaseTime = now;
                    shooterLoader.set(false);
                    curState = HopperState.FINISHING;
                }
                break;
            case HopperState.FINISHING:
                if(now - lastReleaseTime > RELEASE_DELAY.get()) {
                    //If we've waited long enough since last shot
                    curState = HopperState.RESTING;
                }
                break;
        }
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("curState: " + curState);
        System.out.println("stackHolder: " + stackHolder.get());
        System.out.println("stackDropper: " + stackDropper.get());
        System.out.println("shooterLoader: " + shooterLoader.get());
    }
}
