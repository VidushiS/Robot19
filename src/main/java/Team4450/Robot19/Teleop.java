
package Team4450.Robot19;

import java.lang.Math;

import Team4450.Lib.*;
import Team4450.Lib.JoyStick.*;
import Team4450.Lib.LaunchPad.*;
import Team4450.Robot19.Devices;
import Team4450.Robot19.BackLift;
import Team4450.Robot19.FrontLift;
import Team4450.Robot19.IntakeAndSpit;
import Team4450.Robot19.Lift;
import Team4450.Robot19.HatchScoring;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

class Teleop
{
	private final Robot 		robot;
	public  JoyStick			rightStick, leftStick, utilityStick;
	public  LaunchPad			launchPad;
	private boolean				autoTarget, altDriveMode;
	private Vision				vision;
	private GearBox				gearBox;
	private BackLift			backLift;
	private FrontLift			frontLift;
	private IntakeAndSpit		intakeSpit;
	private Lift				lift;
	private HatchScoring		hatch;
	// Constructor.

	Teleop(Robot robot)
	{
		Util.consoleLog();
		
		// Motor safety turned off during initialization.
		Devices.robotDrive.setSafetyEnabled(false);

		this.robot = robot;

		gearBox = new  GearBox(robot);
		backLift = BackLift.getInstance(robot);
		frontLift = FrontLift.getInstance(robot);
		intakeSpit = IntakeAndSpit.getInstance(robot);
		lift = Lift.getInstance(robot);
		hatch = HatchScoring.getInstance(robot);
		
		vision = Vision.getInstance(robot);
	}

	// Free all objects that need it.

	void dispose()
	{
		Util.consoleLog();

		if (leftStick != null) leftStick.dispose();
		if (rightStick != null) rightStick.dispose();
		if (utilityStick != null) utilityStick.dispose();
		if (launchPad != null) launchPad.dispose();
		if (gearBox != null) gearBox.dispose();
		if(intakeSpit != null) intakeSpit.dispose();
		if(hatch != null) hatch.dispose();
		if(backLift != null) backLift.dispose();
		if(frontLift != null) frontLift.dispose();
	}

	void OperatorControl() throws Exception
	{
		double	rightY = 0, leftY = 0, utilX = 0, utilY = 0, rightX = 0, leftX = 0;
		double	gain = .05;
		boolean	steeringAssistMode = false;
		int		angle;

		// Motor safety turned off during initialization.
		Devices.robotDrive.setSafetyEnabled(false);

		Util.consoleLog();

		LCD.printLine(1, "Mode: OperatorControl");
		LCD.printLine(2, "All=%s, Start=%d, FMS=%b", robot.alliance.name(), robot.location, Devices.ds.isFMSAttached());
		
		// Set synchronousPID as a sendable for testing.
		SynchronousPID pidTest = new SynchronousPID(1,2,3,4);
		pidTest.setSetpoint(99);		
		SmartDashboard.putData(pidTest);

		// Configure LaunchPad and Joystick event handlers.

		launchPad = new LaunchPad(Devices.launchPad, LaunchPadControlIDs.BUTTON_RED, this);

		LaunchPadControl lpControl = launchPad.AddControl(LaunchPadControlIDs.ROCKER_LEFT_BACK);
		lpControl.controlType = LaunchPadControlTypes.SWITCH;

		lpControl = launchPad.AddControl(LaunchPadControlIDs.ROCKER_LEFT_FRONT);
		lpControl.controlType = LaunchPadControlTypes.SWITCH;

		//Example on how to track more buttons:
		launchPad.AddControl(LaunchPadControlIDs.BUTTON_GREEN);
		launchPad.AddControl(LaunchPadControlIDs.BUTTON_BLACK);
		launchPad.AddControl(LaunchPadControlIDs.BUTTON_RED_RIGHT);
		launchPad.AddControl(LaunchPadControlIDs.BUTTON_BLUE);
		launchPad.AddControl(LaunchPadControlIDs.BUTTON_BLUE_RIGHT);
		launchPad.AddControl(LaunchPadControlIDs.BUTTON_RED);
		launchPad.AddControl(LaunchPadControlIDs.BUTTON_YELLOW);
		launchPad.AddControl(LaunchPadControlIDs.ROCKER_LEFT_BACK);
		launchPad.AddControl(LaunchPadControlIDs.ROCKER_LEFT_FRONT);
		launchPad.AddControl(LaunchPadControlIDs.ROCKER_RIGHT);
		
		launchPad.addLaunchPadEventListener(new LaunchPadListener());
		launchPad.Start();

		leftStick = new JoyStick(Devices.leftStick, "LeftStick", JoyStickButtonIDs.TRIGGER, this);
		//Example on how to track button:
		//leftStick.AddButton(JoyStickButtonIDs.BUTTON_NAME_HERE);
		leftStick.AddButton(JoyStickButtonIDs.TRIGGER);
		leftStick.AddButton(JoyStickButtonIDs.TOP_RIGHT);
		leftStick.AddButton(JoyStickButtonIDs.TOP_MIDDLE);
		leftStick.AddButton(JoyStickButtonIDs.TOP_LEFT);
		leftStick.AddButton(JoyStickButtonIDs.TOP_RIGHT);
		leftStick.AddButton(JoyStickButtonIDs.TOP_BACK);
		leftStick.AddButton(JoyStickButtonIDs.RIGHT_REAR);
		leftStick.addJoyStickEventListener(new LeftStickListener());
		leftStick.Start();

		rightStick = new JoyStick(Devices.rightStick, "RightStick", JoyStickButtonIDs.TRIGGER, this);
		//Example on how to track button:
		//rightStick.AddButton(JoyStickButtonIDs.BUTTON_NAME_HERE);
		rightStick.AddButton(JoyStickButtonIDs.TRIGGER);
		rightStick.addJoyStickEventListener(new RightStickListener());
		rightStick.Start();

		utilityStick = new JoyStick(Devices.utilityStick, "UtilityStick", JoyStickButtonIDs.TRIGGER, this);
		//Example on how to track button:
		//utilityStick.AddButton(JoyStickButtonIDs.TOP_MIDDLE);
		utilityStick.AddButton(JoyStickButtonIDs.TRIGGER);
		utilityStick.AddButton(JoyStickButtonIDs.TOP_RIGHT);
		utilityStick.AddButton(JoyStickButtonIDs.TOP_MIDDLE);
		utilityStick.AddButton(JoyStickButtonIDs.TOP_LEFT);
		utilityStick.AddButton(JoyStickButtonIDs.TOP_RIGHT);
		utilityStick.AddButton(JoyStickButtonIDs.TOP_BACK);
		utilityStick.AddButton(JoyStickButtonIDs.RIGHT_REAR);
		utilityStick.addJoyStickEventListener(new UtilityStickListener());
		utilityStick.Start();

		// Invert driving joy sticks Y axis so + values mean forward.
		leftStick.invertY(true);
		rightStick.invertY(true);

		// Set CAN Talon brake mode by rocker switch setting.
		// We do this here so that the Utility stick thread has time to read the initial state
		// of the rocker switch. Depends on lpcontrol being the last control defined for the 
		// launch pad as the one that controls brake mode.
		//if (robot.isComp) Devices.SetCANTalonBrakeMode(lpControl.latchedState);
		
		// Post season testing showed Anakin liked this setting, smoothing driving.
		// He also asked for brakes off in low gear, brakes on in high. See GearBox.
		// It controls brake setting. This will need to be checked each year to see
		// if these settings are appropriate for each new  robot.
		Devices.SetCANTalonRampRate(0.5);
		
		// Set Navx current yaw to 0.
		Devices.navx.resetYaw();

		// Reset wheel encoders.
		Devices.leftEncoder.reset();
		Devices.rightEncoder.reset();
		Devices.leftEncoder.resetMaxRate();
		Devices.rightEncoder.resetMaxRate();
		
		// Motor safety turned on.
		Devices.robotDrive.setSafetyEnabled(true);

		// Driving loop runs until teleop is over.

		Util.consoleLog("enter driving loop");
		
		while (robot.isEnabled() && robot.isOperatorControl())
		{
			// Get joystick deflection and feed to robot drive object
			// using calls to our JoyStick class.
			double correctionFactor = 0.5;

			rightY = stickLogCorrection(rightStick.GetY());	// fwd/back
			leftY = stickLogCorrection(leftStick.GetY());	// fwd/back

			rightX = correctionFactor * stickLogCorrection(rightStick.GetX());	// left/right
			leftX = stickLogCorrection(leftStick.GetX());	// left/right

			utilY = utilityStick.GetY();
			lift.setWinchPower(utilY);
			
			LCD.printLine(3, "leftenc=%d  rightenc=%d hatchenc=%d winchenc=%d" , Devices.leftEncoder.get(), Devices.rightEncoder.get(), Devices.hatchEncoder.get(), Devices.winchEncoder.get());
			//LCD.printLine(3, "leftY=%.3f  rightY=%.3f  utilY=%.3f", leftStick.GetY(), rightStick.GetY(), utilY);
			LCD.printLine(4, "leftY=%.3f  rightY=%.3f  utilY=%.3f", leftY, rightY, utilY);
			LCD.printLine(5, "yaw=%.2f, total=%.2f, rate=%.2f, hdng=%.2f", Devices.navx.getYaw(), 
					Devices.navx.getTotalYaw(), Devices.navx.getYawRate(), Devices.navx.getHeading());
			LCD.printLine(10, "pressureV=%.2f  psi=%d", robot.monitorCompressorThread.getVoltage(), 
					robot.monitorCompressorThread.getPressure());

			// Set wheel motors.
			// Do not feed JS input to robotDrive if we are controlling the motors in automatic functions.

			//if (!autoTarget) robot.robotDrive.tankDrive(leftY, rightY);

			// Two drive modes, full tank and alternate. Switch on right stick trigger.

			// if (!autoTarget) 
			// {
			// 	if (altDriveMode)
			// 	{	// normal tank with straight drive assist when sticks within 10% of each other and
					// right stick power is greater than 50%.
					//NEW CODE FOR H-DRIVE RIGHT HERE
					//Devices.hDrive.set(speed);

					// if (isLeftRightEqual(leftY, rightY, 10) && Math.abs(rightY) > .50)
					// {
					// 	// Reset angle measurement when entering this code first time after mode is enabled.
					// 	if (!steeringAssistMode) Devices.navx.resetYaw();

					// 	// Angle is negative if robot veering left, positive if veering right when going forward.
					// 	// It is opposite when going backward.

					// 	angle = (int) Devices.navx.getYaw();

					// 	LCD.printLine(5, "angle=%d", angle);

					// 	// Invert angle for backwards movemment.

					// 	if (rightY < 0) angle = -angle;

					// 	//Util.consoleLog("angle=%d", angle);

					// 	// Note we invert sign on the angle because we want the robot to turn in the opposite
					// 	// direction than it is currently going to correct it. So a + angle says robot is veering
					// 	// right so we set the turn value to - because - is a turn left which corrects our right
					// 	// drift.
						
					// 	Devices.robotDrive.curvatureDrive(rightY, -angle * gain, false);
						

					// 	steeringAssistMode = true;
					// }
					// else
					// {
					// 	steeringAssistMode = false;
					// 	Devices.robotDrive.tankDrive(leftY, rightY);		// Normal tank drive.
					// }

				// 	SmartDashboard.putBoolean("SteeringAssist", steeringAssistMode);
				// }
				// else
				if(rightStick.GetCurrentState(JoyStickButtonIDs.TRIGGER)){
					Devices.hDrive.set(rightX);
				}
				else {Devices.hDrive.set(0);}

				Devices.robotDrive.tankDrive(leftY, rightY);		// Normal tank drive.
				
					// This shows how to use curvature drive mode, toggled by trigger (for testing).
					//Devices.robotDrive.curvatureDrive(rightY, rightX, rightStick.GetLatchedState(JoyStickButtonIDs.TRIGGER));
		//	}

			// Update the robot heading indicator on the DS. Only for labview DB.

			SmartDashboard.putNumber("Gyro", Devices.navx.getHeadingInt());
			
			// Cause smartdashboard to update any registered Sendables, Gyro2.
			
			SmartDashboard.updateValues();

			// End of driving loop.

			Timer.delay(.020);	// wait 20ms for update from driver station.
		//}

		// End of teleop mode.

		// ensure we start next time in low gear.
	}
		gearBox.lowSpeed();
		
		Util.consoleLog("end");
					
	}

	private boolean isLeftRightEqual(double left, double right, double percent)
	{
		if (Math.abs(left - right) <= (1 * (percent / 100))) return true;

		return false;
	}

	// Custom base logarithm.
	// Returns logarithm base of the value.

	private double baseLog(double base, double value)
	{
		return Math.log(value) / Math.log(base);
	}

	// Map joystick y value of 0.0 to 1.0 to the motor working power range of approx 0.5 to 1.0 using
	// logarithmic curve.

	private double stickLogCorrection(double joystickValue)
	{
		double base = Math.pow(2, 1/3) + Math.pow(2, 1/3);

		if (joystickValue > 0)
			joystickValue = baseLog(base, joystickValue + 1);
		else if (joystickValue < 0)
			joystickValue = -baseLog(base, -joystickValue + 1);

		return joystickValue;
	}

	// Handle LaunchPad control events.

	public class LaunchPadListener implements LaunchPadEventListener 
	{
		public void ButtonDown(LaunchPadEvent launchPadEvent) 
		{
			LaunchPadControl	control = launchPadEvent.control;

			Util.consoleLog("%s, latchedState=%b", control.id.name(),  control.latchedState);

			switch(control.id)
			{
				case BUTTON_RED:
					if (gearBox.isLowSpeed())
		    			gearBox.highSpeed();
		    		else
		    			gearBox.lowSpeed();
					
					break;
					
				case BUTTON_GREEN:
					Devices.leftEncoder.reset();
					Devices.rightEncoder.reset();
					break;

				case BUTTON_BLUE:
					if(!backLift.isExtended){
						backLift.Extend();
					}
					else backLift.Retract();
					break;
					
				case BUTTON_RED_RIGHT:
					if(!frontLift.isExtended){
						frontLift.Extend();
					}
					else frontLift.Retract();
					break;
				
					
				default:
					break;
			}
		}

		public void ButtonUp(LaunchPadEvent launchPadEvent) 
		{
			//Util.consoleLog("%s, latchedState=%b", launchPadEvent.control.name(),  launchPadEvent.control.latchedState);
		}

		public void SwitchChange(LaunchPadEvent launchPadEvent) 
		{
			LaunchPadControl	control = launchPadEvent.control;

			Util.consoleLog("%s", control.id.name());

			switch(control.id)
			{
				// Set CAN Talon brake mode.
	    		case ROCKER_LEFT_BACK:
    				if (Devices.isBrakeMode())
    					Devices.SetCANTalonBrakeMode(false);	// coast
    				else
    					Devices.SetCANTalonBrakeMode(true);		// brake
    				
    				break;
    				
	    		case ROCKER_LEFT_FRONT:
					if (robot.cameraThread != null) robot.cameraThread.ChangeCamera();
					//invertDrive = !invertDrive;
	    			break;
	    			
				default:
					break;
			}
		}
	}

	// Handle Right JoyStick Button events.

	private class RightStickListener implements JoyStickEventListener 
	{

		public void ButtonDown(JoyStickEvent joyStickEvent) 
		{
			JoyStickButton	button = joyStickEvent.button;

			Util.consoleLog("%s, latchedState=%b", button.id.name(),  button.latchedState);

			switch(button.id)
			{
				case TRIGGER:
					autoTarget = !autoTarget;
					break;

			//Example of Joystick Button case:
			/*
			case BUTTON_NAME_HERE:
				if (button.latchedState)
					DoOneThing();
				else
					DoOtherThing();
				break;
			 */
					
				default:
					break;
			}
		}

		public void ButtonUp(JoyStickEvent joyStickEvent) 
		{
			//Util.consoleLog("%s", joyStickEvent.button.name());
		}
	}

	// Handle Left JoyStick Button events.

	private class LeftStickListener implements JoyStickEventListener 
	{
		public void ButtonDown(JoyStickEvent joyStickEvent) 
		{
			JoyStickButton	button = joyStickEvent.button;

			Util.consoleLog("%s, latchedState=%b", button.id.name(),  button.latchedState);

			switch(button.id)
			{
				case TRIGGER:
					if (gearBox.isLowSpeed())
	    				gearBox.highSpeed();
	    			else
	    				gearBox.lowSpeed();

					break;
					
				default:
					break;
			}
		}

		public void ButtonUp(JoyStickEvent joyStickEvent) 
		{
			//Util.consoleLog("%s", joyStickEvent.button.name());
		}
	}

	// Handle Utility JoyStick Button events.

	private class UtilityStickListener implements JoyStickEventListener 
	{
		public void ButtonDown(JoyStickEvent joyStickEvent) 
		{
			JoyStickButton	button = joyStickEvent.button;

			Util.consoleLog("%s, latchedState=%b", button.id.name(),  button.latchedState);

			switch(button.id)
			{
				case TOP_LEFT:
					if (!intakeSpit.isIntaking()){
						intakeSpit.Intake(0.8);
					}
					else intakeSpit.StopIntake();
					break;

				case TOP_RIGHT:
					if(!intakeSpit.isSpitting()){
						intakeSpit.Spit(0.3);
					}
					else intakeSpit.StopSpit();
					break;

				case TRIGGER:
					if(!hatch.isMoving()){
						hatch.testHatchMotor(0.1);
					}
					else hatch.stopHatch();
					break;
				
				case TOP_MIDDLE:
					if(!intakeSpit.ISTHREADRUNNING()){
						intakeSpit.StartAutoIntake();
					}
					else intakeSpit.StopAutoIntake();
					break;

				default:
					break;
			}
		}

		public void ButtonUp(JoyStickEvent joyStickEvent) 
		{
			//Util.consoleLog("%s", joyStickEvent.button.id.name());
		}
	}
}
