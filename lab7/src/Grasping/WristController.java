package Grasping;

public class WristController extends JointController{
    protected final int COLLECT_PWM = 550; // PWM for wrist to be at for collecting objects
    private final double GYM_BEND_ANGLE = Math.PI/2;
    private final int GYM_BEND_PWM = 300; // hardcoded for now
    
    public WristController(int minPWM, int maxPWM, double thetaRange, int pwm0, int pwm90) {
        super(minPWM, maxPWM, thetaRange, pwm0, pwm90);
//        GYM_BEND_PWM = getPWM(GYM_BEND_ANGLE);
        System.out.println("Gym Bend PWM: " + GYM_BEND_PWM);
    }
    
    /**
     * Determines whether the wrist joint is at the bend with a desired angle state
     * @param currPWM the last PWM value written to the wrist
     */
    public boolean isGymBent(int currPWM) {
        return (currPWM == GYM_BEND_PWM);
    }
    
    /**
     * Returns the PWM value to write to the wrist servo
     * @param currPWM the last PWM value written to the wrist
     */
    public int bendGym(int currPWM) {
        return getSafePWM(currPWM, GYM_BEND_PWM);
    }   
}