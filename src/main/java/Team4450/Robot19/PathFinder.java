/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package Team4450.Robot19;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.modifiers.TankModifier;
/**
 * Add your docs here.
 */
public class PathFinder {
    //This is the basic class I am working on for PathFinder. I am currently Trying to learn how to use it

    //For more information look at the README in JacisNonsense/Pathfinder/tree/master/Pathfinder-Java on Github
    
    Trajectory.Config config = new Trajectory.Config(FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.02, 10.0, 6.25, 197);
    //Below are the Waypoints that the robot needs to travel through. This is in PathWeaver as well.
    Waypoint[] points = new Waypoint[]{
        //params are x-coor on field, y-coor on field, and exit angle in radians
        new Waypoint(3.525, 3.366, 0),
        new Waypoint(16.456, 4.041, 0)
    };

    

    //Use the above defined vars of points and the configuration to follow and input it in declaration below
    //The statement below makes the path, which is also what the GUI does. 

    Trajectory trajectory = Pathfinder.generate(points, config);

    // The distance between the left and right sides of the wheelbase is 0.6m
double wheelbase_width = 0.6;

// Create the Modifier Object
TankModifier modifier = new TankModifier(trajectory).modify(wheelbase_width);

// Generate the Left and Right trajectories using the original trajectory
// as the centre
Trajectory left  = modifier.getLeftTrajectory();       // Get the Left Side
Trajectory right = modifier.getRightTrajectory();      // Get the Right Side

    public void trajectoryStuff() {
        //Using Trajectory
        for (int i = 0; i < trajectory.length(); i++) {
            Trajectory.Segment seg = trajectory.get(i);
            
            System.out.printf("%f,%f,%f,%f,%f,%f,%f,%f\n", 
                seg.dt, seg.x, seg.y, seg.position, seg.velocity, 
                    seg.acceleration, seg.jerk, seg.heading);
        }
        
    }
}
