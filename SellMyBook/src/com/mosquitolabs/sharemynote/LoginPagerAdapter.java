package com.mosquitolabs.sharemynote;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.facebook.Session;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LoginPagerAdapter extends PagerAdapter {

	private static final int LOGIN_PAGE = 0;
	private static final int INTRODUCTION_PAGE = 1;
	private static final int UNIVERSITY_PAGE = 4;
	private static final int CITY_PAGE = 3;
	private static final int FINAL_PAGE = 2;

	private Session session;

	private Button buttonLogin;
	private Button buttonContinue;

	private InstantAutoComplete autoCompleteUni;
	private InstantAutoComplete autoCompleteCampus;

	private int selectionUni = -1;
	private int selectionCampus = -1;
	private int selectionCity = 0;

	private String campusID;
	List<ParseObject> resultUni = new ArrayList<ParseObject>();

	private final MainActivity context;
	private View v;

	boolean uni = true;

	private RelativeLayout progressLogin;

	private ArrayAdapter<String> adapterCampus;
	private ArrayAdapter<String> adapterUni;

	private ParseAPICalls parseAPICalls = ParseAPICalls.getInstance();

	public LoginPagerAdapter(MainActivity context) {
		this.context = context;
		// session = Session.getActiveSession();
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

		case LOGIN_PAGE:
			v = inflater.inflate(R.layout.welcome, null);
			progressLogin = (RelativeLayout) v.findViewById(R.id.progressLogin);
			buttonLogin = (Button) v.findViewById(R.id.buttonLogin);
			buttonLogin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					progressLogin.setVisibility(View.VISIBLE);
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

			// /

			// //

			break;
		case UNIVERSITY_PAGE:
			v = inflater.inflate(R.layout.login_university, null);

			buttonContinue = (Button) v
					.findViewById(R.id.login_university_buttonContinue);
			buttonContinue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

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
			final ArrayList<String> universityID = new ArrayList<String>();
			final ArrayList<String> university = new ArrayList<String>();

			final ArrayList<String> campus = new ArrayList<String>();
			final ArrayList<String> campusID = new ArrayList<String>();

			adapterUni = new ArrayAdapter<String>(context,
					R.layout.list_item_instant, university);

			adapterCampus = new ArrayAdapter<String>(context,
					R.layout.list_item_instant, campus);

			buttonContinue = (Button) v
					.findViewById(R.id.login_final_buttonContinue);

			final Button buttonUni = (Button) v
					.findViewById(R.id.login_final_buttonUni);

			buttonContinue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (selectionCampus >= 0 && selectionUni >= 0
							&& selectionCity >= 0) {
						saveUserDatasInParse();
						context.initialize();
					} else {
						AlertDialog.Builder dialog = new AlertDialog.Builder(
								context);
						dialog.setMessage("Per continuare devi prima selezionare l'universitˆ (e campus) in cui studi e la cittˆ o paese in cui abiti...\nGrazie.");
						dialog.setTitle("Mmmmm...");
						dialog.setPositiveButton("Ok", null);
						dialog.create().show();
					}

				}
			});

			// UNI //

			buttonUni.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					uni = true;
					adapterUni = new ArrayAdapter<String>(context,
							R.layout.list_item_instant, university);
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.dialog_university);
					dialog.setTitle("Seleziona la tua universitˆ");
					final EditText filter = (EditText) dialog
							.findViewById(R.id.editTextFilter);
					final ListView list = (ListView) dialog
							.findViewById(R.id.listView);

					final RelativeLayout progress = (RelativeLayout) dialog
							.findViewById(R.id.progress);
					progress.setVisibility(View.VISIBLE);

					list.setAdapter(adapterUni);

					filter.addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence cs, int arg1,
								int arg2, int arg3) {
							// When user changed the Text
							if (uni) {
								adapterUni.getFilter().filter(cs);
							} else {
								adapterCampus.getFilter().filter(cs);
							}
						}

						@Override
						public void beforeTextChanged(CharSequence arg0,
								int arg1, int arg2, int arg3) {
						}

						@Override
						public void afterTextChanged(Editable arg0) {
						}
					});

					list.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								final int paramInt, long arg3) {
							if (uni) {
								uni = false;
								progress.setVisibility(View.VISIBLE);

								String name = adapterUni.getItem(paramInt);
								int i = 0;
								for (String temp : university) {
									if (name.equals(temp)) {
										break;
									} else {
										i++;
									}
								}

								final int position = i;

								ParseQuery query = new ParseQuery("Campus");

								query.whereEqualTo("university", ParseObject
										.createWithoutData("University",
												universityID.get(position)));

								query.findInBackground(new FindCallback() {

									@Override
									public void done(
											List<ParseObject> resultCampus,
											ParseException arg1) {
										campus.clear();
										for (ParseObject temp : resultCampus) {
											campus.add(temp.getString("name"));
											campusID.add(temp.getObjectId());
										}

										adapterCampus = new ArrayAdapter<String>(
												context,
												R.layout.list_item_instant,
												campus);
										list.setAdapter(adapterCampus);
										selectionUni = position;
										dialog.setTitle("Seleziona il tuo campus");
										filter.setText("");
										progress.setVisibility(View.GONE);
									}
								});

							} else {

								String name = adapterCampus.getItem(paramInt);
								int i = 0;
								for (String temp : campus) {
									if (name.equals(temp)) {
										break;
									} else {
										i++;
									}
								}

								final int position = i;

								selectionCampus = position;
								buttonUni.setText(university.get(selectionUni)
										+ " > " + campus.get(selectionCampus));
								LoginPagerAdapter.this.campusID = campusID
										.get(selectionCampus);
								dialog.dismiss();
							}

						}

					});

					dialog.show();

					ParseQuery query = new ParseQuery("University");

					query.findInBackground(new FindCallback() {
						@Override
						public void done(List<ParseObject> arg0,
								ParseException arg1) {
							if (resultUni.isEmpty()) {
								resultUni = arg0;
								for (ParseObject temp : resultUni) {
									university.add(temp.getString("name"));
									universityID.add(temp.getObjectId());
								}
							}
							adapterUni.notifyDataSetChanged();
							for (int i = 0; i < university.size(); i++) {
								// list.getChildAt(i).getId();
							}

							progress.setVisibility(View.GONE);
						}
					});

				}
			});

			// / CITY ////

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

	private void saveUserDatasInParse() {
		ParseUser.getCurrentUser().put("campus",
				ParseObject.createWithoutData("Campus", campusID));
		ParseUser.getCurrentUser().saveInBackground();
	}

}
