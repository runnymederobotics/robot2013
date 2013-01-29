package robot.subsystems;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.DirectionVector;
import robot.commands.CommandBase;
import robot.commands.PositioningCommand;
import robot.parsable.SendableInt;

public class PositioningSubsystem extends Subsystem {

    Gyro positionGyro = new Gyro(2);
    DirectionVector overallVector = DirectionVector.ZERO;
    SendableInt x = new SendableInt("position.x", 0);
    SendableInt y = new SendableInt("position.y", 0);

    public PositioningSubsystem() {
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new PositioningCommand());
    }
    
    double lastUpdateTime = 0;

    public void updateVectors() {
        double now = Timer.getFPGATimestamp();
        
        if(lastUpdateTime == 0) {
            lastUpdateTime = now;
        }
        
        //Get angle and convert to radians
        double angle = (int)(positionGyro.getAngle()) * Math.PI / 180;

        //Get current rate in inches/unit time
        double rate = CommandBase.chassisSubsystem.getAverageRate() * ChassisSubsystem.INCHES_PER_ENCODER_COUNT;
        
        //The distance we've travelled since our last update
        //Rate * change in time since last update (update delay)
        double distance = rate * (now - lastUpdateTime);//(1.0 / PositioningCommand.POSITIONING_RESOLUTION.get());

        DirectionVector curVector = new DirectionVector(angle, distance);

        overallVector.add(curVector);

        double overallAngle = overallVector.getAngle();
        double overallMagnitude = overallVector.getMagnitude();

        x.set((int)(overallMagnitude * Math.cos(overallAngle)));
        y.set((int)(overallMagnitude * Math.sin(overallAngle)));
        
        lastUpdateTime = now;
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("Gyro: " + positionGyro.getAngle());
        System.out.println("overallVector angle:" + overallVector.getAngle() + " magnitude: " + overallVector.getMagnitude());
    }
}
