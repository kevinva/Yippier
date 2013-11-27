package love.yippy.chan;

import java.util.ArrayList;

import love.yippy.chan.adapter.AudiosAdapter;
import love.yippy.chan.modal.YippyAudio;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;

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
		LinearLayout parentLayout = (LinearLayout) this.findViewById(R.id.audios_main_layout);
		parentLayout.setBackgroundColor(R.color.dark_blue);
		
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		
		this.mAudiosListView = (ListView) this.findViewById(R.id.audios_list_view);
		ArrayList<YippyAudio> list = this.prepareData();
		AudiosAdapter<YippyAudio> adapter = new AudiosAdapter<YippyAudio>(this, list);
		mAudiosListView.setAdapter(adapter);
		
	}
	
	private ArrayList<YippyAudio> prepareData(){
		ArrayList<YippyAudio> resultList = new ArrayList<YippyAudio>();
		for(int i = 0; i < 20; i++){
			resultList.add(new YippyAudio());
		}
		
		return resultList;
	}
	
}
