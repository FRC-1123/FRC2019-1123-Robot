/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.WaitCommand;

public class Command_ClimbGoldStep4_GetMiddleAxleOn extends AbstractCommand_ClimbBox {
  /**
   * Get the middle axle over the box.
   */
  public Command_ClimbGoldStep4_GetMiddleAxleOn() {
    //
    // Make sure the compressor is running to help keep up pressure on pneumatics.
    //
    addSequential(new Command_StartCompressor());

    //
    // Move the mass to the front of the robot.
    //
    addSequential(new Command_MoveMassForwardStart());
    addSequential(new WaitCommand(massMoverWait));
    addSequential(new Command_MoveMassForwardStop());

    //
    // Raise the middle axle
    //
    addSequential(new Command_ExtendMiddleAxleStop());
    addSequential(new Command_RetractMiddleAxleStart());

  }
}
