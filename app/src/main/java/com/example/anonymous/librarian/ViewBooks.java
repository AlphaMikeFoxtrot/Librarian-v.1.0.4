package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.anonymous.librarian.ViewBooksAdapter.ViewBooksAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ViewBooks extends AppCompatActivity {

    RecyclerView mRecyclerView;
    ProgressDialog progressDialog;
    ViewBooksAdapter adapter;
    // ListView listView;
    // ArrayAdapter<String> adapter;


    @Override
    public void onBackPressed() {
        Intent toMainAcitivity = new Intent(ViewBooks.this, MainActivity.class);
        toMainAcitivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toMainAcitivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);

        mRecyclerView = findViewById(R.id.view_books_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        android.support.v7.widget.Toolbar mToolbar = findViewById(R.id.view_books_toolbar);
        setSupportActionBar(mToolbar);

        // listView = findViewById(R.id.list_view);

        new GetBooksAsyncTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_toolbar, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) menuItem.getActionView();
        EditText searchEditText = (EditText) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(android.R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(android.R.color.white));
        mSearchView.setQueryHint("Enter book name or id");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);

                return true;
            }
        });
        return true;
    }

    public class GetBooksAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ViewBooks.this);
            progressDialog.setMessage("getting books");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String GET_BOOK_URL = "http://www.fardeenpanjwani.com/librarian/get_book_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(GET_BOOK_URL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
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
            if(s != null){

                ArrayList<Books> books = new ArrayList<>();
                ArrayList<String> list = new ArrayList<>();

                try {

                    JSONArray root = new JSONArray(s);
                    // JSONObject rootTwo = root.getJSONObject(0);
//                    String data = "name : " + rootTwo.getString("book_name") + "\nID : " + rootTwo.getString("book_id");
//                    TextView error = findViewById(R.id.error);
//                    error.setText(data);
                    for(int i = 0; i < root.length(); i++){

                        JSONObject nthObject = root.getJSONObject(i);
                        Books book = new Books();

                        // list.add(nthObject.getString("book_name"));
                        book.setmBookName(nthObject.getString("book_name"));
                        book.setmBookId(nthObject.getString("book_id"));

                        // books.add(book);
                        // list.add(book.getmBookName());
                        books.add(book);

                    }

                    Toast.makeText(ViewBooks.this, "" + books.get(0).getmBookName() + " \n" + books.size(), Toast.LENGTH_SHORT).show();

                    // adapter = new ViewBooksAdapter(ViewBooks.this, books);
                    // mRecyclerView.setAdapter(adapter);
                    // adapter = new ArrayAdapter<String>(ViewBooks.this, android.R.layout.simple_list_item_1, list);
                    // listView.setAdapter(adapter);
                    adapter = new ViewBooksAdapter(ViewBooks.this, books);
                    mRecyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ViewBooks.this, "The list seems to be empty!", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
