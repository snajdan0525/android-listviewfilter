package com.snalopainen.android_listview_filter;

import java.util.ArrayList;

import com.snalopainen.android_listview_filter.PinnedHeaderListView.PinnedSectionedHeaderAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PinnedListViewAdapter extends BaseAdapter implements
		PinnedSectionedHeaderAdapter {
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SECTION = 1;
	LayoutInflater mLayoutInflater;
	private Context mContext;
	// array list to store section position
	ArrayList<Integer> mListSectionPos;

	// array list to store list view data
	ArrayList<String> mListItems;

	public PinnedListViewAdapter(Context context, ArrayList<String> listItems,
			ArrayList<Integer> listSectionPos) {
		this.mContext = context;
		this.mListItems = listItems;
		this.mListSectionPos = listSectionPos;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mListItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mListItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mListItems.get(position).hashCode();
	}

	@Override
	public int getItemViewType(int position) {
		return mListSectionPos.contains(position) ? TYPE_SECTION : TYPE_ITEM;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			int type = getItemViewType(position);
			switch (type) {
			case TYPE_ITEM:
				convertView = mLayoutInflater.inflate(R.layout.list_item, null);
				break;
			case TYPE_SECTION:
				convertView = mLayoutInflater.inflate(R.layout.header_item,
						null);
				break;
			default:
				break;
			}
			holder.textView = (TextView) convertView
					.findViewById(R.id.textItem);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textView.setText(mListItems.get(position).toString());
		return convertView;
	}

	private class ViewHolder {
		public TextView textView;
	}

	@Override
	public boolean isSectionHeader(int position) {
		return getItemViewType(position) == TYPE_SECTION ? true : false;
	}

	@Override
	public int getSectionForPosition(int position) {
		Integer sectionPos = mListSectionPos.get(position);
		if (sectionPos != null)
			return sectionPos;
		for( int index = 0 ; index< mListSectionPos.size() ; index++){
			int sectionStart = mListSectionPos.get(index);
			int sectionEnd = mListSectionPos.get(index+1);
			if( position > sectionStart && position < sectionEnd){
				return sectionStart;
			}
		}
		return 0;
	}

	@Override
	public int getSectionHeaderViewType(int section) {
		// TODO Auto-generated method stub
		return 0;
	}
}
