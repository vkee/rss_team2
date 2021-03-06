package Localization;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import Challenge.Fiducial;
import Challenge.GrandChallengeMap;
import MotionPlanning.RRT;

/**
 * ParticleFilter is part of the Localization module. It represents a particle
 * filter with RobotParticle objects as the particles.
 * 
 * It is based off of Thrun's Python implementation from his Udacity course
 * Artifical Intelligence for Robotics.
 *
 */
public class ParticleFilter {
    //    The number of particles used in the particle filter
    private final int numParticles;
    private ArrayList<RobotParticle> particles = new ArrayList<RobotParticle>();

    //    Map Fields
    public GrandChallengeMap map;
    private java.awt.geom.Rectangle2D.Double worldRect;

    //    Map Dimensions
    private double worldWidth;
    private double worldHeight;
    private double botLeftX;
    private double botLeftY;

    //    Noise Variables
    private double transNoise;
    private double rotNoise;
    private double sensorNoise;
    private Fiducial[] fiducials;
    
    /**
     * Creates a Particle filter with the provided parameters (including the robot's actual position)
     * distributing the particles in a radius around the robot's initial position
     * @param robotX the robot's starting x coordinate
     * @param robotY the robot's starting y coordinate
     * @param robotTheta the robot's starting orientation
     * @param particleRadius the radius defining the circle the particles are distributed within
     * @param numParticles the number of particles 
     * @param map the map of the environment
     * @param transNoise the translational noise (std dev of translational measurements)
     * @param rotNoise the rotational noise (std dev of rotation measurements)
     * @param sensorNoise the sensor noise (std dev of sensor measurements)
     */
    public ParticleFilter(double robotX, double robotY, double robotTheta, double particleRadius, 
            int numParticles, GrandChallengeMap map, double transNoise, double rotNoise, double sensorNoise) {
        this.numParticles = numParticles;
        this.map = map;
        this.transNoise = transNoise;
        this.rotNoise = rotNoise;
        this.sensorNoise = sensorNoise;
        this.worldRect = map.getWorldRect();
        this.fiducials = map.getFiducials();
        this.worldWidth = worldRect.getWidth();
        this.worldHeight = worldRect.getHeight();
        this.botLeftX = worldRect.getMinX();
        this.botLeftY = worldRect.getMinY();
        
//        particles.add(new RobotParticle(robotX, robotY, robotTheta, fiducials, worldWidth, worldHeight, 
//                botLeftX, botLeftY, transNoise, rotNoise, sensorNoise));
        
        for (int i = 0; i < numParticles; i++) {
            double particleTheta = Math.random() * 2*Math.PI;
            double particleX = robotX + Math.random() * particleRadius * Math.cos(particleTheta);
            double particleY = robotY + Math.random() * particleRadius * Math.sin(particleTheta);

            particles.add(new RobotParticle(particleX, particleY, robotTheta, fiducials, worldWidth, worldHeight, 
                    botLeftX, botLeftY, transNoise, rotNoise, sensorNoise));
        }
    }
    
//    /**
//     * Creates a Particle filter with the provided parameters.
//     * @param numParticles the number of particles 
//     * @param map the map of the environment
//     * @param transNoise the translational noise (std dev of translational measurements)
//     * @param rotNoise the rotational noise (std dev of rotation measurements)
//     * @param sensorNoise the sensor noise (std dev of sensor measurements)
//     */
//    public ParticleFilter(int numParticles, GrandChallengeMap map, 
//            double transNoise, double rotNoise, double sensorNoise) {
//        this.numParticles = numParticles;
//        this.map = map;
//        this.transNoise = transNoise;
//        this.rotNoise = rotNoise;
//        this.sensorNoise = sensorNoise;
//        this.worldRect = map.getWorldRect();
//        this.fiducials = map.getFiducials();
//        this.worldWidth = worldRect.getWidth();
//        this.worldHeight = worldRect.getHeight();
//        this.botLeftX = worldRect.getMinX();
//        this.botLeftY = worldRect.getMinY();
//
//        //        Creating the particles
//        for (int i = 0; i < numParticles; i++) {
//            particles.add(new RobotParticle(fiducials, worldWidth, worldHeight, 
//                    botLeftX, botLeftY, transNoise, rotNoise, sensorNoise));
//        }
//    }
//
//    /**
//     * Creates a Particle filter with the provided parameters (including the robot's actual position)
//     * @param robotX the robot's starting x coordinate
//     * @param robotY the robot's starting y coordinate
//     * @param robotTheta the robot's starting orientation
//     * @param numParticles the number of particles 
//     * @param map the map of the environment
//     * @param transNoise the translational noise (std dev of translational measurements)
//     * @param rotNoise the rotational noise (std dev of rotation measurements)
//     * @param sensorNoise the sensor noise (std dev of sensor measurements)
//     */
//    public ParticleFilter(double robotX, double robotY, double robotTheta, int numParticles, GrandChallengeMap map, 
//            double transNoise, double rotNoise, double sensorNoise) {
//        this.numParticles = numParticles;
//        this.map = map;
//        this.transNoise = transNoise;
//        this.rotNoise = rotNoise;
//        this.sensorNoise = sensorNoise;
//        this.worldRect = map.getWorldRect();
//        this.fiducials = map.getFiducials();
//        this.worldWidth = worldRect.getWidth();
//        this.worldHeight = worldRect.getHeight();
//        this.botLeftX = worldRect.getMinX();
//        this.botLeftY = worldRect.getMinY();
//
//        //        Creating the particles
//        
//        particles.add(new RobotParticle(robotX, robotY, robotTheta, fiducials, worldWidth, worldHeight, 
//                botLeftX, botLeftY, transNoise, rotNoise, sensorNoise));
//        
//        for (int i = 1; i < numParticles; i++) {
//            particles.add(new RobotParticle(fiducials, worldWidth, worldHeight, 
//                    botLeftX, botLeftY, transNoise, rotNoise, sensorNoise));
//        }
//    }
//
    /**
     * Creates a Particle filter for testing purposes with the provided parameters.
     * @param map the map of the environment
     * @param transNoise the translational noise (std dev of translational measurements)
     * @param rotNoise the rotational noise (std dev of rotation measurements)
     * @param sensorNoise the sensor noise (std dev of sensor measurements)
     */
    public ParticleFilter(GrandChallengeMap map, 
            double transNoise, double rotNoise, double sensorNoise) {
        numParticles = 1;
        this.map = map;
        this.transNoise = transNoise;
        this.rotNoise = rotNoise;
        this.sensorNoise = sensorNoise;
        this.worldRect = map.getWorldRect();
        this.fiducials = map.getFiducials();
        this.worldWidth = worldRect.getWidth();
        this.worldHeight = worldRect.getHeight();
        this.botLeftX = worldRect.getMinX();
        this.botLeftY = worldRect.getMinY();

        //        Creating the particles
        particles.add(new RobotParticle(0.6, 0.6, 0.0, fiducials, worldWidth, worldHeight, 
                botLeftX, botLeftY, transNoise, rotNoise, sensorNoise));
    }

    /**
     * Updates the position of each particle.
     * @param translation the translational distance the robot has moved (in meters)
     * @param rotation the rotational distance the robot has moved (in radians)
     */
    public void motionUpdate(double translation, double rotation) {
        for (RobotParticle particle : particles) {
//            System.out.println("Before motion update: " + particle);
            particle.motionUpdate(translation, rotation);
//            System.out.println("After motion update: " + particle);
        }
    }

    /**
     * Computes the probability that each particle is at it's current position given the provided measurements
     * @param measuredFiducials the indices of the fiducials that have measurements 
     * (corresponding to the indices of the fiducials in the field of GrandChallengeMap)
     * @param measuredDists the measured distances to the fiducials
     */
    public void measurementUpdate(ArrayList<Integer> measuredFiducials, HashMap<Integer, Double> measuredDists) {
        //        Determining the probabilities that each of the particles measured the input measurements
        ArrayList<Double> measurementProbs = new ArrayList<Double>();
        for (RobotParticle particle : particles) {
            measurementProbs.add(particle.measurementProb(measuredFiducials, measuredDists));
        }

//        System.out.println("Particles");
//        for (RobotParticle particle : particles) {
//            System.out.println(particle);
//        }        
//        System.out.println("Measurement Update");
//        for (Double measurementProb : measurementProbs) {
//            System.out.println(measurementProb);
//        }

        
        particles = resampleParticles(measurementProbs);
        

//        Collections.sort(measurementProbs);
//        
//        
//        System.out.println("Displaying top 5 measurement probs");
//        for (int i = measurementProbs.size(); i > measurementProbs.size() - 5; i--) {
//            System.out.println(measurementProbs.get(i - 1));
//        }
    }

    /**
     * Resamples the particles with the provided measurement probabilities using a resampling wheel.
     * See https://www.udacity.com/course/viewer#!/c-cs373/l-48704330/e-48748082/m-48740082
     * @param measurementProbs the probabilities that each of the particles 
     * @return the resampled particles
     */
    public ArrayList<RobotParticle> resampleParticles(ArrayList<Double> measurementProbs){
        ArrayList<RobotParticle> resampledParticles = new ArrayList<RobotParticle>();

        int index = (int) (Math.random() * numParticles);
        double beta = 0.0;

        double maxProb = Collections.max(measurementProbs);
        for (int i = 0; i < numParticles; i++) {
            beta += (Math.random()*2*maxProb);
            while (measurementProbs.get(index) < beta) {
                beta -= measurementProbs.get(index);
                index += 1;
                index += numParticles;
                index %= numParticles;
            }
//            System.out.println("Index chosen: " + index);
            resampledParticles.add(new RobotParticle(particles.get(index)));
        }
        return resampledParticles;
    }

    public RobotParticle sampleParticle() {
        int index = (int) (Math.random() * numParticles);
        return particles.get(index);
    }
    
    /**
     * Determines the distances from the robot to the fiducials in the robot's FOV
     * @param robotPos the robot's current position
     * @param measuredFiducials the indices of the fiducials in the robot's FOV
     * @param map the map
     * @return the distances from the robot's position to the fiducials
     */
    public HashMap<Integer, java.lang.Double> getFidsDists(Point2D.Double robotPos, 
            GrandChallengeMap map, ArrayList<Integer> measuredFiducials) {
        HashMap<Integer, java.lang.Double> fidsDists = new HashMap<Integer, java.lang.Double>();
        Fiducial[] fiducials = map.getFiducials();
        for (Integer index : measuredFiducials) {
            Point2D.Double fidPos = fiducials[index].getPosition();

            //            Potential bug site is if robot position at 0,0 and map goes negative, 
            //            but this should be able to account for it in this ordering
            double dist = RRT.getDist(robotPos.x, robotPos.y, fidPos.x, fidPos.y);
            //            System.out.println("Distance to Fiducial " + index + " at " + fidPos + " is " + dist);
            fidsDists.put(index, dist);
        }

        return fidsDists;
    }
    
    public ArrayList<RobotParticle> getParticles() {
        return particles;
    }

    public void printParticles() {
        System.out.println("ParticleFilter with [numParticles=" + numParticles + ",  transNoise="
                + transNoise + ", rotNoise="
                + rotNoise + ", sensorNoise=" + sensorNoise + "]");
        for (RobotParticle particle : particles) {
            System.out.println(particle.toString());
        }
    }

    @Override
    public String toString() {
        String stringRep = "ParticleFilter with [numParticles=" + numParticles + ",  transNoise="
                + transNoise + ", rotNoise="
                + rotNoise + ", sensorNoise=" + sensorNoise + "]";

        for (RobotParticle particle : particles) {
            stringRep = stringRep + particle.toString();
        }

        return stringRep;
    }
}