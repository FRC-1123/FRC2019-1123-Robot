/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

public class Command_ClimbGoldBox extends AbstractCommand_ClimbBox {
  /**
   * Add your docs here.
   */
  public Command_ClimbGoldBox() {
    climbTheBox(3.0, 3.0, 3.0);
  }
}
