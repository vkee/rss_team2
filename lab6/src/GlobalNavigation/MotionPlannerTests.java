package GlobalNavigation;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.IOException;
import java.text.ParseException;

import org.junit.Test;

public class MotionPlannerTests {

	PolygonMap map;
	MotionPlanner planner;
	
	public MotionPlannerTests() throws IOException, ParseException
		{
		map = new PolygonMap("global-nav-maze-2011-basic.map");
		planner = new MotionPlanner(map);
		}
	
	@Test
	public void intersectTests() {
		assertFalse(planner.lineIntersects(map.obstacles.get(0), new Point2D.Double(1,1), new Point2D.Double(1,1)));
	}	
	
	@Test
	public void planTest() {
		//planner.getPath(start, goal, 0.05)
	}

}
