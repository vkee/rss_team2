package GlobalNavigation;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * Contains methods to deal with configuration spaces
 */
public class CSpace {

    /**
     * Computes the Minkowski sum of two polygons
     * @param poly1 the first polygon
     * @param poly2 the second polygon
     * @return the Minkowski sum of the polygons
     */
    public static PolygonObstacle computeMSum(PolygonObstacle poly1, PolygonObstacle poly2) {
        PolygonObstacle mSum = new PolygonObstacle();
        
        for (Point2D.Double vertex1 : poly1.getVertices()) {
            for (Point2D.Double vertex2 : poly2.getVertices()) {
                mSum.addVertex(new Point2D.Double(vertex1.getX() + vertex2.getX(), vertex1.getY() + vertex2.getY()));
            }
        }
        
        return mSum;
    }
}
