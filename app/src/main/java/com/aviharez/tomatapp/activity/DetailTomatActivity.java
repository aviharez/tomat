package com.aviharez.tomatapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.aviharez.tomatapp.R;

public class DetailTomatActivity extends AppCompatActivity {

    private LinearLayout bt;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tomat);

        bt = findViewById(R.id.linear);
        setDialog();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });


    }

    private void setDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_detail_tomat);
        ImageButton bt_close = dialog.findViewById(R.id.bt_close);
        Button bt_check_stock = dialog.findViewById(R.id.bt_check_stock);

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

    }
}
