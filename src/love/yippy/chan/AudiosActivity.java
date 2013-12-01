package love.yippy.chan;

import java.util.ArrayList;
import java.util.HashMap;

import love.yippy.chan.adapter.AudiosAdapter;
import love.yippy.chan.utils.AudioFileHandler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class AudiosActivity extends Activity {

	private ListView mAudiosListView;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.audios_main);
		
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
		FrameLayout parentLayout = (FrameLayout) this.findViewById(R.id.audios_main_layout);
		parentLayout.setBackgroundColor(R.color.dark_blue);
		
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		
		LinearLayout listLayout = (LinearLayout) this.findViewById(R.id.audios_list_layout);
		TextView noDataLabel = (TextView) this.findViewById(R.id.no_audios_label);		
		this.mAudiosListView = (ListView) this.findViewById(R.id.audios_list_view);
		ArrayList<HashMap<String, String>> list = AudioFileHandler.loadAudioConfiguration(this);
		if(list != null){
			noDataLabel.setVisibility(View.INVISIBLE);
			listLayout.setVisibility(View.VISIBLE);
			
			AudiosAdapter adapter = new AudiosAdapter(this, list);
			mAudiosListView.setAdapter(adapter);
		}
		else{
			noDataLabel.setVisibility(View.VISIBLE);
			listLayout.setVisibility(View.INVISIBLE);
		}
		
	}	
}
