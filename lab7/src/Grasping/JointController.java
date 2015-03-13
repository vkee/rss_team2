package Grasping;

public class JointController {
    protected int MIN_PWM;
    protected int MAX_PWM;
    protected double THETA_RANGE; // in radians
    protected int PWM_0; // PWM value at the 0 radians position
    protected int PWM_90; // PWM value at the PI/2 or 90 deg position
    protected double LINE_SLOPE; // slope of the line between PWM_90 and PWM_0
    protected double LINE_THETA_INTERCEPT; // intercept of the line between PWM_90 and PWM_0

    public JointController(int minPWM, int maxPWM, double thetaRange, int pwm0, int pwm90) {
        this.MIN_PWM = minPWM;
        this.MAX_PWM = maxPWM;
        this.PWM_0 = pwm0;
        this.PWM_90 = pwm90;
        this.LINE_SLOPE = (Math.PI/2)/(PWM_90 - PWM_0);
        this.LINE_THETA_INTERCEPT = 0 - LINE_SLOPE*PWM_90;
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
            System.out.println("Desired PWM value is less than the minimum PWM value.");
            return MIN_PWM;
        } else if (desiredPWM > MAX_PWM) {
            System.out.println("Desired PWM value is more than the max PWM value.");
            return MIN_PWM;
        } else {
            return desiredPWM;
        }
    }

    /**
     * Computes the PWM to write to the servo taking into the constraint of not moving more than 1 radian per control step
     * @param currPWM the last PWM value written to the servo
     * @param up whether the servo is moving up
     * @return the PWM value to be written to the servo
     */
    public int fullRotation(int currPWM, boolean up) {
        int correction;
        //        Max PWM is 1 radian per iteration
        int maxPWMChange = (int) (1/THETA_RANGE * (MAX_PWM - MIN_PWM));
        if (up) {
            correction = Math.min(MAX_PWM - currPWM, maxPWMChange);
            return currPWM + correction;
        } else {
            correction = Math.min(currPWM - MIN_PWM, maxPWMChange);
            return currPWM - correction;
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
}