package com.aviharez.tomatapp.models;

import android.annotation.SuppressLint;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 10/2/2017.
 */

public class Global {
    public static Global obj;
    public static String material;
    public static String dateAdded;
    public static String BUn;
    public static String deskripsi;
    public static String csrf;
    public static String username;
    public static String name;
    public static String email;
    public static String profilePict;
    public static ArrayList<MaterialModel> matModel;
    public static ArrayList<PlantModel> plantModel;
    public static ArrayList<SmartCatModel> smartCatModel;
    public static JSONObject responseSearchSmartCat;
    public static ArrayList<String> columnsSmartCat;
    public static int totalPageSmartCat;
    public static int totalPageTomat;
    public static int currentPageSmartCat;
    public static int currentPageTomat;
    //params selected in SmartCat Search
    public static String selectedPlantIdSmartCat = "";
    public static String selectedStockCodeSmartCat = "";
    public static String selectedOldStockCodeSmartCat = "";
    public static String selectedIncSmartCat = "";
    public static String selectedGroupClassSmartCat = "";
    public static String selectedManufacturerSmartCat = "";
    public static String selectedEquipmentSmartCat = "";
    public static String selectedPartNumSmartCat = "";
    public static HashMap<String, Integer> keysSmartCat;

    //params selected in Tomat
    public static String selectedMaterialTomat = "";
    public static String selectedOldMaterialNoTomat = "";
    public static String selectedPoTextTomat = "";
    public static String selectedDescTomat = "";
    public static String selectedPlantIdTomat = "";
    public static String selectedStockAvailabilityTomat = "";
    public static JSONObject objDetailTomat;
    public static HashMap<String, Integer> keysTomat;

    public static final String BASE_URL = "http://intranet.pupuk-kujang.co.id/tomat/";
    public static final String BASE_URL2 = "https://sikd.pupuk-kujang.co.id/dashboard-android/pages/";

    public Global(){}

    public static Global getInstance(){
        if(obj == null){
            obj = new Global();
        }
        return obj;
    }

    public void setNoMaterial(String mat){
        this.material = mat;
    }

    public String getNoMaterial(){
        return this.material;
    }

    public void setDateAddedMaterial(String dateAdded){
        this.dateAdded = dateAdded;
    }

    public String getDateAdded(){
        return this.dateAdded;
    }

    public void setBUn(String BUn){
        this.BUn = BUn;
    }

    public String getBUn(){
        return this.BUn;
    }

    public void setDeskripsi(String deskripsi){
        this.deskripsi = deskripsi;
    }

    public String getDeskripsi(){
        return this.deskripsi;
    }

    public void setCsrf(String csrf){
        this.csrf = csrf;
    }

    public String getCsrf(){
        return this.csrf;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public void setProfilePict(String profilePict){
        this.profilePict = profilePict;
    }

    public String getProfilePict(){
        return profilePict;
    }

    public void setMatModel(ArrayList<MaterialModel> matModel){
        this.matModel = matModel;
    }

    public ArrayList<MaterialModel> getMatModel(){
        return matModel;
    }

    public ArrayList<PlantModel> getPlantModel(){
        return plantModel;
    }

    public void setPlantModel(ArrayList<PlantModel> plantModel){
        this.plantModel = plantModel;
    }

    public void setTotalPageSmartCat(int totalPageSmartCat){
        Global.totalPageSmartCat = totalPageSmartCat;
    }

    public int getTotalPageSmartCat(){
        return totalPageSmartCat;
    }

    public void setTotalPageTomat(int totalPageTomat){
        Global.totalPageTomat = totalPageTomat;
    }

    public int getTotalPageTomat(){
        return totalPageTomat;
    }

    public void setCurrentPageSmartCat(int currentPageSmartCat){
        Global.currentPageSmartCat = currentPageSmartCat;
    }

    public int getCurrentPageSmartCat(){
        return currentPageSmartCat;
    }

    public void setCurrentPageTomat(int currentPageTomat){
        Global.currentPageTomat = currentPageTomat;
    }

    public int getCurrentPageTomat(){
        return currentPageTomat;
    }

    public String getBaseUrl(){
        return this.BASE_URL;
    }

    public String getBaseUrl2(){
        return this.BASE_URL2;
    }

    public void setResponseSearchSmartCat(JSONObject responseSearchSmartCat){
        Global.responseSearchSmartCat = responseSearchSmartCat;
    }

    public JSONObject getResponseSearchSmartCat(){
        return responseSearchSmartCat;
    }

    public void setSmartCatModel(ArrayList<SmartCatModel> smartCatModel){
        Global.smartCatModel = smartCatModel;
    }

    public ArrayList<SmartCatModel> getSmartCatModel(){
        return smartCatModel;
    }

    public void setColumnsSmartCat(ArrayList<String> columnsSmartCat){
        Global.columnsSmartCat = columnsSmartCat;
    }

    public ArrayList<String> getColumnsSmartCat(){
        return columnsSmartCat;
    }

    /**
     * Enables https connections
     */
    @SuppressLint("TrulyRandom")
    public void handleSSLConn(){
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String host, SSLSession arg1) {
                    boolean val = false;
                    if(host.equalsIgnoreCase("sikd.pupuk-kujang.co.id") || host.equalsIgnoreCase("www.pupuk-kujang.co.id") || host.equalsIgnoreCase("pupuk-kujang.co.id") || host.equalsIgnoreCase("www.instagram.com") || host.equalsIgnoreCase("scontent-sin6-2.cdninstagram.com") || host.equalsIgnoreCase("intranet.pupuk-kujang.co.id")){
                        val = true;
                    }
                    return val;
                }
            });
        } catch (Exception ignored) {
        }
    }

    public String myNumberFormatter(Double nomor){
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        return formatter.format(nomor);
    }
}
