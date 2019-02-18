/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.Command_ExtendBothAxles;
import frc.robot.commands.Command_MassMoveBack;
import frc.robot.commands.Command_MassMoveForward;
import frc.robot.commands.Command_RetractBothAxles;
import frc.robot.commands.Command_RetractFloatAxle;
import frc.robot.commands.Command_RetractMiddleAxle;
import frc.robot.subsystems.Subsystem_Pneumatics;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final Logger log = new Logger(Robot.class);

  // public static ExampleSubsystem m_subsystem = new ExampleSubsystem();
  public static Subsystem_Pneumatics m_subsystemPneumatics = Subsystem_Pneumatics.create();
  public static OI m_oi;

  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  Command_ExtendBothAxles extendBothAxles = new Command_ExtendBothAxles();
  Command_RetractBothAxles retractBothAxles = new Command_RetractBothAxles();
  Command_RetractMiddleAxle retractMiddleAxle = new Command_RetractMiddleAxle();
  Command_RetractFloatAxle retractFloatAxle = new Command_RetractFloatAxle();
  Command_MassMoveForward moveMassForward = new Command_MassMoveForward();
  Command_MassMoveBack moveMassBack = new Command_MassMoveBack();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    log.debug("Begin robotInit");
    m_oi = new OI();
    
    SmartDashboard.putData("Extend Both Axles", extendBothAxles);
    SmartDashboard.putData("Retract Both Axles", retractBothAxles);
    SmartDashboard.putData("Retract Middle Axle", retractMiddleAxle);
    SmartDashboard.putData("Retract Float Axle", retractFloatAxle);
    SmartDashboard.putData("Move Mass Forward", moveMassForward);
    SmartDashboard.putData("Move Mass Back", moveMassBack);

    log.debug("End robotInit");
   }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // log.debug("robotPeriodic");
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
    log.debug("disabledInit");
  }

  @Override
  public void disabledPeriodic() {
//    log.debug("disabledPeriodic");
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons
   * to the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {
    log.debug("autonomouseInit");
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    // log.debug("autonomousPeriodic");
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    log.debug("teleopInit");
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    // log.debug("teleopPeriodic");
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    // log.debug("testPeriodic");
  }
}
