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
    
    /**
     * Determines whether the gripper is open
     * @param currPWM the last PWM value written to the servo
     */
    public boolean isOpen(int currPWM) {
//        may need to change this to max pwm depending on values
        return (currPWM == MIN_PWM);
    }
    
    /**
     * Determines whether the gripper is closed
     * @param currPWM the last PWM value written to the servo
     */
    public boolean isClosed(int currPWM) {
//        may need to change this to min pwm depending on values
        return (currPWM == MAX_PWM);
    }
}