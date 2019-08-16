package com.aviharez.tomatapp.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.*;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aviharez.tomatapp.R;
import com.aviharez.tomatapp.activity.DetailTomatActivity;
import com.aviharez.tomatapp.models.Global;
import com.aviharez.tomatapp.models.PlantModel;
import com.aviharez.tomatapp.models.SmartCatModel;
import customfonts.EditText__SF_Pro_Display_Regular;
import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class TomatFragment extends Fragment {

    private Dialog dialog;
    private Button bt_search;
    public EditText__SF_Pro_Display_Regular matNo, oldNo, poText, deskripsi;
    public NiceSpinner spinnerPlant;
    CheckBox stockAvailability;
    int isStockAvailable;

    public TomatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tomat, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        matNo = (EditText__SF_Pro_Display_Regular)v.findViewById(R.id.et_material_no);
        oldNo = (EditText__SF_Pro_Display_Regular)v.findViewById(R.id.et_old_no);
        poText = (EditText__SF_Pro_Display_Regular)v.findViewById(R.id.et_po_text);
        deskripsi = (EditText__SF_Pro_Display_Regular)v.findViewById(R.id.et_deskripsi);
        spinnerPlant = (NiceSpinner) v.findViewById(R.id.spinnerPlant);
        stockAvailability = (CheckBox)v.findViewById(R.id.stockAvailability);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);

        //reset form
        matNo.setText("");
        oldNo.setText("");
        poText.setText("");
        deskripsi.setText("");

        grabPlantList();

        isStockAvailable = 0;
        Global.getInstance().selectedStockAvailabilityTomat = "no";
        stockAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    isStockAvailable = 1;
                    Global.getInstance().selectedStockAvailabilityTomat = "yes";
                }
                else{
                    isStockAvailable = 0;
                    Global.getInstance().selectedStockAvailabilityTomat = "no";
                }
            }
        });

        bt_search = v.findViewById(R.id.bt_search_tomat);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idxSelectedPlant = spinnerPlant.getSelectedIndex();
                searchMaterialOnSmartCat(idxSelectedPlant);
            }
        });

        return v;
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
                            JSONArray listObj = new JSONArray();
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

    public void searchMaterialOnSmartCat(final int idxPlantId){
        //with API
        final int idxPlant = idxPlantId;
        if ((matNo.getText().toString().length() == 0) && (oldNo.getText().toString().length() == 0) && (poText.getText().toString().length() == 0) && (idxPlant == 0)){
            Toast.makeText(getActivity(), "Minimal isi salah satu dari inputan ini: Material No, Old No, PO Text, atau Plant", Toast.LENGTH_LONG).show();
            return;
        }
        showDialog();
        String url = Global.getInstance().getBaseUrl()+"api/tomat-live_search.php?action=view";
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
                            Global.getInstance().setTotalPageTomat(pagination.getInt("total_pages"));
                            //remember current page
                            Global.getInstance().setCurrentPageTomat(pagination.getInt("page"));
                            JSONArray data = obj.getJSONArray("data");
                            JSONArray columns = obj.getJSONArray("columns");
                            JSONArray keys = obj.getJSONArray("keys");
                            HashMap<String, Integer> keyMap = new HashMap<>();
                            ArrayList<String> columnArr = new ArrayList<>();

                            //collect keys
                            keyMap.put("plant", keys.getJSONObject(0).getInt("plant"));
                            keyMap.put("old_stock_code", keys.getJSONObject(1).getInt("old_stock_code"));
                            keyMap.put("new_stock_code", keys.getJSONObject(2).getInt("new_stock_code"));
                            keyMap.put("description", keys.getJSONObject(3).getInt("description"));
                            Global.getInstance().keysTomat = keyMap;

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
                                Intent i = new Intent(getActivity(), DetailTomatActivity.class);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(getActivity(), "Tidak ada data!", Toast.LENGTH_SHORT).show();
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
                if(idxPlant != 0){
                    params.put("plant", pm.getPlantId());
                    Global.getInstance().selectedPlantIdTomat = pm.getPlantId();
                }
                if(matNo.getText().toString().length()>0){
                    params.put("new_stock_code", matNo.getText().toString());
                    Global.getInstance().selectedMaterialTomat = matNo.getText().toString();
                }
                if(oldNo.getText().toString().length()>0){
                    params.put("old_stock_code", oldNo.getText().toString());
                    Global.getInstance().selectedOldMaterialNoTomat = oldNo.getText().toString();
                }
                if(poText.getText().toString().length()>0){
                    params.put("po_text", poText.getText().toString());
                    Global.getInstance().selectedPoTextTomat = poText.getText().toString();
                }
                if(deskripsi.getText().toString().length()>0){
                    params.put("description", deskripsi.getText().toString());
                    Global.getInstance().selectedDescTomat = deskripsi.getText().toString();
                }
                if(deskripsi.getText().toString().length()>0){
                    params.put("description", deskripsi.getText().toString());
                    Global.getInstance().selectedDescTomat = deskripsi.getText().toString();
                }
                if(Global.getInstance().selectedStockAvailabilityTomat.length()>0){
                    params.put("stock_available", Global.getInstance().selectedStockAvailabilityTomat);
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
