/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

public class Command_MoveForwardOneInch extends Command_DriveDistanceStraight {
  public Command_MoveForwardOneInch() {
    super(1.0d, 0.05d);
  }
}
