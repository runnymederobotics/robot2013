package robot.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.CustomEncoder;
import robot.Pneumatic;
import robot.commands.TeleopShooterCommand;
import robot.parsable.ParsablePIDController;

public class ShooterSubsystem extends Subsystem {

    public static final double MAX_SHOOTER_ENCODER_RATE = 170; //RPS
    Victor vicShooter = new Victor(Constants.SHOOTER_MOTOR_CHANNEL);
    CustomEncoder encShooter = new CustomEncoder(Constants.ENC_SHOOTER);
    ParsablePIDController pidShooter = new ParsablePIDController("pidshooter", 0.05, 0.0, 0.0, 0.0048, encShooter, vicShooter);
    //Pneumatics are initialized in CommandBase.java
    public Pneumatic shooterLifterPneumatic;
    int shooterState = ShooterState.LOAD;
    double setpoint = 0.0;

    public class ShooterState {

        public static final int LOAD = 0;
        public static final int LOW = 1;
        public static final int HIGH = 2;
    }

    public ShooterSubsystem() {
        encShooter.setSemiPeriodMode(false);
        encShooter.setUpdateWhenEmpty(true);
        encShooter.setMaxPeriod(1.0); //Timeout after this time. Will give 1 / x RPS where x is the max period
        encShooter.start();

        pidShooter.setInputRange(0, MAX_SHOOTER_ENCODER_RATE);
        pidShooter.setOutputRange(0.0, 1.0);
        pidShooter.setPercentTolerance(Constants.SHOOTER_PYRAMID_TOLERANCE.get());
        
        //(new BangBangThread()).start();
    }
    
    /*public class BangBangThread extends Thread {        
        public void run() {
            while(true) {
                if(encShooter.pidGet() < setpoint) {
                    vicShooter.set(1.0);
                } else {
                    vicShooter.set(0.5);
                }
            }
        }
    }*/
    
    public void setBangBangSetpoint(double setpoint) {
        this.setpoint = setpoint;
    }
    
    public void disable() {
        disablePID();
    }
    
    public void enable() {
        enablePID();
    }

    public void disablePID() {
        pidShooter.disable();
    }

    public void enablePID() {
        pidShooter.enable();
    }
    
    public boolean pidStatus() {
        return pidShooter.isEnable();
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new TeleopShooterCommand());
    }
    
    public void setTolerance(double value) {
        pidShooter.setPercentTolerance(value);
    }
    
    public void setSetpoint(double setpoint) {
        if (pidShooter.isEnable()) {
            pidShooter.setSetpoint(setpoint);
        } else {
            vicShooter.set(setpoint / MAX_SHOOTER_ENCODER_RATE);
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
    
    public boolean onTarget() {
        //return encShooter.pidGet() >= setpoint - Constants.SHOOTER_OVERSHOOT_VALUE.get();
        return pidShooter.onTarget();
    }

    public boolean aboveShootThreshold() {
        return encShooter.pidGet() > MAX_SHOOTER_ENCODER_RATE * Constants.SHOOTER_MIN_SHOOT_THRESHOLD.get();
    }
    
    public boolean belowReverseThreshold() {
        return encShooter.pidGet() < MAX_SHOOTER_ENCODER_RATE * Constants.SHOOTER_MAX_REVERSE_THRESHOLD.get();
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("vicShooter: " + vicShooter.get());
        System.out.println("pidShooter ontarget: " + pidShooter.onTarget());
        System.out.println("pidShooter input: " + encShooter.pidGet() + " setpoint: " + pidShooter.getSetpoint() + " output: " + pidShooter.get());
        System.out.println("shooterLifterPneumatic: " + shooterLifterPneumatic.get());
    }
}
