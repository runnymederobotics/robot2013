package robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Pneumatic;
import robot.commands.HopperCommand;

public class HopperSubsystem extends Subsystem {
    Pneumatic stackDropper = new Pneumatic(new DoubleSolenoid(3, 4));
    Pneumatic stackHolder = new Pneumatic(new Solenoid(5));
    Pneumatic shooterLoader = new Pneumatic(new DoubleSolenoid(6, 7));

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
