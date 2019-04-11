/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Command_ClimbGoldStep2_RaiseRobot extends CommandGroup {
  /**
   * Raise up the robot body to box height.
   */
  public Command_ClimbGoldStep2_RaiseRobot() {
    //
    // Make sure the compressor is running to help keep up pressure on pneumatics.
    //
    addSequential(new Command_StartCompressor());

    //
    // Adjust the mass mover
    //
    addSequential(new Command_PulseMassMoveBack(0.5d));

    //
    // Extend Float Axle and Foot
    //
    addParallel(new Command_ExtendFoot());
    addSequential(new Command_ExtendFloatAxleStop());
    addSequential(new Command_ExtendMiddleAxle());

    //
    // Move the mass to the back of the robot.
    //
    addSequential(new Command_MoveMassBack());
  }
}
