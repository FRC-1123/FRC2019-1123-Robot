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
 * Mass mover subsystem.
 */
public class Subsystem_MassMover extends Subsystem {
  private static final Logger log = new Logger(Subsystem_MassMover.class);

  private Solenoid m_massForward;
  private Solenoid m_massBack;

  public Subsystem_MassMover(Solenoid m_massForward, Solenoid m_massBack) {
    this.m_massForward = m_massForward;
    this.m_massBack = m_massBack;
    reset();
  }

  public void moveMassForwardStart() {
    log.debug("***massMover moveMassForwardStart");

    if (this.m_massBack.get())
      this.m_massBack.set(false);

    this.m_massForward.set(true);
  }

  public void moveMassForwardStop() {
    log.debug("***massMover moveMassForwardStop");
    this.m_massForward.set(false);
  }

  public void moveMassBackStart() {
    log.debug("***massMover movMassBackStart");

    if (this.m_massForward.get())
      this.m_massForward.set(false);

    this.m_massBack.set(true);
  }

  public void moveMassBackStop() {
    log.debug("***massMover movMassBackStop");
    this.m_massBack.set(false);
  }

  public void pulseMassForward(double durationSeconds) {
    log.debug("***massMover pulseMassForward("+String.valueOf(durationSeconds)+")");
      this.m_massBack.set(false);
    this.m_massForward.setPulseDuration(durationSeconds);
    this.m_massForward.startPulse();
  }

  public void pulseMassBack(double durationSeconds) {
    log.debug("***massMover pulseMassBack("+String.valueOf(durationSeconds)+")");
      this.m_massForward.set(false);
    this.m_massBack.setPulseDuration(durationSeconds);
    this.m_massBack.startPulse();
  }

  public void moveForward() {
    log.debug("*** massMover moveForward");
    pulseMassForward(RobotMap.solenoidPulseTime_MassMover_Forward);
  }

  public void moveBack() {
    log.debug("*** massMover moveBack");
    pulseMassBack(RobotMap.solenoidPulseTime_MassMover_Back);
  }

  @Override
  public void initDefaultCommand() {
  }

  public void reset() {
    log.debug("*** massMover reset");
    moveForward();
  }

  public static Subsystem_MassMover create() {

    Solenoid m_massForward = new Solenoid(RobotMap.solenoid_Module_MassMover_Forward,
        RobotMap.solenoid_Channel_MassMover_Forward);

    Solenoid m_massBack = new Solenoid(RobotMap.solenoid_Module_MassMover_Back,
        RobotMap.solenoid_Channel_MassMover_Back);

    return new Subsystem_MassMover(m_massForward, m_massBack);
  }
}
