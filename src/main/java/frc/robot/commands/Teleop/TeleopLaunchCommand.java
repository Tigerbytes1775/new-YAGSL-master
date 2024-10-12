package frc.robot.commands.Teleop;

import java.util.function.BooleanSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Launch;


public class TeleopLaunchCommand extends Command {

	private final Launch launch;
	private final BooleanSupplier aButton;
	private final BooleanSupplier bButton;

	private boolean launchOn = false;
	private double launchPower;
    
    public TeleopLaunchCommand(Launch launch, BooleanSupplier aButton, BooleanSupplier bButton) {

		addRequirements(launch);
		this.launch = launch;
		this.aButton = aButton;
		this.bButton = bButton;
		
	}

	@Override
	public void execute()
	{
		boolean aButton = this.aButton.getAsBoolean();
		boolean bButton = this.bButton.getAsBoolean();
		
		if(aButton) {
			launchPower = 1;
			launchOn = !launchOn;
		} else if(bButton) {
			launchPower = 0.475;
			launchOn = !launchOn;
		}
		//if launch on set power to launch power else set to 0
		this.launch.setMotors(launchOn ? launchPower : 0);
		
		
	}

	@Override
    public boolean isFinished () { return false; }
}
