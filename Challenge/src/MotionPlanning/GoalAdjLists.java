package MotionPlanning;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GoalAdjLists {

    private HashMap<Point2D.Double, HashMap<Point2D.Double, ArrayList<Point2D.Double>>> pathGrid = new HashMap<Point2D.Double, HashMap<Point2D.Double, ArrayList<Point2D.Double>>>();
    private HashMap<Point2D.Double, HashMap<Point2D.Double, Double>> distanceGrid = new HashMap<Point2D.Double, HashMap<Point2D.Double, Double>>();
    public Point2D.Double goal;

    public GoalAdjLists(Point2D.Double goalPoint)
    {
        goal = goalPoint;		
    }

    /**
     * adds the from/to path and to/from path
     * @param from
     * @param to
     * @param path
     * @param dist
     */
    public void addBiPath(Point2D.Double from, Point2D.Double to, ArrayList<Point2D.Double> path, double dist)
    {
        double pathDistance = dist;//getDistance(path);
        if (distanceGrid.get(from)==null) distanceGrid.put(from, new HashMap<Point2D.Double,Double>());
        if (distanceGrid.get(to)==null) distanceGrid.put(to, new HashMap<Point2D.Double,Double>());
        distanceGrid.get(from).put(to, pathDistance);
        distanceGrid.get(to).put(from, pathDistance);

        if (pathGrid.get(from)==null) pathGrid.put(from, new HashMap<Point2D.Double,ArrayList<Point2D.Double>>());
        if (pathGrid.get(to)==null) pathGrid.put(to, new HashMap<Point2D.Double,ArrayList<Point2D.Double>>());

        ArrayList<Point2D.Double> reversePath = new ArrayList<Point2D.Double>(path.size());
        for (int i = path.size()-1; i >= 0; i--) {
            reversePath.add(path.get(i));
        }

        pathGrid.get(from).put(to, path);
        pathGrid.get(to).put(from, reversePath);
    }


    /**
     * Gives the closest path from where you are (returns null if it only has a path to the goal)
     * @param from
     * @return
     */
    public ArrayList<Point2D.Double> getClosestFeasiblePathFrom(Point2D.Double from, double maxDist)
    {
        HashMap<Point2D.Double,Double> originDistMap = distanceGrid.get(from);

        Point2D.Double closestPoint = null;
        double closestDistance = Double.MAX_VALUE;

        Point2D.Double[] tempArray = new Point2D.Double[originDistMap.keySet().size()];
        originDistMap.keySet().toArray(tempArray);
        
        ArrayList<Point2D.Double> destinations = new ArrayList<Point2D.Double>(Arrays.asList(tempArray));

        if (destinations.size() == 1) return null;

        for (Point2D.Double tos : destinations)
        {
            if (tos == goal) continue;		//not goal node
            if (originDistMap.get(tos) + distanceGrid.get(tos).get(goal) > maxDist) continue;

            if (originDistMap.get(tos) < closestDistance)
            {
                closestDistance = originDistMap.get(tos);
                closestPoint = tos;
            }
        }
        return pathGrid.get(from).get(closestPoint);
    }

    public ArrayList<Point2D.Double> getPathToGoal(Point2D.Double from)
    {
        return pathGrid.get(from).get(goal);
    }

    public ArrayList<Point2D.Double> getUnvisited()
    {return (ArrayList<Point2D.Double>) distanceGrid.keySet();}


    public void useBiPath(Point2D.Double from, Point2D.Double to)
    {
        //distanceGrid.get(from).remove(to);
        distanceGrid.remove(from);
        distanceGrid.get(to).remove(from);
        //pathGrid.get(from).remove(to);
        pathGrid.remove(from);
        pathGrid.get(to).remove(from);
    }

    private double getDistance(ArrayList<Point2D.Double> path)
    {
        double dist = 0;
        for (int i=0; i<path.size()-1; i++)
        {dist += Math.sqrt(Math.pow(path.get(i).x-path.get(i+1).x, 2) + Math.pow(path.get(i).y-path.get(i+1).y, 2));}
        return dist;
    }

}
