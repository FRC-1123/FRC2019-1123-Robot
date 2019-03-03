/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Command_ExtendBothAxlesWithRackStart extends CommandGroup {
  /**
   * Add your docs here.
   */
  public Command_ExtendBothAxlesWithRackStart() {
    addSequential(new Command_RackAxlesStart());
    // addSequential(new Command_ExtendBothAxlesStart());
    addSequential(new WaitCommand(0.25d));
    addSequential(new Command_RackAxlesStop());
    addSequential(new Command_RackAxlesStart());
    addSequential(new WaitCommand(0.25d));
    addSequential(new Command_RackAxlesStop());
    addSequential(new Command_RackAxlesStart());
    addSequential(new WaitCommand(0.25d));
    addSequential(new Command_RackAxlesStop());
  }
}
