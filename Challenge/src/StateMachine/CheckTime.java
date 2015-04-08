package StateMachine;

import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

/**
 * This state decides if there is enough time to search for another block
 */
public class CheckTime implements FSMState {


	private FSM fsm;	

	public CheckTime(FSM stateMachine)
		{
		fsm = stateMachine;

		//init any variables for this state

		}	

	
	public stateENUM getName()
		{return stateENUM.CHECKTIME;}

	
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