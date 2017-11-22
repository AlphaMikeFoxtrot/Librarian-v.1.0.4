package com.example.anonymous.librarian.IssueBookHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.anonymous.librarian.IssueBookOnClickListeners.IssueBookPhaseTwoOnItemClickListener;
import com.example.anonymous.librarian.R;

/**
 * Created by ANONYMOUS on 22-Nov-17.
 */

public class IssueBookPhaseTwoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView subscriberId, subscriberName;
    IssueBookPhaseTwoOnItemClickListener itemClickListener;

    public IssueBookPhaseTwoHolder(View itemView) {
        super(itemView);

        this.subscriberId = itemView.findViewById(R.id.book_id);
        this.subscriberName = itemView.findViewById(R.id.book_name);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick(view, getLayoutPosition());
    }

    public void setItemClickListener(IssueBookPhaseTwoOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
