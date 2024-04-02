package frc.robot;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.pathplanner.lib.auto.CommandUtil;
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
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Launch;
import frc.robot.subsystems.Pivot;
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
	Pivot pivot;
	Intake intake;
	Climb climb;

	Command AutomousCommand;

	// PIVOT code
	
	


	

	

	private final double pivotMultiplier = 0.35;
	private final double fastPivotMultiplier = 0.6;

	

	private final double intakeOutput = 0.50;
	
	private final double speakerPower = 1;
	private final double ampPower = 0.46;//was .475 at comp
	private final double reversePower = -0.15;
	private double rollerPower = 0;
	private boolean launchOn = false;

	private final double climbPower = 0.75;
 
	//function to set the arm output power in the vertical direction
	

	//function to set the arm output power in the horizontal direction
	

	
	
	

	
	/**
	 * This function is run when the robot is first started up and should be used for any
	 * initialization code.
	 */
	@Override
	public void robotInit () {

		// if bug with directly below, ignore and build
		if (BuildConstants.DIRTY == 1) { Alerts.versionControl.set(true); }

		this.robotContainer = new RobotContainer();
		this.disabledTimer = new Timer();
		launch = robotContainer.launch;
		pivot = robotContainer.pivot;
		intake = robotContainer.intake;
		climb = robotContainer.climb;

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
		if (commandsController.getLeftTriggerAxis() > 0.0825) {
		//extend the arm
		// we could set it to srmpower = armXOuptuPower x get left trigger axis ( test it on the pivot firs)
		intakePower = intakeOutput;

		 } else if (commandsController.getRightTriggerAxis() > 0.0825) {
		// //retract the arm
		intakePower = -intakeOutput;

		 } else {
		 // do nothing and let it sit where it is
			intakePower = 0;
		 //intakeMotor.setNeutralMode(NeutralMode.Brake);
		 }
		//setIntakeMotor(intakePower);
		 intake.setMotors(intakePower);

		
		
		
		SmartDashboard.putBoolean("Left Bumper", commandsController.getLeftBumperPressed());
		if(Math.abs(commandsController.getLeftY()) > 0.0825) {

			double pivotPower = commandsController.getLeftY();
			 
			if(commandsController.getLeftBumperPressed()) {

			pivotPower *= fastPivotMultiplier;

			} else {

			pivotPower *= pivotMultiplier;

			}

			pivot.setMotors(pivotPower);

		} else {

			pivot.setMotors(0);
		}
	 	
		
		
		
		
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
			
			launch.setMotors(reversePower);
		} else if(launchOn) {

			commandsController.setRumble(RumbleType.kBothRumble, 0.1);
			launch.setMotors(rollerPower);
		} else {
			rollerPower = 0;
			commandsController.setRumble(RumbleType.kBothRumble, 0);
			launch.setMotors(rollerPower);
		}
		
		
	
		
		
		
		if(driveController.getRightTriggerAxis() > 0.0825) {
			climb.setMotors(-climbPower);
		} else if (driveController.getLeftTriggerAxis() > 0.0825) {
			climb.setMotors(climbPower);
		} else {
			climb.setMotors(0);
		}
		
	}

	@Override
	public void disabledInit () {

		pivot.setMotors(0);
		launch.setMotors(0);
		intake.setMotors(0);
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