/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package Team4450.Robot19;

import Team4450.Lib.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 * Add your docs here.
 */
public class VisionForward {

    private Robot robot;
    private Thread VisionGoForward;
    private Devices devices;
    private Vision vision;
    
   /* private class VisionGoForward extends Thread{

        VisionGoForward(){
            this.setName("VisionGoForward");
        }
        
        public void run(){

            try{
            //  if(vision.getContourDistanceBox()<= 520){
            //     devices.robotDrive.tankDrive(0.5, 0.5);    
            // }  
            // else if(vision.getContourDistanceBox() <= 250){
            //     devices.robotDrive.tankDrive(0.25, 0.25);
            // }
                // else if(vision.getContourDistanceBox() == 250){
                //     devices.robotDrive.tankDrive(0.0, 0.0);
                // }  
                
                // while(!isInterrupted() && vision.getContourDistanceBox() < 520) {
                //     LCD.printLine(9, "cube motor current=%f", vision.getContourDistanceBox());
                //     sleep(50);
                // }
            //     if(!interrupted() && robot.isEnabled()) {
            //         Util.consoleLog(" Cube detected");
            //         sleep(500);
            //     }
            }
            catch (InterruptedException e){
                devices.robotDrive.tankDrive(0.0, 0.0);
            }
            catch (Throwable e){
                e.printStackTrace(Util.logPrintStream); 
            }
            finally {devices.robotDrive.tankDrive(0.0, 0.0);}

            VisionGoForward = null;
        }
    }*/
    
}
