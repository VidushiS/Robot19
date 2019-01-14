/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package Team4450.Robot19;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;

import java.io.File;


/**
 * Add your docs here.
 */
public class PathFinderReadFromCSV {

    File middleTrajectoryCSV = new File("TestPath.pf1.csv");
    File leftTrajectoryCSV = new File("TestPath.left.pf1.csv");
    File rightTrajectoryCSV = new File("TestPath.right.pf1.csv");
 
    Trajectory trajectory = Pathfinder.readFromCSV(middleTrajectoryCSV);
    Trajectory right = Pathfinder.readFromCSV(rightTrajectoryCSV);
    Trajectory left = Pathfinder.readFromCSV(leftTrajectoryCSV);

}
