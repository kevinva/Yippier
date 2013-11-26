package love.yippy.chan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;

@SuppressLint("ResourceAsColor")
public class AudiosActivity extends Activity {

	private ListView audiosListView;
	
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
		
		this.audiosListView = (ListView) this.findViewById(R.id.audios_list_view);

		
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
}
