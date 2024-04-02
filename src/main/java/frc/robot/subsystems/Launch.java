package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Launch extends SubsystemBase{
    

    private final PWMVictorSPX launchMotor1 = new PWMVictorSPX(8);
	private final PWMVictorSPX launchMotor2 = new PWMVictorSPX(7);
    //private final CANSparkMax launchMotor1 = new CANSparkMax(6, MotorType.kBrushless);
    //private final CANSparkMax launchMotor2 = new CANSparkMax(7, MotorType.kBrushless);
    public Launch() {

        launchMotor1.setInverted(false);
        launchMotor2.setInverted(true);
        
    }

    public void setMotors(double percent) {

        launchMotor1.set(percent);
        launchMotor2.set(percent);
        SmartDashboard.putNumber("Launch power(%)", percent);
        
        if(percent == 0) {

            launchMotor1.stopMotor();
            launchMotor2.stopMotor();
            
        }        
        
    }

    
}
