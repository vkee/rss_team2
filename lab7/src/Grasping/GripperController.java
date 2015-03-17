package Grasping;

public class GripperController{
    protected int MIN_PWM;
    protected int MAX_PWM;
    protected double THETA_RANGE; // in radians
    protected int PWM_0; // PWM value at the 0 radians position
    protected int PWM_270; // PWM value at the -PI/2 or 270 deg position
    protected double LINE_SLOPE; // slope of the line between PWM_90 and PWM_0
    protected double LINE_THETA_INTERCEPT; // intercept of the line between PWM_90 and PWM_0
    protected int MAX_PWM_CHANGE; // the largest PWM change in value that can be safety written to the servo (for 1 radian of rotation)
    protected int SHIFT_AMOUNT = 100; // number of divisions of the rotation

    public GripperController(int minPWM, int maxPWM, double thetaRange, int pwm0, int pwm270) {
        this.MIN_PWM = minPWM;
        this.MAX_PWM = maxPWM;
        this.PWM_0 = pwm0;
        this.PWM_270 = pwm270;
        this.THETA_RANGE = thetaRange;
        this.LINE_SLOPE = (-Math.PI/2)/(PWM_270 - PWM_0);
        this.LINE_THETA_INTERCEPT = 0 - LINE_SLOPE*PWM_270;
        this.MAX_PWM_CHANGE = (int) (1/THETA_RANGE * (MAX_PWM - MIN_PWM));
    }

    /**
     * Computes the PWM to write to the servo taking into the constraint of not moving more than 1 radian per control step
     * @param currPWM the last PWM value written to the servo
     * @param up whether the servo is moving up
     * @return the PWM value to be written to the servo
     */
    public int fullRotation(int currPWM, boolean up) {
//      int correction;
      if (up) {
          return Math.min(currPWM + (MAX_PWM - MIN_PWM)/SHIFT_AMOUNT, MAX_PWM);
//          correction = Math.min(MAX_PWM - currPWM, MAX_PWM_CHANGE);
//          return currPWM + correction;
      } else {
          return Math.max(currPWM - (MAX_PWM - MIN_PWM)/SHIFT_AMOUNT, MIN_PWM);
//          correction = Math.min(currPWM - MIN_PWM, MAX_PWM_CHANGE);
//          return currPWM - correction;
      }
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
        return (currPWM == MAX_PWM);
    }

    /**
     * Determines whether the gripper is closed
     * @param currPWM the last PWM value written to the servo
     */
    public boolean isClosed(int currPWM) {
        //        may need to change this to min pwm depending on values
        return (currPWM == MIN_PWM);
    }
}