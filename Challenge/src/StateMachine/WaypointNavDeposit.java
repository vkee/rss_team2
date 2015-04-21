package StateMachine;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import MotionPlanning.WaypointNav;
import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;
import org.ros.node.topic.Subscriber;

/**
 * This state completely navigates to the deposit site using the existing path from our current location
 */
public class WaypointNavDeposit implements FSMState {

	private FSM fsm;
	private ArrayList<Point2D.Double> waypoints;
	private WaypointNav waypointNavigator;


	public WaypointNavDeposit(FSM stateMachine)
		{
		fsm = stateMachine;
		
		waypoints = fsm.foundPaths.getPathToGoal(fsm.currentLocation);
		
//		waypointNavigator = new WaypointNav(waypoints, fsm.foundPaths.goal, fsm.motionPub);


		}	

	
	public stateENUM getName()
		{return stateENUM.WNDEPOSIT;}

	
	public boolean accepts(msgENUM msgType)
		{
		if (msgType == msgENUM.WHEELS) return true;
		return false;
		}


	public void update(GenericMessage msg)
		{
		msg = (GenericMessage<org.ros.message.rss_msgs.OdometryMsg>) msg;
		//do stuff
		//waypointNavigator.wayptNav(msg.message.x, msg.message.y, msg.message.theta);


		//if condition to leave state
		if (waypointNavigator.isDone())
			{fsm.updateState(new OrientAtDeposit(fsm));}



		}
}