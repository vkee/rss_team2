package StateMachine;

import java.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.HashMap;

import Challenge.GrandChallengeMap;
import MotionPlanning.GoalAdjLists;
import MotionPlanning.MultiRRT;
import MotionPlanning.RRT;

import org.ros.message.rss_msgs.*;
import org.ros.message.MessageListener;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;
import org.ros.node.parameter.ParameterTree;
import org.ros.node.NodeMain;
import org.ros.namespace.GraphName;
import org.ros.node.Node;
import org.ros.message.lab5_msgs.*;
import org.ros.message.lab6_msgs.*;
import org.ros.message.Challenge_msgs.*;

/**
 * This class is the root node for all listeners and publishers and offers the control flow for the state machine. 
 * It is also the repository for any shared state variables
 */
public class FSM implements NodeMain{

	public enum stateENUM {
		INITIALIZE, SCAN, WNCLOSE, MOVEFORWARD, OPENGATE, APPROACHBLOCK, BLOCKCOLLECTED, CHECKTIME, RRTUPDATE, CSPACE, ORIENTDEPOSIT, VISUALSERVOCOLLECT, WNDEPOSIT
	}

	public enum msgENUM { 
		NECK, WHEELS, BLOCK, IMAGE, GATE, DOOR
	}

	private FSMState currentState;
	private boolean inState;

	public final int TIME_LIMIT = 10*60;

	public long startTime;
	public final double RRT_TOLERANCE = 0.02;
	
	public int blocksCollected;
	public MultiRRT RRTengine;
	public Point2D.Double currentLocation;			//the current goal point we are at
	public GoalAdjLists foundPaths;

	protected String mapFileName;
	private Publisher<GUIRectMsg> guiRectPub;
	private Publisher<GUIPolyMsg> guiPolyPub;
	private Publisher<GUIEraseMsg> guiErasePub;
	private Publisher<GUIPointMsg> guiPtPub;
	private Publisher<Object> ellipsePub;
	private Publisher<Object> stringPub;
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

	public void setStartTime(){
		startTime = System.currentTimeMillis();
	}

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


	@Override
	public void onStart(Node node) {
		stringPub = node.newPublisher("gui/String",
				"Challenge_msgs/GUIStringMessage");
		ellipsePub = node.newPublisher("/gui/Ellipse",
				"Challenge_msgs/GUIEllipseMessage");
		guiRectPub = node.newPublisher("gui/Rect", "lab6_msgs/GUIRectMsg");
		guiPolyPub = node.newPublisher("gui/Poly", "lab6_msgs/GUIPolyMsg");
		guiErasePub = node.newPublisher("gui/Erase", "lab5_msgs/GUIEraseMsg");
		guiPtPub = node.newPublisher("gui/Point", "lab5_msgs/GUIPointMsg");

		// Reading in a map file whose name is set as the parameter mapFileName
		ParameterTree paramTree = node.newParameterTree();
		mapFileName = paramTree.getString(node.resolveName("~/mapFileName"));
	}
	
    @Override
    public void onShutdown(Node node) {
        if (node != null) {
            node.shutdown();
        }
    }

    @Override
    public void onShutdownComplete(Node node) {
    }

    @Override
    public GraphName getDefaultNodeName() {
        return new GraphName("rss/FSM");
    }
}
