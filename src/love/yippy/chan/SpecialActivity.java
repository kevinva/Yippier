package love.yippy.chan;


import love.yippy.chan.utils.Constants;
import love.yippy.chan.utils.KevinPlayer;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class SpecialActivity extends Activity {

	private KevinPlayer mPlayer;
	private SeekBar mPlayingSeekBar;
	
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.special);		
		initLayout();
		
		this.mPlayer = new KevinPlayer();
		mPlayer.setOnPlayingListener(new KevinPlayer.onPlayingListener() {
			
			@Override
			public void onUpdateProgress(KevinPlayer player) {
				// TODO Auto-generated method stub
				mPlayingSeekBar.setProgress(player.getCurrentPosition() * 100 / player.getDuration());
			}
			
			@Override
			public void onFinishPlaying(KevinPlayer player) {
				// TODO Auto-generated method stub
				mPlayingSeekBar.setProgress(0);
			}
		});
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
		
		mPlayingSeekBar = (SeekBar) this.findViewById(R.id.play_spe_seekbar);
		mPlayingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				if(fromUser){
					if(mPlayer.isPlaying()){
						mPlayer.seek(progress / 100.0f);
					}
				}
			}
		});
		
		ImageButton playBtn = (ImageButton) this.findViewById(R.id.play_spe_button);
		playBtn.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.button_play_spe_drawable));
		playBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImageButton clicked = (ImageButton) v;
				
				if(!(mPlayer.isPlaying())){
					mPlayer.play(SpecialActivity.this, "special.mp3");
					clicked.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_pause_special_drawable));
				}
				else{
					mPlayer.pause();
					clicked.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_play_spe_drawable));
				}
				
			}
		});
	}
	
}
