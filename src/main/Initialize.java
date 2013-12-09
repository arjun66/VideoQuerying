package main;

import view.Interface;
import controller.MVMetric;

public class Initialize {
	Interface start;
	MVMetric data;

	public Initialize(String queryFilename) {
		start = new Interface();
		Thread t1 = new MetaData(this);
		t1.start();

		new PlayVideo(this, queryFilename).start();
		while (t1.isAlive()) {
		}
		new CompareFiles(this, queryFilename).start();
	}

	public void prepareMetaData() {
		data = new MVMetric();
	}

	public void compareFiles(String filename) {
		data.searchWithQuery(filename);
		start.setResults(data.getSortedListOfVideos(), data.getResults());
	}

	public void playVideo(String filename) {
		start.playQueryVideo(filename);
	}

	public static void main(String[] args) {
		new Initialize("query6");
	}
}

class MetaData extends Thread {
	Initialize init;

	public MetaData(Initialize init) {
		this.init = init;
	}

	@Override
	public void run() {
		init.prepareMetaData();
		stop();
	}
}

class PlayVideo extends Thread {
	Initialize init;
	String filename;

	public PlayVideo(Initialize init, String filename) {
		this.init = init;
		this.filename = filename;
	}

	@Override
	public void run() {
		init.playVideo(filename);
	}
}

class CompareFiles extends Thread {
	Initialize init;
	String filename;

	public CompareFiles(Initialize init, String filename) {
		this.init = init;
		this.filename = filename;
	}

	@Override
	public void run() {
		init.compareFiles(filename);
	}
}
