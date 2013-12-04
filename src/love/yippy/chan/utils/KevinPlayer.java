package love.yippy.chan.utils;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

public class KevinPlayer {
	
	public static interface onPlayingListener{
		
		public void onFinishPlaying(KevinPlayer player);
		public void onUpdateProgress(KevinPlayer player);
		
	}
	
	private Runnable mPlayingProgressTask = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			Log.v(Constants.DEBUG_TAG, "listener:" + mListener);
			
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
	
	private Context mContext;
	private MediaPlayer mPlayer;
	private onPlayingListener mListener;
	
	public void setOnPlayingListener(onPlayingListener listener){
		if(listener != mListener){
			mListener = null;
			mListener = listener;
		}		
	}
	
	public KevinPlayer(Context ctx){
		this.mContext = ctx;
	}
	
	/**
	 * ���Ż����������Ƶ
	 * @param filePath �����ļ��ı���·������������ͣ����ô˷������ɴ���null
	 */
	public void play(String filePath){		
		if(mPlayer != null){
			mPlayer.start();
			
			new Thread(mPlayingProgressTask).start();
		}
		else{
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
							mPlayer.release();
							mPlayer = null;
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
