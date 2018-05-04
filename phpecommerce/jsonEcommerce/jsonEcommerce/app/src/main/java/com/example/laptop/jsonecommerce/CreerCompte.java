package com.example.laptop.jsonecommerce;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CreerCompte extends AppCompatActivity {

    EditText nom, email, login, password;
    Button creerCompte;
    RequestQueue requestQueue;
    String insertClientUrl = "http://10.23.22.35/projects/ecommerce_restapi/insertClient.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creer_compte);

        nom = (EditText) findViewById(R.id.editTextNom);
        email = (EditText) findViewById(R.id.editTextEmail);
        login = (EditText) findViewById(R.id.editTextUsername);
        password = (EditText) findViewById(R.id.editTextPassword);
        creerCompte = (Button) findViewById(R.id.btnCreerCompte);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        creerCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest request = new StringRequest(Request.Method.POST, insertClientUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("creer compte:  "+error);
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parameters  = new HashMap<String, String>();
                        parameters.put("nom", nom.getText().toString());
                        parameters.put("email", email.getText().toString());
                        parameters.put("login", login.getText().toString());
                        parameters.put("password", password.getText().toString());

                        return parameters;
                    }
                };
                requestQueue.add(request);
                startActivity(new Intent(getApplicationContext(), Accueil.class));
            }

        });

    }
}
