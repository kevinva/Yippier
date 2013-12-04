package love.yippy.chan.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import love.yippy.chan.AudiosActivity;
import love.yippy.chan.R;
import love.yippy.chan.utils.AudioFileHandler;
import love.yippy.chan.utils.KevinPlayer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class AudiosAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, String>> mAudios;
	private Context mContext;
	private LayoutInflater mInflater;
	private int mPlayingIndex = -1;
	
	static class ViewHolder{
		//front view
		TextView titleLabel;
		TextView durationLabel;
		TextView fileSizeLabel;
		
		//back view
		EditText titleEidtView;
		Button playBtn;
		Button deleteBtn;
		SeekBar playingSeekBar;
	}
	
	public AudiosAdapter(Context ctx, ArrayList<HashMap<String, String>> list){
		this.mContext = ctx;
		this.mAudios = list;
		this.mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAudios != null ? mAudios.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if((mAudios != null && mAudios.size() > 0) && (position >= 0 && position < mAudios.size())){
			return mAudios.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		final int index = position;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.audio_row, parent, false);
			holder = new ViewHolder();
			holder.titleLabel = (TextView) convertView.findViewById(R.id.audio_title_view);
			holder.durationLabel = (TextView) convertView.findViewById(R.id.audio_duration_view);
			holder.fileSizeLabel = (TextView) convertView.findViewById(R.id.auido_file_size_view);
			holder.titleEidtView = (EditText) convertView.findViewById(R.id.audio_title_edit_view);
			holder.playBtn = (Button) convertView.findViewById(R.id.audio_play_btn);
			holder.deleteBtn = (Button) convertView.findViewById(R.id.audio_delete_btn);
			holder.playingSeekBar = (SeekBar) convertView.findViewById(R.id.audio_playing_seek_bar);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		HashMap<String, String> audioInfo = mAudios.get(position);
		String audioTitle = audioInfo.get("title");
		String durationStr = audioInfo.get("duration");
		String fileSizeStr = audioInfo.get("fileSize");
		
		holder.titleLabel.setText(audioTitle);
		holder.durationLabel.setText(durationStr);
		holder.fileSizeLabel.setText(fileSizeStr);
		holder.titleEidtView.setText(audioTitle);
		
		holder.playBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Button clicked = (Button) v;
				
				KevinPlayer player = KevinPlayer.sharedInstance(mContext);
				HashMap<String, String> audioInfo = mAudios.get(index);
				String fileName = audioInfo.get("file");
				String filePath = AudioFileHandler.generateAudioPath(fileName);
				if(index != mPlayingIndex){
					mPlayingIndex = index;
					player.stop();
					player.play(filePath);
					
					clicked.setText("��ͣ");
				}
				else{
					if(player.isPlaying()){
						player.pause();
						
						clicked.setText("����");
					}
					else{
						player.resume();
						
						clicked.setText("��ͣ");
					}
				}
			}
		});

		holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new AlertDialog.Builder(mContext)
				.setTitle("ɾ����Ƶ")
				.setMessage("ȷ��ɾ����¼��ô��")
				.setNegativeButton("����", new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
					
				})
				.setPositiveButton("ɾ��", new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(index == mPlayingIndex){
							KevinPlayer player = KevinPlayer.sharedInstance(mContext);
							player.stop();
						}	
						
						HashMap<String, String> audioInfo = mAudios.get(index);
						String fileName = audioInfo.get("file");
						String filePath = AudioFileHandler.generateAudioPath(fileName);
						File audioFile = new File(filePath);
						if(audioFile.exists()){
							audioFile.delete();
							mAudios.remove(index);
							AudioFileHandler.saveAudiosConfiguration(mContext, mAudios);
							
							AudiosAdapter.this.notifyDataSetChanged();
							AudiosActivity activity = (AudiosActivity) mContext;
							activity.relayoutWhenNoAudios();
						}	
					}
					
				}).show();		
			}
		});

		holder.playingSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return convertView;
	}
}
