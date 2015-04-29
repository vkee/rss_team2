package Servoing;

import org.ros.node.topic.Publisher;
import org.ros.message.rss_msgs.*;

public class NeckController {
	
	private final int TOP_PWM_INDEX = 0;
	private final int BOT_PWM_INDEX = 2;
	private final int OTHER_PWM_INDEX = 1;
	
	public TopNeckController top;
	public BottomNeckController bottom;
	
	public NeckController(Publisher<ArmMsg> armPWMPub)
		{
		top = new TopNeckController(armPWMPub);
		bottom = new BottomNeckController(armPWMPub);
		}
	
	private double[] bottomAndTopAngle(double angle)
		{
		angle %= 360;
		if (angle > 180)
			{return new double[]{180.0, angle-180};}
		else return new double[]{0.0, angle};
		}
	
	public void goToAngle(double angle, int[] pwm)
		{
		double[] angles = bottomAndTopAngle(angle);
		
		// order: bottom, gate, top
		top.sendPWM(
				top.rotateTo(angles[0], pwm[BOT_PWM_INDEX]),
				pwm[OTHER_PWM_INDEX],
				top.rotateTo(angles[1], pwm[TOP_PWM_INDEX])
				);
		
		}
	
	public boolean atAngle(double angle, int[] pwm)
		{
		double[] angles = bottomAndTopAngle(angle);
		return bottom.atTarget(angles[0], pwm[BOT_PWM_INDEX]) &&
				top.atTarget(angles[1], pwm[TOP_PWM_INDEX]);
		}

}
