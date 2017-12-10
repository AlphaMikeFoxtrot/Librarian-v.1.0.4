package com.example.anonymous.librarian.ViewBooksViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.anonymous.librarian.Books;
import com.example.anonymous.librarian.IssueToyOnClickListeners.IssueToyPhaseOneOnItemClickListener;
import com.example.anonymous.librarian.IssueToyPhaseOne;
import com.example.anonymous.librarian.R;

import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 10-Dec-17.
 */

public class ViewBooksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mBookName, mBookId;
    IssueToyPhaseOneOnItemClickListener onItemClickListener;

    public ViewBooksViewHolder(View itemView) {
        super(itemView);

        this.mBookName = itemView.findViewById(R.id.view_books_book_name);
        this.mBookId = itemView.findViewById(R.id.view_books_book_id);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        this.onItemClickListener.onItemClick(view, getLayoutPosition());
    }

    public void setOnItemClickListener(IssueToyPhaseOneOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
