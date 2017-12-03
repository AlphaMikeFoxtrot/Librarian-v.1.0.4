package com.example.anonymous.librarian.IssueToyHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.anonymous.librarian.IssueToyAdapters.IssueToyPhaseOneAdapter;
import com.example.anonymous.librarian.IssueToyOnClickListeners.IssueToyPhaseOneOnItemClickListener;
import com.example.anonymous.librarian.IssueToyPhaseOne;
import com.example.anonymous.librarian.R;

/**
 * Created by ANONYMOUS on 23-Nov-17.
 */

public class IssueToyPhaseOneViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView toyName, toyId;
    IssueToyPhaseOneOnItemClickListener itemClickListener;

    public IssueToyPhaseOneViewHolder(View itemView) {
        super(itemView);

        this.toyName = itemView.findViewById(R.id.book_name);
        this.toyId = itemView.findViewById(R.id.book_id);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick(view, getLayoutPosition());
    }

    public void setItemClickListener(IssueToyPhaseOneOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
