package controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import main.GlobalValues;
import main.Transforms;
import model.MVData;
import model.MetadataResults;

public class MVMetric {
	int width = GlobalValues.WIDTH;
	int height = GlobalValues.HEIGHT;
	String[] filenames;
	String currentimage = "";
	int frameIndex;
	int index;
	BufferedImage prev;

	ArrayList<MVData> differenceFrames;
	ArrayList<MVData> dbFrames;

	ArrayList<String> sortedListOfVideos;
	ArrayList<MetadataResults> results;

	double avgDifference = 0;

	public ArrayList<String> getSortedListOfVideos() {
		return sortedListOfVideos;
	}

	public ArrayList<MetadataResults> getResults() {
		return results;
	}

	public void searchWithQuery(String filename) {
		differenceFrames = new ArrayList<>();
		sortedListOfVideos = new ArrayList<>();
		results = new ArrayList<>();
		frameIndex = 0;
		String file = GlobalValues.QUERY_CLIP_DIRECTORY_PATH + filename
				+ ".rgb";

		searchFile(file);
		dbFrames = readMVFromFile();

		compareFrames();
	}

	void compareFrames() {
		System.out.println("Comparing clip with database...");
		int i = 0;
		int j = 0;
		double red = 0, green = 0, blue = 0, yellow = 0, dbY=0, Y=0;
		while (i < dbFrames.size() - differenceFrames.size()) {
			MetadataResults result = new MetadataResults();
			j = 0;
			red = green = blue = yellow = Y = dbY = 0;
			while (j < differenceFrames.size()) {

				red += (dbFrames.get(i + j).getColor());
				yellow += differenceFrames.get(j).getColor();

				green += dbFrames.get(i + j).getDifference();
				blue += differenceFrames.get(j).getDifference();
				
				dbY += dbFrames.get(i + j).getY();
				Y += differenceFrames.get(j).getY();

				j++;
			}
			double value1 = (blue - green) * 17 / differenceFrames.size() ;	//4.7;// Motion vector
			double value2 = (yellow - red) * 2 / differenceFrames.size();	//3; // Color
			double value3 = (Y - dbY) * 2 / differenceFrames.size();		//2.4; // Y
			
			if(value1 > 0 && value1 < 10)
				value2*=12;
			else
				value3*=12;

			if(dbFrames.get(i).getVideoname().equals("soccer1"))
				System.out.println(value1 + " : " + value2 + " : " + value3 + "  - - " + dbFrames.get(i).getVideoname());

			double avg = (Math.abs(value2) + Math.abs(value1) + Math.abs(value3));
			result.setSimilarity(avg);
			result.setFrameIndex(i);
			result.setVideoname(dbFrames.get(i).getVideoname());
			results.add(result);
			i++;
		}
		sortResults();
	}

	void sortResults() {
		ArrayList<MetadataResults> sortedVideoSimilarityResult = new ArrayList<>(
				results);
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
			videoNames.add(result.getVideoname());
		}

		for (String val : videoNames)
		{
//			if(!videoData.containsKey(val))
//				videoData.put(val, 0);
//			else
//				videoData.put(val, videoData.get(val) + 1);
			
			if (!sortedListOfVideos.contains(val))// && videoData.get(val) > 20)
				sortedListOfVideos.add(val);
		}
	}

	public MVMetric() {
		folders();
		File f = new File(GlobalValues.METADATA_MV_FILENAME);
		if (!f.exists()) {
			differenceFrames = new ArrayList<>();
			readAllvideoFilesForMetaData();
			saveData();
		}
	}

	void folders() {
		File folder = new File(GlobalValues.FULL_VIDEOS_DIRECTORY_PATH);
		File[] files = folder.listFiles();
		filenames = new String[files.length];
		int i = 0;

		for (File file : files) {
			if (file.isDirectory()) {
				filenames[i] = file.getName();
				i++;
			}
		}
	}

	void readAllvideoFilesForMetaData() {
		String filename;
		System.out.println("Reading meta data from all videos in database....");
		for (String file : filenames) {
			frameIndex = 0;
			currentimage = file;
			if (file != null) {
				filename = GlobalValues.FULL_VIDEOS_DIRECTORY_PATH + file
						+ "//" + file + ".rgb";
				searchFile(filename);
			}
		}
	}

	ArrayList<MVData> readMVFromFile() {
		System.out.println("Reading data from file.....");
		ArrayList<MVData> difference = new ArrayList<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					GlobalValues.METADATA_MV_FILENAME));
			String line;
			while ((line = br.readLine()) != null) {
				difference.add(new MVData(line.split(",")));
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("File contains " + difference.size()
				+ " entries.....");
		return difference;
	}

	void searchFile(String filename) {
		System.out.println("Preparing data from " + filename + "........");
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
		System.out
				.println("Info from " + frameIndex + " frames collected.....");
	}

	void saveData() {
		System.out.println("Writing to file....");
		try {
			PrintWriter writer = new PrintWriter(
					GlobalValues.METADATA_MV_FILENAME, "UTF-8");
			for (int i = 0; i < differenceFrames.size(); i++) {
				MVData data = differenceFrames.get(i);
				writer.println(data.getVideoname() + "," + data.getFrameIndex()
						+ "," + data.getDifference() + "," + data.getColor() + "," +data.getY());
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Meta data stored successfully....");
	}

	void prepareFrame(byte[] bytes) {
		index++;
		frameIndex++;
		byte red = 0, blue = 0, green = 0;
		int ind = 0, line = 0, pixel = 0;
		double color = 0, Y = 0;

		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (line = 0; line < height; line++) {
			for (pixel = 0; pixel < width; pixel++) {
				red = bytes[ind];
				green = bytes[ind + height * width];
				blue = bytes[ind + height * width * 2];
				int pix = 0xff000000 | ((red & 0xff) << 16)
						| ((green & 0xff) << 8) | (blue & 0xff);
				img.setRGB(pixel, line, pix);

				double[][] localrgb = new double[][] { { (red) },{ (green) },
				 { (blue) } };
				color += (Math.abs(red) + Math.abs(green) + Math.abs(blue)) / 3;
				//color += Transforms.RGBtoHSV(red, green, blue);
				double[][] yuv = Transforms.RGBtoYUV(localrgb); 
				Y += yuv[0][0];
				ind++;
			}
		}
		if (frameIndex > 1) {
			double difference = findMotionVector(img, 16);
			
			if(difference > -11 && difference < 11)
				Y=0;
			else
				color=0;

			differenceFrames.add(new MVData(new String[] { currentimage,
					frameIndex + "", difference + "",
					color / (width * height) + "" , Y/(width * height) + ""}));
		}
		prev = img;
	}

	double findMotionVector(BufferedImage img, int level) {
		double[][] frame = motionVector(prev, img, level);

		double netMotion = 0;
		for (int i = 0; i < frame.length; i++) {
			for (int j = 0; j < frame[0].length; j++) {
				netMotion += frame[i][j];
			}
		}
		return netMotion / (10 * level * level);
	}

	double[][] motionVector(BufferedImage frame1, BufferedImage frame2,
			int level) {
		int w = width;
		int h = height;

		int m = level;
		int n = level;

		int k = 0, l = 0, i = 0, j = 0;
		double sum = 0;

		double[][] MAD = new double[w][h];
		while (i < w) {
			j = 0;
			while (j < h) {
				k = 0;
				sum = 0;
				while (k < m) {
					l = 0;
					while (l < n) {
						int rgb = frame2.getRGB(k, l);
						int r = (rgb >> 16) & 0xff;
						int g = (rgb >> 8) & 0xff;
						int b = (rgb) & 0xff;
						double[][] localrgb = new double[][] { { (r) },
								{ (g) }, { (b) } };
						double frame2Y = Transforms.RGBtoYUV(localrgb)[0][0];

						rgb = frame1.getRGB(k + i, l + j);
						r = (rgb >> 16) & 0xff;
						g = (rgb >> 8) & 0xff;
						b = (rgb) & 0xff;
						localrgb = new double[][] { { (r) }, { (g) }, { (b) } };
						double frame1Y = Transforms.RGBtoYUV(localrgb)[0][0];

						sum += frame2Y - frame1Y;
						l++;
					}
					k++;
				}
				MAD[i][j] = Math.abs(sum) / (m * n);
				j += n;
			}
			i += m;
		}
		return MAD;
	}
}