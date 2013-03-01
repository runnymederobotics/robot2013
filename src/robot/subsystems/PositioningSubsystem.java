package robot.subsystems;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.Vector;
import robot.commands.CommandBase;
import robot.commands.PositioningCommand;
import robot.parsable.ParsableInt;
import robot.parsable.SendableInt;

public class PositioningSubsystem extends Subsystem {

    ParsableInt xPositionStart = new ParsableInt("x_position_start", 162);
    ParsableInt yPositionStart = new ParsableInt("y_position_start", 216);
    SendableInt xPosition = new SendableInt("xPosition", xPositionStart.get());
    SendableInt yPosition = new SendableInt("yPosition", yPositionStart.get());
    SendableInt anglePosition = new SendableInt("anglePosition", 0);
    Gyro positionGyro = new Gyro(Constants.POSITIONING_GYRO);
    Vector position = Vector.ZERO;
    double lastDistance = 0;

    public PositioningSubsystem() {
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new PositioningCommand());
    }
    
    public void disable() {
    }
    
    public void enable() {
        positionGyro.reset();
    }

    public void updateVectors() {
        double gyroAngle = positionGyro.getAngle();
        double curDistance = CommandBase.chassisSubsystem.getAverageDistance();

        if (lastDistance == 0) {
            lastDistance = curDistance;
        }

        //Get angle and convert to radians
        //Shift by 90 degrees because vectors are calculated with 0 degrees being the x-axis
        //Negative to flip the sign of the y-axis
        double angle = (int) (gyroAngle - 90) * Math.PI / 180;

        //The distance we've travelled since our last update
        double distance = (curDistance - lastDistance) * ChassisSubsystem.INCHES_PER_ENCODER_COUNT;

        position.add(angle, distance);

        xPosition.set(xPositionStart.get() + (int) position.getX());
        yPosition.set(yPositionStart.get() + (int) position.getY());
        anglePosition.set((int) positionGyro.getAngle());

        lastDistance = curDistance;
    }

    //To be used for autonomous modes (update the dashboard based on the autonomous mode selected)
    public void setPosition(int x, int y) {
        xPosition.set(x);
        yPosition.set(y);
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("Gyro: " + positionGyro.getAngle());
    }
}
