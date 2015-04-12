package Localization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import Challenge.GrandChallengeMap;

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
    private GrandChallengeMap map;

    //    Noise Variables
    private double transNoise;
    private double rotNoise;
    private double sensorNoise;

    public ParticleFilter(int numParticles, GrandChallengeMap map, 
            double transNoise, double rotNoise, double sensorNoise) {
        this.numParticles = numParticles;
        this.map = map;
        this.transNoise = transNoise;
        this.rotNoise = rotNoise;
        this.sensorNoise = sensorNoise;

        //        Creating the particles
        for (int i = 0; i < numParticles; i++) {
            particles.add(new RobotParticle(map, transNoise, rotNoise, sensorNoise));
        }
    }

    protected void motionUpdate(double translation, double rotation) {
        for (RobotParticle particle : particles) {
            particle.motionUpdate(translation, rotation);
        }
    }

    protected void measurementUpdate(ArrayList<Integer> measuredFiducials, HashMap<Integer, Double> measuredDists) {
        //        Determining the probabilities that each of the particles measured the input measurements
        ArrayList<Double> measurementProbs = new ArrayList<Double>();
        for (RobotParticle particle : particles) {
            measurementProbs.add(particle.measurementProb(measuredFiducials, measuredDists));
        }

        particles = resampleParticles(measurementProbs);
    }
    
    protected ArrayList<RobotParticle> resampleParticles(ArrayList<Double> measurementProbs){
        ArrayList<RobotParticle> resampledParticles = new ArrayList<RobotParticle>();
        
        int index = (int) (Math.random() * numParticles);
        double beta = 0.0;
        
        double maxProb = Collections.max(measurementProbs);
        for (int i = 0; i < numParticles; i++) {
            beta += (Math.random()*2*maxProb);
            while (measurementProbs.get(index) < beta) {
                beta -= measurementProbs.get(index);
                index = (index + 1) % numParticles;
            }
            resampledParticles.add(particles.get(index));
        }
        return resampledParticles;
    }
}