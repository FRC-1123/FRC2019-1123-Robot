/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Logger;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class Subsystem_FloatAxle extends Subsystem {
  public static final Logger log = new Logger(Subsystem_FloatAxle.class);

  private Solenoid m_floatUp;
  private Solenoid m_floatDown;

  public Subsystem_FloatAxle(Solenoid m_floatUp, Solenoid m_floatDown) {
    this.m_floatUp = m_floatUp;
    this.m_floatDown = m_floatDown;
    reset();
  }

  public void extend() {
    log.debug("*** floatAxle extend");
    pulseExtend(RobotMap.solenoidPulseTime_FloatAxle_Down);
  }

  public void pulseExtend(double durationSeconds) {
    log.debug("*** floatAxle pulseExtend(" + String.valueOf(durationSeconds));
    m_floatUp.set(false);
    m_floatDown.setPulseDuration(durationSeconds);
    m_floatDown.startPulse();
  }

  public void retract() {
    log.debug("*** floatAxle retract");
    pulseRetract(RobotMap.solenoidPulseTime_FloatAxle_Up);
  }

  public void pulseRetract(double durationSeconds) {
    log.debug("*** floatAxle pulseRetract(" + String.valueOf(durationSeconds));
    m_floatDown.set(false);
    m_floatUp.setPulseDuration(durationSeconds);
    m_floatUp.startPulse();
  }

  public void extendStart() {
    log.debug("*** floatAxle extendStart");
    m_floatUp.set(false);
    m_floatDown.set(true);
  }

  public void extendStop() {
    log.debug("*** floatAxle extendStop");
    m_floatDown.set(false);
  }

  public void retractStart() {
    log.debug("*** floatAxle retractStart");
    m_floatDown.set(false);
    m_floatUp.set(true);
  }

  public void retractStop() {
    log.debug("*** floatAxle retractStop");
    m_floatUp.set(false);
  }

  @Override
  public void initDefaultCommand() {
  }

  public void reset() {
    log.debug("*** floatAxle reset");
    retract();
  }

  public static Subsystem_FloatAxle create() {
    Solenoid m_floatUp = new Solenoid(RobotMap.solenoid_Module_Axle_FloatUp, RobotMap.solenoid_Channel_Axle_FloatUp);
    Solenoid m_floatDown = new Solenoid(RobotMap.solenoid_Module_Axle_FloatDown,
        RobotMap.solenoid_Channel_Axle_FloatDown);
    return new Subsystem_FloatAxle(m_floatUp, m_floatDown);
  }
}
