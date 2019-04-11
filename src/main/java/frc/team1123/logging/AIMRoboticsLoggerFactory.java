/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.team1123.logging;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * AIM Robotics, FRC Team 1123 Logger Factory.
 * <p>
 * Applications should <b>NOT</b> instantiate this directly! Robot code should
 * use the <a href="https://www.slf4j.org">slf4j</a> mechanism for obtaining a
 * logger factory.
 * 
 * @author A. Black
 *
 */
class AIMRoboticsLoggerFactory implements org.slf4j.ILoggerFactory, Sendable {
	private static final String DEFAULT_NAME = "Logger";
	private static final String DEFAULT_SUBSYSTEM = "AIMRobotics";

	//
	// Key: logger name, value: AIMRoboticsLogger instance.
	//
	private ConcurrentMap<String, Logger> loggerMap;

	/**
	 * AIM Robotics Logger constructor.
	 * <p>
	 * <b>Do NOT instantiate this class directly in Robot code! Robot application
	 * code should use the <a href="https://www.slf4j.org">slf4j</a> mechanism for
	 * obtaining a logger factory.</b>
	 */
	public AIMRoboticsLoggerFactory() {
		//
		// Setup the name to AIMRoboticsLogger map.
		//
		loggerMap = new ConcurrentHashMap<String, Logger>();
	}

	@Override
	public Logger getLogger(String name) {
		Logger logger = loggerMap.get(name);
		if (logger == null) {
			Logger newInstance = new AIMRoboticsLogger(name);
			Logger oldInstance = loggerMap.putIfAbsent(name, newInstance);
			logger = (oldInstance == null) ? newInstance : oldInstance;
		}
		return logger;
	}

	@Override
	public String getName() {
		return DEFAULT_NAME;
	}

	@Override
	public void setName(String name) {
		// No-op;
	}

	@Override
	public String getSubsystem() {
		return DEFAULT_SUBSYSTEM;
	}

	@Override
	public void setSubsystem(String subsystem) {
		// No-op;
	}

	@Override
	public void initSendable(SendableBuilder builder) {
	}
}
