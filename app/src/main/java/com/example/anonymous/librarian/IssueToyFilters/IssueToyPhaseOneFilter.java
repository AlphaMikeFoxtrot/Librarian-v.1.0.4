package com.example.anonymous.librarian.IssueToyFilters;

import android.widget.Filter;

import com.example.anonymous.librarian.IssueToyAdapters.IssueToyPhaseOneAdapter;
import com.example.anonymous.librarian.IssueToyPhaseOne;
import com.example.anonymous.librarian.Toys;

import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 23-Nov-17.
 */

public class IssueToyPhaseOneFilter extends Filter {

    IssueToyPhaseOneAdapter adapter;
    ArrayList<Toys> oldList;

    public IssueToyPhaseOneFilter(IssueToyPhaseOneAdapter adapter, ArrayList<Toys> oldList) {
        this.adapter = adapter;
        this.oldList = oldList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();

        if(charSequence != null && charSequence.length() > 0){

            charSequence = charSequence.toString().toUpperCase();
            ArrayList<Toys> newList = new ArrayList<>();
            for(int i = 0; i < oldList.size(); i++){

                if(oldList.get(i).getmToyId().toUpperCase().contains(charSequence) || oldList.get(i).getmToyName().toUpperCase().contains(charSequence)){
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

        adapter.oldList = (ArrayList<Toys>) filterResults.values;
        adapter.notifyDataSetChanged();

    }
}
