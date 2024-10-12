package frc.robot.commands.Teleop;

import java.util.function.BooleanSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climb;

public class TeleopClimbCommand extends Command {

	private final double climbStrenght = 1;

	private Climb climb;
    //drive controller
	private BooleanSupplier leftTrigger;
	private BooleanSupplier rightTrigger;

    
    public TeleopClimbCommand(Climb climb, BooleanSupplier leftTrigger, BooleanSupplier rightTrigger) {

		addRequirements(climb);
		this.climb = climb;
		this.leftTrigger = leftTrigger;
		this.rightTrigger = rightTrigger;
		
		
	}

	@Override
	public void execute(){
		
		double climbPower;
		boolean leftTrigger = this.leftTrigger.getAsBoolean();
		boolean rightTrigger = this.rightTrigger.getAsBoolean();

		
		if (leftTrigger) {

			climbPower = climbStrenght;

		} else if (rightTrigger) {
			
			climbPower = -climbStrenght;

		} else {
			
			climbPower = 0;

		}
		climb.setMotors(climbPower);

	}


	@Override
	public boolean isFinished() {return false;}


}
