package love.yippy.chan.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import love.yippy.chan.AudiosActivity;
import love.yippy.chan.R;
import love.yippy.chan.utils.AudioFileHandler;
import love.yippy.chan.utils.Constants;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class AudiosAdapter extends BaseAdapter{

	private ArrayList<HashMap<String, String>> mAudios;
	private Context mContext;
	private LayoutInflater mInflater;
	
	private static class ViewHolder{
		//front view
		TextView titleLabel;
		TextView durationLabel;
		TextView fileSizeLabel;
		
		//back view
		EditText titleEidtView;
		TextView progressLabel;
		ImageButton deleteBtn;
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
			
			//front view
			holder.titleLabel = (TextView) convertView.findViewById(R.id.audio_title_view);
			holder.durationLabel = (TextView) convertView.findViewById(R.id.audio_duration_view);
			holder.fileSizeLabel = (TextView) convertView.findViewById(R.id.auido_file_size_view);
			
			//back view
			holder.titleEidtView = (EditText) convertView.findViewById(R.id.audio_title_edit_view);
			holder.progressLabel = (TextView) convertView.findViewById(R.id.audio_progress_label);
			holder.deleteBtn = (ImageButton) convertView.findViewById(R.id.audio_delete_btn);
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
		holder.progressLabel.setText("00:00:00");
		holder.playingSeekBar.setProgress(0);	
		holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				new AlertDialog.Builder(mContext)
				.setTitle("…æ≥˝“Ù∆µ")
				.setMessage("»∑∂®…æ≥˝¥À¬º“Ù√¥£ø")
				.setNegativeButton("À„¡À", new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
					
				})
				.setPositiveButton("…æ¡À", new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub						
						HashMap<String, String> audioInfo = mAudios.get(index);
						String fileName = audioInfo.get("file");
						String filePath = AudioFileHandler.generateAudioPath(fileName);
						File audioFile = new File(filePath);
						if(audioFile.exists()){
							audioFile.delete();
							mAudios.remove(index);
							
							AudiosAdapter.this.notifyDataSetChanged();
							
							AudiosActivity activity = (AudiosActivity) mContext;
							activity.requestStopPlaying();
							if(mAudios == null || mAudios.size() == 0){								
								activity.relayoutWhenNoAudios();
							}
							else{
								activity.refreshListView();
							}
						}	
					}
					
				}).show();		
			}
		});

		holder.playingSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				if(Constants.DEBUG){
					Log.v(Constants.DEBUG_TAG, "seekbar onStopTrackingTouch");
				}				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				if(Constants.DEBUG){
					Log.v(Constants.DEBUG_TAG, "seekbar onStartTrackingTouch");
				}
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if(Constants.DEBUG){
					Log.v(Constants.DEBUG_TAG, "seekbar onProgressChanged: progress=" + progress + ", fromUser=" + fromUser);
				}
				
				if(fromUser){
					AudiosActivity activity = (AudiosActivity) mContext;
					activity.requestSeekToPlay(progress / 100.0f);
				}
				
			}
		});
		
		return convertView;
	}
	
	public void refreshRowViewWhenPlaying(View rowView, int progress, String formattedTimeStr){		
		if(rowView != null){
			ViewHolder holder = (ViewHolder) rowView.getTag();		
			holder.playingSeekBar.setProgress(progress);		
			holder.progressLabel.setText(formattedTimeStr);
		}
	}
	
	public void refreshRowViewWhenDragToPlay(View rowView, String formattedTimeStr){
		if(rowView != null){
			ViewHolder holder = (ViewHolder) rowView.getTag();		
			holder.progressLabel.setText(formattedTimeStr);
		}
	}
	
	public void refreshAudioTitleAtRow(View rowView, int position){	
		if(rowView != null){
			ViewHolder holder = (ViewHolder) rowView.getTag();		
			String editText = holder.titleEidtView.getText().toString();
			if(!editText.equals(holder.titleLabel.getText().toString())){
				
				if(Constants.DEBUG){
					Log.v(Constants.DEBUG_TAG, "title change to " + editText);
				}
				
				holder.titleLabel.setText(editText);		
				HashMap<String, String> audioInfo = mAudios.get(position);
				audioInfo.put("title", holder.titleEidtView.getText().toString());
				this.notifyDataSetChanged();
			}
		}
	}
}
