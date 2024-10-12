package frc.robot.commands.Teleop;

import java.util.function.BooleanSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;

public class TeleopIntakeCommand extends Command {

	private final double intakeStrenght = 0.7;

	private Intake intake;
	//mech controller
	private BooleanSupplier leftTrigger;
	private BooleanSupplier rightTrigger;

    
    public TeleopIntakeCommand(Intake intake, BooleanSupplier leftTrigger, BooleanSupplier rightTrigger) {

		addRequirements(intake);
		this.intake = intake;
		this.leftTrigger = leftTrigger;
		this.rightTrigger = rightTrigger;
		
		
	}

	@Override
	public void execute(){
		
		double intakePower;
		boolean leftTrigger = this.leftTrigger.getAsBoolean();
		boolean rightTrigger = this.rightTrigger.getAsBoolean();

		
		if (leftTrigger) {

			intakePower = intakeStrenght;

		} else if (rightTrigger) {
			
			intakePower = -intakeStrenght;

		} else {
			
			intakePower = 0;

		}
		intake.setMotors(intakePower);

	}


	@Override
	public boolean isFinished() {return false;}


}
