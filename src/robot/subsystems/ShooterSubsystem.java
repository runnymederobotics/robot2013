package robot.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.Encoder1310;
import robot.Pneumatic;
import robot.commands.ShooterCommand;
import robot.parsable.SendableDouble;

public class ShooterSubsystem extends Subsystem {

    Victor vicShooter = new Victor(Constants.SHOOTER_MOTOR_CHANNEL);
    Encoder1310 encShooter = new Encoder1310(Constants.ENC_SHOOTER);
    //Pneumatics are initialized in CommandBase.java
    public Pneumatic shooterPneumaticA;
    public Pneumatic shooterPneumaticB;
    SendableDouble sendableShooterEncoder = new SendableDouble("shooterEncoder", 0);

    public ShooterSubsystem() {
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new ShooterCommand());
    }

    public void sendEncoder() {
        //sendableShooterEncoder.set((int) (Math.sin(Timer.getFPGATimestamp()) * 10 + 10));
        sendableShooterEncoder.set(encShooter.get());
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("encShooter: " + encShooter.get());
    }
}
