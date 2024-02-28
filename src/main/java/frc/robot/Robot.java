package frc.robot;

import com.revrobotics.CANSparkMax;
//import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
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
	private final PWMVictorSPX intakeMotor = new PWMVictorSPX(9);
	private final PWMVictorSPX launchMotor1 = new PWMVictorSPX(0);
	private final PWMVictorSPX launchMotor2 = new PWMVictorSPX(1);

	private final double pivotOutput = 0.25;
	private final int pivotLimit = 20;

	private final double intakeOutput = 1;

	private final double launchPowerReduction = 0.5;
	//function to set the arm output power in the vertical direction
	public void setPivotMotor(double percent) {
		pivotMotor.set(percent);
		SmartDashboard.putNumber("Pivot power(%)", percent);
	}

	//function to set the arm output power in the horizontal direction
	public void setIntakeMotor(double percent) {
		intakeMotor.set(percent);
		SmartDashboard.putNumber("Intake power(%)", percent);
	}

	public void setLaunchMotor(double percent) {
		launchMotor1.set(percent);
		launchMotor2.set(percent);
		SmartDashboard.putNumber("Launch power(%)", percent);
	
	}
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

		intakeMotor.setInverted(false);


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
		double intakePower;

		// motion for the arm in the horizontal direction
		if (commandsController.getLeftTriggerAxis() > 0.5) {
		//extend the arm
		// we could set it to srmpower = armXOuptuPower x get left trigger axis ( test it on the pivot firs)
		intakePower = intakeOutput;

		 }
		 else if (commandsController.getRightTriggerAxis() > 0.5) {
		// //retract the arm
		intakePower = -intakeOutput;

		 }
		 else {
		 // do nothing and let it sit where it is
			intakePower = 0.0;
			intakeMotor.stopMotor();
		 //intakeMotor.setNeutralMode(NeutralMode.Brake);
		 }
		setIntakeMotor(intakePower);

		double pivotPower;
		// motion for the arm in the vertical direction
		if (commandsController.getLeftY() > 0.5) {
			//raise the arm
			
			pivotPower = pivotOutput;

		}
		else if (commandsController.getLeftY() < -0.5) {
			//lower the arm
			pivotPower = -pivotOutput;

		}
		else {
			//do nothing and let it sit where it is
			
			pivotPower = 0.0;
			pivotMotor.setIdleMode(IdleMode.kBrake);
		}
		setPivotMotor(pivotPower);
		System.out.println(pivotMotor.getAbsoluteEncoder());
		// Cancels all running commands at the start of test mode.
		double rollerPower;
		rollerPower = commandsController.getLeftY() * launchPowerReduction;
		setLaunchMotor(rollerPower);
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