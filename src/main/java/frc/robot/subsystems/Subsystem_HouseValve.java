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
public class Subsystem_HouseValve extends Subsystem {

  private Solenoid m_valve;

  public Subsystem_HouseValve(Solenoid m_valve) {
    this.m_valve = m_valve;
    close();
  }

  public void open() {
    this.m_valve.set(true);
  }

  public void close() {
    this.m_valve.set(false);
  }

  @Override
  public void initDefaultCommand() {
  }

  public static Subsystem_HouseValve create() {
    Solenoid m_valve = new Solenoid(RobotMap.solenoid_Module_House_Valve, RobotMap.solenoid_Channel_House_Valve);
    return new Subsystem_HouseValve(m_valve);
  }
}
