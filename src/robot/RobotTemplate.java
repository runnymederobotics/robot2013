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
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import robot.commands.AutonomousFiveFrisbeeCommandGroup;
import robot.commands.AutonomousSevenFrisbeeCommandGroup;
import robot.commands.AutonomousThreeFrisbeeCommandGroup;
import robot.commands.CommandBase;
import robot.parsable.ParsableInt;

public class RobotTemplate extends IterativeRobot {

    Compressor compressor = new Compressor(Constants.COMPRESSOR_DI, Constants.COMPRESSOR_RELAY);
    ParsableInt autonomousMode = new ParsableInt("autonomous_mode", 0);
    double lastPrintTime = 0;
    
    //Dynamic numbering system to handle single/double solenoids
    private static void initPneumatics() {
        //Primary module
        int primaryChannel = 0;
        if (Constants.SHIFTER_SINGLE_SOLENOID) {
            CommandBase.chassisSubsystem.shifterPneumatic = new Pneumatic(Constants.PRIMARY_MODULE, ++primaryChannel);
        } else {
            CommandBase.chassisSubsystem.shifterPneumatic = new Pneumatic(Constants.PRIMARY_MODULE, ++primaryChannel, ++primaryChannel);
        }
        if (Constants.SHOOTER_LOADER_SINGLE_SOLENOID) {
            CommandBase.hopperSubsystem.shooterLoader = new Pneumatic(Constants.PRIMARY_MODULE, ++primaryChannel);
        } else {
            CommandBase.hopperSubsystem.shooterLoader = new Pneumatic(Constants.PRIMARY_MODULE, ++primaryChannel, ++primaryChannel);
        }

        //Secondary module
        int secondaryChannel = 0;
        if (Constants.PICKUP_SINGLE_SOLENOID) {
            CommandBase.pickupSubsystem.pickupPneumatic = new Pneumatic(Constants.SECONDARY_MODULE, ++secondaryChannel);
        } else {
            CommandBase.pickupSubsystem.pickupPneumatic = new Pneumatic(Constants.SECONDARY_MODULE, ++secondaryChannel, ++secondaryChannel);
        }
        if (Constants.SHOOTER_LOW_SINGLE_SOLENOID) {
            CommandBase.shooterSubsystem.shooterPneumaticLow = new Pneumatic(Constants.SECONDARY_MODULE, ++secondaryChannel);
        } else {
            CommandBase.shooterSubsystem.shooterPneumaticLow = new Pneumatic(Constants.SECONDARY_MODULE, ++secondaryChannel, ++secondaryChannel);
        }
        if (Constants.SHOOTER_HIGH_SINGLE_SOLENOID) {
            CommandBase.shooterSubsystem.shooterPneumaticHigh = new Pneumatic(Constants.SECONDARY_MODULE, ++secondaryChannel);
        } else {
            CommandBase.shooterSubsystem.shooterPneumaticHigh = new Pneumatic(Constants.SECONDARY_MODULE, ++secondaryChannel, ++secondaryChannel);
        }
    }

    public void robotInit() {
        // Initialize all subsystems
        CommandBase.init();
        
        //Initialize our pneumatics. They are controlled by a dynamic numbering system based on whether or not they are double or single solenoids
        initPneumatics();

        compressor.start();
    }
    
    private void disableSubsystems() {
        CommandBase.chassisSubsystem.disable();
        CommandBase.hopperSubsystem.disable();
        CommandBase.positioningSubsystem.disable();
        CommandBase.shooterSubsystem.disable();
        CommandBase.pickupSubsystem.disable();
        CommandBase.hangerSubsystem.disable();
    }
    
    private void enableSubsystems() {
        CommandBase.chassisSubsystem.enable();
        CommandBase.hopperSubsystem.enable();
        CommandBase.positioningSubsystem.enable();
        CommandBase.shooterSubsystem.enable();
        CommandBase.pickupSubsystem.enable();
        CommandBase.hangerSubsystem.enable();
    }

    //This function is called at the start of disabled
    public void disabledInit() {
        disableSubsystems();
    }

    //This function is called periodically during disabled
    public void disabledPeriodic() {
        periodicPrint("Disabled");
    }

    //This function is called at the start of autonomous
    public void autonomousInit() {
        CommandGroup autonomousCommand = null;
        switch (autonomousMode.get()) {
            case 0:
                autonomousCommand = new AutonomousThreeFrisbeeCommandGroup();
                break;
            case 1: 
                autonomousCommand = new AutonomousSevenFrisbeeCommandGroup();
                break;
            case 2: 
                autonomousCommand = new AutonomousFiveFrisbeeCommandGroup();
                break;
        }
        
        if(autonomousCommand != null) {
            autonomousCommand.start();
        }
        
        enableSubsystems();
    }

    //This function is called periodically during autonomous
    public void autonomousPeriodic() {
        periodicPrint("Autonomous");

        Scheduler.getInstance().run();
    }

    //This function is called at the start of teleop
    public void teleopInit() {
        enableSubsystems();
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
            CommandBase.hangerSubsystem.print();

            lastPrintTime = now;
        }
    }
}
