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
public class IntakeAndSpit {

    Robot robot;
    public  boolean isSpitting = false;
    public  boolean isIntaking = false;
    private boolean motorStopped = false;
  //  private boolean switchPressed = false;
    private boolean switchReset = false;
    public static IntakeAndSpit intakeSpit = null;
  //  private Thread autoIntakeThread;

    public static IntakeAndSpit getInstance(Robot robot){
        if(intakeSpit == null) intakeSpit = new IntakeAndSpit(robot);
        return intakeSpit;
    }
    private IntakeAndSpit(Robot robot){
        this.robot = robot;
        isIntaking = false;
        isSpitting = false;
        Util.consoleLog();
    }
    public void dispose(){
        Util.consoleLog();
        Devices.pickupMotor.disable();
        Devices.pickupMotor.free();
        Devices.ballSpit.disable();
        Devices.ballSpit.free();
    }
    private void Display(){
        SmartDashboard.putBoolean("isSpitting", isSpitting);
    }
    public void Spit(double power){
        // if(power > 1){
        //     power = 1;
        // }
        // else if (power <-1){
        //     power = -1;
        // }
        Devices.ballSpit.set(power);
        isSpitting = true;
        Util.consoleLog("The motor is moving");
    }
    public void Intake(double power){
       Devices.pickupMotor.set(power);
       isIntaking = true;
       Util.consoleLog("The motor is intaking?");
    }

   public void StopIntake(){
       Devices.pickupMotor.set(0);
       isIntaking = false;
   }
   public void StopSpit(){
       Devices.ballSpit.set(0);
       isSpitting = false;
   }
   public boolean isIntaking(){
       return isIntaking;
   }
   public boolean isSpitting(){
       return isSpitting;
   }
  /*  public void StartAutoIntake(){
        if(autoIntakeThread != null) return;
        autoIntakeThread = new AutoIntake();
        autoIntakeThread.start();
        Util.consoleLog("Started the thread");
    }
    public void StopAutoIntake(){
        if(autoIntakeThread != null){
            autoIntakeThread.interrupt();
        }
        autoIntakeThread = null;
    }
    //There are a lot of problems with the thread below for intaking the balls. Might want to consider
    // a rewrite when able to think again.

    private class AutoIntake extends Thread{
        AutoIntake(){
            this.setName("AutoIntake");
        }
        public void run(){
            boolean switchPressed = false;
           
            try{

            if(!Devices.ballSwitch.get()){
                if(switchPressed == false){
                    Devices.ballSpit.set(0.5);
                    switchPressed = false;
                }
                else if(switchPressed == true){
                    Devices.ballSpit.set(0);
                    switchPressed = false;
                }
            
            }
            else if(Devices.ballSwitch.get()){
                switchPressed = true;
            }
                while(!isInterrupted()){

                }
                if(!interrupted() && robot.isEnabled()) {
                    Util.consoleLog(" Cube detected");
                    sleep(500);
                }

            }
            catch (InterruptedException e) {
				Devices.pickupMotor.set(0);
			}
			catch (Throwable e) { e.printStackTrace(Util.logPrintStream); }
			finally {Devices.pickupMotor.set(0);}
			
			autoIntakeThread = null;
        }

    }*/
}
