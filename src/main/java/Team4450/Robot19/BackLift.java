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
 * Add your docs here.
 */

//THIS CLASS IS FOR THE EXTENSION OF THE PISTONS TO RAISE THE ROBOT OFF THE GROUND FOR ENDGAME 
public class BackLift {

    Robot robot;
    public boolean isExtended;
    public boolean isRetracted;

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
        isExtended = false;
        isRetracted = true;
        /*Retract();*/
        Util.consoleLog("The rear climb pistons have been created");
        
     }
    //CHANGE THE CONSTRUCTOR AS NEEDED, LOOK AT MATCH SPECIFICATIONS TO SEE IF WE NEED TO RETRACT OR EXTEND

    public void Display(){
        Util.consoleLog("BackLiftExtended =%b BackLiftRetracted =%b", isExtended, isRetracted);
        SmartDashboard.putBoolean("Back Lift Extended", isExtended);
        SmartDashboard.putBoolean("Back Lift Retracted", isRetracted);
    }
    public void Extend(){
        
          //  Devices.rearClimbValve.SetA();
            isExtended = true;
            isRetracted = false;
        
        Display();
    }

    public void Retract(){
        
          //  Devices.rearClimbValve.SetB();
            isExtended = false;
            isRetracted = true;
        
        
        Display();

    }
}

