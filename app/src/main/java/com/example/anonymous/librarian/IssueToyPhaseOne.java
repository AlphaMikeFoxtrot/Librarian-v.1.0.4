package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

public class IssueToyPhaseOne extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    ProgressDialog progressDialog;
    IssueToyPhaseOneAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_toy_phase_one);

        mToolbar = findViewById(R.id.issue_toy_one_toolbar);
        setSupportActionBar(mToolbar);

        mRecyclerView = findViewById(R.id.issue_toy_one_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

    }

    public class getToysListAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(IssueToyPhaseOne.this);
            progressDialog.setMessage("Getting books...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String GET_TOYS_URL = "https://forlibrariandatabasetwo.000webhostapp.com/librarian/get_toy_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(GET_TOYS_URL);
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
