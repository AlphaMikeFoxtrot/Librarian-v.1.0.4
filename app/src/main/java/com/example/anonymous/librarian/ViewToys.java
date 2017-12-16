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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anonymous.librarian.ViewToysAdapter.ViewToysAdapter;
import com.example.anonymous.librarian.ViewToysAdapter.ViewToysAdapter;

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
import java.util.Collections;
import java.util.Comparator;

public class ViewToys extends AppCompatActivity {

    RecyclerView mRecyclerView;
    ProgressDialog progressDialog;
    ViewToysAdapter adapter;
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
        Intent toMainActivity = new Intent(ViewToys.this, MainActivity.class);
        toMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toMainActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_toys);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        mRecyclerView = findViewById(R.id.view_toys_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        android.support.v7.widget.Toolbar mToolbar = findViewById(R.id.view_toys_toolbar);
        setSupportActionBar(mToolbar);

        new GetToysAsyncTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_book_and_search, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) menuItem.getActionView();
        EditText searchEditText = (EditText) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(android.R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(android.R.color.white));
        mSearchView.setQueryHint("Enter toy name or id");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId){

            case R.id.action_add:
                Intent toAddToy = new Intent(ViewToys.this, AddToy.class);
                toAddToy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toAddToy);

        }

        return true;
    }

    public class GetToysAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ViewToys.this);
            progressDialog.setMessage("getting toys");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String GET_TOY_URL = "http://www.fardeenpanjwani.com/librarian/get_toy_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL().GET_TOY_DETAILS());
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

                ArrayList<Toys> toys = new ArrayList<>();
                ArrayList<String> list = new ArrayList<>();

                try {

                    JSONArray root = new JSONArray(s);
                    // JSONObject rootTwo = root.getJSONObject(0);
//                    String data = "name : " + rootTwo.getString("toy_name") + "\nID : " + rootTwo.getString("toy_id");
//                    TextView error = findViewById(R.id.error);
//                    error.setText(data);
                    for(int i = 0; i < root.length(); i++){

                        JSONObject nthObject = root.getJSONObject(i);
                        Toys toy = new Toys();

                        // list.add(nthObject.getString("toy_name"));
                        toy.setmToyName(nthObject.getString("toy_name"));
                        toy.setmToyId(nthObject.getString("toy_id"));
                        toy.setAddedOn(nthObject.getString("added_on"));

                        toys.add(toy);

                    }

                    // Toast.makeText(ViewToys.this, "" + toys.get(0).getmToyName() + " \n" + toys.size(), Toast.LENGTH_SHORT).show();

                    // adapter = new ViewToysAdapter(ViewToys.this, toys);
                    // mRecyclerView.setAdapter(adapter);
                    // adapter = new ArrayAdapter<String>(ViewToys.this, android.R.layout.simple_list_item_1, list);
                    // listView.setAdapter(adapter);
                    adapter = new ViewToysAdapter(ViewToys.this, toys);
                    mRecyclerView.setAdapter(adapter);

                    Collections.sort(toys, new Comparator<Toys>() {
                        @Override
                        public int compare(Toys toys, Toys t1) {
                            return toys.getmToyName().compareToIgnoreCase(t1.getmToyName());
                        }
                    });

                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ViewToys.this, "The list seems to be empty!", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
