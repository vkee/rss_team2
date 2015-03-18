//package Grasping;
//
//import java.util.concurrent.ArrayBlockingQueue;
//
//import VisualServo.*;
//
//public class VisualBlob extends VisualServo {
//
//
//	public VisualBlob() {
//		super();
//	}
//
//	public void run() {
//		while (true) {
//			Image src = null;
//			try {
//				src = new Image(visionImage.take(), width, height);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//				continue;
//			}
//
//			Image dest = new Image(src);
//
//			blobTrack.apply(src, dest);
//
//			// update newly formed vision message
//			// gui.setVisionImage(dest.toArray(), width, height);
//
//			// Begin Student Code
//
//			// publish velocity messages to move the robot towards the target
////			MotionMsg msg = new MotionMsg(); // (Solution)
////			msg.translationalVelocity = blobTrack.translationVelocityCommand; // (Solution)
////			msg.rotationalVelocity = blobTrack.rotationVelocityCommand; // (Solution)
////			publisher.publish(msg); // (Solution)
//
//			// End Student Code
//		}
//
//	}
//}
