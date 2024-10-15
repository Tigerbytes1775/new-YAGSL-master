package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pivot extends SubsystemBase {

    private final CANSparkMax pivotMotor = new CANSparkMax(17, MotorType.kBrushless);

    public Pivot() {}

    public void setMotors(double percent) {

        pivotMotor.set(percent);
        SmartDashboard.putNumber("Pivot power(%)", percent);
        
    }
    
}
