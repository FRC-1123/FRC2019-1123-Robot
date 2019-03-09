/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Logger;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class Subsystem_Pneumatics extends Subsystem {
  private static final Logger log = new Logger(Subsystem_Pneumatics.class);

  public static class ConstructorArgs {
    public Solenoid m_middleUp;
    public Solenoid m_middleDown;
    public Solenoid m_floatUp;
    public Solenoid m_floatDown;
    public Solenoid m_massForward;
    public Solenoid m_massBack;
    public Solenoid m_footDown;
    public Solenoid m_footUp;
    public Compressor m_compressor;
  }

  public static class ControlBlock {
    double operStartTime;
    double lastPulsedStartTime;
    double pulseDuration;
    Solenoid lastPulsedSolenoid;
    double maxCommandTime;
    double[] axisValueRange;
    double tiltAdjustment;
    boolean isFinished;

    public ControlBlock() {
      this.operStartTime = Timer.getFPGATimestamp();
      this.lastPulsedSolenoid = null;
      this.lastPulsedStartTime = 0.0d;
      this.pulseDuration = 0.1d;
      this.maxCommandTime = 3.0d;
      this.axisValueRange = new double[] { -0.05d, 0.05d };
      this.tiltAdjustment = 1.0d;
      this.isFinished = false;
    }

    public ControlBlock setPulseDuration(double seconds) {
      this.pulseDuration = seconds;
      return this;
    }

    public ControlBlock setMaxCommandTime(double seconds) {
      this.maxCommandTime = seconds;
      return this;
    }

    public ControlBlock setAxisValueTargetRange(double[] range) {
      this.axisValueRange = range;
      return this;
    }

    public ControlBlock setTiltAdjustment(double value) {
      this.tiltAdjustment = value;
      return this;
    }

    public double getPulseDuration() {
      return this.pulseDuration;
    }

    public double getMaxCommandTime() {
      return this.maxCommandTime;
    }

    public double[] getAxisValueTargetRange() {
      return this.axisValueRange;
    }

    public double getTiltAdjustment() {
      return this.tiltAdjustment;
    }

    public boolean isFinished() {
      return this.isFinished;
    }
  }

  public enum AXLE {
    MIDDLE, FLOAT
  }

  private Solenoid m_middleUp;
  private Solenoid m_middleDown;
  private Solenoid m_floatUp;
  private Solenoid m_floatDown;
  private Solenoid m_massForward;
  private Solenoid m_massBack;
  private Solenoid m_footDown;
  private Solenoid m_footUp;
  private Compressor m_compressor;

  public Subsystem_Pneumatics(ConstructorArgs args) {
    this.m_middleUp = args.m_middleUp;
    this.m_middleDown = args.m_middleDown;
    this.m_floatUp = args.m_floatUp;
    this.m_floatDown = args.m_floatDown;
    this.m_massForward = args.m_massForward;
    this.m_massBack = args.m_massBack;
    this.m_footDown = args.m_footDown;
    this.m_footUp = args.m_footUp;
    this.m_compressor = args.m_compressor;

    addChild(this.m_middleUp);
    addChild(this.m_middleDown);
    addChild(this.m_floatUp);
    addChild(this.m_floatDown);
    addChild(this.m_massForward);
    addChild(this.m_massBack);
    addChild(this.m_compressor);
  }

  @Override
  public void initDefaultCommand() {
  }

  public void resetStart() {
    log.debug("***reset");
    this.m_footDown.set(false);
    this.m_middleDown.set(false);
    this.m_floatDown.set(false);

    this.m_massBack.set(false);

    this.m_footUp.set(true);
    this.m_middleUp.set(true);
    this.m_floatUp.set(true);

    this.m_massForward.set(true);
  }

  public void resetStop() {
    this.m_footUp.set(false);
    this.m_middleUp.set(false);
    this.m_floatUp.set(false);

    this.m_massForward.set(false);
  }

  public boolean isExtendMiddleAxleOn() {
    log.debug("***isExtendMiddleAxleOn");
    return this.m_middleDown.get();
  }

  public void extendMiddleAxleStart() {
    log.debug("***extendMiddleAxleStart");
    this.m_middleDown.set(true);
  }

  public void extendMiddleAxleStop() {
    log.debug("***extendMiddleAxleStop");
    this.m_middleDown.set(false);
  }

  public void pulseExtendMiddleAxle(double durationSeconds) {
    log.debug("***pulseExtendMiddleAxle");
    this.m_middleDown.setPulseDuration(durationSeconds);
    this.m_middleDown.startPulse();
  }

  public boolean isExtendFloatAxleOn() {
    log.debug("***isExtendFloatAxleOn");
    return this.m_floatDown.get();
  }

  public void extendFloatAxleStart() {
    log.debug("***extendFloatAxleStart");
    this.m_floatDown.set(true);
  }

  public void extendFloatAxleStop() {
    log.debug("***extendFloatAxleStop");
    this.m_floatDown.set(false);
  }

  public boolean isRetractMiddleAxleOn() {
    log.debug("***isRetractMiddleAxleOn");
    return this.m_middleUp.get();
  }

  public void retractMiddleAxleStart() {
    log.debug("***retractMiddleAxlesStart");
    this.m_middleUp.set(true);
  }

  public void retractMiddleAxleStop() {
    log.debug("***retractMiddleAxlesStop");
    this.m_middleUp.set(false);
  }

  public boolean isRetractFloatAxleOn() {
    log.debug("***isRetractFloatAxleOn");
    return this.m_floatUp.get();
  }

  public void retractFloatAxleStart() {
    log.debug("***retractFloatAxlesStart");
    this.m_floatUp.set(true);
  }

  public void retractFloatAxleStop() {
    log.debug("***retractFloatAxlesStop");
    this.m_floatUp.set(false);
  }

  public void moveMassForwardStart() {
    log.debug("***moveMassForwardStart");
    this.m_massForward.set(true);
  }

  public void moveMassForwardStop() {
    log.debug("***moveMassForwardStop");
    this.m_massForward.set(false);
  }

  public void moveMassBackStart() {
    log.debug("***movMassBackStart");
    this.m_massBack.set(true);
  }

  public void moveMassBackStop() {
    log.debug("***movMassBackStop");
    this.m_massBack.set(false);
  }

  public void pulseMassForward(double durationSeconds) {
    this.m_massForward.setPulseDuration(durationSeconds);
    this.m_massForward.startPulse();
  }

  public void pulseMassBack(double durationSeconds) {
    this.m_massBack.setPulseDuration(durationSeconds);
    this.m_massBack.startPulse();
  }

  public void extendFootStart() {
    log.debug("**extendFootStart");
    this.m_footDown.set(true);
  }

  public void extendFootStop() {
    log.debug("**extendFootStop");
    this.m_footDown.set(false);
  }

  public void retractFootStart() {
    log.debug("**retractFootStart");
    this.m_footUp.set(true);
  }

  public void retractFootStop() {
    log.debug("**retractFootStop");
    this.m_footUp.set(false);
  }

  public void startCompressor() {
    log.debug("***startCompressor");
    this.m_compressor.start();
  }

  public void stopCompressor() {
    log.debug("***stopCompressor");
    this.m_compressor.stop();
  }

  public static Subsystem_Pneumatics create() {
    log.debug("***Begin creating pneumatics subsystem.");
    ConstructorArgs args = new ConstructorArgs();

    args.m_middleUp = new Solenoid(RobotMap.solenoid_Module_Axle_MiddleUp, RobotMap.solenoid_Channel_Axle_MiddleUp);
    args.m_middleDown = new Solenoid(RobotMap.solenoid_Module_Axle_MiddleDown,
        RobotMap.solenoid_Channel_Axle_MiddleDown);

    args.m_floatUp = new Solenoid(RobotMap.solenoid_Module_Axle_FloatUp, RobotMap.solenoid_Channel_Axle_FloatUp);
    args.m_floatDown = new Solenoid(RobotMap.solenoid_Module_Axle_FloatDown, RobotMap.solenoid_Channel_Axle_FloatDown);

    args.m_massForward = new Solenoid(RobotMap.solenoid_Module_MassMover_Forward,
        RobotMap.solenoid_Channel_MassMover_Forward);
    args.m_massBack = new Solenoid(RobotMap.solenoid_Module_MassMover_Back, RobotMap.solenoid_Channel_MassMover_Back);

    args.m_footDown = new Solenoid(RobotMap.solenoid_Module_Foot_Down, RobotMap.solenoid_Channel_Foot_Down);
    args.m_footUp = new Solenoid(RobotMap.solenoid_Module_Foot_Up, RobotMap.solenoid_Channel_Foot_Up);

    args.m_compressor = new Compressor(RobotMap.compressor_Module);

    Subsystem_Pneumatics rtn = new Subsystem_Pneumatics(args);
    log.debug("***End creating pneumatics subsystem.");
    return rtn;
  }
}
