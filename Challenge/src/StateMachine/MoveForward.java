package StateMachine;

import java.awt.geom.Point2D;

///import MotionPlanning.MotionMsg;
import MotionPlanning.RRTreeNode;
import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

import org.ros.message.rss_msgs.*;
import org.ros.message.lab5_msgs.*;
import org.ros.message.lab6_msgs.*;
import org.ros.message.Challenge_msgs.*;


/**
 * Capture the block after visual servo centers
 */
public class MoveForward implements FSMState {


    private FSM fsm;	
    private boolean wasClicked;
    
    public MoveForward(FSM stateMachine)
    {
        fsm = stateMachine;

        //init any variables for this state

    }	


    public stateENUM getName()
    {return stateENUM.MOVEFORWARD;}


    public boolean accepts(msgENUM msgType)
    {
        if (msgType == msgENUM.WHEELS || msgType == msgENUM.FLAP) return true;
        return false;
    }


    public void update(GenericMessage msg)
    {
		if (msg.type == msgENUM.WHEELS) updateMove(msg);
		else if (msg.type == msgENUM.FLAP) updateGate(msg);
    }
    
    private void updateMove(GenericMessage msg)
    	{
        MotionMsg motion = new MotionMsg();
        motion.rotationalVelocity = 0.0;
        motion.translationalVelocity = 0.1;
        fsm.motionPub.publish(motion);
    	}
    
    private void updateGate(GenericMessage msg)
		{
        System.out.println("Current state: MoveForward.");

/*


		BumpMsg message = (BumpMsg) msg.message;

    	if (message.back == true)
			{wasClicked = true;}
    	
    	boolean gateCollected = (message.back == false && wasClicked == true);
		
		if (message.front == true || gateCollected)   //hit wall or innner gate opened and closed
    		{MotionMsg motion = new MotionMsg();
	    	motion.rotationalVelocity = 0.0;
	    	motion.translationalVelocity = 0.0;
	    	fsm.motionPub.publish(motion);
    		}
		if (message.front == true)
			{
			    System.out.println("Changing state from MoveForward to MoveBackward...");
                fsm.updateState(new MoveBackward(fsm));
			}
		else if (gateCollected)
			{
                System.out.println("Block collected!");
                System.out.println("Changing state from MoveForward to BackToGoalPoint...");
                fsm.updateState(new BackToGoalPoint(fsm, true));


			}
*/
        }


    @Override
    public void onStart() {
        // TODO Auto-generated method stub

    }


}
