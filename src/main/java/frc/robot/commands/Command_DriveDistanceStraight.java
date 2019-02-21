/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Logger;
import frc.robot.Robot;

/**
 * Command to drive the robot straight for a given distance in inches.
 */
public class Command_DriveDistanceStraight extends Command {
  private static final Logger log = new Logger(Command_DriveDistanceStraight.class);
  private double m_distance;
  private double m_speed;

  /**
   * Create a command to drive the robot straight for a given distance and speed.
   * @param distance
   */
  public Command_DriveDistanceStraight(double distance, double speed) {
    requires(Robot.m_subsystemDriveTrain);
    this.m_distance = distance;
    this.m_speed = speed;
    log.debug("***constructor");
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    log.debug("***initialize");
    Robot.m_subsystemDriveTrain.resetMotorEncoders();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    log.debug("***execute");
    Robot.m_subsystemDriveTrain.driveStraightDistance(m_distance, m_speed);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return Robot.m_subsystemDriveTrain.distanceTraveled() >= m_distance;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    log.debug("***end");
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    log.debug("***interrupted");
  }
}
