package Localization;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import Challenge.Fiducial;
import Challenge.GrandChallengeMap;
import MotionPlanning.RRT;

/**
 * RobotModel is part of the Localization module. It represents the robot as a particle
 * used for the particle filter.
 * 
 * It is based off of Thrun's Python implementation from his Udacity course 
 * Artificial Intelligence for Robotics.
 *
 */
public class RobotParticle {
    //    Map Fields
    private Fiducial[] fiducials;

    //    Map Dimensions
    private double worldWidth;
    private double worldHeight;
    private double botLeftX;
    private double botLeftY;
    
    //    Robot Pose
    private double x;
    private double y;
    private double theta;

    //    Noise Variables
    private double transNoise;
    private double rotNoise;
    private double sensorNoise;
    private Random random;

//    /**
//     * Creates a particle modeling the robot
//     * @param fiducials the map fiducials
//     * @param worldWidth the width of the world in meters
//     * @param worldHeight the height of the world in meters
//     * @param botLeftX the bottom left x coordinate of the map
//     * @param botLeftY the bottom left y coordinate of the map
//     * @param transNoise the translational noise (std dev of translational measurements)
//     * @param rotNoise the rotational noise (std dev of rotation measurements)
//     * @param sensorNoise the sensor noise (std dev of sensor measurements)
//     */
//    public RobotParticle(Fiducial[] fiducials, double worldWidth, double worldHeight, 
//            double botLeftX, double botLeftY, double transNoise, double rotNoise, double sensorNoise) {
//        this.fiducials = fiducials;
//        this.worldWidth = worldWidth;
//        this.worldHeight = worldHeight;
//        this.botLeftX = botLeftX;
//        this.botLeftY = botLeftY;
//
//        this.x = Math.random() * worldWidth + botLeftX;
//        this.y = Math.random() * worldHeight + botLeftY;
//        this.theta = Math.random() * (2*Math.PI);
//
//        this.transNoise = transNoise;
//        this.rotNoise = rotNoise;
//        this.sensorNoise = sensorNoise;
//
//        this.random = new Random();
//    }
    
    /**
     * Creates a particle modeling the robot with predefined coordinates.
     * @param x the x coordinate of the robot
     * @param y the y coordinate of the robot
     * @param theta the orientation of the robot
     * @param fiducials the map fiducials
     * @param worldWidth the width of the world in meters
     * @param worldHeight the height of the world in meters
     * @param botLeftX the bottom left x coordinate of the map
     * @param botLeftY the bottom left y coordinate of the map
     * @param transNoise the translational noise (std dev of translational measurements)
     * @param rotNoise the rotational noise (std dev of rotation measurements)
     * @param sensorNoise the sensor noise (std dev of sensor measurements)
     */
    public RobotParticle(double x, double y, double theta, Fiducial[] fiducials, double worldWidth, 
            double worldHeight, double botLeftX, double botLeftY, double transNoise, double rotNoise, double sensorNoise) {
        this.fiducials = fiducials;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.botLeftX = botLeftX;
        this.botLeftY = botLeftY;

        this.x = x;
        this.y = y;
        this.theta = theta;

        this.transNoise = transNoise;
        this.rotNoise = rotNoise;
        this.sensorNoise = sensorNoise;

        this.random = new Random();
    }

    /**
     * Updates the particle's probable pose with the translation and rotation updates.
     * @param translation the translational distance the robot has moved (in meters)
     * @param rotation the rotational distance the robot has moved (in radians)
     */
    protected void motionUpdate(double translation, double rotation) {
//        System.out.println("Before update of " + translation + " trans and " + rotation + "rotation \n" + this);
        //        Update theta with gaussian noise w/ mean 0.0 and std dev of rotNoise
        theta = (rotation + (random.nextGaussian()*this.rotNoise));
        theta += (2*Math.PI);
        theta %= (2*Math.PI);

        //        Update x and y position with gaussian noise w/ mean 0.0 and std dev of transNoise
        double transDist = translation + random.nextGaussian()*this.transNoise;
        x += Math.cos(theta) * transDist;
        y += Math.sin(theta) * transDist;

        //        x and y positions outside map are clipped to the map boundaries
        x = Math.max(botLeftX, Math.min(x, worldWidth + botLeftX));
        y = Math.max(botLeftY, Math.min(y, worldHeight + botLeftY));
//        System.out.println("After update of " + translation + " trans and " + rotation + "rotation \n" + this);

    }

    /**
     * Computes the probability of x for a 1D Gaussian with the input mean and standard deviation
     * See http://en.wikipedia.org/wiki/Normal_distribution
     * @param mean the mean of the Gaussian
     * @param stdDev the standard deviation of the Gaussian
     * @param x the value to determine the probability of 
     * @return the probability of x
     */
    protected double probX(double mean, double stdDev, double x) {
        return (1.0 / (stdDev * Math.sqrt(2.0 * Math.PI))) * Math.exp(-Math.pow(x - mean, 2) / (2.0 * Math.pow(stdDev, 2)));
    }

    /**
     * Computes the probability that the robot at it's current position given the provided measurements
     * @param measuredFiducials the indices of the fiducials that have measurements 
     * (corresponding to the indices of the fiducials in the robot's FOV in GrandChallengeMap)
     * @param measuredDists the measured distances to the fiducials
     * @return the probability
     */
    protected double measurementProb(ArrayList<Integer> measuredFiducials, HashMap<Integer, Double> measuredDists) {
        //        Adding noise to the fiducial measurements
        for (Integer key : measuredDists.keySet()) {
            double measurement = measuredDists.get(key);
            measurement += (random.nextGaussian()*this.sensorNoise);
            measuredDists.put(key, measurement);
        }

        //        Determining the probability of getting all the measurements at the current position
        double prob = 1.0;
        for (Integer fidIndex : measuredFiducials) {
            //        Determining what the distances from the robot to the fiducials should be
            Point2D.Double pos = fiducials[fidIndex].getPosition();
            double dist = RRT.getDist(pos.getX(), pos.getY(), this.x, this.y);

            //            Assuming that the measurements are independent of each other
            prob *= probX(dist, this.sensorNoise, measuredDists.get(fidIndex));
        }

        return prob;
    }

    @Override
    public String toString() {
        return "RobotParticle [x=" + x + ", y=" + y + ", theta=" + theta + "]";
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}