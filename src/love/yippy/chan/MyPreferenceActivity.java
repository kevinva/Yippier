package love.yippy.chan;

import java.io.File;

import love.yippy.chan.preference.MyLabelPreference;
import love.yippy.chan.preference.MyListPreference;
import love.yippy.chan.utils.Constants;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class MyPreferenceActivity extends PreferenceActivity {
	
	private int mClickCount = 0;

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
		this.getActionBar().setTitle("����");
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
		audioFileLocationPref.title = "¼���ļ��洢λ��";
		audioFileLocationPref.summary = "SD��" + File.separator + Constants.ROOT_DIR + File.separator + Constants.AUDIO_DIR + File.separator;
		audioFileLocationPref.setOnPreferenceClickListener(new OnPreferenceClickListener(){

			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub

				mClickCount++;
				if(mClickCount >=7 && mClickCount <= 9){
					Toast.makeText(MyPreferenceActivity.this, "" + (10 - mClickCount), Toast.LENGTH_SHORT).show();
					if(mClickCount == 9){
						mClickCount = 0;
						
						Intent i = new Intent(MyPreferenceActivity.this, SpecialActivity.class);
						startActivity(i);
					}
				}
				
				return false;
			}
			
		});
		
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
						recordingQualityPref.setSummary("��ǰ���ʣ�" + currentTitle);
					}
				}
			}
			
		});
		
		String currentQuality = sharedPrefs.getString("recording_quality", "8000");
		MyListPreference recordingQualityPref = (MyListPreference) this.findPreference("recording_quality");
		recordingQualityPref.mTitle = "¼������";
		String currentTitle = this.fetchCurrentQualityTitle(currentQuality);
		if(currentTitle != null){
			recordingQualityPref.mSummary = "��ǰ���ʣ�" + currentTitle;
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
