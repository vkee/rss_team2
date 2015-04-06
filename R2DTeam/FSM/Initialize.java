package FSM;

public Initialize implements FSMState {

	private FSM fsm;	

	public Initlialize(FSM stateMachine)
		{
		fsm = stateMachine;
		}	

	
	public StateENUM getName()
		{return stateENUM.INITIALIZE;}

	
	public boolean accepts(msgENUM msgType)
		{
		if (msgType == msgENUM.WHEELS) return true;}
		}


	public void update(Object msg)
		{
		//do stuff

		//if condition to leave state
		//fsm.updateState(new NextState(fsm));

		}

}