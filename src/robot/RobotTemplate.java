/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import robot.commands.CommandBase;

public class RobotTemplate extends IterativeRobot {

    Compressor compressor = new Compressor(Constants.COMPRESSOR_DI, Constants.COMPRESSOR_RELAY);
    double lastPrintTime = 0;
    
    //Dynamic numbering system to handle single/double solenoids
    private static void initPneumatics() {
        //Primary module
        int primaryChannel = 0;
        if (Constants.SHIFTER_SINGLE_SOLENOID) {
            CommandBase.chassisSubsystem.shifterPneumatic = new Pneumatic(true, Constants.PRIMARY_MODULE, ++primaryChannel);
        } else {
            CommandBase.chassisSubsystem.shifterPneumatic = new Pneumatic(Constants.PRIMARY_MODULE, ++primaryChannel, ++primaryChannel);
        }
        if (Constants.SHOOTER_LOADER_SINGLE_SOLENOID) {
            CommandBase.hopperSubsystem.shooterLoader = new Pneumatic(true, Constants.PRIMARY_MODULE, ++primaryChannel);
        } else {
            CommandBase.hopperSubsystem.shooterLoader = new Pneumatic(Constants.PRIMARY_MODULE, ++primaryChannel, ++primaryChannel);
        }
        if (Constants.STACK_HOLDER_SINGLE_SOLENOID) {
            CommandBase.hopperSubsystem.stackHolder = new Pneumatic(true, Constants.PRIMARY_MODULE, ++primaryChannel);
        } else {
            CommandBase.hopperSubsystem.stackHolder = new Pneumatic(Constants.PRIMARY_MODULE, ++primaryChannel, ++primaryChannel);
        }
        if (Constants.STACK_DROPPER_SINGLE_SOLENOID) {
            CommandBase.hopperSubsystem.stackDropper = new Pneumatic(true, Constants.PRIMARY_MODULE, ++primaryChannel);
        } else {
            CommandBase.hopperSubsystem.stackDropper = new Pneumatic(Constants.PRIMARY_MODULE, ++primaryChannel, ++primaryChannel);
        }

        //Secondary module
        int secondaryChannel = 0;
        if (Constants.PICKUP_SINGLE_SOLENOID) {
            CommandBase.pickupSubsystem.pickupPneumatic = new Pneumatic(true, Constants.SECONDARY_MODULE, ++secondaryChannel);
        } else {
            CommandBase.pickupSubsystem.pickupPneumatic = new Pneumatic(Constants.SECONDARY_MODULE, ++secondaryChannel, ++secondaryChannel);
        }
        if (Constants.SHOOTER_A_SINGLE_SOLENOID) {
            CommandBase.shooterSubsystem.shooterPneumaticA = new Pneumatic(true, Constants.SECONDARY_MODULE, ++secondaryChannel);
        } else {
            CommandBase.shooterSubsystem.shooterPneumaticA = new Pneumatic(Constants.SECONDARY_MODULE, ++secondaryChannel, ++secondaryChannel);
        }
        if (Constants.SHOOTER_B_SINGLE_SOLENOID) {
            CommandBase.shooterSubsystem.shooterPneumaticB = new Pneumatic(true, Constants.SECONDARY_MODULE, ++secondaryChannel);
        } else {
            CommandBase.shooterSubsystem.shooterPneumaticB = new Pneumatic(Constants.SECONDARY_MODULE, ++secondaryChannel, ++secondaryChannel);
        }
    }

    public void robotInit() {
        // Initialize all subsystems
        CommandBase.init();
        
        //Initialize our pneumatics. They are controlled by a dynamic numbering system based on whether or not they are double or single solenoids
        initPneumatics();

        compressor.start();
    }

    //This function is called at the start of disabled
    public void disabledInit() {
    }

    //This function is called periodically during disabled
    public void disabledPeriodic() {
        periodicPrint("Disabled");
    }

    //This function is called at the start of autonomous
    public void autonomousInit() {
    }

    //This function is called periodically during autonomous
    public void autonomousPeriodic() {
        periodicPrint("Autonomous");

        Scheduler.getInstance().run();
    }

    //This function is called at the start of teleop
    public void teleopInit() {
    }

    //This function is called periodically during teleop
    public void teleopPeriodic() {
        periodicPrint("Teleop");

        Scheduler.getInstance().run();
    }

    //This function is called periodically during test
    public void testPeriodic() {
        LiveWindow.run();
    }

    void periodicPrint(String mode) {
        double now = Timer.getFPGATimestamp();
        if (now - lastPrintTime > Constants.PRINT_DELAY.get()) {
            System.out.println("------------------------- [" + mode + "] -------------------------");

            CommandBase.chassisSubsystem.print();
            CommandBase.hopperSubsystem.print();
            CommandBase.positioningSubsystem.print();
            CommandBase.shooterSubsystem.print();
            CommandBase.pickupSubsystem.print();

            lastPrintTime = now;
        }
    }
}
