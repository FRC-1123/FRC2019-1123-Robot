/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.WaitCommand;

public class Command_ClimbGoldStep1_Prepare extends AbstractCommand_ClimbBox {
  /**
   * Prepare robot to climb the gold box. 
   * <p>
   * State of Robot pre-command:
   * <ol>
   * <li>Robot font bumper is against the gold box</li>
   * <li>Robot is still</li>
   * </ol>
   */
  public Command_ClimbGoldStep1_Prepare() {
    addSequential(new Command_StartCompressor());

    //
    // Make sure we are in "creep mode."
    addSequential(new Command_EnableCreepMode());
    
    //
    // Move the mass to the back of the robot.
    //
    addSequential(new Command_MoveMassBackStart());
    addSequential(new WaitCommand(massMoverWait));
    
    //
    // Backup a little
    //
    addSequential(new Command_DriveDistanceStraight(backupDistance , backupSpeed));
        
  }
}
