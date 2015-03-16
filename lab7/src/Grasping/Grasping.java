package Grasping;

import org.ros.node.NodeMain;
import org.ros.namespace.GraphName;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;
import org.ros.message.rss_msgs.*;
import org.ros.message.MessageListener;
import org.ros.node.Node;
//import org.ros.message.lab7_msgs.*;

public class Grasping implements NodeMain {
	private Publisher<ArmMsg> armPWMPub;
	private Subscriber<ArmMsg> armStatusSub;
	private Subscriber<BumpMsg> bumpersSub;

	// PART 4
	// public Subscriber<org.ros.message.sensor_msgs.Image> vidSub;
	// public Publisher<org.ros.message.sensor_msgs.Image> vidPub;
	// END PART 4

	private State currState;
	private ArmGymState gymState;
	private ArmGraspState graspState;
	private ShoulderController shoulderServo;
	private WristController wristServo;
	private GripperController gripperServo;
	protected boolean objDetected = false;
	protected boolean objGrasped = false;

	public enum ArmGraspState {
		INIT_WRIST, INIT_GRIPPER, INIT_SHOULDER, GRASP, LIFT, MOVE, DEPOSIT, RETURN
	}

	// States for Arm Gymnastics
	public enum ArmGymState {
		OPEN_GRIPPER, CLOSE_GRIPPER, MOVE_UP, BEND_ELBOW, MOVE_TO_GROUND
	}

	// States dividing up space so that no servo can move more than 1 radian per
	// iteration
	public enum State {
		UP, DOWN
	}

	public Grasping() {
		currState = State.DOWN;
		shoulderServo = new ShoulderController(1225, 2375, Math.PI, 2250, 1200);
		wristServo = new WristController(250, 2000, Math.PI, 900, 1800);
		gripperServo = new GripperController(1700, 2450, Math.PI, 1700, 2450);
	}

	@Override
	public void onStart(Node node) {
		armPWMPub = node.newPublisher("command/Arm", "rss_msgs/ArmMsg");
		armStatusSub = node.newSubscriber("rss/ArmStatus", "rss_msgs/ArmMsg");
		bumpersSub = node.newSubscriber("/rss/BumpSensors", "rss_msgs/BumpMsg");

		// PART 4
		// vidPub = node.newPublisher("/rss/blobVideo", "sensor_msgs/Image");
		// vidSub = node.newSubscriber("/rss/video","sensor_msgs/Image");
		// image dest = null;
		/*
		 * vidSub.addMessageListener(new
		 * MessageListener<org.ros.message.sensor_msgs.Image>() {
		 * 
		 * @Override public void onNewMessage(org.ros.message.sensor_msgs.Image
		 * message) {
		 * 
		 * 
		 * org.ros.message.sensor_msgs.Image pubImage = new
		 * org.ros.message.sensor_msgs.Image(); pubImage.width = width;
		 * pubImage.height = height; pubImage.encoding = "rgb8";
		 * pubImage.is_bigendian = 0; pubImage.step = width*3; pubImage.data =
		 * dest.toArray(); vidPub.publish(pubImage); } });
		 */

		// END PART 4

		armStatusSub.addMessageListener(new MessageListener<ArmMsg>() {
			@Override
			public void onNewMessage(ArmMsg msg) {
				// Simply printing out the PWM values
				long[] pwmVals = msg.pwms;
//				for (int i = 0; i < 3; i++) {
//					System.out.println("PWM Value at Channel " + i + " is: "
//							+ pwmVals[i]);
//				}

				int shoulderPWM = (int) pwmVals[0];
				int wristPWM = (int) pwmVals[1];
				int gripperPWM = (int) pwmVals[2];

//				 rotateAllServos(shoulderPWM, wristPWM, gripperPWM);
				// if (!objectGrasped){
				// gripperServo.close(msg.pwms[2]);
				// }
				 moveArm(InverseKinematics.ARM_LENGTH + InverseKinematics.WRIST_LENGTH, 0, msg.pwms[0], msg.pwms[1]);

				// // Arm Gymnastics
				//
				// // TODO probably need to initialize everything to some
				// positions
				// // so will probably need to make an initialize state
				//
				// if (gymState == ArmGymState.OPEN_GRIPPER) {
				// if (gripperServo.isOpen(gripperPWM)) {
				// gymState = ArmGymState.CLOSE_GRIPPER;
				// } else {
				// writeGripperPWM(gripperServo.open(gripperPWM));
				// }
				// }
				//
				// if (gymState == ArmGymState.CLOSE_GRIPPER) {
				// if (gripperServo.isClosed(gripperPWM)) {
				// gymState = ArmGymState.MOVE_UP;
				// } else {
				// writeGripperPWM(gripperServo.close(gripperPWM));
				// }
				// }
				//
				// if (gymState == ArmGymState.MOVE_UP) {
				// if (shoulderServo.isGymUp(shoulderPWM)) {
				// gymState = ArmGymState.BEND_ELBOW;
				// } else {
				// writeShoulderPWM(shoulderServo.moveGymUp(shoulderPWM));
				// }
				// }
				//
				// // Assuming that bend elbow is bending wrist
				// if (gymState == ArmGymState.BEND_ELBOW) {
				// if (wristServo.isGymBent(wristPWM)) {
				// gymState = ArmGymState.MOVE_TO_GROUND;
				// } else {
				// writeWristPWM(wristServo.bendGym(wristPWM));
				// }
				// }
				//
				// if (gymState == ArmGymState.MOVE_TO_GROUND) {
				// if (shoulderServo.onGround(shoulderPWM)) {
				// // TODO: what to do here? just end?
				// } else {
				// writeShoulderPWM(shoulderServo.moveToGround(shoulderPWM));
				// }
				// }

//				// Grasp and Transport
//				if (graspState == ArmGraspState.INIT_WRIST) {
//					if (wristServo.isGymBent(wristPWM)) {
//						graspState = ArmGraspState.INIT_GRIPPER;
//					} else {
//						writeWristPWM(wristServo.bendGym(wristPWM));
//					}
//				}
//
//				if (graspState == ArmGraspState.INIT_GRIPPER) {
//					if (gripperServo.isOpen(gripperPWM)) {
//						graspState = ArmGraspState.GRASP;
//					} else {
//						writeGripperPWM(gripperServo.open(gripperPWM));
//					}
//				}
//
//				if (graspState == ArmGraspState.INIT_SHOULDER) {
//					if (shoulderServo.onGround(shoulderPWM)) {
//						graspState = ArmGraspState.GRASP;
//					} else {
//						writeShoulderPWM(shoulderServo
//								.moveToGround(shoulderPWM));
//					}
//				}
//
//				if (graspState == ArmGraspState.GRASP) {
//					// TODO: may need to do stuff with camera to make sure that
//					// the object is grasped
//					if (gripperServo.isClosed(gripperPWM) && objDetected) {
//						graspState = ArmGraspState.LIFT;
//					} else if (objDetected) {
//						writeGripperPWM(gripperServo.close(gripperPWM));
//					}
//				}
//
//				if (graspState == ArmGraspState.LIFT) {
//					// TODO: will need to have recovery methods
//					if (!objDetected) {
//						// Attempting recovery by restarting but probably need a
//						// recovery state and then align with block
//						graspState = ArmGraspState.INIT_WRIST;
//					} else {
//						if (shoulderServo.isGymUp(shoulderPWM)) {
//							graspState = ArmGraspState.MOVE;
//						} else {
//							writeShoulderPWM(shoulderServo
//									.moveGymUp(shoulderPWM));
//						}
//					}
//				}
//
//				// TODO: move and the rest of grasp and transport
			}
		});

		bumpersSub.addMessageListener(new MessageListener<BumpMsg>() {
			@Override
			public void onNewMessage(BumpMsg msg) {
//				objDetected = msg.gripper;
			}
		});
	}

	/**
	 * Moves the arm to the desired x, z position in the robot frame
	 * 
	 * @param desX
	 *            the desired x coordinate for the end effector
	 * @param desZ
	 *            the desired z coordinate for the end effector
	 */
	public void moveArm(double desX, double desZ, int currShoulderPWM,
			int currWristPWM) {
		// Inverse Kinematic Equation
		// TODO: don't know how to solve for the required angles
		// Let shoulderTheta, wristTheta be the angles to write for the shoulder
		// and wrist respectively
	    
	    double[] angles = InverseKinematics.getThetaPhi(desX, desZ, shoulderServo.getThetaRad(currShoulderPWM), wristServo.getThetaRad(currWristPWM));

		int desShoulderPWM = shoulderServo.getPWM(angles[0]);
		int desWristPWM = wristServo.getPWM(angles[1]);

		ArmMsg msg = new ArmMsg();
		msg.pwms[0] = shoulderServo.getSafePWM(currShoulderPWM, desShoulderPWM);
		msg.pwms[1] = wristServo.getSafePWM(currWristPWM, desWristPWM);
		armPWMPub.publish(msg);
	}

	/**
	 * Rotates all of the servos concurrently (the handle(ArmMsg msg) method)
	 */
	private void rotateAllServos(int shoulderPWM, int wristPWM, int gripperPWM) {
	    
	    int SHIFT_AMOUNT = 100;
		if (currState == State.UP) {
			// If all servos are at the UP state
			if (shoulderServo.atMax(shoulderPWM) && wristServo.atMax(wristPWM)
					&& gripperServo.isClosed(gripperPWM)) {
			    System.out.println("Switching to Down");
				currState = State.DOWN;
			} else {
				ArmMsg msg = new ArmMsg();
				
              msg.pwms[0] = Math.min(shoulderPWM + (shoulderServo.MAX_PWM - shoulderServo.MIN_PWM)/SHIFT_AMOUNT, shoulderServo.MAX_PWM);
              msg.pwms[1] = Math.min(wristPWM + (wristServo.MAX_PWM - wristServo.MIN_PWM)/SHIFT_AMOUNT, wristServo.MAX_PWM);
              msg.pwms[2] = Math.min(gripperPWM + (gripperServo.MAX_PWM - gripperServo.MIN_PWM)/SHIFT_AMOUNT, gripperServo.MAX_PWM);
				
//              System.out.println("Shoulder Theta: " + shoulderServo.getThetaDeg(msg.pwms[0]));
//              System.out.println("Wrist Theta: " + wristServo.getThetaDeg(msg.pwms[1]));

              
              //				msg.pwms[0] = shoulderServo.fullRotation(shoulderPWM, true);
//				msg.pwms[1] = wristServo.fullRotation(wristPWM, true);
//				msg.pwms[2] = gripperServo.fullRotation(gripperPWM, true);
//				System.out.println("Shoulder PWM: " + msg.pwms[0]);
				armPWMPub.publish(msg);
			}
		} else if (currState == State.DOWN) {

			// If all servos are at the DOWN state
			if (shoulderServo.atMin(shoulderPWM) && wristServo.atMin(wristPWM)
					&& gripperServo.isOpen(gripperPWM)) {
			    System.out.println("Switching to Up");
				currState = State.UP;
			} else {
				ArmMsg msg = new ArmMsg();
                msg.pwms[0] = Math.max(shoulderPWM - (shoulderServo.MAX_PWM - shoulderServo.MIN_PWM)/SHIFT_AMOUNT, shoulderServo.MIN_PWM);
                msg.pwms[1] = Math.max(wristPWM - (wristServo.MAX_PWM - wristServo.MIN_PWM)/SHIFT_AMOUNT, wristServo.MIN_PWM);
                msg.pwms[2] = Math.max(gripperPWM - (gripperServo.MAX_PWM - gripperServo.MIN_PWM)/SHIFT_AMOUNT, gripperServo.MIN_PWM);
//				msg.pwms[0] = shoulderServo.fullRotation(shoulderPWM, false);
//				msg.pwms[1] = wristServo.fullRotation(wristPWM, false);
//				msg.pwms[2] = gripperServo.fullRotation(gripperPWM, false);
                
//                System.out.println("Shoulder Theta: " + shoulderServo.getThetaDeg(msg.pwms[0]));
//                System.out.println("Wrist Theta: " + wristServo.getThetaDeg(msg.pwms[1]));
				armPWMPub.publish(msg);
			}
		}
		// TODO: Not quite sure what supposed to be doing, check with the TA,
		// makes no sense why should be publishing messages each time receive
		// arm pwm msg
		// clamped ff control step for each servo? is this just moving at most 1
		// radian per control step?
		// need to first see the range of motion of the servo, most likely 180
		// deg of rotation so only need 2 states for each direction
		// or maybe publish a message to get to the endgoal if going up or down,
		// feed into the controlle rthe current position and whether
		// going up or down, and then it will return a pwm that is at most 1
		// radian max towards the max/min pwm at the end of the state?

		// public armMsg as follows
		// long bigPWM = msg.pwms[0];
		// long wristPWM = msg.pwms[1];
		// long gripperPWM = msg.pwms[2];
	}

	/**
	 * Writes the provided PWM value for the shoulder servo
	 * 
	 * @param value
	 *            the PWM value to write to the shoulder servo
	 */
	private void writeShoulderPWM(int value) {
		ArmMsg msg = new ArmMsg();
		msg.pwms[0] = value;
		armPWMPub.publish(msg);
	}

	/**
	 * Writes the provided PWM value for the wrist servo
	 * 
	 * @param value
	 *            the PWM value to write to the wrist servo
	 */
	private void writeWristPWM(int value) {
		ArmMsg msg = new ArmMsg();
		msg.pwms[1] = value;
		armPWMPub.publish(msg);
	}

	/**
	 * Writes the provided PWM value for the gripper servo
	 * 
	 * @param value
	 *            the PWM value to write to the gripper servo
	 */
	private void writeGripperPWM(int value) {
		ArmMsg msg = new ArmMsg();
		msg.pwms[2] = value;
		armPWMPub.publish(msg);
	}

	@Override
	public void onShutdown(Node node) {
		if (node != null) {
			node.shutdown();
		}
	}

	@Override
	public void onShutdownComplete(Node node) {
	}

	@Override
	public GraphName getDefaultNodeName() {
		return new GraphName("rss/grasping");
	}
}