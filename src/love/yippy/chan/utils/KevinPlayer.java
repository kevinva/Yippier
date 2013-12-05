package love.yippy.chan.utils;

import java.io.IOException;

import android.media.MediaPlayer;


public class KevinPlayer {
	
	public static interface onPlayingListener{
		
		public void onFinishPlaying(KevinPlayer player);
		public void onUpdateProgress(KevinPlayer player);
		
	}
	
	private Runnable mPlayingProgressTask = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub			
			while(isPlaying()){				
				if(mListener != null){
					mListener.onUpdateProgress(KevinPlayer.this);
				}			
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	};
	
	private MediaPlayer mPlayer;
	private onPlayingListener mListener;
	
	public void setOnPlayingListener(onPlayingListener listener){
		if(listener != mListener){
			mListener = null;
			mListener = listener;
		}		
	}
	

	public void play(String filePath){		
		if(filePath == null){
			return;
		}

		mPlayer = new MediaPlayer(); //���ز��Ų�����MediaPlayer.create(...)����
		try {
			mPlayer.setDataSource(filePath);
			mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					if(mListener != null){
						mListener.onFinishPlaying(KevinPlayer.this);
					}
				}
			});
			mPlayer.prepare();
			mPlayer.start();
			
			new Thread(mPlayingProgressTask).start();
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
		
		mPlayer.start();
		
		new Thread(mPlayingProgressTask).start();
	}
	
	public void seek(float percent){
		if(mPlayer == null){
			return;
		}
		
		int millSecs = (int) (mPlayer.getDuration() * percent);
		mPlayer.seekTo(millSecs);
	}
	
	public int getCurrentPosition(){
		if(mPlayer == null){
			return -1;
		}
		
		return mPlayer.getCurrentPosition();
	}
	
	public int getDuration(){
		if(mPlayer == null){
			return -1;
		}
		
		return mPlayer.getDuration();
	}
	
	public boolean isPlaying(){
		if(mPlayer == null){
			return false;
		}
		
		return mPlayer.isPlaying();
	}
}
