/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public abstract class AbstractCommand_ClimbBox extends CommandGroup {
  protected static final double bumperPad = 3.0d + (3.0d/8.0d);
  protected static final double frameToFixed = 3.5d;
  protected static final double frameToMiddle = 10.0d;
  protected static final double frameToFloat = 16.0d;
  protected static final double fixedAxleToMiddleAxle = frameToMiddle - frameToFixed;
  protected static final double middleAxleToFloatAxle = frameToFloat - frameToMiddle;
  protected static final double pad = 1.5d;
  protected static final double speed = 0.10d;
  protected static final double backupDistance = 2.0d;
  protected static final double backupSpeed = speed * -1.0d;
  protected static final double massMoverWait = 5.0d;
  protected static final double timeToRetractMiddleAxle = 5.0d;
  protected static final double timeToRetractFloatAxle = 5.0d;

  /**
   * Add your docs here.
   */
  public AbstractCommand_ClimbBox() {
  }

  protected void climbTheBox(double waitTimeForBothAxleExtends, double waitTimeRetractMiddleAxle, double waitTimeRetractFloatAxle) {
    addSequential(new Command_StartCompressor());

    //
    // Make sure we are in "creep mode."
    addSequential(new Command_EnableCreepMode());
    
    //
    // Move the mass to the back of the robot.
    //
    addSequential(new Command_MoveMassBackStart());
    addSequential(new WaitCommand(massMoverWait));
    addSequential(new Command_MoveMassBackStop());
    
    //
    // Backup a little
    //
    addSequential(new Command_DriveDistanceStraight(backupDistance , backupSpeed));
        
    addSequential(new Command_ExtendFloatAxleAndFootStart());
    addSequential(new WaitCommand(waitTimeForBothAxleExtends));
    
    addSequential(new Command_StartCompressor());
    
    //
    // Try to get the front axle on the box.
    //
    addSequential(new Command_DriveDistanceStraight(backupDistance+bumperPad+frameToFixed+pad, speed));
    
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
    addSequential(new WaitCommand(waitTimeRetractMiddleAxle));
    addSequential(new Command_RetractMiddleAxleStop());
    
    addSequential(new Command_StartCompressor());

    //
    // Try to get the middle axle over the box.
    //
    addSequential(new Command_DriveDistanceStraight(fixedAxleToMiddleAxle+pad, speed));

    //
    // Raise the rear axle
    //
    addSequential(new Command_RetractFloatAxleStart());
    addSequential(new WaitCommand(waitTimeRetractFloatAxle));
    addSequential(new Command_RetractFloatAxleStop());

    //
    // Try to get the rest of the robot onto the box.
    //
    addSequential(new Command_DriveDistanceStraight(middleAxleToFloatAxle+(pad*2),speed));
  }
}
