package love.yippy.chan.adapter;

import java.util.ArrayList;

import love.yippy.chan.modal.YippyAudio;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AudiosAdapter<YippyAudio> extends BaseAdapter {

	private ArrayList<YippyAudio> mAudios;
	private Context mContext;
	
	public AudiosAdapter(Context ctx, ArrayList<YippyAudio> list){
		this.mContext = ctx;
		this.mAudios = list;
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
		return null;
	}





}
