package love.yippy.chan.fragment;


import java.io.IOException;

import love.yippy.chan.AudiosActivity;
import love.yippy.chan.R;
import love.yippy.chan.utils.AudioFileHandler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;


@SuppressLint("ValidFragment")
public class RecorderFragment extends Fragment implements View.OnClickListener{
	
	private MediaRecorder mRecorder;	
	private String mCurrentFilename;
	private SurfaceView mAmplitudeView;
	private SurfaceHolder mAmplitudeViewHolder;
	private boolean mRecording;
	private int mAmplitudeColor;
	
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mAmplitudeColor = getResources().getColor(R.color.alizarin_red);
		
		FrameLayout parentLayout = (FrameLayout)inflater.inflate(R.layout.recorder_main, null);
		
		mAmplitudeView = (SurfaceView) parentLayout.findViewById(R.id.amplitude_view);
		mAmplitudeView.setBackgroundColor(Color.TRANSPARENT); //注意：设SurfaceView的背景色为透明，否则看不到效果
		mAmplitudeViewHolder = mAmplitudeView.getHolder();
		
		RelativeLayout recordingLayout = (RelativeLayout) parentLayout.findViewById(R.id.recording_layout);
		recordingLayout.setBackgroundColor(Color.TRANSPARENT);
		
		ImageButton itemsBtn = (ImageButton) parentLayout.findViewById(R.id.audios_btn);
		Drawable listBtnDrawable = getResources().getDrawable(R.drawable.button_list_drawable);
		itemsBtn.setBackgroundDrawable(listBtnDrawable);
		itemsBtn.setOnClickListener(this);
		
		ImageButton recBtn = (ImageButton) parentLayout.findViewById(R.id.record_btn);
		Drawable recDrawable = getResources().getDrawable(R.drawable.button_record_drawable);
		recBtn.setBackgroundDrawable(recDrawable);
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
							
							ImageButton click = (ImageButton) v;
							Drawable stopDrawable = getResources().getDrawable(R.drawable.button_stop_drawable);
							click.setBackgroundDrawable(stopDrawable);
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
				
				String filePath = AudioFileHandler.generateAudioPath(mCurrentFilename);
				String fileSize = AudioFileHandler.getFileSize(filePath);				
				MediaPlayer player = MediaPlayer.create(getActivity(), Uri.parse(filePath));
				String duration = AudioFileHandler.formatAudioDuration(player.getDuration());				
				AudioFileHandler.saveAudioConfiguration(getActivity(), mCurrentFilename, "未命名", fileSize, duration);				
				
				ImageButton click = (ImageButton) v;
				Drawable recDrawable = getResources().getDrawable(R.drawable.button_record_drawable);
				click.setBackgroundDrawable(recDrawable);
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
		paint.setColor(mAmplitudeColor);
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
