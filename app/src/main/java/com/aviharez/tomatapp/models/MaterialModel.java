package com.aviharez.tomatapp.models;

import java.util.ArrayList;

/**
 * Created by Admin on 12/5/2017.
 */

public class MaterialModel {
    private String matNo;
    private String oldNo;
    private String bun;
    private String deskripsi;
    private String poText;
    private String dateAdded;
    private String sLoc;
    private ArrayList<StockModel> stocks;
    private int unRestricted;
    private int uU;
    private String matType;

    public MaterialModel(){}

    public MaterialModel(String matno, String oldno, String bun, String desc, String potext, String dateAdded, String matType, ArrayList<StockModel> stocks, int uU){
        this.matNo = matno;
        this.oldNo = oldno;
        this.bun = bun;
        this.deskripsi = desc;
        this.poText = potext;
        this.dateAdded = dateAdded;
        this.matType = matType;
        this.stocks = stocks;
        this.uU = uU;
    }

    public String getMatNo() {
        return matNo;
    }

    public void setMatNo(String matNo) {
        this.matNo = matNo;
    }

    public String getOldNo() {
        return oldNo;
    }

    public void setOldNo(String oldNo) {
        this.oldNo = oldNo;
    }

    public String getBun() {
        return bun;
    }

    public void setBun(String bun) {
        this.bun = bun;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getPoText() {
        return poText;
    }

    public void setPoText(String poText) {
        this.poText = poText;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
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

    public String getMatType() {
        return matType;
    }

    public void setMatType(String matType) {
        this.matType = matType;
    }

    public ArrayList<StockModel> getStocks() {
        return stocks;
    }

    public void setStocks(ArrayList<StockModel> stocks) {
        this.stocks = stocks;
    }

    public int getuU() {
        return uU;
    }

    public void setuU(int uU) {
        this.uU = uU;
    }

}
