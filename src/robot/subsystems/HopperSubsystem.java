package robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.Pneumatic;
import robot.commands.HopperCommand;

public class HopperSubsystem extends Subsystem {
    
    Pneumatic stackDropper = new Pneumatic(new DoubleSolenoid(Constants.SOLENOID_STACK_DROPPER_ONE, Constants.SOLENOID_STACK_DROPPER_TWO));
    Pneumatic stackHolder = new Pneumatic(new Solenoid(Constants.SOLENOID_STACK_HOLDER));
    Pneumatic shooterLoader = new Pneumatic(new DoubleSolenoid(Constants.SOLENOID_SHOOTER_LOADER_ONE, Constants.SOLENOID_SHOOTER_LOADER_TWO));

    public void initDefaultCommand() {
        setDefaultCommand(new HopperCommand());
    }
    
    public void reset() {
        setStackDropper(true);
        setStackHolder(false);
        setShooterLoader(false);
    }
    
    public void setStackHolder(boolean value) {
        stackHolder.set(value);
    }
    
    public boolean getStackHolder() {
        return stackHolder.get();
    }
    
    public void setStackDropper(boolean value) {
        stackHolder.set(value);
    }
    
    public boolean getStackDropper() {
        return stackDropper.get();
    }
    
    public void setShooterLoader(boolean value) {
        shooterLoader.set(value);
    }
    
    public boolean getShooterLoader() {
        return shooterLoader.get();
    }
}
