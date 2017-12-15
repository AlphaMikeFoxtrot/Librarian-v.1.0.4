package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class IssueToyFinalPhase extends AppCompatActivity {

    TextView toyName, toyId, subscriberName, subscriberId;
    Button submit, cancel, reset;
    ProgressDialog progressDialog, issueToyProgressDialog, cancelResetProgressDialog, p;
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
        Intent toPreviousActivity = new Intent(this, IssueToyPhaseTwo.class);
        toPreviousActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toPreviousActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_toy_final_phase);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        Toolbar mToolbar = findViewById(R.id.issue_toy_final_toolbar);
        setSupportActionBar(mToolbar);

        toyName = findViewById(R.id.issue_toy_final_book_name);
        toyId = findViewById(R.id.issue_toy_final_book_id);

        subscriberName = findViewById(R.id.issue_toy_final_subscriber_name);
        subscriberId = findViewById(R.id.issue_toy_final_subscriber_id);

        submit = findViewById(R.id.issue_toy_submit_button);
        cancel = findViewById(R.id.issue_toy_cancel_button);
        reset = findViewById(R.id.issue_toy_reset_button);

        Intent intent = getIntent();
        subscriberName.setText(intent.getStringExtra("subscriberName"));
        subscriberId.setText(intent.getStringExtra("subscriberId"));

        GetTempBookDetailsAsyncTask getTempBookDetailsAsyncTask = new GetTempBookDetailsAsyncTask();
        getTempBookDetailsAsyncTask.execute();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                IssueToyAsyncTask issueToyAsyncTask = new IssueToyAsyncTask();
                // Toast.makeText(IssueToyFinalPhase.this, "" + intent.getStringExtra("subscriberName") + "\n" + intent.getStringExtra("subscriberId"), Toast.LENGTH_SHORT).show();
                issueToyAsyncTask.execute(toyName.getText().toString(), toyId.getText().toString(), intent.getStringExtra("subscriberName"), intent.getStringExtra("subscriberId"));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IssueToyCancelProtocol issueBookCancelProtocol = new IssueToyCancelProtocol();
                issueBookCancelProtocol.execute();

                Intent toMainActivity = new Intent(IssueToyFinalPhase.this, MainActivity.class);
                toMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toMainActivity);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IssueToyCancelProtocol issueBookCancelProtocol = new IssueToyCancelProtocol();
                issueBookCancelProtocol.execute();

                Intent toPhaseOne = new Intent(IssueToyFinalPhase.this, IssueToyPhaseOne.class);
                toPhaseOne.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toPhaseOne);
            }
        });
    }

    public class GetTempBookDetailsAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(IssueToyFinalPhase.this);
            progressDialog.setMessage("Loading necessary Data");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String GET_TEMP_DATA_URL = "http://fardeenpanjwani.com/librarian/get_temp_toy_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL().GET_TEMP_TOY_DETAILS());
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
            if(s.toString().length() <= 0){
                progressDialog.dismiss();
                toyName.setText("un available");
                toyId.setText("un available");
            } else {
                progressDialog.dismiss();
                try {

                    JSONArray root = new JSONArray(s);
                    JSONObject nthObject = root.getJSONObject(0);
                    toyName.setText(nthObject.getString("toy_name"));
                    toyId.setText(nthObject.getString("toy_id"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class IssueToyAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            issueToyProgressDialog = new ProgressDialog(IssueToyFinalPhase.this);
            issueToyProgressDialog.setMessage("Issuing Toy.......");
            issueToyProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String toyName = strings[0];
            String toyId = strings[1];
            String subscriberName = strings[2];
            String subscriberId = strings[3];

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
            String issuedOnDate = df.format(c.getTime());

            final String ISSUE_BOOK_URL = "http://fardeenpanjwani.com/librarian/issue_toy.php";

            HttpURLConnection httpURLConnection = null;
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL().ISSUE_TOY());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("toy_name", "UTF-8") +"="+ URLEncoder.encode(toyName, "UTF-8") +"&"+
                                URLEncoder.encode("toy_id", "UTF-8") +"="+ URLEncoder.encode(toyId, "UTF-8") +"&"+
                                URLEncoder.encode("subscriber_name", "UTF-8") +"="+ URLEncoder.encode(subscriberName, "UTF-8") +"&"+
                                URLEncoder.encode("subscriber_id", "UTF-8") +"="+ URLEncoder.encode(subscriberId, "UTF-8") +"&"+
                                URLEncoder.encode("issued_on", "UTF-8") +"="+ URLEncoder.encode(issuedOnDate, "UTF-8");

                bufferedWriter.write(dataToWrite);
                bufferedWriter.flush();
                bufferedWriter.close();

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
            if(s.contains("success")) {
                issueToyProgressDialog.dismiss();
                Toast.makeText(IssueToyFinalPhase.this, "Toy Successfully issued!", Toast.LENGTH_SHORT).show();
                Intent toMainActivity = new Intent(IssueToyFinalPhase.this, MainActivity.class);
                toMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toMainActivity);
            } else {
                issueToyProgressDialog.dismiss();
                Toast.makeText(IssueToyFinalPhase.this, "Sorry! Something went wrong with the database\n" + s, Toast.LENGTH_LONG).show();
                TextView error = findViewById(R.id.error);
                error.setText(s);
            }
        }
    }

    public class IssueToyCancelProtocol extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            p = new ProgressDialog(IssueToyFinalPhase.this);
            p.setMessage("running protocol");
            p.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            final String CANCEL_PROTOCOL_URL = "http://www.fardeenpanjwani.com/librarian/cancel_issue_protocol/cancel_issue_toy_protocol.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL().CANCEL_ISSUE_TOY_PROTOCOL());
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
                return "fail";
            } catch (IOException e) {
                e.printStackTrace();
                return "fail";
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
}
