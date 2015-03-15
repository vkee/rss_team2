package Grasping;

public class GripperController extends JointController{

    public GripperController(int minPWM, int maxPWM, double thetaRange, int pwm0, int pwm90) {
        super(minPWM, maxPWM, thetaRange, pwm0, pwm90);
    }

    /**
     * Closes the gripper
     * @param currPWM the last PWM value written to the servo
     * @return the PWM value to be written to the servo
     */
    public int close(int currPWM) {
        return fullRotation(currPWM, true);
    }

    /**
     * Closes the gripper
     * @param currPWM the last PWM value written to the servo
     * @return the PWM value to be written to the servo
     */
    public int open(int currPWM) {
        return fullRotation(currPWM, false);
    }
}