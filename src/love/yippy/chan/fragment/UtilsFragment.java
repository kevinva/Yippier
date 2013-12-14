package love.yippy.chan.fragment;

import love.yippy.chan.MyPreferenceActivity;
import love.yippy.chan.R;
import love.yippy.chan.utils.Constants;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UtilsFragment extends ListFragment{

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		UtilsAdapter adapter = new UtilsAdapter(getActivity());
//		adapter.add(new UtilItem("设置", android.R.drawable.ic_menu_preferences));
//		adapter.add(new UtilItem("关于", android.R.drawable.ic_menu_info_details));
		if(Constants.isSrping){
			adapter.add(new UtilItem("设置", R.drawable.icon_spring_settings));
			adapter.add(new UtilItem("关于", R.drawable.icon_spring_about_me));
		}
		else if(Constants.isSummer){
			adapter.add(new UtilItem("设置", R.drawable.icon_summer_settings));
			adapter.add(new UtilItem("关于", R.drawable.icon_summer_about_me));
		}
		else if(Constants.isAutumu){
			adapter.add(new UtilItem("设置", R.drawable.icon_autumu_settings));
			adapter.add(new UtilItem("关于", R.drawable.icon_autumu_about_me));
		}
		else{
			adapter.add(new UtilItem("设置", R.drawable.icon_winter_settings));
			adapter.add(new UtilItem("关于", R.drawable.icon_winter_about_me));
		}

		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		switch(position){
		case 0:
			Intent intent = new Intent(this.getActivity(), MyPreferenceActivity.class);
			this.getActivity().startActivity(intent);
			break;
		case 1:
			break;

		}
	}

	private class UtilItem {
		public String tag;
		public int iconRes;
		
		public UtilItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}

	public class UtilsAdapter extends ArrayAdapter<UtilItem> {

		public UtilsAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);

			return convertView;
		}
	}
	
	
	
}
