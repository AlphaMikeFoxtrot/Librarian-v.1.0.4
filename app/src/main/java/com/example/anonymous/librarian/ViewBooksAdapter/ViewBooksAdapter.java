package com.example.anonymous.librarian.ViewBooksAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.example.anonymous.librarian.Books;
import com.example.anonymous.librarian.IssueToyFilters.IssueToyPhaseOneFilter;
import com.example.anonymous.librarian.IssueToyHolders.IssueToyPhaseOneViewHolder;
import com.example.anonymous.librarian.IssueToyOnClickListeners.IssueToyPhaseOneOnItemClickListener;
import com.example.anonymous.librarian.IssueToyPhaseTwo;
import com.example.anonymous.librarian.R;
import com.example.anonymous.librarian.Books;
import com.example.anonymous.librarian.ViewBooksCustomFilter.ViewBooksCustomFilter;
import com.example.anonymous.librarian.ViewBooksViewHolder.ViewBooksViewHolder;

import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 10-Dec-17.
 */

public class ViewBooksAdapter extends RecyclerView.Adapter<ViewBooksViewHolder> implements Filterable{

//    Context context;
//    public ArrayList<Books> oldList, newList;
//    ViewBooksCustomFilter filter;
//
//    public ViewBooksAdapter(Context context, ArrayList<Books> oldList) {
//        this.context = context;
//        this.oldList = oldList;
//        this.newList = oldList;
//    }
//
//    @Override
//    public ViewBooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View listItemView = LayoutInflater.from(context).inflate(R.layout.activity_view_books, parent, false);
//
//        return new ViewBooksViewHolder(listItemView);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewBooksViewHolder holder, int position) {
//
//        Books currentBook = (Books) oldList.get(position);
//
//        holder.mBookName.setText("books"); // (currentBook.getmBookName());
//        holder.mBookId.setText(currentBook.getmBookId());
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
//            filter = new ViewBooksCustomFilter(this, oldList);
//
//        }
//
//        return filter;
//    }

    Context context;
    public ArrayList<Books> oldList, newList;
    ViewBooksCustomFilter filter;

    public ViewBooksAdapter(Context context, ArrayList<Books> oldList) {
        this.context = context;
        this.oldList = oldList;
        this.newList = oldList;
    }

    @Override
    public ViewBooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(context).inflate(R.layout.view_book_custom_card_view, parent, false);

        return new ViewBooksViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(ViewBooksViewHolder holder, int position) {

        Books currentToy = oldList.get(position);

        holder.mBookName.setText(currentToy.getmBookName());
        holder.mBookId.setText(currentToy.getmBookId());

        holder.setOnItemClickListener(new IssueToyPhaseOneOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Toast.makeText(context, "book name : " + oldList.get(position).getmBookName(), Toast.LENGTH_SHORT).show();

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
            filter = new ViewBooksCustomFilter(this, oldList);
        }

        return filter;
    }
}
