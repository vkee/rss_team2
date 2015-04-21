package StateMachine;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import MotionPlanning.WaypointNav;
import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

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
		
		waypointNavigator = new WaypointNav(waypoints, fsm.foundPaths.goal, fsm.motionPub);


		}	

	
	public stateENUM getName()
		{return stateENUM.WNDEPOSIT;}

	
	public boolean accepts(msgENUM msgType)
		{
		if (msgType == msgENUM.WHEELS) return true;
		return false;
		}


	public void update(Object msg)
		{
		//do stuff
		waypointNavigator.wayptNav(msg.x, msg.y, msg.theta);


		//if condition to leave state
		if (waypointNavigator.isDone())
			{fsm.updateState(new OrientAtDeposit(fsm));}


		}
}