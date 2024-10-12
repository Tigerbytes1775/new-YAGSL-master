package frc.robot;

import java.io.IOException;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import frc.lib.Controller;
import frc.robot.commands.Auto.IntakeCommand;
import frc.robot.commands.Auto.LaunchCommand;
import frc.robot.commands.Auto.PivotCommand;
import frc.robot.commands.Teleop.TeleopLaunchCommand;
import frc.robot.commands.Teleop.TeleopPivotCommand;
import frc.robot.commands.Teleop.TeleopClimbCommand;
import frc.robot.commands.Teleop.TeleopIntakeCommand;
import frc.robot.commands.swerve.BrakeMode;
import frc.robot.commands.swerve.Drive;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Launch;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Swerve;
import frc.robot.util.Alerts;
import frc.robot.util.Constants;

public class RobotContainer {

    private final SendableChooser<Command> autoChooser;
    Swerve swerve;
    private Controller driverOne;
    private XboxController MechDriver;

    public Pivot pivot = new Pivot();
    public Launch launch = new Launch();
    public Intake intake = new Intake();
    public Climb climb = new Climb();

    public RobotContainer() {
        //testing new auto call
        //public Command getAutonomousCommand() {
            //return new PathPlannerAuto();
        //}

        NamedCommands.registerCommand("LaunchSpeaker", new LaunchCommand(launch, 3, 1));
        NamedCommands.registerCommand("LaunchAmp", new LaunchCommand(launch, 3, 0.3));//should be 0.46
        NamedCommands.registerCommand("LaunchOff", new LaunchCommand(launch, 0.1, 0));

        NamedCommands.registerCommand("PivotIn", new PivotCommand(pivot, 2.5, 0.35));
        NamedCommands.registerCommand("PivotOut", new PivotCommand(pivot, 2.5,-0.35));
        NamedCommands.registerCommand("PivotOff", new PivotCommand(pivot, 0.1, 0));

        NamedCommands.registerCommand("IntakeIn", new IntakeCommand(intake, 0.75, -0.5));
        NamedCommands.registerCommand("IntakeOut", new IntakeCommand(intake, 0.75, 0.5));
        NamedCommands.registerCommand("IntakeOff", new IntakeCommand(intake, 0.1, 0));
        

        

        try { this.swerve = new Swerve(); } 
        catch (IOException ioException) { Alerts.swerveInitialized.set(true); }

        this.driverOne = new Controller(0);
        this.MechDriver = new XboxController(1);

        this.configureCommands();
        autoChooser = AutoBuilder.buildAutoChooser("mainshoot");
        SmartDashboard.putData("Auto Chooser", autoChooser);
        



        

        
        
    }

           //NEED FIX FOR AUTO (prolly not even java land copy past from youtube source geoffsmchit pathplanner2023Overveiw) not done leave early bye bye
    //private void configureAutoCommands(){
             //AUTO_EVENT_MAP.put("event1", new PrintCommand("passed marker 1"));

    //}     




    private void configureCommands () {
       



        this.swerve.setDefaultCommand(new Drive(
            this.swerve, 
            () -> MathUtil.applyDeadband(-this.driverOne.getHID().getLeftY(), Constants.SwerveConstants.TRANSLATION_DEADBAND),
            () -> MathUtil.applyDeadband(-this.driverOne.getHID().getLeftX(), Constants.SwerveConstants.TRANSLATION_DEADBAND), 
            () -> MathUtil.applyDeadband(this.driverOne.getHID().getRightX(), Constants.SwerveConstants.OMEGA_DEADBAND), 
            () -> this.driverOne.getHID().getPOV(),
            () -> this.driverOne.getHID().getLeftBumper(),
            () -> this.driverOne.getHID().getAButtonReleased()
        ));

        this.driverOne.x().onTrue(new InstantCommand(this.swerve::zeroGyro, this.swerve));
        this.driverOne.rightBumper().whileTrue(new RepeatCommand(new InstantCommand(this.swerve::lock, this.swerve)));

        this.launch.setDefaultCommand(new TeleopLaunchCommand(
            this.launch,
            () -> this.MechDriver.getAButtonReleased(),
            () -> this.MechDriver.getBButtonReleased()                
        ));

        this.pivot.setDefaultCommand(new TeleopPivotCommand(
            this.pivot,
            //() -> this.MechDriver.getLeftY(),
            () -> MathUtil.applyDeadband(this.MechDriver.getLeftY(), Constants.SwerveConstants.TRANSLATION_DEADBAND),
            () -> this.MechDriver.getLeftBumper()
        ));

        this.intake.setDefaultCommand(new TeleopIntakeCommand(
            this.intake,
            () -> MathUtil.applyDeadband(this.MechDriver.getLeftTriggerAxis(), Constants.SwerveConstants.TRANSLATION_DEADBAND) != 0,
            () -> MathUtil.applyDeadband(this.MechDriver.getRightTriggerAxis(), Constants.SwerveConstants.TRANSLATION_DEADBAND) != 0
        ));

        this.climb.setDefaultCommand(new TeleopClimbCommand(
            this.climb,
            () -> MathUtil.applyDeadband(this.MechDriver.getLeftTriggerAxis(), Constants.SwerveConstants.TRANSLATION_DEADBAND) != 0,
            () -> MathUtil.applyDeadband(this.MechDriver.getRightTriggerAxis(), Constants.SwerveConstants.TRANSLATION_DEADBAND) != 0
        ));

    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }
    public void setBrakeMode (boolean brake) { new BrakeMode(this.swerve, brake).schedule(); }
}
