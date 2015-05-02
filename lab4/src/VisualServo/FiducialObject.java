package VisualServo;

public class FiducialObject {

	private BlobObject top;
	private BlobObject bottom;
	private double distanceTo;

	public FiducialObject() {

	}

	public FiducialObject(BlobObject top_in, BlobObject bottom_in) {
		this.top = top_in;
		this.bottom = bottom_in;
		this.distanceTo = (top.getDistToCentroid() + bottom.getDistToCentroid()) / 2.0;
	}

	public double getDistanceTo() {
		return this.distanceTo;
	}


}
