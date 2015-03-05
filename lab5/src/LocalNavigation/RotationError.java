package LocalNavigation;

public class RotationError {

    /**
     * Computes the rotational velocity for the robot given the current error from a CW PI/2 rotation
     * @param robotTheta the current orientation of the robot
     * @param alignedBotTheta the orientation of the robot at the start of the rotation
     * @return the rotation velocity
     */
    public static double currError(double robotTheta, double alignedBotTheta){
        double error = robotTheta - (alignedBotTheta - Math.PI/2);
        System.out.println("Error: " + error);
        //                    TODO: david figure out the wraparound case
//      may be easiest to quickly make a test class and run junit tests on diff cases
//      rather than compiling and running on robot each time which takes at least a couple minutes
//      I think if the error is greater than pi, then subtract 2 pi might work
        double rotateGain = 0.25;
        return -Math.min(rotateGain*error, 0.25);
    }
}