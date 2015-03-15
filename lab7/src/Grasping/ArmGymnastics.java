package Grasping;

public class ArmGymnastics {

	public ArmGymnastics()
		{
		AllArmControler arm = AllArmControler.getInstance();
		}

	public void dance()
		{
		arm.grip.getPWM(Math.PI/4);
		//send message
		///...
		}
}
