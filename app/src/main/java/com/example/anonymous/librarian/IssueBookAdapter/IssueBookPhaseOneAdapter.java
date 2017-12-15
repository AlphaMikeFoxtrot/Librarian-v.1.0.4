package com.example.anonymous.librarian.IssueBookAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.example.anonymous.librarian.Books;
import com.example.anonymous.librarian.IssueBookFilters.IssueBookPhaseOneFilter;
import com.example.anonymous.librarian.IssueBookFinalPhase;
import com.example.anonymous.librarian.IssueBookHolders.IssueBookPhaseOneHolder;
import com.example.anonymous.librarian.IssueBookOnClickListeners.IssueBookPhaseOneOnItemClickListener;
import com.example.anonymous.librarian.IssueBookPhaseOne;
import com.example.anonymous.librarian.IssuedBookPhaseTwo;
import com.example.anonymous.librarian.R;
import com.example.anonymous.librarian.ServerScriptsURL;

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
 * Created by ANONYMOUS on 22-Nov-17.
 */

public class IssueBookPhaseOneAdapter extends RecyclerView.Adapter<IssueBookPhaseOneHolder> implements Filterable{

    Context context;
    public ArrayList<Books> oldList, newList;
    IssueBookPhaseOneFilter filter;

    public IssueBookPhaseOneAdapter(Context context, ArrayList<Books> oldList) {
        this.context = context;
        this.oldList = oldList;
        this.newList = oldList;
    }

    @Override
    public IssueBookPhaseOneHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.issued_book_one_custom_list_view, parent, false);

        return new IssueBookPhaseOneHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(IssueBookPhaseOneHolder holder, int position) {

        Books currentBook = oldList.get(position);

        holder.bookName.setText(currentBook.getmBookName());
        holder.bookId.setText(currentBook.getmBookId());

        holder.setItemClickListener(new IssueBookPhaseOneOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                InsertTempDataAsyncTask insertTempDataAsyncTask = new InsertTempDataAsyncTask();
                insertTempDataAsyncTask.execute(oldList.get(position).getmBookName(), oldList.get(position).getmBookId());

                Intent toIssueBookPhaseTwo = new Intent(context, IssuedBookPhaseTwo.class);
//                Intent finalPhase = new Intent(context, IssueBookFinalPhase.class);
//                finalPhase.putExtra("bookName", oldList.get(position).getmBookName());
//                finalPhase.putExtra("bookId", oldList.get(position).getmBookId());
                toIssueBookPhaseTwo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Snackbar.make(view, oldList.get(position).getmBookName(), Snackbar.LENGTH_LONG).show();
                context.startActivity(toIssueBookPhaseTwo);

            }
        });

    }

    @Override
    public int getItemCount() {
        return oldList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new IssueBookPhaseOneFilter(this, oldList);
        }

        return filter;
    }

    public class InsertTempDataAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {
            String bookName = strings[0];
            String bookId = strings[1];

            // final String INSERT_TEMP_DATA_URL = "http://fardeenpanjwani.com/librarian/insert_temp_book_details.php";


            HttpURLConnection httpURLConnection = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(new ServerScriptsURL().INSERT_TEMP_BOOK_DETAILS());
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
