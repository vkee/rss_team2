package StateMachine;

import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

/**
 * This state chooses the closest reachable here->goal->deposit path and goes starts waypoint navigation to it. 
 * If no such goal exists, it starts navigation directly to the deposit site by yielding to the waypoint nav deposit state
 */
public class WaypointNavClose implements FSMState {


	private FSM fsm;	

	public WaypointNavClose(FSM stateMachine)
		{
		fsm = stateMachine;

		//init any variables for this state

		}	

	
	public stateENUM getName()
		{return stateENUM.WNCLOSE;}

	
	public boolean accepts(msgENUM msgType)
		{
		if (msgType == msgENUM.WHEELS) return true;
		return false;
		}


	public void update(Object msg)
		{
		//do stuff

		//if condition to leave state
		//fsm.updateState(new NextState(fsm));

		}


}