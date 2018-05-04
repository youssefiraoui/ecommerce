package com.example.laptop.jsonecommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter{

    ArrayList<String> productsName;
    ArrayList<String> productsPrice;
    ArrayList<String> idproducts;
    ArrayList<String> imagesLink;
    Context context;
    LayoutInflater layoutInflater;

    public CustomAdapter(ArrayList<String> productsName, ArrayList<String> productsPrice, ArrayList<String> idproducts, ArrayList<String> imagesLink,Context context) {
        this.productsName = productsName;
        this.productsPrice = productsPrice;
        this.idproducts = idproducts;
        this.imagesLink = imagesLink;
        this.context = context;
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.productsName.size();
    }

    @Override
    public Object getItem(int i) {
        return this.productsName.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.custom_layout,null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        TextView textViewName = (TextView) view.findViewById(R.id.textView_name);
        TextView textView_idproduit = (TextView) view.findViewById(R.id.textView_idproduit);
        TextView textViewPrix = (TextView) view.findViewById(R.id.textView_prix);
        textViewName.setText((CharSequence) this.productsName.get(i));
        textViewPrix.setText((CharSequence) this.productsPrice.get(i) + " DH");
        textView_idproduit.setText((CharSequence) this.idproducts.get(i));

        int id = this.context.getResources().getIdentifier(this.imagesLink.get(i), "drawable", this.context.getPackageName());
        imageView.setImageResource(id);

        return view;
    }
}
