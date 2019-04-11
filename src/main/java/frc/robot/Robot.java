/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.Command_ClimbGoldStep1_Prepare;
import frc.robot.commands.Command_ClimbGoldStep2_RaiseRobot;
import frc.robot.commands.Command_ClimbGoldStep3_GetFixedAxleOn;
import frc.robot.commands.Command_ClimbGoldStep4_MoveMassForward;
import frc.robot.commands.Command_ClimbGoldStep5_GetMiddleAxleOn;
import frc.robot.commands.Command_ClimbGoldStep6_GetFloatAxleOn;
import frc.robot.commands.Command_ClimbGoldStep7_BumpUpFloatAxle;
import frc.robot.commands.Command_DriveManually;
import frc.robot.commands.Command_ExtendFloatAxleStart;
import frc.robot.commands.Command_ExtendFloatAxleStop;
import frc.robot.commands.Command_ExtendFootStart;
import frc.robot.commands.Command_ExtendFootStop;
import frc.robot.commands.Command_ExtendMiddleAxleStart;
import frc.robot.commands.Command_ExtendMiddleAxleStop;
import frc.robot.commands.Command_MoveMassBackStart;
import frc.robot.commands.Command_MoveMassBackStop;
import frc.robot.commands.Command_MoveMassForwardStart;
import frc.robot.commands.Command_MoveMassForwardStop;
import frc.robot.commands.Command_PulseExtendFloatAxle;
import frc.robot.commands.Command_PulseExtendFoot;
import frc.robot.commands.Command_ResetRobot;
import frc.robot.commands.Command_RetractFloatAxleStart;
import frc.robot.commands.Command_RetractFloatAxleStop;
import frc.robot.commands.Command_RetractFootStart;
import frc.robot.commands.Command_RetractFootStop;
import frc.robot.commands.Command_RetractMiddleAxleStart;
import frc.robot.commands.Command_RetractMiddleAxleStop;
import frc.robot.commands.Command_StartCompressor;
import frc.robot.commands.Command_StopCompressor;
import frc.robot.commands.Command_ToggleOpenLoopRampRate;
import frc.robot.subsystems.Subsystem_Antenna;
import frc.robot.subsystems.Subsystem_Compressor;
import frc.robot.subsystems.Subsystem_DriveTrain;
import frc.robot.subsystems.Subsystem_FloatAxle;
import frc.robot.subsystems.Subsystem_Foot;
import frc.robot.subsystems.Subsystem_Hatch;
import frc.robot.subsystems.Subsystem_HouseValve;
import frc.robot.subsystems.Subsystem_MassMover;
import frc.robot.subsystems.Subsystem_MiddleAxle;
import frc.team1123.camera.AIMRoboticsCameraServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final Logger log = LoggerFactory.getLogger(Robot.class);

  // private long aliveCount = 0;

  //
  // Initialize when Robot object is constructed.
  //
  public static OI m_oi = OI.create();

  //
  // Initialized when robotInit is called.
  //
  // public static Subsystem_Pneumatics m_subsystemPneumatics = null;
  public static Subsystem_DriveTrain m_subsystemDriveTrain = null;
  //
  // New subsystems for District Champs and Detroit!
  //
  public static Subsystem_Compressor m_subsystemCompressor = null;
  public static Subsystem_MassMover m_subsystemMassMover = null;
  public static Subsystem_FloatAxle m_subsystemFloatAxle = null;
  public static Subsystem_MiddleAxle m_subsystemMiddleAxle = null;
  public static Subsystem_Foot m_subsystemFoot = null;
  public static Subsystem_Antenna m_subsystemAntenna = null;
  public static Subsystem_Hatch m_subsystemHatch = null;
  public static Subsystem_HouseValve m_subsystemHouseValve = null;

  //
  // Autonoumous Command initialized wwhen robotInit is called.
  //
  Command m_autonomousCommand = null;

  //
  // Our Camera Server.
  //
  AIMRoboticsCameraServer cameraServer;

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
    m_subsystemCompressor = Subsystem_Compressor.create();
    m_subsystemDriveTrain = Subsystem_DriveTrain.create();
    m_subsystemMassMover = Subsystem_MassMover.create();
    m_subsystemFloatAxle = Subsystem_FloatAxle.create();
    m_subsystemMiddleAxle = Subsystem_MiddleAxle.create();
    m_subsystemFoot = Subsystem_Foot.create();
    m_subsystemAntenna = Subsystem_Antenna.create();
    m_subsystemHatch = Subsystem_Hatch.create();
    m_subsystemHouseValve = Subsystem_HouseValve.create();
  
    //
    // Init autonomous command.
    //
    this.m_autonomousCommand = new Command_DriveManually();

    //
    // Init driver control button commands
    //
    m_oi.getBumperRight().whenPressed(new Command_ToggleOpenLoopRampRate());

    //
    // Initialize the cameras
    //
    cameraServer = AIMRoboticsCameraServer.getInstance();
    for (int devId=0; devId<RobotMap.cameraCount;devId++)
      cameraServer.addCamera(devId, "camera_"+String.valueOf(devId), 640, 480, 30);
  
    //
    // Dashboard widgets.
    //
    // SmartDashboard.putData("Extend Both Axles Start", new Command_ExtendFloatAxleAndFootStart());
    // SmartDashboard.putData("Extend Both Axles Stop", new Command_ExtendFloatAxleAndFootStop());

    // SmartDashboard.putData("Retract Both Axles Start", new Command_RetractFloatAxleAndFootStart());
    // SmartDashboard.putData("Retract Both Axles Stop", new Command_RetractFloatAxleAndFootStop());

    SmartDashboard.putData("Extend Middle Axle Start", new Command_ExtendMiddleAxleStart());
    SmartDashboard.putData("Extend Middle Axle Stop", new Command_ExtendMiddleAxleStop());

    SmartDashboard.putData("Retract Middle Axle Start", new Command_RetractMiddleAxleStart());
    SmartDashboard.putData("Retract Middle Axle Stop", new Command_RetractMiddleAxleStop());

    SmartDashboard.putData("Extend Float Axle Start", new Command_ExtendFloatAxleStart());
    SmartDashboard.putData("Extend Float Axle Stop", new Command_ExtendFloatAxleStop());

    SmartDashboard.putData("Retract Float Axle Start", new Command_RetractFloatAxleStart());
    SmartDashboard.putData("Retract Float Axle Stop", new Command_RetractFloatAxleStop());

    SmartDashboard.putData("Extend Foot Start", new Command_ExtendFootStart());
    SmartDashboard.putData("Extend Foot Stop", new Command_ExtendFootStop());

    SmartDashboard.putData("Retract Foot Start", new Command_RetractFootStart());
    SmartDashboard.putData("Retract Foot Stop", new Command_RetractFootStop());

    SmartDashboard.putData("Move Mass Forward Start", new Command_MoveMassForwardStart());
    SmartDashboard.putData("Move Mass Forward Stop", new Command_MoveMassForwardStop());

    SmartDashboard.putData("Move Mass Back Start", new Command_MoveMassBackStart());
    SmartDashboard.putData("Move Mass Back Stop", new Command_MoveMassBackStop());

    SmartDashboard.putData("Start Compressor", new Command_StartCompressor());
    SmartDashboard.putData("Stop Compressor", new Command_StopCompressor());

    SmartDashboard.putData("Climb Step1 - Prepare", new Command_ClimbGoldStep1_Prepare());
    SmartDashboard.putData("Climb Step2 - Raise Bot", new Command_ClimbGoldStep2_RaiseRobot());
    SmartDashboard.putData("Climb Step3 - Fixed On", new Command_ClimbGoldStep3_GetFixedAxleOn());
    SmartDashboard.putData("Climb Step4 - Mass Forward", new Command_ClimbGoldStep4_MoveMassForward());
    SmartDashboard.putData("Climb Step5 - Middle On", new Command_ClimbGoldStep5_GetMiddleAxleOn());
    SmartDashboard.putData("Climb Step6 - Float On", new Command_ClimbGoldStep6_GetFloatAxleOn());
    SmartDashboard.putData("Climb Step7 - Bump Up Float", new Command_ClimbGoldStep7_BumpUpFloatAxle());

    SmartDashboard.putData("Reset the Robot", new Command_ResetRobot());
    SmartDashboard.putData("Pulse Extend Foot", new Command_PulseExtendFoot(0.25d));
    SmartDashboard.putData("Pulse Extend Float", new Command_PulseExtendFloatAxle(0.25d));

    SmartDashboard.putData("Scheduler", Scheduler.getInstance());


    Shuffleboard.getTab("Climb").add("Climb Step1 - Prepare", new Command_ClimbGoldStep1_Prepare());
    Shuffleboard.getTab("Climb").add("Climb Step2 - Raise Bot", new Command_ClimbGoldStep2_RaiseRobot());
    Shuffleboard.getTab("Climb").add("Climb Step3 - Fixed On", new Command_ClimbGoldStep3_GetFixedAxleOn());
    Shuffleboard.getTab("Climb").add("Climb Step4 - Mass Forward", new Command_ClimbGoldStep4_MoveMassForward());
    Shuffleboard.getTab("Climb").add("Climb Step5 - Middle On", new Command_ClimbGoldStep5_GetMiddleAxleOn());
    Shuffleboard.getTab("Climb").add("Climb Step6 - Float On", new Command_ClimbGoldStep6_GetFloatAxleOn());
    Shuffleboard.getTab("Climb").add("Climb Step7 - Bump Up Float", new Command_ClimbGoldStep7_BumpUpFloatAxle());
  
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
    // System.out.println("I'm alive ("+Long.toString(++this.aliveCount)+")");
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

  private StringBuilder _getDSGameInformation() {
    DriverStation ds = DriverStation.getInstance();
    StringBuilder gib = new StringBuilder();
    String crlf = System.getProperty("line.separator");
    gib.append(crlf).append("** DS Information ***************************************");
    gib.append(crlf).append("*  location.......: ").append(ds.getLocation());
    gib.append(crlf).append("*  match number...: ").append(ds.getMatchNumber());
    gib.append(crlf).append("*  replay number..: ").append(ds.getReplayNumber());
    gib.append(crlf).append("*  alliance.......: ").append(ds.getAlliance().name());
    gib.append(crlf).append("*  event name.....: ").append(ds.getEventName());
    gib.append(crlf).append("*  match type.....: ").append(ds.getMatchType().name());
    gib.append(crlf).append("*  game msg.......: ").append(ds.getGameSpecificMessage());
    gib.append(crlf).append("*********************************************************");
    return gib;
  }
}
