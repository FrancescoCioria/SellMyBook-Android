package com.mosquitolabs.sharemynote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyCustomAdapterSearch extends BaseAdapter {
	private LayoutInflater mInflater;
	private BookCollection bookCollection = BookCollection.getInstance();

	public MyCustomAdapterSearch(Context paramContext) {
		this.mInflater = LayoutInflater.from(paramContext);
	}

	public int getCount() {

		return bookCollection.getSearchResult().size();

	}

	public Object getItem(int paramInt) {
		return Integer.valueOf(paramInt);
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	public View getView(final int paramInt, View paramView,
			ViewGroup paramViewGroup) {
		ViewHolderStarPlaces localViewHolder;
		BookData temp = new BookData();
		temp = bookCollection.getSearchResult().get(paramInt);

		final BookData book = temp;
		paramView = mInflater.inflate(R.layout.book_list_item_search, null);
		localViewHolder = new ViewHolderStarPlaces();
		localViewHolder.topMargin = (RelativeLayout) paramView.findViewById(R.id.book_list_item_TopMargin);
		localViewHolder.bottomMargin = (RelativeLayout) paramView.findViewById(R.id.book_list_item_BottomMargin);

		if (paramInt == 0) {
			localViewHolder.topMargin.setVisibility(View.VISIBLE);
		} else {
			localViewHolder.topMargin.setVisibility(View.GONE);
		}

		if (paramInt == bookCollection.getSearchResult().size() - 1) {
			localViewHolder.bottomMargin.setVisibility(View.VISIBLE);
		} else {
			localViewHolder.bottomMargin.setVisibility(View.GONE);
		}
		paramView.setTag(localViewHolder);

		return paramView;
	}

	static class ViewHolderStarPlaces {
		TextView title;
		TextView author;
		RelativeLayout topMargin;
		RelativeLayout bottomMargin;
	}
}
