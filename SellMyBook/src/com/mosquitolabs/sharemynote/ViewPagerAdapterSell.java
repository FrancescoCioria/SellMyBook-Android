package com.mosquitolabs.sharemynote;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

public class ViewPagerAdapterSell extends PagerAdapter {

	private static final int ALL = 0;
	private static final int SELLING = 1;
	private static final int CURRENT = 2;
	private static final int SOLD = 3;

	private ListView listAll;
	private ListView listSelling;
	private ListView listCurrent;
	private ListView listSold;

	private MyCustomAdapterSell adapterSellAll;
	private MyCustomAdapterSell adapterSellSelling;
	private MyCustomAdapterSell adapterSellCurrent;
	private MyCustomAdapterSell adapterSellSold;

	private final MainActivity context;
	private View v;

	public ViewPagerAdapterSell(MainActivity context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return (4);
	}

	@Override
	public Object instantiateItem(View pager, int position) {
		LayoutInflater inflater = (LayoutInflater) pager.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		v = null;

		switch (position) {

		case ALL:
			v = inflater.inflate(R.layout.sell_all, null);
			listAll = (ListView) v.findViewById(R.id.listView);
			adapterSellAll = new MyCustomAdapterSell(context, ALL);
			listAll.setAdapter(adapterSellAll);
			break;

		case SELLING:
			v = inflater.inflate(R.layout.sell_selling, null);
			listSelling = (ListView) v.findViewById(R.id.listView);
			adapterSellSelling = new MyCustomAdapterSell(context, SELLING);
			listSelling.setAdapter(adapterSellSelling);

			break;
		case CURRENT:
			v = inflater.inflate(R.layout.sell_current, null);
			listCurrent = (ListView) v.findViewById(R.id.listView);
			adapterSellCurrent = new MyCustomAdapterSell(context, CURRENT);
			listCurrent.setAdapter(adapterSellCurrent);
		case SOLD:
			v = inflater.inflate(R.layout.sell_sold, null);
			listSold = (ListView) v.findViewById(R.id.listView);
			adapterSellSold = new MyCustomAdapterSell(context, SOLD);
			listSold.setAdapter(adapterSellSold);

			break;

		}
		((ViewPager) pager).addView(v, 0);
		return v;
	}

	@Override
	public void destroyItem(View pager, int position, Object view) {
		((ViewPager) pager).removeView((View) view);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

}