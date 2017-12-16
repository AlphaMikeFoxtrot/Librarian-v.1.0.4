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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.anonymous.librarian.IssueToyAdapters.IssueToyPhaseOneAdapter;

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

public class IssueToyPhaseOne extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    ProgressDialog progressDialog;
    IssueToyPhaseOneAdapter adapter;
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
        setContentView(R.layout.activity_issue_toy_phase_one);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        mToolbar = findViewById(R.id.issue_toy_one_toolbar);
        setSupportActionBar(mToolbar);

        mRecyclerView = findViewById(R.id.issue_toy_one_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        GetToysListAsyncTask getToysListAsyncTask = new GetToysListAsyncTask();
        getToysListAsyncTask.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_toolbar, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
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

    public class GetToysListAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(IssueToyPhaseOne.this);
            progressDialog.setMessage("Getting Toys...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String GET_TOYS_URL = "http://fardeenpanjwani.com/librarian/get_toy_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL().GET_TOY_DETAILS());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
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
            if(s.length() > 0){

                progressDialog.dismiss();
                ArrayList<Toys> toys = new ArrayList<>();

                try {

                    JSONArray root = new JSONArray(s);
                    for(int i = 0; i < root.length(); i++){

                        JSONObject nthObject = root.getJSONObject(i);
                        String toyName = nthObject.getString("toy_name");
                        String toyId = nthObject.getString("toy_id");

                        Toys toy = new Toys();
                        toy.setmToyId(toyId);
                        toy.setmToyName(toyName);

                        toys.add(toy);

                    }

                    adapter = new IssueToyPhaseOneAdapter(IssueToyPhaseOne.this, toys);
                    mRecyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                progressDialog.dismiss();
                Toast.makeText(IssueToyPhaseOne.this, "The list seems to be empty", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(IssueToyPhaseOne.this, MainActivity.class));
            }
        }
    }
}
