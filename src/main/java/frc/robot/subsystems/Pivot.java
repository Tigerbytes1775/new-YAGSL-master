package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pivot extends SubsystemBase {
    
    private final CANSparkMax pivotMotor = new CANSparkMax(23, MotorType.kBrushless);
    
    public Pivot() {

        pivotMotor.setInverted(true);
		pivotMotor.setIdleMode(IdleMode.kBrake);
		pivotMotor.setSmartCurrentLimit(20);
		pivotMotor.burnFlash();

		// limit the direction of the arm's rotations (kReverse = up)
		pivotMotor.enableSoftLimit(SoftLimitDirection.kForward, false);
		pivotMotor.enableSoftLimit(SoftLimitDirection.kReverse, false);
        
    }

    public void setMotors(double percent) {
		pivotMotor.set(percent);
		SmartDashboard.putNumber("Pivot power(%)", percent);
		
	}


}
