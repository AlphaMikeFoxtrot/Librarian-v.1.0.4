package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

public class ViewSubscribers extends AppCompatActivity {

    ListView mListView;
    ProgressDialog progressDialog;
    ViewSubscriberListViewAdapter adapter;
    ArrayList<Subscribers> subscribers;

    @Override
    public void onBackPressed() {
        Intent toMainActivity = new Intent(this, MainActivity.class);
        toMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toMainActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_subscribers);

        mListView = findViewById(R.id.view_subscriber_list_view);

        Toolbar mToolbar = findViewById(R.id.view_subscriber_toolbar);
        setSupportActionBar(mToolbar);

        GetSubscribersAsyncTask getSubscribersAsyncTask = new GetSubscribersAsyncTask();
        getSubscribersAsyncTask.execute();

        mListView.setTextFilterEnabled(true);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Subscribers subscriberClicked = subscribers.get(i);

                // TODO : pass the name and id to the following intent
                Intent toSubscriberDetails = new Intent(ViewSubscribers.this, SubscriberDetails.class);
                String subscriberId = subscriberClicked.getmSubscriberId();
                toSubscriberDetails.putExtra("subscriberId", subscriberId);
                toSubscriberDetails.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toSubscriberDetails);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_subscriber, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.action_add){
            // TODO:
            Intent toAddSubscriber = new Intent(ViewSubscribers.this, AddSubscriber.class);
            toAddSubscriber.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

                URL url = new URL(GET_SUBSCRIBERS_URL);
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
