package Grasping;

public class WristController extends JointController{
    
    public WristController(int minPWM, int maxPWM, double thetaRange, int pwm0, int pwm90) {
        super(minPWM, maxPWM, thetaRange, pwm0, pwm90);
    }
}
