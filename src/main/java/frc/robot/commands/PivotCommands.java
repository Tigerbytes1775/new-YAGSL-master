package frc.robot.commands;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class PivotCommands extends Command {

private final CANSparkMax pivotMotor = new CANSparkMax(23, MotorType.kBrushless);



    public Command setMotors(double percent) {
		pivotMotor.set(percent);
		SmartDashboard.putNumber("Pivot power(%)", percent);
        return null;
	}

}