package org.usfirst.frc.team5854.robot;

import org.usfirst.frc.team5854.Utils.EightDrive;
import org.usfirst.frc.team5854.Utils.SpecialFunctions;
import static org.usfirst.frc.team5854.Utils.SpecialFunctions.map;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class AutoMethods {

	static EightDrive mecanumDrive = Robot.mecanumDrive;
	static ADXRS450_Gyro gyro = Robot.gyro;
	static double gyroAngle = 0;
	
	public static void visionTurn()
	  {
	    gyroAngle = gyro.getAngle();
	    double Angle = RoboSockets.getValue();
	    char directionChar = 'r';
	    if (Angle < 0.0) {
	      directionChar = 'L';
	    } else {
	      directionChar = 'R';
	    }
	    System.out.println(Angle);
	    Angle = Math.abs(Angle);
	    
	    turnGyro(directionChar, Angle);
	  }
	  
	  double angleError = 5.0;
	  
	  public static void turnGyro(char direction, double angle)
	  {
	    double otherNumber = 0.0;
	    double slowMotorSpeed = 0.0;
	    double speed = .5;
	    int state = 1;
	    //angle /= 2.0;
	    gyro.reset();
	    while (state == 1)
	    {
	      otherNumber = Math.abs(gyro.getAngle());
	      System.out.println("Gyro : " + otherNumber);
	      if ((direction == 'L') || (direction == 'l'))
	      {
	    	speed = map(otherNumber, 0, angle, .7, .2);
	        mecanumDrive.mecanumDrive_Polar(0, 0, speed);
	        if (otherNumber < angle) {
	          state = 2;
	        }
	      }
	      else if ((direction == 'R') || (direction == 'r'))
	      {
	    	speed = map(otherNumber, 0, angle, .7, .2);
	        mecanumDrive.mecanumDrive_Polar(0, 0, -speed);
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
	      otherNumber = gyro.getAngle();
	      if (otherNumber < angle)
	      {
	        mecanumDrive.mecanumDrive_Polar(0, 0, slowMotorSpeed);
	        if (otherNumber >= angle)
	        {
	          mecanumDrive.mecanumDrive_Polar(0, 0, 0);
	          state = 3;
	        }
	      }
	      else if (otherNumber > angle)
	      {
	        mecanumDrive.mecanumDrive_Polar(0, 0, -slowMotorSpeed);
	        if (otherNumber <= angle)
	        {
	          mecanumDrive.mecanumDrive_Polar(0, 0, 0);
	          state = 4;
	        }
	      }
	      else
	      {
	        state = 3;
	      }
	    }
	  }
	  
	  
	  public static void moveWithMap(double inches) {
		  double converstion = (4000 / (6 * Math.PI));
		  double desRotation = converstion * inches;
	      System.out.println("DesRotations " + desRotation);

		  double speed = 0;
		  
		  double highSpeed = speed;
		  double slowSpeed = speed + 0.1;
		  
		  mecanumDrive.resetEncoders();
		  double rotation = 0.0;
		  while (rotation < desRotation) {
			  speed = SpecialFunctions.map(mecanumDrive.getEncValueRight(), 0, desRotation, 1, .1);
			  highSpeed = speed;
			  slowSpeed = speed + 0.1;
			  
			  rotation = mecanumDrive.getEncValueLeft();
			  
			  
		      System.out.println("Rotations " + rotation);
		      System.out.println("DesRotations " + desRotation);
		      if (mecanumDrive.getEncValueLeft() > mecanumDrive.getEncValueRight())
		      {
		        mecanumDrive.moveLeftSide(highSpeed);
		        mecanumDrive.moveRightSide(slowSpeed);
		      }
		      else if (mecanumDrive.getEncValueRight() > mecanumDrive.getEncValueLeft())
		      {
		        mecanumDrive.moveLeftSide(slowSpeed);
		        mecanumDrive.moveRightSide(highSpeed);
		      }
		      else
		      {
		        mecanumDrive.moveLeftSide(highSpeed);
		        mecanumDrive.moveRightSide(highSpeed);
		      }
		 }
		  mecanumDrive.moveLeftSide(0);
	      mecanumDrive.moveRightSide(0);
		  
	  }
	  
	  
	  
	  
	  
	  
	  
	  public static void move(double distance, double speed)
	  {
	    double converstion = (4000 / (6 * Math.PI));
	    double desRotation = converstion * distance;
	    
	    double highSpeed = speed;
	    double slowSpeed = speed + .1;
	    
	    mecanumDrive.resetEncoders();
	    
	    System.out.println("Second, Run Once");
	    
	    double rotation = 0;
	    while (rotation < desRotation)
	    {
	      rotation = mecanumDrive.getEncValueLeft();
	      System.out.println("Rotations " + rotation);
	      System.out.println("DesRotations " + desRotation);
	      if (mecanumDrive.getEncValueLeft() > mecanumDrive.getEncValueRight())
	      {
	        mecanumDrive.moveLeftSide(highSpeed);
	        mecanumDrive.moveRightSide(slowSpeed);
	      }
	      else if (mecanumDrive.getEncValueRight() > mecanumDrive.getEncValueLeft())
	      {
	        mecanumDrive.moveLeftSide(slowSpeed);
	        mecanumDrive.moveRightSide(highSpeed);
	      }
	      else
	      {
	        mecanumDrive.moveLeftSide(highSpeed);
	        mecanumDrive.moveRightSide(highSpeed);
	      }
	    }
	    while (rotation > desRotation + converstion * 4)
	    {
	      rotation = mecanumDrive.getEncValueLeft();
	      System.out.println("Rotations " + rotation);
	      System.out.println("DesRotations " + desRotation);
	      
	      mecanumDrive.moveLeftSide(-.1);
	      mecanumDrive.moveRightSide(-.1);
	    }
	    for (int i = 0; i < 100; i++)
	    {
	      mecanumDrive.moveLeftSide(0);
	      mecanumDrive.moveRightSide(0);
	    }
	  }
}
