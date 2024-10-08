package frc.robot.commands.Teleop;

import java.util.function.BooleanSupplier;

import edu.wpi.first.networktables.BooleanSubscriber;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Launch;

public class TeleopLaunchCommand extends Command {

	private final double ampPower = 0.475;
	private final double speakerPower = 1;

	private Launch launch;
	private boolean aButton;
	private boolean bButton;

	private boolean launchOn = false;



    
    public TeleopLaunchCommand(Launch launch, BooleanSupplier aButton, BooleanSupplier bButton) {

		addRequirements(launch);
		this.aButton = aButton.getAsBoolean();
		this.bButton = bButton.getAsBoolean();
		
	}

	@Override
	public void execute()
	{
		if(aButton) {
			launch.setMotors(speakerPower);
			launchOn = !launchOn;
		} else if(bButton) {
			launch.setMotors(ampPower);
			launchOn = !launchOn;
		}
	}



}
