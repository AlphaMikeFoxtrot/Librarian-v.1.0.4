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
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.anonymous.librarian.CurrentlyIssuedBooksAdapter.ViewCurrentlyIssuedBookAdapter;

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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class ViewCurrentlyIssuedBooks extends AppCompatActivity {

    RecyclerView mRecyclerView;
    ArrayList<Books> mIssuedBooks = new ArrayList<>();
    ViewCurrentlyIssuedBookAdapter adapter;
    ProgressDialog progressDialog;
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
        setContentView(R.layout.activity_view_currently_issued_books);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        mRecyclerView = findViewById(R.id.view_currently_issued_book_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ViewCurrentlyIssuedBooks.this));
        mRecyclerView.setHasFixedSize(true);

        Toolbar mToolbar = findViewById(R.id.issued_books_toolbar);
        setSupportActionBar(mToolbar);

        GetIssuedBooksAsyncTask getIssuedBooksAsyncTask = new GetIssuedBooksAsyncTask();
        getIssuedBooksAsyncTask.execute();
    }

    public class GetIssuedBooksAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ViewCurrentlyIssuedBooks.this);
            progressDialog.setMessage("Getting issued books....");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String GET_BOOKS_URL = "http://fardeenpanjwani.com/librarian/get_issued_books.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL(ViewCurrentlyIssuedBooks.this).GET_ISSUED_BOOKS());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
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

            progressDialog.dismiss();

            if(s.isEmpty() || s.length() < 0){
                Toast.makeText(ViewCurrentlyIssuedBooks.this, "Sorry! There seems to be no issued books at the moment", Toast.LENGTH_SHORT).show();
            } else {

                try {

                    progressDialog.dismiss();

                    JSONArray root = new JSONArray(s);
                    for(int i = 0; i < root.length(); i++){

                        JSONObject nthObject = root.getJSONObject(i);

                        String issuedBookId = nthObject.getString("issued_book_id");
                        String issuedBookName = nthObject.getString("issued_book_name");
                        String issuedTo = nthObject.getString("issued_book_to_name");
                        String issuedOn = nthObject.getString("issued_on");

                        String[] issuedOnDates = issuedOn.split(" ");
                        String[] dates = issuedOnDates[0].replace(".", " ").split(" ");

                        Books book = new Books();
                        book.setmBookName(issuedBookName);
                        book.setmBookId(issuedBookId);
                        book.setmBookIssuedTo(issuedTo);
                        if(dates.length > 1){
                            // new DateFormatSymbols().getMonths()[Integer.parseInt(dates[1])-1]
                            book.setmBookIssuedOn(dates[2] + "/" + dates[1] + "/" + dates[0]);
                        } else {
                            book.setmBookIssuedOn(issuedOnDates[0]);
                        }

                        mIssuedBooks.add(book);

                    }

                    adapter = new ViewCurrentlyIssuedBookAdapter(mIssuedBooks, ViewCurrentlyIssuedBooks.this);
                    mRecyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
