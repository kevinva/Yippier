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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;


@SuppressLint("ValidFragment")
public class RecorderFragment extends Fragment implements View.OnClickListener{
	
	private MediaRecorder mRecorder;	
	private String mCurrentFilename;
	private WakeLock mWakeLock;
	private SurfaceView mAmplitudeView;
	private SurfaceHolder mAmplitudeViewHolder;
	private boolean mRecording;
	
//	private Handler mRecordingHandler = new Handler(){
//		
//		public void handleMessage(Message msg){
//			switch(msg.what){
//			case Constants.MESSAGE_RECORDING_AMPLITUDE:				
//				float amplitude = msg.getData().getFloat(Constants.AMPLITUDE_KEY);								
//
//				if(Constants.DEBUG){
//					Log.v(Constants.DEBUG_TAG, "amplitude: " + amplitude);					
//				}
//				
//				break;
//			}
//		}
//		
//	};
	
	private Runnable mAmplitudeTask = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Canvas canvas = null;
			while(mRecording){
				try {
					Thread.sleep(100);
				}  catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(mRecorder != null){				
					float amplitude = mRecorder.getMaxAmplitude() / 32768.0f;
					
					canvas = mAmplitudeViewHolder.lockCanvas();
					drawAmplitudeView(canvas, amplitude);
					mAmplitudeViewHolder.unlockCanvasAndPost(canvas);
					
//					Bundle b = new Bundle();
//					b.putFloat(Constants.AMPLITUDE_KEY, amplitude);
//					Message msg = mRecordingHandler.obtainMessage();
//					msg.what = Constants.MESSAGE_RECORDING_AMPLITUDE;
//					msg.setData(b);
//					mRecordingHandler.sendMessage(msg);	
					
				}
			}
			
			canvas = mAmplitudeViewHolder.lockCanvas();
			clearAmplitudeDrawing(canvas);
			mAmplitudeViewHolder.unlockCanvasAndPost(canvas);
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
		int color = getResources().getColor(R.color.black);
		
		FrameLayout parentLayout = (FrameLayout)inflater.inflate(R.layout.recorder_main, null);
		parentLayout.setBackgroundColor(color);
		
		mAmplitudeView = (SurfaceView) parentLayout.findViewById(R.id.amplitude_view);
		//mSfv.setBackgroundColor(Color.BLACK); //注意：设了SurfaceView的背景色则看不到效果
		mAmplitudeViewHolder = mAmplitudeView.getHolder();
		
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
			if(mRecorder == null){						
				boolean isSDCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
				if(isSDCardExist){
					mCurrentFilename = System.currentTimeMillis() + ".amr";
					String filePath = AudioFileHandler.generateAudioPath(mCurrentFilename);
					mRecorder = initRecorder(filePath);
					if(mRecorder != null){
						try {
							mRecorder.prepare();
							mRecorder.start();
							
							mRecording = true;
							Thread t = new Thread(mAmplitudeTask);
							t.start();
							
							Button click = (Button) v;
							click.setText("Stop");
							
							mWakeLock.acquire(); //录音时保持屏幕唤醒
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				else{
					Toast.makeText(getActivity(), "SD卡不存在，请插入SD卡", Toast.LENGTH_LONG).show();
				}
			}
			else{
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;				
				mRecording = false;
				
				mWakeLock.release();  //录音结束后解除保持屏幕唤醒
				
				String filePath = AudioFileHandler.generateAudioPath(mCurrentFilename);
				String fileSize = AudioFileHandler.getFileSize(filePath);				
				MediaPlayer player = MediaPlayer.create(getActivity(), Uri.parse(filePath));
				String duration = AudioFileHandler.formatAudioDuration(player.getDuration());				
				AudioFileHandler.saveAudioConfiguration(getActivity(), mCurrentFilename, "未命名", fileSize, duration);				
				
				Button click = (Button) v;
				click.setText("Rec");
			}
		}
		else if(viewId == R.id.audios_btn){
			if(mRecording){
				Toast.makeText(getActivity(), "请先停止录音", Toast.LENGTH_LONG).show();
			}
			else{
				Intent intent = new Intent();
				intent.setClass(getActivity(), AudiosActivity.class);
				getActivity().startActivity(intent);
			}
		}			
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
	
	
	private void drawAmplitudeView(Canvas canvas, float ampl){
		if(canvas == null){
			return;
		}
		Paint paint = new Paint();
		paint.setColor(Color.YELLOW);
		canvas.drawColor(Color.BLACK);
		canvas.drawRect(0.0f, canvas.getHeight() * (1 - ampl), canvas.getWidth(), canvas.getHeight(), paint);
	}
	
	private void clearAmplitudeDrawing(Canvas canvas){
		if(canvas == null){
			return;
		}
		canvas.drawColor(Color.BLACK);
	}
}
