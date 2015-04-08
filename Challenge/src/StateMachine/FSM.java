package StateMachine;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import MotionPlanning.CSpace;

/**
 * This class is the root node for all listeners and publishers and offers the control flow for the state machine. It is also the repository for any shared state variables
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
	
	public int startTime;
	public int blocksCollected;
	public CSpace map;
	public HashMap<Integer, Point2D.Double> goals;
	public HashMap<Integer, ArrayList<Double>> rrtDistances;				//sorted by shortest distance adjcentcy lists of distances used to update below
	public HashMap<Integer, ArrayList<ArrayList<Point2D.Double>>> rrtPaths;				//sorted by shortest distance adjcentcy lists of paths 
	public int currentLocation;			//the id of the current goal point we are at
	public HashSet<Integer> visited;
	

	// public wheels publishers
	// public servos publishers

	public FSM()
		{
		
		//initialize publishers

		currentState = new Initialize(this);
		inState = false;

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
		if (currentState.accepts(msg.type))		//may not need this check
			{currentState.update(msg);}		
		inState = false;
		}

}
