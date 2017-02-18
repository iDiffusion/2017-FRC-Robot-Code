package org.usfirst.frc.team5854.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.RobotDrive;

public class EightDrive
{
  private RobotDrive mecanumDriveOne;
  private CANTalon leftFrontMotorOne;
  private CANTalon rightFrontMotorOne;
  private CANTalon leftRearMotorOne;
  private CANTalon rightRearMotorOne;
  private RobotDrive mecanumDriveTwo;
  private CANTalon leftFrontMotorTwo;
  private CANTalon rightFrontMotorTwo;
  private CANTalon leftRearMotorTwo;
  private CANTalon rightRearMotorTwo;
  
  public EightDrive(int leftFrontMotorOneValue, int rightFrontMotorOneValue, int leftRearMotorOneValue, int rightRearMotorOneValue, int leftFrontMotorTwoValue, int rightFrontMotorTwoValue, int leftRearMotorTwoValue, int rightRearMotorTwoValue)
  {
    this.leftFrontMotorOne = new CANTalon(leftFrontMotorOneValue);
    this.rightFrontMotorOne = new CANTalon(rightFrontMotorOneValue);
    this.leftRearMotorOne = new CANTalon(leftRearMotorOneValue);
    this.rightRearMotorOne = new CANTalon(rightRearMotorOneValue);
    
    this.leftFrontMotorTwo = new CANTalon(leftFrontMotorTwoValue);
    this.rightFrontMotorTwo = new CANTalon(rightFrontMotorTwoValue);
    this.leftRearMotorTwo = new CANTalon(leftRearMotorTwoValue);
    this.rightRearMotorTwo = new CANTalon(rightRearMotorTwoValue);
    
    this.leftFrontMotorOne.setInverted(true);
    this.rightFrontMotorOne.setInverted(false);
    this.leftRearMotorOne.setInverted(true);
    this.rightRearMotorOne.setInverted(false);
    
    this.leftFrontMotorTwo.setInverted(true);
    this.rightFrontMotorTwo.setInverted(false);
    this.leftRearMotorTwo.setInverted(true);
    this.rightRearMotorTwo.setInverted(false);
    
    this.mecanumDriveOne = new RobotDrive(this.leftFrontMotorOne, this.leftRearMotorOne, this.rightFrontMotorOne, this.rightRearMotorOne);
    this.mecanumDriveTwo = new RobotDrive(this.leftFrontMotorTwo, this.leftRearMotorTwo, this.rightFrontMotorTwo, this.rightRearMotorTwo);
  }
  
  public EightDrive(CANTalon leftFrontMotorOne, CANTalon rightFrontMotorOne, CANTalon leftRearMotorOne, CANTalon rightRearMotorOne, CANTalon leftFrontMotorTwo, CANTalon rightFrontMotorTwo, CANTalon leftRearMotorTwo, CANTalon rightRearMotorTwo)
  {
    this.leftFrontMotorOne = leftFrontMotorOne;
    
    this.rightFrontMotorOne = rightFrontMotorOne;
    
    this.leftRearMotorOne = leftRearMotorOne;
    
    this.rightRearMotorOne = rightRearMotorOne;
    
    this.leftFrontMotorTwo = leftFrontMotorTwo;
    
    this.rightFrontMotorTwo = rightFrontMotorTwo;
    
    this.leftRearMotorTwo = leftRearMotorTwo;
    
    this.rightRearMotorTwo = rightRearMotorTwo;
    
    this.mecanumDriveOne = new RobotDrive(leftFrontMotorOne, leftRearMotorOne, rightFrontMotorOne, rightRearMotorOne);
    this.mecanumDriveTwo = new RobotDrive(leftFrontMotorTwo, leftRearMotorTwo, rightFrontMotorTwo, rightRearMotorTwo);
  }
  
  public void setCANTalonDriveMode(CANTalon.TalonControlMode control)
  {
    this.leftFrontMotorOne.changeControlMode(control);
    this.rightFrontMotorOne.changeControlMode(control);
    this.leftRearMotorOne.changeControlMode(control);
    this.leftRearMotorOne.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
    this.rightRearMotorOne.changeControlMode(control);
    this.rightRearMotorOne.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
    this.leftFrontMotorTwo.changeControlMode(control);
    this.rightFrontMotorTwo.changeControlMode(control);
    this.leftRearMotorTwo.changeControlMode(control);
    this.rightRearMotorTwo.changeControlMode(control);
  }
  
  private double multiplyer = 1.0D;
  private double twistMult = 1.0D;
  private double deadzone = 0.0D;
  
  public void mecanumDrive_Cartesian(double xAxis, double yAxis, double rotation, double gyroAngle)
  {
    double xValue = 0.0D;double yValue = 0.0D;double rValue = 0.0D;
    if ((xAxis > this.deadzone) || (xAxis < -this.deadzone)) {
      xValue = xAxis * -1.0D * this.multiplyer;
    }
    if ((yAxis > this.deadzone) || (yAxis < -this.deadzone)) {
      yValue = yAxis * -1.0D * this.multiplyer;
    }
    if ((rotation > this.deadzone) || (rotation < -this.deadzone)) {
      rValue = rotation * -1.0D * this.twistMult;
    }
    this.mecanumDriveOne.mecanumDrive_Cartesian(xValue, yValue, rValue, gyroAngle);
    this.mecanumDriveTwo.mecanumDrive_Cartesian(xValue, yValue, rValue, gyroAngle);
  }
  
  public void mecanumDrive_Polar(double magnitude, double direction, double rotation)
  {
    this.mecanumDriveOne.mecanumDrive_Polar(magnitude, direction, rotation);
    this.mecanumDriveTwo.mecanumDrive_Polar(magnitude, direction, rotation);
  }
  
  public void setSpeedMultiplyer(double multiplyer)
  {
    this.multiplyer = multiplyer;
  }
  
  public void setTwistMultiplyer(double multiplyer)
  {
    this.twistMult = multiplyer;
  }
  
  public void setDeadband(double deadband)
  {
    this.deadzone = deadband;
  }
  
  public void setBrakeMode(boolean braker)
  {
    this.leftFrontMotorOne.enableBrakeMode(braker);
    this.rightFrontMotorOne.enableBrakeMode(braker);
    this.leftRearMotorOne.enableBrakeMode(braker);
    
    this.rightRearMotorOne.enableBrakeMode(braker);
    
    this.leftFrontMotorTwo.enableBrakeMode(braker);
    this.rightFrontMotorTwo.enableBrakeMode(braker);
    this.leftRearMotorTwo.enableBrakeMode(braker);
    this.rightRearMotorTwo.enableBrakeMode(braker);
  }
  
  public void moveRightSide(double speed)
  {
    this.rightFrontMotorOne.set(speed);
    this.rightRearMotorOne.set(speed);
    this.rightFrontMotorTwo.set(speed);
    this.rightRearMotorTwo.set(speed);
  }
  
  public void moveLeftSide(double speed)
  {
    this.leftFrontMotorOne.set(speed);
    this.leftRearMotorOne.set(speed);
    this.leftFrontMotorTwo.set(speed);
    this.leftRearMotorTwo.set(speed);
  }
  
  public void resetEncoders()
  {
    this.rightRearMotorOne.setEncPosition(0);
    this.leftRearMotorOne.setEncPosition(0);
  }
  
  public double getEncValueRight()
  {
    return Math.abs(this.rightRearMotorOne.getEncPosition());
  }
  
  public double getEncValueLeft()
  {
    return Math.abs(this.leftRearMotorOne.getEncPosition());
  }
}
