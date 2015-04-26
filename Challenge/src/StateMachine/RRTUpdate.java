package StateMachine;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import MotionPlanning.RRTreeNode;
import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

/**
 * This state kicks off the extra RRT searches for new goals
 */
public class RRTUpdate implements FSMState {


	private FSM fsm;	

	public RRTUpdate(FSM stateMachine, ArrayList<Point2D.Double> newMaterialLocations)
		{
		fsm = stateMachine;
		ArrayList<Point2D.Double> unvis = fsm.foundPaths.getUnvisited();
		
		for (Point2D.Double newMatLoc: newMaterialLocations)
			{
			RRTreeNode[] endpoints = fsm.RRTengine.getPaths(newMatLoc, unvis, fsm.RRT_TOLERANCE);
			for (int i=0; i<endpoints.length; i++)
				{fsm.foundPaths.addBiPath(newMatLoc, unvis.get(i), endpoints[i].pathFromParent(), endpoints[i].distFromRoot);}
			}

		//init any variables for this state

		}	

	
	public stateENUM getName()
		{return stateENUM.RRTUPDATE;}

	
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