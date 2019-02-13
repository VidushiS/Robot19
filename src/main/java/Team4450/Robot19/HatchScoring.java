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
public class HatchScoring {

    Robot robot; //Remember what this does again...
    //Make sure you have the right modifier... they can be globally accessed right now
    public boolean isExtended;
    public boolean isRetracted;

    // public HatchScoring(Robot robot){
    //     this.robot = robot;
    //     isExtended = true;
    //     isRetracted = false;
    //     HatchKickIn();
    // }
    //AGAIN DOUBLE CHECK FOR THIS ONE IF IT WILL BE SUITABLE TO KICK IN THE HATCH DURING TELEOP AND AUTO OR IF WE NEED IT

    public void Display(){
        // Util.consoleLog("IstheHatchOut =%b IstheHatchIn =%b", isExtended, isRetracted);
        // SmartDashboard.putBoolean("HatchExtended", isExtended);
        // SmartDashboard.putBoolean("HatchRetracted", isRetracted);
    }
    public void HatchKickOut(){
        if(isRetracted){
            //devices.hatchKickValve.Open();
            isExtended = true;
            isRetracted = false;
        }
        else Util.consoleLog("The Hatch Kicker is already extended");
        
    }
    public void HatchKickIn(){

        if(isExtended){
            //devices.hatchKickValve.Close();
            isExtended = false;
            isRetracted = true;
        }
        else Util.consoleLog("The Hatch Kicker is already in");

    }
}
