/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

public class Command_ClimbGoldStep3_StabilizeRobot extends AbstractCommand_ClimbBox {
  /**
   * Stabilize the robot with middle axle.
   * <p>
   * State of Robot pre-command:
   * <ol>
   * <li>Creep mode has been enabled</li>
   * <li>Mass mover back stop command has been issued and mass is in the back of the robot</li>
   * <li>Robot is a set distance from the box</li>
   * <li>Both the float axle and foot extend start has been activated.</li>
   * </ol>
   */
  public Command_ClimbGoldStep3_StabilizeRobot() {
    //
    // Make sure the compressor is running to help keep up pressure on pneumatics.
    //
    addSequential(new Command_StartCompressor());

    //
    // Extend the middle axle to help stabilize the robot.
    //
    addSequential(new Command_ExtendMiddleAxleStart());

  }
}
