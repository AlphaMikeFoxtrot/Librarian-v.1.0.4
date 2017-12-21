package com.example.anonymous.librarian;

public class Toys {

    private String mToyName, mToyId, issuedOn, issuedTo, issuedToId, addedOn;

    // issuedTo is the name of the person issued to

    public String getmToyName() {
        return mToyName;
    }

    public void setmToyName(String mToyName) {
        this.mToyName = mToyName;
    }

    public String getmToyId() {
        return mToyId;
    }

    public void setmToyId(String mToyId) {
        this.mToyId = mToyId;
    }

    public String getIssuedOn() {
        return issuedOn;
    }

    public void setIssuedOn(String issuedOn) {
        this.issuedOn = issuedOn;
    }

    public String getIssuedTo() {
        return issuedTo;
    }

    public void setIssuedTo(String issuedTo) {
        this.issuedTo = issuedTo;
    }

    public String getIssuedToId() {
        return issuedToId;
    }

    public void setIssuedToId(String issuedToId) {
        this.issuedToId = issuedToId;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }
}
