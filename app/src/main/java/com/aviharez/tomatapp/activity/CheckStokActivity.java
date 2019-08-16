package com.aviharez.tomatapp.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.aviharez.tomatapp.R;
import com.aviharez.tomatapp.models.Global;
import com.aviharez.tomatapp.models.StockModel;
import customfonts.MyTextView_SF_Pro_Display_Medium;
import customfonts.TextViewSFProDisplayRegular;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckStokActivity extends AppCompatActivity {
    TextViewSFProDisplayRegular tvMatNo;
    ImageView imgAvatar;
    ImageButton bt_home;
    MyTextView_SF_Pro_Display_Medium tvPlant, tvUom, tvLastUpdate, tvDesc;
    ListView lvStock;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_stok);
        imgAvatar = findViewById(R.id.img_avatar);
        tvMatNo = findViewById(R.id.tv_mat_no);
        tvPlant = findViewById(R.id.tv_plant);
        tvUom = findViewById(R.id.tv_uom);
        tvLastUpdate = findViewById(R.id.tv_last_update);
        tvDesc = findViewById(R.id.tv_desc);
        lvStock = findViewById(R.id.lvStock);
        bt_home = findViewById(R.id.bt_home);

        setContentStockOverview();

        bt_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CheckStokActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }

    public void setContentStockOverview(){
        try{
            JSONObject objRes = Global.getInstance().objDetailTomat;
            tvPlant.setText(objRes.getString("Plant"));
            tvMatNo.setText(objRes.getString("Material"));
            tvUom.setText(objRes.getString("BUn"));
            tvLastUpdate.setText(objRes.getString("LastUpdate"));
            tvDesc.setText(objRes.getString("Deskripsi"));
            JSONArray details = objRes.getJSONArray("details");
            ArrayList<StockModel> stockModels = new ArrayList<>();
            for(int i=0; i<details.length(); i++){
                String sLoc = ((JSONObject)details.get(i)).getString("SLoc");
                int uU = ((JSONObject)details.get(i)).getInt("Unrestricted");
                StockModel sm = new StockModel(sLoc, uU);
                stockModels.add(sm);
            }
            lvStock.setDivider(null);
            lvStock.setDividerHeight(0);
            adapter = new CustomAdapter(stockModels, CheckStokActivity.this);
            lvStock.setAdapter(adapter);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public class CustomAdapter extends ArrayAdapter<StockModel> {
        private ArrayList<StockModel> dataSet;
        Context mContext;

        private class ViewHolder {
            TextView sloc;
            TextView qty;
            ImageView imgMarker;
        }

        public CustomAdapter(ArrayList<StockModel> data, Context context) {
            super(context, R.layout.row_stock, data);
            this.dataSet = data;
            this.mContext=context;
        }

        public void setListData(ArrayList<StockModel> data){
            this.dataSet = data;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            StockModel newsModel = getItem(position);
            ViewHolder viewHolder;
            try {
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.row_stock, parent, false);
                    viewHolder.sloc = (TextView) convertView.findViewById(R.id.sloc);
                    viewHolder.qty = (TextView) convertView.findViewById(R.id.qty);
                    viewHolder.imgMarker = (ImageView)convertView.findViewById(R.id.img_marker);
                    convertView.setTag(viewHolder);

                    if(position % 2 == 0){
                        viewHolder.imgMarker.setColorFilter(ContextCompat.getColor(CheckStokActivity.this, R.color.blue));
                    }
                    else{
                        viewHolder.imgMarker.setColorFilter(ContextCompat.getColor(CheckStokActivity.this, R.color.green_light));
                    }

                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.sloc.setText(newsModel.getsLoc());
                viewHolder.qty.setText(Integer.toString(newsModel.getUnRestricted()));
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
        }
    }
}
