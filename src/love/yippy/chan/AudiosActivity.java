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

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;


@SuppressLint("ResourceAsColor")
public class AudiosActivity extends Activity{

	private SwipeListView mAudiosListView;
	
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
		
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		
		LinearLayout listLayout = (LinearLayout) this.findViewById(R.id.audios_list_layout);
		LinearLayout noAudiosLayout = (LinearLayout) this.findViewById(R.id.no_audios_layout);		
		mAudiosListView = (SwipeListView) listLayout.findViewById(R.id.audio_list_view);
		mAudiosListView.setSwipeListViewListener(new BaseSwipeListViewListener(){

			@Override
			public void onOpened(int position, boolean toRight) {
				// TODO Auto-generated method stub
				super.onOpened(position, toRight);
			}

			@Override
			public void onClosed(int position, boolean fromRight) {
				// TODO Auto-generated method stub
				super.onClosed(position, fromRight);
				
				
			}

			@Override
			public void onListChanged() {
				// TODO Auto-generated method stub
				super.onListChanged();
			}

			@Override
			public void onMove(int position, float x) {
				// TODO Auto-generated method stub
				super.onMove(position, x);
			}

			@Override
			public void onStartOpen(int position, int action, boolean right) {
				// TODO Auto-generated method stub
				super.onStartOpen(position, action, right);
			}

			@Override
			public void onStartClose(int position, boolean right) {
				// TODO Auto-generated method stub
				super.onStartClose(position, right);
			}

			@Override
			public void onClickFrontView(int position) {
				// TODO Auto-generated method stub
				super.onClickFrontView(position);
			}

			@Override
			public void onClickBackView(int position) {
				// TODO Auto-generated method stub
				super.onClickBackView(position);
			}

			@Override
			public void onDismiss(int[] reverseSortedPositions) {
				// TODO Auto-generated method stub
				super.onDismiss(reverseSortedPositions);
			}

			@Override
			public int onChangeSwipeMode(int position) {
				// TODO Auto-generated method stub
				return super.onChangeSwipeMode(position);
			}

			@Override
			public void onChoiceChanged(int position, boolean selected) {
				// TODO Auto-generated method stub
				super.onChoiceChanged(position, selected);
			}

			@Override
			public void onChoiceStarted() {
				// TODO Auto-generated method stub
				super.onChoiceStarted();
			}

			@Override
			public void onChoiceEnded() {
				// TODO Auto-generated method stub
				super.onChoiceEnded();
			}

			@Override
			public void onFirstListItem() {
				// TODO Auto-generated method stub
				super.onFirstListItem();
			}

			@Override
			public void onLastListItem() {
				// TODO Auto-generated method stub
				super.onLastListItem();
			}
			
		});		
		
		ArrayList<HashMap<String, String>> list = AudioFileHandler.loadAudioConfiguration(this);
		if(list != null){
			noAudiosLayout.setVisibility(View.INVISIBLE);
			listLayout.setVisibility(View.VISIBLE);
			
			AudiosAdapter adapter = new AudiosAdapter(this, list);
			mAudiosListView.setAdapter(adapter);
		}
		else{
			noAudiosLayout.setVisibility(View.VISIBLE);
			listLayout.setVisibility(View.INVISIBLE);
		}
		
	}	
}
