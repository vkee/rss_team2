package MotionPlanning;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.ros.message.lab5_msgs.*;
import org.ros.message.lab6_msgs.*;
import org.ros.message.rss_msgs.*;
import org.ros.node.topic.Publisher;

public class WaypointNav {
	
	private final boolean wayptNav_debug = false;
	
	// The waypoint tolerance defining whether the robot is at a waypoint
    private final double WAYPT_TOL = 0.05; // in meters
    private final double WAYPT_TOL_THETA = 0.1; // in radians

    private final double FWD_GAIN = .2; // may need to change to 0.1 if runs into things and off waypoints
    private final double ROT_GAIN = .2;
	
	private ArrayList<Point2D.Double> waypoints;
	private Point2D.Double goal;
	private int currWaypt = 0;
	private boolean atGoal = false;
	private Publisher<MotionMsg> motionPub;
	
	public WaypointNav(ArrayList<Point2D.Double> waypts, Point2D.Double goalpt, Publisher<MotionMsg> motionPublisher)
    	{
    	waypoints=waypts;
    	goal = goalpt;
    	motionPub = motionPublisher;
    	}
	
	public boolean isDone()
		{return (currWaypt == waypoints.size()) && atGoal;}
	
	/**
     * Navigates the robot from waypoint to waypoint, called by odometry handler
     * Assuming that the robot starts at the first waypoint
     *
     * @param points
     *            the set of waypoints for the robot to navigate
     */
    public void wayptNav(double robotX, double robotY, double robotTheta) {
        // System.out.println("In waypt nav");
        Point2D.Double wayPoint = null;
        boolean finalWaypt = false;
        if (currWaypt < waypoints.size()) {
            wayPoint = waypoints.get(currWaypt);
        } else {
            wayPoint = goal;
            System.out.println("LAST POINT: X-" + wayPoint.getX() + " Y-" + wayPoint.getY());
            finalWaypt = true;
        }

        double currX = robotX;
        double currY = robotY;
        double currTheta = robotTheta;

        // Accounting for robot initial position without restarting orcboard or reseting odometry
        // double currX = robotX + xShift;
        // double currY = robotY + yShift;
        // double currTheta = robotTheta + thetaShift;

        if (wayptNav_debug == true || finalWaypt ) {
            System.out.println("currX: " + currX);
            System.out.println("currY: " + currY);
            System.out.println("currTheta: " + currTheta);
        }
        double xError = wayPoint.getX() - currX;
        double yError = wayPoint.getY() - currY;
        double thetaToPoint = Math.atan2(yError, xError);

        // Keeping within 0 to 2PI
        if (thetaToPoint < 0) {
            thetaToPoint += 2 * Math.PI;
        }
        if (wayptNav_debug == true || finalWaypt ) {
            System.out.println("xError: " + xError);
            System.out.println("yError: " + yError);
            System.out.println("thetaToPoint: " + thetaToPoint);
        }
        // make sure currTheta is actual angle of robot, may need to play with thetashift
        // TODO make sure that this is actually the theta error
        double thetaError = currTheta - thetaToPoint;
        if (wayptNav_debug == true || finalWaypt ) {
            System.out.println("Theta Error: " + thetaError);
        }
        // Keeping the theta error always in the range -pi to pi
        if (thetaError < -Math.PI) {
            thetaError += 2 * Math.PI;
        } else if (thetaError > Math.PI) {
            thetaError -= 2 * Math.PI;
        }

        if (wayptNav_debug == true || finalWaypt ) {
            System.out.println("Theta Error after correction: " + thetaError);
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
                    FWD_GAIN * MultiRRT.getDist(0.0, 0.0, xError, yError),
                    0.5);
            msg.rotationalVelocity = 0.0;
        } else {
            if (currWaypt < waypoints.size()) {
                int way = currWaypt + 1;
                System.out.println("WAYPOINT: " + way + " REACHED at X: "
                        + wayPoint.getX() + " Y: " + wayPoint.getY()
                        + " ROBOT-X:" + currX + " ROBOT-Y:" + currY
                        + " out of " + waypoints.size() + " waypoints.");
                System.out.println("xError " + xError + " yError " + yError);
                currWaypt += 1;

            } else {
                atGoal = true;
                msg.translationalVelocity = 0.0;
                msg.rotationalVelocity = 0.0;
                System.out.println("At the goal!");
            }
        }
        motionPub.publish(msg);
        if (wayptNav_debug == true) {
            System.out.println("Trans Vel: " + msg.translationalVelocity);
            System.out.println("Rot Vel: " + msg.rotationalVelocity);
        }
    }

}
