package com.example.anonymous.librarian;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 17-Nov-17.
 */

public class SubscriberAnalysisAdapter extends ArrayAdapter {

    public SubscriberAnalysisAdapter(@NonNull Context context, ArrayList<SubscriberAnalysis> resource) {
        super(context, 0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscriber_analysis_custom_list_view, parent, false);
        }

        SubscriberAnalysis currentMonth = (SubscriberAnalysis) getItem(position);

        TextView month = listItemView.findViewById(R.id.subscriber_analysis_month);
        month.setText(currentMonth.getmMonthOfAnalysis());

        return listItemView;

    }
}
