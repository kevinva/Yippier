package love.yippy.chan;

import love.yippy.chan.fragment.SampleListFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity implements ActionBar.TabListener{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

		this.initLayout();

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		TextView selected = (TextView)this.findViewById(R.id.tmp_selected);
		selected.setText("Selected: " + tab.getText());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

//	public boolean onCreateOptionMenu(Menu menu){
//		//this.getSupportMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//	
//	public boolean onOptionItemSelected(MenuItem item){
//		switch(item.getItemId()){
//		case android.R.id.home:
//			this.toggle();
//			return true;
//		}
//		
//		return super.onOptionsItemSelected(item);
//	}
	
	private void initLayout(){
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
		
		this.getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for(int i = 1; i <= 3; i++){
			ActionBar.Tab tab = this.getSupportActionBar().newTab();
			tab.setText("Tab " + i);
			tab.setTabListener(this);
			this.getSupportActionBar().addTab(tab);
		}
		this.getSupportActionBar().setDisplayShowHomeEnabled(false);
		this.getSupportActionBar().setDisplayShowTitleEnabled(false);
	}
}
