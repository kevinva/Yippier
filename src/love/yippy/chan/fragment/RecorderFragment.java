package love.yippy.chan.fragment;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import love.yippy.chan.AudiosActivity;
import love.yippy.chan.R;
import love.yippy.chan.utils.Constants;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
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
		recBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					
				}
				else{
					Toast.makeText(getActivity(), "SDø®≤ª¥Ê‘⁄£¨«Î≤Â»ÎSDø®", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
		return parentLayout;
	}
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		this.initRecorder();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	private void initRecorder(){
		this.mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

	}
}
