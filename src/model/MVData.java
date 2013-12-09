package model;

public class MVData {
	String videoname;
	int frameIndex;
	double difference;
	double color;
	double Y;
	
	public double getY() {
		return Y;
	}

	public void setY(double y) {
		Y = y;
	}

	public double getColor() {
		return color;
	}

	public void setColor(double color) {
		this.color = color;
	}

	public MVData(String[] values) {
		this.videoname=values[0];
		this.frameIndex=Integer.parseInt(values[1]);
		this.difference=Double.parseDouble(values[2]);
		this.color=Double.parseDouble(values[3]);
		this.Y=Double.parseDouble(values[4]);
	}

	public String getVideoname() {
		return videoname;
	}

	public double getDifference() {
		return difference;
	}

	public void setDifference(double h) {
		this.difference = h;
	}

	public void setVideoname(String videoname) {
		this.videoname = videoname;
	}

	public int getFrameIndex() {
		return frameIndex;
	}

	public void setFrameIndex(int frameIndex) {
		this.frameIndex = frameIndex;
	}
}