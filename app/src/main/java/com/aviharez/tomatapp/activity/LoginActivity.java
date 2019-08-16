package com.aviharez.tomatapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aviharez.tomatapp.R;
import com.aviharez.tomatapp.models.Global;
import customfonts.EditText__SF_Pro_Display_Regular;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button bt_login;
    private Dialog dialog;
    private EditText__SF_Pro_Display_Regular username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText__SF_Pro_Display_Regular)findViewById(R.id.et_username);
        password = (EditText__SF_Pro_Display_Regular)findViewById(R.id.et_password);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);

        bt_login = findViewById(R.id.bt_login);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
//                showDialog();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(i);
//                        hideDialog();
//                    }
//                }, 2500);
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

    public void login(){
        //with API
        if((username.getText().toString().length()==0) || (password.getText().toString().length()==0)){
            Toast.makeText(LoginActivity.this, "Silahkan isi username dan password", Toast.LENGTH_LONG).show();
            return;
        }
        showDialog();
        String url = Global.getInstance().getBaseUrl()+"api/login.php";
        Global.getInstance().handleSSLConn();
        StringRequest jsonReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            hideDialog();
                            JSONObject res = new JSONObject(response);
                            boolean success = res.getBoolean("success");
                            if(success){
                                JSONObject usrObj = res.getJSONObject("user");
                                Global.getInstance().setCsrf(res.getString("csrf"));
                                Global.getInstance().setUsername(usrObj.getString("username"));
                                Global.getInstance().setEmail(usrObj.getString("email"));
                                Global.getInstance().setName(usrObj.getString("name"));
                                Global.getInstance().setProfilePict(usrObj.getString("picture"));
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "User Id Atau password Salah", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Periksa Koneksi.", Toast.LENGTH_LONG).show();
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
                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());

                return params;
            }
        };
        jsonReq.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(jsonReq);
    }
}
