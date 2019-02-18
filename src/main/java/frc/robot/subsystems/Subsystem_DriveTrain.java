/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Logger;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class Subsystem_DriveTrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private static final String subsystemName = "Drive Train";

  //
  // Keys to report motor data to Smartdashboard.
  //
  private static final String keyMotorState_DevId_FixedA = subsystemName + "/Motor/Fixed A/DevId";
  private static final String keyMotorState_Temp_FixedA = subsystemName + "/Motor/Fixed A/Temp";
  private static final String keyMotorState_BusVolt_FixedA = subsystemName + "/Motor/Fixed A/BusVolt";
  private static final String keyMotorState_SetSpeed_FixedA = subsystemName + "/Motor/Fixed A/SetSpeed";
  private static final String keyMotorState_OutAmps_FixedA = subsystemName + "/Motor/Fixed A/OutAmps";

  private static final String keyMotorState_DevId_FixedB = subsystemName + "/Motor/Fixed B/DevId";
  private static final String keyMotorState_Temp_FixedB = subsystemName + "/Motor/Fixed B/Temp";
  private static final String keyMotorState_BusVolt_FixedB = subsystemName + "/Motor/Fixed B/BusVolt";
  private static final String keyMotorState_SetSpeed_FixedB = subsystemName + "/Motor/Fixed B/SetSpeed";
  private static final String keyMotorState_OutAmps_FixedB = subsystemName + "/Motor/Fixed B/OutAmps";

  private static final String keyMotorState_DevId_MiddleA = subsystemName + "/Motor/Middle A/DevId";
  private static final String keyMotorState_Temp_MiddleA = subsystemName + "/Motor/Middle A/Temp";
  private static final String keyMotorState_BusVolt_MiddleA = subsystemName + "/Motor/Middle A/BusVolt";
  private static final String keyMotorState_SetSpeed_MiddleA = subsystemName + "/Motor/Middle A/SetSpeed";
  private static final String keyMotorState_OutAmps_MiddleA = subsystemName + "/Motor/Middle A/OutAmps";

  private static final String keyMotorState_DevId_MiddleB = subsystemName + "/Motor/Middle B/DevId";
  private static final String keyMotorState_Temp_MiddleB = subsystemName + "/Motor/Middle B/Temp";
  private static final String keyMotorState_BusVolt_MiddleB = subsystemName + "/Motor/Middle B/BusVolt";
  private static final String keyMotorState_SetSpeed_MiddleB = subsystemName + "/Motor/Middle B/SetSpeed";
  private static final String keyMotorState_OutAmps_MiddleB = subsystemName + "/Motor/Middle B/OutAmps";

  private static final String keyMotorState_DevId_FloatA = subsystemName + "/Motor/Float A/DevId";
  private static final String keyMotorState_Temp_FloatA = subsystemName + "/Motor/Float A/Temp";
  private static final String keyMotorState_BusVolt_FloatA = subsystemName + "/Motor/Float A/BusVolt";
  private static final String keyMotorState_SetSpeed_FloatA = subsystemName + "/Motor/Float A/SetSpeed";
  private static final String keyMotorState_OutAmps_FloatA = subsystemName + "/Motor/Float A/OutAmps";

  private static final String keyMotorState_DevId_FloatB = subsystemName + "/Motor/Float B/DevId";
  private static final String keyMotorState_Temp_FloatB = subsystemName + "/Motor/Float B/Temp";
  private static final String keyMotorState_BusVolt_FloatB = subsystemName + "/Motor/Float B/BusVolt";
  private static final String keyMotorState_SetSpeed_FloatB = subsystemName + "/Motor/Float B/SetSpeed";
  private static final String keyMotorState_OutAmps_FloatB = subsystemName + "/Motor/Float B/OutAmps";

  //
  // Drive train motors.
  //
  private final CANSparkMax m_motorFixedA;
  private final CANSparkMax m_motorFixedB;
  private final CANSparkMax m_motorMiddleA;
  private final CANSparkMax m_motorMiddleB;
  private final CANSparkMax m_motorFloatA;
  private final CANSparkMax m_motorFloatB;

  //
  // Drive train side A and B speed controller groups.
  //
  private final SpeedControllerGroup m_speedControllerGroupA;
  private final SpeedControllerGroup m_speedControllerGroupB;

  //
  // Drive train differential drive.
  //
  private final DifferentialDrive m_differentialDrive;

  //
  // Logger
  //
  private final Logger m_logger;

  public Subsystem_DriveTrain(CANSparkMax motorFixedA, CANSparkMax motorFixedB, CANSparkMax motorMiddleA,
      CANSparkMax motorMiddleB, CANSparkMax motorFloatA, CANSparkMax motorFloatB) {
    super(subsystemName);

    this.m_logger = new Logger("Drive Train", RobotMap.logLevel);

    this.m_motorFixedA = motorFixedA;
    this.m_motorFixedB = motorFixedB;
    this.m_motorMiddleA = motorMiddleA;
    this.m_motorMiddleB = motorMiddleB;
    this.m_motorFloatA = motorFloatA;
    this.m_motorFloatB = motorFloatB;

    addChild(this.m_motorFixedA);
    addChild(this.m_motorMiddleA);
    addChild(this.m_motorFloatA);
    addChild(this.m_motorFixedB);
    addChild(this.m_motorMiddleB);
    addChild(this.m_motorFloatB);

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
    // Group left and right motors into speed controller groups.
    //
    m_logger.debug("Creating speed control groups for side A and B.");
    m_speedControllerGroupA = new SpeedControllerGroup(this.m_motorFixedA, this.m_motorMiddleA, this.m_motorFloatA);
    m_speedControllerGroupB = new SpeedControllerGroup(this.m_motorFixedB, this.m_motorMiddleB, this.m_motorFloatB);

    //
    // Use speed controller groups to create a differential drive.
    //
    m_logger.debug("Creating Differential Drive.");
    m_differentialDrive = new DifferentialDrive(m_speedControllerGroupA, m_speedControllerGroupB);
  }

  public void drive(double leftSpeed, double rightSpeed) {
    m_differentialDrive.tankDrive(leftSpeed, rightSpeed);
    _logState();
  }

  public void stop() {
    drive(0, 0);
  }

  private void _logState() {
    SmartDashboard.putNumber(keyMotorState_DevId_FixedA, m_motorFixedA.getDeviceId());
    SmartDashboard.putNumber(keyMotorState_Temp_FixedA, m_motorFixedA.getMotorTemperature());
    SmartDashboard.putNumber(keyMotorState_BusVolt_FixedA, m_motorFixedA.getBusVoltage());
    SmartDashboard.putNumber(keyMotorState_OutAmps_FixedA, m_motorFixedA.getOutputCurrent());
    SmartDashboard.putNumber(keyMotorState_SetSpeed_FixedA, m_motorFixedA.get());
    SmartDashboard.putNumber(keyMotorState_DevId_FixedB, m_motorFixedB.getDeviceId());
    SmartDashboard.putNumber(keyMotorState_Temp_FixedB, m_motorFixedB.getMotorTemperature());
    SmartDashboard.putNumber(keyMotorState_BusVolt_FixedB, m_motorFixedB.getBusVoltage());
    SmartDashboard.putNumber(keyMotorState_OutAmps_FixedB, m_motorFixedB.getOutputCurrent());
    SmartDashboard.putNumber(keyMotorState_SetSpeed_FixedB, m_motorFixedB.get());

    SmartDashboard.putNumber(keyMotorState_DevId_MiddleA, m_motorMiddleA.getDeviceId());
    SmartDashboard.putNumber(keyMotorState_Temp_MiddleA, m_motorMiddleA.getMotorTemperature());
    SmartDashboard.putNumber(keyMotorState_BusVolt_MiddleA, m_motorMiddleA.getBusVoltage());
    SmartDashboard.putNumber(keyMotorState_OutAmps_MiddleA, m_motorMiddleA.getOutputCurrent());
    SmartDashboard.putNumber(keyMotorState_SetSpeed_MiddleA, m_motorMiddleA.get());
    SmartDashboard.putNumber(keyMotorState_DevId_MiddleB, m_motorMiddleB.getDeviceId());
    SmartDashboard.putNumber(keyMotorState_Temp_MiddleB, m_motorMiddleB.getMotorTemperature());
    SmartDashboard.putNumber(keyMotorState_BusVolt_MiddleB, m_motorMiddleB.getBusVoltage());
    SmartDashboard.putNumber(keyMotorState_OutAmps_MiddleB, m_motorMiddleB.getOutputCurrent());
    SmartDashboard.putNumber(keyMotorState_SetSpeed_MiddleB, m_motorMiddleB.get());

    SmartDashboard.putNumber(keyMotorState_DevId_FloatA, m_motorFloatA.getDeviceId());
    SmartDashboard.putNumber(keyMotorState_Temp_FloatA, m_motorFloatA.getMotorTemperature());
    SmartDashboard.putNumber(keyMotorState_BusVolt_FloatA, m_motorFloatA.getBusVoltage());
    SmartDashboard.putNumber(keyMotorState_OutAmps_FloatA, m_motorFloatA.getOutputCurrent());
    SmartDashboard.putNumber(keyMotorState_SetSpeed_FloatA, m_motorFloatA.get());
    SmartDashboard.putNumber(keyMotorState_DevId_FloatB, m_motorFloatB.getDeviceId());
    SmartDashboard.putNumber(keyMotorState_Temp_FloatB, m_motorFloatB.getMotorTemperature());
    SmartDashboard.putNumber(keyMotorState_BusVolt_FloatB, m_motorFloatB.getBusVoltage());
    SmartDashboard.putNumber(keyMotorState_OutAmps_FloatB, m_motorFloatB.getOutputCurrent());
    SmartDashboard.putNumber(keyMotorState_SetSpeed_FloatB, m_motorFloatB.get());
  }

  @Override
  public void periodic() {
    _logState();
  }

  @Override
  public void initDefaultCommand() {
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
}
