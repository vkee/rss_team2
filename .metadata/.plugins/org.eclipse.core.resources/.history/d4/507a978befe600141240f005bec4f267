package StateMachine;

import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

/**
 * This state kicks off the extra RRT searches for new goals
 */
public class RRTUpdate implements FSMState {


	private FSM fsm;	

	public RRTUpdate(FSM stateMachine)
		{
		fsm = stateMachine;

		//init any variables for this state

		}	

	
	public stateENUM getName()
		{return stateENUM.RRTUPDATE;}

	
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