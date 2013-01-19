package robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import robot.ParsableDouble;

public class HopperCommand extends CommandBase {
    
    public static ParsableDouble RELEASE_DELAY = new ParsableDouble("release_delay", 1);
    public static ParsableDouble STACK_HOLD_DELAY = new ParsableDouble("stack_hold_delay", 0.5);
    public static ParsableDouble STACK_RELEASE_DELAY = new ParsableDouble("stack_release_delay", 0.5);
    public static ParsableDouble FINISH_DELAY = new ParsableDouble("finish_delay", 0.5);
    
    boolean requestRelease = false;
    
    double lastReleaseTime = 0.0;
    double stackHoldTime = 0.0;
    double stackReleaseTime = 0.0;
    double finishTime = 0.0;
    
    public HopperCommand() {
    }
    
    protected void initialize() {
        requires(hopperSubsystem);
        hopperSubsystem.reset();
    }

    protected void execute() {
        double now = Timer.getFPGATimestamp();
        
        if(DriverStation.getInstance().isOperatorControl()) {
            //Add check for shooter up to speed
            if(now - lastReleaseTime > RELEASE_DELAY.get()) {
                requestRelease = oi.getRequestShot() ? true : requestRelease;
            }
        } else {
            requestRelease = true;
        }
        
        if(requestRelease) {
            //Record the time we start holding the stack
            stackHoldTime = !hopperSubsystem.getStackHolder() ? now : stackHoldTime;

            //Hold the stack
            hopperSubsystem.setStackHolder(true);
            
            //If we've waited long enough after we start holding the stack
            if(now - stackHoldTime > STACK_HOLD_DELAY.get()) {
                //Drop the stack and record the time
                stackReleaseTime = now;
                hopperSubsystem.setStackDropper(false);
            }
            
            if(now - stackReleaseTime > STACK_RELEASE_DELAY.get()) {
                //When we've left enough time for the stack to drop, reset the dropper and load the shooter
                finishTime = now;
                hopperSubsystem.setStackDropper(false);
                hopperSubsystem.setShooterLoader(true);
            }
            
            if(now - finishTime > FINISH_DELAY.get()) {
                //If we've waited long enough for the dropper and shooterLoader to do their stuff, then finish the release sequence
                requestRelease = false;
            }
        } else {
            //Reset all pneumatics
            hopperSubsystem.reset();
            
            if(DriverStation.getInstance().isAutonomous()) {
                end();
            }
        }
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
}
