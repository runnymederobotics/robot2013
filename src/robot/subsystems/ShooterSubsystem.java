package robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.Pneumatic;
import robot.commands.ShooterCommand;
import robot.parsable.SendableInt;

public class ShooterSubsystem extends Subsystem {

    Victor vicShooter = new Victor(Constants.SHOOTER_MOTOR_CHANNEL);
    Encoder encShooter = new Encoder(Constants.ENC_SHOOTER_ONE, Constants.ENC_SHOOTER_TWO);
    //Pneumatics are initialized in CommandBase.java
    public Pneumatic shooterPneumaticA;
    public Pneumatic shooterPneumaticB;
    SendableInt sendableShooterEncoder = new SendableInt("shooterEncoder", 0);

    public ShooterSubsystem() {
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new ShooterCommand());
    }

    public void sendEncoder() {
        sendableShooterEncoder.set((int) (Math.sin(Timer.getFPGATimestamp()) * 10 + 10));
        //sendableShooterEncoder.set(encShooter.getRate());
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
    }
}
