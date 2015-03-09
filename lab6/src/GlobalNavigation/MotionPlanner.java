package GlobalNavigation;

/**
 * 
 */
public class MotionPlanner {
    public MotionPlanner() {
        
    }
    
    public lineIntersects(PolygonObstacle p, Point2D.Double x1, Point2D.Double x2)
    	{
			Line2D path = new Line2D.Double(100, 100, 200, 200);
			List<Point2D.Double> verts = p.getVertices();
    	for (int i=0; i<verts.length; i++)
    		{
    		Point2D.Double point1 = verts.get(i);
    		Point2D.Double point2 = verts.get((i+1)%verts.length);
				Line2D side = new Line2D.Double(point1.x, point1.y, point2.x, point2.y);
    		if (side.intersectsLine(path)) return true;
    		}
    	return false;
    	}
    
    
}
