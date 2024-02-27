package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.util.Alerts;
import frc.robot.util.BuildConstants;
import frc.robot.util.Constants;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

	private final XboxController commandsController = new XboxController(1);

	private RobotContainer robotContainer;
	private Command autonomousCommand;
	private Timer disabledTimer;

	// PIVOT code
	private final CANSparkMax pivotMotor = new CANSparkMax(23, MotorType.kBrushless);

	private final double pivotOutput = 0.25;
	private final int pivotLimit = 20;

	//function to set the arm output power in the vertical direction
	public void setArmYAxisMotor(double percent) {
		System.out.println("Setting percent to pivotMotor");
		pivotMotor.set(percent);
		SmartDashboard.putNumber("armYAxis power(%)", percent);
	}

	//function to set the arm output power in the horizontal direction
	// public void setArmXAxisMotor(double percent) {
	// 	armXAxis.set(percent);
	// 	SmartDashboard.putNumber("armXaxis power(%)", percent);
	// }
	/**
	 * This function is run when the robot is first started up and should be used for any
	 * initialization code.
	 */
	@Override
	public void robotInit () {

		// initial conditions for arm
		pivotMotor.setInverted(true);
		pivotMotor.setIdleMode(IdleMode.kBrake);
		pivotMotor.setSmartCurrentLimit(pivotLimit);
		((CANSparkMax) pivotMotor).burnFlash();

		// limit the direction of the arm's rotations (kReverse = up)
		pivotMotor.enableSoftLimit(SoftLimitDirection.kForward, false);
		pivotMotor.enableSoftLimit(SoftLimitDirection.kReverse, false);

		// if bug with directly below, ignore and build
		if (BuildConstants.DIRTY == 1) { Alerts.versionControl.set(true); }

		this.robotContainer = new RobotContainer();
		this.disabledTimer = new Timer();

		Shuffleboard.getTab("Autonomous").add("Command Scheduler", CommandScheduler.getInstance());
	}

	@Override
	public void robotPeriodic () {

		CommandScheduler.getInstance().run();
	}

	@Override
	public void autonomousInit () {

		this.autonomousCommand = this.robotContainer.getAutonomousCommand();
		this.autonomousCommand.schedule();
	}

	@Override
	public void autonomousPeriodic () {}

	@Override
	public void teleopInit () {

		if (this.autonomousCommand != null) { this.autonomousCommand.cancel(); }
		this.robotContainer.setBrakeMode(true);
	}

	@Override
	public void teleopPeriodic () {
		//Code for the arm
		double pivotPower;

		// // motion for the arm in the horizontal direction
		// if (pivotMotor.getLeftTriggerAxis() > 0.5) {
		// //extend the arm
		// // we could set it to srmpower = armXOuptuPower x get left trigger axis ( test it on the pivot firs)
		// pivotPower = ArmXOutputPower;
		// //*pivotMotor.getLeftTriggerAxis();
		// }
		// else if (pivotMotor.getRightTriggerAxis() > 0.5) {
		// //retract the arm
		// pivotPower = -ArmXOutputPower;
		// //*pivotMotor.getRightTriggerAxis();
		// }
		// else {
		// // do nothing and let it sit where it is
		// pivotPower = 0.0;
		// //armXAxis.stopMotor();
		// armXAxis.setNeutralMode(NeutralMode.Brake);
		// }
		// setArmXAxisMotor(pivotPower);


		// motion for the arm in the vertical direction
		if (commandsController.getLeftY() > 0.5) {
			//raise the arm
			System.out.println("raising pivot");
			pivotPower = pivotOutput;
			//*pivotMotor.getLeftY();
		}
		else if (commandsController.getLeftY() < -0.5) {
			//lower the arm
			System.out.println("lowering pivot");
			pivotPower = -pivotOutput;
			//*pivotMotor.getLeftY();
		}
		else {
			//do nothing and let it sit where it is
			System.out.println("Doing nothing to pivot");
			pivotPower = 0.0;
			pivotMotor.setIdleMode(IdleMode. kBrake);
		}
		setArmYAxisMotor(pivotPower);
		// Cancels all running commands at the start of test mode.
		// double rollerPower;
		// rollerPower = driverController.getLeftY() * 0.5;
		// TopMotor.set(rollerPower);
		// BottomMotor.set(rollerPower);
	}

	@Override
	public void disabledInit () {

		pivotMotor.set(0);
		this.disabledTimer.reset();
		this.disabledTimer.start();
	}

	@Override
	public void disabledPeriodic () {

		if (this.disabledTimer.get() >= Constants.SwerveConstants.LOCK_TIME) {

			this.robotContainer.setBrakeMode(false);
		}
	}
	
	@Override
	public void testInit () {}

	@Override
	public void testPeriodic () {}

	@Override
	public void simulationInit () {}

	@Override
	public void simulationPeriodic () {}
}