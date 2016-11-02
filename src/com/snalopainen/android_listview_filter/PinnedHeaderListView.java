package com.snalopainen.android_listview_filter;

import org.w3c.dom.Text;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;
import android.widget.Toast;

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
	private View mIndexScrollBarView;// 索引条
	private View mPreviewView;
	private OnScrollListener mOnScrollListener;
	private float mHeaderOffset;
	private PinnedListViewAdapter mAdapter;
	private int mWidthMode;
	private int mHeightMode;
	private int mCurrentHeaderViewType = 0;
	private static int HEADER_VIEW_TYPE = 0;
	private int mCurrentSection = 0;

	public static interface PinnedSectionedHeaderAdapter {
		public boolean isSectionHeader(int position);

		public int getSectionForPosition(int position);

		public int getSectionHeaderViewType(int section);

		public int getCount();

	}

	public PinnedHeaderListView(Context context) {
		super(context);
		this.mContext = context;
		super.setOnScrollListener(this);
	}

	public PinnedHeaderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		super.setOnScrollListener(this);
	}

	public PinnedHeaderListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		super.setOnScrollListener(this);
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
		if (mAdapter == null || mAdapter.getCount() == 0
				|| firstVisibleItem < getHeaderViewsCount()) {
			mCurrentPinnedHeaderView = null;
			for (int i = 0; i < firstVisibleItem + visibleItemCount; i++) {
				View header = getChildAt(i);
				if (header != null) {
					header.setVisibility(View.VISIBLE);
				}
			}
			return;
		}
		firstVisibleItem -= getHeaderViewsCount();
		int section = mCurrentSection = mAdapter.getSectionForPosition(firstVisibleItem);
		int viewType = HEADER_VIEW_TYPE;
		mCurrentPinnedHeaderView = mAdapter.getView(section,
				mCurrentHeaderViewType != viewType ? null
						: mCurrentPinnedHeaderView, this);
		mCurrentHeaderViewType = viewType;
		ensurePinnedHeaderLayout(mCurrentPinnedHeaderView);
		mHeaderOffset = 0.0f;

		for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
			if (mAdapter.isSectionHeader(i)) {
				View headerView = getChildAt(i - firstVisibleItem);
				float headerTop = headerView.getTop();
				float pinnedHeaderHeight = mCurrentPinnedHeaderView
						.getMeasuredHeight();
				headerView.setVisibility(View.VISIBLE);
				if (pinnedHeaderHeight >= headerTop && headerTop > 0) {
					mHeaderOffset = headerTop - headerView.getHeight();
				} else if (headerTop <= 0) {
					headerView.setVisibility(View.INVISIBLE);
				}
			}
		}

		invalidate();
	}

	private boolean bIndexBarHasDrawn = false;

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
		if (mIndexScrollBarView != null) {
			bIndexBarHasDrawn = true;
			drawChild(canvas, mIndexScrollBarView, getDrawingTime());
			Log.i("drawChild(canvas, mIndexScrollBarView, getDrawingTime());", "drawChild(canvas, mIndexScrollBarView, getDrawingTime());");
		}
		if (mPreviewView != null) {
			
			Log.i("drawChild(canvas, mPreviewView, getDrawingTime());", "drawChild(canvas, mPreviewView, getDrawingTime());");
			Log.i("mAdapter.getItem(mCurrentSection).toString()",mAdapter.getItem(mCurrentSection).toString());
			if( mPreviewView instanceof TextView){
				((TextView) mPreviewView).setText(mAdapter.getItem(mCurrentSection).toString());
			}
			drawChild(canvas, mPreviewView, getDrawingTime()); 
		}
	}

	private int mIndexScrollBarViewWidth;
	private int mIndexScrollBarViewHeight;
	private int mPreviewViewWidth;
	private int mPreviewViewHeight;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidthMode = MeasureSpec.getMode(widthMeasureSpec);
		mHeightMode = MeasureSpec.getMode(heightMeasureSpec);

		if (mIndexScrollBarView != null && !bIndexBarHasDrawn) {
			measureChild(mIndexScrollBarView, widthMeasureSpec,
					heightMeasureSpec);
			mIndexScrollBarViewWidth = mIndexScrollBarView.getMeasuredWidth();
			mIndexScrollBarViewHeight = mIndexScrollBarView.getMeasuredHeight();
		}
		if (mPreviewView != null) {
			measureChild(mPreviewView, widthMeasureSpec, heightMeasureSpec);
			mPreviewViewWidth = mPreviewView.getMeasuredWidth();
			mPreviewViewHeight = mPreviewView.getMeasuredHeight();
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (mIndexScrollBarView != null && !bIndexBarHasDrawn) {
			mIndexScrollBarView.layout(getMeasuredWidth()
					- mIndexScrollBarViewWidth - mIndexBarViewMargin,
					mIndexBarViewMargin, getMeasuredWidth()
							- mIndexBarViewMargin, getMeasuredHeight()
							- mIndexBarViewMargin);
		}
		if (mPreviewView != null) {
			mPreviewView.layout(getMeasuredWidth() - mIndexScrollBarViewWidth
					- mIndexBarViewMargin - mPreviewViewWidth,
					mIndexBarViewMargin + mIndexScrollBarViewHeight / 2
							- mPreviewViewHeight / 2, getMeasuredWidth()
							- mIndexScrollBarViewWidth - mIndexBarViewMargin,
					mIndexBarViewMargin + mIndexScrollBarViewHeight / 2
							+ mPreviewViewHeight / 2);
		}
	}

	private void ensurePinnedHeaderLayout(View header) {
		if (header.isLayoutRequested()) {
			int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(),
					mWidthMode);// 子视图的MeasureSpec

			int heightSpec;
			ViewGroup.LayoutParams layoutParams = header.getLayoutParams();
			if (layoutParams != null && layoutParams.height > 0) {
				heightSpec = MeasureSpec.makeMeasureSpec(layoutParams.height,
						MeasureSpec.EXACTLY);
			} else {
				heightSpec = MeasureSpec.makeMeasureSpec(0,
						MeasureSpec.UNSPECIFIED);
			}
			header.measure(widthSpec, heightSpec);// 设置真正的子视图大小，设置即子视图的measuredWigth
													// , mesuredHeight
			header.layout(0, 0, header.getMeasuredWidth(),
					header.getMeasuredHeight());// 根据子视图的measuredHeigh和mesauredWidth设置位置
		}
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		mCurrentPinnedHeaderView = null;
		this.mAdapter = (PinnedListViewAdapter) adapter;
		super.setAdapter(adapter);
	}

	private int mIndexBarViewMargin;

	public void setIndexScrollBarView(View indexScrollBarView) {
		mIndexBarViewMargin = (int) mContext.getResources().getDimension(
				R.dimen.index_bar_view_margin);
		this.mIndexScrollBarView = indexScrollBarView;
	}
	public void setPreviewView(View view){
		this.mPreviewView = view;
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mIndexScrollBarView != null
				&& ((IndexScrollBarView) mIndexScrollBarView).onTouchEvent(ev)) {
			return true;
		}
		return super.onTouchEvent(ev);
	}
	public void filterListView( int sectionPos , String text ){
		if( mPreviewView instanceof TextView){
			((TextView) mPreviewView).setText(text);
		}
		setSelection(sectionPos);
	}
}
