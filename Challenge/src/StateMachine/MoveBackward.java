package StateMachine;

import java.awt.geom.Point2D;

import MotionPlanning.CSpace2D;
//import MotionPlanning.MotionMsg;
import MotionPlanning.RRTreeNode;
import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

import org.ros.message.rss_msgs.*;
import org.ros.message.lab5_msgs.*;
import org.ros.message.lab6_msgs.*;
import org.ros.message.Challenge_msgs.*;


/**
 * Move back a little if front bump happens then waypoint nav to orginal goal
 */
public class MoveBackward implements FSMState {


    private FSM fsm;	
    private Point2D.Double startLoc;
    
    public MoveBackward(FSM stateMachine)
    {
        fsm = stateMachine;

        //init any variables for this state

    }	


    public stateENUM getName()
    	{return null;}//stateENUM.MOVEBACKWARD;}


    public boolean accepts(msgENUM msgType)
    {
        if (msgType == msgENUM.WHEELS) return true;
        return false;
    }


    public void update(GenericMessage msg)
    	{
        System.out.println("Current state: MoveBackward.");
		org.ros.message.rss_msgs.OdometryMsg message = (org.ros.message.rss_msgs.OdometryMsg) msg.message;
		
		if (startLoc == null) { startLoc=new Point2D.Double(message.x, message.y); }
		
        MotionMsg motion = new MotionMsg();
        motion.rotationalVelocity = 0.0;

        if (RRTreeNode.distance(startLoc, new Point2D.Double(message.x, message.y)) >= CSpace2D.ROBOT_LONGEST_DIM) 
        	{motion.translationalVelocity = 0.0;
            fsm.motionPub.publish(motion);
            System.out.println("Updating state...");
            fsm.updateState(new BackToGoalPoint(fsm, false));
        	}
        else  
        	{motion.translationalVelocity = -0.1;
            fsm.motionPub.publish(motion);
        	}
    	}
    
    


    @Override
    public void onStart() {
        // TODO Auto-generated method stub

    }


}
