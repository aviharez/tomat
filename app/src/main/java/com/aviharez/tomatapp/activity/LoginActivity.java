package com.aviharez.tomatapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.aviharez.tomatapp.R;

public class LoginActivity extends AppCompatActivity {

    private Button bt_login;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);

        bt_login = findViewById(R.id.bt_login);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        hideDialog();
                    }
                }, 2500);
            }
        });

    }

    private void showDialog(){
        if(!dialog.isShowing())
            dialog.show();
    }

    private void hideDialog(){
        if(dialog.isShowing())
            dialog.dismiss();
    }
}
