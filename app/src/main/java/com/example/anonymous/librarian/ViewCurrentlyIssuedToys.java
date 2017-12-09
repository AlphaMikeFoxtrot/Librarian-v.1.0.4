package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anonymous.librarian.CurrentlyIssuedToysAdapter.CurrentlyIssuedToysAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;

public class ViewCurrentlyIssuedToys extends AppCompatActivity {

    RecyclerView mRecyclerView;
    CurrentlyIssuedToysAdapter adapter;
    ProgressDialog progressDialog;
    ArrayList<Toys> toys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_currently_issued_toys);

        mRecyclerView = findViewById(R.id.view_currently_issued_toys_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ViewCurrentlyIssuedToys.this));
        mRecyclerView.setHasFixedSize(true);

        Toolbar mToolbar = findViewById(R.id.currently_issued_toys_toolbar);
        setSupportActionBar(mToolbar);

        GetCurrentlyIssuedToysAsyncTask getCurrentlyIssuedToysAsyncTask = new GetCurrentlyIssuedToysAsyncTask();
        getCurrentlyIssuedToysAsyncTask.execute();
    }

    public class GetCurrentlyIssuedToysAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ViewCurrentlyIssuedToys.this);
            progressDialog.setMessage("Getting issued toys ..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String GET_TOYS_URL = "http://fardeenpanjwani.com/librarian/view_currently_issued_toys.php";

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
                return "MalformedURLException";
            } catch (IOException e) {
                e.printStackTrace();
                return "IOException";
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

            // Toast.makeText(ViewCurrentlyIssuedToys.this, "" + s, Toast.LENGTH_SHORT).show();

            progressDialog.dismiss();
            if(!s.isEmpty() || true){

                // Toast.makeText(ViewCurrentlyIssuedToys.this, "s.length() > 0", Toast.LENGTH_SHORT).show();

                try {

                    // Toast.makeText(ViewCurrentlyIssuedToys.this, "inside Try block", Toast.LENGTH_SHORT).show();
                    JSONArray root = new JSONArray(s);
                    for(int i = 0; i < root.length(); i++){

                        JSONObject nthObject = root.getJSONObject(i);
                        Toys toy = new Toys();

                        toy.setmToyName(nthObject.getString("issued_toy_name"));
                        toy.setmToyId(nthObject.getString("issued_toy_id"));
                        toy.setIssuedToId(nthObject.getString("issued_toy_to_id"));
                        toy.setIssuedTo(nthObject.getString("issued_toy_to_name"));
                        String[] date = nthObject.getString("issued_toy_on").split(" ");
                        // String[] dateActual = date[0].toString().split("."););
                        String[] dates = date[0].toString().replace(".", " ").split(" ");
                        if(dates.length > 1){
                            toy.setIssuedOn(dates[2] + " " + new DateFormatSymbols().getMonths()[Integer.parseInt(dates[1])-1] + " " + dates[0]);
                        } else {
                            toy.setIssuedOn(date[0]);
                        }

                        toys.add(toy);

                    }

                    adapter = new CurrentlyIssuedToysAdapter(toys, ViewCurrentlyIssuedToys.this);
                    mRecyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {

                Toast.makeText(ViewCurrentlyIssuedToys.this, "Sorry! There seems to be a problem with the server!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ViewCurrentlyIssuedToys.this, MainActivity.class));

            }
        }
    }
}
