package love.yippy.chan.fragment;

import java.io.File;
import java.io.IOException;

import love.yippy.chan.AudiosActivity;
import love.yippy.chan.R;
import love.yippy.chan.utils.AudioFileHandler;
import love.yippy.chan.utils.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;


@SuppressLint("ValidFragment")
public class RecorderFragment extends Fragment implements View.OnClickListener{
	
	private MediaRecorder mRecorder;	
	private String mCurrentFilename;
	private WakeLock mWakeLock;
	private View mAmplView;
	private boolean mRecording;
	private int mAmplViewOriginalHeight;
	
	private Handler mRecordingHandler = new Handler(){
		
		public void handleMessage(Message msg){
			switch(msg.what){
			case Constants.MESSAGE_RECORDING_AMPLITUDE:				
				double amplitude = msg.getData().getDouble(Constants.AMPLITUDE_KEY);								
				ViewGroup.LayoutParams params = (LayoutParams) mAmplView.getLayoutParams();
				params.height = (int) (mAmplViewOriginalHeight * (1 - amplitude));
				mAmplView.setLayoutParams(params);
				
				if(Constants.DEBUG){
					Log.v(Constants.DEBUG_TAG, "amplitude: " + amplitude);					
					Log.v(Constants.DEBUG_TAG, "height: " + mAmplView.getHeight());
				}
				
				break;
			}
		}
		
	};
	
	private Runnable mAmplitudeTask = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(mRecording){
				Message msg = mRecordingHandler.obtainMessage();
				try {
					Thread.sleep(200);
				}  catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(mRecorder != null){
					Bundle b = new Bundle();
					double amplitude = mRecorder.getMaxAmplitude() / 32768.0;
					b.putDouble(Constants.AMPLITUDE_KEY, amplitude);
					msg.what = Constants.MESSAGE_RECORDING_AMPLITUDE;
					msg.setData(b);
					mRecordingHandler.sendMessage(msg);					
				}
			}
		}
		
	};
	
	public RecorderFragment() { 
		setRetainInstance(true);
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "cn");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		int color = getResources().getColor(R.color.dark_blue);
		// construct the RelativeLayou
		
		FrameLayout parentLayout = (FrameLayout)inflater.inflate(R.layout.recorder_main, null);
		parentLayout.setBackgroundColor(color);
		
		mAmplView = parentLayout.findViewById(R.id.amplitude_bg);		
		
		Button itemsBtn = (Button) parentLayout.findViewById(R.id.audios_btn);
		itemsBtn.setOnClickListener(this);
		
		Button recBtn = (Button) parentLayout.findViewById(R.id.record_btn);
		recBtn.setText("Rec");
		recBtn.setOnClickListener(this);
		
		return parentLayout;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub		
		int viewId = v.getId();
		if(viewId == R.id.record_btn){
			if(mAmplViewOriginalHeight <= 0 && mAmplView != null){
				mAmplViewOriginalHeight = mAmplView.getHeight();
			}
			if(mRecorder == null){						
				boolean isSDCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
				if(isSDCardExist){
					String dir;
					try {
						dir = Environment.getExternalStorageDirectory().getCanonicalPath() + File.separator + 
								Constants.ROOT_DIR + File.separator + Constants.AUDIO_DIR;
						
						if(Constants.DEBUG){
							Log.v(Constants.DEBUG_TAG, "RecorderFragment: dir = " + dir);
						}						
						
						File dirFile = new File(dir);
						if(!dirFile.exists()){
							dirFile.mkdirs();
						}					
						mCurrentFilename = System.currentTimeMillis() + ".amr";
						String filePath = dir + File.separator + mCurrentFilename;
						mRecorder = initRecorder(filePath);
						if(mRecorder != null){
							mRecorder.prepare();
							mRecorder.start();
							
							mRecording = true;
							Thread t = new Thread(mAmplitudeTask);
							t.start();
							
							Button click = (Button) v;
							click.setText("Stop");
							
							mWakeLock.acquire(); //Â¼ÒôÊ±±£³ÖÆÁÄ»»½ÐÑ
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					Toast.makeText(getActivity(), "SD¿¨²»´æÔÚ£¬Çë²åÈëSD¿¨", Toast.LENGTH_LONG).show();
				}
			}
			else{
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;
				
				mRecording = false;
				
				mWakeLock.release();  //Â¼Òô½áÊøºó½â³ý±£³ÖÆÁÄ»»½ÐÑ
				
				String dir;
				try {
					dir = Environment.getExternalStorageDirectory().getCanonicalPath() + File.separator + 
							Constants.ROOT_DIR + File.separator + Constants.AUDIO_DIR;
					String filePath = dir + File.separator + mCurrentFilename;
					String fileSize = AudioFileHandler.getFileSize(filePath);
					
					MediaPlayer player = MediaPlayer.create(getActivity(), Uri.parse(filePath));
					String duration = AudioFileHandler.formatAudioDuration(player.getDuration());
					
					AudioFileHandler.saveAudioConfiguration(getActivity(), mCurrentFilename, "Î´ÃüÃû", fileSize, duration);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
				
				Button click = (Button) v;
				click.setText("Rec");
			}
		}
		else if(viewId == R.id.audios_btn){
			Intent intent = new Intent();
			intent.setClass(getActivity(), AudiosActivity.class);
			getActivity().startActivity(intent);
		}			
	}			


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	private MediaRecorder initRecorder(String filePath){
		if(filePath == null){
			return null;
		}
		
		MediaRecorder recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		recorder.setOutputFile(filePath);
		return recorder;
	}
}
