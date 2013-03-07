package robot.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.CustomEncoder;
import robot.Pneumatic;
import robot.commands.TeleopShooterCommand;
import robot.parsable.ParsableDouble;
import robot.parsable.ParsablePIDController;

public class ShooterSubsystem extends Subsystem {

    public static final double PID_SHOOTER_PERCENT_TOLERANCE = 0.1;
    public static final double MAX_SHOOTER_ENCODER_RATE = 120; //RPS
    public static ParsableDouble MIN_SHOOT_THRESHOLD = new ParsableDouble("shooter_min_shoot_threshold", 0.5);
    Victor vicShooter = new Victor(Constants.SHOOTER_MOTOR_CHANNEL);
    CustomEncoder encShooter = new CustomEncoder(Constants.ENC_SHOOTER);
    ParsablePIDController pidShooter = new ParsablePIDController("pidshooter", 0.01, 0.0005, 0.0, encShooter, vicShooter);
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
        
        pidShooter.setInputRange(0, MAX_SHOOTER_ENCODER_RATE);
        pidShooter.setOutputRange(0.0, 1.0);
        pidShooter.setPercentTolerance(PID_SHOOTER_PERCENT_TOLERANCE);
        
    }
    
    public void disable() {
        pidShooter.disable();
    }
    
    public void enable() {
        pidShooter.enable();
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new TeleopShooterCommand());
    }

    public void runMotor(double speed) {
        vicShooter.set(speed);
    }
    
    public void setSetpoint(double setpoint) {
        pidShooter.setSetpoint(setpoint * MAX_SHOOTER_ENCODER_RATE);
    }

    public void setShooterState(int state) {
        shooterState = state;
    }
    
    public int getShooterState() {
        return shooterState;
    }

    public void doShooterState() {
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
        boolean aboveThreshold = encShooter.get() > MAX_SHOOTER_ENCODER_RATE * MIN_SHOOT_THRESHOLD.get();
        boolean onTarget = pidShooter.onTarget();
        
        return onTarget && aboveThreshold;
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("vicShooter: " + vicShooter.get());
        System.out.println("pidShooter enabled: " + true);
        System.out.println("pidShooter input: " + encShooter.pidGet() + " setpoint: " + pidShooter.getSetpoint() + " output: " + pidShooter.get());
        System.out.println("shooterPneumaticLow: " + shooterPneumaticLow.get() + " shooterPneumaticHigh: " + shooterPneumaticHigh.get());
    }
}
