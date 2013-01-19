package robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.Pneumatic;
import robot.commands.TeleopDriveCommand;

public class ChassisSubsystem extends Subsystem {
    Victor leftMotor = new Victor(Constants.LEFT_MOTOR_CHANNEL);
    Victor rightMotor = new Victor(Constants.RIGHT_MOTOR_CHANNEL);
    
    Pneumatic shifterPneumatic = new Pneumatic(new DoubleSolenoid(Constants.SOLENOID_SHIFTER_ONE, Constants.SOLENOID_SHIFTER_TWO));
    
    RobotDrive robotDrive = new RobotDrive(leftMotor, rightMotor);
    
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
}
