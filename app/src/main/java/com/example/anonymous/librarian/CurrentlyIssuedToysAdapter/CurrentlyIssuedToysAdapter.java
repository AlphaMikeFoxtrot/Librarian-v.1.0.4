package com.example.anonymous.librarian.CurrentlyIssuedToysAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anonymous.librarian.CurrentlyIssuedToysViewHolder.CurrentlyIssuedToysViewHolder;
import com.example.anonymous.librarian.R;
import com.example.anonymous.librarian.Toys;

import java.util.ArrayList;

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
        holder.issuedOn.setText(currentToy.getIssuedOn());
        holder.issuedTo.setText(currentToy.getIssuedTo());

    }

    @Override
    public int getItemCount() {
        return issuedToys.size();
    }

}
