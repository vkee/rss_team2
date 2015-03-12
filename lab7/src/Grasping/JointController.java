package Grasping;

public class JointController {
    private int PWM_0; // PWM value at the 0 radians position
    private int PWM_90; // PWM value at the PI/2 or 90 deg position
    private double LINE_SLOPE = Math.PI/2/(PWM_90 - PWM_0); // slope of the line between PWM_90 and PWM_0
    private double LINE_INTERCEPT = 0 - LINE_SLOPE*PWM_90; // intercept of the line between PWM_90 and PWM_0

    
    
}


