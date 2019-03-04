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
 * THIS CLASS IS FOR THE EXTENSION OF THE PISTON TO RAISE THE BACK OF THE ROBOT OFF 
 * THE GROUND FOR ENDGAME. 
 * See docs below for more info
 */

public class BackLift {

    Robot robot;
    public boolean isExtended = false;
    public boolean isRetracted = true;

    public static BackLift backLift = null;

    public static BackLift getInstance(Robot robot){
        if(backLift == null){
            backLift = new BackLift(robot);
        }
        return backLift;
    }

    public void dispose(){
        Util.consoleLog();
        backLift = null;
    }
    //REMEMBER TO SET THE BOOLEAN VALUES IN THE CONSTRUCTOR OTHERWISE FACE ERRORS
     private BackLift(Robot robot){
        this.robot = robot;
        Retract();
        Util.consoleLog("SHould retract");
       Devices.unusedValve.SetB();
        Util.consoleLog("Set unused to valve b");
        Util.consoleLog("The rear climb pistons have been created");
        
     }
    //CHANGE THE CONSTRUCTOR AS NEEDED, LOOK AT MATCH SPECIFICATIONS TO SEE IF WE NEED TO RETRACT OR EXTEND

    public void Display(){
        Util.consoleLog("BackLiftExtended =%b BackLiftRetracted =%b", isExtended, isRetracted);
        SmartDashboard.putBoolean("Back Lift Extended", isExtended);
        SmartDashboard.putBoolean("Back Lift Retracted", isRetracted);
    }
    public void Extend(){
        
        //Should extend the pistons
            Devices.rearClimbValve.SetA();
            Util.consoleLog("Extended");
            isExtended = true;
            isRetracted = false;
        
        Display();
    }

    
    public void Retract(){
        
        //Should retract the robot
            Devices.rearClimbValve.SetB();
            Util.consoleLog("Retracted");
            isExtended = false;
            isRetracted = true;
        
        
        Display();

    }
}

