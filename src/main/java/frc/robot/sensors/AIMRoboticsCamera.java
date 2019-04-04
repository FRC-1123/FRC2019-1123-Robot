/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.sensors;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;

/**
 * Add your docs here.
 */
public class AIMRoboticsCamera {
    private static AIMRoboticsCamera singleton;

    private Thread cameraThread;

    public static AIMRoboticsCamera getInstance() {
        if (singleton == null)
            singleton = new AIMRoboticsCamera();
        return singleton;
    }

    private AIMRoboticsCamera() {
        cameraInit();
    }

    private void cameraInit() {
        cameraThread = new Thread(() -> {
            UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
            camera.setResolution(640, 480);
            
            CvSink cvSink = CameraServer.getInstance().getVideo();
            CvSource outputStream = CameraServer.getInstance().putVideo("Oven Window", 640, 480);

            Mat source = new Mat();
            Mat output = new Mat();

            while (!Thread.interrupted()) {
                if (cvSink.grabFrame(source)==0) {
                    outputStream.notifyError(cvSink.getError());
                    continue;
                }
                Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
                outputStream.putFrame(output);
            }
        });
        cameraThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread arg0, Throwable arg1) {
                arg1.printStackTrace(System.err);
            }
        });
        cameraThread.setDaemon(true);
        cameraThread.start();
    }

}
