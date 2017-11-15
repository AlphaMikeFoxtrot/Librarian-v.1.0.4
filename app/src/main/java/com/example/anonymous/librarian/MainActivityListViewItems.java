package com.example.anonymous.librarian;

/**
 * Created by ANONYMOUS on 11-Nov-17.
 */

public class MainActivityListViewItems {

    private String mButtonName;
    private int mButtonImageSource;

    public MainActivityListViewItems(String mButtonName, int mButtonImageSource) {
        this.mButtonName = mButtonName;
        this.mButtonImageSource = mButtonImageSource;
    }

    public String getmButtonName() {
        return mButtonName;
    }

    public int getmButtonImageSource() {
        return mButtonImageSource;
    }
}
