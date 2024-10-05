package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Launch;
import frc.robot.subsystems.Pivot;


public class PivotCommand extends Command {

	private Pivot pivot;
	private double percent = 0;
	private final Timer timer;
	private double commandTime;
    
    public PivotCommand(Pivot pivot, double commandTime, double percent) {

		addRequirements(pivot);
		this.pivot = pivot;
		this.percent = percent;
		this.commandTime = commandTime;
		timer = new Timer();
		
	}

	@Override
    public void initialize() {

        pivot.setMotors(percent);
		
	}

	@Override
	public void end(boolean interrupted) {
		pivot.setMotors(0);
	}

	@Override
	public boolean isFinished() {
		return timer.get() > commandTime;
	}


}
