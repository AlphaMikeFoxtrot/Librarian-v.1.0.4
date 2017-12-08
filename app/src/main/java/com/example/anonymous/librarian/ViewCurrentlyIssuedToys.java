package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.anonymous.librarian.CurrentlyIssuedToysAdapter.CurrentlyIssuedToysAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ViewCurrentlyIssuedToys extends AppCompatActivity {

    RecyclerView mRecyclerView;
    CurrentlyIssuedToysAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_currently_issued_toys);

        mRecyclerView = findViewById(R.id.view_currently_issued_toys_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        Toolbar mToolbar = findViewById(R.id.currently_issued_toys_toolbar);
        setSupportActionBar(mToolbar);
    }

    public class GetCurrentlyIssuedToysAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getApplicationContext());
            progressDialog.setMessage("Getting issued toys ..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String GET_TOYS_URL = "https://forlibrariandatabasetwo.000webhostapp.com/librarian/view_currently_issued_toys.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(GET_TOYS_URL);
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
            progressDialog.dismiss();
            if(s.length() > 0){

                try {
                    JSONArray root = new JSONArray(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
