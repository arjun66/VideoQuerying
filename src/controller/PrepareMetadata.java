package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import main.GlobalValues;

public class PrepareMetadata {
	int width = GlobalValues.WIDTH;
	int height = GlobalValues.HEIGHT;

	String[] filenames;
	PrintWriter writer;
	String currentimage;

	int frameIndex;
	
	public PrepareMetadata()
	{
		folders();
		readAllvideoFilesForMetaData();
	}

	void folders()
	{
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
		try {
			writer = new PrintWriter(GlobalValues.METADATA_FILENAME, "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (String file : filenames) {
			frameIndex = 0;
			currentimage = file;
			if (file != null)
				searchFile(file);
		}
		writer.close();
	}

	void searchFile(String filename) {
		filename = GlobalValues.FULL_VIDEOS_DIRECTORY_PATH + "\\" + filename + "\\"
				+ filename + ".rgb";
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

	void saveData(double red, double green, double blue) {
		if (red < 0)
			red = -1 * red;
		if (green < 0)
			green = -1 * green;
		if (blue < 0)
			blue = -1 * blue;
		writer.println(currentimage + "," + frameIndex + "," + red
				+ "," + green + "," + blue );
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
		saveData(red, blue, green);
	}
}