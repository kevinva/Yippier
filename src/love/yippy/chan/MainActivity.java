package love.yippy.chan;

import love.yippy.chan.fragment.SampleListFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


public class MainActivity extends SlidingFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

		SlidingMenu menu = this.getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.sliding_menu_shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
		menu.setFadeDegree(0.35f);
		
		this.setBehindContentView(R.layout.menu_frame);
		FragmentManager fm = this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		SampleListFragment listFragment = new SampleListFragment();
		ft.replace(R.id.menu_frame, listFragment);
		ft.commit();
		
		this.getSupportActionBar().setDisplayShowHomeEnabled(true);
		this.getSupportActionBar().setDisplayShowTitleEnabled(true);
	}

	public boolean onCreateOptionMenu(Menu menu){
		this.getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			this.toggle();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
