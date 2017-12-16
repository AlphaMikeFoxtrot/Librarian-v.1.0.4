package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.example.anonymous.librarian.IssueBookAdapter.IssueBookPhaseOneAdapter;

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
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class IssueBookPhaseOne extends AppCompatActivity {

    RecyclerView mRecyclerView;
    public static IssueBookPhaseOneAdapter adapter;
    public ProgressDialog progressDialog;
    public ArrayList<Books> mBooks = new ArrayList<>();
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
    public void onBackPressed() {
        Intent toMainActivity = new Intent(this, MainActivity.class);
        toMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toMainActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_book_phase_one);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        Toolbar mToolbar = findViewById(R.id.issue_book_one_toolbar);
        setSupportActionBar(mToolbar);

        // mSearchView = findViewById(R.id.issue_book_one_search_view);

        mRecyclerView = findViewById(R.id.issue_book_one_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(IssueBookPhaseOne.this));
        mRecyclerView.setHasFixedSize(true);

        GetBooksAsyncTask getBooksAsyncTask = new GetBooksAsyncTask();
        getBooksAsyncTask.execute();

    }

    public class GetBooksAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(IssueBookPhaseOne.this);
            progressDialog.setMessage("Loading Book List");
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String GET_BOOK_URL = "http://fardeenpanjwani.com/librarian/get_book_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL().GET_BOOK_DETAILS());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                bufferedReader = new BufferedReader(inputStreamReader);

                String line;
                StringBuilder response = new StringBuilder();

                while((line = bufferedReader.readLine()) != null){
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
                if(httpURLConnection != null){
                    httpURLConnection.disconnect();
                }
                if(bufferedReader != null){
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
            if(s == null){
                progressDialog.dismiss();
                Toast.makeText(IssueBookPhaseOne.this, "The list seems to be empty!", Toast.LENGTH_SHORT).show();
            } else {

                progressDialog.dismiss();

                String bookName, bookId;

                try {

                    JSONArray root = new JSONArray(s);
                    for(int i = 0; i < root.length(); i++){
                        JSONObject nthObject = root.getJSONObject(i);
                        bookName = nthObject.getString("book_name");
                        bookId = nthObject.getString("book_id");
                        Books book = new Books();
                        book.setmBookName(bookName);
                        book.setmBookId(bookId);
                        mBooks.add(book);
                    }

                    adapter = new IssueBookPhaseOneAdapter(getApplicationContext(), mBooks);
                    mRecyclerView.setAdapter(adapter);

                    Collections.sort(mBooks, new Comparator<Books>() {
                        @Override
                        public int compare(Books books, Books t1) {
                            return books.getmBookName().compareToIgnoreCase(t1.getmBookName());
                        }
                    });

                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_toolbar, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Enter issued book id");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText.toUpperCase());
//                newText = newText.toLowerCase();
//                ArrayList<Books> newList = new ArrayList<>();
//                for(Books book : mBooks){
//                    String bookId = book.getmBookId();
//                    if(bookId.toLowerCase().contains(newText)){
//                        newList.add(book);
//                    }
//                }
//                adapter.setFilter(newList);

                return true;
            }
        });
        return true;
    }
}
