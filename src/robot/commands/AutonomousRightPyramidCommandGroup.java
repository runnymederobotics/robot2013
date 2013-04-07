package robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;
import robot.subsystems.ShooterSubsystem;

public class AutonomousRightPyramidCommandGroup extends CommandGroup {

    public AutonomousRightPyramidCommandGroup() {
        addParallel(new AutonomousPickupCommand(true, false)); //Lower the pickup but don't run the roller
        addSequential(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.HIGH, Constants.SHOOTER_AUTONOMOUS_START_SETPOINT.get())); //Raise shooter to medium level
        addSequential(new AutonomousHopperCommand(false)); //Shoot all frisbees
        addSequential(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.LOAD, 0));
        
        addSequential(new AutonomousDriveCommand(-Constants.RIGHT_PYRAMID_INITIAL_REVERSE_INCHES.get()));
        addSequential(new AutonomousCurveDriveCommand(-Constants.RIGHT_PYRAMID_CURVE_INCHES.get(), Constants.RIGHT_PYRAMID_CURVE_CONSTANT.get()));
        addParallel(new AutonomousPickupCommand(true, true)); //Start running pickup
        addSequential(new AutonomousDriveCommand(Constants.RIGHT_PYRAMID_DRIVE_FORWARD_AFTER_CURVE_INCHES.get()));
        addSequential(new AutonomousDriveCommand(-Constants.RIGHT_PYRAMID_REVERSE_AFTER_PICKUP_INCHES.get()));
        
        addSequential(new AutonomousRotateCommand(true, Constants.RIGHT_PYRAMID_ROTATE_AFTER_PICKUP_ANGLE.get()));
        addSequential(new AutonomousDriveCommand(Constants.RIGHT_PYRAMID_FINAL_DRIVE_FORWARD_INCHES.get()));
        addSequential(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.HIGH, Constants.AUTONOMOUS_SIDE_PYRAMID_FINAL_SHOOT_SETPOINT.get()));
        addSequential(new AutonomousHopperCommand(true)); //Shoot all frisbees until the end of autonomous mode
    }
}
