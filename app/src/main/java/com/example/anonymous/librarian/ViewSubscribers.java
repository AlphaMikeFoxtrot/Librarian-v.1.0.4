package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
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
import java.util.Collections;
import java.util.Comparator;

public class ViewSubscribers extends AppCompatActivity {

    ListView mListView;
    ProgressDialog progressDialog;
    ViewSubscriberListViewAdapter adapter;
    ArrayList<Subscribers> subscribers;
    NetworkChangeReceiver receiver;
    Boolean flag = false;
    ArrayList<Subscribers> subscribersTwo;
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
        if(getIntent().getStringExtra("previousAct").toLowerCase().contains("report")){

            Intent toReport = new Intent(this, ReportMainActivity.class);
            toReport.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(toReport);

        } else if(getIntent().getStringExtra("previousAct").toLowerCase().contains("main")){

            Intent toMain = new Intent(this, MainActivity.class);
            toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(toMain);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_subscribers);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        mListView = findViewById(R.id.view_subscriber_list_view);
        mListView.setTextFilterEnabled(true);

        Toolbar mToolbar = findViewById(R.id.view_subscriber_toolbar);
        setSupportActionBar(mToolbar);

        GetSubscribersAsyncTask getSubscribersAsyncTask = new GetSubscribersAsyncTask();
        getSubscribersAsyncTask.execute();

        subscribersTwo = new ArrayList<>();
//        adapter = new ViewSubscriberListViewAdapter(ViewSubscribers.this, new SubscriberProtocol(ViewSubscribers.this).getSubscribers());
//        mListView.setAdapter(adapter);

        mListView.setTextFilterEnabled(true);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Subscribers subscriberClicked = subscribers.get(i);

                // TODO : pass the name and id to the following intent
                Intent toSubscriberDetails = new Intent(ViewSubscribers.this, SubscriberDetails.class);
                String subscriberId = subscriberClicked.getmSubscriberId();
                toSubscriberDetails.putExtra("subscriberName", subscriberClicked.getmSubscriberName());
                toSubscriberDetails.putExtra("subscriberId", subscriberId);
                toSubscriberDetails.putExtra("previousAct", getIntent().getStringExtra("previousAct"));
                toSubscriberDetails.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toSubscriberDetails);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_subscriber, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
//        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menuItem.getActionView();
//        searchView.setQueryHint("Enter subscriber\'s id, name");
//
//        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//
//                return false;
//            }
//        });

        return true;
    }

//    if(newText.length() > 0 && newText != null){
//
//        newText = newText.toString().toUpperCase();
//        ArrayList<Subscribers> newList = new ArrayList<>();
//        for(int i = 0; i < subscribers.size(); i++){
//
//            if(subscribers.get(i).getmSubscriberName().toUpperCase().contains(newText) || subscribers.get(i).getmSubscriberId().toUpperCase().contains(newText)){
//                newList.add(subscribers.get(i));
//            }
//
//        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.action_add){
            Intent toAddSubscriber = new Intent(ViewSubscribers.this, AddSubscriber.class);
            toAddSubscriber.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            toAddSubscriber.putExtra("previousAct", getIntent().getStringExtra("previousAct"));
            startActivity(toAddSubscriber);
        }

        return true;
    }

    public class GetSubscribersAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ViewSubscribers.this);
            progressDialog.setMessage("Loading subscribers..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            final String GET_SUBSCRIBERS_URL = "http://fardeenpanjwani.com/librarian/get_subscribers_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL(ViewSubscribers.this).GET_SUBSCRIBERS_DETAILS());
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
                return "";
            } catch (IOException e) {
                e.printStackTrace();
                return "";
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

            // Toast.makeText(ViewSubscribers.this, "" + s, Toast.LENGTH_LONG).show();
            // Snackbar.make(new CoordinatorLayout(ViewSubscribers.this), s, Snackbar.LENGTH_LONG).show();

            if(s.isEmpty()){
                progressDialog.dismiss();
                Toast.makeText(ViewSubscribers.this, "The list seems to be empty", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.dismiss();
                try {

                    subscribers = new ArrayList<>();

                    JSONArray root = new JSONArray(s);
                    for(int i = 0; i < root.length(); i++){
                        JSONObject nthObject = root.getJSONObject(i);
                        String subscriber_name = nthObject.getString("subscriber_name");
                        String subscriber_id = nthObject.getString("subscriber_id");

                        Subscribers subscriber = new Subscribers();
                        subscriber.setmSubscriberId(subscriber_id);
                        subscriber.setmSubscriberName(subscriber_name);

                        subscribers.add(subscriber);
                    }

                    adapter = new ViewSubscriberListViewAdapter(ViewSubscribers.this, subscribers);
                    mListView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
