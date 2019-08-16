package com.aviharez.tomatapp.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aviharez.tomatapp.R;
import com.aviharez.tomatapp.activity.DetailSmartActivity;
import com.aviharez.tomatapp.activity.SearchableListDialog;
import com.aviharez.tomatapp.activity.SearchableSpinner;
import com.aviharez.tomatapp.models.Global;
import com.aviharez.tomatapp.models.PlantModel;
import com.aviharez.tomatapp.models.SmartCatModel;
import customfonts.EditText__SF_Pro_Display_Regular;
import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmartCatFragment extends Fragment {

    private Dialog dialog;
    private NiceSpinner spinnerPlant;
    public SearchableSpinner spinner_inc, spinner_ncs, spinner_manufacturer, spinner_equipment;
    public EditText__SF_Pro_Display_Regular newStockCode, oldStockCode, partNum;
    private Button bt_search;
    ArrayList<PlantModel> incsModel;
    ArrayList<PlantModel> ncsModel;
    ArrayList<PlantModel> manufacturerModel;
    ArrayList<PlantModel> equipmentModel;


    public SmartCatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_smart_cat, container, false);

        spinnerPlant = v.findViewById(R.id.spinnerPlant);
        spinner_equipment = (SearchableSpinner)v.findViewById(R.id.spinner_equipment);
        spinner_ncs = (SearchableSpinner)v.findViewById(R.id.spinner_ncs);
        spinner_manufacturer = (SearchableSpinner)v.findViewById(R.id.spinner_manufacturer);
        spinner_inc = (SearchableSpinner)v.findViewById(R.id.spinner_inc);
        newStockCode = v.findViewById(R.id.new_stock_code);
        oldStockCode = v.findViewById(R.id.old_stock_code);
        partNum = v.findViewById(R.id.part_number);

        incsModel = new ArrayList<>();
        ncsModel = new ArrayList<>();
        manufacturerModel = new ArrayList<>();
        equipmentModel = new ArrayList<>();

        setupDialog();
        grabPlantList();

        List<String> list = new ArrayList<>();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.my_spinner, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_inc.setAdapter(dataAdapter);
        spinner_inc.setOnSearchTextChangedListener(new SearchableListDialog.OnSearchTextChanged() {
            @Override
            public void onSearchTextChanged(String strText) {
                if(strText.length()>=3){
                    selectIncs(strText);
                }
            }
        });

        spinner_ncs.setAdapter(dataAdapter);
        spinner_ncs.setOnSearchTextChangedListener(new SearchableListDialog.OnSearchTextChanged() {
            @Override
            public void onSearchTextChanged(String strText) {
                if(strText.length() >= 3){
                    selectNcs(strText);
                }
            }
        });

        spinner_manufacturer.setAdapter(dataAdapter);
        spinner_manufacturer.setOnSearchTextChangedListener(new SearchableListDialog.OnSearchTextChanged() {
            @Override
            public void onSearchTextChanged(String strText) {
                if(strText.length() >= 3){
                    selectManufacturers(strText);
                }
            }
        });

        spinner_equipment.setAdapter(dataAdapter);
        spinner_equipment.setOnSearchTextChangedListener(new SearchableListDialog.OnSearchTextChanged() {
            @Override
            public void onSearchTextChanged(String strText) {
                if(strText.length() >= 3){
                    selectEquipment(strText);
                }
            }
        });
//        List<String> dataset = new LinkedList<>(Arrays.asList("One", "Two", "Three", "Four", "Five"));
//
//        spinnerPlant.attachDataSource(dataset);
//        nice_spinner2.attachDataSource(dataset);
//        nice_spinner3.attachDataSource(dataset);
//        nice_spinner4.attachDataSource(dataset);
//        nice_spinner5.attachDataSource(dataset);

        bt_search = v.findViewById(R.id.bt_search_smart);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMaterialOnSmartCat();
//                Intent i = new Intent(getActivity(), DetailSmartActivity.class);
//                startActivity(i);
            }
        });

        return v;
    }

    public void setupDialog(){
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);
    }

    private void showDialog(){
        if(!dialog.isShowing())
            dialog.show();
    }

    private void hideDialog(){
        if(dialog.isShowing())
            dialog.dismiss();
    }

    public void grabPlantList(){
        //with API
        showDialog();
        String url = Global.getInstance().getBaseUrl()+"api/smartcat-master_plant.php?action=view&q=";//3d03d68c25e760eba562a8b1db568504";
        Global.getInstance().handleSSLConn();
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray ja = response.getJSONArray("data");
                            List<String> list = new ArrayList<String>();
                            ArrayList<PlantModel> plantModels = new ArrayList<>();
                            list.add("-");
                            PlantModel pm = new PlantModel("-", "-");
                            plantModels.add(pm);
                            for(int i=0; i<ja.length(); i++){
                                JSONObject c = ja.getJSONObject(i);
                                list.add(c.getString("text"));
                                pm = new PlantModel(c.getString("id"), c.getString("text"));
                                plantModels.add(pm);
                            }
                            Global.getInstance().setPlantModel(plantModels);
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                                    R.layout.my_spinner, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerPlant.setAdapter(dataAdapter);
                            hideDialog();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            hideDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                        hideDialog();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("X-CSRFToken", Global.getInstance().getCsrf());
                headers.put("X-Tomat-API", "1");
                return headers;
            }
        };
        jsonReq.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonReq);
    }

    public void selectIncs(String txtSearch){
        //with API
        showDialog();
        String url = Global.getInstance().getBaseUrl()+"api/smartcat-master_incs.php?action=view&q="+txtSearch;
        Global.getInstance().handleSSLConn();
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray ja = response.getJSONArray("data");
                            List<String> list = new ArrayList<String>();
                            incsModel = new ArrayList<>();
                            list.add("-");
                            PlantModel pm = new PlantModel("-", "-");
                            incsModel.add(pm);
                            for(int i=0; i<ja.length(); i++){
                                JSONObject c = ja.getJSONObject(i);
                                list.add(c.getString("text"));
                                pm = new PlantModel(c.getString("id"), c.getString("text"));
                                incsModel.add(pm);
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                                    R.layout.my_spinner, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_inc.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
                            spinner_inc.refreshListDialog();
                            hideDialog();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            hideDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                        hideDialog();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("X-CSRFToken", Global.getInstance().getCsrf());
                headers.put("X-Tomat-API", "1");
                return headers;
            }
        };
        jsonReq.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonReq);
    }

    public void selectNcs(String txtSearch){
        //with API
        showDialog();
        String url = Global.getInstance().getBaseUrl()+"api/smartcat-master_group_class.php?action=view&q="+txtSearch;
        Global.getInstance().handleSSLConn();
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray ja = response.getJSONArray("data");
                            List<String> list = new ArrayList<String>();
                            ncsModel = new ArrayList<>();
                            list.add("-");
                            PlantModel pm = new PlantModel("-", "-");
                            ncsModel.add(pm);
                            for(int i=0; i<ja.length(); i++){
                                JSONObject c = ja.getJSONObject(i);
                                list.add(c.getString("text"));
                                pm = new PlantModel(c.getString("id"), c.getString("text"));
                                ncsModel.add(pm);
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                                    R.layout.my_spinner, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_ncs.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
                            spinner_ncs.refreshListDialog();
                            hideDialog();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            hideDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                        hideDialog();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("X-CSRFToken", Global.getInstance().getCsrf());
                headers.put("X-Tomat-API", "1");
                return headers;
            }
        };
        jsonReq.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonReq);
    }

    public void selectManufacturers(String txtSearch){
        //with API
        showDialog();
        String url = Global.getInstance().getBaseUrl()+"api/smartcat-master_manufacturers.php?action=view&q="+txtSearch;
        Global.getInstance().handleSSLConn();
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray ja = response.getJSONArray("data");
                            List<String> list = new ArrayList<String>();
                            manufacturerModel = new ArrayList<>();
                            list.add("-");
                            PlantModel pm = new PlantModel("-", "-");
                            manufacturerModel.add(pm);
                            for(int i=0; i<ja.length(); i++){
                                JSONObject c = ja.getJSONObject(i);
                                list.add(c.getString("text"));
                                pm = new PlantModel(c.getString("id"), c.getString("text"));
                                manufacturerModel.add(pm);
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                                    R.layout.my_spinner, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_manufacturer.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
                            spinner_manufacturer.refreshListDialog();
                            hideDialog();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            hideDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                        hideDialog();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("X-CSRFToken", Global.getInstance().getCsrf());
                headers.put("X-Tomat-API", "1");
                return headers;
            }
        };
        jsonReq.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonReq);
    }

    public void selectEquipment(String txtSearch){
        //with API
        showDialog();
        String url = Global.getInstance().getBaseUrl()+"api/smartcat-master_equipment.php?action=view&q="+txtSearch;
        Global.getInstance().handleSSLConn();
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray ja = response.getJSONArray("data");
                            List<String> list = new ArrayList<String>();
                            equipmentModel = new ArrayList<>();
                            list.add("-");
                            PlantModel pm = new PlantModel("-", "-");
                            equipmentModel.add(pm);
                            for(int i=0; i<ja.length(); i++){
                                JSONObject c = ja.getJSONObject(i);
                                list.add(c.getString("text"));
                                pm = new PlantModel(c.getString("id"), c.getString("text"));
                                equipmentModel.add(pm);
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                                    R.layout.my_spinner, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_equipment.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
                            spinner_equipment.refreshListDialog();
                            hideDialog();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            hideDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                        hideDialog();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("X-CSRFToken", Global.getInstance().getCsrf());
                headers.put("X-Tomat-API", "1");
                return headers;
            }
        };
        jsonReq.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonReq);
    }

    public void searchMaterialOnSmartCat(){
        //with API
        final int idxPlant = spinnerPlant.getSelectedIndex();//spinnerPlant.getSelectedItemPosition();
        if((idxPlant==0) && (newStockCode.getText().toString().length()==0)
                && (oldStockCode.getText().toString().length()==0) && (partNum.getText().toString().length()==0)
                && (incsModel==null) && (ncsModel==null)
                && (manufacturerModel==null) && (equipmentModel==null)){
            Toast.makeText(getActivity(), "Minimal Plant yang harus diisi", Toast.LENGTH_LONG).show();
            return;
        }
        showDialog();
        String url = Global.getInstance().getBaseUrl()+"api/smartcat-live_search.php?action=view";
        Global.getInstance().handleSSLConn();
        StringRequest jsonReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            String res = response;
                            JSONObject obj = new JSONObject(res);
                            Global.getInstance().setResponseSearchSmartCat(obj);
                            JSONObject pagination = obj.getJSONObject("pagination");
                            //remember total page
                            Global.getInstance().setTotalPageSmartCat(pagination.getInt("total_pages"));
                            //remember current page
                            Global.getInstance().setCurrentPageSmartCat(pagination.getInt("page"));
                            JSONArray data = obj.getJSONArray("data");
                            JSONArray keys = obj.getJSONArray("keys");
                            HashMap<String, Integer> keyMap = new HashMap<>();
                            JSONArray columns = obj.getJSONArray("columns");
                            ArrayList<String> columnArr = new ArrayList<>();

                            //collect keys
                            keyMap.put("plant", keys.getJSONObject(0).getInt("plant"));
                            keyMap.put("old_stock_code", keys.getJSONObject(1).getInt("old_stock_code"));
                            keyMap.put("new_stock_code", keys.getJSONObject(2).getInt("new_stock_code"));
                            keyMap.put("status", keys.getJSONObject(3).getInt("status"));
                            keyMap.put("description", keys.getJSONObject(4).getInt("description"));
                            Global.getInstance().keysSmartCat = keyMap;

                            for (int i=0; i<columns.length(); i++){
                                JSONObject o = columns.getJSONObject(i);
                                columnArr.add(o.getString("title"));
                            }
                            Global.getInstance().setColumnsSmartCat(columnArr);
                            ArrayList<SmartCatModel> arrSmartCatModel = new ArrayList<>();
                            for(int x=0; x<data.length(); x++){
                                JSONArray jsonArray = data.getJSONArray(x);
                                ArrayList<String> content = new ArrayList<>();
                                SmartCatModel smartCatModel = new SmartCatModel();
                                for (int y=0; y<jsonArray.length(); y++){
                                    content.add(jsonArray.getString(y));
                                }
                                smartCatModel.setContentList(content);
                                arrSmartCatModel.add(smartCatModel);
                            }
                            if(arrSmartCatModel.size()>0){
                                Global.getInstance().setSmartCatModel(arrSmartCatModel);
                                Intent intent = new Intent(getContext(), DetailSmartActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getContext(), "Tidak ada data!", Toast.LENGTH_SHORT).show();
                            }
                            hideDialog();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            hideDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                        hideDialog();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                PlantModel pm = Global.getInstance().getPlantModel().get(idxPlant);
                params.put("page", "0");
                Global.getInstance().selectedPlantIdSmartCat = pm.getPlantId();
                if(idxPlant != 0){
                    params.put("plant", pm.getPlantId());
                }
                if(newStockCode.getText().toString().length()>0){
                    params.put("new_stock_code", newStockCode.getText().toString());
                    Global.getInstance().selectedStockCodeSmartCat = newStockCode.getText().toString();
                }
                if(oldStockCode.getText().toString().length()>0){
                    params.put("old_stock_code", oldStockCode.getText().toString());
                    Global.getInstance().selectedOldStockCodeSmartCat = oldStockCode.getText().toString();
                }
                if(incsModel!=null){
                    int idxSelectedIncs = spinner_inc.getSelectedItemPosition();
                    if(idxSelectedIncs > -1 || idxSelectedIncs==0){
                        pm = incsModel.get(idxSelectedIncs);
                        Global.getInstance().selectedIncSmartCat = pm.getPlantId();
                    }
                    if(idxSelectedIncs > 0) {
                        pm = incsModel.get(idxSelectedIncs);
                        Global.getInstance().selectedIncSmartCat = pm.getPlantId();
                        params.put("inc", pm.getPlantId());
                    }
                }
                if(ncsModel != null){
                    int idxSelectedNcs = spinner_ncs.getSelectedItemPosition();
                    if(idxSelectedNcs > -1 || idxSelectedNcs==0){
                        pm = ncsModel.get(idxSelectedNcs);
                        Global.getInstance().selectedGroupClassSmartCat = pm.getPlantId();
                    }
                    if(idxSelectedNcs > 0) {
                        pm = ncsModel.get(idxSelectedNcs);
                        Global.getInstance().selectedGroupClassSmartCat = pm.getPlantId();
                        params.put("group_class", pm.getPlantId());
                    }
                }
                if(manufacturerModel != null){
                    int idxManufacturer = spinner_manufacturer.getSelectedItemPosition();
                    if(idxManufacturer >-1 || idxManufacturer==0){
                        pm = manufacturerModel.get(idxManufacturer);
                        Global.getInstance().selectedManufacturerSmartCat = pm.getPlantId();
                    }
                    if(idxManufacturer > 0) {
                        pm = manufacturerModel.get(idxManufacturer);
                        Global.getInstance().selectedManufacturerSmartCat = pm.getPlantId();
                        params.put("manufacturer", pm.getPlantId());
                    }
                }
                if(equipmentModel != null){
                    int idxEquipment = spinner_equipment.getSelectedItemPosition();
                    if(idxEquipment > -1 || idxEquipment==0){
                        pm = equipmentModel.get(idxEquipment);
                        Global.getInstance().selectedEquipmentSmartCat = pm.getPlantId();
                    }
                    if(idxEquipment > 0) {
                        pm = equipmentModel.get(idxEquipment);
                        Global.getInstance().selectedEquipmentSmartCat = pm.getPlantId();
                        params.put("equipment", pm.getPlantId());
                    }
                }
                if(partNum.getText().toString().length()>0){
                    params.put("part_number", partNum.getText().toString());
                    Global.getInstance().selectedPartNumSmartCat = partNum.getText().toString();
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("X-CSRFToken", Global.getInstance().getCsrf());
                headers.put("X-Tomat-API", "1");

                return headers;
            }
        };
        jsonReq.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonReq);
    }

}
