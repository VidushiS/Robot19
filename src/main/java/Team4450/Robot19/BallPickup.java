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
public class BallPickup {

    Devices Devices = new Devices();
    public boolean isExtended;
    public boolean isRetracted;
    Robot robot;

    //Maybe a constructor, do we need to pass in any parameters so that the class can funtion

    //Check with Corn why we write Util.consoleLog() and leave it blank
    public BallPickup(Robot robot){
        Util.consoleLog(); 
        this.robot = robot;
        isExtended = true;
        isRetracted = false;
        Retract();
    }

    //REWRITE CONSTRUCTOR BECAUSE I CAN FORESEE SOME ISSUES WITH THE USE OF DISPLAY AND RETRACT CLASSES

    public void Display(){
        // Util.consoleLog("IsExtended =%b IsRetracted =%b", isExtended, isRetracted);
        // SmartDashboard.putBoolean("Extended", isExtended);
        // SmartDashboard.putBoolean("Retracted", isRetracted);
    }
    public void Extend(){
        
        if(isRetracted){
      //      Devices.frontLiftValve.SetA();
        isExtended = true;
        isRetracted = false;
        }
        else Util.consoleLog("You are already Extended!");
        
        
    }
    public void Retract(){

        if(isExtended){
        //    Devices.frontLiftValve.SetB();
        isExtended = false;
        isRetracted = true;
        }
        else Util.consoleLog("You are already retracted");
        
    }
    // public boolean isExtended(){
    //     return isExtended;
    //     }
    
}
