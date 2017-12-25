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
import org.w3c.dom.Text;

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
import java.nio.Buffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IssueBookFinalPhase extends AppCompatActivity {

    TextView bookName, bookId, subscriberName, subscriberId;
    Button issueBookSubmit, issueBookCancel;
    ProgressDialog progressDialog, issueBookProgressDialog, p;
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
        Intent toPreviousActivity = new Intent(this, IssuedBookPhaseTwo.class);
        toPreviousActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toPreviousActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_book_final_phase);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        Toolbar mToolbar = findViewById(R.id.issue_book_final_toolbar);
        setSupportActionBar(mToolbar);

        bookName = findViewById(R.id.issue_book_final_book_name);
        bookId = findViewById(R.id.issue_book_final_book_id);

        subscriberName = findViewById(R.id.issue_book_final_subscriber_name);
        subscriberId = findViewById(R.id.issue_book_final_subscriber_id);

        issueBookSubmit = findViewById(R.id.issue_book_submit_button);
        issueBookCancel = findViewById(R.id.issue_book_cancel_button);

        bookName.setText(getIntent().getStringExtra("bookName"));
        bookId.setText(getIntent().getStringExtra("bookId"));

        Intent intent = getIntent();
        subscriberName.setText(intent.getStringExtra("subscriberName"));
        subscriberId.setText(intent.getStringExtra("subscriberId"));

        GetTempBookDetailsAsyncTask getTempBookDetailsAsyncTask = new GetTempBookDetailsAsyncTask();
        getTempBookDetailsAsyncTask.execute();

        issueBookSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                IssueBookAsyncTask issueBookAsyncTask = new IssueBookAsyncTask();
                issueBookAsyncTask.execute(bookName.getText().toString(), bookId.getText().toString(), intent.getStringExtra("subscriberName"), intent.getStringExtra("subscriberId"));
            }
        });

        issueBookCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IssueBookCancelProtocol().execute();

                Intent toMainActivity = new Intent(IssueBookFinalPhase.this, MainActivity.class);
                toMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toMainActivity);
            }
        });

    }

    public class GetTempBookDetailsAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(IssueBookFinalPhase.this);
            progressDialog.setMessage("Loading necessary Data");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String GET_TEMP_DATA_URL = "http://fardeenpanjwani.com/librarian/get_temp_book_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL().GET_TEMP_BOOK_DETAILS());
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
                bookName.setText("un available");
                bookId.setText("un available");
            } else {
                progressDialog.dismiss();
                try {

                    JSONArray root = new JSONArray(s);
                    JSONObject nthObject = root.getJSONObject(0);
                    bookName.setText(nthObject.getString("book_name"));
                    bookId.setText(nthObject.getString("book_id"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class IssueBookAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            issueBookProgressDialog = new ProgressDialog(IssueBookFinalPhase.this);
            issueBookProgressDialog.setMessage("Issuing Book.......");
            issueBookProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String bookName = strings[0];
            String bookId = strings[1];
            String subscriberName = strings[2];
            String subscriberId = strings[3];

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
            String issuedOnDate = df.format(c.getTime());

            final String ISSUE_BOOK_URL = "http://fardeenpanjwani.com/librarian/issue_book.php";

            HttpURLConnection httpURLConnection = null;
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL().ISSUE_BOOK());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = // URLEncoder.encode("bookName", "UTF-8") +"="+ URLEncoder.encode(bookName, "UTF-8") +"&"+
                        URLEncoder.encode("bookId", "UTF-8") +"="+ URLEncoder.encode(bookId, "UTF-8") +"&"+
                        // URLEncoder.encode("subscriberName", "UTF-8") +"="+ URLEncoder.encode(subscriberName, "UTF-8") +"&"+
                        URLEncoder.encode("subscriberId", "UTF-8") +"="+ URLEncoder.encode(subscriberId, "UTF-8") +"&"+
                        URLEncoder.encode("issuedOn", "UTF-8") +"="+ URLEncoder.encode(issuedOnDate, "UTF-8");

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
                issueBookProgressDialog.dismiss();
                Toast.makeText(IssueBookFinalPhase.this, "Book Successfully issued!", Toast.LENGTH_SHORT).show();
                Intent toMainActivity = new Intent(IssueBookFinalPhase.this, MainActivity.class);
                toMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toMainActivity);
            } else {
                issueBookProgressDialog.dismiss();
                Toast.makeText(IssueBookFinalPhase.this, "Sorry! Something went wrong with the database\n" + s, Toast.LENGTH_LONG).show();
                TextView error = findViewById(R.id.error);
                error.setText(s);
            }
        }
    }

    public class IssueBookCancelProtocol extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            p = new ProgressDialog(IssueBookFinalPhase.this);
            p.setMessage("running protocol");
            p.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            final String CANCEL_PROTOCOL_URL = "http://www.fardeenpanjwani.com/librarian/cancel_issue_protocol/cancel_issue_book_protocol.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL().CANCEL_ISSUE_BOOK_PROTOCOL());
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
