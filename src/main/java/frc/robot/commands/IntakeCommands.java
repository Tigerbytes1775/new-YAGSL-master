package frc.robot.commands;

import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class IntakeCommands extends Command {

    private final PWMVictorSPX intakeMotor = new PWMVictorSPX(9);

    public Command setMotors(double percent) {
		intakeMotor.set(percent);
		SmartDashboard.putNumber("Intake power(%)", percent);
        return null;
	}

}