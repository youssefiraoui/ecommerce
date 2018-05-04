package com.example.laptop.jsonecommerce;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class CatalogueProduit extends AppCompatActivity {

    String catalogueUrl = "http://192.168.1.7/projects/ecommerce_restapi/getCatalogue.php";
    String catproUrl = "http://192.168.1.7/projects/ecommerce_restapi/getAllcatprod.php";
    String specificProdUrl = "http://192.168.1.7/projects/ecommerce_restapi/getSpecificProducts.php";
    String ajoutCommandeUrl = "";
    RequestQueue requestQueue;
    Spinner spinner;
    TableLayout tableLayout;
    Button confirmer;
    HashMap hashtable;
    String espace = "            ";
    String nomlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalogue_produit);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        spinner = (Spinner) findViewById(R.id.spinner);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        confirmer = (Button) findViewById(R.id.confirmer);
        hashtable = new HashMap<>();

        //Intent intent = getIntent();
        //final String loginClient = intent.getExtras().getString("loginClient");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                catalogueUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //System.out.println(response.toString());
                try {
                    JSONArray allCatalogues = response.getJSONArray("catalogue");
                    List<String> list = new ArrayList<String>();
                    for (int i = 0; i < allCatalogues.length(); i++) {
                        JSONObject catalogue = allCatalogues.getJSONObject(i);
                        list.add(catalogue.getString("nomcatalogue"));
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CatalogueProduit.this, android.R.layout.simple_spinner_item, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int childCount = tableLayout.getChildCount();

                // Remove all rows except the first one
                if (childCount > 1) {
                    tableLayout.removeViews(0, childCount);
                }

                final String selectedItem = adapterView.getItemAtPosition(i).toString();
                StringRequest request = new StringRequest(Request.Method.POST, specificProdUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray jsonArray  = jsonObject.getJSONArray("specificprod");
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                TableRow row = new TableRow(CatalogueProduit.this);
                                TextView textView1 = new TextView(CatalogueProduit.this);
                                textView1.setText(jsonObject2.getString("nomproduit")+espace);
                                row.addView(textView1);
                                TextView textView2 = new TextView(CatalogueProduit.this);
                                textView2.setText(jsonObject2.getString("prix")+"MAD"+espace);
                                row.addView(textView2);
                                EditText editText = new EditText(CatalogueProduit.this);
                                int editTextID = Integer.parseInt(jsonObject2.getString("idproduit"));
                                editText.setId(editTextID);
                                row.addView(editText);
                                tableLayout.addView(row);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parameters  = new HashMap<String, String>();
                        parameters.put("selectedItem", selectedItem);

                        return parameters;
                    }
                };
                requestQueue.add(request);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int k = 0; k < tableLayout.getChildCount(); k++) {
                    TableRow tableRow = (TableRow) tableLayout.getChildAt(k);
                    EditText quantitee = (EditText) tableRow.getChildAt(2);
                    final TextView nomProduit = (TextView) tableRow.getChildAt(0);
                    if(quantitee instanceof EditText) {
                        if(quantitee.getText().toString().trim().length() != 0) {
                            String productName = nomProduit.getText().toString();
                            String productQuantity = quantitee.getText().toString();

                            System.out.println(productName+" : "+productQuantity);

                        }
                    }
                }

            }
        });

    }
}
