/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class Command_ResetRobot extends Command {
  public Command_ResetRobot() {
    requires(Robot.m_subsystemFoot);
    requires(Robot.m_subsystemMiddleAxle);
    requires(Robot.m_subsystemFloatAxle);
    requires(Robot.m_subsystemDriveTrain);
    requires(Robot.m_subsystemHatch);
    requires(Robot.m_subsystemAntenna);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.m_subsystemDriveTrain.stop();
    Robot.m_subsystemFoot.reset();
    Robot.m_subsystemMiddleAxle.reset();
    Robot.m_subsystemFloatAxle.reset();
    Robot.m_subsystemHatch.reset();
    Robot.m_subsystemAntenna.reset();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
