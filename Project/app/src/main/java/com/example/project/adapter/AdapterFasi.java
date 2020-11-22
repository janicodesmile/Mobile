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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.AboutUs;
import com.example.project.DetailBengkel;
import com.example.project.ImageFull;
import com.example.project.R;
import com.example.project.URL_SERVER;
import com.example.project.model.ModelFasilitas;
import com.example.project.model.ModelGambar;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterFasi extends RecyclerView.Adapter<AdapterFasi.MyViewHolder> {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelFasilitas> item;
    public static final String Cgambar = URL_SERVER.icon;

    public AdapterFasi(Activity activity, List<ModelFasilitas> item) {
        this.activity = activity;
        this.item = item;
    }


    public class MyViewHolder  extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView namafas;

        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imgfas);
            namafas = view.findViewById(R.id.fasi);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fasilitas, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.namafas.setText(item.get(position).getNama());
        Picasso.get()
                .load(Cgambar+item.get(position).getIcon())
                .placeholder(R.drawable.img)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(activity, ImageFull.class);
                intent.putExtra("image_url",Cgambar+item.get(position).getIcon());
                activity.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return item.size();
    }
}
