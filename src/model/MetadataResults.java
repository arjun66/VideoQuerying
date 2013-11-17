package model;

public class MetadataResults {
	String videoname;
	int frameIndex;
	double similarity;
	public String getVideoname() {
		return videoname;
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
	public double getSimilarity() {
		return similarity;
	}
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
}