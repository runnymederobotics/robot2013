package robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;
import robot.subsystems.ShooterSubsystem;

public class AutonomousThreeFrisbeeCommandGroup extends CommandGroup {

    public AutonomousThreeFrisbeeCommandGroup() {
        addParallel(new AutonomousPickupCommand(true, false)); //Lower the pickup
        addParallel(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.HIGH, Constants.SHOOTER_PYRAMID_SETPOINT.get())); //Raise shooter to medium level
        addSequential(new AutonomousHopperCommand(false)); //Shoot all frisbees
    }
}
