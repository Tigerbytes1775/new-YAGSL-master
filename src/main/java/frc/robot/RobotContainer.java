package frc.robot;

import java.io.IOException;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import frc.lib.Controller;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.LaunchCommand;
import frc.robot.commands.PivotCommand;
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
    private Swerve swerve;
    private Controller driverOne;

    public Pivot pivot = new Pivot();
    public Launch launch = new Launch();
    public Intake intake = new Intake();
    public Climb climb = new Climb();

    public RobotContainer() {
        //testing new auto call
        //public Command getAutonomousCommand() {
            //return new PathPlannerAuto();
        //}

        try { this.swerve = new Swerve(); } 
        catch (IOException ioException) { Alerts.swerveInitialized.set(true); }


        this.driverOne = new Controller(0);

        this.configureCommands();
        
        autoChooser = AutoBuilder.buildAutoChooser("New Auto");
        SmartDashboard.putData("Auto Chooser", autoChooser);


        NamedCommands.registerCommand("LaunchSpeaker", new LaunchCommand(launch, 0.5));
        NamedCommands.registerCommand("LaunchAmp", new LaunchCommand(launch, 0.1));
        NamedCommands.registerCommand("LaunchOff", new LaunchCommand(launch, 0));

        NamedCommands.registerCommand("PivotIn", new PivotCommand(pivot, -0.35));
        NamedCommands.registerCommand("PivotOut", new PivotCommand(pivot, 0.35));
        NamedCommands.registerCommand("PivotOff", new PivotCommand(pivot, 0));

        NamedCommands.registerCommand("IntakeIn", new IntakeCommand(intake, -0.35));
        NamedCommands.registerCommand("IntakeOut", new IntakeCommand(intake, 0.35));
        NamedCommands.registerCommand("IntakeOff", new IntakeCommand(intake, 0));
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
                () -> this.driverOne.getHID().getPOV()
        ));
         
        



        this.driverOne.x().onTrue(new InstantCommand(this.swerve::zeroGyro, this.swerve));
        this.driverOne.leftBumper().whileTrue(new RepeatCommand(new InstantCommand(this.swerve::lock, this.swerve)));

        /**
        this.driverOne.leftTrigger().whileTrue(this.swerve.getDriveSysidRoutine());
        this.driverOne.rightTrigger().whileTrue(this.swerve.getAngleSysidRoutine());
        */
    }
    //TODO: add auto path
    //orig:
    //public Command getAutonomousCommand () { return this.swerve.getAutonomousCommand(); }
    //new
    //public Command getAutonomousCommand () { return autoChooser.getSelected(); }
    /*
     * public Command getAutonomousCommand() {
        PathPlannerPath path = PathPlannerPath.fromPathFile("line");
        return AutoBuilder.followPath(path);
     */
    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }
    public void setBrakeMode (boolean brake) { new BrakeMode(this.swerve, brake).schedule(); }
}
