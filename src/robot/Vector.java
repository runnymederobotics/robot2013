package robot;

public class Vector {

    public static final Vector ZERO = new Vector(0, 0);
    private double x;
    private double y;
    
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    public void add(double angle, double magnitude) {        
        x += magnitude * Math.cos(angle);
        y += magnitude * Math.sin(angle);
    }
}