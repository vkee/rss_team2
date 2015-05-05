package Servoing;

import org.ros.node.topic.Publisher;
import org.ros.message.rss_msgs.*;

public class ServoController {
    protected int       MIN_PWM;
    protected int       MAX_PWM;
    protected double    THETA_RANGE;          // in radians
    public int       PWM_0;                // PWM value at the 0 radians position
    public int       PWM_180;              // PWM value at the PI/2  position
    protected double    LINE_SLOPE;           // slope of the line between PWM_90 and PWM_0
    protected double    LINE_THETA_INTERCEPT; // intercept of the line between PWM_90 and PWM_0
    protected int       MAX_PWM_CHANGE;       // the largest PWM change in value that can be safety written to the servo (for 1 radian of rotation)
    protected int       SHIFT_AMOUNT = 10;    // number of divisions of the rotation
    private Publisher<ArmMsg> armPWMPub;
    public int messageIndex;
 
    
    public ServoController(int minPWM, int maxPWM, double thetaRange, int pwm0, int pwm180, Publisher<ArmMsg> armPWMPub) {
        this.MIN_PWM = minPWM;
        this.MAX_PWM = maxPWM;
        this.PWM_0   = pwm0;
        this.PWM_180 = pwm180;
        this.THETA_RANGE = thetaRange;
        this.LINE_SLOPE = (-Math.PI/2)/(PWM_180 - PWM_0);
        this.LINE_THETA_INTERCEPT = 0 - LINE_SLOPE*PWM_0;
        this.MAX_PWM_CHANGE = (int) (1/THETA_RANGE * (MAX_PWM - MIN_PWM));
        this.armPWMPub = armPWMPub;
    }

    public double getThetaDeg(double PWM) {
        return 180/Math.PI*(LINE_SLOPE*PWM + LINE_THETA_INTERCEPT);
    }

    public double getThetaRad(double PWM) {
        return (LINE_SLOPE*PWM + LINE_THETA_INTERCEPT);
    }

    /**
     * Computes the PWM value to move to the desired angle (in the joint's reference frame)
     * It uses the formula Theta_desired = LINE_SLOPE*PWM_desired + Theta_intercept,
     * simply computing PWM_desired = (Theta_desired - Theta_intercept)/LINE_SLOPE
     * Note that the PWM value must be between MIN_PWM and MAX_PWM (o/w MIN_PWM will be returned 
     * (as an arbitrary value or one outside of the safe operating range may cause issues))
     * @param desiredTheta
     * @return
     */
    public int getPWM(double desiredTheta) {
        int desiredPWM = (int) ((desiredTheta - LINE_THETA_INTERCEPT)/LINE_SLOPE);
        if (desiredPWM < MIN_PWM){
            System.out.println("Desired PWM value is less than the minimum PWM value. " + desiredPWM);
            return MIN_PWM;
        } else if (desiredPWM > MAX_PWM) {
            System.out.println("Desired PWM value is more than the max PWM value." + desiredPWM);
            return MIN_PWM;
        } else {
            return desiredPWM;
        }
    }

    public boolean atTarget(double angle, int[] currentPWMs)
    {return getPWM(angle)==currentPWMs[messageIndex];}

    public boolean atTargetPWM(int target, int[] currentPWMs)
    {return target==currentPWMs[messageIndex];}

    /**
     * Computes the PWM to write to the servo taking into the constraint of not moving more than 1 radian per control step
     * @param currPWM the last PWM value written to the servo
     * @param desPWM the PWM value desired that the servo ends up at
     * @return the PWM value to be written to the servo
     */
    public int getSafePWM(int currPWM, int desPWM) {
        int correction = desPWM - currPWM;

        //      May need to take the min of the correction and the max pwm value, and then the max of that and the min pwm value
        if (correction > 0) {
            correction = Math.min(correction, MAX_PWM_CHANGE);
        } else {
            correction = -Math.min(-correction, MAX_PWM_CHANGE);
        }
        
        int corrected = currPWM + correction;
        if (Math.signum(desPWM-corrected) != Math.signum(correction))		//passed it
        	corrected = desPWM;

        return Math.max(Math.min(corrected, MAX_PWM), MIN_PWM);
    }

    /**
     * Computes the PWM to write to the servo taking into the constraint of not moving more than 1 radian per control step
     * @param currPWM the last PWM value written to the servo
     * @param up whether the servo is moving up
     * @return the PWM value to be written to the servo
     */
    public int fullRotation(int currPWM, boolean up) {
        //        int correction;
        if (up) {
            return Math.max(Math.min(currPWM + (MAX_PWM - MIN_PWM)/SHIFT_AMOUNT, MAX_PWM), MIN_PWM);
            //            correction = Math.min(MAX_PWM - currPWM, MAX_PWM_CHANGE);
            //            return currPWM + correction;
        } else {
            return Math.min(Math.max(currPWM - (MAX_PWM - MIN_PWM)/SHIFT_AMOUNT, MIN_PWM), MAX_PWM);
            //            correction = Math.min(currPWM - MIN_PWM, MAX_PWM_CHANGE);
            //            return currPWM - correction;
        }
    }

    public int rotateTo(double angle, int[] currPWMs) {
        //        int correction;
        int diff = (getPWM(angle)-currPWMs[messageIndex]);
        if (diff!=0)
        	{int direction = diff/Math.abs(diff);
        	return Math.max(Math.min(currPWMs[messageIndex] + direction*(MAX_PWM - MIN_PWM)/SHIFT_AMOUNT, MAX_PWM), MIN_PWM);}
        else return currPWMs[messageIndex];
    } 


    public int rotateToPWM(int target, int[] currPWMs) {
        int diff = (target-currPWMs[messageIndex]);
        if (diff!=0){
            int direction = diff/Math.abs(diff);
            return Math.max(Math.min(currPWMs[messageIndex] + direction*(MAX_PWM - MIN_PWM)/SHIFT_AMOUNT, MAX_PWM), MIN_PWM);
        } else {
            return currPWMs[messageIndex];
        }
    } 

    /**
     * Returns whether the servo is at the minimum position
     * @param currPWM
     * @return
     */
    public boolean atMin(int currPWM) {
        return currPWM == MIN_PWM;
    }

    /**
     * Returns whether the servo is at the maximum position
     * @param currPWM
     * @return
     */
    public boolean atMax(int currPWM) {
        return currPWM == MAX_PWM;
    }
    
	/**
	 * Sends PWM commands to neck and gate servos by publishing 
	 * an ArmMsg with desired PWM commands to the armPWMPub 
	 * associated with the ServoController instance
	 * @param botNeck desired PWM value to command to bottom neck servo
	 * @param gate desired PWM value to command to gate servo
	 * @param topNeck desired PWM value to command to top neck servo
	 */
    public void sendPWM(int botNeck, int gate, int topNeck) {

        ArmMsg msg = new ArmMsg();
        msg.pwms[0] = botNeck;
        msg.pwms[1] = gate;
        msg.pwms[2] = topNeck;
        armPWMPub.publish(msg);

        // System.out.println("message sent");
        // System.out.println(Arrays.toString(pwm_stat));
    }
    
    public static int[] messageConvert(long[] origMsg){
    	int[] messagePWMs = new int[3];
		messagePWMs[0] = (int) origMsg[0]; // convert from long to int
		messagePWMs[1] = (int) origMsg[1]; // convert from long to int
		messagePWMs[2] = (int) origMsg[2]; // convert from long to int
    	return messagePWMs;
    }
}