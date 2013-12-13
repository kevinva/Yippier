package love.yippy.chan.preference;

import love.yippy.chan.R;
import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class MyListPreference extends ListPreference {

	public String mTitle;
	public String mSummary;
	private TextView mTitleLabel;
	private TextView mSummaryLabel;
	
	public MyListPreference(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	public MyListPreference(Context context){
		super(context);
	}
	
	public boolean isPersistent(){
		return true;
	}
	
	protected void onBindView(View view){
		super.onBindView(view);
		
		this.mTitleLabel = (TextView) view.findViewById(R.id.pref_title_label);
		mTitleLabel.setText(mTitle);
		
		mSummaryLabel = (TextView) view.findViewById(R.id.pref_summary_label);
		mSummaryLabel.setText(mSummary);
		
		int titleTextColorRes = 0;
		int summaryTextColorRes = 0;
		if(false){
			titleTextColorRes = R.color.kevin_spring_green2;
			summaryTextColorRes = R.color.kevin_spring_green2;
		}else if(false){
			titleTextColorRes = R.color.kevin_summer_blue2;
			summaryTextColorRes = R.color.kevin_summer_blue2;
		}else if(false){
			titleTextColorRes = R.color.kevin_autumu_yellow2;
			summaryTextColorRes = R.color.kevin_autumu_yellow2;
		}
		else{
			titleTextColorRes = R.color.kevin_blue1;
			summaryTextColorRes = R.color.kevin_blue1;
		}
		mTitleLabel.setTextColor(this.getContext().getResources().getColor(titleTextColorRes));
		mSummaryLabel.setTextColor(this.getContext().getResources().getColor(summaryTextColorRes));
	}

	@Override
	public void setSummary(CharSequence summary) {
		// TODO Auto-generated method stub

		mSummaryLabel.setText(summary);
	}
}
