/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package Team4450.Robot19;

import Team4450.Lib.Util;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Add your docs here.
 */
public class HatchScoring {

    Robot robot; 
    //Make sure you have the right modifier... they can be globally accessed right now
    private boolean isExtended;
    private boolean isRetracted;
    private boolean				holdingPosition;
    private boolean             holdingHeight = false;
    private boolean hatchHoldingHeight = false;
    private boolean isMoving = false;

    private final PIDController hatchPidController;
    public static HatchScoring hatchScoring = null;

    public static HatchScoring getInstance(Robot robot){
        if(hatchScoring == null){
            hatchScoring = new HatchScoring(robot);
        }
        return hatchScoring;
    }

    public void dispose(){
        Util.consoleLog();
        HatchKickIn();
        // Devices.hatchEncoder.reset();
        hatchPidController.disable();
        hatchPidController.close();

        hatchScoring = null;
    }
    private HatchScoring(Robot robot){
        this.robot = robot;
        hatchPidController = new PIDController(0.0, 0.0, 0.0, Devices.hatchEncoder, Devices.hatchWinch);
        // isExtended = true;
        // isRetracted = false;
        
        //HatchKickIn();

        //setHeight(-1400);
        Devices.hatchEncoder.reset();
        Util.consoleLog("The Hatch Has been created");
    }
    //AGAIN DOUBLE CHECK FOR THIS ONE IF IT WILL BE SUITABLE TO KICK IN THE HATCH DURING TELEOP AND AUTO OR IF WE NEED IT

    public void Display(){
        Util.consoleLog("IstheHatchOut =%b IstheHatchIn =%b", isExtended, isRetracted);
        SmartDashboard.putBoolean("HatchExtended", isExtended);
        SmartDashboard.putBoolean("HatchRetracted", isRetracted);

        //COME UP WITH A BETTER NAME...
        SmartDashboard.putBoolean("Hatch Set Position", hatchHoldingHeight);
    }
    public void testHatchMotor(double power){
        Devices.hatchWinch.set(power);
        isMoving = true;
    }
    public void stopHatch(){
        Devices.hatchWinch.set(0);
        isMoving = false;
    }
    public boolean isMoving(){
        return isMoving;
    }
    public void HatchKickOut(){
            Devices.hatchKickValve.Open();
            isExtended = true;
            isRetracted = false;
        
        
        Display();
        
    }
    public void HatchKickIn(){

        
            Devices.hatchKickValve.Close();
            isExtended = false;
            isRetracted = true;

        Display();

    }
    public boolean isExtended(){
        return isExtended;
    }
    //CHANGE THIS UP COMPLETELY
    public void setHeight(int count)
	{
		Util.consoleLog("%d", count);
		
		if (count >= 0)
		{			
			// p,i,d values are a guess.
			// f value is the motor power to apply to move to encoder target count.
			// Setpoint is the target encoder count.
			// The idea is that the difference between the current encoder count and the
			// target count will apply power to bring the two counts together and stay there.
			hatchPidController.setPID(0.0003, 0.00001, 0.0003, 0.0);
			//liftPidController.setPID(0.0003, 0.0, 0.0, 0.0);
			hatchPidController.setOutputRange(-1, 1);
			hatchPidController.setSetpoint(count);
			hatchPidController.setPercentTolerance(1);	// % error.
			hatchPidController.enable();
			hatchHoldingHeight = true;
		}
		else
		{
			hatchPidController.disable();
			hatchHoldingHeight = false;
		}
		
		 Display();
    }
    
    public boolean hatchHoldingHeight(){
        return hatchHoldingHeight;
    }
}
