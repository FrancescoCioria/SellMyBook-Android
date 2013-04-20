package com.mosquitolabs.sharemynote;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.devspark.sidenavigation.SideNavigationView;
import com.facebook.Session;
import com.facebook.SessionState;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.slidingmenu.lib.SlidingMenu;

public class MainActivity extends SherlockActivity {

	private int logState = 0;
	private int bookPicture = 0;

	private int currentLoginTab = 0;

	private ArrayList<SideNavigationItem> menuItems;

	private static final String LOG_TAG = SideNavigationAdapter.class
			.getSimpleName();

	private final static int LOGGED_OUT = 0;
	private final static int LOGGED_IN = 1;
	private final static int LEFT = 0;
	private final static int CENTER = 1;
	private final static int RIGHT = 2;

	private static final String[] TITLES_BUY = { "Tutti",
			"Transazioni in corso", "Libri comprati" };

	private ListView listViewSideMenu;
	private ListView listViewRight;

	private EditText editSearch;
	private EditText editTitle;
	private EditText editAuthor;
	private EditText editYear;

	private RelativeLayout welcome;

	SideNavigationView rightMenu;

	private SharedPreferences mPrefs;
	private Session session;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	private static final List<String> READ_PERMISSIONS = Arrays.asList(
			"user_likes", "user_events", "user_birthday");

	ArrayList<BookData> bookList = new ArrayList<BookData>();

	private SlidingMenu slidingMenu;

	private CustomViewPager loginPager;
	private ViewPager mainPager;

	private LinearLayout triangle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setTheme(com.actionbarsherlock.R.style.Sherlock___Theme_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		getSupportActionBar().hide();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		parseXml(R.menu.menu_sidebar);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		// loginPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
		session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, statusCallback,
						savedInstanceState);
			}
			if (session == null) {
				session = new Session(this);
			}

			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				session.openForRead(new Session.OpenRequest(this)
						.setCallback(statusCallback));
			}

		}
		if (!session.isOpened()) {
			setContentView(R.layout.login);
			loginPager = (CustomViewPager) findViewById(R.id.viewpager);
			loginPager.setAdapter(new LoginPagerAdapter(this));
			loginPager.setPagingEnabled(false);
		} else {
			initialize();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);

		if (scanResult != null) {
			String barcode;
			String typ;

			barcode = scanResult.getContents();
			typ = scanResult.getFormatName();
			editSearch.setText(barcode);

		} else {

			if (Session.getActiveSession().isOpened()) {
				initialize();
			} else {
				// loginVisible(true);
				// progressLoginVisible(false);
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_actionbar, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			slidingMenu.toggle();
			break;

		case R.id.menu_search:

			contactServer();

			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	public int getLogState() {
		return logState;
	}

	public void initialize() {
		setContentView(R.layout.main);

		slidingMenu = (SlidingMenu) findViewById(R.id.slidingmenulayout);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setShadowWidth(10);
		slidingMenu.setShadowDrawable(R.drawable.side_navigation_shadow_right);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		slidingMenu.setBehindOffset(width / 7);
		slidingMenu.setFadeDegree(0.75f);
		slidingMenu.setMenu(R.layout.side_navigation_left);
		slidingMenu.setActivated(true);
		slidingMenu.setContent(R.layout.buy);

		if (loginPager != null) {
			loginPager.invalidate();
		}

		getSupportActionBar().show();
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.rgb(0, 110, 170)));

		final ListView list = (ListView) slidingMenu.getMenu().findViewById(
				R.id.side_navigation_listview);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				switch (list.getChildAt(position).getId()) {

				}
			}
		});

		// initializeSideView();
		// initializeMain();
		cleanDirectory();
	}

	public void initializeMain(View v) {
		// listView = (ListView) v.findViewById(R.id.listView1);

	}

	private synchronized void contactServer() {
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			public String doInBackground(Void... params) {

				try {
					URL url = new URL(
							"http://peaceful-wave-7032.herokuapp.com/api/v1/book/");
					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();
					urlConnection.setRequestMethod("GET");
					urlConnection.connect();
					// gets the server json data
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(
									urlConnection.getInputStream()));
					JSONObject json = new JSONObject(bufferedReader.readLine());
					JSONArray jArray = json.getJSONArray("objects");
					for (int i = 0; i < jArray.length(); i++) {
						json = jArray.getJSONObject(i);
						BookData book = new BookData();
						book.author = json.getString("author");
						book.title = json.getString("title");
						book.isbn = json.getString("isbn13");
						book.originalPrice = json.getString("original_price");
						book.ID = json.getString("id");
						book.year = json.getString("year");
						book.state = json.getInt("state");
						bookList.add(book);
					}

					SSLContext ctx = SSLContext.getInstance("TLS");
					ctx.init(null, new TrustManager[] { new X509TrustManager() {
						public void checkClientTrusted(X509Certificate[] chain,
								String authType) {
						}

						public void checkServerTrusted(X509Certificate[] chain,
								String authType) {
						}

						public X509Certificate[] getAcceptedIssuers() {
							return new X509Certificate[] {};
						}
					} }, null);
					HttpsURLConnection.setDefaultSSLSocketFactory(ctx
							.getSocketFactory());

					HttpsURLConnection
							.setDefaultHostnameVerifier(new HostnameVerifier() {
								public boolean verify(String hostname,
										SSLSession session) {
									return true;
								}
							});

					for (BookData book : bookList) {

						HttpResponse response = null;
						HttpResponse response2 = null;
						try {
							HttpClient client = new DefaultHttpClient();
							HttpGet request = new HttpGet();
							request.setURI(new URI(
									"https://www.googleapis.com/books/v1/volumes?q=isbn:"
											+ book.isbn));
							response = client.execute(request);
						} catch (Exception e) {
							Log.e("id", e.toString());
						}

						JSONObject j = new JSONObject(
								convertStreamToString(response.getEntity()
										.getContent()));
						JSONArray jsonArray = j.getJSONArray("items");
						j = jsonArray.getJSONObject(0);

						try {
							HttpClient client = new DefaultHttpClient();
							HttpGet request = new HttpGet();
							String id = j.getString("id");
							request.setURI(new URI(
									"https://www.googleapis.com/books/v1/volumes/"
											+ id));
							response2 = client.execute(request);
						} catch (Exception e) {
							Log.e("id", e.toString());
						}

						j = new JSONObject(convertStreamToString(response2
								.getEntity().getContent()));
						JSONObject jj = j.getJSONObject("volumeInfo");
						if (!jj.isNull("description")) {
							book.desc = jj.getString("description");
						}
						if (!jj.isNull("publisher")) {
							book.publisher = jj.getString("publisher");
						}
						if (!jj.isNull("imageLinks")) {
							JSONObject imagelinks = jj
									.getJSONObject("imageLinks");
							book.imageUri = imagelinks.getString("thumbnail");
						}

					}

					MainActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							ListViewRightAdapter arrayAdapter = new ListViewRightAdapter(
									MainActivity.this);
							listViewRight.setAdapter(arrayAdapter);

							bookPicture = 0;
							bookPicture();
						}

					});

				} catch (Exception e) {
					Log.e("server", e.toString());
				}

				return null;
			}

		};
		task.execute();

	}

	private synchronized void bookPicture() {
		AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {

			@Override
			public Bitmap doInBackground(Void... params) {
				BookData book = bookList.get(bookPicture);
				Bitmap picture = null;
				if (book.imageUri.length() > 0) {
					try {

						URL img_value = new URL(book.imageUri);
						picture = BitmapFactory.decodeStream(img_value
								.openConnection().getInputStream());
						// saveImageToDisk(book.isbn, picture);
					} catch (Exception e) {
						Log.e("bookPicture", e.toString());
					}
					final Bitmap pic = picture;
					MainActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							{
								if (listViewRight.getFirstVisiblePosition() <= bookPicture
										&& bookPicture <= listViewRight
												.getLastVisiblePosition()) {
									View v = listViewRight.getChildAt(bookPicture
											- listViewRight
													.getFirstVisiblePosition());
									ImageView image = (ImageView) v
											.findViewById(R.id.book_list_item_icon);
									if (pic != null) {
										image.setImageBitmap(pic);
									}
								}
							}
							bookPicture++;
							if (bookPicture < bookList.size()) {
								bookPicture();
							}
						}
					});

				} else {
					bookPicture++;
					if (bookPicture < bookList.size()) {
						bookPicture();
					}
				}
				return null;
			}

		};
		task.execute();
	}

	private void toast(final String msg) {
		MainActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				Toast toast = Toast.makeText(MainActivity.this, msg,
						Toast.LENGTH_SHORT);
				toast.show();
			}

		});
	}

	private void parseXml(int menu) {
		menuItems = new ArrayList<SideNavigationItem>();
		try {
			XmlResourceParser xrp = getResources().getXml(menu);
			xrp.next();
			int eventType = xrp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					String elemName = xrp.getName();
					if (elemName.equals("item")) {
						String textId = xrp.getAttributeValue(
								"http://schemas.android.com/apk/res/android",
								"title");
						String iconId = xrp.getAttributeValue(
								"http://schemas.android.com/apk/res/android",
								"icon");
						String resId = xrp.getAttributeValue(
								"http://schemas.android.com/apk/res/android",
								"id");
						SideNavigationItem item = new SideNavigationItem();
						item.setId(Integer.valueOf(resId.replace("@", "")));
						item.setText(resourceIdToString(textId));
						if (iconId != null) {
							try {
								item.setIcon(Integer.valueOf(iconId.replace(
										"@", "")));
							} catch (NumberFormatException e) {
								Log.d(LOG_TAG, "Item " + item.getId()
										+ " not have icon");
							}
						}
						if ((item.getText().toString().equals("Logout") && getLogState() == LOGGED_OUT)
								|| (item.getText().toString().equals("Login") && getLogState() == LOGGED_IN)) {
						} else {
							menuItems.add(item);
						}

					}
				}
				eventType = xrp.next();
			}
		} catch (Exception e) {
			Log.w(LOG_TAG, e);
		}
	}

	private String resourceIdToString(String resId) {
		if (!resId.contains("@")) {
			return resId;
		} else {
			String id = resId.replace("@", "");
			return getResources().getString(Integer.valueOf(id));
		}
	}

	public static String convertStreamToString(InputStream inputStream)
			throws IOException {
		if (inputStream != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(
						inputStream, "UTF-8"), 1024);
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				inputStream.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	private void visibility(final View v, boolean b) {
		if (b) {
			MainActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					v.setVisibility(View.VISIBLE);
					v.setVisibility(View.GONE);
				}

			});
		} else {
			MainActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					v.setVisibility(View.GONE);
				}

			});
		}
	}

	private void saveImageToDisk(String ID, Bitmap image) {

		try {
			String path = new String(ID);
			java.io.FileOutputStream out = this.openFileOutput(path,
					Context.MODE_PRIVATE);
			image.compress(Bitmap.CompressFormat.PNG, 90, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void cleanDirectory() {
		ArrayList<String> paths = new ArrayList<String>();
		File directory = this.getFilesDir();
		try {
			File[] files = directory.listFiles();

			for (int i = 0; i < files.length; ++i) {
				paths.add(files[i].getName());
			}

			for (String file : paths) {
				File f = new File(directory.getAbsolutePath() + "/" + file);
				f.delete();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session s, SessionState state, Exception exception) {

			if (session.isClosed()) {
				session = s.getActiveSession();
			}

		}
	}

	public void loginButtonClick() {
		// loginVisible(false);
		// rogressLoginVisible(true);
		Session session = Session.getActiveSession();
		if ((!session.isOpened() && !session.isClosed())) {
			session.openForRead(new Session.OpenRequest(MainActivity.this)
					.setCallback(statusCallback));
			// .setPermissions(READ_PERMISSIONS));
		} else {
			Session.openActiveSession(MainActivity.this, true, statusCallback);
			loginPagerNextPage();
		}
	}

	public Session getSession() {
		return session;
	}

	public void loginPagerNextPage() {
		currentLoginTab++;
		loginPager.setCurrentItem(currentLoginTab);
	}

	public void refreshActionBar(int tab) {
		switch (tab) {
		case LEFT:
			getSupportActionBar().setTitle("Acquisti");
			break;

		case CENTER:
			getSupportActionBar().setTitle("Libri osservati");

			break;

		case RIGHT:
			getSupportActionBar().setTitle("Vendite");
			break;
		}
	}

	public void initializeRight(View v) {

		listViewRight = (ListView) v
				.findViewById(R.id.activity_main_right_listView);

		listViewRight.setFooterDividersEnabled(false);
		listViewRight.setDivider(null);
		listViewRight.setDividerHeight(0);
	}

	public ArrayList<BookData> getBookList() {
		return bookList;
	}

}
