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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddBook extends AppCompatActivity {

    EditText mNewBookName, mNewBookAuthor, mNewBookIdTwo;
    ProgressDialog progressDialog, generateIdProgressDialog;
    Button mSubmit, mReset, mCancel;
    boolean isGenerated;
    TextView mNewBookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        mNewBookName = findViewById(R.id.add_book_new_book_name);
        mNewBookAuthor = findViewById(R.id.add_book_new_book_author);
        mNewBookId = findViewById(R.id.add_book_new_book_id);
        mNewBookIdTwo = findViewById(R.id.add_book_new_book_id_edit_text);

        mSubmit = findViewById(R.id.add_book_submit_button);
        mReset = findViewById(R.id.add_book_reset_button);
        mCancel = findViewById(R.id.add_book_cancel_button);

        new GenerateIdProtocol().execute();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO:
            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNewBookName.setText("");
                mNewBookName.setHint("The Secret");
                mNewBookAuthor.setText("");
                mNewBookAuthor.setHint("*optional");
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

    public class GenerateIdProtocol extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            generateIdProgressDialog = new ProgressDialog(AddBook.this);
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
                    Integer.parseInt(actualId);
                    String newId = "SB-" + String.valueOf(actualId);
                    mNewBookId.setText(newId);
                    isGenerated = true;

                } catch (JSONException e) {
                    e.printStackTrace();
                    mNewBookIdTwo.setVisibility(View.VISIBLE);
                    mNewBookId.setVisibility(View.GONE);
                    isGenerated = false;
                }

            } else {

                Toast.makeText(AddBook.this, "Something went wrong when generating new id", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
