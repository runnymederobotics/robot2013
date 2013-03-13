package robot.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.CustomEncoder;
import robot.Pneumatic;
import robot.commands.TeleopShooterCommand;
import robot.parsable.ParsablePIDController;

public class ShooterSubsystem extends Subsystem {

    public static final double PID_SHOOTER_PERCENT_TOLERANCE = 5;
    public static final double MAX_SHOOTER_ENCODER_RATE = 175; //RPS
    Victor vicShooter = new Victor(Constants.SHOOTER_MOTOR_CHANNEL);
    CustomEncoder encShooter = new CustomEncoder(Constants.ENC_SHOOTER);
    ParsablePIDController pidShooter = new ParsablePIDController("pidshooter", 0.01, 0.0005, 0.0, encShooter, vicShooter);
    //Pneumatics are initialized in CommandBase.java
    public Pneumatic shooterLifterPneumatic;
    int shooterState = ShooterState.LOAD;

    public class ShooterState {

        public static final int LOAD = 0;
        public static final int LOW = 1;
        public static final int HIGH = 2;
    }

    public ShooterSubsystem() {
        encShooter.setSemiPeriodMode(false);
        encShooter.setMaxPeriod(1.0); //Timeout after this time. Will give 1 / x RPS where x is the max period
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

    public void setShooter(double value) {
        if (pidShooter.isEnable()) {
            pidShooter.setSetpoint(value * MAX_SHOOTER_ENCODER_RATE);
        } else {
            vicShooter.set(value);
        }
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
                shooterLifterPneumatic.set(false);
                break;
            case ShooterState.LOW:
                shooterLifterPneumatic.set(false);
                break;
            case ShooterState.HIGH:
                shooterLifterPneumatic.set(true);
                break;
        }
    }

    public boolean onTargetAndAboveThreshold() {
        boolean aboveThreshold = encShooter.get() > MAX_SHOOTER_ENCODER_RATE * Constants.SHOOTER_MIN_SHOOT_THRESHOLD.get();
        boolean onTarget = pidShooter.onTarget();

        return onTarget && aboveThreshold;
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("vicShooter: " + vicShooter.get());
        System.out.println("pidShooter ontarget: " + pidShooter.onTarget());
        System.out.println("pidShooter input: " + encShooter.pidGet() + " setpoint: " + pidShooter.getSetpoint() + " output: " + pidShooter.get());
        System.out.println("shooterLifterPneumatic: " + shooterLifterPneumatic.get());
    }
}
