package StateMachine;

import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

/**
 * This state rotates the back servo until the gate is open
 */
public class OpenGate implements FSMState {


	private FSM fsm;	

	public OpenGate(FSM stateMachine)
		{
		fsm = stateMachine;

		//init any variables for this state

		}	

	
	public stateENUM getName()
		{return stateENUM.OPENGATE;}

	
	public boolean accepts(msgENUM msgType)
		{
		if (msgType == msgENUM.GATE) return true;
		return false;
		}


	public void update(GenericMessage msg)
		{
		//do stuff

		//if condition to leave state
		//fsm.updateState(new NextState(fsm));

		}


	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}


}