package love.yippy.chan.fragment;

import love.yippy.chan.AudiosActivity;
import love.yippy.chan.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;


@SuppressLint("ValidFragment")
public class RecorderFragment extends Fragment {
	
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
		
		return parentLayout;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}
