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
   * <p>
   * State of Robot pre-command:
   * <ol>
   * <li>Creep mode has been enabled</li>
   * <li>Mass mover back stop command has been issued and mass is in the back of the robot</li>
   * <li>Robot fixed axle is over the box</li>
   * <li>The float axle and middle axle extend start are active.</li>
   * <li>The foot retract start is active.</li>
   * </ol>
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

    //
    // Raise the middle axle
    //
    addSequential(new Command_ExtendMiddleAxleStop());
    addSequential(new Command_RetractMiddleAxleStart());
    addSequential(new WaitCommand(timeToRetractMiddleAxle));
    
    //
    // Try to get the middle axle over the box.
    //
    // addSequential(new Command_DriveDistanceStraight(fixedAxleToMiddleAxle+pad, speed));

  }
}
