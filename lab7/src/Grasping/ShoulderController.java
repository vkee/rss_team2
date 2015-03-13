package Grasping;

public class ShoulderController extends JointController {
    
    public ShoulderController(int minPWM, int maxPWM, int pwm0, int pwm90) {
        this.MIN_PWM = minPWM;
        this.MAX_PWM = maxPWM;
        this.PWM_0 = pwm0;
        this.PWM_90 = pwm90;
        this.LINE_SLOPE = (Math.PI/2)/(PWM_90 - PWM_0);
        this.LINE_THETA_INTERCEPT =  = 0 - LINE_SLOPE*PWM_90;
    }
    
}