package robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.Pneumatic;
import robot.commands.TeleopDriveCommand;

public class ChassisSubsystem extends Subsystem {

    public static final double INCHES_PER_ENCODER_COUNT = 34.5 / 499;//(8 * Math.PI) / 400;
    Victor leftMotor = new Victor(Constants.LEFT_MOTOR_CHANNEL);
    Victor rightMotor = new Victor(Constants.RIGHT_MOTOR_CHANNEL);
    Pneumatic shifterPneumatic = new Pneumatic(new DoubleSolenoid(Constants.SOLENOID_SHIFTER_ONE, Constants.SOLENOID_SHIFTER_TWO));
    RobotDrive robotDrive = new RobotDrive(leftMotor, rightMotor);
    Encoder encLeft = new Encoder(5, 6, true);
    Encoder encRight = new Encoder(3, 4, true);
    int numberRatesToAverage = 0;
    double averageRateSum = 0;
    double averageRate = 0;

    public ChassisSubsystem() {
        encLeft.start();
        encRight.start();
    }

    public void initDefaultCommand() {
        setDefaultCommand(new TeleopDriveCommand());
    }

    public void arcadeDrive(double speed, double rotation) {
        robotDrive.arcadeDrive(speed, -rotation);
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        robotDrive.tankDrive(leftSpeed, rightSpeed);
    }

    public void shift(boolean value) {
        shifterPneumatic.set(value);
    }

    public void updateAverageRate() {
        averageRateSum += (encRight.getRate() - encLeft.getRate()) / 2;
        ++numberRatesToAverage;
    }

    public double getAverageRate() {
        //Right rate - left rate because left rate is negative
        averageRate = averageRateSum / numberRatesToAverage; //Average rate

        if(averageRate == Double.NaN) {
            System.out.println("averageRate: " + averageRate + " = " + averageRateSum + " / " + numberRatesToAverage);
            averageRate = 0.0;
        }
        
        averageRateSum = 0.0;
        numberRatesToAverage = 0;
        
        return averageRate;
    }

    public int getAverageDistance() {
        //Right counts - left counts because left counts are negative
        return (encRight.get() - encLeft.get()) / 2; //Average rate
    }
    
    public void resetDistance() {
        encLeft.reset();
        encRight.reset();
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        //System.out.println("leftMotor: " + leftMotor.get());
        //System.out.println("rightMotor: " + rightMotor.get());
        //System.out.println("shifterPneumatic: " + shifterPneumatic.get());
        System.out.println("averageEncoderRate: " + averageRate);
        System.out.println("averageEncoderDistance: " + getAverageDistance());
    }
}
