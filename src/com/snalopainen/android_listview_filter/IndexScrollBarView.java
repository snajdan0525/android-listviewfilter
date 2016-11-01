package com.snalopainen.android_listview_filter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

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

	private int perSectionHeight;

	@Override
	protected void onDraw(Canvas canvas) {

		if (mListSection != null && mListSection.size() > 0) {
			perSectionHeight = (getMeasuredHeight() - 2 * mIndexBarViewMargin)
					/ mListSection.size();

			for (int i = 0; i < mListSection.size(); i++) {
				int pos = mListSection.get(i);
				String section = mListItems.get(pos);
				float paddingLeft = (getMeasuredWidth() - mIndexBarPaint
						.measureText(section)) / 2;
				canvas.drawText(section, paddingLeft, perSectionHeight
						* (i + 1), mIndexBarPaint);
			}
		}
		super.onDraw(canvas);
	}

	private boolean isInTheRangeOfIndexBarView(MotionEvent event) {
		return event.getX() >= getLeft() && event.getY() >= getTop()
				&& event.getY() <= getTop() + getMeasuredHeight();
	}

	private int fliterItems(float y) {
		return (int) ((y - mIndexBarViewMargin) / perSectionHeight);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (isInTheRangeOfIndexBarView(event)) {
				int section = fliterItems(event.getY());
				String sectionText = mListItems.get(mListSection.get(section));
				Log.i("onTouchEvent", "按下了:" + sectionText);
				return true;
			} else {
				return false;
			}
		case MotionEvent.ACTION_MOVE:
			if (isInTheRangeOfIndexBarView(event)) {
				int section = fliterItems(event.getY());
				String sectionText = mListItems.get(mListSection.get(section));
				Log.i("onTouchEvent", "按下了:" + sectionText);
				return true;
			} else {
				return false;
			}
		case MotionEvent.ACTION_UP:

			break;
		default:
			break;
		}
		return false;
	}
}
