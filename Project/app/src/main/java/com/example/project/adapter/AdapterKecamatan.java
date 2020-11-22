package com.example.project.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.project.R;
import com.example.project.model.DataModelKecamatan;

import java.util.List;

/**
 * Created by KUNCORO on 09/08/2017.
 */

public class AdapterKecamatan extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataModelKecamatan> item;

    public AdapterKecamatan(Activity activity, List<DataModelKecamatan> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int location) {
        return item.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.listkecamatan, null);

        TextView txtid = (TextView) convertView.findViewById(R.id.txtid_kecamatan);
        TextView txt_namakabupaten = (TextView) convertView.findViewById(R.id.txt_kabupaten);
        TextView txt_namakecamatan = (TextView) convertView.findViewById(R.id.txt_kecamatan);
        TextView txt_jumlahikm = (TextView) convertView.findViewById(R.id.txt_jumlahikm);


        txtid.setText(item.get(position).getId_kecamatan());
        txt_namakabupaten.setText(item.get(position).getNama_kabupaten());
        txt_namakecamatan.setText(item.get(position).getNama_kecamatan());
        txt_jumlahikm.setText(item.get(position).getJumlahikm());


        return convertView;
    }
}