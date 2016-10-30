package com.snalopainen.android_listview_filter;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

/*
 * @author: snalopainen
 * @email: snajdan0525@gmail.com
 * @github: https://github.com/snalopainen
 * 
 * 
 * a ListView hold a header pinned view at the top of the ListView.
 * 
 */
public class PinnedHeaderListView extends ListView implements OnScrollListener {
	private Context mContext;
	private View mCurrentPinnedHeaderView;// pinned header view
	private OnScrollListener mOnScrollListener;
	private float mHeaderOffset;
	private PinnedListViewAdapter mAdapter;

	public static interface PinnedSectionedHeaderAdapter {
		public boolean isSectionHeader(int position);

		public int getSectionForPosition(int position);

		public int getSectionHeaderViewType(int section);

		public int getCount();

	}

	public PinnedHeaderListView(Context context) {
		super(context);
		this.mContext = context;
	}

	public PinnedHeaderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public PinnedHeaderListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (mOnScrollListener != null)
			mOnScrollListener.onScrollStateChanged(view, scrollState);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(view, firstVisibleItem,
					visibleItemCount, totalItemCount);
		}
		mHeaderOffset = 0;
		if (firstVisibleItem < getHeaderViewsCount()) {
			for (int i = 0; i < firstVisibleItem + visibleItemCount; i++) {
				View header = getChildAt(i);
				if (header != null) {
					header.setVisibility(View.VISIBLE);
				}
			}
		}
		firstVisibleItem -= getHeaderViewsCount();
		int section = mAdapter.getSectionForPosition(firstVisibleItem);
		int viewType = HEADER_VIEW_TYPE;
		mCurrentPinnedHeaderView = mAdapter.getView(section,mCurrentHeaderViewType != viewType ? null : mCurrentPinnedHeaderView, this);
		mCurrentHeaderViewType = viewType;
		mHeaderOffset = 0.0f;

		for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
			if( mAdapter.isSectionHeader(i)){
				View headerView = getChildAt(i-firstVisibleItem);
				float headerTop = headerView.getTop();
				float pinnedHeaderHeight = mCurrentPinnedHeaderView.getMeasuredHeight();
				headerView.setVisibility(View.VISIBLE);
				if (pinnedHeaderHeight >= headerTop && headerTop > 0) {
					mHeaderOffset = headerTop;// may be wrong
				} else if (headerTop <= 0) {
					headerView.setVisibility(View.INVISIBLE);
				}
			}
		}
		
		invalidate();
	}

	private int mCurrentHeaderViewType = 0;
	private static int HEADER_VIEW_TYPE = 0;

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mCurrentPinnedHeaderView == null) {
			return;
		}
		int saveCount = canvas.save();
		canvas.translate(0, mHeaderOffset);
		canvas.clipRect(0, 0, mCurrentPinnedHeaderView.getWidth(),
				mCurrentPinnedHeaderView.getMeasuredHeight());
		mCurrentPinnedHeaderView.draw(canvas);
		canvas.restoreToCount(saveCount);
	}

	private int mCurrentSection = 0;

	private View getSectionHeaderView(int section, View oldView) {
		return null;
	}
}
