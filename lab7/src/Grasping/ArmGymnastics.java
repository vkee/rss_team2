package Grasping;

public class ArmGymnastics {

	public ArmGymnastics()
		{
		ArmController arm = ArmController.getInstance();
		}

	public void dance()
		{
		arm.grip.getPWM(Math.PI/4);
		//send message
		///...
		}
}
