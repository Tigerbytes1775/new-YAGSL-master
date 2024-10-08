package frc.robot.commands.Auto;

import edu.wpi.first.units.Time;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;

public class IntakeCommand extends Command {

	private Intake intake;
	private double percent = 0;
	private double commandTime;
	private final Timer timer;
    
    public IntakeCommand(Intake intake, double commandTime, double percent) {

		addRequirements(intake);
		this.intake = intake;
		this.percent = percent;
		this.commandTime = commandTime;
		timer = new Timer();

		
	}

	@Override
    public void initialize() {

        
		timer.reset();
		timer.start();
		
	}

	@Override
	public void execute(){
		intake.setMotors(percent);
	}


	@Override
	public void end(boolean interrupted) {

		//intake.setMotors(0);

	}

	@Override
	public boolean isFinished() {
		
		return timer.get() > commandTime;
	}


}
