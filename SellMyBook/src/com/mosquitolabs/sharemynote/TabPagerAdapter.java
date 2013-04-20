package com.mosquitolabs.sharemynote;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.viewpagerindicator.TitlePageIndicator;

public class TabPagerAdapter extends PagerAdapter {

	private String[] titles = new String[] { "Acquisti", "Libri osservati",
			"Vendite" };

	private static final int LEFT = 0;
	private static final int CENTER = 1;
	private static final int RIGHT = 2;
	
	private ListView listViewRight;

	// private Button buttonLogin;

	private final MainActivity context;
	private View v;

	public TabPagerAdapter(MainActivity context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return (3);
	}

	public String getTitle(int position) {
		return titles[position];
	}

	@Override
	public Object instantiateItem(View pager, int position) {
		LayoutInflater inflater = (LayoutInflater) pager.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		v = null;

		switch (position) {

		case LEFT:
			v = inflater.inflate(R.layout.activity_main_left, null);
			break;

		case CENTER:
			v = inflater.inflate(R.layout.activity_main_left, null);

			break;

		case RIGHT:
			v = inflater.inflate(R.layout.activity_main_right, null);
			context.initializeRight(v);
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
