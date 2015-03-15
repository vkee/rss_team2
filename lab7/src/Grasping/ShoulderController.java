package Grasping;

public class ShoulderController extends JointController {
    
    public ShoulderController(int minPWM, int maxPWM, double thetaRange, int pwm0, int pwm90) {
        super(minPWM, maxPWM, thetaRange, pwm0, pwm90);
    }
    
    /**
     * Determines whether the shoulder joint is at the move up with a desired angle state
     * @param currPWM the last PWM value written to the shoulder
     */
    public boolean isGymUp(int currPWM) {
        return ()
    }
    
}