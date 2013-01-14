package templates;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import java.util.Hashtable;

//Wrapper class to make a parsable PIDController
public class ParsablePIDController {
    String name;
    PIDController pidController;
    ParsableDouble p, i, d;
    
    public static Hashtable parsablePIDControllers = new Hashtable();
    
    public ParsablePIDController(String name, double Kp, double Ki, double Kd, double Kf, PIDSource source, PIDOutput output, double period) {
        makeParsable(name, Kp, Ki, Kd);
        pidController = new PIDController(Kp, Ki, Kd, Kf, source, output, period);
    }
    
    public ParsablePIDController(String name, double Kp, double Ki, double Kd, PIDSource source, PIDOutput output, double period) {
        makeParsable(name, Kp, Ki, Kd);
        pidController = new PIDController(Kp, Ki, Kd, source, output, period);
    }
    
    public ParsablePIDController(String name, double Kp, double Ki, double Kd, PIDSource source, PIDOutput output) {
        makeParsable(name, Kp, Ki, Kd);
        pidController = new PIDController(Kp, Ki, Kd, source, output);
    }
    
    public ParsablePIDController(String name, double Kp, double Ki, double Kd, double Kf, PIDSource source, PIDOutput output) {
        makeParsable(name, Kp, Ki, Kd);
        pidController = new PIDController(Kp, Ki, Kd, Kf, source, output);
    }
    
    public synchronized double get() {
        return pidController.get();
    }
    
    public synchronized void setInputRange(double minimumInput, double maximumInput) {
        pidController.setInputRange(minimumInput, maximumInput);
    }

    public synchronized void setOutputRange(double minimumOutput, double maximumOutput) {
        pidController.setOutputRange(minimumOutput, maximumOutput);
    }

    public synchronized void setSetpoint(double setpoint) {
        pidController.setSetpoint(setpoint);
    }

    public synchronized double getSetpoint() {
        return pidController.getSetpoint();
    }

    public synchronized double getError() {
        return pidController.getError();
    }
    
    public synchronized void setTolerance(double percent) {
        pidController.setTolerance(percent);
    }

    public synchronized void setAbsoluteTolerance(double absvalue) {
        pidController.setAbsoluteTolerance(absvalue);
    }

    public synchronized void setPercentTolerance(double percentage) {
        pidController.setPercentTolerance(percentage);
    }
    
    public synchronized boolean onTarget() {
        return pidController.onTarget();
    }

    public synchronized void enable() {
        pidController.enable();
    }

    public synchronized void disable() {
        pidController.disable();
    }

    public synchronized boolean isEnable() {
        return pidController.isEnable();
    }

    public synchronized void reset() {
        pidController.reset();
    }
    
    private void makeParsable(String name, double Kp, double Ki, double Kd) {
        if(!name.startsWith("pid")) {
            name = "pid" + name;
        }
        this.name = name;
        p = new ParsableDouble(name + ".p", Kp);
        i = new ParsableDouble(name + ".i", Ki);
        d = new ParsableDouble(name + ".d", Kd);
        parsablePIDControllers.put(name, this);
    }
    
    public void updatePID() {
        pidController.setPID(p.get(), i.get(), d.get());
    }
    
    //Do i need these?
    /*public void updateP() {
        pidController.setPID(p.get(), pidController.getI(), pidController.getD());
    }
    
    public void updateI() {
        pidController.setPID(pidController.getP(), i.get(), pidController.getD());
    }
    
    public void updateD() {
        pidController.setPID(pidController.getP(), pidController.getI(), d.get());
    }*/
    
    /*public static class PIDHandler implements WebServer.Handler {
        public String handle(Hashtable params) {
            String ret = "";
            
            return ret;
        }
    }*/
}
