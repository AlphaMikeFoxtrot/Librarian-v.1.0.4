package com.example.anonymous.librarian.IssueToyAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.example.anonymous.librarian.IssueBookHolders.IssueBookPhaseOneHolder;
import com.example.anonymous.librarian.IssueBookHolders.IssueBookPhaseTwoHolder;
import com.example.anonymous.librarian.IssueToyFilters.IssueToyPhaseTwoFilter;
import com.example.anonymous.librarian.IssueToyHolders.IssueToyPhaseTwoViewHolder;
import com.example.anonymous.librarian.IssueToyOnClickListeners.IssueToyPhaseTwoOnItemClickListener;
import com.example.anonymous.librarian.IssueToyPhaseOne;
import com.example.anonymous.librarian.IssueToyPhaseTwo;
import com.example.anonymous.librarian.R;
import com.example.anonymous.librarian.Subscribers;
import com.example.anonymous.librarian.Toys;

import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 23-Nov-17.
 */

public class IssueToyPhaseTwoAdapter extends RecyclerView.Adapter<IssueToyPhaseTwoViewHolder> implements Filterable{

    Context context;
    public ArrayList<Subscribers> oldList, newList;
    IssueToyPhaseTwoFilter filter;

    public IssueToyPhaseTwoAdapter(Context context, ArrayList<Subscribers> oldList) {
        this.context = context;
        this.oldList = oldList;
        this.newList = oldList;
    }


    @Override
    public IssueToyPhaseTwoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(context).inflate(R.layout.issued_book_one_custom_list_view, parent, false);

        return new IssueToyPhaseTwoViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(IssueToyPhaseTwoViewHolder holder, int position) {

        holder.subscriberId.setText(oldList.get(position).getmSubscriberId());
        holder.subscriberName.setText(oldList.get(position).getmSubscriberName());

        holder.setItemClickListener(new IssueToyPhaseTwoOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(context, "name\t" + oldList.get(position).getmSubscriberName() + "\nid\t" + oldList.get(position).getmSubscriberId(), Toast.LENGTH_SHORT).show();
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
            filter = new IssueToyPhaseTwoFilter(this, oldList);
        }

        return filter;
    }
}
