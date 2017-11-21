package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 12-Nov-17.
 */

public class IssuedBookPhaseOneAdapter extends RecyclerView.Adapter<IssuedBookPhaseOneAdapter.IssuedBookPhaseOneViewHolder> {

    ProgressDialog progressDialog;

    public static Context context;
    ArrayList<Books> mBooks = new ArrayList<>();

    final String ID = "ID : ";

    public IssuedBookPhaseOneAdapter(Context context, ArrayList<Books> mBooks) {
        this.context = context;
        this.mBooks = mBooks;
    }

    @Override
    public IssuedBookPhaseOneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.issued_book_one_custom_list_view, parent, false);

        return new IssuedBookPhaseOneViewHolder(listItemView, this.context, this.mBooks);
    }

    @Override
    public void onBindViewHolder(IssuedBookPhaseOneViewHolder holder, int position) {

        Books currentBook = mBooks.get(position);

        holder.mBookName.setText(currentBook.getmBookName());
        holder.mBookId.setText(currentBook.getmBookId());

    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public class IssuedBookPhaseOneViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mBookName, mBookId;
        Context context;
        ArrayList<Books> books;

        public IssuedBookPhaseOneViewHolder(View itemView, Context context, ArrayList<Books> books) {
            super(itemView);

            this.context = context;
            this.books = books;

            itemView.setOnClickListener(this);

            mBookName = itemView.findViewById(R.id.book_name);
            mBookId = itemView.findViewById(R.id.book_id);
        }

        @Override
        public void onClick(View view) {
            // TODO : add Intents to issuedbookfinalphase

            int position = getAdapterPosition();

            InsertTempDataAsyncTask insertTempDataAsyncTask = new InsertTempDataAsyncTask();
            insertTempDataAsyncTask.execute(mBooks.get(position).getmBookName(), mBooks.get(position).getmBookId());

            Intent toIssueBookPhaseTwo = new Intent(context, IssuedBookPhaseTwo.class);
            toIssueBookPhaseTwo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.context.startActivity(toIssueBookPhaseTwo);
        }
    }
    public void setFilter(ArrayList<Books> newList){
        mBooks = new ArrayList<>();
        mBooks.addAll(newList);
        notifyDataSetChanged();
    }

    public class InsertTempDataAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {
            String bookName = strings[0];
            String bookId = strings[1];

            final String INSERT_TEMP_DATA_URL = "https://suppliant-fives.000webhostapp.com/librarian/insert_temp_book_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(INSERT_TEMP_DATA_URL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("book_name", "UTF-8") +"="+ URLEncoder.encode(bookName, "UTF-8") +"&"+
                        URLEncoder.encode("book_id", "UTF-8") +"="+ URLEncoder.encode(bookId, "UTF-8");

                bufferedWriter.write(dataToWrite);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

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
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.contains("success")){
                // progressDialog.dismiss();
            } else {
                // progressDialog.dismiss();
                Toast.makeText(context, "Sorry! There seems to be a problem with the server\n" + s, Toast.LENGTH_LONG).show();
            }
        }
    }

}
