/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class Subsystem_Compressor extends Subsystem {
  private static final Logger log = LoggerFactory.getLogger(Subsystem_Compressor.class);
  private Compressor m_compressor;

  public Subsystem_Compressor(Compressor m_compressor) {
    this.m_compressor = m_compressor;
  }

  public void start() {
    log.debug("*** compressor start");
    m_compressor.start();
  }

  public void stop() {
    log.debug("*** compressor stop");
    m_compressor.stop();
  }

  @Override
  public void initDefaultCommand() {
  }

  public static Subsystem_Compressor create() {
    Compressor m_compressor = new Compressor(RobotMap.compressor_Module);
    return new Subsystem_Compressor(m_compressor);
  }
}
