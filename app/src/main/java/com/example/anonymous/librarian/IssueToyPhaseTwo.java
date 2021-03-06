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
import android.widget.TextView;
import android.widget.Toast;

import com.example.anonymous.librarian.IssueToyAdapters.IssueToyPhaseTwoAdapter;

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

public class IssueToyPhaseTwo extends AppCompatActivity {

    public RecyclerView mRecyclerView;
    public ProgressDialog progressDialog, p;
    public IssueToyPhaseTwoAdapter adapter;
    public ArrayList<Subscribers> subscribers = new ArrayList<>();
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
        new IssueToyCancelProtocol().execute();

        Intent toPreviousActivity = new Intent(this, IssueToyPhaseOne.class);
        toPreviousActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toPreviousActivity);
        
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_toy_phase_two);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        Toolbar mToolbar = findViewById(R.id.issue_toy_two_toolbar);
        setSupportActionBar(mToolbar);

        mRecyclerView = findViewById(R.id.issue_toy_two_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        GetSubscribersAsyncTask getSubscribersAsyncTask = new GetSubscribersAsyncTask();
        getSubscribersAsyncTask.execute();
    }

    public class GetSubscribersAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(IssueToyPhaseTwo.this);
            progressDialog.setMessage("Loading Children Details");
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String GET_SUBSCRIBERS_URL = "http://fardeenpanjwani.com/librarian/get_subscribers_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL(IssueToyPhaseTwo.this).GET_SUBSCRIBERS_DETAILS());
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

                String jsonResponse  = response.toString();
                return jsonResponse;

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

            if(s.length() <= 0){

                progressDialog.dismiss();
                Toast.makeText(IssueToyPhaseTwo.this, "List seems to be empty! Try again after some time", Toast.LENGTH_SHORT).show();

            } else {

                String subName, subId;

                progressDialog.dismiss();
                try {

                    JSONArray root = new JSONArray(s.toString());

                    for(int i = 0; i < root.length(); i++){

                        JSONObject nthObject = root.getJSONObject(i);
                        subName = nthObject.getString("subscriber_name");
                        subId = nthObject.getString("subscriber_id");

                        Subscribers subscriber = new Subscribers();
                        subscriber.setmSubscriberName(subName);
                        subscriber.setmSubscriberId(subId);

                        subscribers.add(subscriber);

                    }

                    adapter = new IssueToyPhaseTwoAdapter(getApplicationContext(), subscribers);
                    mRecyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class IssueToyCancelProtocol extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            p = new ProgressDialog(IssueToyPhaseTwo.this);
            p.setMessage("running protocol");
            p.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            final String CANCEL_PROTOCOL_URL = "http://www.fardeenpanjwani.com/librarian/cancel_issue_protocol/cancel_issue_toy_protocol.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL(IssueToyPhaseTwo.this).CANCEL_ISSUE_TOY_PROTOCOL());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);

                String line;
                StringBuilder response = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {

                    response.append(line);

                }

                return response.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "fail";
            } catch (IOException e) {
                e.printStackTrace();
                return "fail";
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
            p.dismiss();
//            if(s.contains("fail")){
//
//                Toast.makeText(IssueBookFinalPhase.this, "Something went wrong when running cancel protocol" + s, Toast.LENGTH_SHORT).show();
//
//            } else if(s.contains("success")){
//
//                // Toast.makeText(IssueBookFinalPhase.this, "Book successfully deleted", Toast.LENGTH_SHORT).show();
//                Intent toList = new Intent(IssueBookFinalPhase.this, IssueBookPhaseOne.class);
//                toList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(toList);
//
//            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_toolbar, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Enter subscriber\'s id or name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText.toUpperCase());
                return true;
            }
        });
        return true;
    }
}
