package love.yippy.chan.fragment;


import java.io.IOException;

import love.yippy.chan.AudiosActivity;
import love.yippy.chan.R;
import love.yippy.chan.utils.AudioFileHandler;
import love.yippy.chan.utils.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
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
		int ampliColorRes = 0;
		int itemsBtnDrawableRes = 0;
		int recBtnDrawableRes = 0;
		if(false){
			ampliColorRes = R.color.kevin_spring_green2;
			itemsBtnDrawableRes = R.drawable.button_list_spring_drawable;
			recBtnDrawableRes = R.drawable.button_record_drawable;
		}
		else if(false){
			ampliColorRes = R.color.kevin_summer_blue2;
			itemsBtnDrawableRes = R.drawable.button_list_summer_drawable;
			recBtnDrawableRes = R.drawable.button_record_drawable;
		}
		else if(false){
			ampliColorRes = R.color.kevin_autumu_yellow2;
			itemsBtnDrawableRes = R.drawable.button_list_autumu_drawable;
			recBtnDrawableRes = R.drawable.button_record_drawable;
		}
		else{
			ampliColorRes = R.color.alizarin_red;
			itemsBtnDrawableRes = R.drawable.button_list_winter_drawable;
			recBtnDrawableRes = R.drawable.button_record_drawable;
		}
		
		mAmplitudeColor = getResources().getColor(ampliColorRes);
		
		FrameLayout parentLayout = (FrameLayout)inflater.inflate(R.layout.recorder_main, null);
		
		mAmplitudeView = (SurfaceView) parentLayout.findViewById(R.id.amplitude_view);
		mAmplitudeView.setBackgroundColor(Color.TRANSPARENT); //注意：设SurfaceView的背景色为透明，否则看不到效果
		mAmplitudeViewHolder = mAmplitudeView.getHolder();
		
		RelativeLayout recordingLayout = (RelativeLayout) parentLayout.findViewById(R.id.recording_layout);
		recordingLayout.setBackgroundColor(Color.TRANSPARENT);
		
		ImageButton itemsBtn = (ImageButton) parentLayout.findViewById(R.id.audios_btn);
		Drawable listBtnDrawable = getResources().getDrawable(itemsBtnDrawableRes);
		itemsBtn.setBackgroundDrawable(listBtnDrawable);
		itemsBtn.setOnClickListener(this);
		
		ImageButton recBtn = (ImageButton) parentLayout.findViewById(R.id.record_btn);
		Drawable recDrawable = getResources().getDrawable(recBtnDrawableRes);
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
							Drawable stopDrawable = null;
							if(false){
								stopDrawable = getResources().getDrawable(R.drawable.button_stop_spring_drawable);
							}
							else if(false){
								stopDrawable = getResources().getDrawable(R.drawable.button_stop_summer_drawable);
							}
							else if(false){
								stopDrawable = getResources().getDrawable(R.drawable.button_stop_autumu_drawable);
							}
							else{
								stopDrawable = getResources().getDrawable(R.drawable.button_stop_winter_drawable);
							}
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
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String qualityStr = prefs.getString("recording_quality", "8000");
		
		
		if(Constants.DEBUG){
			Log.v(Constants.DEBUG_TAG, "recording_quality: " + qualityStr);
		}		
		
		MediaRecorder recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		recorder.setAudioChannels(2);
		recorder.setAudioSamplingRate(Integer.parseInt(qualityStr));
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
