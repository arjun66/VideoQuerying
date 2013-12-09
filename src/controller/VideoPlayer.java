package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import main.GlobalValues;
import view.Timeline;

public class VideoPlayer {
	int height = GlobalValues.HEIGHT;
	int width = GlobalValues.WIDTH;
	String filename;

	ArrayList<BufferedImage> images;

	JLabel player;

	static int frameNumber;
	AudioPlayer audioPlayer;
	Timeline timeline;

	Timer timer;

	boolean pause;

	public VideoPlayer(String filename, JLabel player, Timeline timeline,
			boolean query) {
		this.player = player;
		this.timeline = timeline;
		if (query)
			filename = GlobalValues.QUERY_CLIP_DIRECTORY_PATH + "//" + filename;
		else
			filename = GlobalValues.FULL_VIDEOS_DIRECTORY_PATH + "//"
					+ filename + "//" + filename;
		String videofilename = filename + ".rgb";
		String audiofilename = filename + ".wav";
		this.filename = videofilename;
		audioPlayer = new AudioPlayer(audiofilename);
		// a = new PlayWaveFile(audiofilename, 274);
		images = new ArrayList<>();

		parseVideo();
		play(0);
	}

	public void parseVideo() {
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
		int ind = 0, line = 0, pixel = 0;
		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (line = 0; line < height; line++) {
			for (pixel = 0; pixel < width; pixel++) {
				byte r = bytes[ind];
				byte g = bytes[ind + height * width];
				byte b = bytes[ind + height * width * 2];

				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8)
						| (b & 0xff);
				// int pix = ((a << 24) + (r << 16) + (g << 8) + b);
				img.setRGB(pixel, line, pix);
				ind++;
			}
		}
		images.add(img);
	}

	public void pauseResume() {
		if (pause)
		{
			stopVideo();
			pause = false;
		}
		else
			play(frameNumber);
	}

	public void stopVideo() {
		timer.cancel();
		timer = null;
		audioPlayer.stop();
	}

	public void play(final int frame) {
		pause=true;
		if (timer != null) {
			timer.cancel();
		}
		frameNumber = frame;
		timeline.setMaximum(images.size());
		timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				player.setIcon(new ImageIcon(images.get(frameNumber)));
				frameNumber++;
				timeline.setValue(frameNumber);
				if (frameNumber >= images.size()) {
					timer.cancel();
				}
			}
		};
		timer.scheduleAtFixedRate(task, 0, (1000 / 25));
		audioPlayer.play(frameNumber * 1764);
	}
}