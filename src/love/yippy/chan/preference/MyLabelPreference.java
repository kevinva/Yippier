package love.yippy.chan.preference;

import love.yippy.chan.R;
import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyLabelPreference extends Preference {
	
	public String title;
	public String summary;

	public MyLabelPreference(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	public MyLabelPreference(Context context){
		super(context);
	}
	
	public boolean isPersistent(){
		return false;
	}
	
	protected void onBindView(View view){
		super.onBindView(view);
		
		TextView titleLabel = (TextView) view.findViewById(R.id.pref_title_label);
		titleLabel.setText(title);
		
		TextView summaryLabel = (TextView) view.findViewById(R.id.pref_summary_label);
		summaryLabel.setText(summary);
	}
}
