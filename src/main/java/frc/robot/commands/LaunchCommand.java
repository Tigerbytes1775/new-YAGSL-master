package frc.robot.commands;

import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class LaunchCommand extends Command {

    private final PWMVictorSPX launchMotor1 = new PWMVictorSPX(8);
	private final PWMVictorSPX launchMotor2 = new PWMVictorSPX(7);

    public Command setMotors(double percent) {
		launchMotor1.set(percent);
		launchMotor2.set(-percent);
		SmartDashboard.putNumber("Launch power(%)", percent);
        return null;
	
	}


}
