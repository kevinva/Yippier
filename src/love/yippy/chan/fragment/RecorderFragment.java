package love.yippy.chan.fragment;

import java.io.File;
import java.io.IOException;

import love.yippy.chan.AudiosActivity;
import love.yippy.chan.R;
import love.yippy.chan.utils.AudioFileHandler;
import love.yippy.chan.utils.Constants;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


@SuppressLint("ValidFragment")
public class RecorderFragment extends Fragment {
	
	private MediaRecorder mRecorder;
	private MediaPlayer mPlayer;
	private boolean mRecording;
	private String mCurrentFilename;
	
	private Handler mRecordingHandler = new Handler(){
		
		public void handleMessage(Message msg){
			switch(msg.what){
			case Constants.MESSAGE_RECORDING_AMPLITUDE:
				Log.v(Constants.DEBUG_TAG, "amplitude: " + msg.getData().getDouble(Constants.AMPLITUDE_KEY));
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
					Thread.sleep(250);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		int color = getResources().getColor(R.color.dark_blue);
		// construct the RelativeLayou
		
		RelativeLayout parentLayout = (RelativeLayout)inflater.inflate(R.layout.recorder_main, null);
		parentLayout.setBackgroundColor(color);
		
		Button itemsBtn = (Button) parentLayout.findViewById(R.id.audios_btn);
		itemsBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), AudiosActivity.class);
				getActivity().startActivity(intent);				
			}
		});
		
		Button recBtn = (Button) parentLayout.findViewById(R.id.record_btn);
		recBtn.setText("Rec");
		recBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
							}
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else{
						Toast.makeText(getActivity(), "SDø®≤ª¥Ê‘⁄£¨«Î≤Â»ÎSDø®", Toast.LENGTH_LONG).show();
					}
				}
				else{
					mRecorder.stop();
					mRecorder.release();
					mRecorder = null;
					
					mRecording = false;
					
					if(mPlayer == null){
						mPlayer = new MediaPlayer();
					}
					
					String dir;
					try {
						dir = Environment.getExternalStorageDirectory().getCanonicalPath() + File.separator + 
								Constants.ROOT_DIR + File.separator + Constants.AUDIO_DIR;
						String filePath = dir + File.separator + mCurrentFilename;
						String fileSize = AudioFileHandler.getFileSize(filePath);
						String duration = AudioFileHandler.formatAudioDuration(mPlayer.getDuration());
						
						AudioFileHandler.saveAudioConfiguration(getActivity(), mCurrentFilename, "Œ¥√¸√˚", fileSize, duration);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Button click = (Button) v;
					click.setText("Rec");
				}				
			}			
		});
		
		return parentLayout;
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
