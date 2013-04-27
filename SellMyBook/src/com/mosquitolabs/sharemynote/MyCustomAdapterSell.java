package com.mosquitolabs.sharemynote;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MyCustomAdapterSell extends BaseAdapter {
	private LayoutInflater mInflater;
	private BookCollection bookCollection = BookCollection.getInstance();
	private int currentList;

	private static final int ALL = 0;
	private static final int SELLING = 1;
	private static final int CURRENT = 2;
	private static final int SOLD = 3;

	private int size;
	private Context context;

	private static String[] SPINNER_CURRENT = { "Profilo compratore",
			"Rimetti in vendita" };
	private static String[] SPINNER_SELLING = { "Modifica", "Rimuovi" };
	private static String[] SPINNER_SOLD = { "Profilo compratore",
			"Vendine un altro" };

	public MyCustomAdapterSell(Context paramContext, int currentList) {
		this.mInflater = LayoutInflater.from(paramContext);
		this.currentList = currentList;
		context = paramContext;

	}

	public int getCount() {
		switch (currentList) {
		case ALL:

			size = bookCollection.getSellAll().size();

			break;
		default:

			size = bookCollection.getSellSizeForSellingState(currentList);
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
		int i = 0;
		switch (currentList) {
		case ALL:
			temp = bookCollection.getSellAll().get(paramInt);
			break;

		default:
			temp = bookCollection.getNextBookForSellingState(currentList,
					paramInt);

			break;

		}

		final BookData book = temp;

		paramView = mInflater.inflate(R.layout.book_list_item_sell, null);
		localViewHolder = new ViewHolderStarPlaces();

		localViewHolder.topMargin = (RelativeLayout) paramView
				.findViewById(R.id.book_list_item_TopMargin);
		localViewHolder.topTitleAll = (TextView) paramView
				.findViewById(R.id.book_list_item_TopTitleAll);
		localViewHolder.bottomMargin = (RelativeLayout) paramView
				.findViewById(R.id.book_list_item_BottomMargin);
		localViewHolder.buttonAccept = (Button) paramView
				.findViewById(R.id.buttonAccept);
		localViewHolder.buttonDeny = (Button) paramView
				.findViewById(R.id.buttonDeny);
		localViewHolder.buttonGreen = (Button) paramView
				.findViewById(R.id.buttonGreen);
		localViewHolder.buttonRed = (Button) paramView
				.findViewById(R.id.buttonRed);
		localViewHolder.buttonBlue = (Button) paramView
				.findViewById(R.id.buttonBlue);
		localViewHolder.sold = (Button) paramView.findViewById(R.id.buttonSold);
		localViewHolder.buttonSetAsSold = (Button) paramView
				.findViewById(R.id.buttonSetAsSold);
		localViewHolder.buttons = (LinearLayout) paramView
				.findViewById(R.id.buttons);
		localViewHolder.request = (LinearLayout) paramView
				.findViewById(R.id.request);
		localViewHolder.overflow = (Spinner) paramView
				.findViewById(R.id.spinnerOverflow);

		// CODICE COMUNE //

		ArrayAdapter<String> dataAdapter;
		localViewHolder.sold.setVisibility(View.GONE);
		localViewHolder.buttonSetAsSold.setVisibility(View.GONE);
		localViewHolder.topTitleAll.setVisibility(View.GONE);
		
		if(currentList==ALL){
			if(paramInt==0 || bookCollection.getSellAll().get(paramInt-1).sellingState!=bookCollection.getSellAll().get(paramInt).sellingState){
				localViewHolder.topTitleAll.setVisibility(View.VISIBLE);

			}
		}
		
		switch (book.sellingState) {

		case SELLING:

			dataAdapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_spinner_item, SPINNER_SELLING);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			localViewHolder.overflow.setAdapter(dataAdapter);
			localViewHolder.overflow
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						public void onItemSelected(AdapterView<?> arg0, View v,
								int position, long id) {
							// t(position);
						}

						public void onNothingSelected(AdapterView<?> arg0) {

						}

					});
			
			localViewHolder.topTitleAll.setText("Libri in vendita");


			break;

		case CURRENT:

			dataAdapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_spinner_item, SPINNER_CURRENT);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			localViewHolder.overflow.setAdapter(dataAdapter);
			localViewHolder.overflow
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						public void onItemSelected(AdapterView<?> arg0, View v,
								int position, long id) {
							// t(position);
						}

						public void onNothingSelected(AdapterView<?> arg0) {

						}

					});

			localViewHolder.buttonSetAsSold.setVisibility(View.VISIBLE);
			localViewHolder.topTitleAll.setText("Transazioni in corso");

			
			break;
		case SOLD:

			dataAdapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_spinner_item, SPINNER_SOLD);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			localViewHolder.overflow.setAdapter(dataAdapter);
			localViewHolder.overflow
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						public void onItemSelected(AdapterView<?> arg0, View v,
								int position, long id) {
							// t(position);
						}

						public void onNothingSelected(AdapterView<?> arg0) {

						}

					});

			localViewHolder.sold.setVisibility(View.VISIBLE);
			localViewHolder.topTitleAll.setText("Libri venduti");

			break;
		}
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
		TextView topTitleAll;
		TextView title;
		TextView author;
		RelativeLayout topMargin;
		RelativeLayout bottomMargin;
		LinearLayout buttons;
		LinearLayout request;
		Spinner overflow;
		Button buttonGreen;
		Button buttonRed;
		Button buttonBlue;
		Button buttonAccept;
		Button buttonDeny;
		Button sold;
		Button buttonSetAsSold;

	}

	private void t(int position) {

		Toast.makeText(context, Integer.toString(position), Toast.LENGTH_SHORT)
				.show();

	}
}
