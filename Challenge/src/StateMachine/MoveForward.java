package StateMachine;

import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

/**
 * This state moves the robot forward to release the blocks
 */
public class MoveForward implements FSMState {


    private FSM fsm;	

    public MoveForward(FSM stateMachine)
    {
        fsm = stateMachine;

        //init any variables for this state

    }	


    public stateENUM getName()
    {return stateENUM.MOVEFORWARD;}


    public boolean accepts(msgENUM msgType)
    {
        if (msgType == msgENUM.WHEELS) return true;
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