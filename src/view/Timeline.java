package view;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JProgressBar;

public class Timeline extends JProgressBar {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Timeline(Rectangle rect) {
		super();
		setValue(0);
		setStringPainted(true);
		setBounds(rect);
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int mouseX = e.getX();

				int progressBarVal = (int) Math
						.round(((double) mouseX / (double) getWidth())
								* getMaximum());

				setValue(progressBarVal);
				onSeek(progressBarVal);
			}
		});
		setString("");
		setOpaque(false);
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.7f));
		super.paint(g2);
		g2.dispose();
	}

	public void onSeek(int frame) {

	}
}