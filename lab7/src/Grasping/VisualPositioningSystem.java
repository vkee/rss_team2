package Grasping;

import VisualServo.BlobTracking;

public class VisualPositioningSystem extends BlobTracking {
	/**
	 * Description:
	 * 
	 * 1 colored 3D-tag on arm (not the wrist). In perspective of the camera, as
	 * the arm rises the size of the sticker increases. That difference can be
	 * measured and correlates to a distance. We know the length of the arm and
	 * distance from camera to arm shoulder joint. We have measured the size of
	 * the sticker for at least 3 distances (probably should do more like 8).
	 * Those distances correspond to angular displacement of the arm. We can
	 * approximate our measurements quadratically. This approximation can allow
	 * us to produce a pd-controller because we have successfully related the
	 * perceived size of the sticker to distance from camera and hence angle of
	 * arm rotation.
	 * 
	 * 
	 */

	public VisualPositioningSystem(int width, int height) {
		super(width, height);
	}

	protected void changeFixationDistance(double distance) {
		desiredFixationDistance = distance;
	}

	protected void trackRed() {
		double hue = 0.48; // CHANGE COLOR
		trackColor(hue);
	}

	/**
	 * TODO ADD API-Like calls to find various configurations of blocks when
	 * given an image
	 */

	protected void trackColor(double hue) {
		targetHueLevel = hue;
	}

	public static void getPosition() {

	}

	public static void main(String[] args) {
	}

}
