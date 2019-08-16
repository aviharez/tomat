package com.aviharez.tomatapp.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aviharez.tomatapp.R;
import com.aviharez.tomatapp.models.Global;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import customfonts.Button_Roboto_Medium;
import customfonts.EditText__SF_Pro_Display_Regular;
import customfonts.MyTextView_SF_Pro_Display_Bold;
import customfonts.MyTextView_SF_Pro_Display_Medium;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private Dialog dialogOverlay;
    private LinearLayout bt_change_pass;
    MyTextView_SF_Pro_Display_Bold txt_name;
    MyTextView_SF_Pro_Display_Medium txt_username;
    RoundedImageView img_avatar;
    EditText__SF_Pro_Display_Regular current_pass, new_pass, confirm_new_pass;
    //private Dialog dialog;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        //dialog = new Dialog(getActivity());
        //dialog.setContentView(R.layout.dialog_loading);
        img_avatar = v.findViewById(R.id.img_avatar);
        txt_name = v.findViewById(R.id.txt_name);
        txt_username = v.findViewById(R.id.txt_username);
        txt_name.setText(Global.getInstance().getName());
        txt_username.setText(Global.getInstance().getUsername());
        Picasso.get().load(Global.getInstance().getProfilePict()).placeholder(R.drawable.default_avatar).into(img_avatar);
        setupDialog();
        bt_change_pass = v.findViewById(R.id.bt_change_pass);
        bt_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChangePass();
            }
        });

        return v;
    }

    public void setupDialog(){
        dialogOverlay = new Dialog(getContext());
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

    private void showDialogChangePass(){

        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(true);

        //View view  = getActivity().getLayoutInflater().inflate(R.layout.dialog_loading, null);
        dialog.setContentView(R.layout.dialog_change_pass);
        ImageButton bt_close = dialog.findViewById(R.id.bt_close);
        current_pass = dialog.findViewById(R.id.current_pass);
        new_pass = dialog.findViewById(R.id.new_pass);
        confirm_new_pass = dialog.findViewById(R.id.confirm_new_pass);
        Button_Roboto_Medium btn_update = dialog.findViewById(R.id.bt_update);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void changePassword(){
        //with API
        if ((current_pass.getText().toString().length() == 0) && (new_pass.getText().toString().length() == 0) && (confirm_new_pass.getText().toString().length() == 0)){
            Toast.makeText(getContext(), "Semua field harus diisi.", Toast.LENGTH_LONG).show();
            return;
        }
        else if(!new_pass.getText().toString().equals(confirm_new_pass.getText().toString())){
            Toast.makeText(getContext(), "New Password dan Confirm New Password tidak sama", Toast.LENGTH_LONG).show();
            return;
        }
        showDialog();
        String url = Global.getInstance().getBaseUrl()+"api/password.php?action=save";
        Global.getInstance().handleSSLConn();
        StringRequest jsonReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            String res = response;
                            JSONObject obj = new JSONObject(res);
                            if(obj.has("success")){
                                Toast.makeText(getContext(), "Password berhasil diubah.", Toast.LENGTH_SHORT).show();
                            }
                            else if(obj.has("error")){
                                JSONObject msg = obj.getJSONObject("error");
                                Toast.makeText(getContext(), msg.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("oldpassword", current_pass.getText().toString());
                params.put("password", new_pass.getText().toString());
                params.put("confirmpassword", confirm_new_pass.getText().toString());
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
