/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.team1123.camera;

import java.util.concurrent.atomic.AtomicBoolean;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;

/**
 * @author AIM Robotics, FRC Team 1123
 * @author A. Black
 *
 */
public class AIMRoboticsUsbCamera implements AIMRoboticsCameraFrameProcessor {
	private int devId;
	private String devName;
	private UsbCamera usbCamera;
	private CvSink cvSink;
	private CvSource outputStream;
	private Mat source;
	private Mat output;
	private AtomicBoolean isEnabled;

	public AIMRoboticsUsbCamera(int devId, String devName, int width, int height, int fps) {
		this.devId = devId;
		this.devName = devName;
		this.source = new Mat();
		this.output = new Mat();

		CameraServer cameraServer = CameraServer.getInstance();
		this.usbCamera = cameraServer.startAutomaticCapture(this.devName, this.devId);
		// this.usbCamera.setFPS(fps);
		this.cvSink = cameraServer.getVideo(usbCamera);
		this.outputStream = cameraServer.putVideo(devName, width, height);
		this.isEnabled = new AtomicBoolean(true);
	}

	@Override
	public void processCameraFrame() {
		if (!this.isEnabled.get()) {
			// outputStream.notifyError(String.join(devName, ":", String.valueOf(devId), " - Disabled"));
			return;
		}

		if (cvSink.grabFrame(source) == 0) {
			outputStream.notifyError(cvSink.getError());
			return;
		}
		Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
		outputStream.putFrame(output);
	}

	public boolean isEnabled() {
		return isEnabled.get();
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled.set(isEnabled);
	}

	public VideoSource getVideoSource() {
		return this.usbCamera;
	}
}
