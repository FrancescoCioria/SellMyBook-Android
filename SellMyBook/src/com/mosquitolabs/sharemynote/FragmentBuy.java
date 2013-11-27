package com.mosquitolabs.sharemynote;

import com.actionbarsherlock.app.SherlockFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
public class FragmentBuy extends SherlockFragment {
	 
 
    public static Fragment newInstance(Context context) {
        FragmentBuy f = new FragmentBuy();
        
        
 
        return f;
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.buy, null);
        
        return root;
    }
 
}
