package Grasping;

public class ArmController {
//	/**
//	 * There should only be one instance of each of the joint controllers.
//	 * As implemented now, they do not save state but i think we will benefit from modifying that
//	 */
//	private static ArmController singleton;
	
	GripperController grip;
	WristController wrist;
	ShoulderController shoulder;
	
	protected ArmController() {}
	
//	public static ArmController getInstance()
//		{
//		if (singleton==null)
//			{
//			singleton = new ArmController();
//			singleton.grip = new GripperController(minPWM, maxPWM, thetaRange, pwm0, pwm90);
//			singleton.wrist = new WristController(minPWM, maxPWM, thetaRange, pwm0, pwm90);
//			singleton.shoulder = new ShoulderController(minPWM, maxPWM, thetaRange, pwm0, pwm90);
//			}
//		return singleton;
//		}
	

}