package frc.robot.commands.Teleop;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.Pivot;



public class TeleopPivotCommand extends Command {

	private Pivot pivot;
	private DoubleSupplier leftY;
	private BooleanSupplier leftBumper;
	
	private double pivotPower = 0;
    
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

		
		if(leftBumper) {
			pivotPower = leftY * 0.6;
		} else {
			pivotPower = leftY * 0.35;
		}
		this.pivot.setMotors(pivotPower);
	}

	@Override
    public boolean isFinished () { return false; }
}
