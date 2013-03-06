package robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Scheduler;

public class AutonomousShooterCommand extends CommandBase {

    int shooterState = 0;
    
    public AutonomousShooterCommand(int shooterState) {
        requires(shooterSubsystem);
        
        this.shooterState = shooterState;
    }

    protected void initialize() {
    }

    protected void execute() {
        shooterSubsystem.setShooterState(shooterState);
        
        shooterSubsystem.doShooterState();
        
        shooterSubsystem.setSetpoint(1.0);
    }

    protected boolean isFinished() {
        if (!DriverStation.getInstance().isAutonomous()) {
            Scheduler.getInstance().add(new TeleopShooterCommand());
            return true;
        }
        
        //Run the shooter for the length of autonomous mode
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}