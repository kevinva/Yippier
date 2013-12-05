package love.yippy.chan;

import java.util.ArrayList;
import java.util.HashMap;

import love.yippy.chan.adapter.AudiosAdapter;
import love.yippy.chan.utils.AudioFileHandler;
import love.yippy.chan.utils.Constants;
import love.yippy.chan.utils.KevinPlayer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;


@SuppressLint("ResourceAsColor")
public class AudiosActivity extends Activity{

	private SwipeListView mAudiosListView;
	private LinearLayout mListLayout;
	private LinearLayout mNoAudiosLayout;
	private int mOpenedIndex = -1;
	private KevinPlayer mRecordPlayer;
	private ArrayList<HashMap<String, String>> mAudios;
	
	private Handler mPlayingHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			if(mOpenedIndex != -1){
				switch(msg.what){
				case Constants.MESSAGE_PLAYING:
					AudiosAdapter adapter = (AudiosAdapter) mAudiosListView.getAdapter();
					int progress = mRecordPlayer.getCurrentPosition() * 100 / mRecordPlayer.getDuration();
					String formattedTimeStr = AudioFileHandler.formatAudioDuration(mRecordPlayer.getCurrentPosition());
					adapter.refreshRowViewWhenPlaying(mAudiosListView.getChildAt(mOpenedIndex), progress, formattedTimeStr);
					break;
				}
			}
		}
		
	};
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		this.mRecordPlayer = new KevinPlayer();
		mRecordPlayer.setOnPlayingListener(new KevinPlayer.onPlayingListener() {
			
			@Override
			public void onUpdateProgress(KevinPlayer player) {
				// TODO Auto-generated method stub
				
				if(Constants.DEBUG){
					Log.v(Constants.DEBUG_TAG, "onUpdateProgress");
				}
				
				mPlayingHandler.sendEmptyMessage(Constants.MESSAGE_PLAYING);
			}
			
			@Override
			public void onFinishPlaying(KevinPlayer player) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		this.setContentView(R.layout.audios_main);		
		this.initLayout();
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == android.R.id.home){
			if(mRecordPlayer != null){
				if(mRecordPlayer.isPlaying()){
					mRecordPlayer.stop();
				}
			}
			
			AudioFileHandler.saveAudiosConfiguration(this, mAudios);
			
			finish();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void relayoutWhenNoAudios(){
		mNoAudiosLayout.setVisibility(View.VISIBLE);
		mListLayout.setVisibility(View.INVISIBLE);
	}
	
	public void refreshListView(){
		mAudiosListView.closeOpenedItems();
	}
	
	public void requestSeekToPlay(float percent){
		if(mRecordPlayer != null){
			mRecordPlayer.seek(percent);
			
			if(mOpenedIndex != -1){
				AudiosAdapter adapter = (AudiosAdapter) mAudiosListView.getAdapter();
				String formattedTimeStr = AudioFileHandler.formatAudioDuration((int)(mRecordPlayer.getDuration() * percent));
				adapter.refreshRowViewWhenDragToPlay(mAudiosListView.getChildAt(mOpenedIndex), formattedTimeStr);
			}
		}		
	}
	
	private void initLayout(){		
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mListLayout = (LinearLayout) this.findViewById(R.id.audios_list_layout);
		mNoAudiosLayout = (LinearLayout) this.findViewById(R.id.no_audios_layout);
		mAudiosListView = (SwipeListView) this.findViewById(R.id.audio_list_view);
		mAudios = AudioFileHandler.loadAudiosConfiguration(this);
		if(mAudios != null && mAudios.size() > 0){
			mNoAudiosLayout.setVisibility(View.INVISIBLE);
			mListLayout.setVisibility(View.VISIBLE);
			
			AudiosAdapter adapter = new AudiosAdapter(this, mAudios);
			mAudiosListView.setAdapter(adapter);
		}
		else{
			mNoAudiosLayout.setVisibility(View.VISIBLE);
			mListLayout.setVisibility(View.INVISIBLE);
		}		
		mAudiosListView.setSwipeListViewListener(new BaseSwipeListViewListener(){

			@Override
			public void onOpened(int position, boolean toRight) {
				// TODO Auto-generated method stub
				super.onOpened(position, toRight);
				
				mOpenedIndex = position;
				
				if(Constants.DEBUG){
					Log.v(Constants.DEBUG_TAG, "onOpened at position: " + position);
				}
				
				HashMap<String, String> audioInfo = mAudios.get(position);
				String fileName = audioInfo.get("file");
				String filePath = AudioFileHandler.generateAudioPath(fileName);
				if(mRecordPlayer != null){
					mRecordPlayer.play(filePath);
				}				
			}

			@Override
			public void onClosed(int position, boolean fromRight) {
				// TODO Auto-generated method stub
				super.onClosed(position, fromRight);
				
				if(Constants.DEBUG){
					Log.v(Constants.DEBUG_TAG, "onClosed at position: " + position);
				}
				
				if(mRecordPlayer != null){
					if(mRecordPlayer.isPlaying()){
						mRecordPlayer.stop();
					}
				}
				
				AudiosAdapter adapter = (AudiosAdapter) mAudiosListView.getAdapter();
				adapter.refreshAudioTitleAtRow(mAudiosListView.getChildAt(position), position);
				
			}

			@Override
			public void onListChanged() {
				// TODO Auto-generated method stub
				super.onListChanged();
				
				if(Constants.DEBUG){
					Log.v(Constants.DEBUG_TAG, "onListChanged");
				}				
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
				
				if(Constants.DEBUG){
					Log.v(Constants.DEBUG_TAG, "onStartOpen: lastopenedIndex=" + mOpenedIndex + ", position=" + position);
				}				

				if(mOpenedIndex != position){					
					if(mOpenedIndex != -1){
						mAudiosListView.closeAnimate(mOpenedIndex);
					}
				}
			}

			@Override
			public void onStartClose(int position, boolean right) {
				// TODO Auto-generated method stub
				super.onStartClose(position, right);
				
				if(Constants.DEBUG){
					Log.v(Constants.DEBUG_TAG, "onStartClose at position: " + position);
				}
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
				
				if(Constants.DEBUG){
					Log.v(Constants.DEBUG_TAG, "onDismiss: " + reverseSortedPositions);
				}				
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
		
	}
	
	
}
