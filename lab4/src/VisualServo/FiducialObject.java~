package VisualServo;

public class FiducialObject {
    private BlobObject top;
    private BlobObject bottom;
	private double distanceTo;

	public FiducialObject() {

	}

	/**
	 * Generates a fiducial object
	 * @param top_in the top BlobObject
	 * @param bottom_in the bottom BlobObject
	 */
	public FiducialObject(BlobObject top_in, BlobObject bottom_in) {
		this.top = top_in;
		this.bottom = bottom_in;
		this.distanceTo = (top.getDistToCentroid() + bottom.getDistToCentroid()) / 2.0;
	}

	/**
	 * Returns the distance to the center of the fiducial object from the robot's current position
	 */
	public double getDistanceTo() {
		return this.distanceTo;
	}

	/**
	 * Returns the top blob object
	 */
    public BlobObject getTop() {
        return top;
    }

    /**
     * Returns the bottom blob object
     */
    public BlobObject getBottom() {
        return bottom;
    }
}
