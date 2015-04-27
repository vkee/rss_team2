package VisualServo;

import java.awt.Color;

public class BlobObject {

	private Color color;
	private Object targetArea;
	private double centroidX;
	private double centroidY;
	private int[] blobArr;

	public BlobObject(double centroidX, double centroidY, int targetArea,
			int[] blobArr) {
		this.centroidX = centroidX;
		this.centroidY = centroidY;
		this.targetArea = targetArea;
		this.blobArr = blobArr;
	}

	public BlobObject(int[] blobArr){
		this.blobArr = blobArr;
		
	}
	public double getCentroidX() {
		return this.centroidX;
	}

	public double getCentroidY() {
		return this.centroidY;
	}

	public int getBlobArrIndex(int index) {
		return blobArr[index];
	}

	public Color getColor() {
		return this.color;
	}

}
