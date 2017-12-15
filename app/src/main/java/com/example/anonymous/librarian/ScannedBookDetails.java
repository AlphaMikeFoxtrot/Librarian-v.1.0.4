package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class ScannedBookDetails extends AppCompatActivity {

    TextView mScannedBookName, mScannedBookAuthor, mScannedBookNewId;
    Button mBack, mAddBook, mNot;
    Toolbar mToolbar;
    ProgressDialog progressDialog, generateIdProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_book_details);

        mScannedBookName = findViewById(R.id.scanned_book_detail_book_name);
        mScannedBookAuthor = findViewById(R.id.scanned_book_detail_book_author);
        mScannedBookNewId = findViewById(R.id.scanned_book_detail_book_id);

        mBack = findViewById(R.id.scanned_book_detail_back_button);
        mAddBook = findViewById(R.id.scanned_book_detail_add_button);
        mNot = findViewById(R.id.scanned_book_detail_not_button);

        mToolbar = findViewById(R.id.scanned_book_detail_toolbar);
        setSupportActionBar(mToolbar);

        new GetScannedBookDetails().execute();
        new GenerateIdProtocol().execute();

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        mAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddScannedBookAsyncTask().execute(mScannedBookName.getText().toString(), mScannedBookNewId.getText().toString(), mScannedBookAuthor.getText().toString());
            }
        });

        mNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScannedBookDetails.this, AddBook.class));
            }
        });
    }



    public class GetScannedBookDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ScannedBookDetails.this);
            progressDialog.setMessage("Getting scanned book details....");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String GET_BOOK_DETAILS = getIntent().getStringExtra("bookUrl");

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                httpURLConnection = (HttpURLConnection) new URL(GET_BOOK_DETAILS).openConnection();
                // httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("GET");
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
            // EditText errorEditText = findViewById(R.id.error_scanned_book);
            // errorEditText.setText(s);
            if(s.isEmpty() || s.length() < 0){
//                Toast.makeText(ScannedBookDetails.this, "s.isEmpty()", Toast.LENGTH_LONG).show();
                Toast.makeText(ScannedBookDetails.this, "Sorry! The scanned book was not found in the database\nPlease add the details manually", Toast.LENGTH_LONG).show();
                finish();
            } else {

                try {

                    JSONObject root = new JSONObject(s);
                    JSONArray data = root.getJSONArray("data");
                    JSONObject book_data = data.getJSONObject(0);
                    mScannedBookName.setText(book_data.getString("title_long"));
                    JSONArray author_data = book_data.getJSONArray("author_data");
                    JSONObject author = author_data.getJSONObject(0);
                    mScannedBookAuthor.setText(author.getString("name"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    // EditText editText = findViewById(R.id.error_scanned_book);
                    // editText.setText(e.toString() + "\n" + getIntent().getStringExtra("bookUrl"));
                    // Toast.makeText(ScannedBookDetails.this, "" + e.toString(), Toast.LENGTH_LONG).show();
                    Toast.makeText(ScannedBookDetails.this, "Sorry! The scanned book was not found in the database\nPlease add the details manually", Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        }
    }

    public class GenerateIdProtocol extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            generateIdProgressDialog = new ProgressDialog(ScannedBookDetails.this);
            generateIdProgressDialog.setMessage("Generating new ID");
            generateIdProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String GET_BOOKS_URL = "http://www.fardeenpanjwani.com/librarian/get_book_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(GET_BOOKS_URL);
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
                    String lastBookId = lastObject.getString("book_id");
                    String[] ids = lastBookId.split("-");
                    String actualId = ids[1];
                    int intActualId = Integer.parseInt(actualId);
                    String newId = "SB-" + String.valueOf(intActualId + 1);
                    mScannedBookNewId.setText(newId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {

                Toast.makeText(ScannedBookDetails.this, "Something went wrong when generating new id", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public class AddScannedBookAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ScannedBookDetails.this);
            progressDialog.setMessage("Adding book to library");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String ADD_BOOK_URL = "http://www.fardeenpanjwani.com/librarian/add_book.php";

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

                URL url = new URL(ADD_BOOK_URL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("newBookId", "UTF-8") + "=" + URLEncoder.encode(newBookId, "UTF-8") + "&" +
                        URLEncoder.encode("newBookName", "UTF-8") + "=" + URLEncoder.encode(newBookName, "UTF-8") + "&" +
                        URLEncoder.encode("newBookAuthor", "UTF-8") + "=" + URLEncoder.encode(newBookAuthor, "UTF-8") + "&" +
                        URLEncoder.encode("newBookAddedOn", "UTF-8") + "=" + URLEncoder.encode(addedOn, "UTF-8");

                bufferedWriter.write(dataToWrite);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);

                String line;
                StringBuilder response = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {

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
                if (httpURLConnection != null) {
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
            progressDialog.dismiss();
            if (s == null || s.contains("fail")) {

                Toast.makeText(ScannedBookDetails.this, "Something went wrong when adding new book to database!\nPlease try again after some time.", Toast.LENGTH_LONG).show();

            } else if (s.contains("success")) {

                Toast.makeText(ScannedBookDetails.this, "Book successfully added to database", Toast.LENGTH_SHORT).show();
                Intent toList = new Intent(ScannedBookDetails.this, ViewBooks.class);
                toList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toList);

            }
        }
    }
}
