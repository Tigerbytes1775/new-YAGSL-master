package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Launch;

public class LaunchCommand extends Command {

	private Launch launch;
	private double percent = 0;
	private double commandTime;
	private final Timer timer;
    
    public LaunchCommand(Launch launch, double commandTime, double percent) {

		addRequirements(launch);
		timer = new Timer();
		this.launch = launch;
		this.percent = percent;
		this.commandTime = commandTime;
		
	}

	@Override
    public void initialize() {

        launch.setMotors(percent);
		timer.reset();
		timer.start();
		
	}

	@Override
	public void execute()
	{
		launch.setMotors(percent);
	}

	@Override
	public boolean isFinished()
	{
		return timer.get() < commandTime;
	}

	@Override
	public void end(boolean interrupted)
	{
		launch.setMotors(0);
	}



}
