/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.WaitCommand;

public class Command_ClimbGoldStep5_GetFloatAxleOn extends AbstractCommand_ClimbBox {
  /**
   * Get the middle axle over the box.
   * <p>
   * State of Robot pre-command:
   * <ol>
   * <li>Creep mode has been enabled</li>
   * <li>Mass mover forward start command is active</li>
   * <li>Robot middle axle is over the box</li>
   * <li>The float axle extend start active.</li>
   * <li>The foot retract start is active.</li>
   * <li>The middle retract start is active.</li>
   * </ol>
   */
  public Command_ClimbGoldStep5_GetFloatAxleOn() {
    //
    // Make sure the compressor is running to help keep up pressure on pneumatics.
    //
    addSequential(new Command_StartCompressor());

    //
    // Turn off mass mover forward to save some pneumatic pressure.
    //
    addSequential(new Command_MoveMassForwardStop());

    //
    // Raise the rear axle
    //
    addSequential(new Command_ExtendFloatAxleStop());
    addSequential(new Command_RetractFloatAxleStart());
    addSequential(new WaitCommand(timeToRetractFloatAxle));

    //
    // Try to get the rest of the robot onto the box.
    //
    // addSequential(new Command_DriveDistanceStraight(middleAxleToFloatAxle+(pad*2),speed));

    //
    // Clean up
    //
    addSequential(new Command_RetractFootStop());
    addSequential(new Command_RetractMiddleAxleStop());
    addSequential(new Command_RetractFloatAxleStop());
    addSequential(new Command_StopCompressor());
 }
}
