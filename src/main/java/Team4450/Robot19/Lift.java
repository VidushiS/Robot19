/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package Team4450.Robot19;

import Team4450.Lib.Util;
import edu.wpi.first.wpilibj.Timer;

/**
 * Add your docs here.
 */
public class Lift {

    Robot robot = new Robot();
    boolean isMoving = true;

    public Lift(Robot robot){
        Util.consoleLog();
        this.robot = robot;
    }

    public void LiftMoveTeleOp(double power){
        if(power <-1.0){
            power = -1.0;
        }
        else if(power > 1.0){
            power = 1.0;
        }

        if(Devices.winchSwitch.get())
        Devices.winchDrive.set(power);;
        isMoving = true;
    }

    public void LiftStop(){
        Devices.winchDrive.set(0);
        isMoving = false;
    }

    public void LiftMoveUp(double seconds){
        Devices.winchDrive.set(0.6);
        isMoving = true;
        Timer.delay(seconds);
        LiftStop();
        isMoving = false;
    }
    public void LiftMoveDown(double seconds){
        Devices.winchDrive.set(-0.6);
        isMoving = true;
        Timer.delay(seconds);
        LiftStop();
        isMoving = false;

    }
}
