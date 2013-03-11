package robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Pneumatic;
import robot.commands.HangerCommand;

public class HangerSubsystem extends Subsystem {

    //Initialized in RobotTemplate.java
    public Pneumatic hangerPneumatic;
    boolean releaseHanger = false;

    public HangerSubsystem() {
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new HangerCommand());
    }

    public void disable() {
    }

    public void enable() {
        hangerPneumatic.set(false);
    }

    public void raiseHanger(boolean value) {
        hangerPneumatic.set(value);
    }

    public void print() {
        System.out.println("[" + this.getName() + "]");
        System.out.println("hangerPneumatic: " + hangerPneumatic.get());
    }
}
