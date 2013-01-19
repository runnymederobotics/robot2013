package robot.commands;

import RobotCLI.WebServer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.OI;
import robot.parsable.Parsable;
import robot.subsystems.ChassisSubsystem;
import robot.subsystems.HopperSubsystem;

public abstract class CommandBase extends Command {

    public static OI oi;
    // Create a single static instance of all of your subsystems
    public static WebServer webServer = new WebServer(8080);
    public static ChassisSubsystem chassisSubsystem = new ChassisSubsystem();
    public static HopperSubsystem hopperSubsystem = new HopperSubsystem();
    
    private static void addPrintables(Hashtable hashtable) {
        Enumeration keys = hashtable.keys();
        while(keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            Object element = hashtable.get(key);
            if(element instanceof JSONPrintable) {
                StreamerHandler.addVariable(key, (JSONPrintable)element);
            }
        }
    }
    
    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();

        StreamerHandler streamerHandler = new StreamerHandler();

        webServer.registerHandler("/constants", new Parsable.ParsablesHandler());
        webServer.registerStreamer("/stream", streamerHandler);
        webServer.registerHandler("/stream/list", streamer.getListHandler());
        webServer.registerHandler("/stream/select", streamer.getSelectHandler());
        webServer.registerHandler("/stream/deselect", streamer.getDeselectHandler());
        webServer.registerHandler("/stream/ls", streamer.getListHandler());
        webServer.registerHandler("/stream/add", streamer.getSelectHandler());
        webServer.registerHandler("/stream/rm", streamer.getDeselectHandler());

        addPrintables(ParsablePIDControllers.parsablePIDControllers);
        addPrintables(Parsable.parsables);
        
        webServer.start();
        
        // Show what command your subsystem is running on the SmartDashboard
        SmartDashboard.putData(chassisSubsystem);
        SmartDashboard.putData(hopperSubsystem);
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}
