package Grasping;

public class GripperController extends JointController{

    public GripperController(int minPWM, int maxPWM, double thetaRange, int pwm0, int pwm90) {
        super(minPWM, maxPWM, thetaRange, pwm0, pwm90);
    }

    /**
     * Closes the gripper
     */
    public void close() {
        //        TODO are we supposed to use the bump sensors? if we are, just use this. 
        //        o/w, call the full rotation as opening and closing is the full range of motion 
    }

    /**
     * Opens the gripper
     */
    public void open() {
        //        TODO are we supposed to use the bump sensors? if we are, just use this. 
        //        o/w, call the full rotation as opening and closing is the full range of motion 
    }
}