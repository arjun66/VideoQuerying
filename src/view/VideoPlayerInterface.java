package view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.GlobalValues;
import model.MetadataResults;
import controller.VideoPlayer;

public class VideoPlayerInterface extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int width = GlobalValues.WIDTH;
	int height = GlobalValues.HEIGHT;

	Timeline timeline;
	JLabel graph;
	VideoPlayer player;
	JLabel frame;

	ArrayList<MetadataResults> results;

	double max = 0, min = 999999999;

	public VideoPlayerInterface() {
		super();
		setLayout(null);

		frame = new JLabel();
		frame.setBorder(BorderFactory.createLineBorder(Color.black));
		frame.setBounds(0, 0, width, height);
		frame.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				player.pauseResume();
			}
		});

		timeline = new Timeline(new Rectangle(0, height - 20, width, 20)) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onSeek(int frame) {
				player.play(frame);
			}
		};
		timeline.setForeground(Color.WHITE);

		graph = new JLabel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 0.5f));
				super.paint(g2);
				g2.dispose();
			}
		};
		graph.setOpaque(false);
		add(frame);
		frame.add(timeline);
		timeline.add(graph);
	}

	public void play(ArrayList<MetadataResults> results, String filename,
			boolean query) {
		this.results = results;
		if (!query) {
			timeline.setBounds(timeline.getX(), height - 50,
					timeline.getWidth(), 50);
			graph.setBounds(0, 0, timeline.getWidth(), timeline.getHeight());
			drawGraph(filename, graph.getWidth(), graph.getHeight());
		}
		if (player != null)
			player.stopVideo();
		player = new VideoPlayer(filename, frame, timeline, query);
	}

	void drawGraph(String filename, int width, int height) {
		ArrayList<MetadataResults> frames = new ArrayList<>();

		for (MetadataResults result : results) {
			if (result.getVideoname().equals(filename))
				frames.add(result);
		}

		for (MetadataResults result : results) {
			if (max < result.getSimilarity())
				max = result.getSimilarity();
			if (min > result.getSimilarity())
				min = result.getSimilarity();
		}

		BufferedImage points = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < frames.size(); i++) {
			int pix = 0xff000000 | (((byte) 255 & 0xff) << 16)
					| (((byte) 255 & 0xff) << 8) | ((byte) 255 & 0xff);
			int x = (i * width) / frames.size();
			double y = (((frames.get(i).getSimilarity() - min) * height) / (max - min));
			if (frames.get(i).getSimilarity() == 0) {
				for (int k = 0; k < height; k++) {
					int line = 0xff000000 | (((byte) 255 & 0xff) << 16)
							| (((byte) 0 & 0xff) << 8) | ((byte) 0 & 0xff);
					points.setRGB(x, k, line);
					try {
						points.setRGB(x + 1, k, line);
						points.setRGB(x + 2, k, line);
					} catch (Exception e) {

					}
				}
			}
			if (x < width && y < height)
				points.setRGB(x, (int) y, pix);
		}
		graph.setIcon(new ImageIcon(points));
	}
}