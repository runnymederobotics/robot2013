package robot.subsystems;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.CustomEncoder;
import robot.Pneumatic;
import robot.commands.ShooterCommand;
import robot.parsable.ParsableDouble;
import robot.parsable.SendableDouble;

public class ShooterSubsystem extends Subsystem {

    public static ParsableDouble MIN_SHOOT_THRESHOLD = new ParsableDouble("shooter_min_shoot_threshold", 0.5);
    Victor vicShooter = new Victor(Constants.SHOOTER_MOTOR_CHANNEL);
    Counter encShooter = new Counter(Constants.ENC_SHOOTER);
    SendableDouble sendableShooter = new SendableDouble("shooterEncoder", 0.0);
    //Pneumatics are initialized in CommandBase.java
    public Pneumatic shooterPneumaticLow;
    public Pneumatic shooterPneumaticHigh;
    int shooterState = ShooterState.LOAD;

    public class ShooterState {
        public static final int LOAD = 0;
        public static final int LOW = 1;
        public static final int MEDIUM = 2;
        public static final int HIGH = 3;
    }

    public ShooterSubsystem() {
        encShooter.setSemiPeriodMode(false);
        encShooter.start();
    }
    
    public void disable() {
        
    }
    
    public void enable() {
        
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new ShooterCommand());
    }

    public void runMotor(double speed) {
        vicShooter.set(speed);
    }

    public void setShooterState(int state) {
        shooterState = state;
    }

    public boolean inLoadState() {
        return shooterState == ShooterState.LOAD;
    }

    public void runShooterStateMachine() {
        sendableShooter.set(1 / encShooter.getPeriod());
        switch (shooterState) {
            case ShooterState.LOAD:
                shooterPneumaticLow.set(false);
                shooterPneumaticHigh.set(false);
                break;
            case ShooterState.LOW:
                shooterPneumaticLow.set(true);
                shooterPneumaticHigh.set(false);
                break;
            case ShooterState.MEDIUM:
                shooterPneumaticLow.set(false);
                shooterPneumaticHigh.set(true);
                break;
            case ShooterState.HIGH:
                shooterPneumaticLow.set(true);
                shooterPneumaticHigh.set(true);
                break;
        }
    }

    public boolean onTargetAndAboveThreshold() {
        //boolean aboveThreshold = encShooter.get() > MAX_SHOOTER_ENCODER_RATE.get() * MIN_SHOOT_THRESHOLD.get();
        //boolean onTarget = pidShooter.onTarget();
        return true;
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("vicShooter: " + vicShooter.get());
        System.out.println("encShooter: " + sendableShooter.get());
        System.out.println("shooterPneumaticLow: " + shooterPneumaticLow.get() + " shooterPneumaticHigh: " + shooterPneumaticHigh.get());
    }
}
