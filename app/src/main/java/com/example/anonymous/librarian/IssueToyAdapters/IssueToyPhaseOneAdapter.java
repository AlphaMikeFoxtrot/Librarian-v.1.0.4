package com.example.anonymous.librarian.IssueToyAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.anonymous.librarian.Toys;
import com.example.anonymous.librarian.IssueToyFilters.IssueToyPhaseOneFilter;
import com.example.anonymous.librarian.IssueToyHolders.IssueToyPhaseOneViewHolder;
import com.example.anonymous.librarian.IssueToyOnClickListeners.IssueToyPhaseOneOnItemClickListener;
import com.example.anonymous.librarian.IssueToyPhaseOne;
import com.example.anonymous.librarian.R;

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
                // TODO
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
}
