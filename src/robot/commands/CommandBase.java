package robot.commands;

import RobotCLI.WebServer;
import edu.wpi.first.wpilibj.command.Command;
import java.util.Enumeration;
import java.util.Hashtable;
import robot.OI;
import robot.StreamerHandler;
import robot.parsable.JSONPrintable;
import robot.parsable.Parsable;
import robot.parsable.ParsablePIDController;
import robot.parsable.SendableDouble;
import robot.parsable.SendableInt;
import robot.subsystems.ChassisSubsystem;
import robot.subsystems.HangerSubsystem;
import robot.subsystems.HopperSubsystem;
import robot.subsystems.PickupSubsystem;
import robot.subsystems.PositioningSubsystem;
import robot.subsystems.ShooterSubsystem;

public abstract class CommandBase extends Command {

    public static OI oi;
    // Create a single static instance of all of your subsystems
    public static WebServer webServer = new WebServer(8080);
    public static PositioningSubsystem positioningSubsystem = new PositioningSubsystem();
    public static ChassisSubsystem chassisSubsystem = new ChassisSubsystem();
    public static HopperSubsystem hopperSubsystem = new HopperSubsystem();
    public static ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
    public static PickupSubsystem pickupSubsystem = new PickupSubsystem();
    public static HangerSubsystem hangerSubsystem = new HangerSubsystem();

    private static void addPrintables(Hashtable hashtable) {
        Enumeration keys = hashtable.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            Object element = hashtable.get(key);
            if (element instanceof JSONPrintable) {
                StreamerHandler.addVariable(key, (JSONPrintable) element);
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
        webServer.registerHandler("/stream/list", streamerHandler.getListHandler());
        webServer.registerHandler("/stream/select", streamerHandler.getSelectHandler());
        webServer.registerHandler("/stream/deselect", streamerHandler.getDeselectHandler());
        webServer.registerHandler("/stream/ls", streamerHandler.getListHandler());
        webServer.registerHandler("/stream/add", streamerHandler.getSelectHandler());
        webServer.registerHandler("/stream/rm", streamerHandler.getDeselectHandler());

        addPrintables(ParsablePIDController.parsablePIDControllers);
        addPrintables(Parsable.parsables);
        addPrintables(SendableInt.sendableInts);
        addPrintables(SendableDouble.sendableDoubles);

        webServer.start();
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}
