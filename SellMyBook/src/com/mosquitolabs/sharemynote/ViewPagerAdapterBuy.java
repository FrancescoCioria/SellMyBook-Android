package com.mosquitolabs.sharemynote;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

public class ViewPagerAdapterBuy extends PagerAdapter {

	private static final int ALL = 0;
	private static final int CURRENT = 1;
	private static final int BOUGHT = 2;

	private ListView listAll;
	private ListView listCurrent;
	private ListView listBought;

	private MyCustomAdapterBuy adapterBuyAll;
	private MyCustomAdapterBuy adapterBuyCurrent;
	private MyCustomAdapterBuy adapterBuyBought;

	private final MainActivity context;
	private View v;

	public ViewPagerAdapterBuy(MainActivity context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return (3);
	}

	@Override
	public Object instantiateItem(View pager, int position) {
		LayoutInflater inflater = (LayoutInflater) pager.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		v = null;

		switch (position) {

		case ALL:
			v = inflater.inflate(R.layout.buy_all, null);
			listAll = (ListView) v.findViewById(R.id.listView);
			adapterBuyAll = new MyCustomAdapterBuy(context, ALL);
			listAll.setAdapter(adapterBuyAll);
			break;

		case CURRENT:
			v = inflater.inflate(R.layout.buy_current, null);
			listCurrent = (ListView) v.findViewById(R.id.listView);
			adapterBuyCurrent = new MyCustomAdapterBuy(context, CURRENT);
			listCurrent.setAdapter(adapterBuyCurrent);

			break;
		case BOUGHT:
			v = inflater.inflate(R.layout.buy_bought, null);
			listBought = (ListView) v.findViewById(R.id.listView);
			adapterBuyBought = new MyCustomAdapterBuy(context, BOUGHT);
			listBought.setAdapter(adapterBuyBought);

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
