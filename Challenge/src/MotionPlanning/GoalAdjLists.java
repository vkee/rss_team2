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
//    	if (from.equals(new Point2D.Double(0.6, 0.6))) System.out.println(to+" -- "+dist);
    	
        double pathDistance = dist;//getDistance(path);
        if (distanceGrid.get(from)==null) distanceGrid.put(from, new HashMap<Point2D.Double,Double>());
        if (distanceGrid.get(to)==null) distanceGrid.put(to, new HashMap<Point2D.Double,Double>());
        distanceGrid.get(from).put(to, pathDistance);
        distanceGrid.get(to).put(from, pathDistance);

        if (pathGrid.get(from)==null) pathGrid.put(from, new HashMap<Point2D.Double,ArrayList<Point2D.Double>>());
        if (pathGrid.get(to)==null) pathGrid.put(to, new HashMap<Point2D.Double,ArrayList<Point2D.Double>>());

        ArrayList<Point2D.Double> reversePath;
        if (path != null)
        	{reversePath = new ArrayList<Point2D.Double>(path.size());
        	for (int i = path.size()-1; i >= 0; i--) {
        		reversePath.add(path.get(i));
        		}
        	}
        else reversePath = null;

        pathGrid.get(from).put(to, path);
        pathGrid.get(to).put(from, reversePath);
    }


    /**
     * Gives the closest path from where you are (returns null if it only has a path to the goal)
     * @param from
     * @return
     */
    public Point2D.Double getClosestFeasiblePointFrom(Point2D.Double from, double maxDist)
    {
        HashMap<Point2D.Double,Double> originDistMap = distanceGrid.get(from);
        
//        System.out.println(originDistMap);

        Point2D.Double closestPoint = null;
        double closestDistance = Double.MAX_VALUE;

        Point2D.Double[] tempArray = new Point2D.Double[originDistMap.keySet().size()];
        originDistMap.keySet().toArray(tempArray);
        
        ArrayList<Point2D.Double> destinations = new ArrayList<Point2D.Double>(Arrays.asList(tempArray));
        
//        System.out.println("destinations.size() " + destinations.size());
        if (destinations.size() == 1) return null;
//        for (Point2D.Double dests : destinations){
//        	System.out.println(dests);
//        }
        
        for (Point2D.Double tos : destinations)
        {
            if (tos.equals(goal)) continue;		//not goal node
           // if (originDistMap.get(tos) + distanceGrid.get(tos).get(goal) > maxDist) continue;

            if (originDistMap.get(tos) < closestDistance)
            {
                closestDistance = originDistMap.get(tos);
                closestPoint = tos;
            }
        }
//        return pathGrid.get(from).get(closestPoint);
        return closestPoint;
    }
    
    /**
     * Gives the path from from pt to to pt
     * @param from
     * @param to
     * @return
     */
    public ArrayList<Point2D.Double> getPath(Point2D.Double from, Point2D.Double to){
    	return pathGrid.get(from).get(to);
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
    	
    	if (!to.equals(from)){
            distanceGrid.remove(from);
            pathGrid.remove(from);
    	}
    	
    	for (Point2D.Double tos : distanceGrid.keySet()){
            distanceGrid.get(tos).remove(from);
            //pathGrid.get(from).remove(to);
            pathGrid.get(tos).remove(from);
    	}
    }

    public void pruneSelfLoops(ArrayList<Point2D.Double> destinations) {
        for (Point2D.Double dest : destinations) {
            distanceGrid.get(dest).remove(dest);
            pathGrid.get(dest).remove(dest);        }
    }
    
    private double getDistance(ArrayList<Point2D.Double> path)
    {
        double dist = 0;
        for (int i=0; i<path.size()-1; i++)
        {dist += Math.sqrt(Math.pow(path.get(i).x-path.get(i+1).x, 2) + Math.pow(path.get(i).y-path.get(i+1).y, 2));}
        return dist;
    }

}
