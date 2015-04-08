package StateMachine;

import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

/**
 * This state initializes the robot to the starting positions engaging all motors and servos
 */
public class Initialize implements FSMState {


	private FSM fsm;	

	public Initialize(FSM stateMachine)
		{
		fsm = stateMachine;

		//init any variables for this state

		}	

	
	public stateENUM getName()
		{return stateENUM.INITIALIZE;}

	
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