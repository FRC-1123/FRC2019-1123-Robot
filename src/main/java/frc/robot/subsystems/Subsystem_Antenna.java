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
public class Subsystem_Antenna extends Subsystem {
  private static final Logger log = new Logger(Subsystem_Antenna.class);

  private Solenoid m_extend;
  private Solenoid m_retract;
  private boolean isAntennaExtended;

  public Subsystem_Antenna(Solenoid m_extend, Solenoid m_retract) {
    //
    // Init the solenoid members.
    //
    this.m_extend = m_extend;
    this.m_retract = m_retract;

    //
    // Initialize starting position.
    //
    reset();
  }

  @Override
  public void initDefaultCommand() {
  }

  public void extend() {
    log.debug("*** antenna extend");
    pulseExtend(RobotMap.solenoidPulseTime_Antenna_Extend);
  }

  public void pulseExtend(double durationSeconds) {
    log.debug("*** antenna pulseExtend(" + String.valueOf(durationSeconds) + ")");
    this.m_retract.set(false);
    this.m_extend.setPulseDuration(durationSeconds);
    this.m_extend.startPulse();
    this.isAntennaExtended = true;
  }

  public void retract() {
    log.debug("*** antenna retract");
    pulseRetract(RobotMap.solenoidPulseTime_Antenna_Retract);
  }

  public void pulseRetract(double durationSeconds) {
    log.debug("*** antenna pulseRetract(" + String.valueOf(durationSeconds) + ")");
    this.m_extend.set(false);
    this.m_retract.setPulseDuration(durationSeconds);
    this.m_retract.startPulse();
    this.isAntennaExtended = false;
  }

  public boolean isExtended() {
    return this.isAntennaExtended;
  }

  public void reset() {
    log.debug("*** antenna reset postion");
    retract();;
  }

  public static Subsystem_Antenna create() {
    Solenoid m_extend = new Solenoid(RobotMap.solenoid_Module_Antenna_Extend, RobotMap.solenoid_Channel_Antenna_Extend);
    Solenoid m_retract = new Solenoid(RobotMap.solenoid_Module_Antenna_Retract,
        RobotMap.solenoid_Channel_Antenna_Retract);
    return new Subsystem_Antenna(m_extend, m_retract);
  }
}
