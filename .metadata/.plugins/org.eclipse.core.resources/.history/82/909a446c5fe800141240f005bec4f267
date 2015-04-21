package StateMachine;

import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

/**
 * This state triggers when the block door opens because a block was collected. The state ends when the door releases
 */
public class BlockCollected implements FSMState {


	private FSM fsm;	

	public BlockCollected(FSM stateMachine)
		{
		fsm = stateMachine;

		//init any variables for this state

		}	

	
	public stateENUM getName()
		{return stateENUM.BLOCKCOLLECTED;}

	
	public boolean accepts(msgENUM msgType)
		{
		if (msgType == msgENUM.DOOR) return true;
		return false;
		}


	public void update(Object msg)
		{
		//do stuff

		//if condition to leave state
		//fsm.updateState(new NextState(fsm));

		}


}