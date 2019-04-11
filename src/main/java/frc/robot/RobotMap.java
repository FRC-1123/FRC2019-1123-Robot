/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {  
  //
  // Robot axles have side A and B. The robot has 3 axles: fixed, middle and float.
  //
  // Motors on side A of the robot have the following addresses
  //   fixed = 11 
  //   middle = 13
  //   float = 15
  //
  // Motors on side B of the robot have the following addresses
  //    fixed = 12
  //    middle = 14
  //    float = 16
  //
  // Fixed and Float motors are mounted with the drive spindle pointing towards the outside of the robot.
  // Middle motors are mounted with the drive spendle pointing towards the inside of the robot.
  //
  public static final int driveMotor_Fixed_A = 11;
  public static final int driveMotor_Middle_A = 15;
  public static final int driveMotor_Float_A = 13;
  public static final int driveMotor_Fixed_B = 12;
  public static final int driveMotor_Middle_B = 14;
  public static final int driveMotor_Float_B = 16;

  public static final boolean invertedFlag_Side_A = true;
  public static final boolean invertedFlag_Side_B = false;

  public static final double driveRampRateNormal = 0.5d;
  public static final double driveRampRateCreep = 1.0d;

  public static final double axleDelay = 0.05d;

  //
  // Wheel radius for drive train.
  //
  public static final double driveTrain_Wheel_Radius = 3d;

  //
  // Solenoid ports for floating axles and mass mover, etc.
  //
  public static final int compressor_Module = 21;
  
  public static final int solenoid_Module_Axle_MiddleDown = 21;
  public static final int solenoid_Channel_Axle_MiddleDown = 0;
  public static final int solenoid_Module_Axle_MiddleUp = 21;
  public static final int solenoid_Channel_Axle_MiddleUp = 1;

  public static final int solenoid_Module_Axle_FloatDown = 21;
  public static final int solenoid_Channel_Axle_FloatDown = 2;
  public static final int solenoid_Module_Axle_FloatUp = 21;
  public static final int solenoid_Channel_Axle_FloatUp = 3;

  public static final int solenoid_Module_MassMover_Forward = 21;
  public static final int solenoid_Channel_MassMover_Forward = 4;
  public static final int solenoid_Module_MassMover_Back = 21;
  public static final int solenoid_Channel_MassMover_Back = 5;

  public static final int solenoid_Module_Foot_Down = 21;
  public static final int solenoid_Channel_Foot_Down = 6;
  public static final int solenoid_Module_Foot_Up = 21;
  public static final int solenoid_Channel_Foot_Up  = 7;

  public static final int solenoid_Module_Hatch_Extend = 22;
  public static final int solenoid_Channel_Hatch_Extend = 0;
  public static final int solenoid_Module_Hatch_Retract = 22;
  public static final int solenoid_Channel_Hatch_Retract = 1;

  public static final int solenoid_Module_Antenna_Extend = 22;
  public static final int solenoid_Channel_Antenna_Extend = 2;
  public static final int solenoid_Module_Antenna_Retract = 22;
  public static final int solenoid_Channel_Antenna_Retract = 3;

  public static final int solenoid_Module_House_Valve = 22;
  public static final int solenoid_Channel_House_Valve = 7;

  public static final double solenoid_AxleFoot_Reset_Wait = 3.0d;
  public static final double solenoid_MassMover_Reset_Wait = 4.0d;

  //
  // Solenoid pulse times in seconds.
  //
  public static final double solenoidPulseTime_MiddleAxle_Down = 2.55d;
  public static final double solenoidPulseTime_MiddleAxle_Up = 2.55d;

  public static final double solenoidPulseTime_FloatAxle_Down = 2.55d;
  public static final double solenoidPulseTime_FloatAxle_Up = 2.55d;

  public static final double solenoidPulseTime_Foot_Down = 2.55d;
  public static final double solenoidPulseTime_Foot_Up = 1.5d;

  public static final double solenoidPulseTime_MassMover_Forward = 2.55d;
  public static final double solenoidPulseTime_MassMover_Back = 2.55d;

  public static final double solenoidPulseTime_Hatch_Extend = 1.5d;
  public static final double solenoidPulseTime_Hatch_Retract = 1.5d;

  public static final double solenoidPulseTime_Antenna_Extend = 1.5d;
  public static final double solenoidPulseTime_Antenna_Retract = 1.5d;

  //
  // Power distribution panel port.
  //
  public static final int powerDistributionPanel = 1;

  //
  // Camera count.
  //
  public static final int cameraCount = 4;
}
