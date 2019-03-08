/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

public class Command_ClimbGoldStep5_GetFloatAxleOn extends AbstractCommand_ClimbBox {
  /**
   * Get the middle axle over the box.
   */
  public Command_ClimbGoldStep5_GetFloatAxleOn() {
    //
    // Make sure the compressor is running to help keep up pressure on pneumatics.
    //
    addSequential(new Command_StartCompressor());

    //
    // Raise the rear axle
    //
    addSequential(new Command_ExtendFloatAxleStop());
    addSequential(new Command_RetractFloatAxleStart());

    //
    // Clean up
    //
    addSequential(new Command_RetractFootStop());
    addSequential(new Command_RetractMiddleAxleStop());
    addSequential(new Command_RetractFloatAxleStop());
    addSequential(new Command_StopCompressor());
 }
}
