package robot.subsystems;

import edu.wpi.first.wpilibj.Accelerometer;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.command.Subsystem;
import java.util.Enumeration;
import java.util.Vector;
import robot.DirectionVector;
import robot.commands.PositioningCommand;
import robot.parsable.SendableDouble;

public class PositioningSubsystem extends Subsystem {

    Gyro positionGyro = new Gyro(1);
    Accelerometer positionAccelerometer = new Accelerometer(2);
    Vector directionVectors = new Vector();
    double rate = 0;
    SendableDouble x = new SendableDouble("position.x", 0), y = new SendableDouble("position.y", 0);

    public PositioningSubsystem() {
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new PositioningCommand());
    }

    public void updateVectors() {
        double angle = positionGyro.getAngle();
        double acceleration = positionAccelerometer.getAcceleration();

        //If we add acceleration together at a high enough resolution we should get speed
        rate += acceleration;

        DirectionVector curVector = new DirectionVector(angle, rate);
        
        if (!directionVectors.isEmpty()) {
            //Add current DirectionVector and previous DirectionVector, and add it to our Vector of DirectionVector
            DirectionVector lastVector = (DirectionVector) directionVectors.lastElement();
            
            directionVectors.addElement(DirectionVector.addVectors(curVector, lastVector));
        } else {
            directionVectors.addElement(curVector);
        }
        
        
    }

    public void calculatePosition() {
        Enumeration elements = directionVectors.elements();

        DirectionVector overallVector = DirectionVector.ZERO;

        //Find the sum of all the DirectionVectors in the directionVectors Vector
        while (elements.hasMoreElements()) {
            DirectionVector curElement = (DirectionVector) elements.nextElement();
            overallVector.add(curElement);
        }

        double angle = overallVector.getAngle();
        double hypotenuse = overallVector.getMagnitude();

        x.set(hypotenuse * Math.cos(angle));
        y.set(hypotenuse * Math.sin(angle));
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("Gyro: " + positionGyro.getAngle());
        System.out.println("Accelerometer: " + positionAccelerometer.pidGet());
        System.out.println("directionVectors.size(): " + directionVectors.size());
    }
}
