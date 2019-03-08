/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

public class Command_ClimbGoldStep2_RaiseRobot extends AbstractCommand_ClimbBox {
  /**
   * Raise up the robot body to box height.
   * <p>
   * State of Robot pre-command:
   * <ol>
   * <li>Creep mode has been enabled</li>
   * <li>Mass mover back start command is active</li>
   * <li>Robot has backed away from the box a little</li>
   * </ol>
   */
  public Command_ClimbGoldStep2_RaiseRobot() {
    //
    // Make sure the compressor is running to help keep up pressure on pneumatics.
    //
    addSequential(new Command_StartCompressor());

    //
    // Stop the mass mover back to pair mass mover back start in step 1.
    //
    // addSequential(new Command_MoveMassBackStop());

    //
    // Extend Float Axle and Foot
    //
    addSequential(new Command_ExtendFloatAxleAndFootStart());
    addSequential(new Command_ExtendMiddleAxleStart());
  }
}
