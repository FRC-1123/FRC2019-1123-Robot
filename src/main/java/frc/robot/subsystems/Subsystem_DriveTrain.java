/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.Command_DriveManually;

/**
 * Drive Train subsystem supports tank drive. We stole code librally from the
 * WPILIB DifferentialDrivde class to make sure we've got all the safety stuff
 * correct.
 */
public class Subsystem_DriveTrain extends Subsystem {
  private static final String subsystemName = "Drive Train";

  private static final double wheelCircumference = Math.PI * 6.0d;
  private static final double axleToMotorSpinRatio = 1.0d / 25.0d;
  private static final double inchesPerMotorRotation = wheelCircumference * axleToMotorSpinRatio;

  /**
   * The {@link MotorSafety} object is reference in the
   * {@link edu.wpi.first.wpilibj.DriverStation DriverStation}. An independent
   * thread is started by the {@link edu.wpi.first.wpilibj.DriverStation
   * DriverStation} object when it is instantiated at Robot creation time. This
   * thread looks for driver station input as well as calls {@link MotorSafety}
   * methods to make sure that we don't have a runaway robot.
   */
  private class DriveTrainSafety extends MotorSafety {
    @Override
    public void stopMotor() {
      for (CANSparkMax motor : m_motors) {
        motor.stopMotor();
      }
    }

    @Override
    public String getDescription() {
      return subsystemName;
    }
  }

  private DriveTrainSafety m_motorSafety;

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

  //
  // Drive Train Axle Motor Enable Flags
  //
  private boolean isFixedAxleEnabled;
  private boolean isMiddleAxleEnabled;
  private boolean isFloatAxleEnabled;

  //
  // Drive train motor encoders.
  //
  CANEncoder m_motorFixedAEncoder;
  CANEncoder m_motorFixedBEncoder;
  CANEncoder m_motorMiddleAEncoder;
  CANEncoder m_motorMiddleBEncoder;
  CANEncoder m_motorFloatAEncoder;
  CANEncoder m_motorFloatBEncoder;
  CANEncoder[] m_motorEncoders;

  //
  // Dead band and max output values for motor control.
  //
  public static final double kDefaultDeadband = 0.02;
  public static final double kDefaultMaxOutput = 1.0;

  protected double m_deadband = kDefaultDeadband;
  protected double m_maxOutput = kDefaultMaxOutput;

  public Subsystem_DriveTrain(CANSparkMax motorFixedA, CANSparkMax motorFixedB, CANSparkMax motorMiddleA,
      CANSparkMax motorMiddleB, CANSparkMax motorFloatA, CANSparkMax motorFloatB) {
    super(subsystemName);
    //
    // Setup drive train motors.
    //
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
    // Setting Axle Enable Flags
    //
    this.isFixedAxleEnabled = true;
    this.isFloatAxleEnabled = true;
    this.isMiddleAxleEnabled = true;

    //
    // Set the inverted flags on the motors individually
    //
    this.m_motorFixedA.setInverted(RobotMap.invertedFlag_Side_A);
    this.m_motorMiddleA.setInverted(!RobotMap.invertedFlag_Side_A);
    this.m_motorFloatA.setInverted(RobotMap.invertedFlag_Side_A);
    this.m_motorFixedB.setInverted(RobotMap.invertedFlag_Side_B);
    this.m_motorMiddleB.setInverted(!RobotMap.invertedFlag_Side_B);
    this.m_motorFloatB.setInverted(RobotMap.invertedFlag_Side_B);

    //
    // Group side A and side B motors into speed controller groups.
    //
    m_motorsSideA = new CANSparkMax[3];
    m_motorsSideA[0] = this.m_motorFixedA;
    m_motorsSideA[1] = this.m_motorMiddleA;
    m_motorsSideA[2] = this.m_motorFloatA;

    m_motorsSideB = new CANSparkMax[3];
    m_motorsSideB[0] = this.m_motorFixedB;
    m_motorsSideB[1] = this.m_motorMiddleB;
    m_motorsSideB[2] = this.m_motorFloatB;

    //
    // Get motor encoders.
    //
    m_motorEncoders = new CANEncoder[6];
    m_motorFixedAEncoder = m_motorFixedA.getEncoder();
    m_motorEncoders[0] = m_motorFixedAEncoder;
    m_motorFixedBEncoder = m_motorFixedB.getEncoder();
    m_motorEncoders[1] = m_motorFixedBEncoder;
    m_motorMiddleAEncoder = m_motorMiddleA.getEncoder();
    m_motorEncoders[2] = m_motorMiddleAEncoder;
    m_motorMiddleBEncoder = m_motorMiddleB.getEncoder();
    m_motorEncoders[3] = m_motorMiddleBEncoder;
    m_motorFloatAEncoder = m_motorFloatA.getEncoder();
    m_motorEncoders[4] = m_motorFixedAEncoder;
    m_motorFloatBEncoder = m_motorFloatB.getEncoder();
    m_motorEncoders[5] = m_motorFloatBEncoder;

    //
    // Add sendables for Dashboard reporting.
    //
    addChild(this.m_motorFixedA);
    addChild(this.m_motorMiddleA);
    addChild(this.m_motorFloatA);
    addChild(this.m_motorFixedB);
    addChild(this.m_motorMiddleB);
    addChild(this.m_motorFloatB);

    //
    // Instantiate motor safety object.
    //
    m_motorSafety = this.new DriveTrainSafety();
    m_motorSafety.setExpiration(0.2);
    m_motorSafety.setSafetyEnabled(true);

    //
    // Set idle behavior to break and reset motor distance on the encoders.
    //
    setIdleBreak();
    resetMotorDistance();
    setOpenLoopRampRate(RobotMap.driveRampRateNormal);
  }

  public void drive(double leftSpeed, double rightSpeed) {
    this._tankDrive(leftSpeed, rightSpeed, true);
  }

  public void enableFixedAxleMotors(boolean flag) {
    this.isFixedAxleEnabled = flag;
  }

  public boolean isFixedAxleEnabled() {
    return this.isFixedAxleEnabled;
  }

  public void enableMiddleAxleMotors(boolean flag) {
    this.isMiddleAxleEnabled = flag;
  }

 public boolean isMiddleAxleEnabled() {
   return this.isMiddleAxleEnabled;
 }

  public void enableFloatAxleMotors(boolean flag) {
    this.isFloatAxleEnabled = flag;
  }

  public boolean isFloatAxleEnabled() {
    return this.isFloatAxleEnabled;
  }

  public void driveStraightDistance(double distance, double speed) {
    if (distance < this.distanceTraveled()) {
      drive(speed, speed);
    } else {
      stop();
    }
  }

  public void rackAxlesStart() {
    stop();

    double pSpeed = 0.2d;
    double nSpeed = pSpeed * -1.0d;

    m_motorFloatA.set(pSpeed);
    m_motorFloatB.set(pSpeed);
    m_motorMiddleA.set(nSpeed);
    m_motorMiddleB.set(nSpeed);

  }

  public void rackAxlesStop() {
    stop();
  }

  public boolean isStopped() {
    boolean rtn = true;
    for (CANSparkMax motor : m_motors) {
      rtn = rtn && (motor.get() == 0);
    }
    return rtn;
  }

  public void stop() {
    drive(0, 0);
    for (CANSparkMax motor : m_motors) {
      motor.stopMotor();
    }
    m_motorSafety.feed();
  }

  public void resetMotorDistance() {
    stop();
    for (CANEncoder encoder : m_motorEncoders) {
      encoder.setPosition(0);
    }
  }

  public double distanceTraveled() {
    double motorTurns = 0;
    for (CANEncoder encoder : m_motorEncoders) {
      double cturns = encoder.getPosition();
      if (cturns > motorTurns)
        motorTurns = cturns;
    }
    double rtn = motorTurns * inchesPerMotorRotation; 
    return rtn;
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

  public void setIdleCoast() {
    stop();
    for (CANSparkMax motor : m_motors) {
      motor.setIdleMode(IdleMode.kCoast);
    }
  }

  public void setOpenLoopRampRate(double rate) {
    stop();
    for (CANSparkMax motor : m_motors) {
      CANError condCode = motor.setOpenLoopRampRate(rate);
    }
  }

  public double getOpenLoopRampRate() {
    double rtn = 0.0d;
    for (CANSparkMax motor : m_motors) {
      if (rtn<motor.getOpenLoopRampRate()) {
        rtn = motor.getOpenLoopRampRate();
      }
    }
    return rtn;
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

    Subsystem_DriveTrain rtn = new Subsystem_DriveTrain(motorFixedA, motorFixedB, motorMiddleA, motorMiddleB, motorFloatA, motorFloatB);
    return rtn;
  }

  // ===============================================================================================
  // We implement our own tank drive code below so we can take advantage of
  // CANSparxMax features and handle our physical motor configuration. Also by way
  // of attribution we stole librally from WPILIBJ to make sure we had all of the
  // safety features available. Wouldn't want the robot running accross the field
  // at full speed uncontrolled! :)
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

    //
    // Square the inputs (while preserving the sign) to increase fine control
    // while permitting full power.
    //
    if (squareInputs) {
      leftSpeed = Math.copySign(leftSpeed * leftSpeed, leftSpeed);
      rightSpeed = Math.copySign(rightSpeed * rightSpeed, rightSpeed);
    }

    // for (int i = 0; i < 3; i++) {
    //   m_motorsSideA[i].set(leftSpeed);
    //   m_motorsSideB[i].set(rightSpeed);
    // }

    if (isFixedAxleEnabled) {
      this.m_motorFixedA.set(leftSpeed);
      this.m_motorFixedB.set(rightSpeed);
    } else {
      this.m_motorFixedA.set(0);
      this.m_motorFixedB.set(0);
    } 
    
    if (isMiddleAxleEnabled) {
      this.m_motorMiddleA.set(leftSpeed);
      this.m_motorMiddleB.set(rightSpeed);
    } else {
      this.m_motorMiddleA.set(0);
      this.m_motorMiddleB.set(0);
    }

  if (isFloatAxleEnabled) {
    this.m_motorFloatA.set(leftSpeed);
    this.m_motorFloatB.set(rightSpeed);
  } else {
    this.m_motorFloatA.set(0);
    this.m_motorFloatB.set(0);
  }

    m_motorSafety.feed();
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

  private String _motorName(CANSparkMax motor) {
    String rtn = "Unknown";
    if (motor == m_motorFixedA) {
      rtn = "m_motorFixedA";
    } else if (motor == m_motorFixedB) {
      rtn = "m_motorFixedB";
    } else if (motor == m_motorMiddleA) {
      rtn = "m_motorMiddleA";
    } else if (motor == m_motorMiddleB) {
      rtn = "m_motorMiddleB";
    } else if (motor == m_motorFloatA) {
      rtn = "m_motorFloatA";
    } else if (motor == m_motorFloatB) {
      rtn = "m_motorFloatB";
    }
    return rtn;
  }

}
