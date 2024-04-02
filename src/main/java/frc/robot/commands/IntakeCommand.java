package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;

public class IntakeCommand extends Command {

	private Intake intake;
	private double percent = 0;
    
    public IntakeCommand(Intake intake, double percent) {

		addRequirements(intake);
		this.intake = intake;
		this.percent = percent;
		
	}

	@Override
    public void initialize() {

        intake.setMotors(percent);
		
	}


}
