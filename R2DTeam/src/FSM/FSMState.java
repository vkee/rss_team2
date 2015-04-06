package FSM;

/**
 * 
 */
public interface FSMState {

	public stateENUM getName();
	public boolean accepts(msgENUM msgType);
	public void update(Object msg);
	
}
