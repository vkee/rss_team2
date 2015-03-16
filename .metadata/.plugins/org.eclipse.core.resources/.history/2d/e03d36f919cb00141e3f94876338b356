package Grasping;

public class AllArmControler {
	
	private static AllArmControler singleton;
	
	GripperController grip;
	WristController wrist;
	ShoulderController shoulder;
	
	protected AllArmControler() 
		{}
	
	public static AllArmControler getInstance()
		{
		if (singleton==null)
			{
			singleton = new AllArmControler();
			singleton.grip = new GripperController(minPWM, maxPWM, thetaRange, pwm0, pwm90);
			singleton.wrist = new WristController(minPWM, maxPWM, thetaRange, pwm0, pwm90);
			singleton.shoulder = new ShoulderController(minPWM, maxPWM, thetaRange, pwm0, pwm90);
			}
		return singleton;
		}

}
