package StateMachine;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import MotionPlanning.WaypointNav;
import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

/**
 * This state triggers when the block door opens because a block was collected.
 * The state ends when the door releases
 */
public class BackToGoalPoint implements FSMState {

	private FSM fsm;
	private WaypointNav waypointNavigator;

	public BackToGoalPoint(FSM stateMachine, boolean collected) {
		fsm = stateMachine;
		if (collected) fsm.blocksCollected++;
		// init any variables for this state

		ArrayList<Point2D.Double> waypoints = new ArrayList<Point2D.Double>();
		waypoints.add(fsm.currentLocation);
		waypointNavigator = new WaypointNav(waypoints, fsm.currentLocation, fsm); // waypont
																					// nav
																					// to
																					// exact
																					// goal
																					// location
	}

	public stateENUM getName() {
		return null;//stateENUM.BACKTOGOALPOINT;
	}

	public boolean accepts(msgENUM msgType) {
		if (msgType == msgENUM.FLAP)
			return true;
		return false;
	}

	public void update(GenericMessage msg) {

		org.ros.message.rss_msgs.OdometryMsg message = (org.ros.message.rss_msgs.OdometryMsg) msg.message;

		waypointNavigator.wayptNav(message.x, message.y, message.theta);

		if (waypointNavigator.isDone()) {
			//fsm.updateState(new NeckScan(fsm));
		}

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

}
