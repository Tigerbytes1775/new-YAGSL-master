package frc.robot;

import com.revrobotics.CANSparkMax;
//import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.lib.Controller;
import frc.robot.subsystems.Launch;
import frc.robot.util.Alerts;
import frc.robot.util.BuildConstants;
import frc.robot.util.Constants;
import java.lang.Math;



/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

	private final XboxController commandsController = new XboxController(1);
	private final XboxController driveController = new XboxController(0);

	private RobotContainer robotContainer;
	private Command autonomousCommand;
	private Timer disabledTimer;
	

	Launch launch;

	// PIVOT code
	private final CANSparkMax pivotMotor = new CANSparkMax(23, MotorType.kBrushless);
	private final PWMVictorSPX intakeMotor = new PWMVictorSPX(9);

	

	private final PWMVictorSPX climbMotor1 = new PWMVictorSPX(0);//Ids not determined
	private final PWMVictorSPX climbMotor2 = new PWMVictorSPX(1);//Ids not determined
	private final double fastPivot = 0.5;
	private final double slowPivot = 0.35;
	private final int pivotLimit = 20;
	private double pivotOutput = 0;

	private final double intakeOutput = 0.50;
	
	private final double speakerPower = 1;
	private final double ampPower = 0.46;//was .475 at comp
	private final double reversePower = -0.15;
	private double rollerPower = 0;
	private boolean launchOn = false;

	private final double climbPower = 0.1;
 
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

	
	
	public void setClimbMotors(double percent) {
		climbMotor1.set(percent);
		climbMotor2.set(-percent);
		SmartDashboard.putNumber("ClimbPower power(%)", percent);
		if(percent == 0) {
			climbMotor1.stopMotor();
			climbMotor2.stopMotor();
		}

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
		launch = robotContainer.launch;

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

		 } else if (commandsController.getRightTriggerAxis() > 0.5) {
		// //retract the arm
		intakePower = -intakeOutput;

		 } else {
		 // do nothing and let it sit where it is
			intakePower = 0.0;
			intakeMotor.stopMotor();
		 //intakeMotor.setNeutralMode(NeutralMode.Brake);
		 }
		//setIntakeMotor(intakePower);
		 intakeMotor.set(intakePower);

		double pivotPower = 0;
		/*// motion for the arm in the vertical direction
		if (commandsController.getLeftY() > 0.5) {
			//raise the arm
			
			pivotPower = -pivotOutput;

		} else if (commandsController.getLeftY() < -0.5) {
			//lower the arm
			pivotPower = pivotOutput;

		} else {
			//do nothing and let it sit where it is
			
			pivotPower = 0.0;
			pivotMotor.setIdleMode(IdleMode.kBrake);
		}*/

		if (commandsController.getLeftBumper()) {
			pivotOutput = fastPivot;
		} else {
			pivotOutput = slowPivot;
		}

		if(Math.abs(commandsController.getLeftY()) > 0.2) {
			pivotPower = commandsController.getLeftY() * pivotOutput;
		} else {
			pivotMotor.stopMotor();
		}
	 	
		setPivotMotor(-pivotPower);
		System.out.println(pivotMotor.getAbsoluteEncoder());
		// Cancels all running commands at the start of test mode.
		
		
		if(commandsController.getAButtonReleased()) {
			rollerPower = speakerPower;
			if(launchOn) {
				launchOn = false;
			} else {
				launchOn = true;
			}
		} else if(commandsController.getBButtonReleased()) {
			rollerPower = ampPower;
			if(launchOn) {
				launchOn = false;
			} else {
				launchOn = true;
			}
		}
		
		if(commandsController.getXButton()) {
			
			launch.setLaunchMotors(reversePower);
		} else if(launchOn) {

			commandsController.setRumble(RumbleType.kBothRumble, 0.2);
			launch.setLaunchMotors(rollerPower);
		} else {
			rollerPower = 0;
			commandsController.setRumble(RumbleType.kBothRumble, 0);
			launch.setLaunchMotors(rollerPower);
		}
		
		
	
		
		
		
		if(driveController.getRightBumper()) {
			setClimbMotors(-climbPower);
		} else if (driveController.getLeftBumper()) {
			setClimbMotors(climbPower);
		} else {
			setClimbMotors(0);
			climbMotor1.stopMotor();
			climbMotor2.stopMotor();
		}
		
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