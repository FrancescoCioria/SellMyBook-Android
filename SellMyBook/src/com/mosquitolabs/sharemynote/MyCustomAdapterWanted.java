package com.mosquitolabs.sharemynote;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyCustomAdapterWanted extends BaseAdapter {
	private LayoutInflater mInflater;
	private BookCollection bookCollection = BookCollection.getInstance();

	Context context;

	public MyCustomAdapterWanted(Context paramContext) {
		this.mInflater = LayoutInflater.from(paramContext);
		context = paramContext;
	}

	public int getCount() {

		return bookCollection.getWanted().size();

	}

	public Object getItem(int paramInt) {
		return Integer.valueOf(paramInt);
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	public View getView(final int paramInt, View paramView,
			ViewGroup paramViewGroup) {
		ViewHolder holder = new ViewHolder();

		final BookData book = bookCollection.getWanted().get(paramInt);

		if (paramView == null) {
			paramView = mInflater.inflate(R.layout.book_list_item_search, null);

			holder.icon = (ImageView) paramView
					.findViewById(R.id.book_list_item_icon);
			holder.topMargin = (RelativeLayout) paramView
					.findViewById(R.id.book_list_item_TopMargin);
			holder.date = (TextView) paramView
					.findViewById(R.id.book_list_item_date);
			holder.bottomMargin = (RelativeLayout) paramView
					.findViewById(R.id.book_list_item_BottomMargin);
			holder.overflow = (Button) paramView
					.findViewById(R.id.spinnerOverflow);
			paramView.setTag(holder);
		}

		holder = (ViewHolder) paramView.getTag();

		if (paramInt == 0) {
			holder.topMargin.setVisibility(View.VISIBLE);
		} else {
			holder.topMargin.setVisibility(View.GONE);
		}

		if (paramInt == bookCollection.getSearchResult().size() - 1) {
			holder.bottomMargin.setVisibility(View.VISIBLE);
		} else {
			holder.bottomMargin.setVisibility(View.GONE);
		}

		Bitmap b = null;
		if (paramInt % 2 == 0) {
			b = getRoundedCornerBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.book_exampleold), 13);
		} else {
			b = getRoundedCornerBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.book_exampleold2), 13);
		}
		holder.icon.setImageBitmap(b);

		holder.overflow.setVisibility(View.GONE);

		return paramView;
	}

	static class ViewHolder {
		ImageView icon;
		TextView title;
		TextView author;
		TextView date;
		RelativeLayout topMargin;
		RelativeLayout bottomMargin;
		Button overflow;
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

}
