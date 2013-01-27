package robot.subsystems;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.DirectionVector;
import robot.commands.PositioningCommand;
import robot.parsable.SendableDouble;

public class PositioningSubsystem extends Subsystem {

    Gyro positionGyro = new Gyro(1);
    DirectionVector overallVector = DirectionVector.ZERO;
    SendableDouble x = new SendableDouble("position.x", 0);
    SendableDouble y = new SendableDouble("position.y", 0);
    double rate = 1; //Calculate using either accelerometer or encoders, preferably encoders

    public PositioningSubsystem() {
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new PositioningCommand());
    }

    public void updateVectors() {
        //Get angle and convert to radians
        double angle = positionGyro.getAngle() * Math.PI / 180;

        //units/second
        //When we add encoders, use chassisSubsystem.getRate();
        rate = 1;
        
        //The distance we've travelled since our last update
        //Rate * change in time since last update (update delay)
        double distance = rate * (1.0 / PositioningCommand.POSITIONING_RESOLUTION.get());

        DirectionVector curVector = new DirectionVector(angle, distance);

        overallVector.add(curVector);

        double overallAngle = overallVector.getAngle();
        double overallMagnitude = overallVector.getMagnitude();

        x.set(overallMagnitude * Math.cos(overallAngle));
        y.set(overallMagnitude * Math.sin(overallAngle));
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("Gyro: " + positionGyro.getAngle());
        System.out.println("overallVector angle:" + overallVector.getAngle() + " magnitude: " + overallVector.getMagnitude());
    }
}
