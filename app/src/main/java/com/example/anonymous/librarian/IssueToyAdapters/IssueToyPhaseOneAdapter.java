package com.example.anonymous.librarian.IssueToyAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.example.anonymous.librarian.IssueToyPhaseTwo;
import com.example.anonymous.librarian.MainActivity;
import com.example.anonymous.librarian.Toys;
import com.example.anonymous.librarian.IssueToyFilters.IssueToyPhaseOneFilter;
import com.example.anonymous.librarian.IssueToyHolders.IssueToyPhaseOneViewHolder;
import com.example.anonymous.librarian.IssueToyOnClickListeners.IssueToyPhaseOneOnItemClickListener;
import com.example.anonymous.librarian.IssueToyPhaseOne;
import com.example.anonymous.librarian.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 23-Nov-17.
 */

public class IssueToyPhaseOneAdapter extends RecyclerView.Adapter<IssueToyPhaseOneViewHolder> implements Filterable{

    Context context;
    public ArrayList<Toys> oldList, newList;
    IssueToyPhaseOneFilter filter;

    public IssueToyPhaseOneAdapter(Context context, ArrayList<Toys> oldList) {
        this.context = context;
        this.oldList = oldList;
        this.newList = oldList;
    }

    @Override
    public IssueToyPhaseOneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(context).inflate(R.layout.issued_book_one_custom_list_view, parent, false);

        return new IssueToyPhaseOneViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(IssueToyPhaseOneViewHolder holder, int position) {

        Toys currentToy = oldList.get(position);

        holder.toyName.setText(currentToy.getmToyName());
        holder.toyId.setText(currentToy.getmToyId());

        holder.setItemClickListener(new IssueToyPhaseOneOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                InsertTempDetailsAsyncTask insertTempDetailsAsyncTask = new InsertTempDetailsAsyncTask();
                insertTempDetailsAsyncTask.execute(oldList.get(position).getmToyName(), oldList.get(position).getmToyId());
                // Toast.makeText(context, "toy name" + oldList.get(position).getmToyName() + "toy id\n" + oldList.get(position).getmToyId(), Toast.LENGTH_SHORT).show();
                Intent toPhaseTwo = new Intent(context, IssueToyPhaseTwo.class);
                toPhaseTwo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(toPhaseTwo);

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
            filter = new IssueToyPhaseOneFilter(this, oldList);
        }

        return filter;
    }

    public class InsertTempDetailsAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String toyName = strings[0];
            String toyId = strings[1];

            final String INSERT_URL = "https://forlibrariandatabasetwo.000webhostapp.com/librarian/insert_temp_toy_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(INSERT_URL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("toy_name", "UTF-8") +"="+ URLEncoder.encode(toyName, "UTF-8") +"&"+
                        URLEncoder.encode("toy_id", "UTF-8") +"="+ URLEncoder.encode(toyId, "UTF-8");

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
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.isEmpty()){
                // Toast.makeText(context, "Sorry", Toast.LENGTH_SHORT).show();
                Intent toMainActivity = new Intent(context, MainActivity.class);
                toMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(toMainActivity);
                Toast.makeText(context, "" + s, Toast.LENGTH_LONG).show();
            } else {
                Intent toPhaseTwo = new Intent(context, IssueToyPhaseTwo.class);
                toPhaseTwo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(toPhaseTwo);
            }
        }
    }
}
