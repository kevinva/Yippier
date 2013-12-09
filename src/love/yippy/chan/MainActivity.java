package love.yippy.chan;

import java.util.Timer;
import java.util.TimerTask;

import love.yippy.chan.adapter.FunctionPagerAdapter;
import love.yippy.chan.fragment.UtilsFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener{

	private ViewPager vp;
	private static boolean isExit = false;
	private Timer mTimerExit = new Timer();
	private TimerTask mExitTask = new TimerTask(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			isExit = false;
		}		
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //保持不锁屏
		
		setContentView(R.layout.activity_main);

		this.initLayout();
	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(isExit == false){
				isExit = true;
				Toast.makeText(this, "再按一次退出Yippier", Toast.LENGTH_LONG).show();
				
				mExitTask = null;
				mExitTask = new TimerTask(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						isExit = false;
					}
					
				};
				mTimerExit.schedule(mExitTask, 2000);
			}
			else{
				this.exit();
			}
		}
		
		return true;
	}
	
	private void exit(){
		finish();
		System.exit(0);
	}
	
	private void  initLayout(){
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
		UtilsFragment listFragment = new UtilsFragment();
		ft.replace(R.id.menu_frame, listFragment);
		ft.commit();
		
		this.getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for(int i = 1; i <= 2; i++){
			ActionBar.Tab tab = this.getSupportActionBar().newTab();
			if(i == 1){
				tab.setText("录音 ");
			}
			else{
				tab.setText("(待开发) ");
			}			
			tab.setTabListener(this);
			this.getSupportActionBar().addTab(tab);
		}
		this.getSupportActionBar().setDisplayShowHomeEnabled(false);
		this.getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		this.vp = new ViewPager(this);
		vp.setId("VP".hashCode());
		vp.setAdapter(new FunctionPagerAdapter(this.getSupportFragmentManager()));
		RelativeLayout mainLayout = (RelativeLayout)this.findViewById(R.id.content_frame);
		mainLayout.addView(vp);
		vp.setCurrentItem(0);
		vp.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		switch(arg0){
		case 0:
			this.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			break;
		default:
			this.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			break;
		}		
		this.getSupportActionBar().getTabAt(arg0).select();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		if(this.vp != null){
			this.vp.setCurrentItem(tab.getPosition());
		}
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
}
