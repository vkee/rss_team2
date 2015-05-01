package StateMachine;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import MotionPlanning.RRTreeNode;
import MotionPlanning.WaypointNav;
import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

import org.ros.message.rss_msgs.*;
import org.ros.message.lab5_msgs.*;
import org.ros.message.lab6_msgs.*;
import org.ros.message.Challenge_msgs.*;

/**
 * This state chooses the closest reachable here->goal->deposit path and goes starts waypoint navigation to it. 
 * If no such goal exists, it starts navigation directly to the deposit site by yielding to the waypoint nav deposit state
 */
public class WaypointNavClose implements FSMState {


	private FSM fsm;
	private ArrayList<Point2D.Double> waypoints;
	private WaypointNav waypointNavigator;
	private Point2D.Double finalGoal;

	public WaypointNavClose(FSM stateMachine)
	{
		fsm = stateMachine;

		//		double maxDist = fsm.ROBOTVEL * (fsm.TIME_LIMIT - (System.currentTimeMillis() - fsm.startTime));
		double maxDist = 1000000.0;
		finalGoal = fsm.foundPaths.getClosestFeasiblePointFrom(fsm.currentLocation, maxDist);
		System.out.println("Final Goal: " + finalGoal);

	}	

	@Override
	public void onStart() {
		if (finalGoal == null)	//no path to goal point left, go to deposit site
		{
			fsm.updateState(new WaypointNavDeposit(fsm));

		} else {
			waypoints = fsm.foundPaths.getPath(fsm.currentLocation, finalGoal);

			if (waypoints.size() == 1){
				//			switch to visual servo
				fsm.updateState(new ApproachBlock(fsm, finalGoal));

			} else {
				Point2D.Double goalpt = waypoints.get(waypoints.size()-1);

				waypointNavigator = new WaypointNav(waypoints, goalpt, fsm);
			}
		}		
	}
	
	public stateENUM getName() {return stateENUM.WNCLOSE;}


	public boolean accepts(msgENUM msgType)
	{
		if (msgType == msgENUM.WHEELS) return true;
		return false;
	}


	public void update(GenericMessage msg)
	{
		//do waypoint nav stuff
		OdometryMsg message = (OdometryMsg)msg.message;

		waypointNavigator.wayptNav(message.x, message.y, message.theta);

		//if condition to leave state (one waypoint away)
		//if (atWaypoint >= waypoints.size()-2)
		
		if (RRTreeNode.distance(new Point2D.Double(message.x, message.y), finalGoal) <= fsm.BLOCKVISUAL_DIST) {
		//if (waypointNavigator.isDone()) {
			fsm.updateState(new ApproachBlock(fsm, finalGoal));
		}
		//	{fsm.updateState(new ApproachBlock(fsm, finalGoal));}		//Approach until visual servo

	}





}