package robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.OutputStorage;
import robot.Pneumatic;
import robot.commands.CommandBase;
import robot.commands.TeleopDriveCommand;
import robot.parsable.ParsableDouble;
import robot.parsable.ParsablePIDController;

public class ChassisSubsystem extends Subsystem {

    public static final double INCHES_PER_ENCODER_COUNT = 34.5 / 499;
    public ParsableDouble MAX_LOW_ENCODER_RATE = new ParsableDouble("max_low_encoder_rate", 500);
    public ParsableDouble MAX_HIGH_ENCODER_RATE = new ParsableDouble("max_high_encoder_rate", 2800);
    Victor leftMotor = new Victor(Constants.LEFT_MOTOR_CHANNEL);
    Victor rightMotor = new Victor(Constants.RIGHT_MOTOR_CHANNEL);
    Encoder encLeft = new Encoder(Constants.ENC_LEFT_ONE, Constants.ENC_LEFT_TWO, true);
    Encoder encRight = new Encoder(Constants.ENC_RIGHT_ONE, Constants.ENC_RIGHT_TWO, true);
    public Pneumatic shifterPneumatic; //Pneumatics are initialized in CommandBase.java
    OutputStorage leftOutputStorage = new OutputStorage();
    OutputStorage rightOutputStorage = new OutputStorage();
    RobotDrive robotDrive = new RobotDrive(leftOutputStorage, rightOutputStorage);
    ParsablePIDController pidLeft = new ParsablePIDController("pidleft", 0.0, 0.00025, 0.0, encLeft, leftMotor);
    ParsablePIDController pidRight = new ParsablePIDController("pidright", 0.0, 0.00025, 0.0, encRight, rightMotor);
    OutputStorage pidGyroStorage = new OutputStorage();
    public ParsablePIDController pidGyro = new ParsablePIDController("pidgyro", 0.02, 0.0, 0.0, CommandBase.positioningSubsystem.positionGyro, pidGyroStorage);
    
    public ChassisSubsystem() {
        encLeft.setPIDSourceParameter(Encoder.PIDSourceParameter.kRate);
        encRight.setPIDSourceParameter(Encoder.PIDSourceParameter.kRate);
        
        encLeft.start();
        encRight.start();
        
        pidLeft.setOutputRange(-1.0, 1.0);
        pidRight.setOutputRange(-1.0, 1.0);
        pidGyro.setOutputRange(-1.0, 1.0);
        
        pidLeft.setPercentTolerance(10.0);
        pidRight.setPercentTolerance(10.0);
        pidGyro.setAbsoluteTolerance(5.0); //+- 5 degrees
        
        updateInputRange();
    }

    public void initDefaultCommand() {
        setDefaultCommand(new TeleopDriveCommand());
    }
    
    public boolean isEnabledPID() {
        return pidLeft.isEnable() && pidRight.isEnable();
    }
    
    public boolean isEnabledPIDGyro() {
        return pidGyro.isEnable();
    }
    
    public void disablePID() {
        if(pidLeft.isEnable() || pidRight.isEnable()) {
            pidLeft.disable();
            pidRight.disable();
        }
    }
    
    public void enablePID() {
        if(!pidLeft.isEnable() || !pidRight.isEnable()) {
            pidLeft.enable();
            pidRight.enable();
        }
    }
    
    public void disablePIDGyro() {
        if(pidGyro.isEnable()) {
            pidGyro.disable();
        }
    }
    
    public void enablePIDGyro() {
        if(!pidGyro.isEnable()) {
            pidGyro.enable();
        }
    }
    
    public void disable() {
        disablePID();
    }
    
    public void enable() {
        encLeft.reset();
        encRight.reset();
        enablePID();
    }
    
    private void updateInputRange() {
        if(isEnabledPID()) {
            pidLeft.setInputRange(-MAX_HIGH_ENCODER_RATE.get(), MAX_HIGH_ENCODER_RATE.get());
            pidRight.setInputRange(-MAX_HIGH_ENCODER_RATE.get(), MAX_HIGH_ENCODER_RATE.get());
        } else {
            pidLeft.setInputRange(-MAX_LOW_ENCODER_RATE.get(), MAX_LOW_ENCODER_RATE.get());
            pidRight.setInputRange(-MAX_LOW_ENCODER_RATE.get(), MAX_LOW_ENCODER_RATE.get());
        }
    }

    public void drive(double speed, double rotation) {
        robotDrive.arcadeDrive(speed, -rotation);
        if(isEnabledPID()) {
            if(getShiftState()) {
                updateInputRange();
                
                //High gear
                pidLeft.setSetpoint(leftOutputStorage.get() * MAX_LOW_ENCODER_RATE.get());
                pidRight.setSetpoint(rightOutputStorage.get() * MAX_LOW_ENCODER_RATE.get());
            } else {
                updateInputRange();
                
                //Low gear
                pidLeft.setSetpoint(leftOutputStorage.get() * MAX_LOW_ENCODER_RATE.get());
                pidRight.setSetpoint(rightOutputStorage.get() * MAX_LOW_ENCODER_RATE.get());
            }
        } else {
            leftMotor.set(leftOutputStorage.get());
            rightMotor.set(rightOutputStorage.get());
        }
    }
    
    public void pidGyroSetpoint(double relativeAngle) {
        pidGyro.setSetpoint(CommandBase.positioningSubsystem.positionGyro.getAngle() + relativeAngle);
    }
    
    public boolean pidGyroOnTarget() {
        return pidGyro.onTarget();
    }

    public void shift(boolean value) {
        shifterPneumatic.set(value);
    }

    public boolean getShiftState() {
        return shifterPneumatic.get();
    }

    public double getAverageRate() {
        return (encRight.getRate() + encLeft.getRate()) / 2;
    }

    public int getAverageDistance() {
        //Right counts - left counts because left counts are negative
        return (encRight.get() + encLeft.get()) / 2; //Average rate
    }

    public void resetDistance() {
        encLeft.reset();
        encRight.reset();
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("encLeftRate: " + encLeft.getRate() + " encRightRate: " + encRight.getRate());
        System.out.println("encLeft: " + encLeft.get() + " encRight: " + encRight.get());
        System.out.println("averageEncoderRate: " + getAverageRate());
        System.out.println("averageEncoderDistance: " + getAverageDistance());
        System.out.println("PIDEnabled: " + isEnabledPID());
        System.out.println("LeftOutputStorage: " + leftOutputStorage.get() + " RightOutputStorage: " + rightOutputStorage.get());
        System.out.println("PIDLeft output: " + pidLeft.get() + " PIDRight output: " + pidRight.get());
        System.out.println("PIDLeft setpoint: " + pidLeft.getSetpoint() + " PIDRight setpoint: " + pidRight.getSetpoint());
        System.out.println("PIDGyro setpoint: " + pidGyro.getSetpoint() + " output: " + pidGyro.get());
        System.out.println("PIDGyro outputStorage: " + pidGyroStorage.get() + " PIDGyro onTarget: " + pidGyroOnTarget());
    }
}
