package robot;

import com.sun.squawk.util.MathUtils;

public class DirectionVector {

    public static final DirectionVector ZERO = new DirectionVector(0, 0);
    private double angle;
    private double magnitude;

    public DirectionVector(double angle, double magnitude) {
        this.angle = angle;
        this.magnitude = magnitude;
    }

    public double getAngle() {
        return angle;
    }

    public double getMagnitude() {
        return magnitude;
    }
    
    //Doesn't create new objects in order to save resources
    public void add(double angle, double magnitude) {
        double thisX = this.magnitude * Math.cos(this.angle);
        double thisY = this.magnitude * Math.sin(this.angle);
        
        double addX = magnitude * Math.cos(angle);
        double addY = magnitude * Math.sin(angle);
        
        double newX = thisX + addX;
        double newY = thisY + addY;
        
        double newAngle = MathUtils.atan2(newY, newX);
        double newMagnitude = Math.sqrt(MathUtils.pow(newX, 2) + MathUtils.pow(newY, 2));
        
        this.angle = newAngle;
        this.magnitude = newMagnitude;
    }

    public void add(DirectionVector bVector) {
        DirectionVector ret = addVectors(this, bVector);

        angle = ret.getAngle();
        magnitude = ret.getMagnitude();
    }

    public static DirectionVector addVectors(DirectionVector aVector, DirectionVector bVector) {
        //Calculate EndPoint of aVector
        Point aEndPoint = new Point(aVector);
        //Calculate the EndPoint of bVector and translate it to the EndPoint of aVector
        Point bEndPoint = new Point(aEndPoint, bVector);
        
        //Calculate resultant
        DirectionVector resultant = bEndPoint.toVector();

        return resultant;
    }
}

class Point {

    public static final Point ZERO = new Point(0, 0);
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(DirectionVector vector) {
        double angle = vector.getAngle();
        double magnitude = vector.getMagnitude();
        //Determine x and y from the vector
        this.x = magnitude * Math.cos(angle);
        this.y = magnitude * Math.sin(angle);
    }

    public Point(Point origin, DirectionVector vector) {
        //Determine the endpoint of the vector
        this(vector);
        //Translate to origin
        translateToPoint(origin);
    }

    private void translateToPoint(Point point) {
        //Java didnt like that add(Point) was overridable when called from a constructor
        add(point);
    }

    public void add(Point point) {
        x += point.getX();
        y += point.getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public DirectionVector toVector() {
        return new DirectionVector(getAngleFromOrigin(), getDistanceFromOrigin());
    }

    public static double getAngle(Point point1, Point point2) {
        //Calculate the change in x and y
        double deltaX = point1.getX() - point2.getX();
        double deltaY = point1.getY() - point2.getY();

        //Trigonometrically solve for the angle
        return MathUtils.atan2(deltaY, deltaX);
    }

    public double getAngleFromOrigin() {
        return getAngle(this, ZERO);
    }

    public double getAngleFromPoint(Point point) {
        return getAngle(this, point);
    }

    public static double getDistance(Point point1, Point point2) {
        //Get the absolute value of the change in x and y
        double deltaX = Math.abs(point1.getX() - point2.getX());
        double deltaY = Math.abs(point1.getY() - point2.getY());

        //Pythagorean theorem
        return Math.sqrt(MathUtils.pow(deltaX, 2) + MathUtils.pow(deltaY, 2));
    }

    public double getDistanceFromOrigin() {
        return getDistance(this, ZERO);
    }

    public double getDistanceFromPoint(Point point) {
        return getDistance(this, point);
    }
}