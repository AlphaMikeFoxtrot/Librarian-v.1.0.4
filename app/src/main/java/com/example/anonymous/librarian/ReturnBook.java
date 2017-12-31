package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

public class ReturnBook extends AppCompatActivity {

    RecyclerView mRecyclerView;
    public ReturnBookAdapter adapter;
    ProgressDialog progressDialog;
    public static ArrayList<Books> books = new ArrayList<>();
    NetworkChangeReceiver receiver;
    Boolean flag = false;
    IntentFilter filter;

    @Override
    protected void onStop() {
        super.onStop();
        if(flag) {
            unregisterReceiver(receiver);
            flag = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(flag) {
            unregisterReceiver(receiver);
            flag = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        mRecyclerView = findViewById(R.id.return_book_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ReturnBook.this));
        mRecyclerView.setHasFixedSize(true);

        Toolbar mToolbar = findViewById(R.id.return_book_toolbar);
        setSupportActionBar(mToolbar);

        if(books.size() > 0){
            books.clear();
        }

        GetBooksAsyncTask getBooksAsyncTask = new GetBooksAsyncTask();
        getBooksAsyncTask.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_toolbar, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Books> newList = new ArrayList<>();
                for(Books book : books){
                    String bookName = book.getmBookName();
                    if(bookName.toLowerCase().contains(newText)){
                        newList.add(book);
                    }
                }
                adapter.setFilter(newList);

                return true;
            }
        });

        return true;
    }

    public class GetBooksAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ReturnBook.this);
            progressDialog.setMessage("Loading Book List");
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String GET_BOOK_URL = "http://fardeenpanjwani.com/librarian/get_issued_books.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL(ReturnBook.this).GET_ISSUED_BOOKS());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                bufferedReader = new BufferedReader(inputStreamReader);

                String line;
                StringBuilder response = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }

                return response.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(ReturnBook.this, "The list seems to be empty!", Toast.LENGTH_SHORT).show();
            } else {

                progressDialog.dismiss();

                String bookName, bookId;

                try {

                    JSONArray root = new JSONArray(s);
                    for (int i = 0; i < root.length(); i++) {
                        JSONObject nthObject = root.getJSONObject(i);
                        bookName = nthObject.getString("issued_book_name");
                        bookId = nthObject.getString("issued_book_id");
                        Books book = new Books();
                        book.setmBookName(bookName);
                        book.setmBookId(bookId);
                        books.add(book);
                    }

                    adapter = new ReturnBookAdapter(getApplicationContext(), books);
                    mRecyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
