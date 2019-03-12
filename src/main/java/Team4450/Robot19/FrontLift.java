/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package Team4450.Robot19;

import Team4450.Lib.Util;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *THIS CLASS IS FOR THE EXTENSION OF THE PISTONS
    TO RAISE THE ROBOT OFF THE GROUND FOR ENDGAME 
 */

public class FrontLift {

    
    public boolean isExtended = false;
    public boolean isRetracted = true;
    Robot robot;

    public static FrontLift frontLift = null;

    
    public static FrontLift getInstance(Robot robot){
        if(frontLift == null){
            frontLift = new FrontLift(robot);
        }
        return frontLift;
    }
    
    private FrontLift(Robot robot){
        Util.consoleLog(); 
        this.robot = robot;
        Retract();
        Util.consoleLog("The front climb pistons have been created");
    }
    public void dispose(){
        Util.consoleLog();
        Retract();
        frontLift = null;
    }

    public void Display(){
        Util.consoleLog("IsExtended =%b IsRetracted =%b", isExtended, isRetracted);
        SmartDashboard.putBoolean("Extended", isExtended);
        SmartDashboard.putBoolean("Retracted", isRetracted);
    }
    public void Extend(){   
        
        Devices.frontClimbValve.SetA();
        isExtended = true;
        isRetracted = false;
        Display();
        
    }
    public void Retract(){
        
        Devices.frontClimbValve.SetB();
        isExtended = false;
        isRetracted = true;
        Display();
        
    }
    
    
}
