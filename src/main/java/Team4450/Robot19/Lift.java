/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package Team4450.Robot19;

import Team4450.Lib.Util;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Add your docs here.
 */
public class Lift {

    Robot robot;
    private boolean isMoving = true;
    private boolean				holdingPosition, holdingHeight;
   // private final PIDController liftPidController;

    private int MAXENCODERCOUNTS = 10800;
    public static Lift lift = null;

    public static Lift getInstance(Robot robot){
        if (lift == null) lift = new Lift(robot);
        return lift;
    }

    private Lift(Robot robot){
        Util.consoleLog();
        this.robot = robot;
       //liftPidController = new PIDController(0.0, 0.0, 0.0, Devices.winchEncoder, Devices.winchDrive);
        Devices.winchEncoder.reset();
        updateDS();
        Util.consoleLog("Lift Created!");
    }
    public void dispose(){
        Util.consoleLog();

       // liftPidController.disable();
    	//liftPidController.close();

        lift = null;
    }

    public void updateDS(){
        SmartDashboard.putBoolean("TargetLocked", holdingHeight);
    }
    public void setWinchPower(double power)
	{
	/*	if (isHoldingHeight()) return;
		
		if (Devices.winchEncoderEnabled)
		{
		//	limit switch and hall effect sensor read in reverse so two sets of code.
			
				// limit switch form.
				if ((power > 0 && Devices.winchEncoder.get() < 14000) ||	// 10800
					(power < 0 && !Devices.winchSwitch.get()))
					Devices.winchDrive.set(power);
				else
				{
					if (Devices.winchSwitch.get()) Devices.winchEncoder.reset();
					
					Devices.winchDrive.set(0);
				}
		}
		else
			Devices.winchDrive.set(power);*/
			Devices.winchDrive.set(power);
	 }
    // public void setHeight(int count)
	// {
	// 	Util.consoleLog("%d", count);
		
	// 	if (count >= 0)
	// 	{
	// 		if (isHoldingPosition()) holdPosition(0);
			
	// // 		// p,i,d values are a guess.
	// // 		// f value is the motor power to apply to move to encoder target count.
	// // 		// Setpoint is the target encoder count.
	// // 		// The idea is that the difference between the current encoder count and the
	// // 		// target count will apply power to bring the two counts together and stay there.
	// 		liftPidController.setPID(0.0003, 0.00001, 0.0003, 0.0);
	// 		liftPidController.setPID(0.0003, 0.0, 0.0, 0.0);
	// 		liftPidController.setOutputRange(-1, 1);
	// 		liftPidController.setSetpoint(count);
	// 		liftPidController.setPercentTolerance(1);	// % error.
	// 		liftPidController.enable();
	// 		holdingHeight = true;
	// 	}
	// 	else
	// 	{
	// 		liftPidController.disable();
	// 		holdingHeight = false;
	// 	}
		
	// 	updateDS();
	// }
	
	// public boolean isHoldingHeight()
	// {
	// 	return holdingHeight;
	// }
	
	// public boolean isHoldingPosition()
	// {
	// 	return holdingPosition;
	// }
	
	// // Automatically hold lift position at specified power level. zero disables.
	
	// void holdPosition(double speed)
	// {
	// 	Util.consoleLog("%f", speed);
		
	// 	if (speed != 0)
	// 	{
	// 		if (isHoldingHeight()) setHeight(-1);
			
	// // 		// p,i,d values are a guess.
	// // 		// f value is the base motor speed, which is where (power) we want to hold position.
	// // 		// Setpoint is current encoder count.
	// // 		// The idea is that any encoder motion will alter motor base speed to hold position.
	// 		liftPidController.setPID(0.0003, 0.00001, 0.0003, speed);
	// 		liftPidController.setSetpoint(Devices.winchEncoder.get());
	// 		liftPidController.setPercentTolerance(1);	// % error.
	// 		liftPidController.enable();
	// 		holdingPosition = true;
	// 	}
	// 	else
	// 	{
	// 		liftPidController.disable();
	// 		holdingPosition = false;
	// 	}
	// }
}
