package love.yippy.chan;

import java.io.File;

import love.yippy.chan.preference.MyLabelPreference;
import love.yippy.chan.preference.MyListPreference;
import love.yippy.chan.utils.Constants;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

public class MyPreferenceActivity extends PreferenceActivity {

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.my_preferences);
		
		this.initLayout();
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == android.R.id.home){
			finish();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void initLayout(){
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		this.getActionBar().setTitle("设置");
		Drawable actionBarDrawable = null;
		if(Constants.isSrping){
			actionBarDrawable = this.getResources().getDrawable(R.drawable.action_bar_spring_drawable);
		}
		else if(Constants.isSummer){
			actionBarDrawable = this.getResources().getDrawable(R.drawable.action_bar_summer_drawable);	
		}
		else if(Constants.isAutumu){
			actionBarDrawable = this.getResources().getDrawable(R.drawable.action_bar_autumu_drawable);
		}
		else{
			actionBarDrawable = this.getResources().getDrawable(R.drawable.action_bar_winter_drawable);
		}
		this.getActionBar().setBackgroundDrawable(actionBarDrawable);
		
		MyLabelPreference audioFileLocationPref = (MyLabelPreference) this.findPreference("audio_file_location");
		audioFileLocationPref.title = "录音文件存储位置";
		audioFileLocationPref.summary = "SD卡" + File.separator + Constants.ROOT_DIR + File.separator + Constants.AUDIO_DIR + File.separator;
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPrefs.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener(){

			@Override
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				// TODO Auto-generated method stub
				
				if(key.equals("recording_quality")){
					MyListPreference recordingQualityPref = (MyListPreference) findPreference("recording_quality");
					String currentQuality = recordingQualityPref.getValue();
					String currentTitle = fetchCurrentQualityTitle(currentQuality);
					if(currentTitle != null){
						recordingQualityPref.setSummary("当前音质：" + currentTitle);
					}
				}
			}
			
		});
		
		String currentQuality = sharedPrefs.getString("recording_quality", "8000");
		MyListPreference recordingQualityPref = (MyListPreference) this.findPreference("recording_quality");
		recordingQualityPref.mTitle = "录音音质";
		String currentTitle = this.fetchCurrentQualityTitle(currentQuality);
		if(currentTitle != null){
			recordingQualityPref.mSummary = "当前音质：" + currentTitle;
		}
	}
	
	private String fetchCurrentQualityTitle(String quality){
		if(quality == null){
			return null;
		}
		String[] quality_values = this.getResources().getStringArray(R.array.quality_values);
		String[] quality_titles = this.getResources().getStringArray(R.array.quality_titles);
		String currentTitle = null;
		for(int i = 0; i < quality_values.length; i++){
			if(quality.equals(quality_values[i])){
				currentTitle = quality_titles[i];
				break;
			}
		}
		
		if(Constants.DEBUG){
			Log.v(Constants.DEBUG_TAG, "fecthCurrentQualityTitle: " + currentTitle);
		}
		
		return currentTitle;
	}
}
