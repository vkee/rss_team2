package Servoing;

import org.ros.node.topic.Publisher;
import org.ros.message.rss_msgs.*;

public class NeckController {
	//	The indices of the servos in the PWM array

	private final int OTHER_PWM_INDEX = 1;

	public TopNeckController top;
	public BottomNeckController bottom;

	public NeckController(Publisher<ArmMsg> armPWMPub){
		top = new TopNeckController(armPWMPub);
		bottom = new BottomNeckController(armPWMPub);
	}

	/**
	 * Generates the angles for the top and bottom servos to be at
	 * for the camera to be pointed at the desired angle.
	 * @param angle the desired angle
	 */
	public double[] bottomAndTopAngle(double angle){
		angle %= 2*Math.PI;
		if (angle > Math.PI){
			return new double[]{Math.PI, angle-Math.PI};
		} else{
			return new double[]{0.0, angle};
		}
	}

	/**
	 * Commands the servos to go to the specified angle
	 * @param angle the desired angle
	 * @param pwm the commanded PWM values
	 */
	public void goToAngle(double angle, int[] pwm){
		double[] angles = bottomAndTopAngle(angle);
		
		System.out.println(bottom.rotateTo(angles[0], pwm) + " - " +top.rotateTo(angles[1], pwm));
		// order: bottom, gate, top
		top.sendPWM(pwm[bottom.messageIndex],
				pwm[OTHER_PWM_INDEX],top.rotateTo(angles[1], pwm));
//		top.sendPWM(bottom.rotateTo(angles[0], pwm),
//				pwm[OTHER_PWM_INDEX],pwm[top.messageIndex]);
		//System.out.println("Bottom PWM: " + bottom.rotateTo(angles[0], pwm[BOT_PWM_INDEX]));
		//System.out.println("Top PWM: " + top.rotateTo(angles[1], pwm[TOP_PWM_INDEX]));
	}

	/**
	 * Determines whether the neck is at the desired angle
	 * @param angle the desired angle
	 * @param pwm the commanded PWM values
	 */
	public boolean atAngle(double angle, int[] pwm)
	{
		double[] angles = bottomAndTopAngle(angle);
		return ((bottom.atTarget(angles[0], pwm)||true) &&
				top.atTarget(angles[1], pwm));
	}
	
	
	
	
	public void fullCycle(boolean full)
	{
		int top0 = top.PWM_0;
		int top180 = top.PWM_180;

		int bot0 = bottom.PWM_0;
		int bot180 = bottom.PWM_180;

if (full){
		for (int i=0; i<6; i++){

			//System.out.println(i+"--"+Math.max(0,i-3)+"--"+Math.min(i, 3));

			bottom.sendPWM(bot0+(bot180-bot0)/3*Math.max(0,i-3), GateController.GATE_CLOSED_PWM, top0+(top180-top0)/3*Math.min(i, 3)); //0 (forward)
			try{Thread.sleep(2000);}catch(Exception e){}

		}
}
		for (int i=6; i>=0; i--){

			//System.out.println(i+"--"+Math.max(0,i-3)+"--"+Math.min(i, 3));

			bottom.sendPWM(bot0+(bot180-bot0)/3*Math.max(0,i-3), GateController.GATE_CLOSED_PWM, top0+(top180-top0)/3*Math.min(i, 3)); //0 (forward)
			try{Thread.sleep(2000);}catch(Exception e){}


		}
	}
	
	public void goToSetting(int i)
		{
		int top0 = top.PWM_0;
		int top180 = top.PWM_180;

		int bot0 = bottom.PWM_0;
		int bot180 = bottom.PWM_180;
		
		bottom.sendPWM(bot0+(bot180-bot0)/3*Math.max(0,i-3), GateController.GATE_CLOSED_PWM, top0+(top180-top0)/3*Math.min(i, 3));
		
		}

}
	
