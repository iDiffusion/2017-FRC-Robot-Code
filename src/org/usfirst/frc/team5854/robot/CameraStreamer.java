package org.usfirst.frc.team5854.robot;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoMode.PixelFormat;

public class CameraStreamer
{
  private UsbCamera usbCamera;
  private MjpegServer mjpegServer1;
  private CvSink cvSink;
  private CvSource outputStream;
  private MjpegServer mjpegServer2;
  
  public CameraStreamer(int cameraId, int port)
  {
    this.usbCamera = new UsbCamera("USB Camera 0", cameraId);
    this.usbCamera.setResolution(640, 400);
    this.usbCamera.setBrightness(1);
    
    this.mjpegServer1 = new MjpegServer("serve_USB Camera 0", port);
    
    this.mjpegServer1.setSource(this.usbCamera);
    
    this.cvSink = new CvSink("opencv_USB Camera 0");
    
    this.cvSink.setSource(this.usbCamera);
    
    this.outputStream = new CvSource("Blur", VideoMode.PixelFormat.kMJPEG, 640, 480, 30);
    
    this.mjpegServer2 = new MjpegServer("serve_Blur", port + 1);
    
    this.mjpegServer2.setSource(this.outputStream);
  }
  
  public void setBrightness(int brightness)
  {
    this.usbCamera.setBrightness(brightness);
  }
  
  public void setResolution(int width, int height)
  {
    this.usbCamera.setResolution(640, 400);
  }
}
