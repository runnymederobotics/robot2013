package templates.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import templates.Constants;
import templates.Pneumatic;
import templates.commands.TeleopDriveCommand;

public class ChassisSubsystem extends Subsystem {
    
    Victor leftMotor = new Victor(Constants.LEFT_MOTOR_CHANNEL);
    Victor rightMotor = new Victor(Constants.RIGHT_MOTOR_CHANNEL);
    
    Pneumatic shifterPneumatic = new Pneumatic(new DoubleSolenoid(Constants.SHIFTER_SOLENOID_ONE, Constants.SHIFTER_SOLENOID_TWO));
    
    RobotDrive robotDrive = new RobotDrive(leftMotor, rightMotor);
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new TeleopDriveCommand());
    }
    
    public void drive(double speed, double rotation) {
        robotDrive.arcadeDrive(speed, rotation);
    }
    
    public void shift(boolean value) {
        shifterPneumatic.set(value);
    }
}
