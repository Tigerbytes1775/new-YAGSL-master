package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Launch;
import frc.robot.subsystems.Pivot;

public class PivotCommand extends Command {

	private Pivot pivot;
	private double percent = 0;
    
    public PivotCommand(Pivot pivot, double percent) {

		addRequirements(pivot);
		this.pivot = pivot;
		this.percent = percent;
		
	}

	@Override
    public void initialize() {

        pivot.setMotors(percent);
		
	}


}
