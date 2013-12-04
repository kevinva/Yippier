package love.yippy.chan.utils;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class KevinPlayer {
	
	private static KevinPlayer instance;
	
	private Context mContext;
	private MediaPlayer mPlayer;
	
	public static KevinPlayer sharedInstance(Context ctx){
		synchronized(instance){
			if(instance == null || ctx != instance.mContext){
				instance = new KevinPlayer(ctx);
			}
			return instance;
		}
	}
	
	private KevinPlayer(Context ctx){
		this.mContext = ctx;
	}
	
	public void play(String filePath){
		if(filePath == null){
			return;
		}
		
		mPlayer = MediaPlayer.create(mContext, Uri.parse(filePath));
		try {
			mPlayer.prepare();
			mPlayer.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stop(){
		if(mPlayer == null){
			return;
		}
		
		mPlayer.stop();
		mPlayer.release();
		mPlayer = null;
	}

	public void pause(){
		if(mPlayer == null){
			return;
		}
		
		mPlayer.pause();
	}
	
	public void resume(){
		if(mPlayer == null){
			return;
		}
		
		if(this.isPlaying() == false){
			mPlayer.start();
		}		
	}
	
	public void seek(float percent){
		if(mPlayer == null){
			return;
		}
		
		int millSecs = (int) (mPlayer.getDuration() * percent);
		mPlayer.seekTo(millSecs);
	}
	
	public boolean isPlaying(){
		if(mPlayer == null){
			return false;
		}
		
		return mPlayer.isPlaying();
	}
}
