package com.mosquitolabs.sharemynote;

import java.io.Serializable;

public class BookData implements Serializable {
	private static final long serialVersionUID = 1L;
	public String title="";
	public String isbn="";
	public String originalPrice="";
	public String price="";
	public int state;
	public int sellingState=1;
	public String year="";
	public String userUri="";
	public String resourceUri="";
	public String author="";
	public String modified="";
	public String ID="";
	public String desc="";
	public String publisher="";
	public String imageUri="";
	
	public boolean used = false;

	public String toString() {
		return this.title;
	}

}
