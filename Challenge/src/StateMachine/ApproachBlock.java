package StateMachine;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import MotionPlanning.WaypointNav;
import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;
import VisualServo.MultipleBlobTracking;

import org.ros.message.rss_msgs.*;
import org.ros.message.lab5_msgs.*;
import org.ros.message.lab6_msgs.*;
import org.ros.message.Challenge_msgs.*;

/**
 * This state moves forward toward a goal location until the visual servo sees the block
 */
public class ApproachBlock implements FSMState {

	private FSM fsm;	
	private Point2D.Double goal;
	private WaypointNav waypointNavigator;
	private MultipleBlobTracking mbt;

	public ApproachBlock(FSM stateMachine, Point2D.Double goalPoint)
	{
		fsm = stateMachine;
		goal = goalPoint;
		ArrayList<Point2D.Double> waypoints = new ArrayList<Point2D.Double>();
		waypoints.add(goalPoint);
		waypointNavigator = new WaypointNav(waypoints, goalPoint, fsm);
		mbt = new MultipleBlobTracking();
		
//		TODO: possibly update this in the vision instead of here
//		may need to add a state if the block location is not where it is supposed to be
		fsm.foundPaths.useBiPath(fsm.currentLocation, goal);
		fsm.currentLocation = goal;
	}	

	
	public stateENUM getName()
	{return stateENUM.APPROACHBLOCK;}


	public boolean accepts(msgENUM msgType)
	{
		if (msgType == msgENUM.WHEELS) return true;
		return false;
	}


	public void update(GenericMessage msg)
	{
		//do stuff

		//if condition to leave state
		//fsm.updateState(new NextState(fsm));

		boolean blockInVision = !mbt.isDone();

		if (blockInVision){
			fsm.updateState(new VisualServoCollect(fsm));
		} else {
			OdometryMsg message = (OdometryMsg)msg.message;

			waypointNavigator.wayptNav(message.x, message.y, message.theta);

			if (waypointNavigator.isDone()) {
				fsm.updateState(new WaypointNavClose(fsm));
			}		
		}
	}


	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}
}