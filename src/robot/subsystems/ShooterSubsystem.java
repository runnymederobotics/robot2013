package robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.commands.ShooterCommand;
import robot.parsable.SendableInt;

public class ShooterSubsystem extends Subsystem {

    Encoder encShooter = new Encoder(7, 8);
    SendableInt sendableShooterEncoder = new SendableInt("shooterEncoder", 0);
    
    public ShooterSubsystem() {
    }
    
    protected void initDefaultCommand() {
        setDefaultCommand(new ShooterCommand());
    }
    
    public void sendEncoder() {
        sendableShooterEncoder.set((int)(Math.sin(Timer.getFPGATimestamp()) * 10));
        System.out.println("sendableShooterEncoder: " + sendableShooterEncoder.get());
        //sendableShooterEncoder.set(encShooter.getRate());
    }
}
