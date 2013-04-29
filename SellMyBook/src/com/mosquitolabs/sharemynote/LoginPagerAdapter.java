package com.mosquitolabs.sharemynote;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.audiofx.AutomaticGainControl;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;

import com.facebook.Session;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class LoginPagerAdapter extends PagerAdapter {

	private static final int LOGIN_PAGE = 0;
	private static final int INTRODUCTION_PAGE = 1;
	private static final int UNIVERSITY_PAGE = 2;
	private static final int CITY_PAGE = 3;
	private static final int FINAL_PAGE = 4;

	private Session session;

	private Button buttonLogin;
	private Button buttonContinue;

	private InstantAutoComplete autoCompleteUni;
	private InstantAutoComplete autoCompleteCampus;

	private int selectionUni = -1;
	private int selectionCampus = -1;
	
	private String campusID;

	private final MainActivity context;
	private View v;

	private ParseAPICalls parseAPICalls = ParseAPICalls.getInstance();

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
			final ArrayList<String> universityID = new ArrayList<String>();
			final ArrayList<String> university = new ArrayList<String>();

			final ArrayList<String> campus = new ArrayList<String>();
			final ArrayList<String> campusID = new ArrayList<String>();
			
			final ArrayAdapter<String> adapterUni = new ArrayAdapter<String>(context,
					R.layout.list_item_instant, university);
			adapterUni
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			final ArrayAdapter<String> adapterCampus = new ArrayAdapter<String>(
					context, R.layout.list_item_instant, campus);
			adapterCampus
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			autoCompleteUni = (InstantAutoComplete) v
					.findViewById(R.id.autoCompleteUni);
			autoCompleteCampus = (InstantAutoComplete) v
					.findViewById(R.id.autoCompleteCampus);
			
			buttonContinue = (Button) v
					.findViewById(R.id.login_university_buttonContinue);
			buttonContinue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (selectionCampus>=0&&selectionUni>=0) {
						LoginPagerAdapter.this.campusID=campusID.get(selectionCampus);
						context.loginPagerNextPage();
					}else{
						AlertDialog.Builder dialog = new AlertDialog.Builder(context);
						dialog.setMessage("Per continuare devi prima selezionare una universitˆ e un campus, grazie.");
						dialog.setTitle("Mmmmm...");
						dialog.setPositiveButton("Ok", null);
						dialog.create().show();
					}
						
				}
			});
			

			List<ParseObject> resultUni = parseAPICalls.getUniversity();

			autoCompleteCampus.setAdapter(adapterCampus);

			for (ParseObject temp : resultUni) {
				university.add(temp.getString("name"));
				universityID.add(temp.getObjectId());
			}

			
			autoCompleteUni.setAdapter(adapterUni);

			autoCompleteUni.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					autoCompleteUni.showDropDown();
				}
			});
			autoCompleteCampus.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					autoCompleteCampus.showDropDown();
				}
			});

			autoCompleteUni.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {

					if (selectionUni != position) {
						selectionUni = position;
						List<ParseObject> resultCampus = parseAPICalls
								.getCampus(universityID.get(position));
						campus.clear();
						for (ParseObject temp : resultCampus) {
							campus.add(temp.getString("name"));
							campusID.add(temp.getObjectId());
						}
						adapterCampus.notifyDataSetChanged();
						selectionCampus = -1;
						
					}
					InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(arg1.getWindowToken(), 0);

				}

			});
			autoCompleteCampus
					.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {

							if (selectionCampus != position) {
								selectionCampus = position;
							}
							InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(arg1.getWindowToken(), 0);
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
					saveUserDatasInParse();
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

	public void popupMenu(View anchor, final BookData book, String[] items) {

		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView = layoutInflater.inflate(R.layout.popup, null);
		final PopupWindow popupWindow = new PopupWindow(popupView);

		final int NUM_OF_VISIBLE_LIST_ROWS = items.length;
		ListView list = (ListView) popupView.findViewById(R.id.listView);
		list.setAdapter(new MyCustomAdapter(context, items));
		list.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		popupWindow.setWidth(list.getMeasuredWidth() + list.getMeasuredWidth()
				/ 5);
		popupWindow.setHeight((list.getMeasuredHeight() + 2)
				* NUM_OF_VISIBLE_LIST_ROWS);

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				popupWindow.dismiss();

			}
		});

		Drawable background = context.getResources().getDrawable(
				android.R.color.transparent);

		popupWindow.setBackgroundDrawable(background);

		popupWindow.setOutsideTouchable(true);
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);

		popupWindow.showAsDropDown(anchor);

	}
	
	
	private void saveUserDatasInParse(){
		ParseUser.getCurrentUser().put("campus", ParseObject.createWithoutData("Campus", campusID));
		
		ParseUser.getCurrentUser().saveInBackground();
	}
	

}
