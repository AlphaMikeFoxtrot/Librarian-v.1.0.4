package com.example.anonymous.librarian.ViewToysAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.anonymous.librarian.ToyDetail;
import com.example.anonymous.librarian.Toys;
import com.example.anonymous.librarian.Toys;
import com.example.anonymous.librarian.IssueToyOnClickListeners.IssueToyPhaseOneOnItemClickListener;
import com.example.anonymous.librarian.R;
import com.example.anonymous.librarian.ViewToysCustomFilter.ViewToysCustomFilter;
import com.example.anonymous.librarian.ViewToysViewHolder.ViewToysViewHolder;

import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 10-Dec-17.
 */

public class ViewToysAdapter extends RecyclerView.Adapter<ViewToysViewHolder> implements Filterable{

//    Context context;
//    public ArrayList<Toys> oldList, newList;
//    ViewToysCustomFilter filter;
//
//    public ViewToysAdapter(Context context, ArrayList<Toys> oldList) {
//        this.context = context;
//        this.oldList = oldList;
//        this.newList = oldList;
//    }
//
//    @Override
//    public ViewToysViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View listItemView = LayoutInflater.from(context).inflate(R.layout.activity_view_books, parent, false);
//
//        return new ViewToysViewHolder(listItemView);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewToysViewHolder holder, int position) {
//
//        Toys currentToy = (Toys) oldList.get(position);
//
//        holder.mToyName.setText("books"); // (currentToy.getmToyName());
//        holder.mToyId.setText(currentToy.getmToyId());
//
//        holder.setOnItemClickListener(new IssueToyPhaseOneOnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                // TODO :
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return oldList.size();
//    }
//
//    @Override
//    public Filter getFilter() {
//        if(filter == null){
//
//            filter = new ViewToysCustomFilter(this, oldList);
//
//        }
//
//        return filter;
//    }

    Context context;
    public ArrayList<Toys> oldList, newList;
    ViewToysCustomFilter filter;

    public ViewToysAdapter(Context context, ArrayList<Toys> oldList) {
        this.context = context;
        this.oldList = oldList;
        this.newList = oldList;
    }

    @Override
    public ViewToysViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(context).inflate(R.layout.view_book_custom_card_view, parent, false);

        return new ViewToysViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(ViewToysViewHolder holder, int position) {

        Toys currentToy = oldList.get(position);

        holder.mToyName.setText(currentToy.getmToyName());
        holder.mToyId.setText(currentToy.getmToyId());

        holder.setOnItemClickListener(new IssueToyPhaseOneOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                // TODO :
                Toys clickedToy = oldList.get(position);
                Intent toToyDetail = new Intent(context, ToyDetail.class);
                toToyDetail.putExtra("toyName", clickedToy.getmToyName());
                toToyDetail.putExtra("toyId", clickedToy.getmToyId());
                toToyDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(toToyDetail);

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
            filter = new ViewToysCustomFilter(this, oldList);
        }

        return filter;
    }
}
