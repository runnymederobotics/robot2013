package robot.commands;

import RobotCLI.WebServer;
import edu.wpi.first.wpilibj.command.Command;
import java.util.Enumeration;
import java.util.Hashtable;
import robot.Constants;
import robot.OI;
import robot.Pneumatic;
import robot.StreamerHandler;
import robot.parsable.JSONPrintable;
import robot.parsable.Parsable;
import robot.parsable.ParsablePIDController;
import robot.parsable.SendableDouble;
import robot.parsable.SendableInt;
import robot.subsystems.ChassisSubsystem;
import robot.subsystems.HopperSubsystem;
import robot.subsystems.PickupSubsystem;
import robot.subsystems.PositioningSubsystem;
import robot.subsystems.ShooterSubsystem;

public abstract class CommandBase extends Command {

    public static OI oi;
    // Create a single static instance of all of your subsystems
    public static WebServer webServer = new WebServer(8080);
    public static ChassisSubsystem chassisSubsystem = new ChassisSubsystem();
    public static HopperSubsystem hopperSubsystem = new HopperSubsystem();
    public static PositioningSubsystem positioningSubsystem = new PositioningSubsystem();
    public static ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
    public static PickupSubsystem pickupSubsystem = new PickupSubsystem();

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

    //Dynamic numbering system to handle single/double solenoids
    private static void initPneumatics() {
        //Primary module
        int primaryChannel = 1;
        if (Constants.SOLENOID_SHIFTER_SINGLE) {
            chassisSubsystem.shifterPneumatic = new Pneumatic(true, Constants.PRIMARY_MODULE, primaryChannel++);
        } else {
            chassisSubsystem.shifterPneumatic = new Pneumatic(Constants.PRIMARY_MODULE, primaryChannel++, primaryChannel++);
        }
        if (Constants.SOLENOID_PICKUP_SINGLE) {
            pickupSubsystem.pickupPneumatic = new Pneumatic(true, Constants.PRIMARY_MODULE, primaryChannel++);
        } else {
            pickupSubsystem.pickupPneumatic = new Pneumatic(Constants.PRIMARY_MODULE, primaryChannel++, primaryChannel++);
        }
        if (Constants.SOLENOID_STACK_HOLDER_SINGLE) {
            hopperSubsystem.stackHolder = new Pneumatic(true, Constants.PRIMARY_MODULE, primaryChannel++);
        } else {
            hopperSubsystem.stackHolder = new Pneumatic(Constants.PRIMARY_MODULE, primaryChannel++, primaryChannel++);
        }
        if (Constants.SOLENOID_STACK_DROPPER_SINGLE) {
            hopperSubsystem.stackDropper = new Pneumatic(true, Constants.PRIMARY_MODULE, primaryChannel++);
        } else {
            hopperSubsystem.stackDropper = new Pneumatic(Constants.PRIMARY_MODULE, primaryChannel++, primaryChannel++);
        }

        //Secondary module
        int secondaryChannel = 1;
        if (Constants.SOLENOID_SHOOTER_LOADER_SINGLE) {
            hopperSubsystem.shooterLoader = new Pneumatic(true, Constants.SECONDARY_MODULE, secondaryChannel++);
        } else {
            hopperSubsystem.shooterLoader = new Pneumatic(Constants.SECONDARY_MODULE, secondaryChannel++, secondaryChannel++);
        }
        if (Constants.SOLENOID_SHOOTER_A_SINGLE) {
            shooterSubsystem.shooterPneumaticA = new Pneumatic(true, Constants.SECONDARY_MODULE, secondaryChannel++);
        } else {
            shooterSubsystem.shooterPneumaticA = new Pneumatic(Constants.SECONDARY_MODULE, secondaryChannel++, secondaryChannel++);
        }
        if (Constants.SOLENOID_SHOOTER_B_SINGLE) {
            shooterSubsystem.shooterPneumaticB = new Pneumatic(true, Constants.SECONDARY_MODULE, secondaryChannel++);
        } else {
            shooterSubsystem.shooterPneumaticB = new Pneumatic(Constants.SECONDARY_MODULE, secondaryChannel++, secondaryChannel++);
        }
    }

    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();

        //Initialize the ParsableInt and ParsableDouble variables in these classes.
        OI.Driver driver = new OI.Driver();
        OI.Operator operator = new OI.Operator();

        //Initialize our pneumatics. They are controlled by a dynamic numbering system based on whether or not they are double or single solenoids
        initPneumatics();

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
