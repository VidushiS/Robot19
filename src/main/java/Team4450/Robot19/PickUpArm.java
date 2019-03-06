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
public class PickUpArm {
    Robot robot;
    public boolean isExtended = false;
    public boolean isRetracted = true;

    public static PickUpArm pickupArm = null;

    public static PickUpArm getInstance(Robot robot){
        if(pickupArm == null){
            pickupArm = new PickUpArm(robot);
        }
        return pickupArm;
    }
    public PickUpArm(Robot robot){
        this.robot = robot;
        Util.consoleLog("The pickup arm has been created");
        Retract();
    }
    public void dispose(){
        Util.consoleLog();
        pickupArm = null;
    }

    public void display(){
        SmartDashboard.putBoolean("Extended", isExtended);
        SmartDashboard.putBoolean("Retracted", isRetracted);
    }
    public void Extend(){
        
        //Should extend the pistons
            Devices.pickupValve.SetA();
            isExtended = true;
            isRetracted = false;
            display();
        
        
    }

    
    public void Retract(){
        
        //Should retract the robot
            Devices.pickupValve.SetB();
            isExtended = false;
            isRetracted = true;
            display();
        

    }
    public boolean isExtended(){
        return isExtended;
    }

}
