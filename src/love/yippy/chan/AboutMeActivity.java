package love.yippy.chan;

import love.yippy.chan.utils.Constants;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zxing.activity.CaptureActivity;

public class AboutMeActivity extends Activity {
	
	private int mClickCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.about_me);
		
		initLayout();
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == Constants.CAPTURE_INTENT_REQUEST){
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result");
				
				if(Constants.DEBUG){
					Log.v(Constants.DEBUG_TAG, "scan result: " + scanResult);
				}				
				
				if(scanResult.equals(Constants.SPECIAL_CODE)){
					Intent i = new Intent(this, SpecialActivity.class);
					this.startActivity(i);
				}
				else{
					Toast.makeText(this, "不是什么都能扫的哦！", Toast.LENGTH_LONG).show();
				}
			}
		}
	}


	private void initLayout(){
		Drawable actionBarDrawable = null;
		int labelColorRes = 0;
		int mainBgColorRes = 0;
		if(Constants.isSrping){
			actionBarDrawable = this.getResources().getDrawable(R.drawable.action_bar_spring_drawable);
			labelColorRes = R.color.kevin_spring_green2;
			mainBgColorRes = R.color.kevin_spring_green1;
		}
		else if(Constants.isSummer){
			actionBarDrawable = this.getResources().getDrawable(R.drawable.action_bar_summer_drawable);
			labelColorRes = R.color.kevin_summer_blue2;
			mainBgColorRes = R.color.kevin_summer_blue1;
		}
		else if(Constants.isAutumu){
			actionBarDrawable = this.getResources().getDrawable(R.drawable.action_bar_autumu_drawable);
			labelColorRes = R.color.kevin_autumu_yellow2;
			mainBgColorRes = R.color.kevin_autumu_yellow1;
		}
		else{
			actionBarDrawable = this.getResources().getDrawable(R.drawable.action_bar_winter_drawable);
			labelColorRes = R.color.kevin_blue1;
			mainBgColorRes = R.color.clouds;
		}
		
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		this.getActionBar().setDisplayShowHomeEnabled(true);
		this.getActionBar().setDisplayShowTitleEnabled(true);
		this.getActionBar().setTitle("关于");
		this.getActionBar().setBackgroundDrawable(actionBarDrawable);
		
		RelativeLayout mainLayout = (RelativeLayout) this.findViewById(R.id.about_me_layout);
		mainLayout.setBackgroundColor(this.getResources().getColor(mainBgColorRes));
		
		ImageButton logoBtn = (ImageButton) this.findViewById(R.id.logo_btn);
		logoBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mClickCount++;
				
				if(mClickCount == 2){
					Toast.makeText(AboutMeActivity.this, "再按一次试试", Toast.LENGTH_LONG).show();
				}
				else if(mClickCount == 3){
					mClickCount = 0;
					
					startQRView();
				}				
			}
		});
		
		
		TextView versionLabel = (TextView) findViewById(R.id.version_label);
		versionLabel.setText("V" + this.getVersionName());
		versionLabel.setTextColor(this.getResources().getColor(labelColorRes));
		
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
		Intent openCameraScanningIntent = new Intent(this, CaptureActivity.class);
		this.startActivityForResult(openCameraScanningIntent, Constants.CAPTURE_INTENT_REQUEST);
	}
}
