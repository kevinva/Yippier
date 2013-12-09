package love.yippy.chan;

import java.io.File;
import java.io.IOException;

import love.yippy.chan.utils.Constants;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class MyPreferenceActivity extends PreferenceActivity {

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.my_preferences);
		
		Preference audioFileLocationPref = this.findPreference("audio_file_location");
		String dir;
		try {
			dir = Environment.getExternalStorageDirectory().getCanonicalPath() + File.separator + 
					Constants.ROOT_DIR + File.separator + Constants.AUDIO_DIR;
			audioFileLocationPref.setSummary("Â¼ÒôÎÄ¼þÎ»ÖÃ£ºSD¿¨" + File.separator + dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPref.getInt("recording_quality", 8000);
		
	}
	
}
