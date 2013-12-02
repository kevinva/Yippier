package love.yippy.chan.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import love.yippy.chan.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AudiosAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, String>> mAudios;
	private Context mContext;
	private LayoutInflater mInflater;
	
	static class ViewHolder{
		TextView titleLabel;
		TextView durationLabel;
		TextView fileSizeLabel;
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
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.audio_row, parent, false);
			holder = new ViewHolder();
			holder.titleLabel = (TextView) convertView.findViewById(R.id.audio_title_view);
			holder.durationLabel = (TextView) convertView.findViewById(R.id.audio_duration_view);
			holder.fileSizeLabel = (TextView) convertView.findViewById(R.id.auido_file_size_view);			
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, String> audioInfo = mAudios.get(position);
		holder.titleLabel.setText(audioInfo.get("title"));
		holder.durationLabel.setText(audioInfo.get("duration"));
		holder.fileSizeLabel.setText(audioInfo.get("fileSize"));
		
		return convertView;
	}
}
