package robot.subsystems;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.DirectionVector;
import robot.commands.CommandBase;
import robot.commands.PositioningCommand;
import robot.parsable.ParsableInt;
import robot.parsable.SendableInt;

public class PositioningSubsystem extends Subsystem {

    Gyro positionGyro = new Gyro(Constants.POSITIONING_GYRO);
    DirectionVector overallVector = DirectionVector.ZERO;
    ParsableInt xPositionStart = new ParsableInt("x_position_start", 162);
    ParsableInt yPositionStart = new ParsableInt("y_position_start", 216);
    SendableInt xPosition = new SendableInt("xPosition", xPositionStart.get());
    SendableInt yPosition = new SendableInt("yPosition", yPositionStart.get());
    SendableInt anglePosition = new SendableInt("anglePosition", 0);
    double lastUpdateTime = 0;
    double lastDistance = 0;
    double lastGyroAngle = 0;
    
    public PositioningSubsystem() {
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new PositioningCommand());
    }

    public void updateVectors() {
        //double now = Timer.getFPGATimestamp();
        double curGyroAngle = positionGyro.getAngle();
        double curDistance = CommandBase.chassisSubsystem.getAverageDistance();
        
        //if (lastUpdateTime == 0) {
        //    lastUpdateTime = now;
        //}
        if(lastDistance == 0) {
            lastDistance = curDistance;
        }
        if(lastGyroAngle == 0) {
            lastGyroAngle = curGyroAngle;
        }
        
        double averageGyroAngle = (curGyroAngle + lastGyroAngle) / 2;
        
        //Get angle and convert to radians
        //Shift by 90 degrees because vectors are calculated with 0 degrees being the x-axis
        double angle = (int) (averageGyroAngle - 90) * Math.PI / 180;

        //Get current rate in inches/unit time
        //double rate = CommandBase.chassisSubsystem.getAverageRate() * ChassisSubsystem.INCHES_PER_ENCODER_COUNT;
        
        //The distance we've travelled since our last update
        //Rate * change in time since last update
        //This will be magnitude of our vector
        //double distance = rate * (now - lastUpdateTime);
        
        double distance = (curDistance - lastDistance) * ChassisSubsystem.INCHES_PER_ENCODER_COUNT;
        
        if(angle != Double.NaN && distance != Double.NaN) {
            overallVector.add(angle, distance);
        }
        
        double overallAngle = overallVector.getAngle();
        double overallMagnitude = overallVector.getMagnitude();

        xPosition.set(xPositionStart.get() + (int) (overallMagnitude * Math.cos(overallAngle)));
        yPosition.set(yPositionStart.get() + (int) (overallMagnitude * Math.sin(overallAngle)));
        anglePosition.set((int) positionGyro.getAngle());

        //lastUpdateTime = now;
        lastDistance = curDistance;
        lastGyroAngle = curGyroAngle;
    }

    //To be used for autonomous modes (update the dashboard based on the autonomous mode selected)
    public void setPosition(int x, int y) {
        xPosition.set(x);
        yPosition.set(y);
    }
    
    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("Gyro: " + positionGyro.getAngle());
        System.out.println("overallVector angle:" + overallVector.getAngle() + " magnitude: " + overallVector.getMagnitude());
    }
}
