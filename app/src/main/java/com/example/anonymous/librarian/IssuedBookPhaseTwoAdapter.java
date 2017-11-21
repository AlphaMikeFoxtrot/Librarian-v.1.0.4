package com.example.anonymous.librarian;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 12-Nov-17.
 */

public class IssuedBookPhaseTwoAdapter extends RecyclerView.Adapter<IssuedBookPhaseTwoAdapter.IssuedBookPhaseTwoViewHolder> {

    Context context;
    ArrayList<Subscribers> mSubscribers;

    final String ID = "ID : ";

    public IssuedBookPhaseTwoAdapter(Context context, ArrayList<Subscribers> mSubscribers) {
        this.context = context;
        this.mSubscribers = mSubscribers;
    }

    @Override
    public IssuedBookPhaseTwoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.issued_book_one_custom_list_view, parent, false);

        return new IssuedBookPhaseTwoViewHolder(listItemView, this.context, this.mSubscribers);
    }

    @Override
    public void onBindViewHolder(IssuedBookPhaseTwoViewHolder holder, int position) {

        Subscribers currentBook = mSubscribers.get(position);

        holder.mBookName.setText(currentBook.getmSubscriberName());
        holder.mBookId.setText(currentBook.getmSubscriberId());

    }

    @Override
    public int getItemCount() {
        return mSubscribers.size();
    }

    public class IssuedBookPhaseTwoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mBookName, mBookId;
        Context context;
        ArrayList<Subscribers> subscribers;

        public IssuedBookPhaseTwoViewHolder(View itemView, Context context, ArrayList<Subscribers> subscribers) {
            super(itemView);

            this.context = context;
            this.subscribers = subscribers;

            itemView.setOnClickListener(this);

            mBookName = itemView.findViewById(R.id.book_name);
            mBookId = itemView.findViewById(R.id.book_id);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            Subscribers clickedSubscriber = subscribers.get(position);

            String subscriberName = clickedSubscriber.getmSubscriberName();
            String subscriberId = clickedSubscriber.getmSubscriberId();

            Intent toFinalPhase = new Intent(context, IssueBookFinalPhase.class);
            toFinalPhase.putExtra("subscriberName", subscriberName);
            toFinalPhase.putExtra("subscriberId", subscriberId);
            toFinalPhase.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.context.startActivity(toFinalPhase);

        }
    }
    public void setFilter(ArrayList<Subscribers> newList){
        mSubscribers = new ArrayList<>();
        mSubscribers.addAll(newList);
        notifyDataSetChanged();
    }

}
