package StateMachine;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import Challenge.ConstructionObject;
import Challenge.GrandChallengeMap;
import MotionPlanning.CSpace;
import MotionPlanning.GoalAdjLists;
import MotionPlanning.MultiRRT;
import MotionPlanning.PolygonObstacle;
import MotionPlanning.RRTreeNode;
import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

/**
 * This state initializes the robot to the starting positions engaging all motors and servos
 */
public class Initialize implements FSMState {

    private FSM fsm;	
    private boolean initialized;

    public Initialize(FSM stateMachine){
        fsm = stateMachine;

        try {
            GrandChallengeMap challengeMap = null;
            try{
                challengeMap = GrandChallengeMap.parseFile(fsm.mapFileName);
            } catch (Exception e) {
                System.err.println("Unable to load map.");
                e.printStackTrace();
            }
            CSpace cSpace = new CSpace(); 
            ArrayList<ArrayList<PolygonObstacle>> obsCSpaces = cSpace.generateCSpace(challengeMap, false);
            challengeMap.set3DCSpace(obsCSpaces);

            ArrayList<Point2D.Double> objectLocations = new ArrayList<Point2D.Double>();
            for (ConstructionObject cobj : challengeMap.getConstructionObjects()){
                boolean unreachable = false;
                Point2D.Double loc = cobj.getPosition();

                for (PolygonObstacle obs : obsCSpaces.get(0)) {		//TODO only the 0degree now
                    if (obs.contains(loc)){
                        unreachable = true;
                        break;
                    }
                }

                if (!unreachable) objectLocations.add(loc);
            }

            Point2D.Double start = challengeMap.getRobotStart();
            Point2D.Double end = challengeMap.getRobotGoal();

            objectLocations.add(end);

            fsm.RRTengine =  new MultiRRT(challengeMap);
            fsm.foundPaths = new GoalAdjLists(end);

            Point2D.Double currLocation = start;
            while (objectLocations.size() > 1)
            {
                RRTreeNode[] pathEnds = fsm.RRTengine.getPaths(currLocation, objectLocations, fsm.RRT_TOLERANCE);

                for (int i=0; i<pathEnds.length; i++)
                {
                    if (pathEnds[i] == null)
                    {objectLocations.remove(i);			//if path was not found, remove it from possible locations
                    continue;}
                    double dist = pathEnds[i].distFromRoot;
                    ArrayList<Point2D.Double> path = pathEnds[i].pathFromParent();
                    fsm.foundPaths.addBiPath(currLocation, objectLocations.get(i), path, dist);
                }

                currLocation = objectLocations.remove(0);
            }			
            initialized = true;

        } catch (Exception e) {
            System.err.println("Error in Initialize.");
            e.printStackTrace();
        }
    }	

    public stateENUM getName(){
        return stateENUM.INITIALIZE;
    }


    public boolean accepts(msgENUM msgType){
        //if (msgType == msgENUM.WHEELS) return true;
        if (msgType == null && initialized){
            return true;
        }
        return false;
    }


    public void update(GenericMessage msg){
        //do stuff

        //if condition to leave state
        fsm.updateState(new WaypointNavClose(fsm));
    }
}