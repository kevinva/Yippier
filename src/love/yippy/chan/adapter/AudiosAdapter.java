package love.yippy.chan.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import love.yippy.chan.R;
import android.content.Context;
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
				
			}
		});

		holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
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
