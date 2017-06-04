package com.example.android.bookends;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Don on 5/29/2017.
 * Helper methods related to requesting and receiving book data from Google Books
 */

public final class QueryUtils {

    /** Tag for the log messages*/
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /*
     * Query the Google Books API and return a {@link Book} object
     */
    public static ArrayList<Book> fetchBookDat(String requestUrl) {
        // Create the URL object
        URL url = createURL(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Errorclosing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an ArrayList of {@link Book} objects
        // Return the {@link Book} objects
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL
     */
    private static URL createURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }

        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book results");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies that an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<Book> extractFeatureFromJson(String bookJSON) {
        // If the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        ArrayList<Book> books = new ArrayList<>();

        // Try to parse the JSON. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Creates root of JSON
            JSONObject rootJsonResponse = new JSONObject(bookJSON);

            // Extracts "items" JSONArray, which contains all books
            JSONArray booksArray = rootJsonResponse.getJSONArray("items");

            for (int index = 0; index < booksArray.length(); index++) {
                // Get book JSONObject at position book
                JSONObject currentBook = booksArray.getJSONObject(index);

                // Get book volumeInfo JSONObject
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                // Get book author JSONArray
                JSONArray authorArray = volumeInfo.getJSONArray("authors");
                ArrayList<String> authors = new ArrayList<>();

                for (int i = 0; i < authorArray.length(); i++) {
                    authors.add(authorArray.get(i).toString());
                }

                // Get book saleInfo JSONObject
                //JSONObject saleInfo = currentBook.getJSONObject("saleInfo");

                // Get book listPrice JSONObject
                //JSONObject listPrice = saleInfo.getJSONObject("listPrice");

                // Extract "title" for book title, "amount" for USD price, "infoLink" for url
                // Create book java object from title, author(s), price, and URL.
                // Add book to list of books
                Double salePriceDummy = 0.0;
                // ToDo add logic to extract author array
                books.add(new Book(volumeInfo.getString("title"), authors, salePriceDummy /**listPrice.getDouble("amount")*/, volumeInfo.getString("infoLink")));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }

        // Return the list of books
        return books;
    }

}
