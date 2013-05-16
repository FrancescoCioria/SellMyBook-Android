package com.mosquitolabs.sharemynote;

import java.util.ArrayList;

public class BookCollection {

	private static final int ALL = 0;
	private static final int SELLING = 2;
	private static final int CURRENT_SELL = 1;
	private static final int CURRENT_BUY = 1;
	private static final int BOUGHT = 2;
	private static final int SOLD = 3;

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

	public BookData getBookFromSearchByID(String ID) {
		for (BookData book : searchResult) {
			if (book.ID.equals(ID)) {
				return book;
			}
		}
		return null;
	}

	public void addBookToList(ArrayList<BookData> list, BookData book) {

		for (BookData temp : list) {
			if (temp.ID.equals(book.ID)) {
				return;
			}
		}

		list.add(book);

	}

	public void addBookToListWanted(BookData book) {

		for (BookData temp : wanted) {
			if (temp.ID.equals(book.ID)) {
				return;
			}
		}

		wanted.add(book);

	}

	public void addBookToListSell(BookData book) {
		sellSelling.clear();
		sellSold.clear();
		sellCurrent.clear();
		for (BookData temp : sellAll) {
			if (temp.ID.equals(book.ID)) {
				return;
			}
		}
		sellAll.add(book);
		for (BookData temp : sellAll) {
			switch (temp.sellingState) {
			case CURRENT_SELL:
				sellCurrent.add(temp);
				break;
			case SELLING:
				sellSelling.add(temp);
				break;
			case SOLD:
				sellSold.add(temp);
				break;

			default:
				break;
			}
		}
		sellAll.clear();
		for (BookData temp : sellCurrent) {
			sellAll.add(temp);
		}
		for (BookData temp : sellSelling) {
			sellAll.add(temp);
		}
		for (BookData temp : sellSold) {
			sellAll.add(temp);
		}

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

	public void removeBookFromListSell(BookData book) {
		int counter = 0;
		for (BookData temp : sellAll) {
			if (temp.ID.equals(book.ID)) {
				break;
			}
			counter++;
		}
		sellAll.remove(counter);
	}

	public void initializeSellSelling() {
		sellSelling.clear();
		for (BookData book : sellAll) {
			if (book.sellingState == SELLING) {
				sellSelling.add(book);
			}
		}
	}

	public void initializeSellCurrent() {
		sellCurrent.clear();
		for (BookData book : sellAll) {
			if (book.sellingState == CURRENT_SELL) {
				sellCurrent.add(book);
			}
		}
	}

	public void initializeSellSold() {
		sellSold.clear();
		for (BookData book : sellAll) {
			if (book.sellingState == SOLD) {
				sellSold.add(book);
			}
		}
	}

	public BookData getNextBookForSellingState(int sellingState, int paramInt) {
		int i = 0;
		for (BookData book : sellAll) {
			if (book.sellingState == sellingState) {
				if (i == paramInt) {
					return book;
				} else {
					i++;
				}

			}
		}
		return null;
	}

	public int getSellSizeForSellingState(int sellingState) {
		int i = 0;
		for (BookData book : sellAll) {
			if (book.sellingState == sellingState) {
				i++;
			}
		}
		return i;
	}

}
