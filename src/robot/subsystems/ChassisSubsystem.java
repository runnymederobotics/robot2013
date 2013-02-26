package robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.Pneumatic;
import robot.commands.TeleopDriveCommand;
import robot.parsable.ParsableDouble;

public class ChassisSubsystem extends Subsystem {

    public static final double INCHES_PER_ENCODER_COUNT = 34.5 / 499;
    public ParsableDouble MAX_LOW_ENCODER_RATE = new ParsableDouble("max_low_encoder_rate", 500);
    public ParsableDouble MAX_HIGH_ENCODER_RATE = new ParsableDouble("max_high_encoder_rate", 2000);
    Victor leftMotor = new Victor(Constants.LEFT_MOTOR_CHANNEL);
    Victor rightMotor = new Victor(Constants.RIGHT_MOTOR_CHANNEL);
    //Pneumatics are initialized in CommandBase.java
    public Pneumatic shifterPneumatic;
    RobotDrive robotDrive = new RobotDrive(leftMotor, rightMotor);
    Encoder encLeft = new Encoder(Constants.ENC_LEFT_ONE, Constants.ENC_LEFT_TWO, false);
    Encoder encRight = new Encoder(Constants.ENC_RIGHT_ONE, Constants.ENC_RIGHT_TWO, true);

    public ChassisSubsystem() {
        encLeft.start();
        encRight.start();
    }

    public void initDefaultCommand() {
        setDefaultCommand(new TeleopDriveCommand());
    }

    public void drive(double speed, double rotation) {
        robotDrive.arcadeDrive(speed, -rotation);
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
    }
}
