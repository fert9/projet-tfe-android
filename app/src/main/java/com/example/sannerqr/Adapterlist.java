package com.example.sannerqr;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Adapterlist extends ArrayAdapter<String> {

        Activity context;
        String[] items;
        Adapterlist (Activity context, String[] items){
            super(context, R.layout.recycler,items);
            this.context=context;
            this.items=items;
        }
        @Override
        public View getView (int position, View converterView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View ligne = inflater.inflate(R.layout.recycler, null);
            TextView label = (TextView) ligne.findViewById(R.id.list);
            label.setText(items[position]);
            return ligne;
        };


}
