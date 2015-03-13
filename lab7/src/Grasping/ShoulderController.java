package Grasping;

public class ShoulderController extends JointController {
    
    public ShoulderController(int minPWM, int maxPWM, double thetaRange, int pwm0, int pwm90) {
        super(minPWM, maxPWM, thetaRange, pwm0, pwm90);
    }
    
}