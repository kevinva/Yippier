package love.yippy.chan;

import love.yippy.chan.utils.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

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
		this.getActionBar().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.action_bar_special_drawable));
		
		ImageButton playBtn = (ImageButton) this.findViewById(R.id.play_spe_button);
		playBtn.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.button_play_spe_drawable));
		playBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				
				
			}
		});
	}
	
}
