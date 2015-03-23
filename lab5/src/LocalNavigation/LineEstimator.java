package LocalNavigation;
import utils.*;

/**
 * LineEstimator based off of Lab 5 Notes using Moore-Penrose Pseudoinverse
 */
public class LineEstimator {
    private double sumX = 0.0;
    private double sumY = 0.0;
    private double sumX2 = 0.0;
    private double sumY2 = 0.0;
    private double sumZ = 0.0;

    private double d = 0.0;
    private double a = 0.0;
    private double b = 0.0;
    private double c = -1.0;

    private final double tolerance = 0.0001; 

    public LineEstimator(){
    }

    /**
     * Updates the best fit line
     * @param x the new x_i term
     * @param y the new y_i term
     */
    public void updateTerms(double x, double y){
        sumX += x;
        sumY += y;
        sumX2 += x*x;
        sumY2 += y*y;
        sumZ += x*y;

        d = sumX2*sumY2 - sumZ*sumZ;

        //        Account for case where d is near 0
        if (d < tolerance){

        } else {
            a = (sumX*sumY2 - sumY*sumZ)/d;
            b = (sumY*sumX2 - sumX*sumZ)/d;
            c = -1.0;
            
            double l_n = Math.sqrt(a*a + b*b);
            
            a = a / l_n;
            b = b / l_n;
            c = c / l_n;
        }
    }
    
    /**
     * Gets the perpendicular distance from any arbitrary point in the plane to the line
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @return the perpendicular distance
     */
    public double getPerpDist(double x, double y){
        return -c -a*x -b*y;
    }
    
    /**
     * Resets the filter
     */
    public void resetFilter(){
        sumX = 0.0;
        sumY = 0.0;
        sumX2 = 0.0;
        sumY2 = 0.0;
        sumZ = 0.0;

        d = 0.0;
        a = 0.0;
        b = 0.0;
        c = -1.0;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }
    
    public Point getPerpendicularPointOnLine(double x, double y) {
		double denom = a*a + b*b;
		double num = -b*x + a*y;
		double x_projected = (-b*num - a*c)/denom;
		double y_projected = (a*num - b*c)/denom;
		return new Point(x_projected, y_projected);
	}
}