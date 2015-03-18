package Grasping;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

public class InverseKinematics {

	protected static double ARM_LENGTH = 0.245;
	protected static double WRIST_LENGTH = 0.18;
	
	public static double getTheta(double zsol)
		{
		double theta = Math.asin(zsol/ARM_LENGTH);		//check that x and y give same solution??
		return theta;
		}
	
	public static double getPhi(double zsol, double ztarg)
		{
		double phi = Math.asin((ztarg-zsol)/WRIST_LENGTH);
		return phi;
		}
	
	/**
	 * 
	 * @param X negative wrt arm joint
	 * @param Z negative wrt arm joint
	 * @return solutions (2)
	 */
	
	public static double[] getPositions(double xtarg, double ztarg)
		{
		double[] Zs = new double[2];
		
		double A = (Math.pow(WRIST_LENGTH,2) - Math.pow(ARM_LENGTH,2)
					-Math.pow(xtarg,2) -Math.pow(ztarg,2))/(-2*xtarg); 
		double rat = ztarg/xtarg;
		
		double a = Math.pow(rat,2)+1;
		double b = -2*A*rat;
		double c = Math.pow(A,2)-Math.pow(ARM_LENGTH,2);
		
				
		double plain = -b/2/a;
		double plusmin = Math.sqrt(Math.pow(b,2) - 4*a*c)/2/a;
		
		Zs[0] = plain+plusmin;
		Zs[1] = plain-plusmin;
		
		return Zs;
		}
	
	public static double[] getThetaPhi(double xtarg, double ztarg, double currTheta, double currPhi)
		{
		double[] Zs = getPositions(xtarg, ztarg);		//always 2 solutions
		
		
		double theta1 = getTheta(Zs[0]);
		double phi1 = getPhi(Zs[0], ztarg)-theta1;		//because phi is moving with theta
		double distance1 = Math.abs(currTheta-theta1)+Math.abs(currPhi-phi1);
		
		double theta2 = getTheta(Zs[1]);
		double phi2 = getPhi(Zs[1], ztarg)-theta2;
		double distance2 = Math.abs(currTheta-theta2)+Math.abs(currPhi-phi2);
		
		/// TODO need to test for validity
		
		if (distance1<distance2) return new double[]{theta1,phi1};
		else return new double[]{theta2,phi2};
				
		}
	
	/*public static void main(String[] args)
		{
		double sum = ARM_LENGTH + WRIST_LENGTH;
		System.out.println(Arrays.toString(getThetaPhi(ARM_LENGTH + WRIST_LENGTH, 0, 0, 0)));
		System.out.println(Arrays.toString(getThetaPhi(sum*Math.cos(-Math.PI/4), sum*Math.sin(-Math.PI/4), 0, 0)));
		}*/
}
