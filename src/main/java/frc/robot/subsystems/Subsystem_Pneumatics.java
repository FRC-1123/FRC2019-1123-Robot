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
public class Subsystem_Pneumatics extends Subsystem {
  private static final Logger log = new Logger(Subsystem_Pneumatics.class);

  public enum AXLE {MIDDLE,FLOAT}

  private Solenoid m_middleUp;
  private Solenoid m_middleDown;
  private Solenoid m_floatUp;
  private Solenoid m_floatDown;
  private Solenoid m_massForward;
  private Solenoid m_massBack;

  public Subsystem_Pneumatics(Solenoid middleUp, Solenoid middleDown, Solenoid floatUp, Solenoid floatDown, Solenoid massForward, Solenoid massBack) {
    this.m_middleUp = middleUp;
    this.m_middleDown = middleDown;
    this.m_floatUp = floatUp;
    this.m_floatDown = floatDown;
    this.m_massForward = massForward;
    this.m_massBack = massBack;
    
    addChild(this.m_middleUp);
    addChild(this.m_middleDown);
    addChild(this.m_floatUp);
    addChild(this.m_floatDown);
    addChild(this.m_massForward);
    addChild(this.m_massBack);
  }

  @Override
  public void initDefaultCommand() {
  }

  public void extendBothAxles() {
    log.debug("***extendBothAxles");
    extendAxle(AXLE.FLOAT);
    extendAxle(AXLE.MIDDLE);
  }

  public void retractBothAxles() {
    log.debug("***retractBothAxles");
    retractAxle(AXLE.MIDDLE);
    retractAxle(AXLE.FLOAT);
  }

  public void extendAxle(AXLE axle) {
    log.debug("***extendAxle("+axle.name()+")");
    if (axle==AXLE.FLOAT) {
      this.m_floatUp.set(false);
      this.m_floatDown.set(true);   
    } else {
      this.m_middleUp.set(false);
      this.m_middleDown.set(true);
    }
  }

  public void retractAxle(AXLE axle) {
    log.debug("***retractAxle("+axle.name()+")");
    if (axle==AXLE.FLOAT) {
      this.m_floatDown.set(false);
      this.m_floatUp.set(true);
    } else {
      this.m_middleDown.set(false);
      this.m_middleUp.set(true);
    }
  }

  public void moveMassForward() {
    log.debug("***moveMassForward");
  }

  public void moveMassBack() {
    log.debug("***movMassBack");
  }

  public static Subsystem_Pneumatics create() {
    Solenoid middleUp = new Solenoid(RobotMap.solenoid_Module_Axle_MiddleUp, RobotMap.solenoid_Channel_Axle_MiddleUp);
    Solenoid middleDown = new Solenoid(RobotMap.solenoid_Module_Axle_MiddleDown, RobotMap.solenoid_Channel_Axle_MiddleDown);
    Solenoid floatUp = new Solenoid(RobotMap.solenoid_Module_Axle_FloatUp, RobotMap.solenoid_Channel_Axle_FloatUp);
    Solenoid floatDown = new Solenoid(RobotMap.solenoid_Module_Axle_FloatDown, RobotMap.solenoid_Channel_Axle_FloatDown);
    Solenoid massForward = new Solenoid(RobotMap.solenoid_Module_MassMover_Forward, RobotMap.solenoid_Channel_MassMover_Forward);
    Solenoid massBack = new Solenoid(RobotMap.solenoid_Module_MassMover_Back, RobotMap.solenoid_Channel_MassMover_Back);

    return new Subsystem_Pneumatics(middleUp, middleDown, floatUp, floatDown, massForward, massBack);
  }
}
