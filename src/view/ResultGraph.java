package view;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import model.MetadataResults;

public class ResultGraph extends JLabel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<MetadataResults> frames;
	ArrayList<MetadataResults> results;;
	double max=0;
	double min=999999999;

	public ResultGraph(ArrayList<MetadataResults> results) {
		this.results=results;
	}

	void drawGraph(String filename, int width, int height)
	{
		frames=new ArrayList<>();
		for(MetadataResults result : results)
		{
			if(result.getVideoname() .equals(filename))
				frames.add(result);
			if(max < result.getSimilarity())
				max=result.getSimilarity();
			if(min > result.getSimilarity())
				min=result.getSimilarity();
		}
		BufferedImage points = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for(int i=0; i < frames.size(); i++)
		{
			int pix = 0xff000000 | (((byte)255 & 0xff) << 16) | (((byte)255 & 0xff) << 8) | ((byte)255 & 0xff);
			int x = (i * width) / frames.size();
			int y = (int)( ((frames.get(i).getSimilarity() - min) * 200) / (max-min) ) ;
			System.out.println(x+","+y+","+frames.get(i).getSimilarity());
			points.setRGB(x, y, pix);
//			x = x - 1 < 0 ? x+1 : x;
//			y = y - 1 < 0 ? y+1 : y;
//			points.setRGB(x+1, y+1, pix);
//			points.setRGB(x+1, y-1, pix);
//			points.setRGB(x-1, y+1, pix);
//			points.setRGB(x-1, y-1, pix);
		}
		setIcon(new ImageIcon(points));
	}
}