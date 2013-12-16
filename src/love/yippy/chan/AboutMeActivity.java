package love.yippy.chan;

import love.yippy.chan.utils.Constants;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class AboutMeActivity extends Activity {
	
	private int clickCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.about_me);
		
		initLayout();
	}

	private void initLayout(){
		Drawable actionBarDrawable = null;
		int labelColorRes = 0;
		if(Constants.isSrping){
			actionBarDrawable = this.getResources().getDrawable(R.drawable.action_bar_spring_drawable);
			labelColorRes = R.color.kevin_spring_green2;
		}
		else if(Constants.isSummer){
			actionBarDrawable = this.getResources().getDrawable(R.drawable.action_bar_summer_drawable);
			labelColorRes = R.color.kevin_summer_blue2;
		}
		else if(Constants.isAutumu){
			actionBarDrawable = this.getResources().getDrawable(R.drawable.action_bar_autumu_drawable);
			labelColorRes = R.color.kevin_autumu_yellow2;
		}
		else{
			actionBarDrawable = this.getResources().getDrawable(R.drawable.action_bar_winter_drawable);
			labelColorRes = R.color.kevin_blue1;
		}
		
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		this.getActionBar().setDisplayShowHomeEnabled(true);
		this.getActionBar().setDisplayShowTitleEnabled(true);
		this.getActionBar().setTitle("¹ØÓÚ");
		this.getActionBar().setBackgroundDrawable(actionBarDrawable);
		
		ImageButton logoBtn = (ImageButton) this.findViewById(R.id.logo_btn);
		logoBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clickCount++;
				
				if(clickCount == 3){
					clickCount = 0;
					
					startQRView();
				}
				
			}
		});
		
		
		TextView versionLabel = (TextView) findViewById(R.id.version_label);
		versionLabel.setText("V" + this.getVersionName());
		
		TextView copyRightLabel1 = (TextView) findViewById(R.id.copyright_label1);
		copyRightLabel1.setTextColor(this.getResources().getColor(labelColorRes));
		
		TextView copyRightLabel2 = (TextView) findViewById(R.id.copyright_label2);		
		copyRightLabel2.setTextColor(this.getResources().getColor(labelColorRes));
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}		
	}
	
	private String getVersionName(){
		PackageManager pm = this.getPackageManager();
		PackageInfo pInfo = null;
		try {
			pInfo = pm.getPackageInfo(this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(pInfo == null){
			return "0";
		}
		
		return pInfo.versionName;
	}
	
	private void startQRView(){
//		Intent openCameraScanningIntent = new Intent(this, CaptureActivity.class);
	}
}
