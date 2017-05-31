package com.example.android.bookends;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookEndActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {
    /** URL for book data from the Google Books API */
    private static final String BOOK_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=10";

    /** Adapter for the list of books */
    private BookAdapter mAdapter;

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    /** ProgressBar that is displayed when loading data from HTTP request */
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_end);

        // Find areference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        // Set empty state
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_state);
        bookListView.setEmptyView(mEmptyStateTextView);

        // Set ProgressBar
        mProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);

        // Create a new adapter that takes an empty list of books
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Callback method to be invoked when an item in this AdapterView has
             * been clicked.
             * <p>
             * Implementers can call getItemAtPosition(position) if they need
             * to access the data associated with the selected item.
             *
             * @param parent   The AdapterView where the click happened.
             * @param view     The view within the AdapterView that was clicked (this
             *                 will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id       The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current book that was clicked on
                Book book = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUrl = Uri.parse(book.getUrl());
                // Create a new intent to view the book URI
                Intent intent = new Intent(Intent.ACTION_VIEW, bookUrl);
                // Send the intent ot launch a new activity
                startActivity(intent);
            }
        });

        // Get a reference to the LoadManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Check if network connection exists
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    }
}
