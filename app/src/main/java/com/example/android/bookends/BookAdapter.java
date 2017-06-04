package com.example.android.bookends;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Don on 5/29/2017.
 */

public class BookAdapter extends ArrayAdapter<Book>{

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the earthquake data we want
     * to populate into the lists.
     *
     * @param context       The current context. Used to inflate the layout file.
     * @param objects       A List of Earthquake objects to dipsplay in a list.
     */
    public BookAdapter(@NonNull Context context, @NonNull List<Book> objects) {
        super(context, 0, objects);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the xisting view is being reused, otherwise inflate the view.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Book} object located at this position in the list
        Book currentBook = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID book_title_text
        TextView bookTitleTextView = (TextView) listItemView.findViewById(R.id.book_title_text);
        // Get the book title from the current Book object and
        // set this text on the bookTitleTextView
        bookTitleTextView.setText(currentBook.getTitle());

        // Find the TextView in the list_item.xml layout with the ID book_author_text
        TextView bookAuthorTextView = (TextView) listItemView.findViewById(R.id.book_author_text);
        // Get the book author(s) from t he current Book object and
        // set this text to the bookAuthorTextView
        String authorFormatted = formatAuthor(currentBook.getAuthor());
        bookAuthorTextView.setText(authorFormatted);

        // Find the TextView in the list_item.xml layout with the ID price_text
        TextView priceTextView = (TextView) listItemView.findViewById(R.id.price_text);
        // Get the book list price from the current Book object and
        // set this text on the priceTextView
        String priceFormatted = "$ " + Double.toString((currentBook.getPrice()));
        priceTextView.setText(priceFormatted);


        // Return the whole list item layout so that it can be shown in the ListView
        return listItemView;
    }

    private String formatAuthor(ArrayList<String> authors) {
        String formattedAuthors = authors.get(0);

        for (int i = 1; i < authors.size(); i++) {
            formattedAuthors += ", " + authors.get(i);
        }
        return formattedAuthors;
    }
}
