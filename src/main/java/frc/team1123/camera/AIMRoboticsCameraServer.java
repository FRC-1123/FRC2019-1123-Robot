/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019-Present FIRST. All Rights Reserved.                     */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.team1123.camera;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import edu.wpi.cscore.VideoSource;

/**
 * The AIM Robotics camera server wraps the WPILIB camera server API and
 * provides additional capabilities commonly used by the AIM Robotics team.
 * <p>
 * 
 * @author AIM Robotics, FRC Team 1123
 * @author A. Black
 *
 */
public class AIMRoboticsCameraServer {
	private static final int DEFAULT_MAX_CAMERAS = 8;
	private static final ThreadGroup threadGroup = new ThreadGroup("AIMRoboticsCameraThreadGroup");
	private final BlockingQueue<AIMRoboticsCameraFrameProcessor> cameraQueue;
	private final Map<Integer,AIMRoboticsUsbCamera> devIdMap;
	private final Map<String,AIMRoboticsUsbCamera> devNameMap;
	private int cameraQueueCapacity;
	private static final Object singletonLock = new Object();
	private static AIMRoboticsCameraServer singleton;

	private class CameraFrameProcessorThread extends Thread {
		public CameraFrameProcessorThread(String name) {
			super(threadGroup, new Runnable() {
				@Override
				public void run() {
					while (!Thread.interrupted()) {
						try {
							AIMRoboticsCameraFrameProcessor fProcessor = cameraQueue.take();
							fProcessor.processCameraFrame();
							cameraQueue.offer(fProcessor);
						} catch (InterruptedException e) {
							break;
						}
					}
				}
			}, name);

			this.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(Thread thread, Throwable throwable) {
					StringBuilder b = new StringBuilder();
					String crlf = System.getProperty("line.separator");
					b.append(crlf).append(thread.getThreadGroup().getName()).append(":").append(thread.getName())
							.append(":").append(thread.getId());
					StringWriter error = new StringWriter();
					throwable.printStackTrace(new PrintWriter(error));
					b.append(crlf).append(error.toString());
				}
			});
		}
	}

	private AIMRoboticsCameraServer(int maxCameras, int maxFrameProcessorThreads) {
		cameraQueue = new ArrayBlockingQueue<>(maxCameras);
		devIdMap = new HashMap<>();
		devNameMap = new HashMap<>();
		this.cameraQueueCapacity = maxCameras;
		for (int i = 0; i < maxFrameProcessorThreads; i++) {
			CameraFrameProcessorThread cThread = new CameraFrameProcessorThread("AIMRoboticsCameraThread_" + String.valueOf(i));
			cThread.setDaemon(true);
			cThread.start();
		}
	}

	public static AIMRoboticsCameraServer getInstance() {
		return getInstance(DEFAULT_MAX_CAMERAS);
	}

	public static AIMRoboticsCameraServer getInstance(int maxCameras) {
		return getInstance(maxCameras, 1);
	}

	public static AIMRoboticsCameraServer getInstance(int maxCameras, int maxFrameProcessorThreads) {
		synchronized (singletonLock) {
			if (singleton == null)
				singleton = new AIMRoboticsCameraServer(maxCameras, maxFrameProcessorThreads);
		}
		return singleton;
	}

	public void addCamera(int devId, String devName, int width, int height, int fps) {
		if (cameraQueue.remainingCapacity() > 0) {
			AIMRoboticsUsbCamera usbCamera = new AIMRoboticsUsbCamera(devId, devName, width, height, fps);
			devIdMap.put(devId, usbCamera);
			devNameMap.put(devName, usbCamera);
			cameraQueue.offer(usbCamera);
		} else
			throw new IllegalStateException(
					this.getClass().getSimpleName() + " exceeded max camera count of " + this.cameraQueueCapacity);
	}

	public boolean isCameraEnabled(int devId) {
		boolean rtn = false;
		AIMRoboticsUsbCamera c  = devIdMap.get(devId);
		if (c!=null)
			rtn = c.isEnabled();
		return rtn;
	}

	public void setCameraEnabled(int devId, boolean isEnabled) {
		AIMRoboticsUsbCamera c  = devIdMap.get(devId);
		if (c!=null)
			c.setEnabled(isEnabled);
	}

	public boolean isCameraEnabled(String devName) {
		boolean rtn = false;
		AIMRoboticsUsbCamera c  = devNameMap.get(devName);
		if (c!=null)
			rtn = c.isEnabled();
		return rtn;
	}

	public void setCameraEnabled(String devName, boolean isEnabled) {
		AIMRoboticsUsbCamera c  = devNameMap.get(devName);
		if (c!=null)
			c.setEnabled(isEnabled);
	}

	public List<VideoSource> getCameras() {
		ArrayList<VideoSource> rtn = new ArrayList<>();
		for (AIMRoboticsUsbCamera vs : devIdMap.values()) {
			rtn.add(vs.getVideoSource());
		}
		return rtn;
	}
}
