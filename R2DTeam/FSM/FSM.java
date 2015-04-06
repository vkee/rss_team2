package FSM;

/**
 * 
 *
 */
public class FSM {

	private FSMState currentState;
	private boolean inState;

	// public wheels publishers
	// public servos publishers

	public FSM()
		{
		
		//initialize publishers

		currentState = new Initialize(this);
		inState = false;

		//initialize all listeners with dispatchState as the callback

		}

	public void updateState(FSMState newState)
		{
		currentState = newState
		}

	public void dispachState(Object msg)
		{
		if (inState) return;
		inState = true;
		if (currentState.accepts(msg.type))		//may not need this check
			{currentState.update(msg);}		
		inState = false;
		}

}
