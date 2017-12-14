package com.example.anonymous.librarian.ViewToysViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.anonymous.librarian.IssueToyOnClickListeners.IssueToyPhaseOneOnItemClickListener;
import com.example.anonymous.librarian.R;

/**
 * Created by ANONYMOUS on 10-Dec-17.
 */

public class ViewToysViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mToyName, mToyId;
    IssueToyPhaseOneOnItemClickListener onItemClickListener;

    public ViewToysViewHolder(View itemView) {
        super(itemView);

        this.mToyName = itemView.findViewById(R.id.view_books_book_name);
        this.mToyId = itemView.findViewById(R.id.view_books_book_id);

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
