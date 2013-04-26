package com.mosquitolabs.sharemynote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyCustomAdapterSell extends BaseAdapter {
	private LayoutInflater mInflater;
	private BookCollection bookCollection = BookCollection.getInstance();
	private int currentList;

	private static final int ALL = 0;
	private static final int SELLING = 1;
	private static final int CURRENT = 2;
	private static final int SOLD = 3;

	private int size;

	public MyCustomAdapterSell(Context paramContext, int currentList) {
		this.mInflater = LayoutInflater.from(paramContext);
		this.currentList = currentList;
	}

	public int getCount() {
		switch (currentList) {
		case ALL:

			size = bookCollection.getSellAll().size();
			break;
		case SELLING:

			size = bookCollection.getSellSelling().size();
			break;

		case CURRENT:

			size = bookCollection.getSellCurrent().size();
			break;

		case SOLD:

			size = bookCollection.getSellSold().size();
			break;

		}
		return (size);

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

		switch (currentList) {
		case ALL:
			temp = bookCollection.getSellAll().get(paramInt);
			break;

		case SELLING:
			temp = bookCollection.getSellSelling().get(paramInt);
			break;

		case CURRENT:

			temp = bookCollection.getSellCurrent().get(paramInt);
			break;
		case SOLD:

			temp = bookCollection.getSellSold().get(paramInt);
			break;
		}

		final BookData book = temp;
		paramView = mInflater.inflate(R.layout.book_list_item_search, null);
		localViewHolder = new ViewHolderStarPlaces();
		
		localViewHolder.topMargin = (RelativeLayout) paramView.findViewById(R.id.book_list_item_TopMargin);
		localViewHolder.bottomMargin = (RelativeLayout) paramView.findViewById(R.id.book_list_item_BottomMargin);

		// CODICE COMUNE //

		// switch(book.sellingState){

		// case selling

		// case current

		// case sold
		if (paramInt == 0) {
			localViewHolder.topMargin.setVisibility(View.VISIBLE);
		} else {
			localViewHolder.topMargin.setVisibility(View.GONE);
		}

		if (paramInt == size - 1) {
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
