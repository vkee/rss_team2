package StateMachine;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import Challenge.ConstructionObject;
import Challenge.GrandChallengeMap;
import Localization.ParticleFilter;
import MotionPlanning.CSpace2D;
import MotionPlanning.CSpace3D;
import MotionPlanning.GoalAdjLists;
import MotionPlanning.MultiRRT2D;
import MotionPlanning.MultiRRT3D;
//import MotionPlanning.MultiRRT3D;
import MotionPlanning.PolygonObstacle;
import MotionPlanning.RRT3DSmoother;
import MotionPlanning.RRTreeNode;
import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

import org.ros.message.rss_msgs.*;
import org.ros.message.lab5_msgs.*;
import org.ros.message.lab6_msgs.*;
import org.ros.message.Challenge_msgs.*;
/**
 * This state initializes the robot to the starting positions engaging all
 * motors and servos
 */
public class Initialize implements FSMState {

	private FSM fsm;
	private boolean initialized;

	public Initialize(FSM stateMachine) {
		fsm = stateMachine;

		//try {
		GrandChallengeMap challengeMap = new GrandChallengeMap();
		try {
			challengeMap = challengeMap.parseFile(fsm.mapFileName);
		} catch (Exception e) {
			System.err.println("Unable to load map.");
			e.printStackTrace();
		}

		//            Using 2D CSpace *************************************************
		//            CSpace2D cSpace = new CSpace2D();
		//            ArrayList<PolygonObstacle> cSpaces = cSpace.generateCSpace(
		//                    challengeMap, false);
		//            challengeMap.cSpace = cSpaces;
		//            fsm.mapDrawer.displayMap(challengeMap);
		//
		//            fsm.mapDrawer.displayMapCSpace(cSpaces);
		//
		//            //			Initialize the particle filter
		//            Double robotStartPos = challengeMap.getRobotStart();
		//
		//            fsm.particleFilter = new ParticleFilter(robotStartPos.x, robotStartPos.y, 0.0, 
		//                    fsm.PARTICLE_FILTER_RADIUS, fsm.NUM_PARTICLES, challengeMap, fsm.TRANS_NOISE, 
		//                    fsm.ROT_NOISE, fsm.SENSOR_NOISE);
		//
		//            fsm.prevPt = robotStartPos;
		//
		//            ArrayList<Point2D.Double> objectLocations = new ArrayList<Point2D.Double>();
		//            for (ConstructionObject cobj : challengeMap
		//                    .getConstructionObjects()) {
		//                boolean unreachable = false;
		//                Point2D.Double loc = cobj.getPosition();
		//
		//                for (PolygonObstacle obs : cSpaces) {
		//                    if (obs.contains(loc)) {
		//                        unreachable = true;
		//                        break;
		//                    }
		//                }
		//
		//                if (!unreachable) {
		//                    objectLocations.add(loc);
		//                }
		//            }

		//            Using 3D Cspace *************************************************
		CSpace3D cSpace3D = new CSpace3D();
		ArrayList<ArrayList<PolygonObstacle>> obsCSpaces =
				cSpace3D.generateCSpace(challengeMap, false);
		challengeMap.set3DCSpace(obsCSpaces);
		fsm.mapDrawer.displayMap(challengeMap);
		fsm.map = challengeMap; 

		// 2D CSpace for checking if can reach obstacles CSpace2D
		/*CSpace2D cSpace2D = new CSpace2D();
		//            Using 2D CSpace *************************************************
		//            CSpace2D cSpace = new CSpace2D();
		//            ArrayList<PolygonObstacle> cSpaces = cSpace.generateCSpace(
		//                    challengeMap, false);
		//            challengeMap.cSpace = cSpaces;
		//            fsm.mapDrawer.displayMap(challengeMap);
		//
		//            fsm.mapDrawer.displayMapCSpace(cSpaces);
		//
		//            //			Initialize the particle filter
		//            Double robotStartPos = challengeMap.getRobotStart();
		//
		//            fsm.particleFilter = new ParticleFilter(robotStartPos.x, robotStartPos.y, 0.0, 
		//                    fsm.PARTICLE_FILTER_RADIUS, fsm.NUM_PARTICLES, challengeMap, fsm.TRANS_NOISE, 
		//                    fsm.ROT_NOISE, fsm.SENSOR_NOISE);
		//
		//            fsm.prevPt = robotStartPos;
		//
		//            ArrayList<Point2D.Double> objectLocations = new ArrayList<Point2D.Double>();
		//            for (ConstructionObject cobj : challengeMap
		//                    .getConstructionObjects()) {
		//                boolean unreachable = false;
		//                Point2D.Double loc = cobj.getPosition();
		//
		//                for (PolygonObstacle obs : cSpaces) {
		//                    if (obs.contains(loc)) {
		//                        unreachable = true;
		//                        break;
		//                    }
		//                }
		//
		//                if (!unreachable) {
		//                    objectLocations.add(loc);
		//                }
		//            }

		//            Using 3D Cspace *************************************************
		CSpace3D cSpace3D = new CSpace3D();
		ArrayList<ArrayList<PolygonObstacle>> obsCSpaces =
				cSpace3D.generateCSpace(challengeMap, false);
		challengeMap.set3DCSpace(obsCSpaces);
		fsm.mapDrawer.displayMap(challengeMap);
		fsm.mapDrawer.displayMapCSpace(obsCSpaces.get(0));

		// 2D CSpace for checking if can reach obstacles CSpace2D
		/*CSpace2D cSpace2D = new CSpace2D();
			ArrayList<PolygonObstacle> obs2DCSpaces = cSpace2D.generateCSpace(challengeMap, false);
			fsm.mapDrawer.displayMapCSpace(obs2DCSpaces);

			ArrayList<Point2D.Double> objectLocations = new ArrayList<Point2D.Double>(); 
			Point2D.Double start = challengeMap.getRobotStart(); 
			Point2D.Double end = challengeMap.getRobotGoal();

			for (ConstructionObject cobj : challengeMap.getConstructionObjects()){
				boolean unreachable = false; 
				Point2D.Double loc = cobj.getPosition();

				// System.out.println(obsCSpaces.get(0).size());

				for (PolygonObstacle obs : obs2DCSpaces) { //TODO only the 0degree now obs2DCSpaces 
					if (obs.contains(loc)||loc.equals(start)){ 
						unreachable = true;
						break; 
					} 
				}

				if (!unreachable){ 
					objectLocations.add(loc);
				}
			}*/

		// NO HUERISTIC CHECK ************************************************

		ArrayList<Point2D.Double> objectLocations = new ArrayList<Point2D.Double>(); 
		Point2D.Double start = challengeMap.getRobotStart(); 
		Point2D.Double end = challengeMap.getRobotGoal();
		for (ConstructionObject cobj : challengeMap.getConstructionObjects())
		{objectLocations.add(cobj.getPosition());}

		System.out.println("Num locs: " + objectLocations.size());

		fsm.mapDrawer.displayCObj(challengeMap.getConstructionObjects());

		fsm.currentLocation = start;
		objectLocations.add(end);
		fsm.prevPt = start;
		fsm.particleFilter = new ParticleFilter(start.x, start.y, 0.0, 
				fsm.PARTICLE_FILTER_RADIUS, fsm.NUM_PARTICLES, challengeMap, fsm.TRANS_NOISE, 
				fsm.ROT_NOISE, fsm.SENSOR_NOISE);
		System.out.println("Start Publish");

		/// publishing the real start position
		//OdometryMsg msg = new OdometryMsg();
		//msg.x = start.x;
		//msg.y = start.y;
		//msg.theta = 0;
		//fsm.odometryPub.publish(msg);
		fsm.updateODO(start.x, start.y);

		System.out.println("Published");

		fsm.RRTengine = new MultiRRT3D(challengeMap);
		fsm.foundPaths = new GoalAdjLists(end);
		RRT3DSmoother smoother = new RRT3DSmoother(fsm.RRTengine);

		Point2D.Double currLocation = start;
		while (objectLocations.size() > 0) {
			/*System.out.println("starting loc " + currLocation);
				System.out.println("Printing locs");
				for (Point2D.Double locs : objectLocations) {
					System.out.println(locs);
				}*/
			RRTreeNode[] pathEnds = fsm.RRTengine.getPaths(currLocation,
					objectLocations, fsm.RRT_TOLERANCE);

			// Prints out the path to the MapGUI
			for (RRTreeNode r : pathEnds) {
				if (r == null)
					continue;
			}

			for (int i = 0; i < pathEnds.length; i++) {
				double dist;
				ArrayList<Point2D.Double> path;
				if (pathEnds[i] != null) {
					// objectLocations.remove(i); // if path was not found,
					// remove it from possible
					// locations
					dist = pathEnds[i].distFromRoot;
					path = pathEnds[i].pathFromParent();
					path = smoother.smoothPath(path);
					fsm.mapDrawer.outputPath(path);

				}
				else 
				{dist = java.lang.Double.MAX_VALUE;
				path = null;}

				fsm.foundPaths.addBiPath(currLocation,
						objectLocations.get(i), path, dist);
			}

			currLocation = objectLocations.remove(0);
		}



		initialized = true;

	}


	public stateENUM getName() {
		return stateENUM.INITIALIZE;
	}

	public boolean accepts(msgENUM msgType) {
		// if (msgType == msgENUM.WHEELS) return true;
		if (msgType == msgENUM.SERVO) {
			return false;
		}
		if (msgType == null && initialized) {
			return true;
		}
		return false;
	}

	public void update(GenericMessage msg) {
		// do stuff
		/*System.out.println("Hi. I'm a robot.");
		System.out.println("My current state is: Initialize.");


		// get gate PWM value from arm message
		ArmMsg message = (ArmMsg)msg.message;
		int gatePWM = (int) message.pwms[1]; // convert from long to int

		// if gate is not closed, close gate
		if (!fsm.gateServo.isClosed(gatePWM))
		{
			int[] messagePWMs = new int[3];
			messagePWMs[0] = (int) message.pwms[0]; // convert from long to int
			messagePWMs[1] = (int) message.pwms[1]; // convert from long to int
			messagePWMs[2] = (int) message.pwms[2]; // convert from long to int
			fsm.gateServo.close(messagePWMs);
			//  System.out.println("CurrPWM: "+gatePWM);
			System.out.println("Trying to close gate.");
		}
		else //if condition to leave state
		{
			System.out.println("Done closing gate.");
			//fsm.updateState(new MoveForward(fsm));
		}


		// if condition to leave state
		System.out.println("Initialization complete. TIME TO GO COLLECT SOME BLOCKS!");
		System.out.println("ARE YOU EXCITED?");
		System.out.println("I'M EXCITED!!!");*/
		fsm.updateState(new WaypointNavClose(fsm));
	}

	@Override
	public void onStart() {
	}
}
