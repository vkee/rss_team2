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

	private ArrayList<Point2D.Double> newGoals;

	public NeckScan(FSM stateMachine) {
		fsm = stateMachine;
		newGoals = new ArrayList<Point2D.Double>();
		blobTrack = new FiducialTracking();
		cl = new client();

		// init any variables for this state

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

		// ArmMsg message = (ArmMsg) msg.message;
		// int[] pwms = ServoController.messageConvert((message).pwms);

		//		//for (int i = 0; i < 6; i++) {
		//		//fsm.neckServo.goToSetting(i);
		//		src = cl.getImage();
		//		depth_array = cl.getDepthImage();
		//		detectedFids.addAll(blobTrack.getFiducials(src, depth_array));
		//		try{Thread.sleep(2000);}catch(Exception e){}
		//		//}

		for (int i = 0; i < 3; i++) {
			fsm.neckServo.goToSettingOne(i);
			src = cl.getImage();
			depth_array = cl.getDepthImage();
			detectedFids.addAll(blobTrack.getFiducials(src, depth_array));
			try{Thread.sleep(2000);}catch(Exception e){}
		}

		ArrayList<Integer> measuredFids = new ArrayList<Integer>();

		for (FiducialObject fids : detectedFids) {
			System.out.println("Distance to " + fids.getFiducialNumber() + " is "
					+ fids.getDistanceTo() + " m");
			measuredFids.add(fids.getFiducialNumber());
		}

		if(detectedFids.size() == 0){
			System.out.println("No Fids Detected");
		}
//		fsm.neckServo.fullCycle(false); // go back
		fsm.neckServo.fullCycleOne(false);// go back

//		// Determining the distances to the
//		// fiducials that are in the FOV of the robot 
//		HashMap<Integer, java.lang.Double> measuredDists = fsm.particleFilter.getFidsDists(fsm.prevPt, fsm.map, measuredFids); // //
//		fsm.particleFilter.measurementUpdate(measuredFids, measuredDists);
//
//		RobotParticle particle = fsm.particleFilter.sampleParticle();
//
//		System.out.println("Prev Stored Pt: X-" + fsm.prevPt.x + " Y-" + fsm.prevPt.y);
//		System.out.println("Sampled Particle Position: X-" + particle.getX() + " Y-" + particle.getY());
//		TODO: need to have the robot's orientation b/c cannot simply subtract the camera x pos
//		fsm.updateODO(particle.getX() - fsm.CAMERA_X_POS, particle.getY());
//
//		fsm.updateState(new WaypointNavClose(fsm));


		//fsm.updateState(null);

		// double[] angles = fsm.neckServo.bottomAndTopAngle(neckAngleTarget);
		// if (fsm.neckServo.top.atTarget(angles[1], pwms))
		// {System.out.print("top done");
		// if (fsm.neckServo.bottom.atTarget(angles[0], pwms))
		// {System.out.print("bottom done");}
		// else
		// {int pwmBot = fsm.neckServo.bottom.rotateTo(angles[0], pwms);
		// fsm.neckServo.top.sendPWM(pwmBot,pwms[1],pwms[0]);}}
		// else
		// {int pwmTop= fsm.neckServo.top.rotateTo(angles[1], pwms);
		// fsm.neckServo.top.sendPWM(pwms[2],pwms[1],pwmTop);
		// System.out.println("Going bottom to: "+
		// fsm.neckServo.bottom.getPWM(angles[0])+
		// " from "+
		// message.pwms[2]);}

		//
		//
		// if (fsm.neckServo.atAngle(neckAngleTarget, pwms)) { //process image
		// System.out.println("Processing image at: "+neckAngleTarget);
		// try{
		// // TODO: this should be changed to boolean flags that the visual
		// processing stuff updates
		// Thread.sleep(2000); //update fiducial and goal lists
		// }catch(Exception e){
		//
		// }
		// double newTarget = neckAngleTarget + 2*Math.PI/NECKSTATES;
		// System.out.println(newTarget);
		// if (newTarget < 2*Math.PI){
		// neckAngleTarget = newTarget;
		// } else {}//fsm.updateState(new RRTUpdate(fsm, newGoals)); //TODO:
		// insert arraylist of new goalpoint 2ds
		// } else
		// {
		// fsm.neckServo.goToAngle(neckAngleTarget, pwms);
		//
		// System.out.println("Going bottom to: "+
		// fsm.neckServo.bottom.getPWM(fsm.neckServo.bottomAndTopAngle(neckAngleTarget)[0])+
		// " from "+
		// message.pwms[fsm.neckServo.bottom.messageIndex]);

		// }
		// }

		// TODO Most likely want to be running the below in each of the neck
		// servo positions
		// after done taking all the shots and processing them, want to update
		// the particle filter

		//
		// ArrayList<Integer> measuredFiducials = new ArrayList<Integer>();
		// // Determining which fiducials are in the robot's field of view
		// for (FiducialObject fid : detectedFids) {
		// measuredFiducials.add(fid.getFiducialNumber());
		// }

		//
		//
		// // Particle Filter Measurement Update // TODO: determine which
		// fiducials are in the fov and get the distances to them // //
		// Determining which fiducials are in the FOV of the robot //
		// ArrayList<Integer> measuredFiducials = getFidsInFOV(pt); //
		// System.out.println("Num of fids in FOV: " +
		// measuredFiducials.size()); // // Determining the distances to the
		// fiducials that are in the FOV of the robot // HashMap<Integer,
		// java.lang.Double> measuredDists = getFidsDists(fsm.prevPoint,
		// measuredFiducials); // //
		// fsm.particleFilter.measurementUpdate(measuredFiducials,
		// measuredDists);
		//
		// //TODO: actually update odometery to reflect the updated
		// localization??
		//
		// //if condition to leave state //
		// System.out.println("Updating state...");
		// fsm.updateState(new NextState(fsm));

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
	}
}