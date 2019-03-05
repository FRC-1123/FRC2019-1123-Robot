/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.Command_ClimbGoldBox;
import frc.robot.commands.Command_ClimbGoldStep1_Prepare;
import frc.robot.commands.Command_ClimbGoldStep2_RaiseRobot;
import frc.robot.commands.Command_ClimbGoldStep3_StabilizeRobot;
import frc.robot.commands.Command_ClimbGoldStep4_GetFixedAxleOn;
import frc.robot.commands.Command_ClimbGoldStep5_GetMiddleAxleOn;
import frc.robot.commands.Command_ClimbGoldStep6_GetFloatAxleOn;
import frc.robot.commands.Command_DriveManually;
import frc.robot.commands.Command_ExtendFloatAxleAndFootStart;
import frc.robot.commands.Command_ExtendFloatAxleAndFootStop;
import frc.robot.commands.Command_ExtendFloatAxleStart;
import frc.robot.commands.Command_ExtendFloatAxleStop;
import frc.robot.commands.Command_ExtendFootStart;
import frc.robot.commands.Command_ExtendFootStop;
import frc.robot.commands.Command_ExtendMiddleAxleStart;
import frc.robot.commands.Command_ExtendMiddleAxleStop;
import frc.robot.commands.Command_MoveBackOneInch;
import frc.robot.commands.Command_MoveForwardOneInch;
import frc.robot.commands.Command_MoveMassBackStart;
import frc.robot.commands.Command_MoveMassBackStop;
import frc.robot.commands.Command_MoveMassForwardStart;
import frc.robot.commands.Command_MoveMassForwardStop;
import frc.robot.commands.Command_RetractBothAxlesStart;
import frc.robot.commands.Command_RetractBothAxlesStop;
import frc.robot.commands.Command_RetractFloatAxleStart;
import frc.robot.commands.Command_RetractFloatAxleStop;
import frc.robot.commands.Command_RetractFootStart;
import frc.robot.commands.Command_RetractFootStop;
import frc.robot.commands.Command_RetractMiddleAxleStart;
import frc.robot.commands.Command_RetractMiddleAxleStop;
import frc.robot.commands.Command_StartCompressor;
import frc.robot.commands.Command_StopCompressor;
import frc.robot.commands.Command_ToggleAxleDelay;
import frc.robot.commands.Command_ToggleOpenLoopRampRate;
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
  public static final Accelerometer m_accelerometer = new BuiltInAccelerometer();

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
    // Init driver control button commands
    //
    m_oi.getBumperRight().whenPressed(new Command_ToggleOpenLoopRampRate());
  
    //
    // Put the subsystems onto the dashboard.
    //
    // SmartDashboard.putData(m_subsystemDriveTrain);
    // SmartDashboard.putData(m_subsystemPneumatics);
  
    //
    // Dashboard widgets.
    //
    SmartDashboard.putData("Extend Both Axles Start", new Command_ExtendFloatAxleAndFootStart());
    SmartDashboard.putData("Extend Both Axles Stop", new Command_ExtendFloatAxleAndFootStop());

    SmartDashboard.putData("Retract Both Axles Start", new Command_RetractBothAxlesStart());
    SmartDashboard.putData("Retract Both Axles Stop", new Command_RetractBothAxlesStop());

    SmartDashboard.putData("Retract Middle Axle Start", new Command_RetractMiddleAxleStart());
    SmartDashboard.putData("Retract Middle Axle Stop", new Command_RetractMiddleAxleStop());

    SmartDashboard.putData("Retract Float Axle Start", new Command_RetractFloatAxleStart());
    SmartDashboard.putData("Retract Float Axle Stop", new Command_RetractFloatAxleStop());

    SmartDashboard.putData("Extend Middle Axle Start", new Command_ExtendMiddleAxleStart());
    SmartDashboard.putData("Extend Middle Axle Stop", new Command_ExtendMiddleAxleStop());

    SmartDashboard.putData("Extend Float Axle Start", new Command_ExtendFloatAxleStart());
    SmartDashboard.putData("Extend Float Axle Stop", new Command_ExtendFloatAxleStop());

    SmartDashboard.putData("Move Mass Forward Start", new Command_MoveMassForwardStart());
    SmartDashboard.putData("Move Mass Forward Stop", new Command_MoveMassForwardStop());

    SmartDashboard.putData("Move Mass Back Start", new Command_MoveMassBackStart());
    SmartDashboard.putData("Move Mass Back Stop", new Command_MoveMassBackStop());

    SmartDashboard.putData("Extend Foot Start", new Command_ExtendFootStart());
    SmartDashboard.putData("Extend Foot Stop", new Command_ExtendFootStop());

    SmartDashboard.putData("Retract Foot Start", new Command_RetractFootStart());
    SmartDashboard.putData("Retract Foot Stop", new Command_RetractFootStop());

    SmartDashboard.putData("Start Compressor", new Command_StartCompressor());
    SmartDashboard.putData("Stop Compressor", new Command_StopCompressor());

    SmartDashboard.putData("Move Forward 1 Inch", new Command_MoveForwardOneInch());
    SmartDashboard.putData("Move Back 1 Inch", new Command_MoveBackOneInch());

    SmartDashboard.putData("Climb Box Step1 - Prepare", new Command_ClimbGoldStep1_Prepare());
    SmartDashboard.putData("Climb Box Step2 - Raise Bot", new Command_ClimbGoldStep2_RaiseRobot());
    SmartDashboard.putData("Climb Box Step3 - Stabilize", new Command_ClimbGoldStep3_StabilizeRobot());
    SmartDashboard.putData("Climb Box Step4 - Fixed On", new Command_ClimbGoldStep4_GetFixedAxleOn());
    SmartDashboard.putData("Climb Box Step5 - Middle On", new Command_ClimbGoldStep5_GetMiddleAxleOn());
    SmartDashboard.putData("Climb Box Step6 - Float On", new Command_ClimbGoldStep6_GetFloatAxleOn());

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
    // double x = m_accelerometer.getX();
    // double y = m_accelerometer.getY();
    // double z = m_accelerometer.getZ();
    // SmartDashboard.putNumber("Accelerometer X = ", x);
    // SmartDashboard.putNumber("Accelerometer Y = ", y);
    // SmartDashboard.putNumber("Accelerometer Z = ", z);
    SmartDashboard.putNumber("Ramp Rate = ", m_subsystemDriveTrain.getOpenLoopRampRate());
    // SmartDashboard.putNumber("Axle Delay = ", m_oi.getAxleDelay());
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
