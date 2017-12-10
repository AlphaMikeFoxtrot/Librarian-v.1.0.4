package com.example.anonymous.librarian.IssueBookFilters;

import android.widget.Filter;

import com.example.anonymous.librarian.Subscribers;
import com.example.anonymous.librarian.IssueBookAdapter.IssueBookPhaseOneAdapter;
import com.example.anonymous.librarian.IssueBookAdapter.IssueBookPhaseTwoAdapter;
import com.example.anonymous.librarian.Subscribers;

import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 22-Nov-17.
 */

public class IssueBookPhaseTwoFilter extends Filter {

    IssueBookPhaseTwoAdapter adapter;
    ArrayList<Subscribers> oldList;

    public IssueBookPhaseTwoFilter(IssueBookPhaseTwoAdapter adapter, ArrayList<Subscribers> oldList) {
        this.adapter = adapter;
        this.oldList = oldList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {

        FilterResults results = new FilterResults();

        if(charSequence != null && charSequence.length() > 0){
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

//    IssueBookPhaseTwoAdapter adapter;
//    ArrayList<Subscribers> oldList;
//
//    public IssueBookPhaseTwoFilter(IssueBookPhaseTwoAdapter adapter, ArrayList<Subscribers> oldList) {
//        this.adapter = adapter;
//        this.oldList = oldList;
//    }
//
//    @Override
//    protected FilterResults performFiltering(CharSequence charSequence) {
//
//        FilterResults result = new FilterResults();
//
//        if(charSequence != null && charSequence.length() > 0){
//
//            charSequence = charSequence.toString().toUpperCase();
//
//            ArrayList<Subscribers> newList = new ArrayList<>();
//
//            for(int i = 0; i < oldList.size(); i++){
//                if(oldList.get(i).getmSubscriberName().toUpperCase().contains(charSequence)){
//                    newList.add(oldList.get(i));
//                }
//            }
//
//            result.count = newList.size();
//            result.values = newList;
//
//        } else {
//
//            result.count = oldList.size();
//            result.values = oldList;
//
//        }
//
//        return result;
//    }
//
//    @Override
//    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//
//        adapter.oldList = (ArrayList<Subscribers>) filterResults.values;
//
//        adapter.notifyDataSetChanged();
//
//    }

// }
