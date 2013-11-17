package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import main.GlobalValues;
import model.MetadataResults;
import model.VideoMetada;

public class QueryVideo {
	int width = GlobalValues.WIDTH;
	int height = GlobalValues.HEIGHT;

	int frameIndex;

	ArrayList<VideoMetada> dbOfVideos;
	ArrayList<VideoMetada> queryVideo;

	ArrayList<MetadataResults> videoSimilarityResult;
	ArrayList<MetadataResults> sortedVideoSimilarityResult;

	ArrayList<String> sortedListOfVideos;

	public QueryVideo(String filename) {
		dbOfVideos = new ArrayList<>();
		queryVideo = new ArrayList<>();
		videoSimilarityResult = new ArrayList<>();
		sortedVideoSimilarityResult = new ArrayList<>();
		sortedListOfVideos = new ArrayList<>();

		readAllvideoFilesForMetaData();
		searchWithVideo(filename);
		System.out.println(dbOfVideos.size() + " - " + queryVideo.size());

		compareVideoFiles();
	}

	public ArrayList<String> getSortedListOfVideos() {
		return sortedListOfVideos;
	}

	public ArrayList<MetadataResults> getVideoSimilarityResult() {
		return videoSimilarityResult;
	}

	ArrayList<String> compareVideoFiles() {
		int i = 0;
		int j = 0;
		double red = 0, green = 0, blue = 0;
		while (i < dbOfVideos.size() - queryVideo.size()) {
			MetadataResults result = new MetadataResults();
			j = 0;
			red = green = blue = 0;
			while (j < queryVideo.size()) {
				red += dbOfVideos.get(i + j).getRed()
						- queryVideo.get(j).getRed();
				green += dbOfVideos.get(i + j).getGreen()
						- queryVideo.get(j).getGreen();
				blue += dbOfVideos.get(i + j).getBlue()
						- queryVideo.get(j).getBlue();
				j++;
			}
			double avg = (red + blue + green) / 300;
			if (avg < 0)
				avg = -avg;
			result.setSimilarity(avg);
			result.setFrameIndex(i);
			result.setVideoname(dbOfVideos.get(i).getImagename());
			videoSimilarityResult.add(result);
			i++;
		}
		sortResults();
		return sortedListOfVideos;
	}

	void sortResults() {
		sortedVideoSimilarityResult = new ArrayList<>(videoSimilarityResult);
		Collections.sort(sortedVideoSimilarityResult,
				new Comparator<MetadataResults>() {
					@Override
					public int compare(MetadataResults o1, MetadataResults o2) {
						return Double.compare(o1.getSimilarity(),
								o2.getSimilarity());
					}
				});

		ArrayList<String> videoNames = new ArrayList<>();
		for (MetadataResults result : sortedVideoSimilarityResult) {
			// System.out.println(result.getSimilarity()+","+
			// result.getVideoname());
			videoNames.add(result.getVideoname());
		}
		int repeat = 0;
		for (String val : videoNames) {
			if (!sortedListOfVideos.contains(val)  && repeat > 5) {
				sortedListOfVideos.add(val);
				repeat = 0;
			} else
				repeat++;
		}
	}

	void saveVideoComparisonResult(MetadataResults result) {
		videoSimilarityResult.add(result);
	}

	void readAllvideoFilesForMetaData() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					GlobalValues.METADATA_FILENAME));
			String line;
			while ((line = br.readLine()) != null) {
				dbOfVideos.add(new VideoMetada(line.split(",")));
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void searchWithVideo(String filename) {
		searchFile(filename);
	}

	void searchFile(String filename) {
		filename = GlobalValues.QUERY_CLIP_DIRECTORY_PATH + "\\" + filename
				+ ".rgb";
		InputStream is = null;
		try {
			is = new FileInputStream(new File(filename));

			byte[] bytes = new byte[width * height * 3];
			int bytesRead = 0;
			while ((bytesRead = is.read(bytes, 0, bytes.length)) != -1) {
				if (bytesRead == width * height * 3) {
					prepareFrame(bytes);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void prepareFrame(byte[] bytes) {
		frameIndex++;
		long red = 0, blue = 0, green = 0;
		int ind = 0, line = 0, pixel = 0;
		for (line = 0; line < height; line++) {
			for (pixel = 0; pixel < width; pixel++) {
				red += (int) bytes[ind];
				green += (int) bytes[ind + height * width];
				blue += (int) bytes[ind + height * width * 2];
				ind++;
			}
		}
		saveData(red, green, blue);
	}

	void saveData(double red, double green, double blue) {
		if (red < 0)
			red = -1 * red;
		if (green < 0)
			green = -1 * green;
		if (blue < 0)
			blue = -1 * blue;
		queryVideo.add(new VideoMetada(new String[] { "", frameIndex + "",
				"" + red, "" + green, "" + blue }));
	}
}