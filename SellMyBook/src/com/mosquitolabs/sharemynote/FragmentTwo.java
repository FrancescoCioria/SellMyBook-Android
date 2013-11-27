package com.mosquitolabs.sharemynote;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
 
public class FragmentTwo extends SherlockFragment {
	 
	 
    public static Fragment newInstance(Context context) {
        FragmentTwo f = new FragmentTwo();
 
        return f;
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.sell, null);
        return root;
    }
 
}
