package love.yippy.chan;

import java.io.File;

import love.yippy.chan.preference.MyLabelPreference;
import love.yippy.chan.preference.MyListPreference;
import love.yippy.chan.utils.Constants;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
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
		
		MyLabelPreference audioFileLocationPref = (MyLabelPreference) this.findPreference("audio_file_location");
		audioFileLocationPref.title = "¼���ļ��洢λ��";
		audioFileLocationPref.summary = "SD��" + File.separator + Constants.ROOT_DIR + File.separator + Constants.AUDIO_DIR + File.separator;
		
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
