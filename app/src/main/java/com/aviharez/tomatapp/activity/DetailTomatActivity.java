package com.aviharez.tomatapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aviharez.tomatapp.R;
import com.aviharez.tomatapp.models.Global;
import com.aviharez.tomatapp.models.SmartCatModel;
import customfonts.MyTextView_SF_Pro_Display_Medium;
import customfonts.TextViewSFProDisplayRegular;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailTomatActivity extends AppCompatActivity {

    private LinearLayout bt;
    private Dialog dialog, dialogOverlay;
    ListView lvMaterial;
    ImageButton btnNext, btnPrev;
    MyTextView_SF_Pro_Display_Medium pageInfo;
    private CustomAdapter adapter;
    int destPage, prevPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tomat);
        lvMaterial = findViewById(R.id.lvMaterial);
        btnNext = findViewById(R.id.next);
        btnPrev = findViewById(R.id.prev);
        pageInfo = findViewById(R.id.page_info);

        lvMaterial.setDivider(null);
        lvMaterial.setDividerHeight(0);

        adapter = new CustomAdapter(Global.getInstance().getSmartCatModel(), DetailTomatActivity.this);
        lvMaterial.setAdapter(adapter);

        lvMaterial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<SmartCatModel> sc = Global.getInstance().getSmartCatModel();
                SmartCatModel scm = sc.get(i);
                String newStockCode = scm.getContentList().get(Global.getInstance().keysTomat.get("new_stock_code"));
                searchDetailTomat(newStockCode);
            }
        });

        setDialogOverlay();
        pageInfo.setText(Global.getInstance().getCurrentPageTomat()+" dari "+Global.getInstance().getTotalPageTomat());
        if(Global.getInstance().getCurrentPageTomat()==Global.getInstance().getTotalPageTomat()){
            btnNext.setEnabled(false);
            btnPrev.setEnabled(false);
        }
        else if(Global.getInstance().getCurrentPageTomat()==1){
            btnNext.setEnabled(true);
            btnPrev.setEnabled(false);
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String x = "";
                if(Global.getInstance().getCurrentPageTomat()+1 <= Global.getInstance().getTotalPageTomat()){
                    destPage = Global.getInstance().getCurrentPageTomat()+1;
                    searchMaterialOnSmartCat();
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Global.getInstance().getCurrentPageTomat()-1 > 0){
                    destPage = Global.getInstance().getCurrentPageTomat()-1;
                    searchMaterialOnSmartCat();
                }
            }
        });
//        setDialog();

//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.show();
//            }
//        });


    }

    public void setDialogOverlay(){
        dialogOverlay = new Dialog(DetailTomatActivity.this);
        dialogOverlay.setContentView(R.layout.dialog_loading);
        dialogOverlay.setCancelable(false);
    }

    private void showDialog(){
        if(!dialogOverlay.isShowing())
            dialogOverlay.show();
    }

    private void hideDialog(){
        if(dialogOverlay.isShowing())
            dialogOverlay.dismiss();
    }

    public void searchMaterialOnSmartCat(){
        //with API
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
                            pageInfo.setText(Global.getInstance().getCurrentPageTomat()+" dari "+Global.getInstance().getTotalPageTomat());
                            if(Global.getInstance().getCurrentPageTomat()==Global.getInstance().getTotalPageTomat()){
                                btnNext.setEnabled(false);
                                btnPrev.setEnabled(true);
                            }
                            else if(Global.getInstance().getCurrentPageTomat()==1){
                                btnNext.setEnabled(true);
                                btnPrev.setEnabled(false);
                            }
                            else{
                                btnNext.setEnabled(true);
                                btnPrev.setEnabled(true);
                            }

                            JSONArray data = obj.getJSONArray("data");
                            JSONArray columns = obj.getJSONArray("columns");
                            ArrayList<String> columnArr = new ArrayList<>();
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
                                adapter = new CustomAdapter(Global.getInstance().getSmartCatModel(), DetailTomatActivity.this);
                                lvMaterial.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                            else{
                                Toast.makeText(DetailTomatActivity.this, "Tidak ada data!", Toast.LENGTH_SHORT).show();
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
                params.put("page", String.valueOf(destPage));
                if(Global.getInstance().selectedPlantIdTomat.length()>0){
                    params.put("plant", Global.getInstance().selectedPlantIdTomat);
                }
                if(Global.getInstance().selectedMaterialTomat.length()>0){
                    params.put("new_stock_code", Global.getInstance().selectedMaterialTomat);
                }
                if(Global.getInstance().selectedOldMaterialNoTomat.length()>0){
                    params.put("old_stock_code", Global.getInstance().selectedOldMaterialNoTomat);
                }
                if(Global.getInstance().selectedPoTextTomat.length()>0){
                    params.put("po_text", Global.getInstance().selectedPoTextTomat);
                }
                if(Global.getInstance().selectedDescTomat.length()>0){
                    params.put("description", Global.getInstance().selectedDescTomat);
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
        RequestQueue requestQueue = Volley.newRequestQueue(DetailTomatActivity.this);
        requestQueue.add(jsonReq);
    }

    public void searchDetailTomat(String stockCode){
        //with API
        showDialog();
        String url = Global.getInstance().getBaseUrl()+"api/matools-sco.php?code="+stockCode+"&action=view";//3d03d68c25e760eba562a8b1db568504";
        Global.getInstance().handleSSLConn();
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.getJSONObject("tomat") != null){
                                JSONObject objTomat = response.getJSONObject("tomat");
                                Global.getInstance().objDetailTomat = objTomat;
                                setDialog();
                            }
                            else{
                                Toast.makeText(DetailTomatActivity.this, "Tidak ada data detail!", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(DetailTomatActivity.this);
        requestQueue.add(jsonReq);
    }

    private void setDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_detail_tomat);
        ImageButton bt_close = dialog.findViewById(R.id.bt_close);
        Button bt_check_stock = dialog.findViewById(R.id.bt_check_stock);
        ImageView imgAvatar = dialog.findViewById(R.id.img_avatar);
        TextViewSFProDisplayRegular tvMatNo = dialog.findViewById(R.id.tv_mat_no);
        MyTextView_SF_Pro_Display_Medium tvPlant = dialog.findViewById(R.id.tv_plant);
        MyTextView_SF_Pro_Display_Medium tvOldNo = dialog.findViewById(R.id.tv_oldno);
        MyTextView_SF_Pro_Display_Medium tvDesc = dialog.findViewById(R.id.tv_desc);
        MyTextView_SF_Pro_Display_Medium tvUom = dialog.findViewById(R.id.tv_uom);
        MyTextView_SF_Pro_Display_Medium tvMatType = dialog.findViewById(R.id.tv_mat_type);
        MyTextView_SF_Pro_Display_Medium tvPoText = dialog.findViewById(R.id.tv_po_text);

        //set content detail
        try {
            JSONObject objRes = Global.getInstance().objDetailTomat;
            tvPlant.setText(objRes.getString("Plant"));
            tvMatNo.setText(objRes.getString("Material"));
            tvOldNo.setText(objRes.getString("OldMaterialNo"));
            tvUom.setText(objRes.getString("BUn"));
            tvMatType.setText(objRes.getString("Mat_Type"));
            tvDesc.setText(objRes.getString("Deskripsi"));
            tvPoText.setText(objRes.getString("POText"));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        bt_check_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailTomatActivity.this, CheckStokActivity.class);
                startActivity(i);
            }
        });
        dialog.show();
    }

    public class CustomAdapter extends ArrayAdapter<SmartCatModel> {
        private ArrayList<SmartCatModel> dataSet;
        Context mContext;

        private class ViewHolder {
            ImageView imgAvatar;
            LinearLayout layoutContent;
            TextViewSFProDisplayRegular tvMatNo;
            MyTextView_SF_Pro_Display_Medium tvPlant;
            MyTextView_SF_Pro_Display_Medium tvOldNo;
            MyTextView_SF_Pro_Display_Medium tvDesc;
        }

        public CustomAdapter(ArrayList<SmartCatModel> data, Context context) {
            super(context, R.layout.row_tomat, data);
            this.dataSet = data;
            this.mContext=context;
        }

        public void setListData(ArrayList<SmartCatModel> data){
            this.dataSet = data;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SmartCatModel newsModel = getItem(position);
            ViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.row_tomat, parent, false);
                viewHolder.layoutContent = (LinearLayout)convertView.findViewById(R.id.layout_content);
                viewHolder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
                viewHolder.tvMatNo = convertView.findViewById(R.id.tv_mat_no);
                viewHolder.tvPlant = convertView.findViewById(R.id.tv_plant);
                viewHolder.tvOldNo = convertView.findViewById(R.id.tv_oldno);
                viewHolder.tvDesc = convertView.findViewById(R.id.tv_desc);

                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvMatNo.setText(newsModel.getContentList().get(Global.getInstance().keysTomat.get("new_stock_code")));
            viewHolder.tvPlant.setText(newsModel.getContentList().get(Global.getInstance().keysTomat.get("plant")));
            viewHolder.tvOldNo.setText(newsModel.getContentList().get(Global.getInstance().keysTomat.get("old_stock_code")));
            viewHolder.tvDesc.setText(newsModel.getContentList().get(Global.getInstance().keysTomat.get("description")));

            return convertView;
        }
    }
}
