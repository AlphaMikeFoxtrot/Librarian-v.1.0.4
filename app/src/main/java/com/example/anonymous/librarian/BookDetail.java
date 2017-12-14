package com.example.anonymous.librarian;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class BookDetail extends AppCompatActivity {

    TextView mBookName, mBookId, mBookAuthor, mBookAddedOn;
    Button mBack, mDelete;
    Toolbar mToolbar;
    ProgressDialog progressDialog;

    @Override
    public void onBackPressed() {
        Intent toPreviousActivity = new Intent(BookDetail.this, ViewBooks.class);
        toPreviousActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toPreviousActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        mToolbar = findViewById(R.id.book_detail_toolbar);
        setSupportActionBar(mToolbar);

        mBack = findViewById(R.id.book_detail_back_button);
        mDelete = findViewById(R.id.book_detail_delete_button);

        mBookName = findViewById(R.id.book_detail_book_name);
        mBookId = findViewById(R.id.book_detail_book_id);
        mBookAuthor = findViewById(R.id.book_detail_book_author);
        mBookAddedOn = findViewById(R.id.book_detail_added_on);

        mBookName.setText(getIntent().getStringExtra("bookName"));
        mBookId.setText(getIntent().getStringExtra("bookId"));
        mBookAuthor.setText(getIntent().getStringExtra("bookAuthor"));

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date;
        try {
            date = dateFormat.parse(getIntent().getStringExtra("addedOn"));
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            String dateTwo = calendar.getTime().toString();
            String[] dates = dateTwo.split(" ");
            mBookAddedOn.setText(dates[0] + " " + dates[1] + " " + dates[2] + " " + dates[dates.length - 1]);
        } catch (ParseException e) {
            e.printStackTrace();
            mBookAddedOn.setText(getIntent().getStringExtra("addedOn"));
        }

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent toViewBooks = new Intent(BookDetail.this, ViewBooks.class);
                toViewBooks.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toViewBooks);

            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                new DeleteBookProtocol().execute(mBookId.getText().toString());
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(BookDetail.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });
    }

    public class DeleteBookProtocol extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(BookDetail.this);
            progressDialog.setMessage("deleting book");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String DELETE_BOOK_URL = "http://www.fardeenpanjwani.com/librarian/delete_book.php";

            String bookId = strings[0];

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(DELETE_BOOK_URL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("bookId", "UTF-8") +"="+ URLEncoder.encode(bookId, "UTF-8");

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
                Toast.makeText(BookDetail.this, "something went wrong when deleting the book.\nPlease try again after sometime", Toast.LENGTH_LONG).show();
            } else if(s.contains("success")) {

                Toast.makeText(BookDetail.this, "Book successfully deleted.", Toast.LENGTH_SHORT).show();
                Intent toList = new Intent(BookDetail.this, ViewBooks.class);
                toList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toList);

            }
        }
    }
}
