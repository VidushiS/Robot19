/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package Team4450.Robot19;

import Team4450.Lib.Util;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is meant to handle the intake and spit motor functions.
 * There are separate classes here so that the manipulator can have some control,
 * but there is also a thread so that we can autoIntake and be efficient. 
 * (Kinda nice, no?)
 */
public class IntakeAndSpit {

    Robot robot;
    public  boolean isSpitting = false;
    public  boolean isIntaking = false;
     
    public static IntakeAndSpit intakeSpit = null;
    private Thread autoIntakeThread = null;
    public boolean ISTHREADRUNNING = false;

    public static IntakeAndSpit getInstance(Robot robot){
        if(intakeSpit == null) intakeSpit = new IntakeAndSpit(robot);
        return intakeSpit;
    }
    private IntakeAndSpit(Robot robot){
        this.robot = robot;
        isIntaking = false;
        isSpitting = false;
        Display();
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
        SmartDashboard.putBoolean("isIntaking", isIntaking);
    }

    public void Spit(double power){
        
        Devices.ballSpit.set(power);
        isSpitting = true;
        isIntaking = false;
        Util.consoleLog("The motor is moving");
        Display();
    }

    public void Intake(double power){
       Devices.pickupMotor.set(power);
       isSpitting = false;
       isIntaking = true;
       Util.consoleLog("The motor is intaking");
       Display();
    }

   public void StopIntake(){
       Devices.pickupMotor.set(0);
       isIntaking = false;
       Display();
   }
   public void StopSpit(){
      Devices.ballSpit.set(0);
       isSpitting = false;
       Display();
   }
   public boolean isIntaking(){
       return isIntaking;
   }
   public boolean isSpitting(){
       return isSpitting;
   }
    public void StartAutoIntake(){
        if(autoIntakeThread != null) return;
        autoIntakeThread = new AutoIntake();
        autoIntakeThread.start();
        Util.consoleLog("Started the thread");
        ISTHREADRUNNING = true;
    }
    public void StopAutoIntake(){
        if(autoIntakeThread != null){
            autoIntakeThread.interrupt();
        }
        autoIntakeThread = null;
        ISTHREADRUNNING = false;
    }
    public boolean ISTHREADRUNNING(){
        return ISTHREADRUNNING;
    }
    private class AutoIntake extends Thread{
        AutoIntake(){
            this.setName("AutoIntake");
        }
        public void run(){
            boolean switchPressed = false;
            boolean doneIntaking = false;
           
            try{
                //Intake the ball for 2.5 seconds
                Intake(0.8);
                Spit(0.1);
                Timer.delay(4.5);
                //Stop running the intake motors 
                StopIntake();
                StopSpit();
                //See boolean doneIntaking in this thread. It essentially says keep
                //this loop running until the intake is over. 
                while(!doneIntaking && !interrupted()){
                
                    //If the switch is not pressed...
                    if(Devices.ballSwitch.get()){
                //         //And the ball hasn't passed over the switch already
                        if(switchPressed == false){
                            //Keep the ball spit motor running
                            Spit(0.1);
                            switchPressed = false;
                        }
                //         //And the ball has passed over the switch already
                        else if(switchPressed == true){
                //             //Stop the motor
                            StopSpit();
                //             //Set doneIntaking to true so that we can break out of this loop
                            doneIntaking = true;
                        }
                    
                    }
                    //If the switch is being pressed by the ball
                    else if(!Devices.ballSwitch.get()){
                        //Set switchPressed to true so that we know that the ball
                        //has passed over the switch
                        switchPressed = true;
                    }
                 }
            
                //Alerts when its done
                if(!interrupted() && robot.isEnabled()) {
                    Util.consoleLog("The ball is ready to be scored");
                    sleep(500);
                }

            }

            //If we terminate the thread early this saves us
            catch (InterruptedException e) {
                Devices.pickupMotor.set(0);
                Devices.ballSpit.set(0);
                Util.consoleLog("Over so soon?");
			}
			catch (Throwable e) { e.printStackTrace(Util.logPrintStream); }
            
            //Runs this code even after the thread is interrupted
            finally {
                Devices.pickupMotor.set(0);
                Devices.ballSpit.set(0);
            }
            
            //You want to set this to null so we can recreate it again. 
			autoIntakeThread = null;
        }

    }
}
