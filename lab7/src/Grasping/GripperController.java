package Grasping;

public class GripperController extends JointController{
    
    public GripperController(int minPWM, int maxPWM, double thetaRange, int pwm0, int pwm90) {
        super(minPWM, maxPWM, thetaRange, pwm0, pwm90);
    }
}
