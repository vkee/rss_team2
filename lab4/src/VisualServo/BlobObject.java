package VisualServo;

import java.awt.Color;

public class BlobObject {

	private Color color;
	private int targetArea;
	private double centroidX;
	private double centroidY;
	private int[] blobArr;
	private int distToCentroid;

	public BlobObject(double centroidX, double centroidY, int distToCentroid,
			int targetArea, Color color, int[] blobArr) {
		this.centroidX = centroidX;
		this.centroidY = centroidY;
		this.targetArea = targetArea;
		this.blobArr = blobArr;
		this.color = color;
		this.distToCentroid = distToCentroid;
	}

	public BlobObject(int[] blobArr) {
		this.blobArr = blobArr;

	}

	public double getCentroidX() {
		return this.centroidX;
	}

	public double getCentroidY() {
		return this.centroidY;
	}

	public int getDistToCentroid() {
		return this.distToCentroid;
	}

	public int getBlobArrIndex(int index) {
		return blobArr[index];
	}

	public Color getColor() {
		return this.color;
	}

	public int getTargetArea() {
		return this.targetArea;
	}

	public int[] getBlobArr() {
		return this.blobArr;
	}
}
