package Grasping;

public class ShoulderController extends JointController {
    private final double GYM_UP_ANGLE = Math.PI/4;
    private final int GYM_UP_PWM;
    private final double GYM_GROUND_ANGLE = -Math.PI/4;
    private final int GYM_GROUND_PWM;
    
    public ShoulderController(int minPWM, int maxPWM, double thetaRange, int pwm0, int pwm270) {
        super(minPWM, maxPWM, thetaRange, pwm0, pwm270);
        GYM_UP_PWM = getPWM(GYM_UP_ANGLE);
        GYM_GROUND_PWM = getPWM(GYM_GROUND_ANGLE);
        System.out.println("Gym Up PWM: " + GYM_UP_PWM);
        System.out.println("Gym Ground PWM: " + GYM_GROUND_PWM);
    }
    
    /**
     * Determines whether the shoulder joint is at the move up with a desired angle state
     * @param currPWM the last PWM value written to the shoulder
     */
    public boolean isGymUp(int currPWM) {
        return (currPWM == GYM_UP_PWM);
    }
    
    /**
     * Returns the PWM value to write to the shoulder servo to move the arm up
     * @param currPWM the last PWM value written to the shoulder
     */
    public int moveGymUp(int currPWM) {
        return getSafePWM(currPWM, GYM_UP_PWM);
    }   
    
    /**
     * Determines whether the shoulder joint is such that the arm is on the ground
     * @param currPWM the last PWM value written to the shoulder
     */
    public boolean onGround(int currPWM) {
        return (currPWM == GYM_GROUND_PWM);
    }
    
    /**
     * Returns the PWM value to write to the shoulder servo to move the arm to the ground
     * @param currPWM the last PWM value written to the shoulder
     */
    public int moveToGround(int currPWM) {
        return getSafePWM(currPWM, GYM_GROUND_PWM);
    }
}