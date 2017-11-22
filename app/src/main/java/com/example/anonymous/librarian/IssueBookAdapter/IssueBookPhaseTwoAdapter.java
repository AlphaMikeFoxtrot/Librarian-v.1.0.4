package com.example.anonymous.librarian.IssueBookAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.anonymous.librarian.Books;
import com.example.anonymous.librarian.IssueBookFilters.IssueBookPhaseTwoFilter;
import com.example.anonymous.librarian.IssueBookFinalPhase;
import com.example.anonymous.librarian.IssueBookHolders.IssueBookPhaseTwoHolder;
import com.example.anonymous.librarian.IssueBookOnClickListeners.IssueBookPhaseTwoOnItemClickListener;
import com.example.anonymous.librarian.IssueBookPhaseOne;
import com.example.anonymous.librarian.R;
import com.example.anonymous.librarian.Subscribers;

import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 22-Nov-17.
 */

public class IssueBookPhaseTwoAdapter extends RecyclerView.Adapter<IssueBookPhaseTwoHolder> implements Filterable{

    Context context;
    public ArrayList<Subscribers> oldList, newList;
    IssueBookPhaseTwoFilter filter;
    IssueBookPhaseTwoAdapter adapter;

    public IssueBookPhaseTwoAdapter(Context context, ArrayList<Subscribers> oldList) {
        this.context = context;
        this.oldList = oldList;
        this.newList = oldList;
    }

    @Override
    public IssueBookPhaseTwoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.issued_book_one_custom_list_view, parent, false);

        return new IssueBookPhaseTwoHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(IssueBookPhaseTwoHolder holder, int position) {

        Subscribers currentSubscriber = oldList.get(position);

        holder.subscriberId.setText(currentSubscriber.getmSubscriberId());
        holder.subscriberName.setText(currentSubscriber.getmSubscriberName());

        holder.setItemClickListener(new IssueBookPhaseTwoOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent toFinalPhase = new Intent(context, IssueBookFinalPhase.class);
                toFinalPhase.putExtra("subscriberId", oldList.get(position).getmSubscriberId());
                toFinalPhase.putExtra("subscriberName", oldList.get(position).getmSubscriberName());
                toFinalPhase.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(toFinalPhase);
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
            filter = new IssueBookPhaseTwoFilter(this, oldList);
        }

        return filter;
    }
}
