/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

public class Command_ClimbGoldStep4_GetFixedAxleOn extends AbstractCommand_ClimbBox {
  /**
   * Get the fixed axle over the box.
   * <p>
   * State of Robot pre-command:
   * <ol>
   * <li>Creep mode has been enabled</li>
   * <li>Mass mover back stop command has been issued and mass is in the back of the robot</li>
   * <li>Robot is a set distance from the box</li>
   * <li>The float axle, middle axle, and foot extend start are active.</li>
   * </ol>
   */
  public Command_ClimbGoldStep4_GetFixedAxleOn() {
    //
    // Make sure the compressor is running to help keep up pressure on pneumatics.
    //
    addSequential(new Command_StartCompressor());

    //
    // Retract the foot.
    //
    addSequential(new Command_ExtendFootStop());
    addSequential(new Command_RetractFootStart());

    //
    // Drive forward enough to get the front axle over the box.
    //
    addSequential(new Command_DriveDistanceStraight(backupDistance+bumperPad+frameToFixed+pad, speed));

  }
}
