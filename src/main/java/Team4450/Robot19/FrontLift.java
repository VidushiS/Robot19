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
public class FrontLift {

    
    public boolean isExtended;
    public boolean isRetracted;
    Robot robot;

    public static FrontLift frontLift = null;

    //Maybe a constructor, do we need to pass in any parameters so that the class can funtion

    public static FrontLift getInstance(Robot robot){
        if(frontLift == null){
            frontLift = new FrontLift(robot);
        }
        return frontLift;
    }
    
    private FrontLift(Robot robot){
        Util.consoleLog(); 
        this.robot = robot;
        isExtended = true;
        isRetracted = false;
        Retract();
        Util.consoleLog("The front climb pistons have been created");
    }

    //REWRITE CONSTRUCTOR BECAUSE I CAN FORESEE SOME ISSUES WITH THE USE OF DISPLAY AND RETRACT CLASSES

    public void Display(){
        // Util.consoleLog("IsExtended =%b IsRetracted =%b", isExtended, isRetracted);
        // SmartDashboard.putBoolean("Extended", isExtended);
        // SmartDashboard.putBoolean("Retracted", isRetracted);
    }
    public void Extend(){
        
        if(isRetracted){
        Devices.frontClimbValve.SetA();
        isExtended = true;
        isRetracted = false;
        }
        else Util.consoleLog("The front of the robot is already extended!");
        
        
    }
    public void Retract(){

        if(isExtended){
        Devices.frontClimbValve.SetB();
        isExtended = false;
        isRetracted = true;
        }
        else Util.consoleLog("The front of the robot is already retracted");
        
    }
    
    
}
