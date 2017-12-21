package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Date;

public class ReturnBookFinalPhase extends AppCompatActivity {

    TextView mBookName, mBookId, mSubscriberName, mSubscriberId, mIssuedOnDate;
    Button mSubmit, mCancel, mReset;
    ProgressDialog progressDialog, returnBookProgressDialog;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book_final_phase);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        mBookName = findViewById(R.id.return_book_final_book_name);
        mBookId = findViewById(R.id.return_book_final_book_id);

        mSubscriberName = findViewById(R.id.return_book_final_subscriber_name);
        mSubscriberId = findViewById(R.id.return_book_final_subscriber_id);

        mIssuedOnDate = findViewById(R.id.return_book_final_issued_on_date);

        mSubmit = findViewById(R.id.return_book_submit_button);
        mCancel = findViewById(R.id.return_book_cancel_button);
        mReset = findViewById(R.id.return_book_reset_button);

        Intent intent = getIntent();

        Date currentTime = Calendar.getInstance().getTime();
        String returnedOnDate = currentTime.toString();

        GetIssuedBookDetailsAsyncTask getIssuedBookDetailsAsyncTask = new GetIssuedBookDetailsAsyncTask();
        getIssuedBookDetailsAsyncTask.execute(intent.getStringExtra("book_id"), intent.getStringExtra("book_name"));

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
                String returnedOnDate = df.format(c.getTime());
                ReturnBookAsyncTask returnBookAsyncTask = new ReturnBookAsyncTask();
                Toast.makeText(ReturnBookFinalPhase.this, "" + mBookId.getText().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(ReturnBookFinalPhase.this, "" + returnedOnDate, Toast.LENGTH_SHORT).show();
                // returnBookAsyncTask.execute(mBookId.getText().toString(), returnedOnDate);
            }
        });

    }

    public class GetIssuedBookDetailsAsyncTask extends AsyncTask<String, Void, String>{

        public String bookId, bookName;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ReturnBookFinalPhase.this);
            progressDialog.setMessage("Gathering required information");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            bookId = strings[0];
            bookName = strings[1];

            final String GET_ISSUED_BOOK_DETAILS_URL = "http://fardeenpanjwani.com/librarian/get_single_issued_book_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(new ServerScriptsURL().GET_SINGLE_ISSUED_BOOK_DETAILS());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("returnedBookId", "UTF-8") +"="+ URLEncoder.encode(bookId, "UTF-8");

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
            if(s.isEmpty()){
                progressDialog.dismiss();
                Toast.makeText(ReturnBookFinalPhase.this, "Sorry! There seems to be no issued book with the entered id", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.dismiss();
                mBookId.setText(bookId);
                mBookName.setText(bookName);
                try {

                    JSONObject root = new JSONObject(s);
                    mSubscriberId.setText(root.getString("issued_book_to_id"));
                    mSubscriberName.setText(root.getString("issued_book_to_name"));
                    mIssuedOnDate.setText(root.getString("issued_on"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class ReturnBookAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            returnBookProgressDialog = new ProgressDialog(ReturnBookFinalPhase.this);
            returnBookProgressDialog.setMessage("logging returned book details");
            returnBookProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String bookId = strings[0];
            String returnedOn = strings[1];

            final String RETURN_BOOK_URL = "http://fardeenpanjwani.com/librarian/return_book.php";

            HttpURLConnection httpURLConnection = null;
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL().RETURN_BOOK());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("returnedBookId", "UTF-8") +"="+ URLEncoder.encode(bookId, "UTF-8") +"&"+
                        URLEncoder.encode("returned_on", "UTF-8") +"="+ URLEncoder.encode(returnedOn, "UTF-8") +"&"+
                        URLEncoder.encode("returnedById", "UTF-8") +"="+ URLEncoder.encode(mSubscriberId.getText().toString(), "UTF-8");

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
            if(s.contains("success")){
                returnBookProgressDialog.dismiss();
                Toast.makeText(ReturnBookFinalPhase.this, "Entry registered successfully", Toast.LENGTH_SHORT).show();
                Intent toMainActivity = new Intent(ReturnBookFinalPhase.this, MainActivity.class);
                toMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toMainActivity);
            } else {
                returnBookProgressDialog.dismiss();
                Toast.makeText(ReturnBookFinalPhase.this, "Sorry! Something went wrong\n" + s, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
