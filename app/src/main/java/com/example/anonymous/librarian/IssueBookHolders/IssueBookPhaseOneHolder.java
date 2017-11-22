package com.example.anonymous.librarian.IssueBookHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.anonymous.librarian.IssueBookOnClickListeners.IssueBookPhaseOneOnItemClickListener;
import com.example.anonymous.librarian.R;

/**
 * Created by ANONYMOUS on 22-Nov-17.
 */

public class IssueBookPhaseOneHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView bookName, bookId;
    IssueBookPhaseOneOnItemClickListener itemClickListener;

    public IssueBookPhaseOneHolder(View itemView) {
        super(itemView);

        this.bookName = itemView.findViewById(R.id.book_name);
        this.bookId = itemView.findViewById(R.id.book_id);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick(view, getLayoutPosition());
    }

    public void setItemClickListener(IssueBookPhaseOneOnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
