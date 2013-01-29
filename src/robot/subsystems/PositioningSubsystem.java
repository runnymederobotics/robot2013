package robot.subsystems;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.DirectionVector;
import robot.commands.CommandBase;
import robot.commands.PositioningCommand;
import robot.parsable.SendableInt;

public class PositioningSubsystem extends Subsystem {

    Gyro positionGyro = new Gyro(Constants.POSITIONING_GYRO);
    DirectionVector overallVector = DirectionVector.ZERO;
    SendableInt x = new SendableInt("position.x", 0);
    SendableInt y = new SendableInt("position.y", 0);
    double lastUpdateTime = 0;

    public PositioningSubsystem() {
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new PositioningCommand());
    }

    public void updateVectors() {
        double now = Timer.getFPGATimestamp();

        if (lastUpdateTime == 0) {
            lastUpdateTime = now;
        }

        //Get angle and convert to radians
        //Shift by 90 degrees because vectors are calculated with 0 degrees being the x-axis
        double angle = (int) (positionGyro.getAngle() + 90) * Math.PI / 180;

        //Get current rate in inches/unit time
        double rate = CommandBase.chassisSubsystem.getAverageRate() * ChassisSubsystem.INCHES_PER_ENCODER_COUNT;

        //The distance we've travelled since our last update
        //Rate * change in time since last update
        //This will be magnitude of our vector
        double distance = rate * (now - lastUpdateTime);
        
        //DirectionVector curVector = new DirectionVector(angle, distance);
        //overallVector.add(curVector);

        overallVector.add(angle, distance);

        double overallAngle = overallVector.getAngle();
        double overallMagnitude = overallVector.getMagnitude();

        x.set((int) (overallMagnitude * Math.cos(overallAngle)));
        y.set((int) (overallMagnitude * Math.sin(overallAngle)));

        lastUpdateTime = now;
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("Gyro: " + positionGyro.getAngle());
        System.out.println("overallVector angle:" + overallVector.getAngle() + " magnitude: " + overallVector.getMagnitude());
    }
}
