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
public class BackLift {

    Robot robot;
    public boolean isExtended;
    public boolean isRetracted;

    //REMEMBER TO SET THE BOOLEAN VALUES IN THE CONSTRUCTOR OTHERWISE FACE ERRORS
     public BackLift(Robot robot){
    //     this.robot = robot;
    //     isExtended = true;
    //     isRetracted = false;
    //     Retract();
        
     }
    //CHANGE THE CONSTRUCTOR AS NEEDED, LOOK AT MATCH SPECIFICATIONS TO SEE IF WE NEED TO RETRACT OR EXTEND

    public void Display(){
        Util.consoleLog("BackLiftExtended =%b BackLiftRetracted =%b", isExtended, isRetracted);
        SmartDashboard.putBoolean("Back Lift Extended", isExtended);
        SmartDashboard.putBoolean("Back Lift Retracted", isRetracted);
    }
    public void Extend(){
        if(isRetracted){
            Devices.rearLiftValve.SetA();
            isExtended = true;
            isRetracted = false;
        }
        else Util.consoleLog("The back lift is already extended");
    }

    public void Retract(){
        if(isExtended){
            Devices.rearLiftValve.SetB();
            isExtended = true;
            isRetracted = false;
        }
        else Util.consoleLog("The back lift is already retracted");

    }
}

