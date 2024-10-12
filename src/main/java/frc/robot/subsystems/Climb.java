package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climb extends SubsystemBase{

    private final PWMVictorSPX climbMotor1 = new PWMVictorSPX(2);//Ids not determined
	private final PWMVictorSPX climbMotor2 = new PWMVictorSPX(4);//Ids not determined

    public Climb() {

    }

    public void setMotors(double percent) {
		climbMotor1.set(percent);
		climbMotor2.set(percent);
		SmartDashboard.putNumber("Climb power(%)", percent);
		if(percent == 0) {

			climbMotor1.stopMotor();
			climbMotor2.stopMotor();

		}
    }
}
