package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase{

    private final PWMVictorSPX intakeMotor = new PWMVictorSPX(9);
 
    public Intake() {

        intakeMotor.setInverted(false);  

    }


    public void setMotors(double percent) {

		intakeMotor.set(percent);
		SmartDashboard.putNumber("Intake power(%)", percent);

        if(percent == 0) {
            
            intakeMotor.stopMotor();

        }
	}

}
