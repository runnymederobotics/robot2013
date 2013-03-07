package robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Scheduler;

public class AutonomousShooterCommand extends CommandBase {

    int shooterState = 0;
    double setpoint = 0.0;
    
    public AutonomousShooterCommand(int shooterState, double setpoint) {
        requires(shooterSubsystem);
        
        this.shooterState = shooterState;
        this.setpoint = setpoint;
    }

    protected void initialize() {
        shooterSubsystem.setSetpoint(setpoint);
    }

    protected void execute() {
        shooterSubsystem.setShooterState(shooterState);
        
        shooterSubsystem.doShooterState();
    }

    protected boolean isFinished() {
        if (!DriverStation.getInstance().isAutonomous()) {
            Scheduler.getInstance().add(new TeleopShooterCommand());
            return true;
        }
      
        return shooterSubsystem.getShooterState() == shooterState;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}