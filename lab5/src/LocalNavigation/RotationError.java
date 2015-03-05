package LocalNavigation;

public class RotationError {

    /**
     * Computes the rotational velocity for the robot given the current error from a CW PI/2 rotation
     * @param robotTheta the current orientation of the robot
     * @param alignedBotTheta the orientation of the robot at the start of the rotation
     * @return the rotation velocity
     */
    public static double currError(double robotTheta, double alignedBotTheta){
        double error = (robotTheta - (alignedBotTheta - Math.PI/2)) % (2*Math.PI);
        
        if (Math.abs(error) > Math.PI){
            error -= 2*Math.PI;
        }
        
        System.out.println("Error: " + error);

        return error;
    }
}