package com.example.project.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.DetailBengkel;
import com.example.project.R;
import com.example.project.URL_SERVER;
import com.example.project.model.DataModelBengkel;
import com.example.project.model.ModelGambar;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by Balpir on 20/11/2020.
 */

public class AdapterBengkel extends RecyclerView.Adapter<AdapterBengkel.MyViewHolder> {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataModelBengkel> item;
    public static final String Cgambar = URL_SERVER.GambarSampul;

    public AdapterBengkel(Activity activity, List<DataModelBengkel> item) {
        this.activity = activity;
        this.item = item;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new AdapterBengkel.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nama.setText(item.get(position).getNama_bengkel());
        holder.hari.setText("Hari : "+item.get(position).getHari_kerja());
        holder.jam.setText("Jam : "+item.get(position).getJam_buka()+" - "+item.get(position).getJam_tutup());
        holder.alamat.setText(item.get(position).getAlamat_bengkel());
        if(Double.valueOf(item.get(position).getJarak()) >= 1000){
            Double jarr = Double.valueOf(item.get(position).getJarak())/1000;
            holder.jarak.setText("Jarak : "+ String.format("%.2f", jarr) + " KM");
        }
        else{
            holder.jarak.setText(String.valueOf(item.get(position).getJarak()) +" meter");
        }
        Picasso.get()
                .load(Cgambar+item.get(position).getGambar_sampul())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.img)
                .into(holder.imageView);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kotak = new Intent(activity, DetailBengkel.class);
                kotak.putExtra("id_bengkel",item.get(position).getId_bengkel());
                activity.startActivity(kotak);
                Toasty.info(activity,item.get(position).getId_bengkel(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.btn_hubungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telp = item.get(position).getNo_hp();
                if (telp != null){
                    activity.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telp)));
                    activity.finish();
                }else{
                    Toast.makeText(activity, "No Telp Tidak ada", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.kunjungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lng = item.get(position).getLng();
                String lat = item.get(position).getLat();
                if (lng.equals(null)){
                    Toast.makeText(activity, "Lokasi Tidak Lengkap", Toast.LENGTH_SHORT).show();
                } else if(lat.equals(null)){
                    Toast.makeText(activity, "Lokasi Tidak Lengkap", Toast.LENGTH_SHORT).show();
                }else if (lat.equals(null)&&lng.equals(null)){
                    Toast.makeText(activity, "Lokasi Tidak Ada", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(activity, "Lokasi "+ lng+","+lat, Toast.LENGTH_SHORT).show();
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lng+","+lat);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    activity.startActivity(mapIntent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama, hari,jam,alamat,jarak;
        public ImageView imageView;
        public RelativeLayout relativeLayout;
        public Button btn_hubungi, kunjungi;

        public MyViewHolder(View view) {
            super(view);

            relativeLayout = view.findViewById(R.id.relatip);
            imageView = view.findViewById(R.id.gambar);
            nama = view.findViewById(R.id.namaBengkel);
            hari = view.findViewById(R.id.hari);
            jam = view.findViewById(R.id.jam);
            alamat = view.findViewById(R.id.alamat);
            jarak = view.findViewById(R.id.jarak);
            btn_hubungi = view.findViewById(R.id.btn1);
            kunjungi = view.findViewById(R.id.btn2);
        }
    }
}