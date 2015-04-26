package StateMachine;

import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

/**
 * This defines the interface that all states must implement in order to operate in the linked list style
 */
public interface FSMState {

	public stateENUM getName();
	public boolean accepts(msgENUM msgType);
	public void update(GenericMessage msg);
	public void onStart();
}
