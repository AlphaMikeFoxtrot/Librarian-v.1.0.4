package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddToy extends AppCompatActivity {

    EditText mNewToyName;
    ProgressDialog progressDialog, generateIdProgressDialog;
    Button mSubmit, mReset, mCancel;
    TextView mNewToyId;

    @Override
    public void onBackPressed() {
        Intent toPrevious = new Intent(AddToy.this, ViewToys.class);
        toPrevious.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toPrevious);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toy);

        mNewToyName = findViewById(R.id.add_toy_new_toy_name);
        mNewToyId = findViewById(R.id.add_toy_new_toy_id);

        mSubmit = findViewById(R.id.add_toy_submit_button);
        mReset = findViewById(R.id.add_toy_reset_button);
        mCancel = findViewById(R.id.add_toy_cancel_button);

        new GenerateToyIdProtocol().execute();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AddToyAsyncTask().execute(mNewToyName.getText().toString(), mNewToyId.getText().toString());

            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNewToyName.setText("");
                mNewToyName.setHint("Rubik\'s Cube");
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toList = new Intent(AddToy.this, ViewToys.class);
                toList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toList);
            }
        });
    }

    public class AddToyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(AddToy.this);
            progressDialog.setMessage("Adding Toy to library");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String ADD_TOY_URL = "http://www.fardeenpanjwani.com/librarian/add_toy.php";

            String newToyName = strings[0];
            String newToyId = strings[1];
            // String newToyAuthor = strings[2];
            // String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String addedOn = format.format(new Date());

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(ADD_TOY_URL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("toyId", "UTF-8") +"="+ URLEncoder.encode(newToyId, "UTF-8") +"&"+
                        URLEncoder.encode("toyName", "UTF-8") +"="+ URLEncoder.encode(newToyName, "UTF-8") +"&"+
                        URLEncoder.encode("addedOn", "UTF-8") +"="+ URLEncoder.encode(addedOn, "UTF-8");

                bufferedWriter.write(dataToWrite);
                bufferedWriter.flush();
                bufferedWriter.close();

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
            if(s == null || s.contains("fail")){

                Toast.makeText(AddToy.this, "Something went wrong when adding new toy to database!\nPlease try again after some time.", Toast.LENGTH_LONG).show();

            } else if(s.contains("success")){

                Toast.makeText(AddToy.this, "Toy successfully added to database", Toast.LENGTH_SHORT).show();
                Intent toList = new Intent(AddToy.this, ViewToys.class);
                toList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toList);

            }
        }
    }

    public class GenerateToyIdProtocol extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            generateIdProgressDialog = new ProgressDialog(AddToy.this);
            generateIdProgressDialog.setMessage("Generating new ID");
            generateIdProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String GET_TOYS_URL = "http://www.fardeenpanjwani.com/librarian/get_toy_details.php";

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
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if(httpURLConnection != null){
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
            generateIdProgressDialog.dismiss();
            if(s != null){

                try {

                    JSONArray root = new JSONArray(s);
                    JSONObject lastObject = root.getJSONObject(root.length() - 1);
                    String lastToyId = lastObject.getString("toy_id");
                    String[] ids = lastToyId.split("/");
                    String actualId = ids[2];
                    int intActualId = Integer.parseInt(actualId);
                    String newId = "TL/SB/" + String.valueOf(intActualId + 1);
                    mNewToyId.setText(newId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {

                Toast.makeText(AddToy.this, "Something went wrong when generating new id", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
