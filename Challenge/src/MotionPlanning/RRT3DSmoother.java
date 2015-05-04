package MotionPlanning;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class RRT3DSmoother {

	MultiRRT3D engine;
	public RRT3DSmoother(MultiRRT3D engine)
	{this.engine = engine;}

	public ArrayList<Point2D.Double> smoothPath(ArrayList<Point2D.Double> path)
	{//deepcopy??
		double atAngle = 0.0;
		ArrayList<Integer> removes = new ArrayList<Integer>();
		if (path.size() < 3) return path;
		for (int i=2; i<path.size(); i++)
		{
			Point2D.Double point1 = path.get(i-2);
			Point2D.Double point2 = path.get(i);
			boolean remove = validPath(point1, point2, atAngle);
			if (remove) 
			{
				removes.add(i-1);
				atAngle =  MultiRRT3D.getAngle(point1.x, point1.y, point2.x, point2.y); ///between P1 P2

				// Keeping the angle between 0 and 2PI
				if (atAngle < 0.0) {
					atAngle += 2*Math.PI; 
				}
				i++;}
			else
			{
				atAngle = MultiRRT3D.getAngle(path.get(i-1).x, path.get(i-1).y, point2.x, point2.y);	//between (I-1) P2

				// Keeping the angle between 0 and 2PI
				if (atAngle < 0.0) {
					atAngle += 2*Math.PI; 
				}
			}
		}
		System.out.println(removes.size()+ " of "+path.size());
		if (removes.size()==0) 
			{return path;}
		for (int k=removes.size()-1; k>=0; k--)
			{path.remove((int)removes.get(k));}
		return smoothPath(path);

	}

	private boolean validPath(Point2D.Double point1, Point2D.Double point2, double atAngle)
	{
		double angle2TestPt = MultiRRT3D.getAngle(point1.x, point1.y, point2.x, point2.y);

		//                Keeping the angle between 0 and 2PI
		if (angle2TestPt < 0.0) {
			angle2TestPt += 2*Math.PI; 
		}

		double robotAngleError = (angle2TestPt - atAngle) % (2*Math.PI);

		//                Keeping the error in angle between -PI and PI so that the robot minimizes rotation
		//robotAngleError = (robotAngleError+Math.PI)%(2*Math.PI)-Math.PI;

		if (robotAngleError > Math.PI) {				
			robotAngleError -= 2*Math.PI;
		}

		//                Checking whether the robot will collide with any obstacles while it rotates to face the new point
		boolean collisionInRotation = engine.collisionInRotation(atAngle, robotAngleError, point1);

		//System.out.println("collides "+Boolean.toString(collisionInRotation));


		if (!collisionInRotation) {
			//                Making the robot aligned pointing from the closest node to the test point
			double testRobotOrientation = angle2TestPt;
			//                Checking that there is a path in the robot orientation when moving straight to the point
			boolean noClearPath = engine.lineIntersectsObs(engine.getCSpaceIndex(testRobotOrientation), point2, point1);
			return !noClearPath;
		}

		return false;
	}

}
