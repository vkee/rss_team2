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
public class BlockCollected implements FSMState {

	private FSM fsm;
	private WaypointNav waypointNavigator;

	public BlockCollected(FSM stateMachine) {
		fsm = stateMachine;
		fsm.blocksCollected++;
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
		return stateENUM.BLOCKCOLLECTED;
	}

	public boolean accepts(msgENUM msgType) {
		if (msgType == msgENUM.FLAP)
			return true;
		return false;
	}

	public void update(GenericMessage msg) {

		waypointNavigator.wayptNav(message.x, message.y, message.theta);

		if (waypointNavigator.isDone()) {
			fsm.updateState(new NeckScan(fsm));
		}

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

}