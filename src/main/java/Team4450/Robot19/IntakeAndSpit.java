/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package Team4450.Robot19;

import Team4450.Lib.Util;

/**
 * Add your docs here.
 */
public class IntakeAndSpit {

    Robot robot;

    public static IntakeAndSpit intakeSpit = null;

    public static IntakeAndSpit getInstance(Robot robot){
        if(intakeSpit == null) intakeSpit = new IntakeAndSpit(robot);
        return intakeSpit;
    }
    private IntakeAndSpit(Robot robot){
        this.robot = robot;
        Util.consoleLog();
    }
}
