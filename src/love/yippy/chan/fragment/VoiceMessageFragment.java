package love.yippy.chan.fragment;

import love.yippy.chan.R;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class VoiceMessageFragment extends Fragment {
	public VoiceMessageFragment() { 
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		int color = this.getResources().getColor(R.color.voice_fragment_bg);
		RelativeLayout parentLayout = (RelativeLayout)inflater.inflate(R.layout.function, null);
		parentLayout.setBackgroundColor(color);
		return parentLayout;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}
