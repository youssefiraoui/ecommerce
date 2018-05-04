package com.example.laptop.jsonecommerce;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListProduct extends AppCompatActivity {

    String catalogueUrl = "http://10.23.22.35/projects/ecommerce_restapi/getCatalogue.php";
    String specificProdUrl = "http://10.23.22.35/projects/ecommerce_restapi/getSpecificProducts.php";
    String commandeUrl = "http://10.23.22.35/projects/ecommerce_restapi/commande.php";
    String ligneCommandeUrl = "http://10.23.22.35/projects/ecommerce_restapi/ligneCommande.php";

    String idClient = "9";

    RequestQueue requestQueue;
    Spinner spinner;
    Button commander;

    final HashMap<String,String> hashMap = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_product);

        final Context context = this;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        spinner = (Spinner) findViewById(R.id.spinner);
        commander = (Button) findViewById(R.id.commander);

        final ListView listView = (ListView) findViewById(R.id.listView);

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
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ListProduct.this, android.R.layout.simple_spinner_item, list);
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

                final String selectedItem = adapterView.getItemAtPosition(i).toString();
                StringRequest request = new StringRequest(Request.Method.POST, specificProdUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray jsonArray  = jsonObject.getJSONArray("specificprod");
                            ArrayList<String> listOfProducs = new ArrayList<String>();
                            ArrayList<String> priceOfProducts = new ArrayList<String>();
                            ArrayList<String> idproducts = new ArrayList<String>();
                            ArrayList<String> imagesLink = new ArrayList<String>();
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                listOfProducs.add(jsonObject2.getString("nomproduit"));
                                priceOfProducts.add(jsonObject2.getString("prix"));
                                idproducts.add(jsonObject2.getString("idproduit"));
                                imagesLink.add(jsonObject2.getString("image"));
                            }
                            CustomAdapter customAdapter = new CustomAdapter(listOfProducs, priceOfProducts, idproducts, imagesLink, context);
                            listView.setAdapter(customAdapter);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idProduct = view.findViewById(R.id.textView_idproduit);
                alertFormElements(idProduct);
            }
        });



        commander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String dateCommande = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date());

                StringRequest request2 = new StringRequest(Request.Method.POST, commandeUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parameters  = new HashMap<String, String>();
                        parameters.put("idClient", idClient );
                        parameters.put("dateCommande", dateCommande);

                        return parameters;
                    }
                };
                requestQueue.add(request2);

                // ligne de commande


                for(Map.Entry<String,String> entry : hashMap.entrySet()){
                    final String idproduit = (String) entry.getKey() ;
                    final String Qtecomm = (String) entry.getValue();
                    StringRequest request3 = new StringRequest(Request.Method.POST, ligneCommandeUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> parameters  = new HashMap<String, String>();
                            parameters.put("dateCommande", dateCommande);
                            parameters.put("idproduit", idproduit);
                            parameters.put("Qtecomm", Qtecomm);

                            return parameters;
                        }
                    };
                    requestQueue.add(request3);
                    System.out.println(entry.getKey()+"********************"+entry.getValue());
                }
            }
        });



    }

    public void alertFormElements(final TextView idProduct) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.choose_quantity,
                null, false);

        final EditText quantity = (EditText) formElementsView.findViewById(R.id.quantityEditText);
        // the alert dialog
        new AlertDialog.Builder(ListProduct.this).setView(formElementsView)
                .setTitle("choose Quantity")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {

                        hashMap.put(idProduct.getText().toString(), quantity.getText().toString());
                        dialog.cancel();
                    }

                }).show();
    }




}
