package com.aviharez.tomatapp.models;

/**
 * Created by Admin on 12/6/2017.
 */

public class StockModel {
    String sLoc;
    int unRestricted;

    public StockModel(String sLoc, int unRestricted){
        this.sLoc = sLoc;
        this.unRestricted = unRestricted;
    }

    public String getsLoc() {
        return sLoc;
    }

    public void setsLoc(String sLoc) {
        this.sLoc = sLoc;
    }

    public int getUnRestricted() {
        return unRestricted;
    }

    public void setUnRestricted(int unRestricted) {
        this.unRestricted = unRestricted;
    }
}
