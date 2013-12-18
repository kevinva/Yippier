package love.yippy.chan.fragment;

import love.yippy.chan.R;
import love.yippy.chan.utils.Constants;
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
		int bgColorRes = 0;
		if(Constants.isSrping){
			bgColorRes = R.color.kevin_spring_green2;
		}
		else if(Constants.isSummer){
			bgColorRes = R.color.kevin_summer_blue2;
		}
		else if(Constants.isAutumu){
			bgColorRes = R.color.kevin_autumu_yellow2;
		}
		else{
			bgColorRes = R.color.kevin_blue1;
		}
		
		RelativeLayout parentLayout = (RelativeLayout)inflater.inflate(R.layout.function, null);
		parentLayout.setBackgroundColor(this.getActivity().getResources().getColor(bgColorRes));
		
		return parentLayout;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}
