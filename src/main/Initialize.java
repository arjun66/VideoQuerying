package main;

import controller.PrepareMetadata;
import controller.QueryVideo;
import view.Interface;

public class Initialize {
	Interface start;
	PrepareMetadata data;
	QueryVideo query;

	public Initialize() {
		start=new Interface();
		data=new PrepareMetadata();
		search("query1");
	}

	void search(String filename)
	{
		query=new QueryVideo(filename);
		start.setResults(query.getSortedListOfVideos(), query.getVideoSimilarityResult());
		start.playQueryVideo(filename);
	}

	public static void main(String[] args) {
		new Initialize();
	}
}