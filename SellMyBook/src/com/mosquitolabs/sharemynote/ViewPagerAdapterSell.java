package com.mosquitolabs.sharemynote;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ViewPagerAdapterSell extends PagerAdapter {

	private static final int ALL = 0;
	private static final int SELLING = 2;
	private static final int CURRENT = 1;
	private static final int SOLD = 3;

	private ListView listAll;
	private ListView listSelling;
	private ListView listCurrent;
	private ListView listSold;

	private int counter = 0;

	private BookCollection bookCollection = BookCollection.getInstance();

	private MyCustomAdapterSell adapterSellAll;
	private MyCustomAdapterSell adapterSellSelling;
	private MyCustomAdapterSell adapterSellCurrent;
	private MyCustomAdapterSell adapterSellSold;

	private final MainActivity context;
	private View v;
	private View vAll = null;
	private View vSelling = null;
	private View vCurrent = null;
	private View vSold = null;
	

	public ViewPagerAdapterSell(MainActivity context) {
		this.context = context;
		adapterSellAll = new MyCustomAdapterSell(context, ALL);
		adapterSellSelling = new MyCustomAdapterSell(context, SELLING);
		adapterSellCurrent = new MyCustomAdapterSell(context, CURRENT);
		adapterSellSold = new MyCustomAdapterSell(context, SOLD);

	}

	@Override
	public int getCount() {
		return (4);
	}

	@Override
	public Object instantiateItem(View pager, int position) {
		LayoutInflater inflater = (LayoutInflater) pager.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.i("ViewPagerAdapter", "position: " + Integer.toString(position)
				+ ", time: " + context.getCounter());

		v = null;

		switch (position) {

		case ALL:
			counter=0;
			v = inflater.inflate(R.layout.sell_all, null);
			listAll = (ListView) v.findViewById(R.id.listView);
			listAll.setAdapter(adapterSellAll);
			

			break;

		case SELLING:
			counter=0;

			v = inflater.inflate(R.layout.sell_selling, null);
			listSelling = (ListView) v.findViewById(R.id.listView);
			listSelling.setAdapter(adapterSellSelling);

			break;
		case CURRENT:
			counter=0;

			v = inflater.inflate(R.layout.sell_current, null);
			listCurrent = (ListView) v.findViewById(R.id.listView);
			listCurrent.setAdapter(adapterSellCurrent);
			break;
		case SOLD:
			counter=0;

			v = inflater.inflate(R.layout.sell_sold, null);
			listSold = (ListView) v.findViewById(R.id.listView);
			listSold.setAdapter(adapterSellSold);

			break;

		}
		((ViewPager) pager).addView(v, 0);
		return v;
	}

	@Override
	public void destroyItem(View pager, int position, Object view) {
		Log.i("ViewPagerAdapter", "destroyItem");

		((ViewPager) pager).removeView((View) view);

	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	public void refreshAdapter(int adapter) {
		switch (adapter) {

		case ALL:
			adapterSellAll.notifyDataSetChanged();

		case SELLING:
			adapterSellSelling.notifyDataSetChanged();

		case CURRENT:
			adapterSellCurrent.notifyDataSetChanged();

		case SOLD:
			adapterSellSold.notifyDataSetChanged();

		}
	}

	public int getCounter(){
		return counter;
	}
	public void increaseCounter(){
		counter++;
	}
	
	
}