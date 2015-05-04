package VisualServo;

public class FiducialObject {
	private BlobObject top;
	private BlobObject bottom;
	private double distanceTo;

	int centroidX;
	int centroidY;
	private int fiducialNumber;

	public FiducialObject(BlobObject top_in, BlobObject bottom_in, int i) {
		this.top = top_in;
		this.bottom = bottom_in;
		this.distanceTo = (top.getDistToCentroid() + bottom.getDistToCentroid()) / 2.0;
		this.centroidX = (int) ((top.getCentroidX() + bottom.getCentroidX()) / 2.0);
		this.centroidY = (int) ((top.getCentroidY() + bottom.getCentroidY()) / 2.0);
		this.fiducialNumber = i;
	}

	/**
	 * Generates a fiducial object
	 * 
	 * @param top_in
	 *            the top BlobObject
	 * @param bottom_in
	 *            the bottom BlobObject
	 */
	public FiducialObject(BlobObject top_in, BlobObject bottom_in) {
		this.top = top_in;
		this.bottom = bottom_in;
		this.distanceTo = (top.getDistToCentroid() + bottom.getDistToCentroid()) / 2.0;
		this.centroidX = (int) ((top.getCentroidX() + bottom.getCentroidX()) / 2.0);
		this.centroidY = (int) ((top.getCentroidY() + bottom.getCentroidY()) / 2.0);

	}

	/**
	 * Returns the distance to the center of the fiducial object from the
	 * robot's current position
	 */
	public double getDistanceTo() {
		return this.distanceTo;
	}

	public int getCentroidX() {
		return this.centroidX;
	}

	public int getCentroidY() {
		return this.centroidY;
	}

	/**
	 * Returns the top blob object
	 */
	public BlobObject getTop() {
		return this.top;
	}

	/**
	 * Returns the bottom blob object
	 */
	public BlobObject getBottom() {
		return this.bottom;
	}
	
	public int getFiducialNumber(){
		return this.fiducialNumber;
	}

}
