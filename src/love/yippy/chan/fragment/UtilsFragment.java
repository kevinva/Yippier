package love.yippy.chan.fragment;

import love.yippy.chan.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UtilsFragment extends ListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		UtilsAdapter adapter = new UtilsAdapter(getActivity());
		adapter.add(new UtilItem("…Ë÷√", android.R.drawable.ic_menu_search));
		adapter.add(new UtilItem("πÿ”⁄", android.R.drawable.ic_menu_send));
		setListAdapter(adapter);
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
