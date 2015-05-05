package StateMachine;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Challenge.Fiducial;
import Localization.RobotParticle;
import MotionPlanning.RRT;
import Servoing.ServoController;
import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;
import VisualServo.FiducialObject;
import VisualServo.FiducialTracking;
import VisualServo.Image;
import VisualServo.MultipleBlobTracking;
import VisualServo.client;

import org.ros.message.rss_msgs.*;
import org.ros.message.lab5_msgs.*;
import org.ros.message.lab6_msgs.*;
import org.ros.message.Challenge_msgs.*;

/**
 * This state turns the neck all around taking snapshots of the world at
 * different angles
 */
public class NeckScan implements FSMState {

	// TODO: make servos rotate

	private FSM fsm;
	private double neckAngleTarget = 0;
	private final int NECKSTATES = 12;
	protected FiducialTracking blobTrack = null;
	client cl = null;

	int debug_count;

	private ArrayList<Point2D.Double> newGoals;

	public NeckScan(FSM stateMachine) {
		fsm = stateMachine;
		newGoals = new ArrayList<Point2D.Double>();
		blobTrack = new FiducialTracking();
		cl = new client();

		// init any variables for this state
		debug_count = 0;
	}

	public stateENUM getName() {
		return stateENUM.SCAN;
	}

	public boolean accepts(msgENUM msgType) {
		if (msgType == msgENUM.SERVO)
			return true;
		return false;
	}

	public void update(GenericMessage msg) {
		Image src = null;
		float[] depth_array = null;
		List<FiducialObject> detectedFids = new ArrayList<FiducialObject>();

//		Making the 
		
		for (int i = 0; i < 3; i++) {
			fsm.neckServo.goToSettingOne(i);
			src = cl.getImage();
			depth_array = cl.getDepthImage();
			Image dest = new Image(src);
			blobTrack.apply(src, dest, depth_array);
			org.ros.message.sensor_msgs.Image pubImage = new org.ros.message.sensor_msgs.Image();
			Image.fillImageMsg(pubImage, dest);
			fsm.vidPub.publish(pubImage);
			detectedFids.addAll(blobTrack.getFiducials(src, depth_array));
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
			}
		}

		for (int i = 3; i >= 0; i--) {
			fsm.neckServo.goToSettingOne(i);
			src = cl.getImage();
			depth_array = cl.getDepthImage();
			Image dest = new Image(src);
			blobTrack.apply(src, dest, depth_array);
			org.ros.message.sensor_msgs.Image pubImage = new org.ros.message.sensor_msgs.Image();
			Image.fillImageMsg(pubImage, dest);
			fsm.vidPub.publish(pubImage);
			detectedFids.addAll(blobTrack.getFiducials(src, depth_array));
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
			}
		}

		System.out.println(debug_count);
		debug_count++;
		ArrayList<Integer> measuredFids = new ArrayList<Integer>();

		for (FiducialObject fids : detectedFids) {
			System.out.println("Distance to " + fids.getFiducialNumber()
					+ " is " + fids.getDistanceTo() + " m");
			measuredFids.add(fids.getFiducialNumber());
		}

		if (detectedFids.size() == 0) {
			System.out.println("No Fids Detected");
		}
		// fsm.neckServo.fullCycle(false); // go back

		// // Determining the distances to the
		// // fiducials that are in the FOV of the robot
//		 HashMap<Integer, java.lang.Double> measuredDists = fsm.particleFilter.getFidsDists(fsm.prevPt, fsm.map, measuredFids);
//		 fsm.particleFilter.measurementUpdate(measuredFids, measuredDists);
//		
//		 RobotParticle particle = fsm.particleFilter.sampleParticle();
//		
//		 System.out.println("Prev Stored Pt: X-" + fsm.prevPt.x + " Y-" +
//		 fsm.prevPt.y);
//		 System.out.println("Sampled Particle Position: X-" + particle.getX()
//		 + " Y-" + particle.getY());
//
//		 fsm.updateODO(particle.getX() - fsm.CAMERA_X_POS*Math.cos(fsm.robotTheta), particle.getY() - fsm.CAMERA_X_POS*Math.sin(fsm.robotTheta));
		//
		 fsm.updateState(new WaypointNavClose(fsm));
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
	}
}
