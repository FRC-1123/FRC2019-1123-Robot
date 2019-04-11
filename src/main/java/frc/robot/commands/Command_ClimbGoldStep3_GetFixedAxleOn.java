/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Command_ClimbGoldStep3_GetFixedAxleOn extends CommandGroup {
  /**
   * Get the fixed axle over the box.
   */
  public Command_ClimbGoldStep3_GetFixedAxleOn() {
    //
    // Make sure the compressor is running to help keep up pressure on pneumatics.
    //
    addSequential(new Command_StartCompressor());

    //
    // Retract the foot.
    //
    addSequential(new Command_RetractFoot());

  }
}
