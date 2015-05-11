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
import VisualServo.*;

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
        float[] depth_float_array = null;
        List<FiducialObject> detectedFids = new ArrayList<FiducialObject>();

        // Making the Scans

        // Forward and Backward
        int dir = 1;
        for (int i = 0; i >= 0; i+=dir) {
            List<BlobObject> detectedBlobs = new ArrayList<BlobObject>();
            fsm.neckServo.goToSettingOne(i);
            try {
                Thread.sleep(1500);
            } catch (Exception e) {
            }
            for (int j = 0; j < 3 ; j++) {
            	try{
                src = cl.getImage();
                depth_float_array = cl.getDepthImage();
    			Image temp = new Image(src);
    			Image.filterImage(src, temp, depth_float_array, 640, 480,
    					3.048, 4.6419, 0.6, 0.6, 0, .1);

    			Image dest = new Image(temp);

                blobTrack.apply(temp, dest, depth_float_array);

                org.ros.message.sensor_msgs.Image pubImage = new org.ros.message.sensor_msgs.Image();
                Image.fillImageMsg(pubImage, dest);
                
                fsm.vidPub.publish(pubImage);
                detectedBlobs.addAll(blobTrack.getBlobs(src, depth_float_array));
            	}catch(Exception e){
            	}
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }
            }
           // System.out.println(detectedBlobs.size());
            detectedFids.addAll(blobTrack.sortBlobs(detectedBlobs));
            // detectedFids.addAll(blobTrack.getFiducials(src, depth_array));

            if (i==3) dir = -1;

        }

        /*// Backward
		for (int i = 3; i >= 0; i--) {
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
			}
			fsm.neckServo.goToSettingOne(i);
			src = cl.getImage();
			depth_array = cl.getDepthImage();
			Image dest = new Image(src);
			blobTrack.apply(src, dest, depth_array);
			org.ros.message.sensor_msgs.Image pubImage = new org.ros.message.sensor_msgs.Image();
			Image.fillImageMsg(pubImage, dest);
			fsm.vidPub.publish(pubImage);
			detectedFids.addAll(blobTrack.getFiducials(src, depth_array));
			// try {
			// Thread.sleep(3000);
			// } catch (Exception e) {
			// }
		}*/

        if (detectedFids.size() == 0) {
            System.out.println("No Fids Detected");
        } else {
        	
            HashMap<Integer, Double> totalFidDist = new HashMap<Integer, Double>();
            HashMap<Integer, Integer> numMeasurements = new HashMap<Integer, Integer>();

            // Computing the number of measurements and distances
            for (FiducialObject fids : detectedFids) {
                int fidIndex = fids.getFiducialNumber();
                double fidDist = fids.getDistanceTo();
                // Updating the distances
                if (totalFidDist.containsKey(fidIndex)) {
                    totalFidDist
                    .put(fidIndex, totalFidDist.get(fidIndex) + fidDist);
                } else {
                    totalFidDist.put(fidIndex, fidDist);
                }

                // Updating the number of measurements
                if (numMeasurements.containsKey(fidIndex)) {
                    numMeasurements
                    .put(fidIndex, numMeasurements.get(fidIndex) + 1);
                } else {
                    numMeasurements.put(fidIndex, 1);
                }
            }

            ArrayList<Integer> measuredFids = new ArrayList<Integer>();
            HashMap<Integer, Double> measuredDists = new HashMap<Integer, Double>();

            // Averaging the distances
            for (Integer index : totalFidDist.keySet()) {
                double avgDist = totalFidDist.get(index)
                        / numMeasurements.get(index) + .110; // adding .110 m b/c accounting for ball radius
                measuredDists.put(index, avgDist);
            }

            // Determining the distances to the fiducials that are in the FOV of the
            // robot
            fsm.particleFilter.measurementUpdate(measuredFids, measuredDists);

            RobotParticle particle = fsm.particleFilter.sampleParticle();

            System.out.println("Prev Stored Pt: X-" + fsm.prevPt.x + " Y-"
                    + fsm.prevPt.y);
            System.out.println("Sampled Particle Position: X-" + particle.getX()
                    + " Y-" + particle.getY());

           // fsm.updateODO(
           //         particle.getX() - fsm.CAMERA_X_POS * Math.cos(fsm.robotTheta)-fsm.prevPt.x,
           //         particle.getY() - fsm.CAMERA_X_POS * Math.sin(fsm.robotTheta)-fsm.prevPt.y);
        }
        fsm.updateState(new WaypointNavClose(fsm));
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
    }
}
