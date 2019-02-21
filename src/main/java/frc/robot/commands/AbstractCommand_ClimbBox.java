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
  protected static final double bumperPad = 5.0d;
  protected static final double frameToFixed = 5.0d;
  protected static final double fixedAxleToMiddleAxle = 8.0d;
  protected static final double middleAxleToFloatAxle = 8.0d;
  protected static final double pad = 1.5d;
  protected static final double speed = 0.10;

  /**
   * Add your docs here.
   */
  public AbstractCommand_ClimbBox() {
  }

  protected void climbTheBox(double waitTimeForBothAxleExtends, double waitTimeRetractMiddleAxle, double waitTimeRetractFloatAxle) {
    addSequential(new Command_StartCompressor());
    addSequential(new Command_ExtendBothAxlesStart());
    addSequential(new WaitCommand(waitTimeForBothAxleExtends));
    addSequential(new Command_StartCompressor());
    addSequential(new Command_DriveDistanceStraight(bumperPad+frameToFixed+pad, speed));
    addSequential(new Command_ExtendMiddleAxleStop());
    addSequential(new Command_RetractMiddleAxleStart());
    addSequential(new WaitCommand(waitTimeRetractMiddleAxle));
    addSequential(new Command_StartCompressor());
    addSequential(new Command_RetractMiddleAxleStop());
    addSequential(new Command_DriveDistanceStraight(fixedAxleToMiddleAxle+pad, speed));
    addSequential(new Command_RetractFloatAxleStart());
    addSequential(new WaitCommand(waitTimeRetractFloatAxle));
    addSequential(new Command_RetractFloatAxleStop());
    addSequential(new Command_StopCompressor());
    addSequential(new Command_DriveDistanceStraight(middleAxleToFloatAxle+(pad*2),speed));
  }
}
