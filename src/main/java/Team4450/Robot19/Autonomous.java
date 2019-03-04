
package Team4450.Robot19;

import Team4450.Lib.*;
import Team4450.Robot19.Devices;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import java.io.File;
import Team4450.Lib.LaunchPad.*;


public class Autonomous
{
	private final Robot			robot;
	// Next statement only used with LabView dashboard.
	//private int					program = (int) SmartDashboard.getNumber("AutoProgramSelect",0);
	
	//Not an integer anymore, we are using the enum AutoProgram instead
	private AutoProgram			program;
//	private final GearBox		gearBox;

	//private LaunchPad 			lp;
	private Notifier 			m_follower_notifier;

	

	
	//Create an autoChooser from the SendableChooser funtion using the AutoProgram enum as a parameter
	private static SendableChooser<AutoProgram>	autoChooser;
	
	Autonomous(Robot robot)
	{
		Util.consoleLog();
		
		this.robot = robot;
		
		//gearBox = new GearBox(robot);
	}

	public void dispose()
	{
		Util.consoleLog();
		
		//if (gearBox != null) gearBox.dispose();
		//if (lp != null) lp.dispose();
	}
	
	private boolean isAutoActive()
	{
		return robot.isEnabled() && robot.isAutonomous();
	}
	
	// Configure SendableChooser with auto program choices and
	// send them to SmartDashboard/ShuffleBoard. Called a robot
	// program start up to get dash board ready before anything
	// happens.
	
	public static void setAutoChoices()
	{
		Util.consoleLog();
		
		//init autoChooser.
		autoChooser = new SendableChooser<AutoProgram>();
		
		autoChooser.setName("Auto Program");
		autoChooser.addDefault("No Program", AutoProgram.NoProgram);

		//The naming convention is Upper/Lower Rocket and The side of the rocket the robot is going to 
		//from the perspective of the driver station
		
		//Instead of numbers we are using the constants within the enum now
		//If you want to add another option, make sure to add in the name of the choice into
		//the AutoProgram enum
		autoChooser.addOption("Upper Close", AutoProgram.RocketUpClose);
		autoChooser.addOption("Upper Middle", AutoProgram.RocketUpMiddle);
		autoChooser.addOption("Upper Far", AutoProgram.RocketUpFar);
		autoChooser.addOption("Down Close", AutoProgram.RocketDownClose);
		autoChooser.addOption("Down Middle", AutoProgram.RocketDownMiddle);
		autoChooser.addOption("Down Far", AutoProgram.RocketDownFar);
		autoChooser.addOption("Middle Left", AutoProgram.RocketMiddlePathLeft);
		autoChooser.addOption("Middle Right", AutoProgram.RocketMiddlePathRight);
		autoChooser.addOption("Right Cargo", AutoProgram.RocketRightCargo);
		autoChooser.addOption("Left Cargo", AutoProgram.RocketLeftCargo);
		autoChooser.addOption("PathFinder Test", AutoProgram.TestPathFinder);
		
		
		SmartDashboard.putData(autoChooser);
	}

	// Launch autonomous mode execution.

	public void execute()
	{
		try
		{
			program = autoChooser.getSelected();
		}
		catch (Exception e)	{ Util.logException(e); }
		
		Util.consoleLog("Alliance=%s, Location=%d, Program=%s, FMS=%b, msg=%s", robot.alliance.name(), robot.location, program.name(), 
				Devices.ds.isFMSAttached(), robot.gameMessage);
		LCD.printLine(2, "Alliance=%s, Location=%d, FMS=%b, Program=%s, msg=%s", robot.alliance.name(), robot.location, 
				Devices.ds.isFMSAttached(), program.name(), robot.gameMessage);
		
		Devices.robotDrive.setSafetyEnabled(false);

		// Initialize wheel encoders.
		Devices.leftEncoder.setStatusFramePeriod(100);
		Devices.rightEncoder.setStatusFramePeriod(100);
		Util.consoleLog("right encoder counts =%d, left encoder counts =%d", Devices.rightEncoder.get(), Devices.leftEncoder.get());
		Devices.rightEncoder.reset(2);
		Devices.leftEncoder.reset(170);
		Util.consoleLog("right encoder counts =%d, left encoder counts =%d", Devices.rightEncoder.get(), Devices.leftEncoder.get());
		
       // Set NavX yaw tracking to 0.
		Devices.navx.resetYaw();

		Devices.navx.getAHRS().resetDisplacement();
		
		// Set heading to initial angle (0 is robot pointed down the field) so
		// NavX class can track which way the robot is pointed during the match.
		Devices.navx.setHeading(0);
		
		// Target heading should be the same.
		Devices.navx.setTargetHeading(0);
		
		// Set Talon ramp rate for smooth acceleration from stop. Determine by observation.
		Devices.SetCANTalonRampRate(1.0);

		//Initialize LaunchPad to make a kill switch
		// lp = new LaunchPad(Devices.launchPad, LaunchPadControlIDs.BUTTON_RED, this);

		// if(lp.GetLatchedState(LaunchPadControlIDs.BUTTON_RED))
		
		//Finish initialization

		// Determine which auto program to run as indicated by driver station.
		switch (program)
		{
			case NoProgram:		// No auto program.
				//startTeleop();
				break;
			case RocketUpClose:
				switch (robot.alliance){
					case Red:
					pathSelector(AutoProgram.RocketUpClose, DriverStation.Alliance.Red);
					break;
					case Blue:
					pathSelector(AutoProgram.RocketUpClose, DriverStation.Alliance.Blue);
					break;
					case Invalid:
					Util.consoleLog("Could not find alliance color!");
					break;
				}
		
				break;

			case RocketUpMiddle:
			switch (robot.alliance){
					case Red:
					pathSelector(AutoProgram.RocketUpMiddle, DriverStation.Alliance.Red);
					break;
					case Blue:
					pathSelector(AutoProgram.RocketUpMiddle, DriverStation.Alliance.Blue);
					break;
					case Invalid:
					Util.consoleLog("Could not find alliance color!");
					break;
			}
				break;

			case RocketUpFar:
			switch (robot.alliance){
					case Red:
					pathSelector(AutoProgram.RocketUpFar, DriverStation.Alliance.Red);
					break;
					case Blue:
					pathSelector(AutoProgram.RocketUpFar, DriverStation.Alliance.Blue);
					break;
					case Invalid:
					Util.consoleLog("Could not find alliance color!");
					break;
			}
				break;
			
			case RocketDownClose:
			switch (robot.alliance){
					case Red:
					pathSelector(AutoProgram.RocketDownClose, DriverStation.Alliance.Red);
					break;
					case Blue:
					pathSelector(AutoProgram.RocketDownClose, DriverStation.Alliance.Blue);
					break;
					case Invalid:
					Util.consoleLog("Could not find alliance color!");
					break;
			}
				break;
			
			case RocketDownMiddle:
			switch (robot.alliance){
					case Red:
					pathSelector(AutoProgram.RocketDownMiddle, DriverStation.Alliance.Red);
					break;
					case Blue:
					pathSelector(AutoProgram.RocketDownMiddle, DriverStation.Alliance.Blue);
					break;
					case Invalid:
					Util.consoleLog("Could not find alliance color!");
					break;

			}
				break;
			case RocketDownFar:
			switch (robot.alliance){
					case Red:
					pathSelector(AutoProgram.RocketDownFar, DriverStation.Alliance.Red);
					break;
					case Blue:
					pathSelector(AutoProgram.RocketDownFar, DriverStation.Alliance.Blue);
					break;
					case Invalid:
					Util.consoleLog("Could not find alliance color!");
					break;
			}
				break;
			case RocketMiddlePathLeft:
			switch (robot.alliance){
					case Red:
					pathSelector(AutoProgram.RocketMiddlePathLeft, DriverStation.Alliance.Red);
					break;
					case Blue:
					pathSelector(AutoProgram.RocketMiddlePathLeft, DriverStation.Alliance.Blue);
					break;
					case Invalid:
					Util.consoleLog("Could not find alliance color!");
					break;
			}
				break;
			case RocketMiddlePathRight:
			switch (robot.alliance){
					case Red:
					pathSelector(AutoProgram.RocketMiddlePathRight, DriverStation.Alliance.Red);
					break;
					case Blue:
					pathSelector(AutoProgram.RocketMiddlePathRight, DriverStation.Alliance.Blue);
					break;
					case Invalid:
					Util.consoleLog("Could not find alliance color!");
					break;
			}
				break;
			case RocketRightCargo:
			switch (robot.alliance){
					case Red:
					pathSelector(AutoProgram.RocketRightCargo, DriverStation.Alliance.Red);
					break;
					case Blue:
					pathSelector(AutoProgram.RocketRightCargo, DriverStation.Alliance.Blue);
					break;
					case Invalid:
					Util.consoleLog("Could not find alliance color!");
					break;
			}
				break;
			case RocketLeftCargo:
			switch (robot.alliance){
					case Red:
					pathSelector(AutoProgram.RocketLeftCargo, DriverStation.Alliance.Red);
					break;
					case Blue:
					pathSelector(AutoProgram.RocketLeftCargo, DriverStation.Alliance.Blue);
					break;
					case Invalid:
					Util.consoleLog("Could not find alliance color!");
					break;
			}
			break;
			case TestPathFinder:
					testPathfinder();
				break;

		}
		
		// Update the robot heading indicator on the DS.

		// Next statement only used with labview DB.
		//SmartDashboard.putNumber("Gyro", Devices.navx.getHeadingInt());
		
		SmartDashboard.updateValues();
		
		Util.consoleLog("final heading=%.2f  R=%.2f", Devices.navx.getHeading(), Devices.navx.getHeadingR());
		
		Util.consoleLog("end");
	}

	/**
	 * Auto drive straight in set direction and power for specified encoder count. Stops
	 * with or without brakes on CAN bus drive system. Uses NavX yaw to drive straight.
	 * @param power Power applied, + is forward.
	 * @param encoderCounts Target encoder counts to move, always +.
	 * @param stop Stop stops motors at end of curve, dontStop leaves power on to flow into next move.
	 * @param brakes Brakes on or off.
	 * @param pid On is use PID to control movement, off is simple drive.
	 * @param heading Heading is measure steering yaw from last set navx target heading, angle is measure yaw
	 * from direction robot is pointing when driving starts.
	 * 
	 * Note: This routine is designed for tank drive and the P,I,D,steering gain values will likely need adjusting for each
	 * new drive base as gear ratios and wheel configuration may require different values to stop smoothly
	 * and accurately.
	 */

	 private void startTeleop(){
		// if(lp != null) lp.dispose();
		// lp = null;
		 Util.consoleLog("Starting Teleop");
		 robot.operatorControl();

	 }
	 private void testPathfinder(){
		 Pathfinder.setTrace(true);
		 Util.consoleLog("Pathfinder Trace =%b", Pathfinder.isTracing());
		double wheel_diameter = Util.inchesToMeters(5.8);
		double max_velocity = 1.86; //1.86 m/s was the actual velocity
		int encoder_counts = 4096;

		//File middleTrajectoryCSV = new File("TestPath.pf1.csv");
		File leftTrajectoryCSV = new File("/home/lvuser/output/PortablePath.right.pf1.csv");
		File rightTrajectoryCSV = new File("/home/lvuser/output/PortablePath.left.pf1.csv");
	
		Util.consoleLog("I made the CSV files into java.io files");

		//Trajectory path = Pathfinder.readFromCSV(middleTrajectoryCSV);
		Trajectory rightPath = Pathfinder.readFromCSV(rightTrajectoryCSV);
		Trajectory leftPath = Pathfinder.readFromCSV(leftTrajectoryCSV);

		
		
		Util.consoleLog("I read the files");
		EncoderFollower left = new EncoderFollower(leftPath, "left");
		EncoderFollower right = new EncoderFollower(rightPath, "right");

		
		left.configureEncoder(Devices.leftEncoder.get(), encoder_counts, wheel_diameter);
		right.configureEncoder(Devices.rightEncoder.get(), encoder_counts, wheel_diameter);

		//NOTE TO SELF, FIND MAX VELOCITY
		left.configurePIDVA(0.5, 0.0, 0.0, 1/max_velocity, 0.0);
		right.configurePIDVA(0.5, 0.0, 0.0, 1/max_velocity, 0.0);
		

		//Initialize segment tracker variable
		int SegCount = 0;
		double totalTime = 0;
		Util.consoleLog("I reset the total time");
		Timer.delay(0.01);
		double elapsedTime = 0;
		double elapsedSegmentTime = 0;
		double totalSegmentTime = 0;
		double averageElapsedTime = 0;

		//This is the delay I am setting
		double delay = 0;
		Util.getElaspedTime();
		while(isAutoActive() && (SegCount < leftPath.length())){

			//Keeping Track of Segments
			elapsedTime = Util.getElaspedTime();
			totalTime += elapsedTime;

			elapsedSegmentTime = leftPath.get(SegCount).dt;
			totalSegmentTime += elapsedSegmentTime;
			
			double leftSpeed = left.calculate(Devices.leftEncoder.get(), SegCount);
			double rightSpeed = right.calculate(Devices.rightEncoder.get(), SegCount);

			Util.consoleLog("lp= %.4f rp=%.4f SegCount=%d", leftSpeed, rightSpeed, SegCount);
			
			double gyro_heading = Devices.navx.getHeading();
			//NOTE TO SELF, getHeadingR RETURNS THE HEADING IN RADIANS DESPITE THE DOCUMENTATION
			double segment_heading = Pathfinder.r2d(left.getHeading());

			double angleDifference = Pathfinder.boundHalfDegrees(segment_heading - gyro_heading);

			double turn = 0.8 *(1.0/80.0) *angleDifference;

			leftSpeed = Util.clampValue(leftSpeed + turn, 1);
			rightSpeed = Util.clampValue(rightSpeed - turn, 1);

			Util.consoleLog("le=%.4f lp=%.2f  re==%.4f rp=%.2f  dhdg=%.0f  hdg=%.0f ad=%.2f  turn=%.5f  time=%.3f totalelaspedtime = %.3f segmentTime = %.3f totalSegmentTime = %.3f", 
							Util.inchesToMeters(Devices.leftEncoder.getDistance()), leftSpeed, Util.inchesToMeters(Devices.rightEncoder.getDistance()), rightSpeed, 
							segment_heading, gyro_heading, angleDifference, turn,  elapsedTime, totalTime, elapsedSegmentTime, totalSegmentTime);
			
			

			Devices.robotDrive.tankDrive(leftSpeed, rightSpeed);

			
			//Difference between the total time the loop has been running and the total time the segments have been running
			delay = totalSegmentTime - totalTime;
			//Set the delay to be a minimum of 0, can't set the delay to be a negative number
			if(delay < 0.00){
				delay = 0.00;
			}
			Util.consoleLog("delay =%.3f", delay);

			//Set the delay 
			Timer.delay(delay);

			//Increment the Segment the robot is on
			SegCount++;
		}
		
		if(isAutoActive()){
			Util.consoleLog("The Trajectory is Complete");
			averageElapsedTime = totalTime/SegCount;
			Util.consoleLog("averageTime =%.3f", averageElapsedTime);
			
		}
		Devices.robotDrive.stopMotor();
	 }

	 public enum AutoProgram{
		RocketUpClose,
		RocketUpMiddle,
		RocketUpFar,
		RocketDownClose,
		RocketDownMiddle,
		RocketDownFar,
		RocketMiddlePathLeft,
		RocketMiddlePathRight,
		NoProgram,
		RocketRightCargo,
		RocketLeftCargo,
		TestPathFinder
	}
	

	private void pathSelector(AutoProgram pathName,  DriverStation.Alliance allianceColor){
		String rightPathFile;
		String leftPathFile;

		
		//I REALIZED THAT THE DIRECTORIES ARE THE SAME FOR EVERY PATH
		//SO GUESS WHAT I AM ADDING ALL THE STRINGS TOGETHER INSTEAD TO SAVE ME SOME TIME
		
		rightPathFile = "/home/lvuser/output/" + allianceColor.toString() + pathName.toString()+ ".left.pf1.csv";
		leftPathFile = "/home/lvuser/output/" + allianceColor.toString() + pathName.toString() + ".right.pf1.csv";
		
		Util.consoleLog("Left Path chose =%s Right Path chose =%s", leftPathFile, rightPathFile);
		PathfinderAuto(rightPathFile, leftPathFile);	
	}

	private void PathfinderAuto(String rightPathFile, String leftPathFile){
		Pathfinder.setTrace(true);
		Util.consoleLog("Pathfinder Trace =%b", Pathfinder.isTracing());
	   double wheel_diameter = Util.inchesToMeters(5.8);
	   double max_velocity = 1.86; //1.86 m/s was the actual velocity
	   int encoder_counts = 4096;

	   //NOTE THIS ISN'T A MISTAKE, WE NEED TO INVERT THE PATHS DUE TO AN ERROR 
	   //IN PATHFINDER
	   File leftTrajectoryCSV = new File(leftPathFile);
	   File rightTrajectoryCSV = new File(rightPathFile);
   
	   Util.consoleLog("I made the CSV files into java.io files");

	   //Trajectory path = Pathfinder.readFromCSV(middleTrajectoryCSV);
	   Trajectory rightPath = Pathfinder.readFromCSV(rightTrajectoryCSV);
	   Trajectory leftPath = Pathfinder.readFromCSV(leftTrajectoryCSV);

	   
	   
	   Util.consoleLog("I read the files");
	   EncoderFollower left = new EncoderFollower(leftPath, "left");
	   EncoderFollower right = new EncoderFollower(rightPath, "right");

	   
	   left.configureEncoder(Devices.leftEncoder.get(), encoder_counts, wheel_diameter);
	   right.configureEncoder(Devices.rightEncoder.get(), encoder_counts, wheel_diameter);

	   //NOTE TO SELF, FIND MAX VELOCITY
	   left.configurePIDVA(0.5, 0.0, 0.0, 1/max_velocity, 0.0);
	   right.configurePIDVA(0.5, 0.0, 0.0, 1/max_velocity, 0.0);
	   

	   //Initialize segment tracker variable
	   int SegCount = 0;
	   double totalTime = 0;
	   Util.consoleLog("I reset the total time");
	   Timer.delay(0.01);
	   double elapsedTime = 0;
	   double totalSegmentTime = 0;
	   double averageElapsedTime = 0;
	   double elapsedSegmentTime = leftPath.get(SegCount).dt;
	   //This is the delay I am setting
	   double delay = 0;
	   Util.getElaspedTime();
	   while(isAutoActive() && (SegCount < leftPath.length())){

		   //Keeping Track of Segments
		   elapsedTime = Util.getElaspedTime();
		   totalTime += elapsedTime;

		   
		   totalSegmentTime += elapsedSegmentTime;
		   
		   double leftSpeed = left.calculate(Devices.leftEncoder.get(), SegCount);
		   double rightSpeed = right.calculate(Devices.rightEncoder.get(), SegCount);

		   Util.consoleLog("lp= %.4f rp=%.4f SegCount=%d", leftSpeed, rightSpeed, SegCount);
		   
		   double gyro_heading = Devices.navx.getHeading();
		   //NOTE TO SELF, getHeadingR RETURNS THE HEADING IN RADIANS DESPITE THE DOCUMENTATION
		   double segment_heading = Pathfinder.r2d(left.getHeading());

		   double angleDifference = Pathfinder.boundHalfDegrees(segment_heading - gyro_heading);

		   double turn = 0.8 *(1.0/80.0) *angleDifference;

		   leftSpeed = Util.clampValue(leftSpeed + turn, 1);
		   rightSpeed = Util.clampValue(rightSpeed - turn, 1);

		   Util.consoleLog("le=%.4f lp=%.2f  re==%.4f rp=%.2f  dhdg=%.0f  hdg=%.0f ad=%.2f  turn=%.5f  time=%.3f totalelaspedtime = %.3f segmentTime = %.3f totalSegmentTime = %.3f", 
						   Util.inchesToMeters(Devices.leftEncoder.getDistance()), leftSpeed, Util.inchesToMeters(Devices.rightEncoder.getDistance()), rightSpeed, 
						   segment_heading, gyro_heading, angleDifference, turn,  elapsedTime, totalTime, elapsedSegmentTime, totalSegmentTime);
		   
		   

		   Devices.robotDrive.tankDrive(leftSpeed, rightSpeed);

		   
		   //Difference between the total time the loop has been running and the total time the segments have been running
		   delay = totalSegmentTime - totalTime;
		   //Set the delay to be a minimum of 0, can't set the delay to be a negative number
		   if(delay < 0.00){
			   delay = 0.00;
		   }
		   Util.consoleLog("delay =%.3f", delay);

		   //Set the delay 
		   Timer.delay(delay);

		   //Increment the Segment the robot is on
		   SegCount++;
	   }
	   
	   if(isAutoActive()){
		   Util.consoleLog("The Trajectory is Complete");
		   averageElapsedTime = totalTime/SegCount;
		   Util.consoleLog("averageTime =%.3f", averageElapsedTime);
		   
	   }
	   Devices.robotDrive.stopMotor();

	   /*
	   VISION STUFF WILL BE HERE IN THE NEXT UPDATE (HOPEFULLY)!
	   THEN AFTERWARDS WE WILL PUT IN FUNCTIONS REGARDING THE MOVEMENT OF THE MOTORS
	   */
	 }
	private void autoDrive(double power, int encoderCounts, StopMotors stop, Brakes brakes, Pid pid, 
						    Heading heading)
	{
		double			yaw, kSteeringGain = .10, elapsedTime = 0;
		double			kP = .002, kI = 0.001, kD = 0.001;
		
		SynchronousPID	pidController = null;

		Util.consoleLog("pwr=%.2f  count=%d  stop=%s  brakes=%s  pid=%s  hdg=%s", power, encoderCounts, stop, brakes, 
						pid, heading);

		Util.checkRange(power, 1.0);
		
		if (encoderCounts <= 0) throw new IllegalArgumentException("Encoder counts < 1");
		
		if (brakes == Brakes.on)
			Devices.SetCANTalonBrakeMode(true);
		else
			Devices.SetCANTalonBrakeMode(false);
			
		Devices.rightEncoder.reset();
		
		if (robot.isClone) Timer.delay(0.3);
		
		// If not measuring yaw from current heading, reset yaw based on current direction robot is facing.
		
		if (heading == Heading.angle)
		{
			Util.consoleLog("before reset=%.2f  hdg=%.2f", Devices.navx.getYaw(), Devices.navx.getHeading());
			
			Devices.navx.resetYawWait(1, 500);
			
			Util.consoleLog("after reset2=%.2f  hdg=%.2f", Devices.navx.getYaw(), Devices.navx.getHeading());
		}
		
		// If using PID to control distance, configure the PID object.
		
		if (pid == Pid.on)
		{
			pidController = new SynchronousPID(kP, kI, kD);
			
			if (power < 0)
			{
				pidController.setSetpoint(-encoderCounts);
				pidController.setOutputRange(power, 0);
			}
			else
			{
				pidController.setSetpoint(encoderCounts);
				pidController.setOutputRange(0, power);
			}

			Util.getElaspedTime();
		}
		
		// Drive until we get there.
		
		while (isAutoActive() && Math.abs(getEncoderCounts()) < encoderCounts) 
		{
			LCD.printLine(4, "wheel encoder=%d", getEncoderCounts());

			// Use PID to determine the power applied. Should reduce power as we get close
			// to the target encoder value.
			
			if (pid == Pid.on)
			{
				elapsedTime = Util.getElaspedTime();
				
				pidController.calculate(getEncoderCounts(), elapsedTime);
				
				power = pidController.get();
				
				Util.consoleLog("error=%.2f  power2=%.2f  time=%f", pidController.getError(), power, elapsedTime);
			}

			// Yaw angle is negative if robot veering left, positive if veering right when going forward.
			
			if (heading == Heading.heading)
				yaw = Devices.navx.getHeadingYaw();
			else
				yaw = Devices.navx.getYaw();
			
			LCD.printLine(5, "yaw=%.2f", yaw);
			
			Util.consoleLog("yaw=%.2f  hdg=%.2f  rot=%.2f", yaw, Devices.navx.getHeading(), -yaw * kSteeringGain);
			
			// Note we invert sign on the angle because we want the robot to turn in the opposite
			// direction than it is currently going to correct it. So a + angle says robot is veering
			// right so we set the turn value to - because - is a turn left which corrects our right
			// drift. kSteeringGain controls how aggressively we turn to stay on course.
			
			Devices.robotDrive.curvatureDrive(power, Util.clampValue(-yaw * kSteeringGain, 1.0), false);
			
			Timer.delay(.010);
		}

		if (stop == StopMotors.stop) Devices.robotDrive.stopMotor();				
		
		Util.consoleLog("end: actual count=%d  error=%.3f  ena=%b  isa=%b", Math.abs(getEncoderCounts()), 
				(double) Math.abs(Devices.rightEncoder.get()) / encoderCounts, robot.isEnabled(), robot.isAutonomous());
	}
	
	/** 
	 *Average left and right encoder counts to see how far robot has moved.
	 * @return Average encoder counts.
	 */
	private  int getEncoderCounts()
	{
		return (Devices.leftEncoder.get() + Devices.rightEncoder.get()) / 2;
	}
	
	/**
	 * Auto rotate the specified target angle from where the robot is currently pointing or rotate
	 * to a target heading.
	 * @param power Max power for rotation. Power is always +.
	 * @param target Target angle (-left, +right) to rotate from robot current direction -180..+180, or 
	 * target heading (0..359) to rotate to from robot current heading. Target heading is always + and cannot be more 
	 * than 180 degrees away from current heading.
	 * @param usePid False for simple rotation, true use PID controller to manage the rotation slowing rotation as
	 * target is reached.
	 * @param useHeading False target is an angle, true target is a heading.
	 * 
	 * Note: This routine is designed for tank drive and the P,I,D,tolerance values will likely need adjusting for each
	 * new drive base as gear ratios and wheel configuration may require different values to turn smoothly
	 * and accurately.
	 */
	private void autoRotate(double power, double target, boolean usePid, boolean useHeading)
	{
		double	kP = .02, kI = 0.003, kD = 0.001, kTolerance = 1.0;
		double	elapsedTime, yaw = 0;
		
		SynchronousPID	pid = null;

		Util.consoleLog("pwr=%.2f  target=%.2f  pid=%b  hdg=%b", power, target, usePid, useHeading);
		
		if (power <= 0) throw new IllegalArgumentException("power");
		
		// Try to prevent over rotation.
		Devices.SetCANTalonBrakeMode(true);

		// Reset yaw to current robot direction or target heading.
		
		if (useHeading) 
		{
			Util.checkRange(target, 0, 359, "target");
			
			Devices.navx.setTargetHeading(target);
		}
		else
		{
			Util.checkRange(target, 180, "target");
			
			Devices.navx.resetYawWait(1, 500);
		}
		
		if (usePid)
		{
			// Use PID to control power as we turn slowing as we approach target heading.
			
			pid = new SynchronousPID(kP, kI, kD);
			
			pid.setOutputRange(-power , power);
			
			if (useHeading)
				pid.setSetpoint(0);			// We are trying to get the yaw to zero.
			else
				pid.setSetpoint(target);	// We are trying to get to the target yaw.
			
			// The PID class needs delta time between calls to calculate the I term.
			
			Util.getElaspedTime();
			
			while (isAutoActive() && !pid.onTarget(kTolerance)) 
			{
				if (useHeading)
					yaw = Devices.navx.getHeadingYaw();
				else
					yaw = Devices.navx.getYaw();
				
				elapsedTime = Util.getElaspedTime();
				
				// Our target is zero yaw so we determine the difference between
				// current yaw and target and perform the PID calculation which
				// results in the speed of turn, reducing power as the difference
				// approaches zero. So our turn should slow and not overshoot. If
				// it does, the PID controller will reverse power and turn it back.
				// This continues until the error is within tolerance.
				
				pid.calculate(yaw, elapsedTime);
				
				power = pid.get();
				
				// When quick turn is true, first parameter is not used, power is fed to the
				// rate of turn parameter. PID controller takes care of the sign, that 
				// is the left/right direction of the turn.
				
				Devices.robotDrive.curvatureDrive(0, power, true);
				
				Util.consoleLog("power=%.2f  hdg=%.2f  yaw=%.2f  err=%.2f  time=%f  ena=%b", power, Devices.navx.getHeading(), 
						yaw, pid.getError(), elapsedTime, robot.isEnabled());
				
				Timer.delay(.010);
			} 
		}
		else
		{
			// Simple turn, full power until target reached.
			
			if (useHeading)
			{
				yaw = Devices.navx.getHeadingYaw();

				if (yaw > 0) power = power * -1;
				
				while (isAutoActive() && !Util.checkRange(yaw, 1.0)) 
				{
					Devices.robotDrive.curvatureDrive(0, power, true);
					
					Util.consoleLog("yaw=%.2f  hdg=%.2f", yaw, Devices.navx.getHeading());
					
					Timer.delay(.010);
					
					yaw = Devices.navx.getHeadingYaw();
				}
			}
			else
			{
				yaw = Devices.navx.getYaw();
			
				if (target < 0) power = power * -1;
			
				while (isAutoActive() && Math.abs(yaw) < Math.abs(target)) 
				{
					Devices.robotDrive.curvatureDrive(0, power, true);
					
					Util.consoleLog("yaw=%.2f  hdg=%.2f", yaw, Devices.navx.getHeading());
					
					Timer.delay(.010);
					
					yaw = Devices.navx.getYaw();
				}
			}
		}
		
		Util.consoleLog("end loop  hdg=%.2f  yaw=%.2f", Devices.navx.getHeading(), yaw);
		
		// Stop rotation.
		Devices.robotDrive.stopMotor();

		Util.consoleLog("after stop  hdg=%.2f  yaw=%.2f", Devices.navx.getHeading(), yaw);

		// Wait for robot to stop moving.
		Util.consoleLog("moving=%b", Devices.navx.isRotating());
		//while (isAutoActive() && Devices.navx.isRotating()) {Timer.delay(.10);}
		//Util.consoleLog("moving=%b", Devices.navx.isRotating());
		
		Util.consoleLog("2  hdg=%.2f  yaw=%.2f  ena=%b", Devices.navx.getHeading(), yaw, robot.isEnabled());
	}
	
	/**
	 * Automatically drive in a curve.
	 * @param power Speed to drive, + is forward.
	 * @param curve Speed of rotation 0..1.0, always +.
	 * @param target Target angle to turn. If not using heading, this is 0..180, - left, + right. If using heading
	 * this is the target heading 0..359.
	 * @param stop Stop stops motors at end of curve, dontStop leaves power on to flow into next move.
	 * @param brakes On turns on brakes for end of curve, off turns them off.
	 * @param pid On uses PID controller to manage the curve slowing rotation as target is reached. Off
	 * uses the fixed curve value for whole rotation.
	 * @param heading Heading: target is a heading, angle: target is an angle from current direction.
	 * 
	 * Note: This routine is designed for tank drive and the P,I,D,tolerance values will likely need adjusting for each
	 * new drive base as gear ratios and wheel configuration may require different values to stop smoothly
	 * and accurately.
	 */
	private void autoCurve(double power, double curve, double target, StopMotors stop, Brakes brakes, Pid pid, 
						   Heading heading)
	{
		double	kP = .04, kI = 0.003, kD = 0.001, kTolerance= 1.0;
		double	elapsedTime, yaw = 0, originalCurve, power2;
		
		SynchronousPID	pidController = null;

		Util.consoleLog("pwr=%.2f  curve=%.2f  target=%.2f  stop=%s  brakes=%s  pid=%s  hdg=%s  kP=%.3f  kI=%.3f", 
						power, curve, target, stop, brakes, pid, heading, kP, kI);
		
		Util.checkRange(power, 1.0, "power");
		
		Util.checkRange(curve, 0, 1.0, "curve");
		
		if (brakes == Brakes.on)
			Devices.SetCANTalonBrakeMode(true);
		else
			Devices.SetCANTalonBrakeMode(false);

		// Reset yaw to current robot direction or target heading.
		
		if (heading == Heading.heading) 
		{
			Util.checkRange(target, 0, 359.999, "target");
			
			Devices.navx.setTargetHeading(target);
		}
		else
		{
			Util.checkRange(target, 180, "target");
			
			Devices.navx.resetYawWait(1, 500);
		}
		
		if (pid == Pid.on)
		{
			// Use PID to control power as we turn slowing as we approach target heading.
			
			pidController = new SynchronousPID(kP, kI, kD);
			
			pidController.setOutputRange(-Math.abs(curve) , Math.abs(curve));
			
			originalCurve = curve;
			
			if (heading == Heading.heading)
				pidController.setSetpoint(0);		// We are trying to get the yaw to zero.
			else
				pidController.setSetpoint(target);	// We are trying to get to the target yaw.
			
			// The PID class needs delta time between calls to calculate the I term.
			
			Util.getElaspedTime();
			
			while (isAutoActive() && !pidController.onTarget(kTolerance)) 
			{
				elapsedTime = Util.getElaspedTime();
				
				if (heading == Heading.heading)
					yaw = Devices.navx.getHeadingYaw();
				else
					yaw = Devices.navx.getYaw();

				// Our target is zero yaw so we determine the difference between
				// current yaw and target and perform the PID calculation which
				// results in the speed (curve) of turn, reducing curve as the difference
				// approaches zero. So our turn should slow and not overshoot. If
				// it does, the PID controller will reverse curve and turn it back.
				
				pidController.calculate(yaw, elapsedTime);
				
				curve = pidController.get();
				
				// When quick turn is false, power is constant, curve is fed to the
				// rate of turn parameter. PID controller takes care of the sign, that 
				// is the left/right direction of the turn. If stopping at end of curve
				// we use the PID calculated curve (which is getting smaller) to also
				// reduce motor power for smooth stop.
				
				if (stop == StopMotors.stop)
					power2 = power * Math.abs(curve / originalCurve);
				else
					power2 = power;
				
				Devices.robotDrive.curvatureDrive(power2, curve, false);
				
				Util.consoleLog("power=%.2f  hdg=%.2f  yaw=%.2f  curve=%.2f  err=%.2f  time=%f  ena=%b", power2, Devices.navx.getHeading(), 
						yaw, curve, pidController.getError(), elapsedTime, robot.isEnabled());
				
				Timer.delay(.010);
			} 
		}
		else
		{
			// Simple turn, full curve until target reached.
			
			if (heading == Heading.heading)
			{
				yaw = Devices.navx.getHeadingYaw();

				if (yaw > 0) curve = curve * -1;
				
				while (isAutoActive() && !Util.checkRange(yaw, 1.0)) 
				{
					Devices.robotDrive.curvatureDrive(power, curve, false);
					
					Util.consoleLog("yaw=%.2f  hdg=%.2f  curve=%.2f", yaw, Devices.navx.getHeading(), curve);
					
					Timer.delay(.010);
					
					yaw = Devices.navx.getHeadingYaw();
				}
			}
			else
			{
				yaw = Devices.navx.getYaw();
			
				if (target < 0) curve = curve * -1;
						
				while (isAutoActive() && Math.abs(yaw) < Math.abs(target)) 
				{
					Devices.robotDrive.curvatureDrive(power, curve, false);
					
					Util.consoleLog("yaw=%.2f  hdg=%.2f  curve=%.2f", yaw, Devices.navx.getHeading(), curve);
					
					Timer.delay(.010);
					
					yaw = Devices.navx.getYaw();
				}
			}
		}
		
		Util.consoleLog("end loop  hdg=%.2f  yaw=%.2f", Devices.navx.getHeading(), yaw);
		
		// Stop motors. If you don't use stop, motors will keep running.
		if (stop == StopMotors.stop) Devices.robotDrive.stopMotor();

		Util.consoleLog("after stop  hdg=%.2f  yaw=%.2f", Devices.navx.getHeading(), yaw);

		// Wait for robot to stop moving.
		Util.consoleLog("moving=%b", Devices.navx.isRotating());
		//while (isAutoActive() && Devices.navx.isRotating()) {Timer.delay(.10);}
		//Util.consoleLog("moving=%b", Devices.navx.isRotating());
		
		Util.consoleLog("end hdg=%.2f  yaw=%.2f  ena=%b", Devices.navx.getHeading(), yaw, robot.isEnabled());
	}
	
	/**
	 * Drive in S curve, curve one direction, drive straight, curve back to starting heading.
	 * @param power Speed to drive + is forward.
	 * @param curve Rate of rotation (0..1.0), always +. PID controller will turn left/right
	 * as needed to reach target heading.
	 * @param target Heading to turn to 0..359.
	 * @param straightEncoderCounts Length of straight led between curves.
	 */
	private void autoSCurve(double power, double curve, double target, int straightEncoderCounts)
	{
		double	saveHeading = Devices.navx.getHeading();
		
		Util.consoleLog("pwr=%.2f  curve=%.2f  target=%.2f  counts=%d", power, curve, target, straightEncoderCounts);
		
		Devices.SetCANTalonRampRate(0.5);	

		// We start out driving in a curve until we have turned to the desired heading.
		// Then we drive straight the desired distance then curve back to starting
		// heading. PID controller in autoCurve will determine the sign of curve.
		
		autoCurve(power, curve, target, StopMotors.dontStop, Brakes.off, Pid.on, Heading.heading);
		
		autoDrive(power, straightEncoderCounts, StopMotors.dontStop, Brakes.off, Pid.off, Heading.heading);
		
		autoCurve(power, curve, saveHeading, StopMotors.stop, Brakes.on, Pid.on, Heading.heading);
	}
	
	//Enums section for this class
	private enum Brakes
	{
		off,
		on
	}
	
	private enum Pid
	{
		off,
		on
	}
	
	private enum Heading
	{
		angle,
		heading
	}
	
	private enum StopMotors
	{
		dontStop,
		stop
	}

		
}
