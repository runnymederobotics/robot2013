package robot.commands;

public class ChangePIDGyroValuesCommand extends CommandBase {

    boolean changedValue = false;
    double p, i, d, f;

    public ChangePIDGyroValuesCommand(double p, double i, double d, double f) {
        this.p = p;
        this.i = i;
        this.d = d;
        this.f = f;
    }

    protected void initialize() {
    }

    protected void execute() {
        if (!changedValue) {
            chassisSubsystem.pidGyro.setP(p);
            chassisSubsystem.pidGyro.setI(i);
            chassisSubsystem.pidGyro.setD(d);
            chassisSubsystem.pidGyro.setF(f);
            changedValue = true;
        }
    }

    protected boolean isFinished() {
        return changedValue;
    }

    protected void end() {
    }

    protected void interrupted() {
        end();
    }
}
