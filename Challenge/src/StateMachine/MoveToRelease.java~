package StateMachine;

import java.awt.geom.Point2D;

import MotionPlanning.RRTreeNode;
import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;
import org.ros.message.rss_msgs.*;
import org.ros.message.lab5_msgs.*;
import org.ros.message.lab6_msgs.*;
import org.ros.message.Challenge_msgs.*;
/**
 * This state moves the robot forward to release the blocks
 */
public class MoveToRelease implements FSMState {


	private FSM fsm;	
	private Point2D.Double startPt;

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
		org.ros.message.rss_msgs.OdometryMsg message = (org.ros.message.rss_msgs.OdometryMsg) msg.message;
		//do stuff
		if (startPt == null){
			startPt = new Point2D.Double(message.x, message.y);
		}


		//if condition to leave state
//		System.out.println("Man, that's some rainbow colored crap right there...");
		if (RRTreeNode.distance(startPt, new Point2D.Double(message.x, message.y)) < 0.4){
			MotionMsg motion = new MotionMsg();
			motion.rotationalVelocity = 0.0;
			motion.translationalVelocity = 0.1;
			fsm.motionPub.publish(motion);
		} else {
			MotionMsg motion = new MotionMsg();
			motion.rotationalVelocity = 0.0;
			motion.translationalVelocity = 0.0;
			fsm.motionPub.publish(motion);
			System.out.println("Done"); 
		}
		//fsm.updateState(new NextState(fsm));

	}


	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}


}