package StateMachine;

import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

/**
 * This state moves the robot forward to release the blocks
 */
public class MoveToRelease implements FSMState {


    private FSM fsm;	

    public MoveToRelease(FSM stateMachine)
    {
        fsm = stateMachine;

        //init any variables for this state

    }	


    public stateENUM getName()
    {return stateENUM.MOVETORELEASE;}


    public boolean accepts(msgENUM msgType)
    {
        if (msgType == msgENUM.WHEELS) return true;
        return false;
    }


    public void update(GenericMessage msg)
    {
        //do stuff

        System.out.println("Current state: MoveToRelease.");

        //if condition to leave state
        System.out.println("Updating state...");
        //fsm.updateState(new NextState(fsm));

    }


    @Override
    public void onStart() {
        // TODO Auto-generated method stub

    }


}