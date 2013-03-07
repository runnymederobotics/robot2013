package robot.commands;

public class AutonomousShooterCommand extends CommandBase {

    int shooterState = 0;
    boolean doSetpoint = false;
    double setpoint = 0.0;

    public AutonomousShooterCommand(int shooterState) {
        requires(shooterSubsystem);

        this.doSetpoint = false;
        this.shooterState = shooterState;
    }

    public AutonomousShooterCommand(double setpoint) {
        requires(shooterSubsystem);

        this.doSetpoint = true;
        this.setpoint = setpoint;
    }

    public AutonomousShooterCommand(int shooterState, double setpoint) {
        requires(shooterSubsystem);

        this.shooterState = shooterState;
        this.doSetpoint = true;
        this.setpoint = setpoint;
    }

    protected void initialize() {
        if (doSetpoint) {
            shooterSubsystem.setShooter(setpoint);
        }
    }

    protected void execute() {
        shooterSubsystem.setShooterState(shooterState);

        shooterSubsystem.doShooterState();
    }

    protected boolean isFinished() {
        return shooterSubsystem.getShooterState() == shooterState;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}