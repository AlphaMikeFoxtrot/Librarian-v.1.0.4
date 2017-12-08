package com.example.anonymous.librarian.CurrentlyIssuedToysViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.anonymous.librarian.Toys;
import com.example.anonymous.librarian.R;

import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 07-Dec-17.
 */

public class CurrentlyIssuedToysViewHolder extends RecyclerView.ViewHolder {

    public TextView toyName, toyId, issuedTo, issuedOn;
    Context context;
    ArrayList<Toys> toys;

    public CurrentlyIssuedToysViewHolder(View itemView, Context context, ArrayList<Toys> toys) {
        super(itemView);

        this.context = context;
        this.toys = toys;

        toyName = itemView.findViewById(R.id.currently_issued_book_name);
        toyId = itemView.findViewById(R.id.currently_issued_book_id);
        issuedTo = itemView.findViewById(R.id.currently_issued_book_to);
        issuedOn = itemView.findViewById(R.id.currently_issued_book_on);
    }

}
