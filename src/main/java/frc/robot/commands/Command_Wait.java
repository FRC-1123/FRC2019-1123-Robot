/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.command.Command;

/**
 * The {@link Command_Wait} will wait for a number of milliseconds before
 * finishing. This is useful if you want a
 * {@link edu.wpi.first.wpilibj.command.CommandGroup CommandGroup} to pause for
 * a moment. Note that the wait is not exact in that it is within the period
 * cycle of about 20ms.
 */
public class Command_Wait extends Command {
  private long m_waitTime;
  private long m_initTime;

  /**
   * Instantiate the command giving the number of milliseconds to wait.
   * 
   * @param milliseconds
   */
  public Command_Wait(long milliseconds) {
    super("Wait(" + milliseconds + "ms)");
    if (milliseconds < 0) {
      throw new IllegalArgumentException("Wait time must not be negative.  Given:" + milliseconds);
    }
    this.m_waitTime = milliseconds;
  }

  //
  // Called just before this Command runs the first time
  //
  @Override
  protected void initialize() {
    //
    // Get roboRIO FPGA time as our initialized time.
    //
    m_initTime = getFPGATimeMilliseconds();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    long currentTime = getFPGATimeMilliseconds();
    long timeSinceInit = (m_initTime < 0) ? 0 : currentTime - m_initTime;
    return timeSinceInit >= m_waitTime;
  }

  /**
   * Gets the FPGA time and converts it from microseconds to milliseconds.
   * 
   * @return FPGA time in milliseconds.
   */
  private long getFPGATimeMilliseconds() {
    return RobotController.getFPGATime() / 1000;
  }
}
