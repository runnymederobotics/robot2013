package robot.parsable;

import RobotCLI.WebServer.JSONStringBuilder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import java.util.Hashtable;

//Wrapper class to make a parsable PIDController
public class ParsablePIDController implements JSONPrintable {

    public static Hashtable parsablePIDControllers = new Hashtable();
    String name;
    PIDController pidController;
    PIDSource pidSource;
    ParsableDouble p, i, d, f;

    public void jsonPrint(String name, JSONStringBuilder response) {
        JSONStringBuilder stringBuilder = response.startObject(name);

        stringBuilder.append("setpoint", pidController.getSetpoint());
        stringBuilder.append("output", pidController.get());
        stringBuilder.append("input", pidSource.pidGet());

        response.endObject();
    }

    public ParsablePIDController(String name, double Kp, double Ki, double Kd, double Kf, PIDSource source, PIDOutput output, double period) {
        makeParsable(name, Kp, Ki, Kd, Kf, source);
        pidController = new PIDController(Kp, Ki, Kd, Kf, source, output, period);
    }

    public ParsablePIDController(String name, double Kp, double Ki, double Kd, PIDSource source, PIDOutput output, double period) {
        makeParsable(name, Kp, Ki, Kd, 0.0, source);
        pidController = new PIDController(Kp, Ki, Kd, 0.0, source, output, period);
    }

    public ParsablePIDController(String name, double Kp, double Ki, double Kd, PIDSource source, PIDOutput output) {
        makeParsable(name, Kp, Ki, Kd, 0.0, source);
        pidController = new PIDController(Kp, Ki, Kd, 0.0, source, output);
    }

    public ParsablePIDController(String name, double Kp, double Ki, double Kd, double Kf, PIDSource source, PIDOutput output) {
        makeParsable(name, Kp, Ki, Kd, Kf, source);
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

    private void makeParsable(String name, double Kp, double Ki, double Kd, double Kf, PIDSource pidSource) {
        if (!name.startsWith("pid")) {
            name = "pid" + name;
        }
        this.name = name.toLowerCase();
        p = new ParsableDouble(this.name + "_p", Kp);
        i = new ParsableDouble(this.name + "_i", Ki);
        d = new ParsableDouble(this.name + "_d", Kd);
        f = new ParsableDouble(this.name + "_f", Kf);

        this.pidSource = pidSource;
        parsablePIDControllers.put(this.name, this);
    }

    public void updatePID() {
        pidController.setPID(p.get(), i.get(), d.get(), f.get());
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
