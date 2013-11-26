package love.yippy.chan.adapter;

import java.util.ArrayList;

import love.yippy.chan.fragment.RecorderFragment;
import love.yippy.chan.fragment.VoiceMessageFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FunctionPagerAdapter extends FragmentPagerAdapter {
	
	private ArrayList<Fragment> mFragments;

	public FunctionPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
		
		this.mFragments = new ArrayList<Fragment>();
		this.mFragments.add(new RecorderFragment());
		this.mFragments.add(new VoiceMessageFragment());
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mFragments.size();
	}

}
