package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.GlobalValues;
import model.MetadataResults;

public class Interface {
	int width = GlobalValues.WIDTH;
	int height = GlobalValues.HEIGHT;
	JPanel videoList;

	VideoPlayerInterface queryVideo;
	VideoPlayerInterface fullVideo;

	JPanel originalVideoPanel;

	ArrayList<MetadataResults> results;

	public Interface() {
		JFrame window = new JFrame();
		window.setTitle(GlobalValues.WINDOW_TITLE);
		window.setLayout(null);
		window.setVisible(true);
		window.setAlwaysOnTop(true);
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		int screenWidth = gd.getDisplayMode().getWidth();
		int screenHeight = gd.getDisplayMode().getHeight();
		window.setBounds((screenWidth - ((width * 2) + 80)) / 2,
				(screenHeight - ((height * 2) + 100)) / 2, width * 2 + 80,
				height * 2 + 100);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 1st panel
		JPanel queryVideoPanel = new JPanel();
		// queryVideoPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		window.add(queryVideoPanel, BorderLayout.EAST);
		queryVideoPanel.setLayout(null);
		queryVideoPanel.setBounds(0, 0, window.getWidth() / 2,
				window.getHeight() / 2);

		queryVideo = new VideoPlayerInterface();
		queryVideoPanel.add(queryVideo);
		queryVideo.setBounds(10, 10, width, height + 15);

		// 2nd panel
		originalVideoPanel = new JPanel();
		// originalVideoPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
		window.add(originalVideoPanel, BorderLayout.EAST);
		originalVideoPanel.setLayout(null);
		originalVideoPanel.setBounds(0, window.getHeight() / 2,
				window.getWidth() / 2, window.getHeight() / 2);
		fullVideo = new VideoPlayerInterface();
		originalVideoPanel.add(fullVideo);
		fullVideo.setBounds(10, 10, width, height + 15);

		// 3rd panel
		videoList = new JPanel();
		window.add(videoList, BorderLayout.WEST);
		videoList.setLayout(null);
		videoList.setBounds(originalVideoPanel.getWidth(), 40,
				window.getWidth() - originalVideoPanel.getWidth(),
				window.getHeight());
	}

	public void playQueryVideo(String filename) {
		queryVideo.play(results, filename, true);
	}

	public void setResults(ArrayList<String> table,
			final ArrayList<MetadataResults> results) {

		int j = 0;
		this.results = results;
		for (int i = 0; i < table.size(); i++) {
			final JButton button = new JButton(table.get(i));
			button.setAlignmentX(Component.LEFT_ALIGNMENT);
			button.setBounds(10, j, videoList.getWidth() - 20, 40);
			videoList.add(button);
			button.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					fullVideo.play(results, button.getText(), false);
				}
			});
			button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			j += 40;
		}
	}
}