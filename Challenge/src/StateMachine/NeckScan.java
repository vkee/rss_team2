package StateMachine;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import Challenge.Fiducial;
import MotionPlanning.RRT;
import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;

/**
 * This state turns the neck all around taking snapshots of the world at different angles
 */
public class NeckScan implements FSMState {
    //TODO: make servos rotate

    private FSM fsm;	

    public NeckScan(FSM stateMachine)
    {
        fsm = stateMachine;

        //init any variables for this state

    }	


    public stateENUM getName()
    {return stateENUM.SCAN;}


    public boolean accepts(msgENUM msgType)
    {
        if (msgType == msgENUM.SERVO) return true;
        if (msgType == msgENUM.IMAGE) return true;
        return false;
    }


    public void update(GenericMessage msg)
    {

        //	    Particle Filter Measurement Update
        //	    TODO: determine which fiducials are in the fov and get the distances to them
        //        //                    Determining which fiducials are in the FOV of the robot
        //        ArrayList<Integer> measuredFiducials = getFidsInFOV(pt);
        //                            System.out.println("Num of fids in FOV: " + measuredFiducials.size());
        //        //                    Determining the distances to the fiducials that are in the FOV of the robot
        //        HashMap<Integer, java.lang.Double> measuredDists = getFidsDists(fsm.prevPoint, measuredFiducials);
        //
        //        fsm.particleFilter.measurementUpdate(measuredFiducials, measuredDists);

        //if condition to leave state
        //		fsm.updateState(new NextState(fsm));

    }
    
    /**
     * Determines the distances from the robot to the fiducials in the robot's FOV
     * @param robotPos the robot's current position
     * @param measuredFiducials the indices of the fiducials in the robot's FOV
     * @return the distances from the robot's position to the fiducials
     */
    private HashMap<Integer, java.lang.Double> getFidsDists(Point2D.Double robotPos, ArrayList<Integer> measuredFiducials) {
        HashMap<Integer, java.lang.Double> fidsDists = new HashMap<Integer, java.lang.Double>();
        Fiducial[] fiducials = fsm.particleFilter.map.getFiducials();
        for (Integer index : measuredFiducials) {
            Point2D.Double fidPos = fiducials[index].getPosition();

            //            Potential bug site is if robot position at 0,0 and map goes negative, 
            //            but this should be able to account for it in this ordering
            double dist = RRT.getDist(robotPos.x, robotPos.y, fidPos.x, fidPos.y);
            //            System.out.println("Distance to Fiducial " + index + " at " + fidPos + " is " + dist);
            fidsDists.put(index, dist);
        }

        return fidsDists;
    }


    @Override
    public void onStart() {
        // TODO Auto-generated method stub

    }


}