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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.Command_ClimbGoldBox;
import frc.robot.commands.Command_ClimbSilverBox;
import frc.robot.commands.Command_DriveManually;
import frc.robot.commands.Command_ExtendBothAxlesStart;
import frc.robot.commands.Command_ExtendBothAxlesStop;
import frc.robot.commands.Command_ExtendFloatAxleStart;
import frc.robot.commands.Command_ExtendFloatAxleStop;
import frc.robot.commands.Command_ExtendMiddleAxleStart;
import frc.robot.commands.Command_ExtendMiddleAxleStop;
import frc.robot.commands.Command_MoveMassBackStart;
import frc.robot.commands.Command_MoveMassBackStop;
import frc.robot.commands.Command_MoveMassForwardStart;
import frc.robot.commands.Command_MoveMassForwardStop;
import frc.robot.commands.Command_RetractBothAxlesStart;
import frc.robot.commands.Command_RetractBothAxlesStop;
import frc.robot.commands.Command_RetractFloatAxleStart;
import frc.robot.commands.Command_RetractFloatAxleStop;
import frc.robot.commands.Command_RetractMiddleAxleStart;
import frc.robot.commands.Command_RetractMiddleAxleStop;
import frc.robot.commands.Command_StartCompressor;
import frc.robot.commands.Command_StopCompressor;
import frc.robot.subsystems.Subsystem_DriveTrain;
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
  //
  // Initialize when Robot object is constructed.
  //
  public static OI m_oi = OI.create();

  //
  // Initialize when robotInit is called.
  //
  public static Subsystem_Pneumatics m_subsystemPneumatics = null;
  public static Subsystem_DriveTrain m_subsystemDriveTrain = null;

  //
  // Autonoumous Command
  //
  Command m_autonomousCommand = null;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    log.debug("Begin robotInit");

    //
    // Initialize subsystems and add to dashboard.
    //
    m_subsystemPneumatics = Subsystem_Pneumatics.create();
    m_subsystemDriveTrain = Subsystem_DriveTrain.create();

    //
    // Init autonomous command.
    //
    this.m_autonomousCommand = new Command_DriveManually();

    //
    // Init commands for testing.
    //
    Command_ExtendBothAxlesStart extendBothAxlesStart = new Command_ExtendBothAxlesStart();
    Command_ExtendBothAxlesStop extendBothAxlesStop = new Command_ExtendBothAxlesStop();
    Command_RetractBothAxlesStart retractBothAxlesStart = new Command_RetractBothAxlesStart();
    Command_RetractBothAxlesStop retractBothAxlesStop = new Command_RetractBothAxlesStop();
    Command_RetractMiddleAxleStart retractMiddleAxleStart = new Command_RetractMiddleAxleStart();
    Command_RetractMiddleAxleStop retractMiddleAxleStop = new Command_RetractMiddleAxleStop();
    Command_RetractFloatAxleStart retractFloatAxleStart = new Command_RetractFloatAxleStart();
    Command_RetractFloatAxleStop retractFloatAxleStop = new Command_RetractFloatAxleStop();
    Command_ExtendMiddleAxleStart extendMiddleAxleStart = new Command_ExtendMiddleAxleStart();
    Command_ExtendMiddleAxleStop extendMiddleAxleStop = new Command_ExtendMiddleAxleStop();
    Command_ExtendFloatAxleStart extendFloatAxleStart = new Command_ExtendFloatAxleStart();
    Command_ExtendFloatAxleStop extendFloatAxleStop = new Command_ExtendFloatAxleStop();
    Command_MoveMassForwardStart moveMassForwardStart = new Command_MoveMassForwardStart();
    Command_MoveMassForwardStop moveMassForwardStop = new Command_MoveMassForwardStop();
    Command_MoveMassBackStart moveMassBackStart = new Command_MoveMassBackStart();
    Command_MoveMassBackStop moveMassBackStop = new Command_MoveMassBackStop();
  
    Command_StartCompressor startCompressor = new Command_StartCompressor();
    Command_StopCompressor stopCompressor = new Command_StopCompressor();
  
    //
    // Put the subsystems onto the dashboard.
    //
    SmartDashboard.putData(m_subsystemDriveTrain);
    SmartDashboard.putData(m_subsystemPneumatics);
  

    //
    // Test dashboard widgets.
    //
    SmartDashboard.putData("Extend Both Axles Start", extendBothAxlesStart);
    SmartDashboard.putData("Extend Both Axles Stop", extendBothAxlesStop);
    SmartDashboard.putData("Retract Both Axles Start", retractBothAxlesStart);
    SmartDashboard.putData("Retract Both Axles Stop", retractBothAxlesStop);
    SmartDashboard.putData("Retract Middle Axle Start", retractMiddleAxleStart);
    SmartDashboard.putData("Retract Middle Axle Stop", retractMiddleAxleStop);
    SmartDashboard.putData("Retract Float Axle Start", retractFloatAxleStart);
    SmartDashboard.putData("Retract Float Axle Stop", retractFloatAxleStop);
    SmartDashboard.putData("Extend Middle Axle Start", extendMiddleAxleStart);
    SmartDashboard.putData("Extend Middle Axle Stop", extendMiddleAxleStop);
    SmartDashboard.putData("Extend Float Axle Start", extendFloatAxleStart);
    SmartDashboard.putData("Extend Float Axle Stop", extendFloatAxleStop);

    SmartDashboard.putData("Move Mass Forward Start", moveMassForwardStart);
    SmartDashboard.putData("Move Mass Forward Stop", moveMassForwardStop);
    SmartDashboard.putData("Move Mass Back Start", moveMassBackStart);
    SmartDashboard.putData("Move Mass Back Stop", moveMassBackStop);

    SmartDashboard.putData("Start Compressor", startCompressor);
    SmartDashboard.putData("Stop Compressor", stopCompressor);

    SmartDashboard.putData("Get on the Gold Box", new Command_ClimbGoldBox());
    SmartDashboard.putData("Get on the Silver Box", new Command_ClimbSilverBox());

    log.debug("End robotInit");
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // log.debug("robotPeriodic");
  }

  /**
   * This function is called once each time the robot enters Disabled mode. You
   * can use it to reset any subsystem information you want to clear when the
   * robot is disabled.
   */
  @Override
  public void disabledInit() {
    log.debug("disabledInit");
    
    //
    // Make sure we don't have any commands sitting in the scheduler that will execute when the robot is enabled.
    //
    Scheduler.getInstance().removeAll();
  }

  @Override
  public void disabledPeriodic() {
    // log.debug("disabledPeriodic");
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString code to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons to
   * the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {
    log.debug("autonomouseInit");
    //
    // Make sure we don't have any commands sitting in the scheduler that will execute.
    //
    Scheduler.getInstance().removeAll();

    //
    // Schedule the autonomous command.
    //
    this.m_autonomousCommand.start();
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
    
    //
    // Cancel the autonomous command.
    //
    this.m_autonomousCommand.cancel();

    //
    // Make sure we don't have any commands sitting in the scheduler that will execute.
    //
    Scheduler.getInstance().removeAll();
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
