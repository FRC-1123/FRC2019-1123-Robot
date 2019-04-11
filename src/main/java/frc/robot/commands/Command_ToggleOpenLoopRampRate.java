/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class Command_ToggleOpenLoopRampRate extends Command {

  public Command_ToggleOpenLoopRampRate() {
    requires(Robot.m_subsystemDriveTrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.m_subsystemDriveTrain.stop();
    double rate = Robot.m_subsystemDriveTrain.getOpenLoopRampRate();
    if (rate==RobotMap.driveRampRateNormal) {
      Robot.m_subsystemDriveTrain.setOpenLoopRampRate(RobotMap.driveRampRateCreep);
    } else {
      Robot.m_subsystemDriveTrain.setOpenLoopRampRate(RobotMap.driveRampRateNormal);
    }
    SmartDashboard.putBoolean("Creep Mode", Robot.m_subsystemDriveTrain.getOpenLoopRampRate() == RobotMap.driveRampRateCreep);    
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
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
