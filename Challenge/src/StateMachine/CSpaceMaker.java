package StateMachine;

import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

/**
 * This state creates the initial CSPace map
 */
public class CSpaceMaker implements FSMState {


	private FSM fsm;	

	public CSpaceMaker(FSM stateMachine)
		{
		fsm = stateMachine;

		//init any variables for this state

		}	

	
	public stateENUM getName()
		{return stateENUM.CSPACE;}

	
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