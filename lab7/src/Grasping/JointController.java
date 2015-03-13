package Grasping;

public class JointController {
    protected int MIN_PWM;
    protected int MAX_PWM;
    protected int PWM_0; // PWM value at the 0 radians position
    protected int PWM_90; // PWM value at the PI/2 or 90 deg position
    protected double LINE_SLOPE; // slope of the line between PWM_90 and PWM_0
    protected double LINE_THETA_INTERCEPT; // intercept of the line between PWM_90 and PWM_0

    public JointController() {
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
}