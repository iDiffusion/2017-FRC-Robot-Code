package org.usfirst.frc.team5854.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot
  extends IterativeRobot
{
  boolean CalebsController = true;
  EightDrive mecanumDrive;
  Encoder backLeftEnc;
  VictorSP shootermotor;
  VictorSP agitatormotor;
  Servo leftgearservo;
  Servo rightgearservo;
  Joystick mainJoystick;
  Joystick buttonJoystick;
  ADXRS450_Gyro gyro;
  VictorSP climbermotor;
  VictorSP harvestermotor;
  final String driveForward = "driveforward";
  final String spin = "spin";
  final String visionTrackingTest = "Vision Test";
  final String objective1 = "Objective1";
  final String objective2 = "Objective2";
  final String objective3 = "Objective3";
  final String objective4 = "Objective4";
  final String objective45 = "Objective45";
  final String objective6 = "Objective6";
  final String objective67 = "Objective67";
  String autoSelected;
  SendableChooser<String> chooser;
  final String calebsController = "caleb";
  final String mainjoy = "mainjoy";
  String stickSelected;
  SendableChooser<String> controllerChooser;
  CameraStreamer cameraserver;
  CameraStreamer cameraserver1;
  CameraStreamer cameraserver2;
  
  public void robotInit()
  {
    this.mecanumDrive = new EightDrive(2, 3, 1, 4, 7, 5, 8, 6);
    this.backLeftEnc = new Encoder(0, 1, false, CounterBase.EncodingType.k4X);
    this.leftgearservo = new Servo(2);
    this.rightgearservo = new Servo(1);
    
    this.shootermotor = new VictorSP(3);
    this.agitatormotor = new VictorSP(4);
    
    this.climbermotor = new VictorSP(5);
    
    this.mainJoystick = new Joystick(1);
    this.buttonJoystick = new Joystick(0);
    
    this.gyro = new ADXRS450_Gyro();
    
    this.harvestermotor = new VictorSP(0);
    
    this.chooser = new SendableChooser();
    this.chooser.addDefault("Drive forward", "driveforward");
    this.chooser.addObject("spin", "spin");
    this.chooser.addObject("Vision Test", "Vision Test");
    this.chooser.addObject("Objective #1", "Objective1");
    this.chooser.addObject("Objective #2", "Objective2");
    this.chooser.addDefault("Objective #3", "Objective3");
    this.chooser.addDefault("Objective #4", "Objective4");
    this.chooser.addDefault("Objective #4 and #5", "Objective45");
    this.chooser.addDefault("Objective #6", "Objective6");
    this.chooser.addDefault("Objective #6 and #7", "Objective67");
    SmartDashboard.putData("Auto choices", this.chooser);
    this.controllerChooser = new SendableChooser();
    this.controllerChooser.addDefault("Calebs Controller", "caleb");
    this.controllerChooser.addObject("main Joystick", "mainjoy");
    SmartDashboard.putData("Choose Controller", this.controllerChooser);
    
    this.cameraserver = new CameraStreamer(0, 1181);
    this.cameraserver.setResolution(640, 400);
    this.cameraserver.setBrightness(1);
  }
  
  double gyroOffset = 0.0D;
  
  public void autonomousInit()
  {
    this.autoSelected = ((String)this.chooser.getSelected());
    this.mecanumDrive.setCANTalonDriveMode(CANTalon.TalonControlMode.PercentVbus);
    this.gyro.reset();
    this.go = true;
  }
  
  boolean go = true;
  
  public void autonomousPeriodic()
  {
    if (this.buttonJoystick.getRawButton(7))
    {
      this.go = true;
      this.gyro.reset();
    }
    if (this.go) {
      switch (this.autoSelected)
      {
      case "Vision Test": 
        visionTurn();
        this.go = false;
        break;
      case "spin": 
        turnGyro('R', 90.0D);
        this.go = false;
        break;
      case "driveforward": 
        move(30.0D, 0.2D);
        this.go = false;
        break;
      case "Objective1": 
        move(94.75D, 0.5D);
        gearManager(true);
        break;
      case "Objective2": 
        move(68.234D, 0.5D);
        turnGyro('R', 30.0D);
        move(66.217D, 0.5D);
        gearManager(true);
        break;
      case "Objective3": 
        move(68.234D, 0.5D);
        turnGyro('L', 30.0D);
        move(66.217D, 0.5D);
        gearManager(true);
        break;
      case "Objective4": 
        move(13.0D, -0.5D);
        turnGyro('R', 23.0D);
        for (int i = 0; i < 3000; i++) {
          shooterManager(true, false);
        }
        for (int i = 0; i < 7000; i++) {
          shooterManager(true, true);
        }
        break;
      case "Objective45": 
        turnGyro('R', 148.0D);
        move(100.0D, 0.5D);
        gearManager(true);
        move(8.0D, -0.5D);
        break;
      case "Objective6": 
        move(13.0D, -0.5D);
        turnGyro('L', 23.0D);
        for (int i = 0; i < 3000; i++) {
          shooterManager(true, false);
        }
        for (int i = 0; i < 7000; i++) {
          shooterManager(true, true);
        }
        break;
      }
    }
  }
  
  public int secToTicks(double secs)
  {
    return (int)(secs * 500.0D);
  }
  
  double gyroAngle = 0.0D;
  
  public void teleopPeriodic()
  {
    this.stickSelected = ((String)this.controllerChooser.getSelected());
    switch (this.stickSelected)
    {
    case "caleb": 
      this.CalebsController = true;
      break;
    case "mainjoy": 
      this.CalebsController = false;
    }
    this.mecanumDrive.setCANTalonDriveMode(CANTalon.TalonControlMode.PercentVbus);
    
    this.mecanumDrive.setDeadband(0.1);
    
    this.mecanumDrive.setTwistMultiplyer(0.3);
    
    this.mecanumDrive.setSpeedMultiplyer(.5);
    if (this.CalebsController) {
      this.mecanumDrive.mecanumDrive_Cartesian(this.mainJoystick.getRawAxis(0), this.mainJoystick.getRawAxis(1), this.mainJoystick
        .getRawAxis(2), 0);
    } else {
      this.mecanumDrive.mecanumDrive_Cartesian(this.buttonJoystick.getX(), this.buttonJoystick.getY(), this.buttonJoystick.getTwist(), 0.0D);
    }
    gearManager(this.buttonJoystick.getRawButton(3));
    
   // climberManager(this.buttonJoystick.getRawButton(11));
    
    shooterManager(this.buttonJoystick.getRawButton(2), this.buttonJoystick.getRawButton(1));
    
    harvesterManager(this.buttonJoystick.getRawButton(7), this.buttonJoystick.getRawButton(8));
    
    this.gyroAngle = this.gyro.getAngle();
  }
  
  public void gearManager(boolean go)
  {
    if (go == true)
    {
      this.leftgearservo.setAngle(0.0D);
      this.rightgearservo.setAngle(90.0D);
    }
    else
    {
      this.leftgearservo.setAngle(90.0D);
      this.rightgearservo.setAngle(0.0D);
    }
  }
  
  public void harvesterManager(boolean go, boolean reverse)
  {
    if (go) {
      this.harvestermotor.setSpeed(-1.0D);
    } else if (reverse) {
      this.harvestermotor.setSpeed(1.0D);
    } else {
      this.harvestermotor.setSpeed(0.0D);
    }
  }
  
  public void climberManager(boolean go)
  {
    if (go) {
      this.climbermotor.setSpeed(0.7D);
    } else {
      this.climbermotor.setSpeed(0.0D);
    }
  }
  
  public void shooterManager(boolean go, boolean second)
  {
    if (go)
    {
      this.shootermotor.setSpeed(-1.0);
      if (second) {
        this.agitatormotor.setSpeed(map(this.buttonJoystick.getThrottle(), -1, 1, 1, .25));
      } else {
        this.agitatormotor.setSpeed(0.0);
      }
    }
    else
    {
      this.shootermotor.setSpeed(0.0D);
      this.agitatormotor.setSpeed(0.0D);
    }
  }
  
  public double map(double x, double in_min, double in_max, double out_min, double out_max)
  {
    return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
  }
  
  public void visionTurn()
  {
    this.gyroAngle = this.gyro.getAngle();
    double Angle = RoboSockets.getValue();
    char directionChar = 'r';
    if (Angle < 0.0D) {
      directionChar = 'L';
    } else {
      directionChar = 'R';
    }
    System.out.println(Angle);
    Angle = Math.abs(Angle);
    
    turnGyro(directionChar, Angle);
  }
  
  double angleError = 5.0D;
  
  public void turnGyro(char direction, double angle)
  {
    double otherNumber = 0.0D;
    double slowMotorSpeed = 0.0D;
    int state = 1;
    angle /= 2.0D;
    while (state == 1)
    {
      otherNumber = this.gyro.getAngle();
      System.out.println("Gyro : " + otherNumber);
      if ((direction == 'L') || (direction == 'l'))
      {
        this.mecanumDrive.mecanumDrive_Polar(0.0D, 0.0D, 1.0D);
        if (otherNumber < angle) {
          state = 2;
        }
      }
      else if ((direction == 'R') || (direction == 'r'))
      {
        this.mecanumDrive.mecanumDrive_Polar(0.0D, 0.0D, -1.0D);
        if (otherNumber > angle) {
          state = 2;
        }
      }
      else
      {
        System.out.println("This is not a direction. Usage: L or R");
      }
    }
    while (state == 2)
    {
      otherNumber = this.gyro.getAngle();
      if (otherNumber < angle)
      {
        this.mecanumDrive.mecanumDrive_Polar(0.0D, 0.0D, slowMotorSpeed);
        if (otherNumber >= angle)
        {
          this.mecanumDrive.mecanumDrive_Polar(0.0D, 0.0D, 0.0D);
          state = 3;
        }
      }
      else if (otherNumber > angle)
      {
        this.mecanumDrive.mecanumDrive_Polar(0.0D, 0.0D, -slowMotorSpeed);
        if (otherNumber <= angle)
        {
          this.mecanumDrive.mecanumDrive_Polar(0.0D, 0.0D, 0.0D);
          state = 4;
        }
      }
      else
      {
        state = 3;
      }
    }
  }
  
  
  public void moveWithMap(double inches) {
	  double converstion = (4000 / (6 * Math.PI));
	  double desRotation = converstion * inches;
	  
	  double speed = 0;
	  
	  double highSpeed = speed;
	  double slowSpeed = speed + 0.1;
	  
	  
	  double rotation = 0.0;
	  while (rotation < desRotation) {
		  speed = map (mecanumDrive.getEncValueRight(), 0, desRotation, 1, 0);
		  
		  
		  rotation = this.mecanumDrive.getEncValueLeft();
		  
		  
	      System.out.println("Rotations " + rotation);
	      System.out.println("DesRotations " + desRotation);
	      if (this.mecanumDrive.getEncValueLeft() > this.mecanumDrive.getEncValueRight())
	      {
	        this.mecanumDrive.moveLeftSide(highSpeed);
	        this.mecanumDrive.moveRightSide(slowSpeed);
	      }
	      else if (this.mecanumDrive.getEncValueRight() > this.mecanumDrive.getEncValueLeft())
	      {
	        this.mecanumDrive.moveLeftSide(slowSpeed);
	        this.mecanumDrive.moveRightSide(highSpeed);
	      }
	      else
	      {
	        this.mecanumDrive.moveLeftSide(highSpeed);
	        this.mecanumDrive.moveRightSide(highSpeed);
	      }
	 }
  }
  
  
  
  
  
  
  
  public void move(double distance, double speed)
  {
    double converstion = (4000 / (6 * Math.PI));
    double desRotation = converstion * distance;
    
    double highSpeed = speed;
    double slowSpeed = speed + 0.1D;
    
    this.mecanumDrive.resetEncoders();
    
    System.out.println("Second, Run Once");
    
    double rotation = 0.0;
    while (rotation < desRotation)
    {
      rotation = this.mecanumDrive.getEncValueLeft();
      System.out.println("Rotations " + rotation);
      System.out.println("DesRotations " + desRotation);
      if (this.mecanumDrive.getEncValueLeft() > this.mecanumDrive.getEncValueRight())
      {
        this.mecanumDrive.moveLeftSide(highSpeed);
        this.mecanumDrive.moveRightSide(slowSpeed);
      }
      else if (this.mecanumDrive.getEncValueRight() > this.mecanumDrive.getEncValueLeft())
      {
        this.mecanumDrive.moveLeftSide(slowSpeed);
        this.mecanumDrive.moveRightSide(highSpeed);
      }
      else
      {
        this.mecanumDrive.moveLeftSide(highSpeed);
        this.mecanumDrive.moveRightSide(highSpeed);
      }
    }
    while (rotation > desRotation + converstion * 4.0D)
    {
      rotation = this.mecanumDrive.getEncValueLeft();
      System.out.println("Rotations " + rotation);
      System.out.println("DesRotations " + desRotation);
      
      this.mecanumDrive.moveLeftSide(-0.1D);
      this.mecanumDrive.moveRightSide(-0.1D);
    }
    for (int i = 0; i < 100; i++)
    {
      this.mecanumDrive.moveLeftSide(0.0D);
      this.mecanumDrive.moveRightSide(0.0D);
    }
  }
  
  double r = 0.0D;
  double prevR = 0.0D;
  double l = 0.0D;
  double prevL = 0.0D;
  boolean reset = true;
  boolean once = true;
  int i = 0;
  
  public void testPeriodic()
  {
    if (this.buttonJoystick.getRawButton(4)) {
      this.once = true;
    }
    if (this.once)
    {
      move(50.0D, 0.5D);
      this.once = false;
    }
  }
}
