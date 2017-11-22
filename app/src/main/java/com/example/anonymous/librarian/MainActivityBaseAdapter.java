package com.example.anonymous.librarian;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 21-Nov-17.
 */

public class MainActivityBaseAdapter extends BaseAdapter {

    Context context;
    ArrayList<MainActivityListViewItems> items;

    public MainActivityBaseAdapter(Context context, ArrayList<MainActivityListViewItems> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.main_activity_card_view, viewGroup, false);
        }

        final MainActivityListViewItems item = items.get(i);

        ImageView buttonImage = view.findViewById(R.id.button_image_view);
        buttonImage.setImageResource(item.getmButtonImageSource());

        TextView buttonName = view.findViewById(R.id.button_name_text_view);
        buttonName.setText(item.getmButtonName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String itemClicked = item.getmButtonName();

                if(itemClicked.toLowerCase().contains("issue a book")){

                    Intent toIssueBookPhaseOne = new Intent(context, IssueBookPhaseOne.class);
                    toIssueBookPhaseOne.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(toIssueBookPhaseOne);

                } else if(itemClicked.toLowerCase().contains("register returned book")){

                    Intent toReturnBook = new Intent(context, ReturnBook.class);
                    toReturnBook.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(toReturnBook);

                } else if(itemClicked.toLowerCase().contains("view subscribers details")){

                    Intent toViewSubscribers = new Intent(context, ViewSubscribers.class);
                    toViewSubscribers.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(toViewSubscribers);

                } else if(itemClicked.toLowerCase().contains("view currently issued books")){

                    Intent toViewIssuedBooks = new Intent(context, ViewCurrentlyIssuedBooks.class);
                    toViewIssuedBooks.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(toViewIssuedBooks);

                }

            }
        });

        return view;
    }
}