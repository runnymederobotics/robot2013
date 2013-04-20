package robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;
import robot.subsystems.ShooterSubsystem;

public class AutonomousSevenFrisbeeCommandGroup extends CommandGroup {

    public AutonomousSevenFrisbeeCommandGroup() {
        //WE SHOULD NOT RUN THE ROLLER UNTIL WE START MOVING
        addParallel(new AutonomousPickupCommand(true, true)); //Lower the pickup and run the roller
        addSequential(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.HIGH, Constants.SHOOTER_AUTONOMOUS_START_SETPOINT.get())); //Raise shooter to medium level
        addSequential(new AutonomousHopperCommand(false)); //Shoot all frisbees
        addSequential(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.LOAD));
        
        double p = CommandBase.chassisSubsystem.pidGyro.getP();
        double i = CommandBase.chassisSubsystem.pidGyro.getI();
        double d = CommandBase.chassisSubsystem.pidGyro.getD();
        double f = CommandBase.chassisSubsystem.pidGyro.getF();
        addSequential(new ChangePIDGyroValuesCommand(Constants.CHASSIS_GYRO_SEVEN_FRISBEE_P.get(), 0.0, 0.0, 0.0));
        addSequential(new AutonomousDriveCommand(Constants.SEVEN_FRISBEE_DRIVE_FORWARD_INCHES.get()));
        addSequential(new AutonomousDriveCommand(-Constants.SEVEN_FRISBEE_REVERSE_INCHES.get()));
        addSequential(new ChangePIDGyroValuesCommand(p, i, d, f));
        //addParallel(new AutonomousPickupCommand(true, false)); //Lower the pickup but dont run the roller
        addSequential(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.HIGH, Constants.SHOOTER_PYRAMID_SETPOINT.get())); //This will be for setting the setpoint after we've reached our destination
        addSequential(new AutonomousHopperCommand(true)); //Shoot all frisbees until the end of autonomous mode
    }
}
