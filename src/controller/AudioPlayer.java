package controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer
{
	Clip clip;
	InputStream is;
	BufferedInputStream bin;
	AudioInputStream audio;
    public AudioPlayer(String filename )
    {
        try
        {
        	is=new FileInputStream(new File(filename));
			bin = new BufferedInputStream(is);
			audio = AudioSystem.getAudioInputStream( bin );
        	clip = AudioSystem.getClip();
			clip.open( audio );
        }
        catch ( LineUnavailableException e )
        {
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        catch ( UnsupportedAudioFileException e )
        {
            e.printStackTrace();
        }
    }

    void play(int frame)
    {
    	clip.setFramePosition(frame);
    	clip.start();
    }

    public int audioPosition()
    {
    	return clip.getFramePosition();
    }

	public void stop()
	{
		clip.stop();
//		clip.close();
//		try {
//			audio.close();
//			bin.close();
//			is.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}