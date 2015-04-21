package StateMachine;

import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

/**
 * This state uses the visual servoing to approach the block.
 */
public class VisualServoCollect implements FSMState {


	private FSM fsm;	

	public VisualServoCollect(FSM stateMachine)
		{
		fsm = stateMachine;

		//init any variables for this state

		}	

	
	public stateENUM getName()
		{return stateENUM.VISUALSERVOCOLLECT;}

	
	public boolean accepts(msgENUM msgType)
		{
		if (msgType == msgENUM.WHEELS || msgType == msgENUM.IMAGE) return true;
		return false;
		}


	public void update(GenericMessage msg)
		{
		//do stuff

		//if condition to leave state
		//fsm.updateState(new MoveForward(fsm));

		}


}
