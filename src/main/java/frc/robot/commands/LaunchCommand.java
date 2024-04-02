package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Launch;

public class LaunchCommand extends Command {

	private Launch launch;
	private double percent = 0;
    
    public LaunchCommand(Launch launch, double percent) {

		addRequirements(launch);
		this.launch = launch;
		this.percent = percent;
		
	}

	@Override
    public void initialize() {

        launch.setMotors(percent);
		
	}

	@Override
	public void execute()
	{
		launch.setMotors(percent);
	}

	@Override
	public boolean isFinished()
	{
		return false;
	}

	@Override
	public void end(boolean interrupted)
	{
		launch.setMotors(0);
	}



}
