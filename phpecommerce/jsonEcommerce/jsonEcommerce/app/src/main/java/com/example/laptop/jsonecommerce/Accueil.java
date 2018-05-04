package com.example.laptop.jsonecommerce;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Accueil extends AppCompatActivity {

    EditText login, password;
    TextView creerCompte;
    Button btnlogin;

    RequestQueue requestQueue;
    String loginUrl = "http://10.23.22.35/projects/ecommerce_restapi/getClient.php";
    StringRequest stringRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        login = (EditText) findViewById(R.id.editTextLogin);
        password = findViewById(R.id.editTextPassword);
        btnlogin = (Button) findViewById(R.id.btnLogin);
        creerCompte = (TextView) findViewById(R.id.btnSignUp);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stringRequest = new StringRequest(Request.Method.POST, loginUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject client = new JSONObject(response);
                            client.getJSONArray("client").getJSONObject(0).getString("loginclient");
                            int idclient = Integer.parseInt(client.getJSONArray("client").getJSONObject(0).getString("idclient"));
                            Intent intent = new Intent(getApplicationContext(), ListProduct.class);
                            intent.putExtra("idclient", idclient);
                            startActivity(intent);

                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("acceuil:  "+error);
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parameters  = new HashMap<String, String>();
                        parameters.put("login", login.getText().toString());
                        parameters.put("password", password.getText().toString());

                        return parameters;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });

        creerCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CreerCompte.class));
            }
        });

    }
}
