package mx.nanocode.util;

import android.content.Context;
import android.media.MediaPlayer;

public class PlaySound {
	
	public void Play(Context context,int ResID)
	{
		 MediaPlayer mediaPlayer=MediaPlayer.create(context,ResID);
         mediaPlayer.start();
	}

}
