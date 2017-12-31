package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
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

public class AddBook extends AppCompatActivity {

    EditText mNewBookName, mNewBookAuthor, mNewBookId;
    ProgressDialog progressDialog, generateIdProgressDialog;
    Button mSubmit, mReset, mCancel;
    String newId;
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
        Intent toPrevious = new Intent(AddBook.this, ViewBooks.class);
        toPrevious.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toPrevious);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        mNewBookName = findViewById(R.id.add_book_new_book_name);
        mNewBookAuthor = findViewById(R.id.add_book_new_book_author);
        mNewBookId = findViewById(R.id.add_book_new_book_id);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        mSubmit = findViewById(R.id.add_book_submit_button);
        mReset = findViewById(R.id.add_book_reset_button);
        mCancel = findViewById(R.id.add_book_cancel_button);

        // new GenerateIdProtocol().execute();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AddBookAsyncTask().execute(mNewBookName.getText().toString(), mNewBookId.getText().toString(), mNewBookAuthor.getText().toString());

            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNewBookName.setText("");
                mNewBookAuthor.setText("");
                mNewBookId.setText("");
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toList = new Intent(AddBook.this, ViewBooks.class);
                toList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toList);
            }
        });
    }

    public class AddBookAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(AddBook.this);
            progressDialog.setMessage("Adding book to library");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String newBookName = strings[0];
            String newBookId = strings[1];
            String newBookAuthor = strings[2];
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String addedOn = format.format(new Date());

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(new ServerScriptsURL(AddBook.this).ADD_BOOK());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("newBookId", "UTF-8") +"="+ URLEncoder.encode(newBookId, "UTF-8") +"&"+
                        URLEncoder.encode("newBookName", "UTF-8") +"="+ URLEncoder.encode(newBookName, "UTF-8") +"&"+
                        URLEncoder.encode("newBookAuthor", "UTF-8") +"="+ URLEncoder.encode(newBookAuthor, "UTF-8") +"&"+
                        URLEncoder.encode("newBookAddedOn", "UTF-8") +"="+ URLEncoder.encode(addedOn, "UTF-8");

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

                Toast.makeText(AddBook.this, "Something went wrong when adding new book to database!\nPlease try again after some time.", Toast.LENGTH_LONG).show();

            } else if(s.contains("success")){

                Toast.makeText(AddBook.this, "Book successfully added to database", Toast.LENGTH_SHORT).show();
                Intent toList = new Intent(AddBook.this, ViewBooks.class);
                toList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toList);

            }
        }
    }
}
