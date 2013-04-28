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

import android.R.anim;
import android.R.color;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TitlePageIndicator;

public class MainActivity extends BaseActivity {

	private int logState = 0;
	private int bookPicture = 0;

	private int currentLoginTab = 0;

	private final static int LOGGED_OUT = 0;
	private final static int LOGGED_IN = 1;

	private final static int BUY = 0;
	private final static int SELL = 1;
	private final static int WANTED = 2;
	private final static int SETTINGS = 3;
	private final static int LICENSE = 4;

	private static final int ALL = 0;
	private static final int SELLING = 2;
	private static final int CURRENT = 1;
	private static final int SOLD = 3;

	private final static int COLOR_BACKGROUND_GRAY = Color.rgb(238, 238, 238);
	private final static int COLOR_BLUE_MARCO = Color.rgb(0, 110, 170);
	private final static int COLOR_DARK_GRAY = Color.rgb(38, 38, 38);

	private static final String[] TITLES_BUY = { "Tutti",
			"Transazioni in corso", "Libri comprati" };

	private static final String[] TITLES_SELL = { "Tutti",
			"Transazioni in corso", "In vendita", "Libri venduti" };

	private ListView listViewSlidingMenu;
	private ListView listViewRight;

	private EditText editSearch;

	private ViewPagerAdapterSell pagerAdapterSell;

	private RelativeLayout welcome;

	private SharedPreferences mPrefs;
	private Session session;
	// private Session.StatusCallback statusCallback = new
	// SessionStatusCallback();
	private static final List<String> READ_PERMISSIONS = Arrays.asList(
			"user_likes", "user_events", "user_birthday");

	ArrayList<BookData> bookList = new ArrayList<BookData>();

	private SlidingMenu slidingMenu;

	private CustomViewPager loginPager;

	private ViewPager pagerBuy;
	private ViewPager pagerSell;

	private Button buttonBuy;
	private Button buttonSell;

	private TitlePageIndicator indicatorBuy;
	private TitlePageIndicator indicatorSell;

	private BookCollection bookCollection = BookCollection.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.setTheme(com.actionbarsherlock.R.style.Sherlock___Theme_DarkActionBar);
		super.onCreate(savedInstanceState);

		Parse.initialize(this, "ZI3LPjIiwQMdyNpBiRsl4vhLKUNXe196Hyw2NKH9",
				"pf1HuTyDqXUmqJ6EhigzKx1tAJ2jwmgGACC9w3nx");
		ParseAnalytics.trackAppOpened(getIntent());
		ParseFacebookUtils.initialize(getString(R.string.app_id));

		setContentView(R.layout.welcome);
		getSupportActionBar().hide();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		session = Session.getActiveSession();

		if (false && ParseFacebookUtils.getSession() == null) {
			initializeLogin();
		} else {
			initialize();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, data);

		if (scanResult != null) {
			String barcode;
			String typ;

			barcode = scanResult.getContents();
			typ = scanResult.getFormatName();
			editSearch.setText(barcode);

		} else {
			ParseFacebookUtils.finishAuthentication(requestCode, resultCode,
					data);

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

		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	public int getLogState() {
		return logState;
	}

	private void initializeLogin() {
		setContentView(R.layout.login);
		loginPager = (CustomViewPager) findViewById(R.id.viewpager);
		loginPager.setAdapter(new LoginPagerAdapter(this));
		loginPager.setPagingEnabled(false);
	}

	public void initialize() {
		// activate actionbar
		getSupportActionBar().show();
		Drawable background = getResources().getDrawable(R.drawable.untitled2);

		getSupportActionBar().setBackgroundDrawable(background);

		// invalidate login viewpager
		if (loginPager != null) {
			loginPager.invalidate();
		}

		// initialize slidingMenu
		slidingMenu = getSlidingMenu();
		listViewSlidingMenu = getSlidingMenuList();
		listViewSlidingMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				switch (position) {
				case BUY:
					initializeBuy();
					break;
				case WANTED:
					initializeWanted();
					break;
				case SELL:
					initializeSell();
					break;

				case SETTINGS:
					toast("settings");
					break;
				case LICENSE:
					toast("license");
					break;
				}

			}
		});

		// ParseObject testObject = new ParseObject("TestObject");
		// testObject.put("foo", "bar");
		// testObject.saveInBackground();

		cleanDirectory();

		BookData b1 = new BookData();
		BookData b2 = new BookData();
		BookData b3 = new BookData();
		BookData b4 = new BookData();
		b1.sellingState = SOLD;
		b2.ID = "2";
		b2.sellingState = SELLING;
		b3.ID = "3";
		b3.sellingState = CURRENT;
		b4.ID = "4";
		b4.sellingState = SOLD;
		bookCollection.addBookToListSell(b1);
		bookCollection.addBookToListSell(b2);
		bookCollection.addBookToListSell(b3);
		bookCollection.addBookToListSell(b4);

		bookCollection.initializeSellCurrent();
		bookCollection.initializeSellSold();
		bookCollection.initializeSellSelling();

		// go to second tab
		initializeSell();
	}

	public void initializeBuy() {
		slidingMenu.setActivated(true);
		setContentView(R.layout.buy);

		if (indicatorSell != null) {
			indicatorSell.invalidate();
		}
		RelativeLayout topBar = (RelativeLayout) findViewById(R.id.topBar);
		TextView desc = (TextView) findViewById(R.id.descriptionPage);
		int textcolor = getColor(R.color.side_navigation_outside_background);
		desc.setTextColor(textcolor);

		indicatorBuy = (TitlePageIndicator) findViewById(R.id.indicator);
		indicatorBuy.setTitles(TITLES_BUY);
		indicatorBuy.setBackgroundColor(COLOR_BACKGROUND_GRAY);
		indicatorBuy.setFooterColor(COLOR_BLUE_MARCO);
		indicatorBuy
				.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Triangle);
		indicatorBuy.setTextColor(textcolor);
		indicatorBuy.setSelectedColor(textcolor);
		indicatorBuy.setTextSize(17);

		indicatorBuy
				.setFooterColor(getColor(R.color.side_navigation_outside_background));
		indicatorBuy.setFooterLineHeight(0);

		pagerBuy = (ViewPager) findViewById(R.id.viewpager);
		pagerBuy.setAdapter(new ViewPagerAdapterBuy(this));
		indicatorBuy.setViewPager(pagerBuy);
		int background = getColor(R.color.background_gray_darker);

		topBar.setBackgroundColor(background);
		indicatorBuy.setBackgroundColor(background);

		buttonBuy = (Button) findViewById(R.id.buttonBuy);

		buttonBuy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent localIntent = new Intent(MainActivity.this,
						SearchBuyActivity.class);
				startActivity(localIntent);
			}
		});

		slidingMenu.toggle();

	}

	public void initializeSell() {
		slidingMenu.setActivated(true);
		setContentView(R.layout.sell);

		if (indicatorBuy != null) {
			indicatorBuy.invalidate();
		}
		RelativeLayout topBar = (RelativeLayout) findViewById(R.id.topBar);
		TextView desc = (TextView) findViewById(R.id.descriptionPage);
		int textcolor = getColor(R.color.side_navigation_outside_background);
		desc.setTextColor(textcolor);

		indicatorSell = (TitlePageIndicator) findViewById(R.id.indicator);
		indicatorSell.setTitles(TITLES_SELL);

		indicatorSell
				.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Triangle);
		indicatorSell
				.setFooterColor(getColor(R.color.side_navigation_outside_background));

		indicatorSell.setTextColor(textcolor);
		indicatorSell.setSelectedColor(textcolor);
		indicatorSell.setTextSize(17);
		indicatorSell.setFooterLineHeight(0);

		int background = getColor(R.color.background_gray_darker);

		topBar.setBackgroundColor(background);
		indicatorSell.setBackgroundColor(background);

		pagerSell = (ViewPager) findViewById(R.id.viewpager);
		pagerAdapterSell = new ViewPagerAdapterSell(this);
		pagerSell.setAdapter(pagerAdapterSell);
		indicatorSell.setViewPager(pagerSell);

		buttonSell = (Button) findViewById(R.id.buttonSell);

		buttonSell.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent localIntent = new Intent(MainActivity.this,
						SearchISBNActivity.class);

				startActivity(localIntent);
			}
		});

		// pagerAdapterSell.refreshAdapter(ALL);

		slidingMenu.toggle();

	}

	public void initializeWanted() {
		slidingMenu.setActivated(true);
		setContentView(R.layout.wanted);

		slidingMenu.toggle();

	}

	public void initializeAccount() {
		// IMPLEMENTARE ACTIVITY SEPARATA

		slidingMenu.setActivated(true);
		setContentView(R.layout.buy);

	}

	public void initializeSettings() {
		// IMPLEMENTARE ACTIVITY SEPARATA (SE SETTINGS NECESSARIE...DA VEDERE)

		slidingMenu.setActivated(true);
		setContentView(R.layout.buy);

	}

	public void initializeLicense() {
		// IMPLEMENTARE ACTIVITY SEPARATA

		slidingMenu.setActivated(true);
		setContentView(R.layout.buy);

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

	public void loginButtonClick() {
		setContentView(R.layout.facebook);
		ParseFacebookUtils.logIn(this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				if (user == null) {
					Log.d("MyApp",
							"Uh oh. The user cancelled the Facebook login.");
					initializeLogin();
				} else if (user.isNew()) {

					Log.d("MyApp",
							"User signed up and logged in through Facebook!");
					initializeLogin();
					loginPagerNextPage();
				} else {
					Log.d("MyApp", "User logged in through Facebook!");
					initializeLogin();
					loginPagerNextPage();

				}
			}
		});
	}

	public Session getSession() {
		return session;
	}

	public void loginPagerNextPage() {
		currentLoginTab++;
		loginPager.setCurrentItem(currentLoginTab);
	}

	public ArrayList<BookData> getBookList() {
		return bookList;
	}

	private int getColor(int resID) {
		return Color.parseColor(getString(resID));
	}

	public void setBookAsSold(BookData book) {
		if (book.sellingState == CURRENT) {
			book.sellingState = SOLD;
			pagerAdapterSell.refreshAdapter(0);
		}
	}

	public void setBookAsSelling(BookData book) {
		if (book.sellingState == CURRENT) {
			book.sellingState = SELLING;
			pagerAdapterSell.refreshAdapter(0);
		}
	}

	public void removeBookFromSell(BookData book) {
		bookCollection.removeBookFromListSell(book);
		pagerAdapterSell.refreshAdapter(0);
	}

}
