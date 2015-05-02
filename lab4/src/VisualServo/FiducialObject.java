package VisualServo;

public class FiducialObject {

	private BlobObject top;
	private BlobObject bottom;
	private double distanceTo;

	public FiducialObject() {

	}

	public FiducialObject(BlobObject top, BlobObject bottom) {
		this.top = top;
		this.bottom = bottom;
		this.distanceTo = (top.getDistToCentroid() + bottom.getDistToCentroid()) / 2.0;
	}

	public double getDistanceTo() {
		return this.distanceTo;
	}

	public void addTop(BlobObject top_in) {
		this.top = top_in;
	}

	public void addBottom(BlobObject bottom_in) {
		this.bottom = bottom_in;
	}

}
