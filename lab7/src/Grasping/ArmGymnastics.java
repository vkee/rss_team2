package Grasping;

public class ArmGymnastics {

	public ArmGymnastics()
		{
		AllArmController arm = AllArmController.getInstance();
		}

	public void dance()
		{
		arm.grip.getPWM(Math.PI/4);
		//send message
		///...
		}
}
