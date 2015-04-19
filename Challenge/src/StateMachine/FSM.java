package StateMachine;

import java.awt.geom.Point2D;

import MotionPlanning.GoalAdjLists;
import MotionPlanning.MultiRRT;

/**
 * This class is the root node for all listeners and publishers and offers the control flow for the state machine. 
 * It is also the repository for any shared state variables
 */
public class FSM {
	
	public enum stateENUM {
		INITIALIZE, SCAN, WNCLOSE, MOVEFORWARD, OPENGATE, APPROACHBLOCK, BLOCKCOLLECTED, CHECKTIME, RRTUPDATE, CSPACE, ORIENTDEPOSIT, VISUALSERVOCOLLECT, WNDEPOSIT
	}
	
	public enum msgENUM { 
		NECK, WHEELS, BLOCK, IMAGE, GATE, DOOR
	}

	private FSMState currentState;
	private boolean inState;
	
	public final int TIME_LIMIT = 10*60;
	public final double RRT_TOLERANCE = 0.02;
	
	public int startTime;
	public int blocksCollected;
	public MultiRRT RRTengine;
	public Point2D.Double currentLocation;			//the current goal point we are at
	public GoalAdjLists foundPaths;
	//public HashSet<Integer> visited;
	

	// public wheels publishers
	// public servos publishers

    protected String mapFileName;
	
	public FSM()
		{
		
		//initialize publishers

		currentState = new Initialize(this);
		inState = false;
		
		dispachState(null);

		//initialize all listeners with dispatchState as the callback

		}
	
	public void setStartTime()
		{startTime = 0;}	//TODO correct this

	public void updateState(FSMState newState)
		{
		currentState = newState;
		}

	public void dispachState(Object msg)
		{
		if (inState) return;					// may instead use a LOCK and queue for other msgs instead
		inState = true;
//		if (currentState.accepts(msg.type))		//may not need this check
//			{currentState.update(msg);}		
		inState = false;
		}

}
