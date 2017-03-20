package org.usfirst.frc.team5854.robot;

import static org.usfirst.frc.team5854.Utils.SpecialFunctions.currentColor;
import static org.usfirst.frc.team5854.Utils.SpecialFunctions.map;
import static org.usfirst.frc.team5854.robot.AutoMethods.moveBackward;
import static org.usfirst.frc.team5854.robot.AutoMethods.moveForward;
import static org.usfirst.frc.team5854.robot.AutoMethods.shootFor;
import static org.usfirst.frc.team5854.robot.AutoMethods.turnRightGyro;
import static org.usfirst.frc.team5854.robot.AutoMethods.turnLeftGyro;

import org.usfirst.frc.team5854.Utils.EightDrive;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	// Setup robot
	public static EightDrive mecanumDrive;
	static VictorSP shooterMotor;
	static VictorSP agitatorMotor;
	VictorSP climberMotor;
	VictorSP climberMotor2;
	VictorSP harvesterMotor;
	Servo leftGearServo;
	Servo rightGearServo;
	Joystick mainJoystick;
	static Joystick buttonJoystick;
	Joystick altJoystick;
	static ADXRS450_Gyro gyro;

	// Setup variables for autonomous
	final String ObjectiveD = "Objective #D";
	final String Objective0 = "Objective #0";
	final String Objective1 = "Objective #1";
	final String Objective2 = "Objective #2";
	final String Objective3 = "Objective #3";
	final String Objective4 = "Objective #4";
	final String Objective45 = "Objective #45";
	final String Objective27 = "Objective #27";
	final String Objective37 = "Objective #37";
	String autoSelected;
	SendableChooser<String> chooser;

	// Setup variables for drivers and operators
	final String Default = "driverDefault";
	final String Caleb = "driverCaleb";
	final String Abby = "driverAbby";
	final String Aeron = "driverAeron";
	final String Jacob = "driverJacob";
	final String Josh = "driverJosh";
	SendableChooser<String> operator;
	SendableChooser<String> driver;

	// Setup camera variables
	//CameraStreamer cameraServer;

	// Setup driver array (Default)(Caleb)(Abby)(Aeron)  (buttons: forward, side, twist, cam1, cam2, cam3, speed)
	int pDriverArray[][] = { { 0, 1, 2, 4, 3, 1, 2 }, { 2, 3, 0, 4, 3, 1, 6 }, { 0, 1, 2, 3, 4, 1, 2 }, { 0, 1, 2, 3, 2, 4, 8 } };

	// setup driver array (Default)(Abby)(Aeron)(Jacob)(Josh)
	int sDriverArray[][] = { { 7, 8, 11, 3, 6, 4, 5 }, { 8,	7, 12, 3, 11, 5, 4 }, { 7, 8, 4, 3, 11, 12, 9}, { 7, 8, 4, 3, 6, 5, 10 }, { 7, 8, 11, 3, 6, 4, 5 } };

	// store the value of the drivers
	int d, o;

	// Setup debug boolean
	boolean debug = false;

	public void robotInit() {
		// Drive Train Creation
		mecanumDrive = new EightDrive(2, 3, 1, 4, 7, 5, 8, 6);

		// PWM motors for non-drive mechanics
		harvesterMotor = new VictorSP(0);
		rightGearServo = new Servo(1);
		leftGearServo = new Servo(2);
		shooterMotor = new VictorSP(3);
		agitatorMotor = new VictorSP(4);
		climberMotor = new VictorSP(5);
		climberMotor2 = new VictorSP(6);

		// Configure ports for each joystick.
		buttonJoystick = new Joystick(0); // Buttons joystick
		mainJoystick = new Joystick(1); // Main joystick
		altJoystick = new Joystick(2); // Alternate joystick

		// Setup gyro
		gyro = new ADXRS450_Gyro();

		// Setup SmartDashboard - Driver
		driver = new SendableChooser<String>();
		driver.addDefault("Caleb", Caleb);
		driver.addObject("Abby", Abby);
		driver.addObject("Aeron", Aeron);
		driver.addObject("Default", Default);
		SmartDashboard.putData("Driver", driver);

		// Setup SmartDashboard - Operator
		operator = new SendableChooser<String>();
		operator.addDefault("Default", Default);
		operator.addObject("Abby", Abby);
		operator.addObject("Aeron", Aeron);
		operator.addObject("Jacob", Jacob);
		operator.addObject("Josh", Josh);
		SmartDashboard.putData("Operator", operator);

		// Setup SmartDashboard - Autonomous
		chooser = new SendableChooser<String>();
		chooser.addObject("Dont Move", ObjectiveD);
		chooser.addDefault("Objective #0", Objective0);
		chooser.addObject("Objective #1", Objective1);
		chooser.addObject("Objective #2", Objective2);
		chooser.addObject("Objective #3", Objective3);
		chooser.addObject("Objective #4", Objective4);
		chooser.addObject("Objective #4 + #5", Objective45);
		chooser.addObject("Objective #2 + #7", Objective27);
		chooser.addObject("Objective #3 + #7", Objective37);
		SmartDashboard.putData("Autonomous choices", chooser);

		// Setup Camera - Default to Gear Camera
		//cameraServer = new CameraStreamer(1181);
		//cameraServer.setResolution();
		//cameraServer.setCameraNumber(0);
	}

	boolean autoOnce = true;

	public void autonomousInit() {
		// select autonomous
		autoSelected = ((String) chooser.getSelected()); 
		
		// initiate mecanum autonomous mode
		mecanumDrive.setCANTalonDriveMode(CANTalon.TalonControlMode.PercentVbus);
		
		// reset sensors
		gyro.reset();
		mecanumDrive.resetEncoders();
		
		// zero all motors
		gearManager(false); // keep gear servos closed
		shooterManager(false, false);
		climberManager(false);
		harvesterManager(false, false);
		mecanumDrive.stop();
		
		// reset autoOnce
		autoOnce = true;
	}

	public void autonomousPeriodic() {
		autoSelected = ((String) chooser.getSelected()); // select autonomous
		if (autoOnce) {
			switch (autoSelected) {
			case Objective0:
				moveForward(120.0); // move forward for 110 inches at 1.0 speed
				break;
			///////////////////////////////////////
			case Objective1:
				moveForward(110); // move forward for 80 inches at 1.0 speed Originally 94.75
				break;
			///////////////////////////////////////
			case Objective2:
			case Objective27:
				moveForward(84); // move forward for 68.234 inches at 1.0 speed
				turnRightGyro(57.0); // turn left to 30 degree
				moveForward(102); // move forward 66.22 inches at 1.0 speed
				if(autoSelected == Objective27 && AutoMethods.checkAuton()){
					gearManager(true);
					for(int i = 0; i < 2; i++){}
					moveBackward(20.0);
					turnRightGyro(138.0);
					moveForward(93.0);
					shootFor(0.2, true, false);
					shootFor(4.8, true, true);
				}
				break;
			///////////////////////////////////////
			case Objective3:
			case Objective37:
				moveForward(84); // move forward for 68.234 inches at 1.0 speed
				turnLeftGyro(57.0); // turn left to 30 degree
				moveForward(102); // move forward 66.22 inches at 1.0 speed
				if(autoSelected == Objective37 && AutoMethods.checkAuton()){
					gearManager(true);
					for(int i = 0; i < 2; i++){}
					moveBackward(20.0);
					turnLeftGyro(145.0);
					moveForward(91.0);
					shootFor(0.2, true, false);
					shootFor(4.8, true, true);
				}
				break;
			///////////////////////////////////////
			case Objective4:
			case Objective45:
				moveBackward(13.0); // move backward for 13 in at 1.0 speed
				if (currentColor() == "Blue") turnLeftGyro(23.0); // if blue turn right
				else turnRightGyro(23.0); // if red turn left
				shootFor(.2, true, false); // spin up shooter for 2 seconds
				shootFor(4.0, true, true); // shoot balls for 5 seconds
				shooterManager(false, false); // disable shooter
				if (autoSelected == Objective45 && AutoMethods.checkAuton()) {
					if (currentColor() == "Blue") turnLeftGyro(148.0); // if blue turn right
					else turnRightGyro(148.0); // if red turn left
					moveForward(100.0); // move forward for 100 inches at 1.0 speed
				}
				break;
			//////////////////////////////////
			default:
				mecanumDrive.stop(); // Stop all motor movement
				break;
			}
			mecanumDrive.stop(); // stop all motors
		}
		autoOnce = false; // reset autoOnce
	}

	boolean fullSpeed = false;

	public void teleopInit() {
		// initiate mecanum drive mode
		mecanumDrive.setCANTalonDriveMode(CANTalon.TalonControlMode.PercentVbus);

		// sets the deadband for the drive system.
		mecanumDrive.setDeadband(0.1);

		// ensure that full speed starts as false
		fullSpeed = false;

		// sets how fast the robot strafes and drives
		mecanumDrive.setSpeedMultiplyer(0.5);

		// set how fast the robot rotates
		mecanumDrive.setTwistMultiplyer(0.3);
		
		// stop all robot movement
		// mecanumDrive.stop();

	}

	public void teleopPeriodic() {
		
		// set primary - driver
		if (driver.getSelected() == Caleb)
			d = 1;
		else if (driver.getSelected() == Abby)
			d = 2;
		else if (driver.getSelected() == Aeron)
			d = 3;
		else
			d = 0;

		// set secondary - operator
		if (operator.getSelected() == Abby)
			o = 1;
		else if (operator.getSelected() == Aeron)
			o = 2;
		else if (operator.getSelected() == Jacob)
			o = 3;
		else if (operator.getSelected() == Josh) 
			o = 4;
		else
			o = 0;

		// sets how fast you can rotate the robot
		if (altJoystick.getRawButton(pDriverArray[d][6])) {
			while (altJoystick.getRawButton(pDriverArray[d][6])) {}
			fullSpeed = !fullSpeed;
			
			if (fullSpeed) {
				mecanumDrive.setSpeedMultiplyer(1.0);
				mecanumDrive.setTwistMultiplyer(0.8);
			} else {
				mecanumDrive.setSpeedMultiplyer(0.5);
				mecanumDrive.setTwistMultiplyer(0.3);
			}
		}

//		if (mainJoystick.getRawButton(pDriverArray[d][6])) {
//			mecanumDrive.setSpeedMultiplyer(1.0);
//			mecanumDrive.setTwistMultiplyer(0.8);
//		} else {
//			mecanumDrive.setSpeedMultiplyer(0.5);
//			mecanumDrive.setTwistMultiplyer(0.3);
//		}
		
		
		// set how the robot drive is manipulated
		if (mainJoystick.getName().startsWith("HK-MT6")) {
			mecanumDrive.mecanumDrive_Cartesian(mainJoystick.getRawAxis(pDriverArray[d][0]), mainJoystick.getRawAxis(pDriverArray[d][1]), mainJoystick.getRawAxis(pDriverArray[d][2]), 0);
		} else if (!altJoystick.getName().equals("")) {
			mecanumDrive.mecanumDrive_Cartesian(altJoystick.getRawAxis(pDriverArray[d][0]), altJoystick.getRawAxis(pDriverArray[d][1]), altJoystick.getRawAxis(pDriverArray[d][2]), 0);
		} else {
			mecanumDrive.mecanumDrive_Cartesian(buttonJoystick.getX(), buttonJoystick.getY(), buttonJoystick.getTwist(), 0);
		}

		// set how to operate gear
		gearManager(buttonJoystick.getRawButton(sDriverArray[o][3]));

		// set how to activate climber
		climberManager(buttonJoystick.getRawButton(sDriverArray[o][2]));

		// set how to operate agitator and shooter
		shooterManager(buttonJoystick.getRawButton(2), buttonJoystick.getRawButton(1));

		// set how to operate
		harvesterManager(buttonJoystick.getRawButton(sDriverArray[o][0]), buttonJoystick.getRawButton(sDriverArray[o][1]));
	}

	public void gearManager(boolean open) {
		if (open) {
			leftGearServo.setAngle(0.0);
			rightGearServo.setAngle(90.0);
		} else {
			leftGearServo.setAngle(90.0);
			rightGearServo.setAngle(0.0);
		}
	}

	public void harvesterManager(boolean intake, boolean outtake) {
		if (outtake) {
			harvesterMotor.setSpeed(1.0);
		} else if (intake) {
			harvesterMotor.setSpeed(-1.0);
		} else {
			harvesterMotor.setSpeed(0.0);
		}
	}

	public void climberManager(boolean climb) {
		if (climb) {
			climberMotor.setSpeed(map(buttonJoystick.getThrottle(), -1, 1, -1, -.50));
			climberMotor2.setSpeed(map(buttonJoystick.getThrottle(), -1, 1, 1, .50));
		} else {
			climberMotor.setSpeed(0.0);
			climberMotor2.setSpeed(0.0);
		}
	}

	public static void shooterManager(boolean shoot, boolean agitate) {
		double shooterSpeed = -1.0;
		double agitatorSpeed = 0.5;
		boolean isAuton = DriverStation.getInstance().isAutonomous();
		if (!shoot && agitate) { // reverse agitator
			shooterMotor.setSpeed(0.0);
			if (isAuton)
				agitatorMotor.setSpeed(-agitatorSpeed);
			else
				agitatorMotor.setSpeed(map(buttonJoystick.getThrottle(), -1, 1, -1, -.25));
		} else if (shoot && !agitate) { // spin up shooter
			shooterMotor.setSpeed(shooterSpeed);
			agitatorMotor.setSpeed(0.0);
		} else if (shoot && agitate) { // shoot and agitate
			shooterMotor.setSpeed(shooterSpeed);
			if (isAuton)
				agitatorMotor.setSpeed(agitatorSpeed);
			else
				agitatorMotor.setSpeed(map(buttonJoystick.getThrottle(), -1, 1, 1, .25));
		} else { // stop both shooter and agitator
			shooterMotor.setSpeed(0.0);
			agitatorMotor.setSpeed(0.0);
		}
	}

	boolean once = false;

	public void testInit() {
		mecanumDrive.resetEncoders(); // reset encoders
		mecanumDrive.stop(); // stop all motors
		gyro.reset(); // reset gyro
		gearManager(false); // keep gear servos closed
		climberManager(false); // keep climber off
		shooterManager(false, false); // dont shoot
		harvesterManager(false, false); //dont harvest
		once = false; //reset once
	}

	public void testPeriodic() {
		if (buttonJoystick.getRawButton(4)) {
			while (buttonJoystick.getRawButton(4)) {
				mecanumDrive.resetEncoders();
				gyro.reset();
			}
			once = false;
		}
		if (!once) { // BEGIN TEST

			moveForward(10.0);
			// strafeLeft(2.0);
			
		} // END TEST
		mecanumDrive.stop(); // stop all motors
		gearManager(false); // keep gear servos closed
		climberManager(false); // keep climber off
		shooterManager(false, false); // dont shoot
		harvesterManager(false, false); //dont harvest
		once = true; // code has run once
	}
}
