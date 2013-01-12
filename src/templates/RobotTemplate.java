/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import templates.commands.CommandBase;

public class RobotTemplate extends IterativeRobot {
    private WebServer webServer;
    
    public void robotInit() {
        // instantiate the command used for the autonomous period

        // Initialize all subsystems
        CommandBase.init();
        
        webServer = new WebServer(8080);
        webServer.registerHandler("/hello/world", new WebServer.PrintHandler());
        System.out.println("Server starting at " + ((ServerSocketConnection) Connector.open("socket://:1234")).getLocalAddress());
        webServer.start();
    }
    
    public void disabledInit() {
    }
    
    public void disabledPeriodic() {
        periodicPrint("[Disabled]");
    }

    public void autonomousInit() {
    }

    //This function is called periodically during autonomous
    public void autonomousPeriodic() {
        periodicPrint("[Autonomous]");
        
        Scheduler.getInstance().run();
    }

    //This function is called at the start of teleop mode
    public void teleopInit() {
    }

    //This function is called periodically during operator control
    public void teleopPeriodic() {
        periodicPrint("[Teleop]");
        
        Scheduler.getInstance().run();
    }
    
    //This function is called periodically during test mode
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    double lastPrintTime = 0;
    void periodicPrint(String mode) {
        double now = Timer.getFPGATimestamp();
        if(now - lastPrintTime > Constants.PRINT_DELAY) {
            System.out.println(mode);
        }
    }
}
