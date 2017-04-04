package org.usfirst.frc.team5854.robot;

import edu.wpi.first.wpilibj.AnalogInput;

public class Ultrasonar {
	
	static AnalogInput input = new AnalogInput(0);
	
	// The double returned will be in meters
	public static double getDistance(){
		
		double voltage = input.getVoltage();	
		double distance = voltage;// / .977;
		return distance;
	}
}
