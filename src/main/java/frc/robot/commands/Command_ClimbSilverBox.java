/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

public class Command_ClimbSilverBox extends AbstractCommand_ClimbBox {
  /**
   * Add your docs here.
   */
  public Command_ClimbSilverBox() {
    super();
    climbTheBox(0.75, 1.0, 1.0);
  }
}
