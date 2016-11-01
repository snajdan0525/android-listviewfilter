package com.snalopainen.android_listview_filter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class IndexScrollBarView extends View {
	private ArrayList<String> mListItems;
	private ArrayList<Integer> mListSection;
	private ListView mListView;
	private Context mContext;

	public IndexScrollBarView(Context context) {
		super(context);
		this.mContext = context;
	}

	public IndexScrollBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public IndexScrollBarView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
	}

	private int mIndexBarViewMargin;
	private Paint mIndexBarPaint;

	public void setIndexScrollViewData(ListView listview,
			ArrayList<String> listItems, ArrayList<Integer> listSection) {
		this.mListView = listview;
		this.mListSection = listSection;
		this.mListItems = listItems;
		mIndexBarViewMargin = (int) mContext.getResources().getDimension(
				R.dimen.index_bar_view_margin);
		mIndexBarPaint = new Paint();
		mIndexBarPaint.setColor(mContext.getResources().getColor(
				R.color.color_black));
		mIndexBarPaint.setAntiAlias(true);
		mIndexBarPaint.setTextSize(mContext.getResources().getDimension(
				R.dimen.index_bar_view_text_size));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mListSection != null && mListSection.size() > 0) {
			int perSectionHeight = (getMeasuredHeight() - 2 * mIndexBarViewMargin)
					/ mListSection.size();
			
			for (int i = 0; i < mListSection.size(); i++) {
				int pos = mListSection.get(i);
				String section = mListItems.get(pos);
				Log.i("onDraw", section);
				float paddingLeft = (getMeasuredWidth()-mIndexBarPaint.measureText(section))/2;
				canvas.drawText(section, paddingLeft, perSectionHeight * (i+1),
						mIndexBarPaint);
			}
		}
		super.onDraw(canvas);
	}
}
