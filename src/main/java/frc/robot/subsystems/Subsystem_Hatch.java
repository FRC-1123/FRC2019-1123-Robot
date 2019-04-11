/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class Subsystem_Hatch extends Subsystem {

  private Solenoid m_extend;
  private Solenoid m_retract;
  private boolean isExtended;

  public Subsystem_Hatch(Solenoid m_extend, Solenoid m_retract) {
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
    pulseExtend(RobotMap.solenoidPulseTime_Hatch_Extend);
  }

  public void pulseExtend(double durationSeconds) {
    this.m_retract.set(false);
    this.m_extend.setPulseDuration(durationSeconds);
    this.m_extend.startPulse();
    this.isExtended = true;
  }

  public void retract() {
    pulseRetract(RobotMap.solenoidPulseTime_Hatch_Retract);
  }

  public void pulseRetract(double durationSeconds) {
    this.m_extend.set(false);
    this.m_retract.setPulseDuration(durationSeconds);
    this.m_retract.startPulse();
    this.isExtended = false;
  }

  public boolean isExtended() {
    return this.isExtended;
  }

  public void reset() {
    retract();;
  }

  public static Subsystem_Hatch create() {
    Solenoid m_extend = new Solenoid(RobotMap.solenoid_Module_Hatch_Extend, RobotMap.solenoid_Channel_Hatch_Extend);
    Solenoid m_retract = new Solenoid(RobotMap.solenoid_Module_Hatch_Retract,
        RobotMap.solenoid_Channel_Hatch_Retract);
    return new Subsystem_Hatch(m_extend, m_retract);
  }
}
