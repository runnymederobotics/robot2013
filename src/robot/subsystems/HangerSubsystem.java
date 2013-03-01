package robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.commands.HangerCommand;
import robot.parsable.ParsableDouble;

public class HangerSubsystem extends Subsystem {

    public static ParsableDouble SERVO_POSITION = new ParsableDouble("hanger_servo_position", 0.0);
    public static final double SERVO_ENGAGE_POSITION = 0.0;
    public static final double SERVO_RELEASE_POSITION = 0.0;
    Servo hangerServo = new Servo(Constants.HANGER_SERVO);
    boolean releaseHanger = false;
    
    public HangerSubsystem() {
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new HangerCommand());
    }

    public void disable() {
    }

    public void enable() {
        hangerServo.set(SERVO_ENGAGE_POSITION);
    }
    
    public void releaseHanger(boolean value) {
        /*releaseHanger = value ? true : releaseHanger;
        
        if(releaseHanger) {
            hangerServo.set(SERVO_RELEASE_POSITION);
        } else {
            hangerServo.set(SERVO_ENGAGE_POSITION);
        }*/
        //SERVO_POSITION will be used for testing purposes only. Once proper values are found, they will be put into SERVO_ENGAGE_POSITION and SERVO_RELEASE_POSITION permanently
        hangerServo.set(SERVO_POSITION.get());
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("hangerServo: " + hangerServo.get());
    }
}
