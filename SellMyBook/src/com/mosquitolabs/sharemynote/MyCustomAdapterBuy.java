package com.mosquitolabs.sharemynote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MyCustomAdapterBuy extends BaseAdapter {
	private LayoutInflater mInflater;
	private BookCollection bookCollection = BookCollection.getInstance();
	private int currentList;

	private static final int ALL = 0;
	private static final int CURRENT = 1;
	private static final int BOUGHT = 2;

	public MyCustomAdapterBuy(Context paramContext, int currentList) {
		this.mInflater = LayoutInflater.from(paramContext);
		this.currentList = currentList;
	}

	public int getCount() {
		switch (currentList) {
		case ALL:

			return bookCollection.getBuyAll().size();

		case CURRENT:

			return bookCollection.getBuyCurrent().size();

		case BOUGHT:

			return bookCollection.getBuyBought().size();

		}
		return (0);

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
			temp = bookCollection.getBuyAll().get(paramInt);
			break;
		case CURRENT:

			temp = bookCollection.getBuyCurrent().get(paramInt);
			break;

		case BOUGHT:

			temp = bookCollection.getBuyBought().get(paramInt);
			break;

		}

		final BookData book = temp;
		paramView = mInflater.inflate(R.layout.book_list_item_search, null);
		localViewHolder = new ViewHolderStarPlaces();

		// CODICE COMUNE //

		// if(!book.isBought){
		// CODICE DEI CURRENT //

		// else{
		// CODICE DEI BOUGHT //

		paramView.setTag(localViewHolder);

		return paramView;
	}

	static class ViewHolderStarPlaces {

	}
}
