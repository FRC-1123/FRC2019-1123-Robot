/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.sensors;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cameraserver.CameraServerShared;

/**
 * Add your docs here. 
 */
public class AIMRoboticsUsbCamera {

    private int devId;
    private String devName;
    private UsbCamera usbCamera;
    private CvSink cvSink;

    public AIMRoboticsUsbCamera(int devId, String devName, int width, int height) {
        this.devId = devId;
        this.devName = devName;
        CameraServer cameraServer = CameraServer.getInstance();
        usbCamera = cameraServer.startAutomaticCapture(devName, devId);
        usbCamera.setResolution(width, height);
        cvSink = cameraServer.getVideo(usbCamera);
    }

}
