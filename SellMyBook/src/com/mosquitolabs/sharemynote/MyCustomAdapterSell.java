package com.mosquitolabs.sharemynote;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyCustomAdapterSell extends BaseAdapter {
	private LayoutInflater mInflater;
	private BookCollection bookCollection = BookCollection.getInstance();
	private int currentList;

	private static final int ALL = 0;
	private static final int CURRENT = 1;
	private static final int SELLING = 2;
	private static final int SOLD = 3;

	private static final int EDIT = 0;
	private static final int USER = 0;
	private static final int FAILED = 2;
	private static final int REMOVE = 1;
	private static final int SET_AS_SOLD = 1;
	private static final int SELL_ANOTHER = 1;

	private int size;
	private MainActivity context;

	private static String[] MENU_CURRENT = { "Profilo compratore",
			"Segna come venduto", "Transazione fallita" };
	private static String[] MENU_SELLING = { "Modifica", "Rimuovi" };
	private static String[] MENU_SOLD = { "Profilo compratore",
			"Vendine un altro" };

	public MyCustomAdapterSell(MainActivity paramContext, int currentList) {
		this.mInflater = LayoutInflater.from(paramContext);
		this.currentList = currentList;
		context = paramContext;
		Log.i("MyCustomAdapterSell", "created: " + context.getCounter2());
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
		ViewHolder localViewHolder;

		if (paramView == null) {

			paramView = mInflater.inflate(R.layout.book_list_item_sell, null);
			localViewHolder = new ViewHolder();

			localViewHolder.icon = (ImageView) paramView
					.findViewById(R.id.book_list_item_icon);
			localViewHolder.topMargin = (RelativeLayout) paramView
					.findViewById(R.id.book_list_item_TopMargin);
			localViewHolder.topTitleAll = (TextView) paramView
					.findViewById(R.id.book_list_item_TopTitleAll);
			localViewHolder.date = (TextView) paramView
					.findViewById(R.id.book_list_item_date);
			localViewHolder.bottomMargin = (RelativeLayout) paramView
					.findViewById(R.id.book_list_item_BottomMargin);

			localViewHolder.sold = (ImageView) paramView
					.findViewById(R.id.book_list_item_corner_sold);

			localViewHolder.overflow = (Button) paramView
					.findViewById(R.id.spinnerOverflow);

			paramView.setTag(localViewHolder);

		}

		localViewHolder = (ViewHolder) paramView.getTag();

		final ViewHolder holder = localViewHolder;

		BookData temp = new BookData();

		switch (currentList) {
		case ALL:

			temp = bookCollection.getNextBookForSellingState(CURRENT, paramInt);
			if (temp == null) {
				temp = bookCollection.getNextBookForSellingState(
						SELLING,
						paramInt
								- bookCollection
										.getSellSizeForSellingState(CURRENT));
			}
			if (temp == null) {
				temp = bookCollection
						.getNextBookForSellingState(
								SOLD,
								paramInt
										- (bookCollection
												.getSellSizeForSellingState(CURRENT) + bookCollection
												.getSellSizeForSellingState(SELLING)));
			}
			break;

		default:
			temp = bookCollection.getNextBookForSellingState(currentList,
					paramInt);
			break;

		}
		long end = Calendar.getInstance().getTimeInMillis();

		// Log.i("AdapterSell",
		// "ricerca libro: "+Long.toString(end-start)+" millis");

		final BookData book = temp;

		// CODICE COMUNE //
		Bitmap b = null;
		if (paramInt % 2 == 0) {
			b = getRoundedCornerBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.book_exampleold), 13);
		} else {
			b = getRoundedCornerBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.book_exampleold2), 13);
		}
		holder.icon.setImageBitmap(b);

		holder.sold.setVisibility(View.GONE);

		holder.topTitleAll.setVisibility(View.GONE);

		if (currentList == ALL) {
			if ((paramInt == 0)
					|| (paramInt == bookCollection
							.getSellSizeForSellingState(CURRENT))
					|| (paramInt == (bookCollection
							.getSellSizeForSellingState(CURRENT) + bookCollection
							.getSellSizeForSellingState(SELLING)))) {

				holder.topTitleAll.setVisibility(View.VISIBLE);

			}
		}

		switch (book.sellingState) {

		case SELLING:

			holder.overflow.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					popupMenu(arg0, book, MENU_SELLING);
				}
			});

			holder.topTitleAll.setText("Libri in vendita");

			holder.date.setText("In vendita dal 23/04/2013");

			break;

		case CURRENT:

			holder.overflow.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					popupMenu(arg0, book, MENU_CURRENT);
				}
			});

			// localViewHolder.buttonSetAsSold.setVisibility(View.VISIBLE);
			localViewHolder.topTitleAll.setText("Transazioni in corso");

			localViewHolder.date.setText("Transazione iniziata il 25/04/2013");

			break;
		case SOLD:

			holder.overflow.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					popupMenu(arg0, book, MENU_SOLD);
				}
			});

			holder.sold.setVisibility(View.VISIBLE);
			holder.topTitleAll.setText("Libri venduti");

			holder.date.setText("Venduto il 18/04/2013");

			break;
		}
		if (paramInt == 0) {
			holder.topMargin.setVisibility(View.VISIBLE);
		} else {
			holder.topMargin.setVisibility(View.GONE);
		}

		if (paramInt == size - 1) {
			holder.bottomMargin.setVisibility(View.VISIBLE);
		} else {
			holder.bottomMargin.setVisibility(View.GONE);
		}

		return paramView;

	}

	static class ViewHolder {
		ImageView icon;
		ImageView sold;
		TextView topTitleAll;
		TextView title;
		TextView author;
		TextView date;
		RelativeLayout topMargin;
		RelativeLayout bottomMargin;
		Button overflow;

	}

	private void t(int position) {

		Toast.makeText(context, Integer.toString(position), Toast.LENGTH_SHORT)
				.show();

	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public void popupMenu(View anchor, final BookData book, String[] items) {

		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView = layoutInflater.inflate(R.layout.popup, null);
		final PopupWindow popupWindow = new PopupWindow(popupView);

		final int NUM_OF_VISIBLE_LIST_ROWS = items.length;
		ListView list = (ListView) popupView.findViewById(R.id.listView);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				R.layout.list_item_instant, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		list.setAdapter(adapter);
		list.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		popupWindow.setWidth(list.getMeasuredWidth() + list.getMeasuredWidth()
				/ 5);
		popupWindow.setHeight((list.getMeasuredHeight() + 2)
				* NUM_OF_VISIBLE_LIST_ROWS);

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				switch (book.sellingState) {

				case CURRENT:
					switch (position) {
					case USER:
						t(position);

						break;

					case SET_AS_SOLD:
						context.setBookAsSold(book);
						t(position);
						break;

					case FAILED:
						context.setBookAsSelling(book);
						t(position);
						break;

					}
					break;

				case SELLING:
					switch (position) {
					case EDIT:
						t(position);
						break;

					case REMOVE:
						context.removeBookFromSell(book);
						t(position);
						break;
					}
					break;

				case SOLD:
					switch (position) {
					case USER:
						t(position);
						break;

					case SELL_ANOTHER:
						t(position);
						break;
					}
					break;

				default:
					size = bookCollection
							.getSellSizeForSellingState(currentList);
					break;

				}

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

}
