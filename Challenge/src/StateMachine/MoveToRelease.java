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


        //if condition to leave state
        System.out.println("Man, that's some rainbow colored crap right there...");

        //fsm.updateState(new NextState(fsm));

    }


    @Override
    public void onStart() {
        // TODO Auto-generated method stub

    }


}