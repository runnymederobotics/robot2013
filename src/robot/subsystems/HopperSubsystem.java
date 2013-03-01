package robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.Pneumatic;
import robot.commands.TeleopHopperCommand;
import robot.parsable.ParsableDouble;

public class HopperSubsystem extends Subsystem {

    public static ParsableDouble PNEUMATIC_DELAY = new ParsableDouble("hopper_pneumatic_delay", 0.5);
    public static ParsableDouble RELEASE_DELAY = new ParsableDouble("hopper_shoot_delay", 0.5);
    //Pneumatics are initialized in CommandBase.java
    public Pneumatic stackDropper;
    public Pneumatic stackHolder;
    public Pneumatic shooterLoader;
    DigitalInput frisbeeSensor = new DigitalInput(Constants.HOPPER_FRISBEE_SENSOR);
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

    public void reset() {
        shooterLoader.set(false);
    }
    
    public boolean hasFrisbee() {
        return frisbeeSensor.get();
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
