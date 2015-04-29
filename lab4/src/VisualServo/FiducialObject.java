package VisualServo;

public class FiducialObject {

	private BlobObject top;
	private BlobObject bottom;
	private double distanceTo;

	public FiducialObject(BlobObject top, BlobObject bottom) {
		this.top = top;
		this.bottom = bottom;
		this.distanceTo = (top.getDistToCentroid() + bottom.getDistToCentroid()) / 2.0;
	}

	public double getDistanceTo() {
		return this.distanceTo;
	}

}
