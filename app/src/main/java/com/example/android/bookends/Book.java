package com.example.android.bookends;

import java.util.ArrayList;

/**
 * Created by Don on 5/29/2017.
 * {@link Book} represents a Book volume data from Google Books.
 * It contains the title, author(s), book cover, and price of the book.
 */

public class Book {

    /* Title of the book */
    private String mTitle;

    /* Author(s) of the book */
    private ArrayList<String> mAuthor;

    /* List price of the book in USD */
    private Double mPrice;

    /* URL of the earthquake on USGS.gov */
    private String mUrl;

    /**
     * Create a new Book object.
     *
     * @param title is the title of the book.
     * @param author is the string holding the author(s) of the book.
     * @param price is the list price of the book.
     * @param url is the Google Book URL to find more details about the book.
     */
    public Book(String title, ArrayList<String> author, Double price, String url) {
        this.mTitle = title;
        this.mAuthor = author;
        this.mPrice = price;
        this.mUrl = url;
    }

    /**
     * Return the title of the book.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Return the string holding the author(s) of the book.
     */
    public ArrayList<String> getAuthor() {
        return mAuthor;
    }

    /**
     * Return the list price of the book.
     */
    public Double getPrice() {
        return mPrice;
    }

    /**
     * Return the Google Book URL to find more details about the book.
     */
    public String getUrl() {
        return mUrl;
    }
}
