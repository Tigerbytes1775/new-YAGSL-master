package frc.robot.commands.Teleop;

import java.util.function.BooleanSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Launch;


public class TeleopLaunchCommand extends Command {

	private final Launch launch;
	private final boolean aButton;
	private final boolean bButton;

	private boolean launchOn = false;
	private double launchPower;
    
    public TeleopLaunchCommand(Launch launch, BooleanSupplier aButton, BooleanSupplier bButton) {

		addRequirements(launch);
		this.launch = launch;
		this.aButton = aButton.getAsBoolean();
		this.bButton = bButton.getAsBoolean();
		
	}

	@Override
	public void execute()
	{
		if(aButton) {
			launchPower = 1;
			launchOn = !launchOn;
		} else if(bButton) {
			launchPower = 0.475;
			launchOn = !launchOn;
		}
		this.launch.setMotors(launchPower);
	}

	@Override
    public boolean isFinished () { return false; }
}
