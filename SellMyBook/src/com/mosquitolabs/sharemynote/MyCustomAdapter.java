package com.mosquitolabs.sharemynote;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyCustomAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Display display;
	private Activity context;

	private String[] items;
	private MainActivity parentActivity;

	public MyCustomAdapter(Activity paramContext, String[] items) {
		this.mInflater = LayoutInflater.from(paramContext);
		context = paramContext;
		this.items = items;
	}

	public int getCount() {
		return items.length;
	}

	public Object getItem(int paramInt) {
		return Integer.valueOf(paramInt);
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		ViewHolder localViewHolder;

		paramView = mInflater.inflate(R.layout.list_item, null);
		localViewHolder = new ViewHolder();
		localViewHolder.textView = (TextView) paramView
				.findViewById(R.id.textViewName);

		localViewHolder.textView.setText(items[paramInt].toString());

		return paramView;

	}

	static class ViewHolder {

		TextView textView;

	}

}
