package com.mosquitolabs.sharemynote;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ParseAPICalls {

	private static ParseAPICalls instance = new ParseAPICalls();

	public static ParseAPICalls getInstance() {
		return instance;
	}

	public List<ParseObject> getUniversity() {
		ParseQuery query = new ParseQuery("University");
		List<ParseObject> university;

		try {

			university = query.find();

			return university;

		} catch (ParseException e) {

			Log.e(ParseAPICalls.class.getCanonicalName(), e.toString());

		}

		return null;
	}

	public List<ParseObject> getCampus(String universityID) {
		ParseQuery query = new ParseQuery("Campus");
		
		query.whereEqualTo("university", ParseObject.createWithoutData("University", universityID));
		List<ParseObject> campus;

		try {

			campus = query.find();
			
			
			return campus;

		} catch (ParseException e) {

			Log.e(ParseAPICalls.class.getCanonicalName(), e.toString());

		}

		return null;
	}

}
