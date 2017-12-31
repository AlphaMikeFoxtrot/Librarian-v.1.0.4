package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
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

public class FinalDetailsActivity extends AppCompatActivity {

    ListView mListView;
    ReportAdapter adapter;
    ProgressDialog progressDialog;
    ArrayList<Subscribers> subscribers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_details);

        mListView = findViewById(R.id.report_list_view);

        Toolbar mToolbar = findViewById(R.id.report_toolbar);
        setSupportActionBar(mToolbar);

        new GetReport().execute();
    }

    public class GetReport extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(FinalDetailsActivity.this);
            progressDialog.setMessage("Loading subscribers..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            final String GET_SUBSCRIBERS_URL = "http://fardeenpanjwani.com/librarian/get_subscribers_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL(FinalDetailsActivity.this).GET_SUBSCRIBERS_DETAILS());
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
                Toast.makeText(FinalDetailsActivity.this, "The list seems to be empty", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.dismiss();
                try {

                    subscribers = new ArrayList<>();

                    JSONArray root = new JSONArray(s);
                    for(int i = 0; i < root.length(); i++){
                        JSONObject nthObject = root.getJSONObject(i);
                        String subscriber_name = nthObject.getString("subscriber_name");
                        String subscriber_book = nthObject.getString("books_activity");
                        String subscriber_toy = nthObject.getString("toys_activity");
                        // String subscriber_id = nthObject.getString("subscriber_id");

                        Subscribers subscriber = new Subscribers();
                        subscriber.setmSubscriberName(subscriber_name);
                        subscriber.setmSubscriberToyActivity(subscriber_toy);
                        subscriber.setmSubscriberBookActivity(subscriber_book);

                        subscribers.add(subscriber);
                    }

                    adapter = new ReportAdapter(FinalDetailsActivity.this, subscribers);
                    mListView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
