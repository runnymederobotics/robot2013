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

    public void robotInit() {
        // Initialize all subsystems
        CommandBase.init();
        
        compressor.start();
    }

    //This function is called at the start of disabled
    public void disabledInit() {
    }

    //This function is called periodically during disabled
    public void disabledPeriodic() {
        periodicPrint("[Disabled]");
    }

    //This function is called at the start of autonomous
    public void autonomousInit() {
    }

    //This function is called periodically during autonomous
    public void autonomousPeriodic() {
        periodicPrint("[Autonomous]");

        Scheduler.getInstance().run();
    }

    //This function is called at the start of teleop
    public void teleopInit() {
    }

    //This function is called periodically during teleop
    public void teleopPeriodic() {
        periodicPrint("[Teleop]");

        Scheduler.getInstance().run();
    }

    //This function is called periodically during test
    public void testPeriodic() {
        LiveWindow.run();
    }

    void periodicPrint(String mode) {
        double now = Timer.getFPGATimestamp();
        if (now - lastPrintTime > Constants.PRINT_DELAY.get()) {
            System.out.println(mode + " *********************************************************");

            CommandBase.chassisSubsystem.print();
            //CommandBase.hopperSubsystem.print();
            CommandBase.positioningSubsystem.print();

            lastPrintTime = now;
        }
    }
}
