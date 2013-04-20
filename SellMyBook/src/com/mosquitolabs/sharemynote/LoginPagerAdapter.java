package com.mosquitolabs.sharemynote;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.Session;

public class LoginPagerAdapter extends PagerAdapter {

	private static final int LOGIN_PAGE = 0;
	private static final int INTRODUCTION_PAGE = 1;
	private static final int UNIVERSITY_PAGE = 2;
	private static final int CITY_PAGE = 3;
	private static final int FINAL_PAGE = 4;

	private Session session;

	private Button buttonLogin;
	private Button buttonContinue;

	private final MainActivity context;
	private View v;

	public LoginPagerAdapter(MainActivity context) {
		this.context = context;
		// session = Session.getActiveSession();
	}

	@Override
	public int getCount() {
		return (5);
	}

	@Override
	public Object instantiateItem(View pager, int position) {
		LayoutInflater inflater = (LayoutInflater) pager.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		v = null;

		switch (position) {

		case LOGIN_PAGE:
			v = inflater.inflate(R.layout.welcome, null);
			buttonLogin = (Button) v.findViewById(R.id.buttonLogin);
			buttonLogin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					context.loginButtonClick();

				}

			});

			break;

		case INTRODUCTION_PAGE:
			v = inflater.inflate(R.layout.login_introduction, null);
			buttonContinue = (Button) v
					.findViewById(R.id.login_introduction_buttonContinue);
			buttonContinue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					context.loginPagerNextPage();
				}
			});
			break;
		case UNIVERSITY_PAGE:
			v = inflater.inflate(R.layout.login_university, null);
			buttonContinue = (Button) v
					.findViewById(R.id.login_university_buttonContinue);
			buttonContinue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					context.loginPagerNextPage();
				}
			});

			break;
		case CITY_PAGE:
			v = inflater.inflate(R.layout.login_city, null);
			buttonContinue = (Button) v
					.findViewById(R.id.login_city_buttonContinue);
			buttonContinue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					context.loginPagerNextPage();
				}
			});
			break;
		case FINAL_PAGE:
			v = inflater.inflate(R.layout.login_final, null);
			buttonContinue = (Button) v
					.findViewById(R.id.login_final_buttonContinue);
			buttonContinue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					context.initialize();
				}
			});
			break;

		}
		((CustomViewPager) pager).addView(v, 0);
		return v;
	}

	@Override
	public void destroyItem(View pager, int position, Object view) {
		((CustomViewPager) pager).removeView((View) view);
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
