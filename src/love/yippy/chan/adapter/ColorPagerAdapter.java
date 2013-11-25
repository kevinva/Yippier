package love.yippy.chan.adapter;

import java.util.ArrayList;

import love.yippy.chan.R;
import love.yippy.chan.fragment.ColorFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ColorPagerAdapter extends FragmentPagerAdapter {
	
	private final int[] COLORS = new int[]{
			R.color.red,
			R.color.green,
	};
	
	private ArrayList<Fragment> mFragments;

	public ColorPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
		
		this.mFragments = new ArrayList<Fragment>();
		for(int color : COLORS){
			this.mFragments.add(new ColorFragment(color));
		}
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
