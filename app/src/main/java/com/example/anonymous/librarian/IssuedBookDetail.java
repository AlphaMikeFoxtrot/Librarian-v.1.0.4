package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.AsyncTaskLoader;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class IssuedBookDetail extends AppCompatActivity {

    TextView mIssuedBookId, mIssuedBookName, mIssuedBookToId, mIssuedBookToName, mIssuedBookOn, mIssuedBookDueDate;
    Button mBack, mReturnBook;
    ProgressDialog progressDialog;
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
        setContentView(R.layout.activity_issued_book_detail);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        mIssuedBookName = findViewById(R.id.issued_book_details_book_name);
        mIssuedBookId = findViewById(R.id.issued_book_details_book_id);
        mIssuedBookToId = findViewById(R.id.issued_book_details_issued_to_id);
        mIssuedBookToName = findViewById(R.id.issued_book_details_issued_to_name);
        mIssuedBookOn = findViewById(R.id.issued_book_details_issued_on);
        mIssuedBookDueDate = findViewById(R.id.issued_book_details_due_date);

        mReturnBook = findViewById(R.id.issued_book_details_return_button);
        mBack = findViewById(R.id.issued_book_details_back_button);

        mIssuedBookName.setText(getIntent().getStringExtra("bookName"));
        mIssuedBookId.setText(getIntent().getStringExtra("bookId"));
        mIssuedBookToName.setText(getIntent().getStringExtra("issuedToName"));
        // TODO : get issuedToId;
        // mIssuedBookToId.setText();
        new GetIssuedToId().execute(getIntent().getStringExtra("bookId"));

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        final Date startDate;
        // Toast.makeText(this, "" + getIntent().getStringExtra("issuedOn"), Toast.LENGTH_SHORT).show();
        try {
            // Toast.makeText(this, "inside date try block", Toast.LENGTH_SHORT).show();
            startDate = df.parse(getIntent().getStringExtra("issuedOn"));
            // mIssuedBookDueDate.setText(startDate.toString());
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(startDate);
            String dueDateOne = cal.getTime().toString();
            String[] dueDatesOne = dueDateOne.split(" ");
            // Toast.makeText(this, ""+dueDatesOne[0] + " " + dueDatesOne[1] + " " + dueDatesOne[2] + " " + dueDatesOne[dueDatesOne.length - 1], Toast.LENGTH_SHORT).show();
            mIssuedBookOn.setText(dueDatesOne[0] + " " + dueDatesOne[1] + " " + dueDatesOne[2] + " " + dueDatesOne[dueDatesOne.length - 1]);
            cal.add(Calendar.DAY_OF_MONTH, 25);
            // mIssuedBookDueDate.setText(cal.getTime().toString());
            String dueDate = cal.getTime().toString();
            String[] dueDates = dueDate.split(" ");
            mIssuedBookDueDate.setText(dueDates[0] + " " + dueDates[1] + " " + dueDates[2] + " " + dueDates[dueDates.length - 1]);
        } catch (ParseException e) {
            // Toast.makeText(this, "inside date catch block", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            mIssuedBookOn.setText(getIntent().getStringExtra("issuedOn"));
            mIssuedBookDueDate.setText("Unavailable");
        }

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent toIssuedBooks = new Intent(IssuedBookDetail.this, ViewCurrentlyIssuedBooks.class);
                toIssuedBooks.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toIssuedBooks);

            }
        });

        mReturnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
                String returnedOnDate = df.format(c.getTime());

                ReturnBookAsyncTask returnBookAsyncTask = new ReturnBookAsyncTask();
                returnBookAsyncTask.execute(getIntent().getStringExtra("bookId"), returnedOnDate);

            }
        });

    }
    
    public class ReturnBookAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(IssuedBookDetail.this);
            progressDialog.setMessage("logging returned book details");
            progressDialog.show();
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

                URL url = new URL(new ServerScriptsURL(IssuedBookDetail.this).RETURN_BOOK());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("returnedBookId", "UTF-8") +"="+ URLEncoder.encode(bookId, "UTF-8") +"&"+
                        URLEncoder.encode("returned_on", "UTF-8") +"="+ URLEncoder.encode(returnedOn, "UTF-8") +"&"+
                        URLEncoder.encode("returnedById", "UTF-8") +"="+ URLEncoder.encode(mIssuedBookToId.getText().toString(), "UTF-8");

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
                progressDialog.dismiss();
                Toast.makeText(IssuedBookDetail.this, "Entry registered successfully", Toast.LENGTH_SHORT).show();
                Intent toMainActivity = new Intent(IssuedBookDetail.this, MainActivity.class);
                toMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toMainActivity);
            } else {
                progressDialog.dismiss();
                Toast.makeText(IssuedBookDetail.this, "Sorry! Something went wrong\n" + s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class GetIssuedToId extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(IssuedBookDetail.this);
            progressDialog.setMessage("Getting Subscriber ID....");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String book_id = strings[0];

            HttpURLConnection httpURLConnection = null;
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL(IssuedBookDetail.this).GET_ISSUED_BOOK_TO_ID());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("bookId", "UTF-8") +"="+ URLEncoder.encode(book_id, "UTF-8");

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
            if (s.isEmpty() || s.length() < 0) {
                Toast.makeText(IssuedBookDetail.this, "Error when getting subscriber id", Toast.LENGTH_SHORT).show();
            } else {
                mIssuedBookToId.setText(s);
            }
        }
    }
}
