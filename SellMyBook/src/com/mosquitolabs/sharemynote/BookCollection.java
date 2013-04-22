package com.mosquitolabs.sharemynote;

import java.util.ArrayList;

public class BookCollection {

	private static BookCollection instance = new BookCollection();

	
	private ArrayList<BookData> buyAll = new ArrayList<BookData>();
	private ArrayList<BookData> buyCurrent = new ArrayList<BookData>();
	private ArrayList<BookData> buyBought = new ArrayList<BookData>();

	private ArrayList<BookData> sellAll = new ArrayList<BookData>();
	private ArrayList<BookData> sellSelling = new ArrayList<BookData>();
	private ArrayList<BookData> sellCurrent = new ArrayList<BookData>();
	private ArrayList<BookData> sellSold = new ArrayList<BookData>();

	private ArrayList<BookData> searchResult = new ArrayList<BookData>();

	private ArrayList<BookData> wanted = new ArrayList<BookData>();

	public static BookCollection getInstance() {
		return instance;
	}

	public ArrayList<BookData> getBuyAll() {
		return buyAll;
	}

	public ArrayList<BookData> getBuyCurrent() {
		return buyCurrent;
	}

	public ArrayList<BookData> getBuyBought() {
		return buyBought;
	}

	public ArrayList<BookData> getSellAll() {
		return sellAll;
	}

	public ArrayList<BookData> getSellSelling() {
		return sellSelling;
	}

	public ArrayList<BookData> getSellCurrent() {
		return sellCurrent;
	}

	public ArrayList<BookData> getSellSold() {
		return sellSold;
	}

	public ArrayList<BookData> getSearchResult() {
		return searchResult;
	}

	public ArrayList<BookData> getWanted() {
		return wanted;
	}

	public void addBookToList(ArrayList<BookData> list, BookData book) {

		for (BookData temp : list) {
			if (temp.ID.equals(book.ID)) {
				return;
			}
		}

		list.add(book);

	}

	public void removeBookFromList(ArrayList<BookData> list, BookData book) {
		int counter = 0;
		for (BookData temp : list) {
			if (temp.ID.equals(book.ID)) {
				break;
			}
			counter++;
		}
		list.remove(counter);
	}

}
