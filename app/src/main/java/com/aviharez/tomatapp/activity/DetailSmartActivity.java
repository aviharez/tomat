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
import androidx.core.content.ContextCompat;
import com.android.volley.*;
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

public class DetailSmartActivity extends AppCompatActivity {
    ListView lvMaterial;
    ImageButton btnNext, btnPrev;
    MyTextView_SF_Pro_Display_Medium pageInfo;
    private CustomAdapter adapter;
    private Dialog dialog, dialogOverlay;
    int destPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_smart);
        lvMaterial = (ListView)findViewById(R.id.lvMaterial);
        btnNext = findViewById(R.id.next);
        btnPrev = findViewById(R.id.prev);
        pageInfo = findViewById(R.id.page_info);

        lvMaterial.setDivider(null);
        lvMaterial.setDividerHeight(0);
        setDialogOverlay();
        adapter = new CustomAdapter(Global.getInstance().getSmartCatModel(), DetailSmartActivity.this);
        lvMaterial.setAdapter(adapter);

        lvMaterial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), SCOverviewActivity.class);
                startActivity(intent);
            }
        });

        pageInfo.setText(Global.getInstance().getCurrentPageSmartCat()+" dari "+Global.getInstance().getTotalPageSmartCat());
        if(Global.getInstance().getCurrentPageSmartCat()==Global.getInstance().getTotalPageSmartCat()){
            btnNext.setEnabled(false);
            btnPrev.setEnabled(false);
        }
        else if(Global.getInstance().getCurrentPageSmartCat()==1){
            btnNext.setEnabled(true);
            btnPrev.setEnabled(false);
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String x = "";
                if(Global.getInstance().getCurrentPageSmartCat()+1 <= Global.getInstance().getTotalPageSmartCat()){
                    destPage = Global.getInstance().getCurrentPageSmartCat()+1;
                    searchMaterialOnSmartCat();
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Global.getInstance().getCurrentPageSmartCat()-1 > 0){
                    destPage = Global.getInstance().getCurrentPageSmartCat()-1;
                    searchMaterialOnSmartCat();
                }
            }
        });
    }

    public void setDialogOverlay(){
        dialogOverlay = new Dialog(DetailSmartActivity.this);
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
                            pageInfo.setText(Global.getInstance().getCurrentPageSmartCat()+" dari "+Global.getInstance().getTotalPageSmartCat());
                            if(Global.getInstance().getCurrentPageSmartCat()==Global.getInstance().getTotalPageSmartCat()){
                                btnNext.setEnabled(false);
                                btnPrev.setEnabled(true);
                            }
                            else if(Global.getInstance().getCurrentPageSmartCat()==1){
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
                                adapter = new CustomAdapter(Global.getInstance().getSmartCatModel(), DetailSmartActivity.this);
                                lvMaterial.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                            else{
                                Toast.makeText(DetailSmartActivity.this, "Tidak ada data!", Toast.LENGTH_SHORT).show();
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
                if(Global.getInstance().selectedPlantIdSmartCat.length()>1){
                    params.put("plant", Global.getInstance().selectedPlantIdSmartCat);
                }
                if(Global.getInstance().selectedStockCodeSmartCat.length()>0){
                    params.put("new_stock_code", Global.getInstance().selectedStockCodeSmartCat);
                }
                if(Global.getInstance().selectedOldStockCodeSmartCat.length()>0){
                    params.put("old_stock_code", Global.getInstance().selectedOldStockCodeSmartCat);
                }
                if(Global.getInstance().selectedIncSmartCat.length()>1){
                    params.put("inc", Global.getInstance().selectedIncSmartCat);
                }
                if(Global.getInstance().selectedGroupClassSmartCat.length()>1){
                    params.put("group_class", Global.getInstance().selectedGroupClassSmartCat);
                }
                if(Global.getInstance().selectedManufacturerSmartCat.length()>1){
                    params.put("manufacturer", Global.getInstance().selectedManufacturerSmartCat);
                }
                if(Global.getInstance().selectedEquipmentSmartCat.length()>1){
                    params.put("equipment", Global.getInstance().selectedEquipmentSmartCat);
                }
                if(Global.getInstance().selectedPartNumSmartCat.length()>0){
                    params.put("part_number", Global.getInstance().selectedPartNumSmartCat);
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
        RequestQueue requestQueue = Volley.newRequestQueue(DetailSmartActivity.this);
        requestQueue.add(jsonReq);
    }

    public class CustomAdapter extends ArrayAdapter<SmartCatModel> {
        private ArrayList<SmartCatModel> dataSet;
        Context mContext;

        private class ViewHolder {
            ImageView imgStatusMark;
            ImageView imgAvatar;
            TextViewSFProDisplayRegular tvMatNo;
            MyTextView_SF_Pro_Display_Medium tvPlant;
            MyTextView_SF_Pro_Display_Medium tvStatus;
            MyTextView_SF_Pro_Display_Medium tvOldNo;
            MyTextView_SF_Pro_Display_Medium tvDesc;
            LinearLayout layoutContent;
        }

        public void addContentViewSmartCat(LinearLayout contentWrapper, String label, String val){
            LinearLayout v = new LinearLayout(getContext());
            v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            v.setOrientation(LinearLayout.VERTICAL);
            v.setPadding(10, 10, 10, 10);

            LinearLayout ll = new LinearLayout(getContext());
            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ll.setOrientation(LinearLayout.HORIZONTAL);

            TextView tvLabel = new TextView(getContext());
            tvLabel.setTextColor(Color.BLACK);
            tvLabel.setLayoutParams(new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.WRAP_CONTENT));
            tvLabel.setTypeface(tvLabel.getTypeface(), Typeface.BOLD);
            tvLabel.setText(label);
            ll.addView(tvLabel);

            TextView tvSeparator = new TextView(getContext());
            tvSeparator.setTextColor(Color.BLACK);
            tvSeparator.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tvSeparator.setTypeface(tvLabel.getTypeface(), Typeface.BOLD);
            tvSeparator.setText(":");
            ll.addView(tvSeparator);

            TextView tvContent = new TextView(getContext());
            tvContent.setTextColor(Color.BLACK);
            tvContent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tvContent.setText(val);
            String tagLabel = label.replaceAll(" ","_");
            tvContent.setTag(tagLabel);
            ll.addView(tvContent);

            //v.addView(ll);
            contentWrapper.addView(ll);
        }

        public CustomAdapter(ArrayList<SmartCatModel> data, Context context) {
            super(context, R.layout.row_smartcat, data);
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
                convertView = inflater.inflate(R.layout.row_smartcat, parent, false);
                viewHolder.layoutContent = (LinearLayout)convertView.findViewById(R.id.layout_content);
                viewHolder.imgAvatar = convertView.findViewById(R.id.img_avatar);
                viewHolder.imgStatusMark = convertView.findViewById(R.id.img_status_mark);
                viewHolder.tvMatNo = convertView.findViewById(R.id.tv_mat_no);
                viewHolder.tvPlant = convertView.findViewById(R.id.tv_plant);
                viewHolder.tvStatus = convertView.findViewById(R.id.tv_status);
                viewHolder.tvOldNo = convertView.findViewById(R.id.tv_oldno);
                viewHolder.tvDesc = convertView.findViewById(R.id.tv_desc);

                if(position % 2 == 0){
                    viewHolder.imgStatusMark.setColorFilter(ContextCompat.getColor(DetailSmartActivity.this, R.color.blue));
                }
                else{
                    viewHolder.imgStatusMark.setColorFilter(ContextCompat.getColor(DetailSmartActivity.this, R.color.green_light));
                }

                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvMatNo.setText(newsModel.getContentList().get(Global.getInstance().keysSmartCat.get("new_stock_code")));
            viewHolder.tvPlant.setText(newsModel.getContentList().get(Global.getInstance().keysSmartCat.get("plant")));
            viewHolder.tvOldNo.setText(newsModel.getContentList().get(Global.getInstance().keysSmartCat.get("old_stock_code")));
            viewHolder.tvStatus.setText(newsModel.getContentList().get(Global.getInstance().keysSmartCat.get("status")));
            viewHolder.tvDesc.setText(newsModel.getContentList().get(Global.getInstance().keysSmartCat.get("description")));

            return convertView;
        }
    }
}
