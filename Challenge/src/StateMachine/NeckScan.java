package StateMachine;

import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

/**
 * This state turns the neck all around taking snapshots of the world at different angles
 */
public class NeckScan implements FSMState {
	//TODO: make servos rotate

	private FSM fsm;	

	public NeckScan(FSM stateMachine)
		{
		fsm = stateMachine;

		//init any variables for this state

		}	

	
	public stateENUM getName()
		{return stateENUM.SCAN;}

	
	public boolean accepts(msgENUM msgType)
		{
		if (msgType == msgENUM.SERVO) return true;
		if (msgType == msgENUM.IMAGE) return true;
		return false;
		}


	public void update(GenericMessage msg)
		{
		//do stuff

		//if condition to leave state
//		fsm.updateState(new NextState(fsm));

		}


	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}


}