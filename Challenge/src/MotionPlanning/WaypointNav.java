package MotionPlanning;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.ros.message.lab5_msgs.*;
import org.ros.message.lab6_msgs.*;
import org.ros.message.rss_msgs.*;
import org.ros.node.topic.Publisher;

import StateMachine.FSM;

public class WaypointNav {

    private FSM fsm;
    private final boolean wayptNav_debug = false;

    // The waypoint tolerance defining whether the robot is at a waypoint
    private final double WAYPT_TOL = 0.05; // in meters
    private final double WAYPT_TOL_THETA = 0.1; // in radians

    private final double FWD_GAIN = .3; // may need to change to 0.1 if runs into things and off waypoints
    private final double ROT_GAIN = .3;

    private ArrayList<Point2D.Double> waypoints;
    private Point2D.Double goal;
    private int currWaypt = 0;
    private boolean atGoal = false;
    private Publisher<MotionMsg> motionPub;

    public WaypointNav(ArrayList<Point2D.Double> waypts, Point2D.Double goalpt, FSM fsm)
    {
        waypoints=waypts;
        goal = goalpt;
        this.fsm = fsm;
        motionPub = fsm.motionPub;
    }

    public boolean isDone()
    {
        return (currWaypt == waypoints.size()) && atGoal;
    }

    /**
     * Navigates the robot from waypoint to waypoint, called by odometry handler
     * Assuming that the robot starts at the first waypoint
     *
     * @param points
     *            the set of waypoints for the robot to navigate
     */
    public void wayptNav(double robotX, double robotY, double robotTheta) {
        Point2D.Double wayPoint = null;
        boolean finalWaypt = false;
        if (currWaypt < waypoints.size()) {
            wayPoint = waypoints.get(currWaypt);
        } else {
            wayPoint = goal;
            System.out.println("LAST POINT: X-" + wayPoint.getX() + " Y-" + wayPoint.getY());
            finalWaypt = true;
        }

        //        Displaying the next waypoint to go to in the GUI
        fsm.mapDrawer.publishEllipse(wayPoint.getX(), wayPoint.getY(), 0.1, 0.1, Color.YELLOW);

        double currX = robotX;
        double currY = robotY;
        double currTheta = robotTheta;

        double xError = wayPoint.getX() - currX;
        double yError = wayPoint.getY() - currY;
        double thetaToPoint = Math.atan2(yError, xError);

        // Keeping within 0 to 2PI
        if (thetaToPoint < 0) {
            thetaToPoint += 2 * Math.PI;
        }

        double thetaError = currTheta - thetaToPoint;

        // Keeping the theta error always in the range -pi to pi
        if (thetaError < -Math.PI) {
            thetaError += 2 * Math.PI;
        } else if (thetaError > Math.PI) {
            thetaError -= 2 * Math.PI;
        }

        // While not at the current waypoint, adjust proportionally to the error
        MotionMsg msg = new MotionMsg();

        // adjust theta error first
        if (Math.abs(thetaError) > WAYPT_TOL_THETA) {
            msg.rotationalVelocity = -Math.min(ROT_GAIN * thetaError, 0.25);
            msg.translationalVelocity = 0.0;
            // only when theta has been reached, adjust translation
        } else if ((Math.abs(xError)+Math.abs(yError)) > WAYPT_TOL) {
            msg.translationalVelocity = Math.min(
                    FWD_GAIN * MultiRRT3D.getDist(0.0, 0.0, xError, yError),
                    0.5);
            msg.rotationalVelocity = 0.0;
        } else {
            //			Marking the waypoint as complete
            fsm.mapDrawer.publishEllipse(wayPoint.getX(), wayPoint.getY(), 0.1, 0.1, Color.GRAY);

            //            Particle Filter Motion Update
            double transDist = RRT.getDist(fsm.prevPt.x, fsm.prevPt.y, robotX, robotY);
            double rotAng = RRT.getAngle(fsm.prevPt.x, fsm.prevPt.y, robotX, robotY);

            if (rotAng < 0.0) {
                rotAng += 2*Math.PI;
            }

            fsm.particleFilter.motionUpdate(transDist, rotAng);

            if (currWaypt < waypoints.size()) {
                int way = currWaypt + 1;
                currWaypt += 1;

            } else {
                atGoal = true;
                msg.translationalVelocity = 0.0;
                msg.rotationalVelocity = 0.0;
                System.out.println("At the goal!");
            }
            fsm.prevPt = wayPoint;
        }
        
        motionPub.publish(msg);
        
        if (wayptNav_debug == true) {
            System.out.println("Trans Vel: " + msg.translationalVelocity);
            System.out.println("Rot Vel: " + msg.rotationalVelocity);
        }
    }
}