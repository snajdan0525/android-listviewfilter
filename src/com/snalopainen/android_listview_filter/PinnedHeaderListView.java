package com.snalopainen.android_listview_filter;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
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
    private int mWidthMode;
    private int mHeightMode;
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
		ensurePinnedHeaderLayout(mCurrentPinnedHeaderView);
		mHeaderOffset = 0.0f;

		for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
			if( mAdapter.isSectionHeader(i)){
				View headerView = getChildAt(i-firstVisibleItem);
				float headerTop = headerView.getTop();
				float pinnedHeaderHeight = mCurrentPinnedHeaderView.getMeasuredHeight();
				headerView.setVisibility(View.VISIBLE);
				if (pinnedHeaderHeight >= headerTop && headerTop > 0) {
					mHeaderOffset = headerTop-headerView.getHeight();
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
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        mHeightMode = MeasureSpec.getMode(heightMeasureSpec);
	}
	private int mCurrentSection = 0;

	private View getSectionHeaderView(int section, View oldView) {
		return null;
	}
    private void ensurePinnedHeaderLayout(View header) {
        if (header.isLayoutRequested()) {
            int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), mWidthMode);//子视图的MeasureSpec
            
            int heightSpec;
            ViewGroup.LayoutParams layoutParams = header.getLayoutParams();
            if (layoutParams != null && layoutParams.height > 0) {
                heightSpec = MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY);
            } else {
                heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            }
            header.measure(widthSpec, heightSpec);//设置真正的子视图大小，设置即子视图的measuredWigth , mesuredHeight
            header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());//根据子视图的measuredHeigh和mesauredWidth设置位置
        }
    }
}
