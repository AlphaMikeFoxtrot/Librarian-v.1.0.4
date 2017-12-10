package com.example.anonymous.librarian.CurrentlyIssuedToysAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.anonymous.librarian.CurrentlyIssuedToysViewHolder.CurrentlyIssuedToysViewHolder;
import com.example.anonymous.librarian.IssueToyOnClickListeners.IssueToyPhaseOneOnItemClickListener;
import com.example.anonymous.librarian.IssuedToyDetail;
import com.example.anonymous.librarian.R;
import com.example.anonymous.librarian.Toys;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ANONYMOUS on 07-Dec-17.
 */

public class CurrentlyIssuedToysAdapter extends RecyclerView.Adapter<CurrentlyIssuedToysViewHolder> {

    ArrayList<Toys> issuedToys;
    Context context;

    public CurrentlyIssuedToysAdapter(ArrayList<Toys> issuedToys, Context context) {
        this.issuedToys = issuedToys;
        this.context = context;
    }

    @Override
    public CurrentlyIssuedToysViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(context).inflate(R.layout.currently_issued_book_custom_recycler_view, parent, false);
        return new CurrentlyIssuedToysViewHolder(listItemView, this.context, this.issuedToys);
    }

    @Override
    public void onBindViewHolder(CurrentlyIssuedToysViewHolder holder, int position) {

        Toys currentToy = issuedToys.get(position);

        holder.toyName.setText(currentToy.getmToyName());
        holder.toyId.setText(currentToy.getmToyId());
        // holder.issuedOn.setText(currentToy.getIssuedOn());

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate;
        Toast.makeText(context, "" + currentToy.getIssuedOn(), Toast.LENGTH_SHORT).show();
        try {

            startDate = df.parse(currentToy.getIssuedOn());
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(startDate);
            String date = cal.getTime().toString();
            String[] dueDatesOne = date.split(" ");
            holder.issuedOn.setText(dueDatesOne[0] + " " + dueDatesOne[1] + " " + dueDatesOne[2] + " " + dueDatesOne[dueDatesOne.length - 1]);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.issuedTo.setText(currentToy.getIssuedTo());

        holder.setOneOnItemClickListener(new IssueToyPhaseOneOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                // Toast.makeText(context, "" + issuedToys.get(position).getmToyName(), Toast.LENGTH_SHORT).show();
                Intent toDetails = new Intent(context, IssuedToyDetail.class);
                toDetails.putExtra("toyName", issuedToys.get(position).getmToyName());
                toDetails.putExtra("toyId", issuedToys.get(position).getmToyId());
                toDetails.putExtra("issuedToId", issuedToys.get(position).getIssuedToId());
                toDetails.putExtra("issuedToName", issuedToys.get(position).getIssuedTo());
                toDetails.putExtra("issuedOn", issuedToys.get(position).getIssuedOn());
                toDetails.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(toDetails);

            }
        });

    }

    @Override
    public int getItemCount() {
        return issuedToys.size();
    }

}
