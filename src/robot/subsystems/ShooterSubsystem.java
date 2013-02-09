package robot.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.CustomEncoder;
import robot.Pneumatic;
import robot.commands.ShooterCommand;
import robot.parsable.SendableDouble;

public class ShooterSubsystem extends Subsystem {

    Victor vicShooter = new Victor(Constants.SHOOTER_MOTOR_CHANNEL);
    //Thread must be started from elsewhere, therefore encShooter must be public
    public CustomEncoder encShooter = new CustomEncoder("shooterEncoder", Constants.ENC_SHOOTER);
    //Pneumatics are initialized in CommandBase.java
    public Pneumatic shooterPneumaticA;
    public Pneumatic shooterPneumaticB;

    public ShooterSubsystem() {
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new ShooterCommand());
    }
    
    public void runMotor(double speed) {
        vicShooter.set(speed);
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("vicShooter: " + vicShooter.get());
        System.out.println("encShooter: " + encShooter.get());
    }
}
