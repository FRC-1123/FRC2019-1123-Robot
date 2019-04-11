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
public class Subsystem_Foot extends Subsystem {
  public static final Logger log = new Logger(Subsystem_Foot.class);

  private Solenoid m_footUp;
  private Solenoid m_footDown;

  public Subsystem_Foot(Solenoid m_footUp, Solenoid m_footDown) {
    this.m_footUp = m_footUp;
    this.m_footDown = m_footDown;
    reset();
  }

  public void extend() {
    log.debug("*** foot extend");
    pulseExtend(RobotMap.solenoidPulseTime_Foot_Down);
  }

  public void pulseExtend(double durationSeconds) {
    log.debug("*** foot pulseExtend(" + String.valueOf(durationSeconds));
    m_footUp.set(false);
    m_footDown.setPulseDuration(durationSeconds);
    m_footDown.startPulse();
  }

  public void retract() {
    log.debug("*** foot retract");
    pulseRetract(RobotMap.solenoidPulseTime_Foot_Up);
  }

  public void pulseRetract(double durationSeconds) {
    log.debug("*** foot pulseRetract(" + String.valueOf(durationSeconds));
    m_footDown.set(false);
    m_footUp.setPulseDuration(durationSeconds);
    m_footUp.startPulse();
  }

  public void extendStart() {
    log.debug("*** foot extendStart");
    m_footUp.set(false);
    m_footDown.set(true);
  }

  public void extendStop() {
    log.debug("*** foot extendStop");
    m_footDown.set(false);
  }

  public void retractStart() {
    log.debug("*** foot retractStart");
    m_footDown.set(false);
    m_footUp.set(true);
  }

  public void retractStop() {
    log.debug("*** foot retractStop");
    m_footUp.set(false);
  }

  @Override
  public void initDefaultCommand() {
  }

  public void reset() {
    log.debug("*** foot reset");
    retract();
  }

  public static Subsystem_Foot create() {
    Solenoid m_footUp = new Solenoid(RobotMap.solenoid_Module_Foot_Up, RobotMap.solenoid_Channel_Foot_Up);
    Solenoid m_footDown = new Solenoid(RobotMap.solenoid_Module_Foot_Down,
        RobotMap.solenoid_Channel_Foot_Down);
    return new Subsystem_Foot(m_footUp, m_footDown);
  }
}
