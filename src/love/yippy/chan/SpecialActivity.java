package love.yippy.chan;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class SpecialActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.special);
		
		initLayout();
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		
		return true;
	}
	
	private void initLayout(){
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		this.getActionBar().setTitle("Special");
		
	}
	
}
