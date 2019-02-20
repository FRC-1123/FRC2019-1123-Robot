/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Logger;
import frc.robot.RobotMap;
import frc.robot.commands.Command_DriveManually;

/**
 * Drive Train subsystem supports tank drive. We stole code librally from the
 * WPILIB DifferentialDrivde class to make sure we've got all the safety stuff
 * correct.
 */
public class Subsystem_DriveTrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private static final String subsystemName = "Drive Train";

  //
  // Drive train motors.
  //
  private final CANSparkMax m_motorFixedA;
  private final CANSparkMax m_motorFixedB;
  private final CANSparkMax m_motorMiddleA;
  private final CANSparkMax m_motorMiddleB;
  private final CANSparkMax m_motorFloatA;
  private final CANSparkMax m_motorFloatB;
  private final CANSparkMax[] m_motors;
  private final CANSparkMax[] m_motorsSideA;
  private final CANSparkMax[] m_motorsSideB;

  public static final double kDefaultDeadband = 0.02;
  public static final double kDefaultMaxOutput = 1.0;

  protected double m_deadband = kDefaultDeadband;
  protected double m_maxOutput = kDefaultMaxOutput;

  //
  // Logger
  //
  private final Logger m_logger;

  //
  // Motor safety members
  //
  private static final long kDefaultSafetyExpiration = 100;

  private long m_expiration = kDefaultSafetyExpiration;
  private long m_stopTime = RobotController.getFPGATime();
  private final Object m_thisMutex = new Object();

  public Subsystem_DriveTrain(CANSparkMax motorFixedA, CANSparkMax motorFixedB, CANSparkMax motorMiddleA,
      CANSparkMax motorMiddleB, CANSparkMax motorFloatA, CANSparkMax motorFloatB) {
    super(subsystemName);

    this.m_logger = new Logger(Subsystem_DriveTrain.class);

    this.m_motors = new CANSparkMax[6];
    this.m_motorFixedA = motorFixedA;
    this.m_motors[0] = motorFixedA;
    this.m_motorFixedB = motorFixedB;
    this.m_motors[1] = motorFixedB;
    this.m_motorMiddleA = motorMiddleA;
    this.m_motors[2] = motorMiddleA;
    this.m_motorMiddleB = motorMiddleB;
    this.m_motors[3] = motorMiddleB;
    this.m_motorFloatA = motorFloatA;
    this.m_motors[4] = motorFloatA;
    this.m_motorFloatB = motorFloatB;
    this.m_motors[5] = motorFloatB;

    //
    // Set the inverted flags on the motors individually
    //
    m_logger.debug("Setting motor inverted flags.");
    this.m_motorFixedA.setInverted(RobotMap.invertedFlag_Side_A);
    this.m_motorMiddleA.setInverted(!RobotMap.invertedFlag_Side_A);
    this.m_motorFloatA.setInverted(RobotMap.invertedFlag_Side_A);
    this.m_motorFixedB.setInverted(RobotMap.invertedFlag_Side_B);
    this.m_motorMiddleB.setInverted(!RobotMap.invertedFlag_Side_B);
    this.m_motorFloatB.setInverted(RobotMap.invertedFlag_Side_B);

    //
    // Group side A and side B motors into speed controller groups.
    //
    m_logger.debug("Creating speed control groups for side A and B.");
    m_motorsSideA = new CANSparkMax[3];
    m_motorsSideA[0] = this.m_motorFixedA;
    m_motorsSideA[1] = this.m_motorMiddleA;
    m_motorsSideA[2] = this.m_motorFloatA;

    m_motorsSideB = new CANSparkMax[3];
    m_motorsSideB[0] = this.m_motorFixedB;
    m_motorsSideB[1] = this.m_motorMiddleB;
    m_motorsSideB[2] = this.m_motorFloatB;

    //
    // Use speed controller groups to create a differential drive.
    //
    m_logger.debug("Creating Differential Drive.");

    //
    // Add sendables for Dashboard reporting.
    //
    addChild(this.m_motorFixedA);
    addChild(this.m_motorMiddleA);
    addChild(this.m_motorFloatA);
    addChild(this.m_motorFixedB);
    addChild(this.m_motorMiddleB);
    addChild(this.m_motorFloatB);

  }

  public void drive(double leftSpeed, double rightSpeed) {
    this._tankDrive(leftSpeed, rightSpeed, true);
  }

  public void stop() {
    drive(0, 0);
    for (CANSparkMax motor : m_motors) {
      motor.stopMotor();
    }
  }

  public void setIdleCost() {
    stop();
    for (CANSparkMax motor : m_motors) {
      motor.setIdleMode(IdleMode.kCoast);
    }
  }

  public void setIdleBreak() {
    stop();
    for (CANSparkMax motor : m_motors) {
      motor.setIdleMode(IdleMode.kBrake);
    }
  }

  @Override
  public void periodic() {
    _check();
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new Command_DriveManually());
  }

  public static Subsystem_DriveTrain create() {
    //
    // Setup the individual motor controllers.
    //
    CANSparkMax motorFixedA = new CANSparkMax(RobotMap.driveMotor_Fixed_A, MotorType.kBrushless);
    CANSparkMax motorFixedB = new CANSparkMax(RobotMap.driveMotor_Fixed_B, MotorType.kBrushless);
    CANSparkMax motorMiddleA = new CANSparkMax(RobotMap.driveMotor_Middle_A, MotorType.kBrushless);
    CANSparkMax motorMiddleB = new CANSparkMax(RobotMap.driveMotor_Middle_B, MotorType.kBrushless);
    CANSparkMax motorFloatA = new CANSparkMax(RobotMap.driveMotor_Float_A, MotorType.kBrushless);
    CANSparkMax motorFloatB = new CANSparkMax(RobotMap.driveMotor_Float_B, MotorType.kBrushless);

    return new Subsystem_DriveTrain(motorFixedA, motorFixedB, motorMiddleA, motorMiddleB, motorFloatA, motorFloatB);
  }

  // ===============================================================================================
  // We implement our own tank drive code below so we can take advantage of
  // CANSparxMax features. Also by way of attribution we stole librally from
  // WPILIBJ to make sure
  // we had all of the safety features available. Wouldn't want the robot running
  // accross the
  // field at full speed uncontrolled! :)
  // ===============================================================================================
  /**
   * Tank Drive
   * 
   * @param leftSpeed    is a value in the interval [-1,1]
   * @param rightSpeed   is a value in the interval [-1,1]
   * @param squareInputs is a boolean indicating whether or not to square inputs
   *                     to reduce sensitivity to low values.
   */
  private void _tankDrive(double leftSpeed, double rightSpeed, boolean squareInputs) {

    leftSpeed = _limit(leftSpeed);
    leftSpeed = _applyDeadband(leftSpeed, m_deadband);

    rightSpeed = _limit(rightSpeed);
    rightSpeed = _applyDeadband(rightSpeed, m_deadband);

    // Square the inputs (while preserving the sign) to increase fine control
    // while permitting full power.
    if (squareInputs) {
      leftSpeed = Math.copySign(leftSpeed * leftSpeed, leftSpeed);
      rightSpeed = Math.copySign(rightSpeed * rightSpeed, rightSpeed);
    }

    for (int i = 0; i < 3; i++) {
      m_motorsSideA[i].set(leftSpeed);
      m_motorsSideB[i].set(rightSpeed);
    }

    _feed();
  }

  /**
   * Limit motor values to the -1.0 to +1.0 range.
   */
  private double _limit(double value) {
    if (value > 1.0) {
      return 1.0;
    }
    if (value < -1.0) {
      return -1.0;
    }
    return value;
  }

  /**
   * Returns 0.0 if the given value is within the specified range around zero. The
   * remaining range between the deadband and 1.0 is scaled from 0.0 to 1.0.
   *
   * @param value    value to clip
   * @param deadband range around zero
   */
  protected double _applyDeadband(double value, double deadband) {
    if (Math.abs(value) > deadband) {
      if (value > 0.0) {
        return (value - deadband) / (1.0 - deadband);
      } else {
        return (value + deadband) / (1.0 - deadband);
      }
    } else {
      return 0.0;
    }
  }

  //
  // Motor safety methods.
  //

  /**
   * Feed the motor safety object.
   *
   * <p>
   * Resets the timer on this object that is used to do the timeouts.
   */
  public void _feed() {
    synchronized (m_thisMutex) {
      m_stopTime = RobotController.getFPGATime() + m_expiration;
    }
  }

  /**
   * Check if this motor has exceeded its timeout. This method is called
   * periodically to determine if this motor has exceeded its timeout value. If it
   * has, the stop method is called, and the motor is shut down until its value is
   * updated again.
   */
  private void _check() {
    long stopTime;

    synchronized (m_thisMutex) {
      stopTime = m_stopTime;
    }

    if (RobotState.isDisabled() || RobotState.isTest()) {
      return;
    }

    if (stopTime < RobotController.getFPGATime()) {
      DriverStation.reportError("Drive Train... Output not updated often enough.", false);

      for (CANSparkMax motor : m_motors) {
        motor.stopMotor();
      }
    }
  }
}
