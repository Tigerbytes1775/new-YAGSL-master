package frc.robot.commands.Teleop;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.Pivot;



public class TeleopPivotCommand extends Command {

	private Pivot pivot;
	private DoubleSupplier leftY;
	private BooleanSupplier leftBumper;
    
    public TeleopPivotCommand(Pivot pivot, DoubleSupplier leftY, BooleanSupplier leftBumper) {
		this.pivot = pivot;
		this.leftY = leftY;
		this.leftBumper = leftBumper;
		addRequirements(pivot);

	}

	@Override
	public void execute() {

		boolean leftBumper = this.leftBumper.getAsBoolean();
		double leftY = this.leftY.getAsDouble();

		this.pivot.setMotors(leftY  * (leftBumper ? 0.6 : 0.35));
	}

	@Override
    public boolean isFinished () { return false; }
}
