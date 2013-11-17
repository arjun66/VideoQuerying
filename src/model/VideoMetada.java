package model;

public class VideoMetada {
	String imagename;
	int frameIndex;
	double red;
	double green;
	double blue;
	
	public VideoMetada(String[] values) {
		this.imagename=values[0];
		this.frameIndex=Integer.parseInt(values[1]);
		this.red=Double.parseDouble(values[2]);
		this.green=Double.parseDouble(values[3]);
		this.blue=Double.parseDouble(values[4]);
	}

	public String getImagename() {
		return imagename;
	}

	public void setImagename(String imagename) {
		this.imagename = imagename;
	}

	public int getFrameIndex() {
		return frameIndex;
	}

	public void setFrameIndex(int frameIndex) {
		this.frameIndex = frameIndex;
	}

	public double getRed() {
		return red;
	}

	public void setRed(double red) {
		this.red = red;
	}

	public double getGreen() {
		return green;
	}

	public void setGreen(double green) {
		this.green = green;
	}

	public double getBlue() {
		return blue;
	}

	public void setBlue(double blue) {
		this.blue = blue;
	}
}