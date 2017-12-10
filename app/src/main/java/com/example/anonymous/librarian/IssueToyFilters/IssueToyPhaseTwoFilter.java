package com.example.anonymous.librarian.IssueToyFilters;

import android.widget.Filter;

import com.example.anonymous.librarian.IssueToyAdapters.IssueToyPhaseTwoAdapter;
import com.example.anonymous.librarian.IssueToyPhaseTwo;
import com.example.anonymous.librarian.SubscriberAnalysis;
import com.example.anonymous.librarian.Subscribers;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 23-Nov-17.
 */

public class IssueToyPhaseTwoFilter extends Filter {

    IssueToyPhaseTwoAdapter adapter;
    ArrayList<Subscribers> oldList;

    public IssueToyPhaseTwoFilter(IssueToyPhaseTwoAdapter adapter, ArrayList<Subscribers> oldList) {
        this.adapter = adapter;
        this.oldList = oldList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {

        FilterResults results = new FilterResults();

        if(charSequence.length() > 0 && charSequence != null){

            charSequence = charSequence.toString().toUpperCase();
            ArrayList<Subscribers> newList = new ArrayList<>();
            for(int i = 0; i < oldList.size(); i++){

                if(oldList.get(i).getmSubscriberName().toUpperCase().contains(charSequence) || oldList.get(i).getmSubscriberId().toUpperCase().contains(charSequence)){
                    newList.add(oldList.get(i));
                }

            }

            results.count = newList.size();
            results.values = newList;

        } else {
            results.count = oldList.size();
            results.values = oldList;
        }

        return results;

    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        adapter.oldList = (ArrayList<Subscribers>) filterResults.values;

        adapter.notifyDataSetChanged();

    }
}
