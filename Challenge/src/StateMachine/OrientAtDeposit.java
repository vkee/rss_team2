package StateMachine;

import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

/**
 * This state puts the robot in place to deposit blocks
 */
public class OrientAtDeposit implements FSMState {


	private FSM fsm;	

	public OrientAtDeposit(FSM stateMachine)
		{
		fsm = stateMachine;

		//init any variables for this state

		}	

	
	public stateENUM getName()
		{return stateENUM.ORIENTDEPOSIT;}

	
	public boolean accepts(msgENUM msgType)
		{
		if (msgType == msgENUM.WHEELS) return true;
		return false;
		}


	public void update(GenericMessage msg)
		{
		//do stuff



		//if condition to leave state
		System.out.println("Done orienting at deposit site.");
		fsm.updateState(new OpenGate(fsm));

		}


	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}


}