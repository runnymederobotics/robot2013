package robot.commands;

import RobotCLI.WebServer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.OI;
import robot.Parsable;
import robot.subsystems.ChassisSubsystem;

public abstract class CommandBase extends Command {

    public static OI oi;
    // Create a single static instance of all of your subsystems
    public static ChassisSubsystem chassisSubsystem = new ChassisSubsystem();
    public static WebServer webServer = new WebServer(8080);
    
    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();

        webServer.registerHandler("/constants", new Parsable.ParsablesHandler());
        webServer.start();
        
        // Show what command your subsystem is running on the SmartDashboard
        SmartDashboard.putData(chassisSubsystem);
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}
